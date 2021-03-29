package com.dbtechprojects.awsmessenger.ui.viewmodels

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
import kotlinx.coroutines.launch
import java.io.InputStream

class ChatLogViewModel @ViewModelInject constructor(
    private val repository: Repository
) : ViewModel() {
    init {
        Log.i("ViewModel", "ViewModel created!")

    }

    // LIVE DATA GETTERS
    fun getUploadedFileKey(): MutableLiveData<String>{
        return repository.getUploadedFileKey()
    }

    fun getLoggedInUserObject(): MutableLiveData<User>{
        return repository.getLoggedInUserObject()
    }


    fun getChatMessages(): MutableLiveData<List<ChatMessage>>{
        return repository.getChatMessages()
    }

    fun listenForChatMessages(): MutableLiveData<ChatMessage>{
        return repository.incomingChatMessage()
    }


    // Execute Functions

    fun setupChatMessageListener(toid: String, fromid: String){
        viewModelScope.launch {
            repository.setupChatMessageListener(toid, fromid)
        }
    }

    fun UploadFile(inputStream: InputStream) {
        viewModelScope.launch {
            repository.UploadFile(inputStream)
        }
    }


     fun SendImage(S3Url: String, fromid: String, toid: String){
        viewModelScope.launch {
            repository.sendImage(S3Url, fromid, toid)
            repository.setLatestMessage("", fromid, toid)
        }
    }

     fun QueryLoggedInUserObject(){
        viewModelScope.launch {
            repository.QueryLoggedInUserObject()
        }
    }

     fun SendMessage(messagetxt: String, fromid: String, toid: String){
         viewModelScope.launch {
             repository.sendMessage(messagetxt, fromid, toid)
             repository.setLatestMessage(messagetxt, fromid, toid)
         }
    }

    fun CreateLatestMessageAsRead(messagetxt: String, fromid: String, toid: String){
        viewModelScope.launch {
            repository.createLatestMessageAsRead(messagetxt, fromid, toid)
        }
    }


     fun QueryChatMessages(fromid: String, toid: String) {

        repository.QueryChatMessages(fromid, toid)
    }


}
