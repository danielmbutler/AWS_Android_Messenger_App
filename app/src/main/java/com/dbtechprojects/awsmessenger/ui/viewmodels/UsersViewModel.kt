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
import com.dbtechprojects.awsmessenger.database.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.InputStream

class UsersViewModel @ViewModelInject constructor(
    private val repository: Repository
) : ViewModel() {
    init {
        Log.i("ViewModel", "ViewModel created!")

    }

    // observer
    fun getUserList(): MutableLiveData<List<User>> {
        return repository.getUsers()
    }


    // gets all users from db
    fun fetchusers(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.fetchUsers()
        }
    }

}

