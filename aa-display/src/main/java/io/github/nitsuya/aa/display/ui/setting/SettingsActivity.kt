package io.github.nitsuya.aa.display.ui.setting

import android.os.Bundle
import android.content.Context
import android.widget.Toast
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import io.github.duzhaokun123.template.bases.BaseActivity
import io.github.nitsuya.aa.display.R
import io.github.nitsuya.aa.display.databinding.ActivitySettingsBinding
import io.github.nitsuya.aa.display.util.AADisplayConfig
import io.github.nitsuya.aa.display.util.AADisplayLogger


class SettingsActivity : BaseActivity<ActivitySettingsBinding>(ActivitySettingsBinding::class.java) {
    override fun initViews() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fl_root, SettingsFragment())
            .commit()
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            requireContext().theme.applyStyle(rikka.material.preference.R.style.ThemeOverlay_Rikka_Material3_Preference, true)
            preferenceManager.apply {
                sharedPreferencesName = AADisplayConfig.ConfigName
                sharedPreferencesMode = Context.MODE_PRIVATE
            }
            setPreferencesFromResource(R.xml.pref_aadisplay_config, rootKey)

            // Debug log toggle: sync with AADisplayLogger
            findPreference<Preference>("DebugInputInjectionLog")?.setOnPreferenceChangeListener { _, newValue ->
                AADisplayLogger.enabled = newValue as Boolean
                if (newValue) AADisplayLogger.clear()
                true
            }

            // Export log button
            findPreference<Preference>("ExportDiagnosticLog")?.setOnPreferenceClickListener {
                if (!AADisplayLogger.enabled) {
                    Toast.makeText(requireContext(), "Please enable Debug Input Injection Log first", Toast.LENGTH_LONG).show()
                    return@setOnPreferenceClickListener true
                }
                val path = AADisplayLogger.exportToFile(requireContext())
                if (path != null) {
                    Toast.makeText(requireContext(), "Log exported to:\n$path", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(requireContext(), "Export failed", Toast.LENGTH_SHORT).show()
                }
                true
            }
        }
    }



}
