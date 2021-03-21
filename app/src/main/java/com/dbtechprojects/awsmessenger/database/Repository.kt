package com.dbtechprojects.awsmessenger.database

import android.app.Activity
import android.net.Uri
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.amplifyframework.datastore.generated.model.LatestMessage
import com.dbtechprojects.awsmessenger.ui.activities.MainActivity
import kotlinx.coroutines.flow.collect
import java.io.InputStream
import java.util.*
import javax.inject.Inject

// Fragment/Activity -> ViewModel -> Repo -> DB
class Repository @Inject constructor(
    private val db: DatabaseHandler,
) {


    suspend fun getUserLoggedInObject() = db.getLoggedInUserObject()

    suspend fun getLatestMessages(fromid: String) = db.getLatestMessages(fromid)

    suspend fun setLatestMessageasRead(message: LatestMessage) = db.setLatestMessageasRead(message)

    suspend fun UploadFile(inputStream: InputStream) = db.uploadFile(inputStream)

    suspend fun fetchUsers() = db.fetchusers()

    suspend fun getChatMessages(fromid: String, toid: String) = db.getMessages(fromid, toid)

    suspend fun sendMessage(messagetxt: String, fromid: String, toid: String) = db.sendMessage(fromid = fromid, toid = toid, messagetext = messagetxt)

    suspend fun setLatestMessage(messagetxt: String, fromid: String, toid: String) = db.setLatestMessage(fromid = fromid, toid = toid, messagetext = messagetxt)

    suspend fun createLatestMessageAsRead(messagetxt: String, fromid: String, toid: String) = db.createLatestMessageasRead(fromid = fromid, toid = toid, messagetext = messagetxt)


}