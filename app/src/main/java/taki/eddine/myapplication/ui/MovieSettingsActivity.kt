package taki.eddine.myapplication.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.CheckBoxPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import taki.eddine.myapplication.fragments.RatingBottomSheet
import taki.eddine.myapplication.R
import taki.eddine.myapplication.databinding.ActivityMovieSettingsBinding

class MovieSettingsActivity : AppCompatActivity() {
    private var _binding : ActivityMovieSettingsBinding? = null
    private val binding get() = _binding!!
    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMovieSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainer, SettingsFragment()).commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.settingslayout, rootKey)
            // TODO : Language Selection Prefs
            val languagesPrefs = requireActivity().getSharedPreferences("languages", MODE_PRIVATE)
            val editor = languagesPrefs.edit()
             // TODO : DarkMode Selection Prefs
            val modesPrefs = requireActivity().getSharedPreferences("darkMode", MODE_PRIVATE)
            val darkModeEditor = modesPrefs.edit()

            val englishPreference = findPreference<CheckBoxPreference>("english")
            val spanishPreference = findPreference<CheckBoxPreference>("spanish")
            val modesPreference = findPreference<SwitchPreference>("mode")
            val appRatingPreference = findPreference<Preference>("rate")
            englishPreference!!.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { preference, newValue ->
                val isChecked = newValue as Boolean
                if (isChecked) {
                    englishPreference.isChecked = true
                    spanishPreference!!.isChecked = false
                    editor.putString("language", "en")
                    editor.putString("languageCode", "en-US")
                    editor.apply()
                     Intent(requireContext(), MainActivity::class.java).apply {
                         startActivity(this)
                     }
                }
                true
            }
            spanishPreference!!.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { preference, newValue ->
                val isChecked = newValue as Boolean
                if (isChecked) {
                    spanishPreference.isChecked = true
                    englishPreference.isChecked = false
                    editor.putString("language", "es")
                    editor.putString("languageCode", "es-ES")
                    editor.apply()
                    Intent(requireContext(), MainActivity::class.java).apply {
                        startActivity(this)
                    }
                }
                true
            }
            modesPreference!!.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { preference, newValue ->
                val isSwitched = newValue as Boolean
                if (isSwitched) {
                    modesPreference.title = requireContext().getString(R.string.darkmode)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    darkModeEditor.putBoolean("mode", isSwitched)
                } else {
                    modesPreference.title = requireContext().getString(R.string.lightmode)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    darkModeEditor.putBoolean("mode", isSwitched)
                }
                darkModeEditor.apply()
                true
            }
            if (!englishPreference.isChecked && !spanishPreference.isChecked) {
                spanishPreference.isChecked = false
                englishPreference.isChecked = true
                editor.putString("language", "en")
                editor.putString("languageCode", "en-US")
                editor.apply()
                Intent(requireContext(), MainActivity::class.java).apply {
                    startActivity(this)
                }
            }
            appRatingPreference!!.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                val bottomSheet = RatingBottomSheet()
                bottomSheet.show(requireFragmentManager(), "tag")
                true
            }
        }
    }
}