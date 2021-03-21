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
import com.amplifyframework.datastore.DataStoreException
import com.amplifyframework.datastore.DataStoreItemChange
import com.amplifyframework.datastore.generated.model.User
import com.dbtechprojects.awsmessenger.R
import com.dbtechprojects.awsmessenger.database.AmplifyAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.log

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private val auth = AmplifyAuth()

    private lateinit var mUsername: String
    private lateinit var mEmail: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        RegisterSignupBTN.setOnClickListener {
            if (RegisterTxtEmailEditTxt.text.isNullOrEmpty() ||
                RegisterTxtUsernameEditTXT.text.isNullOrEmpty() ||
                RegisterTxtPasswordEditTxt.text.isNullOrEmpty()
            ) {
                Toast.makeText(this, "Please fill out form", Toast.LENGTH_SHORT).show()
            } else {
                RegisterProgressBar.visibility = View.VISIBLE

                // SignUp User

                auth.Signup(
                    RegisterTxtEmailEditTxt.text.toString(),
                    RegisterTxtUsernameEditTXT.text.toString(),
                    RegisterTxtPasswordEditTxt.text.toString()
                )

                // Observe result

               auth.SignupValue.observe(this, Observer { result ->
                    Log.d("registerAct", result.toString())
                    if (result == true){
                        RegisterProgressBar.visibility = View.INVISIBLE
                        mUsername = RegisterTxtEmailEditTxt.text.toString()
                        mEmail = RegisterTxtEmailEditTxt.text.toString()
                        updateUiRegisterSuccess()
                    } else{

                            Toast.makeText(this@RegisterActivity, "something went wrong", Toast.LENGTH_SHORT).show()
                            RegisterProgressBar.visibility = View.INVISIBLE
                    }
                })

            }
        }

        RegisterConfirmationCodeBTN.setOnClickListener {
            if (RegisterConfirmationCodeEditText.text.toString().isNullOrEmpty()) {
                Toast.makeText(this, "Please Provide Code", Toast.LENGTH_SHORT).show()
            } else {

                RegisterProgressBar.visibility = View.VISIBLE
                // check validation code success from AWS
                auth.ValidateCode(
                    mUsername, RegisterConfirmationCodeEditText.text.toString()
                )

                //observe and respond accordingly
                auth.ConfirmationCode.observe(this, Observer { result ->
                    Log.d("registerAct", result.toString())
                    if (result == true){
                        RegisterProgressBar.visibility = View.INVISIBLE
                        auth.CreateUser(mEmail, mUsername)
                        auth.UserCreated.observe(this, Observer { UserCreationresult ->
                            if (UserCreationresult == true){
                                redirect()
                            } else{
                                Toast.makeText(this@RegisterActivity, "something went wrong", Toast.LENGTH_SHORT).show()
                                RegisterProgressBar.visibility = View.INVISIBLE
                            }

                        })
                    } else{

                        Toast.makeText(this@RegisterActivity, "something went wrong", Toast.LENGTH_SHORT).show()
                        RegisterProgressBar.visibility = View.INVISIBLE
                    }
                })
            }
        }


    }


    private fun updateUiRegisterSuccess() {

        Toast.makeText(
            this, "Sign up Successful please provide code from email",
            Toast.LENGTH_SHORT
        ).show()
        RegisterConfirmationCodeEditText.visibility = View.VISIBLE
        RegisterConfirmationCode.visibility = View.VISIBLE
        RegisterConfirmationCodeBTN.visibility = View.VISIBLE
    }

    private fun redirect(){
        val intent = Intent(this, LoginActivity::class.java)
        intent.putExtra("Register", "Success")
        startActivity(intent)
        finish()
    }



}