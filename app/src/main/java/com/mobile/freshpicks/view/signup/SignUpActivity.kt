package com.mobile.freshpicks.view.signup

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.mobile.freshpicks.R
import com.mobile.freshpicks.databinding.ActivitySignUpBinding
import com.mobile.freshpicks.helper.ViewModelFactory
import com.mobile.freshpicks.view.login.LoginActivity

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private val viewModel by viewModels<SignUpViewModel>{
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.isLoading.observe(this) { isLoading -> if (isLoading) showLoading(true) else showLoading(false) }

        setupTextWatchers()
        setupAction()
    }

    private fun setupTextWatchers(){
        val emailEditText = binding.edRegisterEmail
        val emailInputLayout = binding.emailEditTextLayout
        emailInputLayout.setEditText(emailEditText)

        val passwordEditText = binding.edRegisterPassword
        val passwordInputLayout = binding.passwordEditTextLayout
        passwordInputLayout.setEditText(passwordEditText)
    }

    private fun setupAction() {
        binding.signupButton.setOnClickListener {
            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()

            binding.loadingProgressBar.visibility = View.VISIBLE

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                viewModel.register(name, email, password) { response ->
                    if (!response.error) {
                        showAlertDialog(getString(R.string.yeah), getString(R.string.register_success), true)
                    } else {
                        showAlertDialog(getString(R.string.oops), getString(R.string.registered_email))
                    }
                }
            } else {
                binding.loadingProgressBar.visibility = View.GONE
                showAlertDialog(getString(R.string.oops), getString(R.string.field_not_filled))
            }
        }

        binding.clickableLoginTv.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun showAlertDialog(title: String, message: String, isSuccess: Boolean = false) {
        AlertDialog.Builder(this).apply {
            setTitle(title)
            setMessage(message)
            if (isSuccess) {
                setPositiveButton(getString(R.string.next)) { _, _ ->
                    val intent = Intent(context, LoginActivity::class.java)
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