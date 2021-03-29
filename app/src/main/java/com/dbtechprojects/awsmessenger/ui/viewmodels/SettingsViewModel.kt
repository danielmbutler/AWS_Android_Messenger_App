package com.dbtechprojects.awsmessenger.ui.viewmodels

import android.net.Uri
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.ChatMessage
import com.amplifyframework.datastore.generated.model.LatestMessage
import com.amplifyframework.datastore.generated.model.User
import com.dbtechprojects.awsmessenger.database.AmplifyAuth
import com.dbtechprojects.awsmessenger.database.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.InputStream

class SettingsViewModel @ViewModelInject constructor(
    private val repository: Repository,

) : ViewModel() {
    init {
        Log.i("ViewModel", "ViewModel created!")

    }

    // LiveData Getters
    fun getLoggedInUserObject(): MutableLiveData<User> {
        return repository.getLoggedInUserObject()
    }
    fun isUsernameUpdated(): MutableLiveData<Boolean>{
        return repository.getUsernameEditedValue()
    }

    fun getUploadedFileKey(): MutableLiveData<String>{
        return repository.getUploadedFileKey()
    }

    fun getProfileImageEditedValue(): MutableLiveData<Boolean>{
        return repository.getProfileImageEditedValue()
    }

    fun getUserCreatedValue(): MutableLiveData<Boolean>{
        return repository.getUserCreatedValue()
    }

    // Launch Functions

    fun edituser(uid: String, username: String){
        viewModelScope.launch(Dispatchers.IO) { repository.editUsername(uid, username) }
    }

     fun queryLoggedInUserObject(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.QueryLoggedInUserObject()
        }
    }

    fun editProfileImage(userid: String, imagekey: String){
        viewModelScope.launch(Dispatchers.IO) {
            repository.editProfileImage(userid, imagekey)
        }
    }


     fun uploadFile(inputStream: InputStream){
        viewModelScope.launch(Dispatchers.IO) {
            repository.UploadFile(inputStream)
        }
    }

    fun createUser(username: String, email: String){
        viewModelScope.launch(Dispatchers.IO) {
            repository.createUser(username, email)
        }
    }

}

