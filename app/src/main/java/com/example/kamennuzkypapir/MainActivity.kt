package com.example.kamennuzkypapir

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var resultTextView: TextView
    private lateinit var computerChoiceTextView: TextView
    private lateinit var userChoiceTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        resultTextView = findViewById(R.id.result_text)
        computerChoiceTextView = findViewById(R.id.computer_choice_text)
        userChoiceTextView = findViewById(R.id.user_choice_text)

        val rockButton: Button = findViewById(R.id.rock_button)
        val paperButton: Button = findViewById(R.id.paper_button)
        val scissorsButton: Button = findViewById(R.id.scissors_button)

        rockButton.setOnClickListener { playGame("Kámen") }
        paperButton.setOnClickListener { playGame("Papír") }
        scissorsButton.setOnClickListener { playGame("Nůžky") }
    }

    private fun playGame(userChoice: String) {
        userChoiceTextView.text = "Ty: $userChoice"
        val computerChoice = getComputerChoice()
        computerChoiceTextView.text = "Telefon: $computerChoice"
        resultTextView.text = determineWinner(userChoice, computerChoice)
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
}
