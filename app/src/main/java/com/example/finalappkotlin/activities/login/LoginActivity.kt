package com.example.finalappkotlin.activities.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.finalappkotlin.*
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {

    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val mGoogleApiCliente: GoogleApiClient by lazy { getGoogleApliCliente() }
    private val RC_GOOGLE_SIGN_IN = 99


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

        btnLoginGoogle.setOnClickListener {
            val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiCliente)
            startActivityForResult(signInIntent,RC_GOOGLE_SIGN_IN)
        }

        etEmailSignIn.validate {
            etEmailSignIn.error = if (iSvalidEmail(it)) null else "Email is not valid"
        }
        etEmailSignInPass.validate {
            etEmailSignInPass.error =
                if (iSvalidPassword(it)) null else "Password should contain 1 lowerCase, 1 uppercase, 1 number, 1 specialCaracter and 4 characters length at least"
        }

    }

    private fun getGoogleApliCliente(): GoogleApiClient {
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("411989201058-0mceq91navevo8qkdb01fdh6tsmm2hf9.apps.googleusercontent.com")
            .requestEmail()
            .build()
        return GoogleApiClient.Builder(this)
            .enableAutoManage(this, this)
            .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
            .build()
    }

    private fun loginByGoogleAccountIntoFirebase(googleAccount: GoogleSignInAccount){
        val credential = GoogleAuthProvider.getCredential(googleAccount.idToken, null)
        mAuth.signInWithCredential(credential).addOnCompleteListener(this) {
            if(mGoogleApiCliente.isConnected){
                Auth.GoogleSignInApi.signOut(mGoogleApiCliente)
            }
            goToActivity<MainActivity>{
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
        }
    }

    private fun logInByEmail(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                if(mAuth.currentUser!!.isEmailVerified){
                    toast("${mAuth.currentUser?.displayName} is now logged in.")
                }else{
                    toast("User must confirm email first")
                }
            } else {
                toast("an unexpected error occurred, please try again.")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == RC_GOOGLE_SIGN_IN){
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if(result.isSuccess){
                val account = result.signInAccount
                loginByGoogleAccountIntoFirebase(account!!)
            }
        }
    }


    override fun onConnectionFailed(p0: ConnectionResult) {
        toast("Connection Failed!! ")
    }

}
