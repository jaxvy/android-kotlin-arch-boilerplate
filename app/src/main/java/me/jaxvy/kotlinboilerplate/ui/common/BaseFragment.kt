package me.jaxvy.kotlinboilerplate.ui.common

import android.arch.lifecycle.LifecycleFragment
import android.content.Context
import android.support.annotation.StringRes
import android.view.inputmethod.InputMethodManager
import android.widget.Toolbar
import me.jaxvy.kotlinboilerplate.utils.setBackButtonAction

abstract class BaseFragment : LifecycleFragment() {

    protected fun hideKeyboard() {
        val content = view
        val activity = activity
        if (content == null || activity == null) {
            return
        }
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val focus = content.findFocus()
        if (focus != null) {
            val view = content.findFocus()
            if (view != null) {
                view.clearFocus()
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        } else {
            imm.hideSoftInputFromWindow(content.windowToken, 0)
        }
    }

    protected fun setupToolbar(toolbar: Toolbar, @StringRes title: Int, showBackArrow: Boolean) {
        activity.setActionBar(toolbar)
        toolbar.setTitle(title)
        activity.actionBar.setDisplayHomeAsUpEnabled(showBackArrow)
        activity.actionBar.setDisplayShowHomeEnabled(showBackArrow)
        toolbar.setBackButtonAction(showBackArrow, onActive = { activity.onBackPressed() })
    }
}