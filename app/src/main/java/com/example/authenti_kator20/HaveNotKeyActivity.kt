package com.example.authenti_kator20

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.authenti_kator20.databinding.ActivityHaveNotKeyBinding

class HaveNotKeyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHaveNotKeyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHaveNotKeyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.haveKeyAlreadyTitle.setOnClickListener {
            startActivity(Intent(this, HaveKeyActivity::class.java))
        }
    }
}