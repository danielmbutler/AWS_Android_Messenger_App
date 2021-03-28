package com.dbtechprojects.awsmessenger.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.amplifyframework.core.Amplify
import com.dbtechprojects.awsmessenger.R
import com.dbtechprojects.awsmessenger.database.AmplifyAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_login.*

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


            viewModel.checkSessionValue()
            viewModel.getSessionValue().observe(this, Observer { result ->
                // if user is already signed in lets redirect to main activity
                if (result == true){
                    LoginSuccess()
                }
            })




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
               viewModel.signInUser(
                   LoginTxtUsernameEditTxt.text.toString(),
                   LoginTxtPasswordEditTxt.text.toString()
               )
                // check livedata to see if user sign in was successeful
               viewModel.getSignedInConfirmation().observe(this, Observer { result->
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