package com.dbtechprojects.awsmessenger.database

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.ChatMessage
import com.amplifyframework.datastore.generated.model.LatestMessage
import com.amplifyframework.datastore.generated.model.User
import kotlinx.android.synthetic.main.user_item.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


// class to hold database observe commands to receive realtime data

class DatabaseListener {

    val message          : MutableLiveData<ChatMessage> = MutableLiveData()
    val latestmessage    : MutableLiveData<LatestMessage> = MutableLiveData()

    fun listenformessages(toid: String, Fromid: String){

            Amplify.DataStore.observe(
                ChatMessage::class.java,
                { Log.i("MyAmplifyApp", "Observation began") },
                {
                    // only listen for incoming messages sent to the logged in user
                    if (it.item().toid == Fromid && it.item().fromid == toid){
                        Log.i("MyAmplifyApp", "Message: ${it.item().messageTxt}")

                        GlobalScope.launch(Dispatchers.Main) {
                            val chatMessage = it.item()

                            Log.d("ChatlogActivity", chatMessage.messageTxt!!)
                            message.postValue(chatMessage)

                        }
                    }

                },
                { Log.e("ChatLog-Listen", "Observation failed", it) },
                { Log.i("ChatLog-Listen", "Observation complete") }
            )
        }

    fun listenforLatestMessages(user: User){

        println("listener: ${user.id}")
            Amplify.DataStore.observe(
                LatestMessage::class.java,
                { Log.i("DBlistner", "Observation began") },
                {
                    //if new latestmessage item is sent to current user and is from the user in this row from the latest message model
                    Log.d("DBListner", it.item().toString())
                    if (it.item().toid == user.id ){
                        Log.i("Latest message listener", "Message: ${it.item().messageTxt}")
                        latestmessage.postValue(it.item())

                    }

                },
                { Log.e("DBListner", "Observation failed", it) },
                { Log.i("DBListner", "Observation complete") }

            )
        }


}