package com.appcake.modsforvalheim.utlis

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.google.gson.Gson

class SessionManager(
    // Context
    var _context: Context,
) {
    var pref: SharedPreferences

    var editor: SharedPreferences.Editor

    var PRIVATE_MODE = 0


    /**
     * Clear session details
     */
    fun clearPrefs() {
        editor.clear()
        editor.commit()
    }

    fun logoutUser() {
        // Clearing all data from Shared Preferences
        editor.remove(SubscriptionCheck)

        editor.clear()
        editor.commit()

        // After logout redirect user to Login Activity
//        val i = Intent(_context, LoginAct::class.java)
//        Closing all the Activities
//        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        Staring Login Activity
//        _context.startActivity(i)
    }

    fun putSubscriptionSkf(user: Boolean) {
        editor.remove(SubscriptionCheck)
        editor.putBoolean(SubscriptionCheck, user)
        editor.commit()
    }

    /**
     * Quick check for login
     */
    val subscriptionCheck: Boolean
        get() = pref.getBoolean(SubscriptionCheck, false)

    companion object {
        // Shared pref file name
        private const val PREF_NAME = "SPF_PREF"
        // All Shared Preferences Keys
        private const val SubscriptionCheck = "SubscriptionCheck"
    }

    // Constructor
    init {
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref.edit()
        editor.apply()
    }
}