package com.example.finalappkotlin.activities.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.example.finalappkotlin.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : Activity() {

    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        btnGoLogIn.setOnClickListener {
            goToActivity<LoginActivity> { flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        btnSignUp.setOnClickListener {
            val email = etEMailSignUp.text.toString()
            val password = etLoginPassword.text.toString()
            val confirmPassword = etLoginConfirmPassword.text.toString()
            if (iSvalidEmail(email) && iSvalidPassword(password) && isValidConfirmPassword(password, confirmPassword)) {
                signUpByEmail(email, password)
            } else {
                toast("Please make sure all the data is correct.")
            }
        }

        etEMailSignUp.validate {
            etEMailSignUp.error = if (iSvalidEmail(it)) null else "Email is not valid"
        }
        etLoginPassword.validate {
            etLoginPassword.error =
                if (iSvalidPassword(it)) null else "Password should contain 1 lowerCase, 1 uppercase, 1 number, 1 specialCaracter and 4 characters length at least"
        }
        etLoginConfirmPassword.validate {
            etLoginConfirmPassword.error = if (isValidConfirmPassword(
                    etLoginPassword.text.toString(),
                    it
                )
            ) null else "The passwords are differentes"
        }

    }

    private fun signUpByEmail(email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    mAuth.currentUser!!.sendEmailVerification().addOnCompleteListener(this){
                        toast("An email has sent to you. Please confirm before sign in.")
                        // Sign in success, update UI with the signed-in user's information
                        goToActivity<LoginActivity> {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    toast("An unexpected error occurred, please try again.")
                }
            }
    }


}
