package me.jaxvy.kotlinboilerplate.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import java.io.Serializable

inline fun SharedPreferences.edit(func: SharedPreferences.Editor.() -> Unit) {
    val editor = edit()
    editor.func()
    editor.apply()
}

fun ViewGroup.inflate(@LayoutRes layoutId: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
}

inline fun AlertDialog.Builder.pop(@StringRes title: Int, message: String?, func: AlertDialog.Builder.() -> Unit) {
    setTitle(title)
    setMessage(message)
    func()
    show()
}

fun AlertDialog.Builder.dismissOnOk() {
    setPositiveButton("Ok") { dialog, _ ->
        dialog.dismiss()
    }
}

fun AlertDialog.Builder.dismissOnOk(onClickListener: () -> (Unit)) {
    setPositiveButton("Ok") { dialogInterface, _ ->
        onClickListener()
        dialogInterface.dismiss()
    }
}

inline fun <reified T : Activity> Context.startActivity(vararg inputs: Pair<String, Any?>) {
    val intent = Intent(this, T::class.java)
    inputs.forEach {
        val value = it.second
        when (value) {
            null -> intent.putExtra(it.first, null as Serializable?)
            is Int -> intent.putExtra(it.first, value)
            is Long -> intent.putExtra(it.first, value)
            is CharSequence -> intent.putExtra(it.first, value)
            is String -> intent.putExtra(it.first, value)
            is Float -> intent.putExtra(it.first, value)
            is Double -> intent.putExtra(it.first, value)
            is Char -> intent.putExtra(it.first, value)
            is Short -> intent.putExtra(it.first, value)
            is Boolean -> intent.putExtra(it.first, value)
            is Serializable -> intent.putExtra(it.first, value)
            is Bundle -> intent.putExtra(it.first, value)
            is Parcelable -> intent.putExtra(it.first, value)
            is Array<*> -> when {
                value.isArrayOf<CharSequence>() -> intent.putExtra(it.first, value)
                value.isArrayOf<String>() -> intent.putExtra(it.first, value)
                value.isArrayOf<Parcelable>() -> intent.putExtra(it.first, value)
                else -> throw Exception("Intent extra ${it.first} has wrong type ${value.javaClass.name}")
            }
            is IntArray -> intent.putExtra(it.first, value)
            is LongArray -> intent.putExtra(it.first, value)
            is FloatArray -> intent.putExtra(it.first, value)
            is DoubleArray -> intent.putExtra(it.first, value)
            is CharArray -> intent.putExtra(it.first, value)
            is ShortArray -> intent.putExtra(it.first, value)
            is BooleanArray -> intent.putExtra(it.first, value)
            else -> throw Exception("Intent extra ${it.first} has wrong type ${value.javaClass.name}")
        }
    }
    startActivity(intent)
}

inline fun consume(func: () -> Unit): Boolean {
    func()
    return true
}

fun FirebaseAuth.isUserLoggedIn(): Boolean = currentUser != null

inline fun Toolbar.setBackButtonAction(activate: Boolean, crossinline onActive: () -> (Unit)) {
    setNavigationOnClickListener(if (activate) View.OnClickListener { onActive() } else null)
}