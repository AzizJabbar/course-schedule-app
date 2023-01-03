package com.dicoding.courseschedule.ui.setting

import android.content.BroadcastReceiver
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import androidx.work.Data
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.notification.DailyReminder
import java.util.concurrent.TimeUnit

class SettingsFragment : PreferenceFragmentCompat() {

    private lateinit var dailyReminder: DailyReminder

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        // 10 : Update theme based on value in ListPreference
        val themePrefList = findPreference<ListPreference>(getString(R.string.pref_key_dark))
        themePrefList?.setOnPreferenceChangeListener{_, newValue ->
            when(newValue){
                getString(R.string.pref_dark_auto) -> updateTheme(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                getString(R.string.pref_dark_on) -> updateTheme(AppCompatDelegate.MODE_NIGHT_YES)
                getString(R.string.pref_dark_off) -> updateTheme(AppCompatDelegate.MODE_NIGHT_NO)
                else -> {true}
            }
        }
        // 11 : Schedule and cancel notification in DailyReminder based on SwitchPreference
        dailyReminder = DailyReminder()
        val prefNotification = findPreference<SwitchPreference>(getString(R.string.pref_key_notify))
        prefNotification?.setOnPreferenceChangeListener { _, newValue ->

            if (newValue as Boolean){
                context?.let { dailyReminder.setDailyReminder(it) }
                true
            } else {
                context?.let { dailyReminder.cancelAlarm(it) }
                true
            }
        }

    }

    private fun updateTheme(nightMode: Int): Boolean {
        AppCompatDelegate.setDefaultNightMode(nightMode)
        requireActivity().recreate()
        return true
    }
}