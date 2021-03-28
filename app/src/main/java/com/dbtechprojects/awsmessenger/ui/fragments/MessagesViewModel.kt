package com.dbtechprojects.awsmessenger.ui.fragments

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
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

//viewmodel class to call repo

class MessagesViewModel @ViewModelInject constructor(
    private val repository: Repository
) : ViewModel() {
    init {
        Log.i("ViewModel", "ViewModel created!")

    }

    //LiveDataGetters

    fun getLoggedInUserObject(): MutableLiveData<User> {
        return repository.getLoggedInUserObject()
    }

    fun getSessionValue() : LiveData<Boolean> {
        return repository.isSignedin()
    }

    fun getLatestMessages(): LiveData<List<LatestMessage>>{
        return repository.getLatestMessages()
    }

    fun getIncomingLatestMessage(): LiveData<LatestMessage>{
        return repository.incomingLatestMessage()
    }

    //execute functions

    fun setupLatestMessageListener(user: User){
        viewModelScope.launch(Dispatchers.IO) {
            repository.setupLatestMessageListener(user)
        }
    }

    fun checkSessionValue(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.checkSession()
        }
    }

   fun QueryLoggedInUserObject(){
        viewModelScope.launch (Dispatchers.IO){
            repository.QueryLoggedInUserObject()
        }
    }

     fun QueryLatestMessages(fromid: String){
         viewModelScope.launch(Dispatchers.Main) {
             repository.QueryLatestMessages(fromid)
         }
    }

   fun setLatestMessageAsRead(message: LatestMessage) {
       viewModelScope.launch(Dispatchers.IO) {
           repository.setLatestMessageasRead(message)
       }
    }

}
