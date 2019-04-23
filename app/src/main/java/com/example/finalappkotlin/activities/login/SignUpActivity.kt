package com.example.finalappkotlin.activities.login

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import com.example.finalappkotlin.R
import com.google.firebase.auth.FirebaseAuth
import java.lang.Exception

class SignUpActivity : Activity() {

    private val mAuth : FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth.currentUser
        if(currentUser == null){
            Toast.makeText(this,"User IS NOT logged in",Toast.LENGTH_SHORT).show()
            try {
                createAccount("diemp94@hotmail.com","petete0714")
            }catch (exception: Exception){
                Toast.makeText(this, exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(this,"User IS logged in",Toast.LENGTH_SHORT).show()
        }

    }

    private fun createAccount(email: String, password: String){
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(this, "createUserWithEmail:success", Toast.LENGTH_SHORT).show()
                    val user = mAuth.currentUser
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this, "createUserWithEmail:failure"+ task.exception, Toast.LENGTH_SHORT).show()
                }
            }
    }
}
