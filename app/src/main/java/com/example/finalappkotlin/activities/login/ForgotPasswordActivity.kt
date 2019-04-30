package com.example.finalappkotlin.activities.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.example.finalappkotlin.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPasswordActivity : Activity() {

    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        etEmailForgotPassword.validate {
            etEmailForgotPassword.error = if(iSvalidEmail(it))null else "Email is not valid"
        }

        btnGoLogIn.setOnClickListener {
            goToActivity<LoginActivity>(){
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        btnForgotPassword.setOnClickListener {
            val email = etEmailForgotPassword.text.toString()
            if(iSvalidEmail(email)){
                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(this){
                    toast("Email has been sent to reset your password.")
                    goToActivity<LoginActivity>(){
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                }
            }else{
                toast("Please make sure email address is correct.")
            }
        }

    }
}
