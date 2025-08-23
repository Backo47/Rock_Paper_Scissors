package com.example.kamennuzkypapir

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.os.Build
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import android.content.Intent

/**
 * Simple activity displaying information about the application. It shows
 * the author, version name taken from the build configuration and the
 * release date formatted with the current locale. This screen is
 * accessible from the hamburger menu.
 */
class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        // Set title of the action bar if present
        supportActionBar?.title = getString(R.string.about_title)

        // Populate fields with static and dynamic information
        // Retrieve versionName from package information rather than BuildConfig to avoid
        // unresolved reference errors. This uses the package manager to get the
        // current version name defined in the manifest.
        // Retrieve the application version name using the appropriate API based on the OS level.
        val versionName: String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val info = packageManager.getPackageInfo(
                packageName,
                PackageManager.PackageInfoFlags.of(0)
            )
            info.versionName ?: ""
        } else {
            @Suppress("DEPRECATION")
            val info = packageManager.getPackageInfo(packageName, 0)
            info.versionName ?: ""
        }
        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val dateString = dateFormat.format(Date())
        findViewById<TextView>(R.id.about_author).text = getString(R.string.about_author)
        findViewById<TextView>(R.id.about_version).text = getString(R.string.about_version_format, versionName)
        findViewById<TextView>(R.id.about_date).text = getString(R.string.about_date_format, dateString)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Add hamburger menu to the about screen
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
     * Displays a hamburger menu with options to navigate back to the mode
     * selection screen, change the language or view the About page again.
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
                        // Start the mode selection screen
                        val intent = Intent(this, StartActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)
                    }
                    1 -> showLanguageDialog()
                    2 -> {
                        // Already on about page; reopen itself (no-op)
                    }
                }
            }
            .show()
    }

    /**
     * Shows a dialog allowing the user to select a language. When a
     * language is selected, the locale is updated and the activity is
     * recreated to apply new resources.
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
     * Updates the application locale and recreates the activity so that
     * strings and other resources update to the newly selected language.
     */
    private fun setLocale(languageCode: String) {
        // Use AppCompatDelegate to set application locales for modern Android versions
        val localeList = LocaleListCompat.forLanguageTags(languageCode)
        AppCompatDelegate.setApplicationLocales(localeList)
        // Recreate the activity to apply changes to current UI
        recreate()
    }
}