package com.example.authenti_kator20

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.authenti_kator20.databinding.ActivityHaveKeyBinding
import com.jakewharton.rxbinding2.widget.RxTextView

@SuppressLint("CheckResult")
class HaveKeyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHaveKeyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHaveKeyBinding.inflate(layoutInflater)
        setContentView(binding.root)

//  Serial Key Validation
        val serialKeyStream = RxTextView.textChanges(binding.etSerialKey)
            .skipInitialValue()
            .map { serialKey ->
                serialKey.isEmpty()
            }
        serialKeyStream.subscribe{
            showTextMinimalAlert(it, "Serial Key")
        }

        binding.tvHaveKey.setOnClickListener {
            startActivity(Intent(this, HaveNotKeyActivity::class.java))
        }
    }

    private fun showTextMinimalAlert(isNotValid: Boolean, fieldName: String) {
        if (fieldName == "Serial Key") {
            val regex = "\\d+".toRegex()
            val serialKey = binding.etSerialKey.text.toString()
            val isValid = serialKey.matches(regex)
            binding.etSerialKey.error = if (!isValid) {
                "Invalid $fieldName, please enter only numbers."
            } else if (isNotValid) {
                "$fieldName cannot be empty"
            } else {
                null
            }
        }
    }
}