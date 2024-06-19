package com.mobile.freshpicks.view.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.mobile.freshpicks.view.main.MainActivity
import com.mobile.freshpicks.R
import com.mobile.freshpicks.data.pref.UserModel
import com.mobile.freshpicks.databinding.ActivityLoginBinding
import com.mobile.freshpicks.helper.ViewModelFactory
import com.mobile.freshpicks.view.signup.SignUpActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.isLoading.observe(this) { isLoading -> if (isLoading) showLoading(true) else showLoading(false) }

        setupTextWatchers()
        setupAction()
    }

    private fun setupTextWatchers(){
        val emailEditText = binding.edLoginEmail
        val emailInputLayout = binding.emailEditTextLayout
        emailInputLayout.setEditText(emailEditText)

        val passwordEditText = binding.edLoginPassword
        val passwordInputLayout = binding.passwordEditTextLayout
        passwordInputLayout.setEditText(passwordEditText)
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()

            binding.loadingProgressBar.visibility = View.VISIBLE

            if (email.isNotEmpty() && password.isNotEmpty()) {
                viewModel.login(email, password) { response ->
                    if (!response.error) {
                        viewModel.saveSession(UserModel(email, response.data?.token ?: ""))
                        showAlertDialog(getString(R.string.yeah), getString(R.string.login_success), true)
                    } else {
                        showAlertDialog(getString(R.string.oops), getString(R.string.wrong_account_details))
                    }
                }
            } else {
                binding.loadingProgressBar.visibility = View.GONE
                showAlertDialog(getString(R.string.oops), getString(R.string.field_not_filled))
            }
        }
        binding.clickableRegisterTv.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showAlertDialog(title: String, message: String, isSuccess: Boolean = false) {
        AlertDialog.Builder(this).apply {
            setTitle(title)
            setMessage(message)
            if (isSuccess) {
                setPositiveButton(getString(R.string.next)) { _, _ ->
                    val intent = Intent(context, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
            } else {
                setPositiveButton(getString(R.string.back)) { _, _ -> }
            }
            create()
            show()
        }
    }

    private fun showLoading(isLoading: Boolean) { binding.loadingProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE}
}