package com.dbtechprojects.awsmessenger.ui.fragments

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.ChatMessage
import com.amplifyframework.datastore.generated.model.LatestMessage
import com.amplifyframework.datastore.generated.model.User
import com.dbtechprojects.awsmessenger.database.Repository

//viewmodel class to call repo

class MessagesViewModel @ViewModelInject constructor(
    private val repository: Repository
) : ViewModel() {
    init {
        Log.i("ViewModel", "ViewModel created!")

    }

    suspend fun getLoggedInUserObject(): User{
        val user = repository.getUserLoggedInObject()
        return user

    }

    suspend fun getLatestMessages(fromid: String): List<LatestMessage>{
        val messages = repository.getLatestMessages(fromid)
        return messages
    }

    suspend fun setLatestMessageAsRead(message: LatestMessage) {
        repository.setLatestMessageasRead(message)
    }

}
