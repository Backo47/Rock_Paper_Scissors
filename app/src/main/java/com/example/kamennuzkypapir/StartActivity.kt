package com.example.kamennuzkypapir

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import java.util.Locale

/**
 * Activity displayed at launch allowing the user to choose between
 * a single‑player game against the phone and a two‑player game on
 * one device. The language selection menu from the main game is
 * reused here so that users can change the locale before entering
 * either mode. Selecting an option launches the appropriate
 * activity.
 */
class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        // Single player button launches the existing MainActivity
        val singleButton: Button = findViewById(R.id.button_single)
        singleButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        // Two player button launches the TwoPlayerActivity
        val twoButton: Button = findViewById(R.id.button_two)
        twoButton.setOnClickListener {
            val intent = Intent(this, TwoPlayerActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the hamburger menu
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
     * Displays a menu with options specific to the start screen.
     * Allows the user to change the language or view the About page.
     */
    private fun showHamburgerMenu() {
        val options = arrayOf(
            getString(R.string.menu_change_language),
            getString(R.string.menu_about)
        )
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.menu))
            .setItems(options) { _, which ->
                when (which) {
                    0 -> showLanguageDialog()
                    1 -> startActivity(Intent(this, AboutActivity::class.java))
                }
            }
            .show()
    }

    /**
     * Presents a dialog for choosing a language. When a language is
     * selected the locale is updated and the activity is recreated.
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
     * Updates the application locale to the provided language code and
     * recreates the activity to apply the change.
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