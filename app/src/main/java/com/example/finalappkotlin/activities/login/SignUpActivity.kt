package com.example.finalappkotlin.activities.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.example.finalappkotlin.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : Activity() {

    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        btnGoLogIn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        btnSignUp.setOnClickListener {
            val email = etEMailSignUp.text.toString()
            val password = etLoginPassword.text.toString()
            if(isVailidEmailAndPassword(email,password)){
                signUpByEmail(email,password)
            }else{
                Toast.makeText(this, "Please fill all the data and confirm password is correct.", Toast.LENGTH_SHORT).show()
            }
        }

        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth.currentUser
        if (currentUser == null) {
            Toast.makeText(this, "User IS NOT logged in", Toast.LENGTH_SHORT).show()
            try {
                signUpByEmail("diemp94@hotmail.com", "petete0714")
            } catch (exception: Exception) {
                Toast.makeText(this, exception.toString(), Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "User IS logged in", Toast.LENGTH_SHORT).show()
        }

    }

    private fun signUpByEmail(email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(this, "An email has sent to you. Please confirm before sign in.", Toast.LENGTH_SHORT)
                        .show()
                    val user = mAuth.currentUser
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this, "An unexpected error occurred, please try again.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun isVailidEmailAndPassword(email: String,password: String):Boolean{
        return !email.isNullOrEmpty() && !password.isNullOrEmpty() &&
                etLoginPassword.text.toString() == etLoginConfirmPassword.text.toString()
    }
}
