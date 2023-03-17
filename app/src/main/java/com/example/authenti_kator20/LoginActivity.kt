package com.example.authenti_kator20

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.authenti_kator20.databinding.ActivityLoginBinding
import com.example.authenti_kator20.databinding.ActivityMainBinding
import com.jakewharton.rxbinding2.widget.RxTextView

@SuppressLint("CheckResult")
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

//  Username Validation
        val usernameStream = RxTextView.textChanges(binding.etEmail)
            .skipInitialValue()
            .map { username ->
                username.isEmpty()
            }
        usernameStream.subscribe{
            showTextMinimalAlert(it, "Email/Username")
        }

//  Password Validation
        val passwordStream = RxTextView.textChanges(binding.etPassword)
            .skipInitialValue()
            .map { password ->
                password.isEmpty()
            }
        passwordStream.subscribe{
            showTextMinimalAlert(it, "Password")
        }

//  Button Enable True or False
        val invalidFieldStream = io.reactivex.Observable.combineLatest(
            usernameStream,
            passwordStream
        ) {usernameInvalid: Boolean, passwordInvalid: Boolean ->
            !usernameInvalid && !passwordInvalid
        }
        invalidFieldStream.subscribe { isValid ->
            if (isValid) {
                binding.btnLogin.isEnabled = true
                binding.btnLogin.backgroundTintList = ContextCompat.getColorStateList(this, R.color.primary_color)
            } else {
                binding.btnLogin.isEnabled = false
                binding.btnLogin.backgroundTintList = ContextCompat.getColorStateList(this, android.R.color.darker_gray)
            }
        }

//  Click
        binding.btnLogin.setOnClickListener{
            startActivity(Intent(this, HomeActivity::class.java))
        }

        binding.tvHaventAccount.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun showTextMinimalAlert(isNotValid: Boolean, text: String){
        if (text == "Email/Username")
            binding.etEmail.error = if (isNotValid) "$text Cannot be empty!" else null
        else if (text == "Password")
            binding.etPassword.error = if (isNotValid) "$text Cannot be empty!" else null
    }
}