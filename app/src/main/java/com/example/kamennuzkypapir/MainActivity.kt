package com.example.kamennuzkypapir

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import kotlin.random.Random
import java.util.Locale
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import android.content.Intent

class MainActivity : AppCompatActivity() {
    private lateinit var resultTextView: TextView
    private lateinit var computerLabel: TextView
    private lateinit var playerLabel: TextView
    private lateinit var scoreTextView: TextView
    private lateinit var rockButton: ImageButton
    private lateinit var paperButton: ImageButton
    private lateinit var scissorsButton: ImageButton
    private lateinit var computerIcon: ImageView
    private lateinit var playerIcon: ImageView
    private lateinit var choicesContainer: View
    private lateinit var repeatButton: ImageButton

    // Scores for the computer (phone) and player. These persist until the app is restarted.
    private var computerScore: Int = 0
    private var playerScore: Int = 0

    // Internal constants representing the choices. These are used for logic and are independent of locale.
    private val ROCK = "rock"
    private val PAPER = "paper"
    private val SCISSORS = "scissors"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Restore scores if available (e.g. after configuration change)
        if (savedInstanceState != null) {
            computerScore = savedInstanceState.getInt("computerScore", 0)
            playerScore = savedInstanceState.getInt("playerScore", 0)
        }

        resultTextView = findViewById(R.id.result_text)
        // Scoreboard text view
        scoreTextView = findViewById(R.id.score_text)
        // Initialize labels and icons
        computerLabel = findViewById(R.id.computer_label)
        playerLabel = findViewById(R.id.player_label)
        computerIcon = findViewById(R.id.computer_icon)
        playerIcon = findViewById(R.id.player_icon)
        // Initialize choice container and buttons
        choicesContainer = findViewById(R.id.choices_container)
        rockButton = findViewById(R.id.rock_button)
        paperButton = findViewById(R.id.paper_button)
        scissorsButton = findViewById(R.id.scissors_button)
        // Repeat button
        repeatButton = findViewById(R.id.repeat_button)

        // Set click listeners for player's choice
        rockButton.setOnClickListener { playGame(ROCK) }
        paperButton.setOnClickListener { playGame(PAPER) }
        scissorsButton.setOnClickListener { playGame(SCISSORS) }

        // Set click listener for repeat button
        repeatButton.setOnClickListener { resetGame() }

