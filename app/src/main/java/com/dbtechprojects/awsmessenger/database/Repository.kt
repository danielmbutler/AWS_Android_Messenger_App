package com.dbtechprojects.awsmessenger.database

import androidx.lifecycle.MutableLiveData
import com.amplifyframework.datastore.generated.model.ChatMessage
import com.amplifyframework.datastore.generated.model.LatestMessage
import com.amplifyframework.datastore.generated.model.User
import kotlinx.android.synthetic.main.activity_register.*
import java.io.InputStream
import javax.inject.Inject

// Fragment/Activity -> ViewModel -> Repo -> DB
class Repository @Inject constructor(
    private val db: DatabaseHandler,
    private val auth: AmplifyAuth,
    private val dblistener: DatabaseListener
) {

    // Auth LiveDataGetters

    fun isSignedin(): MutableLiveData<Boolean> {
        return auth.isUserSignedIn
    }

    fun signInConfirmation(): MutableLiveData<Boolean>{
        return auth.SignInResultConfirmation
    }

    fun getProfileImageEditedValue(): MutableLiveData<Boolean>{
        return auth.ProfileImageEdited
    }

    fun getUserCreatedValue(): MutableLiveData<Boolean>{
        return auth.UserCreated
    }

    fun getSignUpValue(): MutableLiveData<Boolean>{
        return auth.SignupValue
    }

    fun getValidationCodeCheckValue(): MutableLiveData<Boolean>{
        return auth.ConfirmationCode
    }

    fun getUsernameEditedValue(): MutableLiveData<Boolean>{
        return auth.UsernameEdited
    }



    // DB LISTENER LIVEDATA GETTERS

    fun incomingChatMessage(): MutableLiveData<ChatMessage>{
        return dblistener.message
    }


    fun incomingLatestMessage(): MutableLiveData<LatestMessage>{
        return dblistener.latestmessage
    }



   // DATABASE HANDLER LIVEDATE GETTERS


    fun getUploadedFileKey(): MutableLiveData<String>{
        return db.UploadedFileValue
    }

    fun getLoggedInUserObject(): MutableLiveData<User>{
        return db.LoggedInUser
    }

    fun getChatMessages(): MutableLiveData<List<ChatMessage>>{
        return db.ChatMessageList
    }

    fun getLatestMessages(): MutableLiveData<List<LatestMessage>>{
        return db.LatestMessageList
    }


    fun getUsers(): MutableLiveData<List<User>>{
        return db.UsersList
    }



    // EXECUTION FUNCTIONS

    //AUTH FUNCTIONS

    fun editUsername(uid: String, username: String) = auth.EditUsername(uid, username)

    fun checkSession() = auth.checkSignIn()

    fun signIn(email: String, Password: String) = auth.SignIn(email,Password)

    fun editProfileImage(userid : String, imagekey: String) = auth.EditProfileImage(userid,imagekey)

    fun createUser(username: String, email: String) = auth.CreateUser(username, email)

    fun signUpUser(email: String, username: String, password: String) = auth.Signup(email,username,password)

    fun validateConfirmationCode(username: String, code: String) = auth.ValidateCode(username, code)

    // DB LISTENER FUNCTIONS

    fun setupLatestMessageListener(user: User){
        dblistener.listenforLatestMessages(user)
    }
    fun setupChatMessageListener(toid: String, fromid: String){
        dblistener.listenformessages(toid, fromid)
    }

    // DATABASE HANDLER FUNCTIONS

    fun QueryLoggedInUserObject() = db.getLoggedInUserObject()

    fun QueryLatestMessages(fromid: String) = db.getLatestMessages(fromid)

    fun setLatestMessageasRead(message: LatestMessage) = db.setLatestMessageasRead(message)

    fun UploadFile(inputStream: InputStream) = db.uploadFile(inputStream)

    fun fetchUsers() = db.fetchusers()

    fun QueryChatMessages(fromid: String, toid: String) = db.getMessages(fromid, toid)

    fun sendMessage(messagetxt: String, fromid: String, toid: String) = db.sendMessage(fromid = fromid, toid = toid, messagetext = messagetxt)

    fun setLatestMessage(messagetxt: String, fromid: String, toid: String) = db.setLatestMessage(fromid = fromid, toid = toid, messagetext = messagetxt)

    fun createLatestMessageAsRead(messagetxt: String, fromid: String, toid: String) = db.createLatestMessageasRead(fromid = fromid, toid = toid, messagetext = messagetxt)

    fun sendImage(S3url: String, fromid: String, toid: String) = db.sendImage(S3url,fromid, toid)





}