package com.dbtechprojects.awsmessenger.database

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.amplifyframework.auth.AuthException
import com.amplifyframework.auth.AuthUserAttributeKey
import com.amplifyframework.auth.options.AuthSignUpOptions
import com.amplifyframework.auth.result.AuthSignInResult
import com.amplifyframework.auth.result.AuthSignUpResult
import com.amplifyframework.core.Amplify
import com.amplifyframework.core.model.query.Where
import com.amplifyframework.datastore.DataStoreException
import com.amplifyframework.datastore.DataStoreItemChange
import com.amplifyframework.datastore.generated.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

//CLASS TO provide livedata for Amplify Authentitication properties

class AmplifyAuth {

    private  val TAG = "AmplifyAuth"

    val SignupValue                    : MutableLiveData<Boolean> = MutableLiveData()
    val ConfirmationCode               : MutableLiveData<Boolean> = MutableLiveData()
    val UserCreated                    : MutableLiveData<Boolean> = MutableLiveData()
    val UsernameEdited                 : MutableLiveData<Boolean> = MutableLiveData()
    val ProfileImageEdited             : MutableLiveData<Boolean> = MutableLiveData()
    val SignInResultConfirmation       : MutableLiveData<Boolean> = MutableLiveData()
    val isUserSignedIn                 : MutableLiveData<Boolean> = MutableLiveData()


    fun Signup(email: String, Username: String, Password: String) {

        Log.d("Register Activity", "email: $email, username: $Username, Password: $Password")

        val options = AuthSignUpOptions.builder()
            .userAttribute(AuthUserAttributeKey.email(), email)
            .build()

        try {
            val result =
                Amplify.Auth.signUp(email, Password, options, this::onSuccess, this::SignUpError)
            Log.i(TAG, "Result: $result")
            println(result)
        } catch (error: AuthException) {
            Log.e(TAG, "Sign up failed", error)
        }
    }


    private fun onSuccess(authSignUpResult: AuthSignUpResult) {
        Log.d(TAG, "Sign up success")
        GlobalScope.launch(Dispatchers.Main) {
            SignupValue.setValue(true)
        }


    }

    private fun SignUpError(e: AuthException) {
        Log.e(TAG, "Error: ${e.message}")
        GlobalScope.launch(Dispatchers.Main) {
            SignupValue.setValue(false)
            println(SignupValue.value)
        }
        println(SignupValue.value)


    }

  fun ValidateCode(email: String, code: String)  {

        try {

            val result = Amplify.Auth.confirmSignUp(
                email,
                code,
                this::onConfirmationCodeSuccess,
                this::onConfirmationCodeError
            )
            Log.i(TAG, "Result: $result")
        } catch (error: AuthException) {
            Log.e(TAG, "Failed to confirm signup", error)
        }


    }
    private fun onConfirmationCodeSuccess(signUpResult: AuthSignUpResult) {
        Log.d(TAG, "Confirmation Code success")
        GlobalScope.launch(Dispatchers.Main) {
            ConfirmationCode.setValue(true)

        }


    }

    private fun onConfirmationCodeError(e: AuthException) {
        Log.e(TAG, "Error: ${e.message}")
        GlobalScope.launch(Dispatchers.Main) {
            ConfirmationCode.setValue(false)

        }

    }

     fun SignIn(email: String, Password: String){

        Log.d(TAG, "email: $email, Password: $Password")
        try {
            val result = Amplify.Auth.signIn(email, Password,this::onSignInSucess, this::onSignInError)

        } catch (error: AuthException) {
            Log.e(TAG, "Sign in failed", error)
        }
    }

    private fun onSignInSucess(authSignInResult: AuthSignInResult){
        Log.d(TAG, "SignIn Success")
        GlobalScope.launch(Dispatchers.Main) {
            SignInResultConfirmation.setValue(true)

        }
    }
    private fun onSignInError(e: AuthException){
        Log.d(TAG,"${e.message}")
        GlobalScope.launch(Dispatchers.Main) {
            SignInResultConfirmation.setValue(false)

        }

    }

    fun CreateUser(email: String, Username: String) {

        val user = User.builder()
            .id(java.util.UUID.randomUUID().toString())
            .email(email)
            .isProfileComplete(false)
            .username(Username)
            .profilePhotoUrl("")
            .build()

        try {
            Amplify.DataStore.save(user, this::onCreateUserSuccess, this::onCreateUserFailure)
            Log.i(TAG, "Saved a new user successfully")
        } catch (error: DataStoreException) {
            Log.e(TAG, "Error saving user", error)
        }

    }
    private fun onCreateUserSuccess(dataStoreItemChange: DataStoreItemChange<User>){
        Log.d(TAG, "UserCreated success")
        GlobalScope.launch(Dispatchers.Main) {
            UserCreated.setValue(true)

        }


    }

    private fun onCreateUserFailure(dataStoreException: DataStoreException) {
        Log.d(TAG, "UserCreated failure: ${dataStoreException.message}")
        GlobalScope.launch(Dispatchers.Main) {
            UserCreated.setValue(false)

        }
    }

    fun EditUsername(uid: String, username: String) {

            Amplify.DataStore.query(User::class.java, Where.id(uid),
                { matches ->
                    if (matches.hasNext()) {
                        val original = matches.next()
                        val edited = original.copyOfBuilder()
                            .username(username)
                            .build()
                        Amplify.DataStore.save(edited,
                           this::onEditSuccess, this::onEditFailure
                        )
                    }
                },
                { Log.e("Edit username", "Query failed", it) }
            )


    }




    private fun onEditSuccess(dataStoreItemChange: DataStoreItemChange<User>){
        Log.d(TAG, "UserEdit success")
        GlobalScope.launch(Dispatchers.Main) {
            UsernameEdited.setValue(true)

        }
    }

    private fun onEditFailure(dataStoreException: DataStoreException) {
        Log.d(TAG, "UserEdit failure: ${dataStoreException.message}")
        GlobalScope.launch(Dispatchers.Main) {
            UsernameEdited.setValue(false)

        }
    }

    fun EditProfileImage( uid: String, imagekey: String) {

        Amplify.DataStore.query(User::class.java, Where.id(uid),
            { matches ->
                if (matches.hasNext()) {
                    val original = matches.next()
                    val edited = original.copyOfBuilder()
                        .profilePhotoUrl(imagekey)
                        .build()
                    Amplify.DataStore.save(
                        edited,
                        this::onImageEditSuccess, this::onImageEditFailure
                    )
                }
            },
            { Log.e("Edit Image", "Query failed", it) }
        )

    }



    private fun onImageEditSuccess(dataStoreItemChange: DataStoreItemChange<User>){
        Log.d(TAG, "UserEdit success")
        GlobalScope.launch(Dispatchers.Main) {
            ProfileImageEdited.setValue(true)

        }
    }

    private fun onImageEditFailure(dataStoreException: DataStoreException) {
        Log.d(TAG, "UserEdit failure: ${dataStoreException.message}")
        GlobalScope.launch(Dispatchers.Main) {
            ProfileImageEdited.setValue(false)

        }
    }


      fun checkSignIn(){

            Amplify.Auth.fetchAuthSession(
                { result ->
                    run {
                        if (!result.isSignedIn){
                            Log.d("LoginActivity", "not signed in")

                            isUserSignedIn.postValue(false)

                        } else{

                            isUserSignedIn.postValue(true)
                        }

                    }

                },
                { error -> Log.e("Main Activity", error.toString()) }
            )

    }




}