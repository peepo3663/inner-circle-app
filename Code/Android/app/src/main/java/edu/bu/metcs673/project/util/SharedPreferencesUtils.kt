package edu.bu.metcs673.project.util

import android.content.Context
import android.content.SharedPreferences

/**
 * SharedPreferencesUtils is the util class for having keys.
 */
object SharedPreferencesUtils {
    val PREFERENCES = "APP_SHARED_PREFS"

    var FIREBASE_TOKEN = "FIREBASE_TOKEN"
        private set

    fun setBoolean(context: Context?, key: String, value: Boolean) {
        context?.let {
            getSharedPreferencesEditor(it)?.apply {
                putBoolean(key, value)
                apply()
            }
        }
    }

    fun getBoolean(context: Context?, key: String, defaultValue: Boolean): Boolean {
        return getSharedPreference(context)?.getBoolean(key, defaultValue) ?: false
    }

    fun setString(context: Context?, key: String, value: String) {
        context?.let {
            getSharedPreferencesEditor(it)?.apply {
                putString(key, value)
                apply()
            }
        }
    }

    fun getString(context: Context?, key: String): String {
        return getSharedPreference(context)?.getString(key, "") ?: ""
    }


    fun getString(context: Context?, key: String, defaultValue: String?): String {
        return defaultValue.let {
            getSharedPreference(context)?.getString(key, defaultValue)
        } ?: ""
    }

    @JvmStatic
    fun setInt(context: Context?, key: String, value: Int) {
        context?.let {
            getSharedPreferencesEditor(it)?.apply {
                putInt(key, value)
                apply()
            }
        }
    }

    fun getInt(context: Context?, key: String, defaultInt: Int): Int {
        return getSharedPreference(context)?.getInt(key, defaultInt) ?: 0
    }

    fun setLong(context: Context?, key: String, value: Long) {
        context?.let {
            getSharedPreferencesEditor(it)?.apply {
                putLong(key, value)
                apply()
            }
        }
    }

    fun getLong(context: Context?, key: String, defaultValue: Long): Long {
        return getSharedPreference(context)?.getLong(key, defaultValue) ?: 0
    }

    fun setFloat(context: Context?, key: String, value: Float) {
        context?.let {
            getSharedPreferencesEditor(it)?.apply {
                putFloat(key, value)
                apply()
            }
        }
    }

    fun getFloat(context: Context?, key: String, defaultValue: Float): Float {
        return getSharedPreference(context)?.getFloat(key, defaultValue) ?: 0.0f

    }

    fun remove(context: Context?, key: String) {
        getSharedPreferencesEditor(context)?.apply {
            remove(key)
            apply()
        }
    }

    private fun getSharedPreferencesEditor(context: Context?): SharedPreferences.Editor? {
        return getSharedPreference(context)?.edit()
    }

    private fun getSharedPreference(context: Context?): SharedPreferences? {
        return context?.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
    }


    fun registerOnSharedPreferenceChangeListener(
        context: Context?,
        sharedPreferenceChangeListener: SharedPreferences.OnSharedPreferenceChangeListener
    ) {
        getSharedPreference(context)?.registerOnSharedPreferenceChangeListener(
            sharedPreferenceChangeListener
        )
    }

    fun unregisterOnSharedPreferenceChangeListener(
        context: Context?,
        sharedPreferenceChangeListener: SharedPreferences.OnSharedPreferenceChangeListener
    ) {
        getSharedPreference(context)?.unregisterOnSharedPreferenceChangeListener(
            sharedPreferenceChangeListener
        )
    }

    fun incrementValue(context: Context?, key: String) {
        // Increment count  by 1
        var value = getInt(context, key, 0)
        setInt(context, key, ++value)
    }
}