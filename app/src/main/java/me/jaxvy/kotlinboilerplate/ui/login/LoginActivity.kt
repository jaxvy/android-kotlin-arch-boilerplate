package me.jaxvy.kotlinboilerplate.ui.login

import android.app.AlertDialog
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_login.*
import me.jaxvy.kotlinboilerplate.R
import me.jaxvy.kotlinboilerplate.ui.common.BaseActivity
import me.jaxvy.kotlinboilerplate.ui.home.HomeActivity
import me.jaxvy.kotlinboilerplate.utils.dismissOnOk
import me.jaxvy.kotlinboilerplate.utils.pop
import me.jaxvy.kotlinboilerplate.utils.startActivity

class LoginActivity : BaseActivity() {

    lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setupToolbar(toolbar, R.string.LoginActivity_toolbar)
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        // We need to rebind login error handing function each time activity is created so that
        // signin button is not leaked when device rotates and correct signin button is updated.
        viewModel.registerLoginErrorHandler { exception -> onLoginError(exception?.message) }
        updateSigninButton()

        signin.setOnClickListener {
            val emailStr = email.text.toString()
            val passwordStr = password.text.toString()
            if (!emailStr.isEmpty() && !passwordStr.isEmpty()) {
                viewModel.login(emailStr, passwordStr,
                        onSuccess = {
                            finish()
                            startActivity<HomeActivity>()
                        })
                updateSigninButton()
            }
        }
    }

    private fun updateSigninButton() {
        if (viewModel.isLoggingIn) {
            signin.setText(R.string.LoginActivity_login_button_in_progress)
            signin.isEnabled = false
        } else {
            signin.setText(R.string.LoginActivity_login_button_default)
            signin.isEnabled = true
        }
    }

    private fun onLoginError(errorMessage: String?) {
        updateSigninButton()
        AlertDialog.Builder(this).pop(R.string.LoginActivity_login_error_dialog_title, errorMessage) {
            dismissOnOk()
        }
    }
}