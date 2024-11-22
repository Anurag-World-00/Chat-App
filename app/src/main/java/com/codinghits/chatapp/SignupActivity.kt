package com.codinghits.chatapp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.codinghits.chatapp.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignupActivity : AppCompatActivity() {
    private val auth = FirebaseAuth.getInstance()
    private lateinit var mDbRef: DatabaseReference
    private lateinit var signupBinding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signupBinding = ActivitySignupBinding.inflate(layoutInflater)
        val view = signupBinding.root
        enableEdgeToEdge()
        setContentView(view)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        signupBinding.buttonUserSignup.setOnClickListener {
            val name = signupBinding.editUserName.text.toString().trim()
            val userEmail = signupBinding.editTextUserEmail.text.toString().trim()
            val userPassword = signupBinding.editTextUserPassword.text.toString().trim()

            // Check if fields are empty
            if (name.isEmpty() || userEmail.isEmpty() || userPassword.isEmpty()) {
                Toast.makeText(applicationContext, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validate email format
            if (!isValidEmail(userEmail)) {
                Toast.makeText(applicationContext, "Invalid Email Address", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Proceed with Firebase signup
            signupWithFirebase(name, userEmail, userPassword)
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun signupWithFirebase(name: String, userEmail: String, userPassword: String) {
        auth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(applicationContext, "Successfully Signed up", Toast.LENGTH_LONG).show()
                addUserToDatabase(name, userEmail, auth.currentUser?.uid!!)
                finish()
            } else {
                Log.e("SignupError", "Error: ${task.exception?.localizedMessage}")
                Toast.makeText(applicationContext, "Failure in Signed up: ${task.exception?.localizedMessage}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun addUserToDatabase(name: String, userEmail: String, uid: String) {
        mDbRef = FirebaseDatabase.getInstance().getReference()
        mDbRef.child("user").child(uid).setValue(User(name, userEmail, uid))
    }
}
