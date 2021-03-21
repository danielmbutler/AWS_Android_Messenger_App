package com.dbtechprojects.awsmessenger.ui.fragments

import android.net.Uri
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.ChatMessage
import com.amplifyframework.datastore.generated.model.LatestMessage
import com.amplifyframework.datastore.generated.model.User
import com.dbtechprojects.awsmessenger.database.Repository
import java.io.InputStream

class SettingsViewModel @ViewModelInject constructor(
    private val repository: Repository
) : ViewModel() {
    init {
        Log.i("ViewModel", "ViewModel created!")

    }

    suspend fun getLoggedInUserObject(): User{
        val user = repository.getUserLoggedInObject()
        return user

    }

    suspend fun UploadFile(inputStream: InputStream): String{
        val imagekey = repository.UploadFile(inputStream)
        if (imagekey.isNotEmpty()){
            return imagekey
            } else{
                return ""
            }
    }

}