        // Initialize scoreboard text
        updateScoreText()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Save scores across configuration changes (e.g. language change)
        outState.putInt("computerScore", computerScore)
        outState.putInt("playerScore", playerScore)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the new hamburger menu
        menuInflater.inflate(R.menu.menu_game, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_hamburger -> {
                showHamburgerMenu()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Displays a hamburger menu offering navigation back to the mode selection
     * screen, a language change option and an about page. Selecting an
     * option performs the corresponding action.
     */
    private fun showHamburgerMenu() {
        val options = arrayOf(
            getString(R.string.menu_select_mode),
            getString(R.string.menu_change_language),
            getString(R.string.menu_about)
        )
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.menu))
            .setItems(options) { _, which ->
                when (which) {
                    0 -> {
                        // Navigate back to the mode selection start screen
                        val intent = Intent(this, StartActivity::class.java)
                        // Clear the back stack so that pressing back from StartActivity
                        // does not return here
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)
                    }
                    1 -> showLanguageDialog()
                    2 -> startActivity(Intent(this, AboutActivity::class.java))
                }
            }
            .show()
    }

    /**
     * Displays a dialog allowing the user to select a language. When a language
     * is chosen the locale is updated and the activity is recreated.
     */
    private fun showLanguageDialog() {
        val languages = arrayOf("CZ", "EN", "SK")
        val codes = arrayOf("cs", "en", "sk")
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.choose_language_title))
            .setItems(languages) { _, which ->
                setLocale(codes[which])
            }
            .show()
    }

    /**
     * Changes the app's locale to the given language code and recreates
     * the activity so that resources reload.
     */
    private fun setLocale(languageCode: String) {
        // Set the locale using AppCompatDelegate for proper support on modern Android versions
        val localeList = LocaleListCompat.forLanguageTags(languageCode)
        AppCompatDelegate.setApplicationLocales(localeList)
        // Recreate activity to apply changes
        recreate()
    }

    private fun playGame(userChoice: String) {
        // Disable all choice buttons to prevent multiple selections
        rockButton.isEnabled = false
        paperButton.isEnabled = false
        scissorsButton.isEnabled = false

        // Hide the entire container of choice buttons so the result area can expand
        choicesContainer.visibility = View.GONE

        // Show labels for computer and player
        computerLabel.visibility = View.VISIBLE
        playerLabel.visibility = View.VISIBLE

        // Set player's icon based on their choice and show it
        when (userChoice) {
            ROCK -> playerIcon.setImageResource(R.drawable.rock)
            PAPER -> playerIcon.setImageResource(R.drawable.paper)
            SCISSORS -> playerIcon.setImageResource(R.drawable.scissors)
        }
        playerIcon.visibility = View.VISIBLE

        // Generate computer's choice
        val computerChoice = getComputerChoice()
        // Set computer's icon accordingly
        when (computerChoice) {
            ROCK -> computerIcon.setImageResource(R.drawable.rock)
            PAPER -> computerIcon.setImageResource(R.drawable.paper)
            SCISSORS -> computerIcon.setImageResource(R.drawable.scissors)
        }
        computerIcon.visibility = View.VISIBLE

        // Determine winner and update scores. Compare internal constants
        val resultMessageId: Int = if (userChoice == computerChoice) {
            R.string.draw_message
        } else if (
            (userChoice == ROCK && computerChoice == SCISSORS) ||
            (userChoice == PAPER && computerChoice == ROCK) ||
            (userChoice == SCISSORS && computerChoice == PAPER)
        ) {
            playerScore++
            R.string.win_message
        } else {
            computerScore++
            R.string.lose_message
        }
        // Set localized result message and update scoreboard
        resultTextView.text = getString(resultMessageId)
        updateScoreText()

        // Show the repeat button to allow starting a new round
        repeatButton.visibility = View.VISIBLE
    }

    private fun getComputerChoice(): String {
        val choices = listOf(ROCK, PAPER, SCISSORS)
        return choices[Random.nextInt(choices.size)]
    }

    private fun determineWinner(userChoice: String, computerChoice: String): String {
        // Determine winner based on internal constants and return localized message
        return if (userChoice == computerChoice) {
            getString(R.string.draw_message)
        } else if (
            (userChoice == ROCK && computerChoice == SCISSORS) ||
            (userChoice == PAPER && computerChoice == ROCK) ||
            (userChoice == SCISSORS && computerChoice == PAPER)
        ) {
            getString(R.string.win_message)
        } else {
            getString(R.string.lose_message)
        }
    }

    /**
     * Reset the UI to allow a new round to be played. Shows all choice
     * buttons again, hides the computer's icon and the repeat button, and
     * resets the result and choice labels.
     */
    private fun resetGame() {
        // Show the container with all choice buttons
        choicesContainer.visibility = View.VISIBLE
        // Re-enable all choice buttons
        rockButton.isEnabled = true
        paperButton.isEnabled = true
        scissorsButton.isEnabled = true
        // Hide result-specific elements
        computerLabel.visibility = View.GONE
        playerLabel.visibility = View.GONE
        computerIcon.visibility = View.GONE
        playerIcon.visibility = View.GONE
        repeatButton.visibility = View.GONE
        // Reset prompt asking the user to choose a symbol
        resultTextView.text = getString(R.string.choose_symbol)

        // Do not reset scores; just refresh scoreboard
        updateScoreText()
    }

    /**
     * Updates the scoreboard text using the current computer and player scores.
     */
    private fun updateScoreText() {
        val scoreString = getString(R.string.score_format, computerScore, playerScore)
        scoreTextView.text = scoreString
        // Also display the current score in the action bar subtitle so that
        // it remains visible even if the TextView is hidden or clipped. This
        // ensures the score is always shown at the top of the screen.
        supportActionBar?.subtitle = scoreString
    }
}
