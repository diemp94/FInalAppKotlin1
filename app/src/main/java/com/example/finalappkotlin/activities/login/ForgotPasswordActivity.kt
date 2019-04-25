package com.example.finalappkotlin.activities.login

import android.app.Activity
import android.os.Bundle
import com.example.finalappkotlin.R
import com.example.finalappkotlin.goToActivity
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPasswordActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        btnGoLogIn.setOnClickListener {
            goToActivity<LoginActivity>()
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
        }
    }
}
