package com.example.kamennuzkypapir

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

/**
 * Activity implementing a two‑player version of Rock–Paper–Scissors. Two
 * people play on the same device by taking turns selecting a symbol. When
 * both players have chosen, the winner is determined, the appropriate
 * scores are updated and displayed, and players can start another round.
 */
class TwoPlayerActivity : AppCompatActivity() {
    // UI elements for the scoreboard and instructions
    private lateinit var scoreTextView: TextView
    private lateinit var turnLabel: TextView
    private lateinit var selectLabel: TextView
    // Choice buttons for both players (reused for each turn)
    private lateinit var rockButton: ImageButton
    private lateinit var paperButton: ImageButton
    private lateinit var scissorsButton: ImageButton
    // Views for displaying each player's choice after both have played
    private lateinit var resultTextView: TextView
    private lateinit var player1Label: TextView
    private lateinit var player2Label: TextView
    private lateinit var player1Icon: ImageView
    private lateinit var player2Icon: ImageView
    private lateinit var repeatButton: ImageButton
    private lateinit var choicesContainer: View
    private lateinit var resultContainer: View

    // Scores for the two players
    private var player1Score: Int = 0
    private var player2Score: Int = 0

    // Internal constants for choices independent of locale
    private val ROCK = "rock"
    private val PAPER = "paper"
    private val SCISSORS = "scissors"

    // Variables to hold the current round's choices
    private var player1Choice: String? = null
    private var player2Choice: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_two_player)

        // Restore scores on configuration changes
        if (savedInstanceState != null) {
            player1Score = savedInstanceState.getInt("player1Score", 0)
            player2Score = savedInstanceState.getInt("player2Score", 0)
        }

        // Initialize UI references
        scoreTextView = findViewById(R.id.score_text_two)
        turnLabel = findViewById(R.id.turn_label)
        selectLabel = findViewById(R.id.select_label)
        rockButton = findViewById(R.id.rock_button_two)
        paperButton = findViewById(R.id.paper_button_two)
        scissorsButton = findViewById(R.id.scissors_button_two)
        resultTextView = findViewById(R.id.result_text_two)
        player1Label = findViewById(R.id.player1_label)
        player2Label = findViewById(R.id.player2_label)
        player1Icon = findViewById(R.id.player1_icon)
        player2Icon = findViewById(R.id.player2_icon)
        repeatButton = findViewById(R.id.repeat_button_two)
        choicesContainer = findViewById(R.id.choices_container_two)
        resultContainer = findViewById(R.id.result_container)

        // Assign click listeners for choice buttons
        rockButton.setOnClickListener { onChoiceSelected(ROCK) }
        paperButton.setOnClickListener { onChoiceSelected(PAPER) }
        scissorsButton.setOnClickListener { onChoiceSelected(SCISSORS) }
        // Repeat button resets only the round (not scores)
        repeatButton.setOnClickListener { resetRound() }

        // Initialize scoreboard and UI for player 1's turn
        updateScoreText()
        prepareForPlayer(1)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("player1Score", player1Score)
        outState.putInt("player2Score", player2Score)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val currentLanguage = Locale.getDefault().language
        val code = when (currentLanguage) {
            "cs" -> "CZ"
            "en" -> "EN"
            "sk" -> "SK"
            else -> currentLanguage.uppercase()
        }
        menu.findItem(R.id.menu_language)?.title = code
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_language -> {
                showLanguageDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Called when either player selects a symbol. Depending on whose turn it is,
     * the choice is stored and either the next player's turn is prepared or
     * the result is shown.
     */
    private fun onChoiceSelected(choice: String) {
        if (player1Choice == null) {
            player1Choice = choice
            // Move to second player's turn
            prepareForPlayer(2)
        } else if (player2Choice == null) {
            player2Choice = choice
            // Both choices have been made; show the result
            showResult()
        }
    }

    /**
     * Configures the UI for the given player's turn. Shows the appropriate
     * label and ensures that the choice buttons are enabled. Hides any result
     * views that may have been visible from a previous round.
     */
    private fun prepareForPlayer(playerNumber: Int) {
        // Hide result layout and repeat button while selecting
        resultContainer.visibility = View.GONE
        repeatButton.visibility = View.GONE
        // Show the selection container
        choicesContainer.visibility = View.VISIBLE
        // Enable choice buttons for new selection
        rockButton.isEnabled = true
        paperButton.isEnabled = true
        scissorsButton.isEnabled = true
        // Set labels for current player
        turnLabel.text = if (playerNumber == 1) getString(R.string.player1_label) else getString(R.string.player2_label)
        selectLabel.text = getString(R.string.vyberte_k_men_n_ky_nebo_pap_r)
    }

    /**
     * Determines the winner based on both players' choices, updates scores,
     * shows the result message and displays each player's choice along with
     * a repeat button for starting another round.
     */
    private fun showResult() {
        // Disable further selection until next round
        rockButton.isEnabled = false
        paperButton.isEnabled = false
        scissorsButton.isEnabled = false

        // Determine the winner and update scores
        val resultMessage: String = if (player1Choice == player2Choice) {
            getString(R.string.draw_message)
        } else if (
            (player1Choice == ROCK && player2Choice == SCISSORS) ||
            (player1Choice == PAPER && player2Choice == ROCK) ||
            (player1Choice == SCISSORS && player2Choice == PAPER)
        ) {
            player1Score++
            getString(R.string.player1_win_message)
        } else {
            player2Score++
            getString(R.string.player2_win_message)
        }
        // Update scoreboard
        updateScoreText()

        // Prepare result views
        resultTextView.text = resultMessage
        // Set icons for each player's choice
        player1Icon.setImageResource(choiceToDrawable(player1Choice))
        player2Icon.setImageResource(choiceToDrawable(player2Choice))
        // Show labels and icons
        player1Label.text = getString(R.string.player1_label)
        player2Label.text = getString(R.string.player2_label)
        resultContainer.visibility = View.VISIBLE
        choicesContainer.visibility = View.GONE
        // Show repeat button to allow starting another round
        repeatButton.visibility = View.VISIBLE
    }

    /**
     * Resets only the round data (choices) and prepares for Player 1's turn
     * without resetting the scores. Called when the user taps the repeat
     * button after a round has completed.
     */
    private fun resetRound() {
        player1Choice = null
        player2Choice = null
        prepareForPlayer(1)
    }

    /**
     * Returns the drawable resource ID corresponding to a given internal
     * choice constant. If the choice is null this returns 0 (no image).
     */
    private fun choiceToDrawable(choice: String?): Int {
        return when (choice) {
            ROCK -> R.drawable.rock
            PAPER -> R.drawable.paper
            SCISSORS -> R.drawable.scissors
            else -> 0
        }
    }

    /**
     * Updates the scoreboard text using the scores for both players.
     */
    private fun updateScoreText() {
        val scoreString = getString(R.string.player_score_format, player1Score, player2Score)
        scoreTextView.text = scoreString
    }

    /**
     * Displays a dialog to choose a language. When a language is selected
     * the locale is updated and the activity is recreated to load new
     * resources.
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
     * Changes the app locale to the given language code and recreates
     * the activity so the new resources take effect.
     */
    private fun setLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
        recreate()
    }
}