package com.example.finalappkotlin.activities.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.finalappkotlin.*
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
            if (iSvalidEmail(email) && iSvalidPassword(password)) {
                logInByEmail(email, password)
            }else{
                toast("Please make sure all the data is currect.")
            }
        }
        tvForgotPassword.setOnClickListener {
            goToActivity<ForgotPasswordActivity>()
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        }
        btnCreateAccount.setOnClickListener {
            goToActivity<SignUpActivity>()
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        }

        etEmailSignIn.validate {
            etEmailSignIn.error = if (iSvalidEmail(it)) null else "Email is not valid"
        }
        etEmailSignInPass.validate {
            etEmailSignInPass.error =
                if (iSvalidPassword(it)) null else "Password should contain 1 lowerCase, 1 uppercase, 1 number, 1 specialCaracter and 4 characters length at least"
        }

    }

    private fun logInByEmail(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                toast("${mAuth.currentUser?.displayName} is now logged in.")
                val currentUser = mAuth.currentUser!!
            } else {
                toast("an unexpected error occurred, please try again.")

            }
        }
    }

}
