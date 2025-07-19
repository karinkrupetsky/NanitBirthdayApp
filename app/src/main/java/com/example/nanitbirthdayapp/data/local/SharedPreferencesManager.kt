package com.example.nanitbirthdayapp.data.local

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferencesManager @Inject constructor(
    @ApplicationContext context: Context
) {
    private val prefs: SharedPreferences = context.getSharedPreferences(
        "birthday_prefs",
        Context.MODE_PRIVATE
    )

    fun saveImagePathForBaby(babyKey: String, path: String?) {
        prefs.edit()
            .putString(KEY_IMAGE_PATH_PREFIX + babyKey, path)
            .apply()
    }

    fun getImagePathForBaby(babyKey: String): String? {
        return prefs.getString(KEY_IMAGE_PATH_PREFIX + babyKey, null)
    }

    companion object {
        private const val KEY_IMAGE_PATH_PREFIX = "image_path_"
    }
}