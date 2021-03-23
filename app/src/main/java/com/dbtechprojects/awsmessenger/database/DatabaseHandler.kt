package com.dbtechprojects.awsmessenger.database

import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.amplifyframework.auth.AuthException
import com.amplifyframework.auth.AuthUserAttributeKey
import com.amplifyframework.auth.options.AuthSignUpOptions
import com.amplifyframework.auth.result.AuthSignUpResult
import com.amplifyframework.core.Amplify
import com.amplifyframework.core.model.query.Where
import com.amplifyframework.datastore.DataStoreException
import com.amplifyframework.datastore.DataStoreItemChange
import com.amplifyframework.datastore.generated.model.ChatMessage
import com.amplifyframework.datastore.generated.model.LatestMessage
import com.amplifyframework.datastore.generated.model.User
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.InputStream
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

//Amplify Database operations

class DatabaseHandler{
    // interface handler


    private val TAG = "DatabaseHandler"

    suspend fun sendImage(S3Url: String, fromid: String, toid: String){
        val chatMessage = ChatMessage.builder()
                .messageTxt("")
                .fromid(fromid)
                .toid(toid)
                .timestamp((System.currentTimeMillis() /1000).toInt())
                .hasImage(true)
                .imageUrl(S3Url)
                .readReceipt("unread")
                .build()
        try {
            Amplify.DataStore.save(chatMessage, this::onSuccess, this::onFailure)
            Log.i("MyAmplifyApp", "Saved a new message successfully")
        } catch (error: DataStoreException) {
            Log.e("MyAmplifyApp", "Error saving post", error)
        }
    }


    suspend fun sendMessage(
        messagetext: String,
        fromid: String,
        toid: String,
    ){

        Log.d(TAG, "message received setting message")

            val chatMessage = ChatMessage.builder()
                .messageTxt(messagetext)
                .fromid(fromid)
                .toid(toid)
                .readReceipt("unread")
                .timestamp((System.currentTimeMillis() / 1000).toInt())
                .build()

            try {
                Amplify.DataStore.save(chatMessage, this::onSuccess, this::onFailure)
                Log.i("MyAmplifyApp", "Saved a new post successfully")
            } catch (error: DataStoreException) {
                Log.e("MyAmplifyApp", "Error saving post", error)
            }


    }

    suspend fun setLatestMessageasRead(message: LatestMessage){
            val messagetosave = LatestMessage.builder()
                .messageTxt(message.messageTxt)
                .fromid(message.fromid)
                .id(message.id)
                .readReceipt("read")
                .timestamp(message.timestamp)
                .toid(message.toid)
                .build()

        try {
            Amplify.DataStore.save(messagetosave, this::onSuccessLatestMessage, this::onFailure)
            Log.i("setLatestMessageRead", "Saved a new post successfully")
        } catch (error: DataStoreException) {
            Log.e("MyAmplifyApp", "Error saving post", error)
        }


    }

    suspend fun setLatestMessage(
        messagetext: String,
        fromid: String,
        toid: String,
    ){

        val readreceipt: String

        if (fromid == toid){
             readreceipt = "read"
        } else {
             readreceipt = "unread"
        }

        val latestMessage = LatestMessage.builder()
            .messageTxt(messagetext)
            .fromid(fromid)
            .toid(toid)
            .timestamp((System.currentTimeMillis() / 1000).toInt().toString())
            .readReceipt(readreceipt)
            .build()

        Amplify.DataStore.query(LatestMessage::class.java, Where.matches(
            LatestMessage.FROMID.eq(fromid).and(LatestMessage.TOID.eq(toid))
                .or(LatestMessage.TOID.eq(fromid).and(LatestMessage.FROMID.eq(toid)))
        ),
            { matches ->
                matches.forEach {
                    Amplify.DataStore.delete(it,
                        { Log.i("MyAmplifyApp", "Deleted a post.") },
                        { Log.e("MyAmplifyApp", "Delete failed.", it) }
                    )
                }

            },
            { Log.e("MyAmplifyApp", "Query failed.", it) }
        )

        try {
            Amplify.DataStore.save(latestMessage, this::onSuccessLatestMessage, this::onFailure)
            Log.i("MyAmplifyApp", "Saved a new post successfully")
        } catch (error: DataStoreException) {
            Log.e("MyAmplifyApp", "Error saving post", error)
        }


    }


