package com.dbtechprojects.awsmessenger.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.exifinterface.media.ExifInterface
import androidx.lifecycle.Observer
import com.amplifyframework.auth.AuthException
import com.amplifyframework.auth.result.AuthSignInResult
import com.amplifyframework.core.Amplify
import com.dbtechprojects.awsmessenger.R
import com.dbtechprojects.awsmessenger.database.AmplifyAuth
import com.dbtechprojects.awsmessenger.util.SharedPref
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val auth = AmplifyAuth()

        GlobalScope.launch(Dispatchers.IO) {
            val check = auth.checkSignIn()
            // if user is already signed in lets redirect to main activity
            if(check == true){
                LoginSuccess()
            }
        }

        LoginRegisterBtn.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }



        LoginBtn.setOnClickListener {
            val check = ValidateLogin() // validates form
            if(!check){
                return@setOnClickListener
            } else{
                LoginProgressBar.visibility = View.VISIBLE
               auth.SignIn(
                   LoginTxtUsernameEditTxt.text.toString(),
                   LoginTxtPasswordEditTxt.text.toString()
               )
                // check livedata to see if user sign in was successeful
               auth.SignInValue.observe(this, Observer { result->
                   if (result == true){
                       LoginSuccess()
                       LoginProgressBar.visibility = View.INVISIBLE
                   } else{
                       Toast.makeText(this, "Sign In Failed", Toast.LENGTH_SHORT).show()
                       LoginProgressBar.visibility = View.INVISIBLE
                   }
               })
            }
        }


    }

    private fun ValidateLogin(): Boolean{
        if (
            LoginTxtUsernameEditTxt.text.toString().isEmpty() ||
            LoginTxtPasswordEditTxt.text.toString().isEmpty()
        ){
            Toast.makeText(this, "Please Provide Email and Password",
                Toast.LENGTH_SHORT).show()
            return false
        } else return true
    }



private fun LoginSuccess(){
    Log.d("MainActivity", Amplify.Auth.currentUser.username)
    val intent = Intent(this@LoginActivity, MainActivity::class.java)
    startActivity(intent)
    finish()
}

}