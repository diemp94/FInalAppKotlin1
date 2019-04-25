package com.example.finalappkotlin.activities.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.example.finalappkotlin.R
import com.example.finalappkotlin.goToActivity
import com.example.finalappkotlin.toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : Activity() {

    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        btnGoLogIn.setOnClickListener {
            goToActivity<LoginActivity> { flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK }
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
        }

        btnSignUp.setOnClickListener {
            val email = etEMailSignUp.text.toString()
            val password = etLoginPassword.text.toString()
            if (isVailidEmailAndPassword(email, password)) {
                signUpByEmail(email, password)
            } else {
                toast("Please fill all the data and confirm password is correct.")
            }
        }

    }

    private fun signUpByEmail(email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    toast("An email has sent to you. Please confirm before sign in.")
                    goToActivity<LoginActivity> { flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK }
                    overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
                } else {
                    // If sign in fails, display a message to the user.
                    toast("An unexpected error occurred, please try again.")
                }
            }
    }

    private fun isVailidEmailAndPassword(email: String, password: String): Boolean {
        return !email.isEmpty() && !password.isEmpty() &&
                etLoginPassword.text.toString() == etLoginConfirmPassword.text.toString()
    }
}
