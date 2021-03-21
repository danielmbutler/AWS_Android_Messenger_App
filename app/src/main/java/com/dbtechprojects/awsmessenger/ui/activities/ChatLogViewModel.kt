package com.dbtechprojects.awsmessenger.ui.activities

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.ChatMessage
import com.amplifyframework.datastore.generated.model.LatestMessage
import com.amplifyframework.datastore.generated.model.User
import com.dbtechprojects.awsmessenger.database.Repository

class ChatLogViewModel @ViewModelInject constructor(
    private val repository: Repository
) : ViewModel() {
    init {
        Log.i("ViewModel", "ViewModel created!")

    }

    suspend fun getLoggedInUserObject(): User{
        val user = repository.getUserLoggedInObject()
        return user

    }

    suspend fun SendMessage(messagetxt: String, fromid: String, toid: String){
        repository.sendMessage(messagetxt, fromid, toid)
        repository.setLatestMessage(messagetxt, fromid, toid)
    }

    suspend fun CreateLatestMessageAsRead(messagetxt: String, fromid: String, toid: String){
        repository.createLatestMessageAsRead(messagetxt, fromid, toid)
    }


    suspend fun getChatMessages(fromid: String, toid: String): List<ChatMessage> {

        val messages = repository.getChatMessages(fromid, toid)
        return messages
    }


}