    // if user who is sent the message is observing chat log
    suspend fun createLatestMessageasRead(
        messagetext: String,
        fromid: String,
        toid: String,
    ){
        val readreceipt = "read"

        val latestMessage = LatestMessage.builder()
            .messageTxt(messagetext)
            .fromid(fromid)
            .toid(toid)
            .timestamp((System.currentTimeMillis() / 1000).toInt().toString())
            .readReceipt(readreceipt)
            .build()

        Amplify.DataStore.query(LatestMessage::class.java, Where.matches(
            LatestMessage.FROMID.eq(fromid).and(LatestMessage.TOID.eq(toid))
                .or(LatestMessage.TOID.eq(fromid).and(LatestMessage.FROMID.eq(toid)))
        ),
            { matches ->
                matches.forEach {
                    Amplify.DataStore.delete(it,
                        { Log.i("MyAmplifyApp", "Deleted a post.") },
                        { Log.e("MyAmplifyApp", "Delete failed.", it) }
                    )
                }

            },
            { Log.e("MyAmplifyApp", "Query failed.", it) }
        )

        try {
            Amplify.DataStore.save(latestMessage, this::onSuccessLatestMessage, this::onFailure)
            Log.i("MyAmplifyApp", "Saved a new post successfully")
        } catch (error: DataStoreException) {
            Log.e("MyAmplifyApp", "Error saving post", error)
        }


    }

    private fun onSuccess(dataStoreItemChange: DataStoreItemChange<ChatMessage>){
        Log.i("MyAmplifyApp", "Saved a new post successfully")
    }
    private fun onSuccessLatestMessage(dataStoreItemChange: DataStoreItemChange<LatestMessage>){
        Log.i("MyAmplifyApp", "Saved a new post successfully")
    }

    private fun onFailure(dataStoreException: DataStoreException) = dataStoreException.printStackTrace()


    suspend fun getMessages(fromid: String, toid: String): List<ChatMessage>{
        return suspendCoroutine { continuation ->
            val list = arrayListOf<ChatMessage>()
            Amplify.DataStore.query(ChatMessage::class.java, Where.matches(
                ChatMessage.FROMID.eq(fromid).and(ChatMessage.TOID.eq(toid))
                    .or(ChatMessage.TOID.eq(fromid).and(ChatMessage.FROMID.eq(toid)))
            ),
                { ChatMesssage ->
                    run {
                        ChatMesssage.forEach {
                            list.add(it)
                            Log.d("DatabaseHandler", it.toString())
                        }
                        continuation.resume(list)


                    }


                },
                { Log.e("MyAmplifyApp", "Query failed", it) }
            )
        }

    }

    suspend fun getUserFromId(id: String): User{
        return suspendCoroutine { continuation ->

            Amplify.DataStore.query(User::class.java, Where.matches(User.ID.eq(id)),
                { ChatMesssage ->
                    run {
                        ChatMesssage.forEach {
                            continuation.resume(it)
                        }
                    }

                },
                { Log.e("MyAmplifyApp", "Query failed", it) }
            )
        }

    }

    suspend fun getLatestMessages(fromid: String): List<LatestMessage>{
        println("latestmessage fromid $fromid")
        return suspendCoroutine { continuation ->
            val list = arrayListOf<LatestMessage>()
            Amplify.DataStore.query(LatestMessage::class.java, Where.matches(
                LatestMessage.FROMID.eq(
                    fromid
                ).or(LatestMessage.TOID.eq(fromid))
            ),
                { ChatMesssage ->
                    run {
                        ChatMesssage.forEach {
                            list.add(it)
                        }
                        continuation.resume(list)
                    }

                },
                { Log.e("MyAmplifyApp", "Query failed", it) }
            )
        }

    }

    suspend fun getLoggedInUserObject(): User {

        return suspendCoroutine { continuation ->
            Amplify.DataStore.query(
                User::class.java,
                Where.matches(User.EMAIL.eq(Amplify.Auth.currentUser.username)),
                { response ->
                    response.forEach {
                        continuation.resume(it)
                    }

                },
                { error ->
                    Log.e("AmplifyApi", "Query failure", error)
                    // return default data
                }
            )
        }

    }

     suspend fun uploadFile(exampleInputStream: InputStream) : String {
        return suspendCoroutine{ continuation ->



            println("Upload: $exampleInputStream")

            val randomNumber = (1000..9999).random()

            exampleInputStream.let {
                Amplify.Storage.uploadInputStream(
                    "UploadedFile" + randomNumber.toString() + ".jpg",
                    it,
                    { result -> continuation.resume(result.key)},
                    { error -> Log.e("MyAmplifyApp", "Upload failed", error) }
                )
            }
        }

    }

    suspend fun fetchusers() : ArrayList<User> {
        return suspendCoroutine { continuation ->
            Amplify.DataStore.query(User::class.java,
                { Users -> run {
                    val users = arrayListOf<User>()
                    Users.forEach {
                        users.add(it)
                    }
                    continuation.resume(users)
                }

                },
                { Log.e("MyAmplifyApp", "Query failed", it) }
            )
        }

    }



}