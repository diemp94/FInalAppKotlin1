package com.example.finalappkotlin.activities.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.finalappkotlin.R
import com.example.finalappkotlin.goToActivity
import com.example.finalappkotlin.toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnLogIn.setOnClickListener {
            val email = etEmailSignIn.text.toString()
            val password = etEmailSignInPass.text.toString()
            if (isValidEmailAndPassword(email, password)) {
                logInByEmail(email, password)
            }
        }
        tvForgotPassword.setOnClickListener {
            goToActivity<ForgotPasswordActivity>()
            overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right)
        }
        btnCreateAccount.setOnClickListener {
            goToActivity<SignUpActivity>()
            overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right)
        }

    }

    private fun logInByEmail(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                toast("${mAuth.currentUser?.displayName} is now logged in.")
            } else {
                toast("an unexpected error occurred, please try again.")

            }
        }
    }

    private fun isValidEmailAndPassword(email: String, password: String): Boolean {
        return !email.isEmpty() && !password.isEmpty()
    }

}
