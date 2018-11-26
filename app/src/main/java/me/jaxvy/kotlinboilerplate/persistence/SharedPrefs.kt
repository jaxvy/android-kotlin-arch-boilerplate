package me.jaxvy.kotlinboilerplate.persistence

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import me.jaxvy.kotlinboilerplate.utils.edit
import java.util.*

private const val AUTH_TOKEN = "auth_token"
private const val AUTH_TOKEN_TIME_CREATE = "auth_token_time_create"

class SharedPrefs(context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
            "shared_prefs",
            Context.MODE_PRIVATE
    )

    fun setAuthToken(authToken: String) {
        Log.d("AUTH_TOKEN", authToken)
        sharedPreferences.edit {
            putLong(AUTH_TOKEN_TIME_CREATE, Date().getTime())
            putString(AUTH_TOKEN, authToken)
        }
    }

    fun getAuthToken(): String? {
        return sharedPreferences.getString(AUTH_TOKEN, null)
    }

    fun getAuthTokenTimeCreate(): Long {
        return sharedPreferences.getLong(AUTH_TOKEN_TIME_CREATE, -1)
    }

    fun clearAuthToken() {
        sharedPreferences.edit {
            remove(AUTH_TOKEN)
            remove(AUTH_TOKEN_TIME_CREATE)
        }
    }
}

