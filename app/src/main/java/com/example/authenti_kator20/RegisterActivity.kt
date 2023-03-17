package com.example.authenti_kator20

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import androidx.core.content.ContextCompat
import com.example.authenti_kator20.databinding.ActivityLoginBinding
import com.example.authenti_kator20.databinding.ActivityRegisterBinding
import com.jakewharton.rxbinding2.widget.RxTextView
import java.util.Observable

@SuppressLint("CheckResult")
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

//  Full Name Validation
        val nameStream = RxTextView.textChanges(binding.etFullname)
            .skipInitialValue()
            .map { name ->
                name.isEmpty()
            }
        nameStream.subscribe {
            showNameExistAlert(it)
        }

//  Email Validation
        val emailStream = RxTextView.textChanges(binding.etEmail)
            .skipInitialValue()
            .map { email ->
                !Patterns.EMAIL_ADDRESS.matcher(email).matches()
            }
        emailStream.subscribe{
            showEmailValidAlert(it)
        }

//  Username Validation
        val usernameStream = RxTextView.textChanges(binding.etUsername)
            .skipInitialValue()
            .map { username ->
                username.length < 6
            }
        usernameStream.subscribe{
            showTextMinimalAlert(it, "Username")
        }

//  Password Validation
        val passwordStream = RxTextView.textChanges(binding.etPassword)
            .skipInitialValue()
            .map { password ->
                password.length < 6
            }
        passwordStream.subscribe{
            showTextMinimalAlert(it, "Password")
        }

//  Confirm Password Validation
        val passwordConfirmStream = io.reactivex.Observable.merge(
            RxTextView.textChanges(binding.etPassword)
                .skipInitialValue()
                .map { password ->
                    password.toString() != binding.etConfirmPassword.text.toString()
                },
            RxTextView.textChanges(binding.etConfirmPassword)
                .skipInitialValue()
                .map { confirmPassword ->
                    confirmPassword.toString() != binding.etPassword.text.toString()
                })
        passwordConfirmStream.subscribe {
            showPasswordConfirmedAlert(it)
        }

//  Button Enable True or False
        val invalidFieldStream = io.reactivex.Observable.combineLatest(
            nameStream,
            emailStream,
            usernameStream,
            passwordStream,
            passwordConfirmStream
        ) { nameInvalid: Boolean, emailInvalid: Boolean, usernameInvalid: Boolean, passwordInvalid: Boolean, passwordConfirmedInvalid: Boolean ->
            !nameInvalid && !emailInvalid && !usernameInvalid && !passwordInvalid && !passwordConfirmedInvalid
        }
        invalidFieldStream.subscribe { isValid ->
            if (isValid) {
                binding.btnRegister.isEnabled = true
                binding.btnRegister.backgroundTintList = ContextCompat.getColorStateList(this, R.color.primary_color)
            } else {
                binding.btnRegister.isEnabled = false
                binding.btnRegister.backgroundTintList = ContextCompat.getColorStateList(this, android.R.color.darker_gray)
            }
        }

//  Click
        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        binding.tvHaveAccount.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun showNameExistAlert(isNotValid: Boolean){
        binding.etFullname.error = if (isNotValid) "Name Cannot be Empty!" else null
    }

    private fun showTextMinimalAlert(isNotValid: Boolean, text: String){
        if (text == "Username")
            binding.etUsername.error = if (isNotValid) "$text Must be more than 6 letters!" else null
        else if (text == "Password")
            binding.etPassword.error = if (isNotValid) "$text Must be more than 8 letters!" else null
    }

    private fun  showEmailValidAlert(isNotValid: Boolean) {
        binding.etEmail.error = if (isNotValid) "Invalid Email" else null
    }

    private fun showPasswordConfirmedAlert(isNotValid: Boolean){
        binding.etConfirmPassword.error = if(isNotValid) "Please enter same passwords!" else null
    }
}