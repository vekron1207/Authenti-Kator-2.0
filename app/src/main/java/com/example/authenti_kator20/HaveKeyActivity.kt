package com.example.authenti_kator20

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.authenti_kator20.databinding.ActivityHaveKeyBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.jakewharton.rxbinding2.widget.RxTextView
import kotlinx.android.synthetic.main.activity_have_key.authenticate_btn

@SuppressLint("CheckResult")
class HaveKeyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHaveKeyBinding
    private lateinit var databaseRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHaveKeyBinding.inflate(layoutInflater)
        databaseRef = FirebaseDatabase.getInstance("https://authenti-kator2-default-rtdb.firebaseio.com/").getReference("Nike_DB")
        setContentView(binding.root)

        // Serial Key Validation
        val serialKeyStream = RxTextView.textChanges(binding.etSerialKey)
            .skipInitialValue()
            .map { serialKey ->
                serialKey.isEmpty()
            }
        serialKeyStream.subscribe {
            showTextMinimalAlert(it, "Serial Key")
        }

        binding.tvHaveKey.setOnClickListener {
            startActivity(Intent(this, HaveNotKeyActivity::class.java))
        }

        binding.authenticateBtn.setOnClickListener {
            val serialNumber = binding.etSerialKey.text.toString().trim().toInt()
            databaseRef.orderByChild("serial_number").equalTo(serialNumber.toDouble())
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            Log.d("HavekeyActivity", "Data from database: $snapshot")
                            snapshot.children.forEach { productSnapshot ->
                                val isAuthenticated = productSnapshot.child("isAuthenticated").value as Boolean
                                Log.d("HavekeyActivity", "isAuthenticated value: $isAuthenticated")
                                val productType = productSnapshot.child("product_type").value as String
                                if (isAuthenticated) {
                                    Toast.makeText(this@HaveKeyActivity, "Product already activated", Toast.LENGTH_SHORT).show()
                                } else {
                                    databaseRef.child(productSnapshot.key!!).child("isAuthenticated").setValue(true)
                                    Toast.makeText(this@HaveKeyActivity, "Authentic $productType", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {
                            Toast.makeText(this@HaveKeyActivity, "Fake product", Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        Log.e("HavekeyActivity", "Error:${error.message} ")
                    }
                })
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
