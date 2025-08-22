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
    private lateinit var computerChoiceTextView: TextView
    private lateinit var userChoiceTextView: TextView

    private lateinit var rockButton: ImageButton
    private lateinit var paperButton: ImageButton
    private lateinit var scissorsButton: ImageButton
    private lateinit var computerIcon: ImageView
    private lateinit var repeatButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        resultTextView = findViewById(R.id.result_text)
        computerChoiceTextView = findViewById(R.id.computer_choice_text)
        userChoiceTextView = findViewById(R.id.user_choice_text)

        // Initialize choice buttons
        rockButton = findViewById(R.id.rock_button)
        paperButton = findViewById(R.id.paper_button)
        scissorsButton = findViewById(R.id.scissors_button)

        // Initialize computer icon and repeat button
        computerIcon = findViewById(R.id.computer_icon)
        repeatButton = findViewById(R.id.repeat_button)

        // Set click listeners for player's choice
        rockButton.setOnClickListener { playGame("Kámen") }
        paperButton.setOnClickListener { playGame("Papír") }
        scissorsButton.setOnClickListener { playGame("Nůžky") }

        // Set click listener for repeat button
        repeatButton.setOnClickListener { resetGame() }
    }

    private fun playGame(userChoice: String) {
        // Display player's choice text
        userChoiceTextView.text = "Ty: $userChoice"
        // Hide buttons that were not chosen
        when (userChoice) {
            "Kámen" -> {
                paperButton.visibility = View.GONE
                scissorsButton.visibility = View.GONE
            }
            "Papír" -> {
                rockButton.visibility = View.GONE
                scissorsButton.visibility = View.GONE
            }
            "Nůžky" -> {
                rockButton.visibility = View.GONE
                paperButton.visibility = View.GONE
            }
        }
        // Disable all buttons to prevent multiple selections
        rockButton.isEnabled = false
        paperButton.isEnabled = false
        scissorsButton.isEnabled = false

        val computerChoice = getComputerChoice()
        computerChoiceTextView.text = "Telefon: $computerChoice"
        // Set computer icon based on choice and show it
        when (computerChoice) {
            "Kámen" -> computerIcon.setImageResource(R.drawable.rock)
            "Papír" -> computerIcon.setImageResource(R.drawable.paper)
            "Nůžky" -> computerIcon.setImageResource(R.drawable.scissors)
        }
        computerIcon.visibility = View.VISIBLE
        // Determine winner and display result
        resultTextView.text = determineWinner(userChoice, computerChoice)
        // Show repeat button
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
        // Show all buttons
        rockButton.visibility = View.VISIBLE
        paperButton.visibility = View.VISIBLE
        scissorsButton.visibility = View.VISIBLE
        // Enable buttons again for selection
        rockButton.isEnabled = true
        paperButton.isEnabled = true
        scissorsButton.isEnabled = true
        // Hide computer icon and repeat button
        computerIcon.visibility = View.GONE
        repeatButton.visibility = View.GONE
        // Reset text views
        resultTextView.text = getString(R.string.vyberte_k_men_n_ky_nebo_pap_r)
        userChoiceTextView.text = ""
        computerChoiceTextView.text = ""
    }
}
