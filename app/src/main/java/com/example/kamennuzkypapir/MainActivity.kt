package com.example.kamennuzkypapir

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var resultTextView: TextView
    private lateinit var computerLabel: TextView
    private lateinit var playerLabel: TextView
    private lateinit var rockButton: ImageButton
    private lateinit var paperButton: ImageButton
    private lateinit var scissorsButton: ImageButton
    private lateinit var computerIcon: ImageView
    private lateinit var playerIcon: ImageView
    private lateinit var choicesContainer: View
    private lateinit var repeatButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        resultTextView = findViewById(R.id.result_text)
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
        rockButton.setOnClickListener { playGame("Kámen") }
        paperButton.setOnClickListener { playGame("Papír") }
        scissorsButton.setOnClickListener { playGame("Nůžky") }

        // Set click listener for repeat button
        repeatButton.setOnClickListener { resetGame() }
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
            "Kámen" -> playerIcon.setImageResource(R.drawable.rock)
            "Papír" -> playerIcon.setImageResource(R.drawable.paper)
            "Nůžky" -> playerIcon.setImageResource(R.drawable.scissors)
        }
        playerIcon.visibility = View.VISIBLE

        // Generate computer's choice
        val computerChoice = getComputerChoice()
        // Set computer's icon accordingly
        when (computerChoice) {
            "Kámen" -> computerIcon.setImageResource(R.drawable.rock)
            "Papír" -> computerIcon.setImageResource(R.drawable.paper)
            "Nůžky" -> computerIcon.setImageResource(R.drawable.scissors)
        }
        computerIcon.visibility = View.VISIBLE

        // Determine winner and display result
        resultTextView.text = determineWinner(userChoice, computerChoice)

        // Show the repeat button to allow starting a new round
        repeatButton.visibility = View.VISIBLE
    }

    private fun getComputerChoice(): String {
        val choices = listOf("Kámen", "Papír", "Nůžky")
        return choices[Random.nextInt(choices.size)]
    }

    private fun determineWinner(userChoice: String, computerChoice: String): String {
        return if (userChoice == computerChoice) {
            "Remíza!"
        } else if (
            (userChoice == "Kámen" && computerChoice == "Nůžky") ||
            (userChoice == "Papír" && computerChoice == "Kámen") ||
            (userChoice == "Nůžky" && computerChoice == "Papír")
        ) {
            "Vyhrál jsi!"
        } else {
            "Prohrál jsi!"
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
        // Reset result text
        resultTextView.text = getString(R.string.vyberte_k_men_n_ky_nebo_pap_r)
    }
}
