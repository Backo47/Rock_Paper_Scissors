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
        menuInflater.inflate(R.menu.menu_main, menu)
        // Show current language code in uppercase on the menu item
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
     * Presents a simple dialog for choosing a language. When a language
     * is selected the locale is updated and the activity is recreated
     * so that the correct resources are loaded.
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