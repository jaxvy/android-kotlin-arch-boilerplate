package me.jaxvy.kotlinboilerplate.ui.common

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import me.jaxvy.kotlinboilerplate.utils.setBackButtonAction

abstract class BaseFragment : Fragment() {

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
        (activity as? AppCompatActivity)?.run {
            setSupportActionBar(toolbar)
            toolbar.setTitle(title)
            actionBar?.setDisplayHomeAsUpEnabled(showBackArrow)
            actionBar?.setDisplayShowHomeEnabled(showBackArrow)
            toolbar.setBackButtonAction(showBackArrow, onActive = { activity?.onBackPressed() })
        }
    }
}