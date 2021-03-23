package com.dbtechprojects.awsmessenger.ui.activities

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.ChatMessage
import com.amplifyframework.datastore.generated.model.LatestMessage
import com.amplifyframework.datastore.generated.model.User
import com.dbtechprojects.awsmessenger.R
import com.dbtechprojects.awsmessenger.database.AmplifyAuth
import com.dbtechprojects.awsmessenger.database.DatabaseHandler
import com.dbtechprojects.awsmessenger.database.DatabaseListener
import com.dbtechprojects.awsmessenger.database.entitites.LocalUserModel
import com.dbtechprojects.awsmessenger.ui.adapters.LatestMessageAdapter
import com.dbtechprojects.awsmessenger.ui.dialog.SendImageDialog
import com.dbtechprojects.awsmessenger.ui.fragments.MessagesViewModel
import com.dbtechprojects.awsmessenger.util.Constants
import com.dbtechprojects.awsmessenger.util.ImageUtils
import com.dbtechprojects.awsmessenger.util.Mapper
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.chat_from_row.view.imageViewFromUser
import kotlinx.android.synthetic.main.chat_from_row.view.textView
import kotlinx.android.synthetic.main.chat_from_row.view.textView_from_row_time
import kotlinx.android.synthetic.main.chat_from_row_with_image.view.*
import kotlinx.android.synthetic.main.chat_from_row_with_image.view.chat_from_Row_iv
import kotlinx.android.synthetic.main.chat_to_row.view.*
import kotlinx.android.synthetic.main.chat_to_row_with_image.view.*
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.util.*

@AndroidEntryPoint
class ChatLogActivity : AppCompatActivity(), SendImageDialog.ImagePicker {

    private val viewModel: ChatLogViewModel by viewModels()
    private var fromUser: User? = null
    private var ToUser: User? = null
    private var auth = AmplifyAuth()
    private var latestchatmessage: ChatMessage? = null
    val adapter = GroupAdapter<GroupieViewHolder>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        if (intent.hasExtra("user")) {

            // getting to and from user details

            val user = intent.getParcelableExtra<LocalUserModel>("user")
            val awsuser = Mapper().LocalUserToAwsUser(user!!)
            ToUser = awsuser
            Log.d("ChatLog", awsuser.email)
            ChatLog_UserNameTv.text = awsuser.username
        }



        recyclerview_chatlog.adapter = adapter

        GlobalScope.launch(Dispatchers.Main) {
            fromUser = viewModel.getLoggedInUserObject()

            val messages = viewModel.getChatMessages(fromid = fromUser!!.id, toid = ToUser!!.id)

            messages.forEach(){
                setuprv(it)
            }
            Log.d("chatlog", messages.toString())
            val user = viewModel.getLoggedInUserObject()
            fromUser = user
            val dblistener = DatabaseListener()
            dblistener.listenformessages(ToUser!!.id, fromUser!!.id)
            dblistener.message.observe(this@ChatLogActivity, androidx.lifecycle.Observer {

                setuprv(it)
                latestchatmessage = it
            })

        }

        // starts chat from bottom
        val layoutManager = LinearLayoutManager(this)
        layoutManager.stackFromEnd = true
        recyclerview_chatlog.layoutManager = layoutManager

        // pushes up recycler view when softkeyboard popups up
        recyclerview_chatlog.addOnLayoutChangeListener { view, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            if (bottom < oldBottom) {
                recyclerview_chatlog.postDelayed(Runnable {
                    recyclerview_chatlog.scrollToPosition(
                        recyclerview_chatlog.adapter!!.itemCount - 1
                    )
                }, 100)
            }
        }

        send_button_chatlog.setOnClickListener {
            performSendMessage()
        }

        chatlog_camera_iv.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                    == PackageManager.PERMISSION_GRANTED

            )
            {
                showImageChooser()
            } else {
                /*Requests permissions to be granted to this application. These permissions
                 must be requested in your manifest, they should not be granted to your app,
                 and they should have protection level*/
                ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        3
                )
            }
        }



    }

    private fun showImageChooser() {
        // An intent for launching the image selection of phone storage.
        val galleryIntent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        // Launches the image selection of phone storage using the constant code.
        this.startActivityForResult(galleryIntent, 2)
    }

    //override image chooser

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 2) {
                if (data != null) {
                    try {

                        // The uri of selected image from phone storage.
                        val uri =  data.data!!

                        // pass uri to dialog
                        var bundle = Bundle()
                        bundle.putString("image",uri.toString())

                        val dialog =
                                SendImageDialog()
                        dialog.arguments = bundle
                        dialog.show(supportFragmentManager, "Help")


                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            // A log is printed when user close or cancel the image selection.
            Log.e("Request Cancelled", "Image selection cancelled")
        }
    }

    private fun sendImageMessage(imagekey: String){

        val fromId = fromUser!!.id // current logged in user
        val toId = ToUser?.id


        GlobalScope.launch(Dispatchers.Main) {
            viewModel.SendImage(imagekey,fromId,toId!!)

            if (fromId != toId){
                adapter.add(
                        ChatToImageItem(
                                imagekey,
                                (System.currentTimeMillis() / 1000).toString(),
                                fromUser!!,
                                this@ChatLogActivity
                        )
                )
                // scroll to bottom
                recyclerview_chatlog.scrollToPosition(adapter.itemCount - 1)
            }

        }


    }

    private fun performSendMessage() {


        val text = editText_chatlog.text.toString()
        val user = intent.getParcelableExtra<LocalUserModel>("user")
        val fromId = fromUser!!.id // current logged in user
        val toId = user!!.id

        //message details
        Log.d("ChatActivity", "text: $text, user: $user, fromid: $fromId, toid: $toId")

        // send to Aws

        GlobalScope.launch(Dispatchers.Main) {
            viewModel.SendMessage(messagetxt = text, fromid = fromId, toid = toId!! )
            // add chat item to chat if user is not logged in user (creates duplicates)

            if (fromId != toId){
                adapter.add(
                    ChatToItem(
                        text,
                        (System.currentTimeMillis() / 1000).toString(),
                        fromUser!!,
                        this@ChatLogActivity
                    )
                )
                // scroll to bottom
                recyclerview_chatlog.scrollToPosition(adapter.itemCount - 1)
            }

        }

        editText_chatlog.setText("")

    }


    class ChatFromItem(val text: String,val timestamp: String, val user: User, val context: Context) :
        Item<GroupieViewHolder>() {
        override fun bind(viewHolder: GroupieViewHolder, position: Int) {
            viewHolder.itemView.textView.text = text

            //load user image into chat
            val uri = user.profilePhotoUrl
            val targetImageView = viewHolder.itemView.imageViewFromUser
            ImageUtils().loadImage(context, targetImageView,
        Constants.S3_LINK + uri)

            // convert unix timestamp to readable string

            val sdf = java.text.SimpleDateFormat("h:mm a")
            val date = java.util.Date(timestamp.toLong() * 1000)
            val datetxt = sdf.format(date)


            viewHolder.itemView.textView_from_row_time.setText("$datetxt")


        }

        override fun getLayout(): Int {
            return R.layout.chat_from_row
        }
    }

    class ChatToItem(val text: String, val timestamp: String, val user: User, val context: Context) :
        Item<GroupieViewHolder>() {
        override fun bind(viewHolder: GroupieViewHolder, position: Int) {
            viewHolder.itemView.textView.text = text

            //load user image into chat
            val uri = user.profilePhotoUrl
            val targetImageView = viewHolder.itemView.imageViewChatToRow
            ImageUtils().loadImage(context, targetImageView,
                Constants.S3_LINK + uri)
            val sdf = java.text.SimpleDateFormat("h:mm a")
            val date = java.util.Date(timestamp.toLong() * 1000)
            val datetxt = sdf.format(date)
            Log.d("ChatlogRv" , "timestamp: $timestamp")
            Log.d("ChatlogRv" , "firstconvert: ${date.toString()}")
            Log.d("ChatlogRv" , "secondconvert: $datetxt")
            viewHolder.itemView.textView_to_row_time.setText("$datetxt")

        }

        override fun getLayout(): Int {
            return R.layout.chat_to_row
        }


    }
    class ChatToImageItem(val imagekey: String, val timestamp: String, val user: User, val context: Context) :
            Item<GroupieViewHolder>() {
        override fun bind(viewHolder: GroupieViewHolder, position: Int) {

            // load sent image into chat
            val ImageUri = imagekey
            val ImageView = viewHolder.itemView.chat_to_Row_iv
            ImageUtils().loadImage(context, ImageView,
                    Constants.S3_LINK + ImageUri)

            //load user image into chat

            val sdf = java.text.SimpleDateFormat("h:mm a")
            val date = java.util.Date(timestamp.toLong() * 1000)
            val datetxt = sdf.format(date)
            Log.d("ChatlogRv" , "timestamp: $timestamp")
            Log.d("ChatlogRv" , "firstconvert: ${date.toString()}")
            Log.d("ChatlogRv" , "secondconvert: $datetxt")
            viewHolder.itemView.textView_to_row_image_time.setText("$datetxt")

        }

        override fun getLayout(): Int {
            return R.layout.chat_to_row_with_image
        }


    }
    class ChatFromImageItem(val imagekey: String,val timestamp: String, val user: User, val context: Context) :
            Item<GroupieViewHolder>() {
        override fun bind(viewHolder: GroupieViewHolder, position: Int) {

            // load sent image into chat
            val ImageUri = imagekey
            val ImageView = viewHolder.itemView.chat_from_Row_iv
            ImageUtils().loadImage(context, ImageView,
                    Constants.S3_LINK + ImageUri)


            // convert unix timestamp to readable string

            val sdf = java.text.SimpleDateFormat("h:mm a")
            val date = java.util.Date(timestamp.toLong() * 1000)
            val datetxt = sdf.format(date)


            viewHolder.itemView.textView_from_row_image_time.setText("$datetxt")


        }

        override fun getLayout(): Int {
            return R.layout.chat_from_row_with_image
        }
    }



    private fun setuprv(chatMessage: ChatMessage){

        if (chatMessage.fromid == fromUser!!.id) {
            if (chatMessage.hasImage != null){
                adapter.add(
                    ChatToImageItem(
                        chatMessage.imageUrl,
                        chatMessage.timestamp.toString(),
                        fromUser!!,
                        this@ChatLogActivity
                    )
                )
                recyclerview_chatlog.scrollToPosition(adapter.itemCount - 1)
            } else{
                adapter.add(
                        ChatToItem(
                                chatMessage.messageTxt,
                                chatMessage.timestamp.toString(),
                                fromUser!!,
                                this@ChatLogActivity
                        )
                )
                recyclerview_chatlog.scrollToPosition(adapter.itemCount - 1)
            }


        } else {

            if (chatMessage.hasImage != null ){
                adapter.add(
                        ChatFromImageItem(
                            chatMessage.imageUrl,
                            chatMessage.timestamp.toString(),
                            ToUser!!,
                            this@ChatLogActivity
                        )
                )
                recyclerview_chatlog.scrollToPosition(adapter.itemCount - 1)
            } else{
                adapter.add(
                        ChatFromItem(
                                chatMessage.messageTxt,
                                chatMessage.timestamp.toString(),
                                ToUser!!,
                                this@ChatLogActivity
                        )

                )
                recyclerview_chatlog.scrollToPosition(adapter.itemCount - 1)
            }

        }
    }

private fun setLatestMessageStatus(messagetxt: String,fromid: String, toid: String){
   // if message being received in chat log is not from the logged in user and the logged in user is
    // in the chat log activity then we want to set the message read receipt as read.
    if (fromid != fromUser?.id){
        val test = getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)
        if (test == true){
            // user is observing chat activity lets set the latest message to read
            GlobalScope.launch(Dispatchers.Main) {
               viewModel.CreateLatestMessageAsRead(messagetxt = messagetxt, fromid = fromid, toid = toid)
            }

        }
    }

}


    // set latest message on exit of chatlog activity
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (latestchatmessage != null){
            setLatestMessageStatus(messagetxt = latestchatmessage!!.messageTxt,
                toid = latestchatmessage!!.toid, fromid = latestchatmessage!!.fromid)
        }
    }


    //from dialog
    override fun displayimage(uri: Uri) {
        Log.e("MainActivity ",uri.toString())

        // send file picked from picker for upload and get ImageKey
        val inputstream = this.contentResolver?.openInputStream(uri)
        GlobalScope.launch(Dispatchers.IO) {

            val imagekey = viewModel.UploadFile(inputstream!!)
            if (imagekey.isNotEmpty()){
                sendImageMessage(imagekey)
            }
        }
    }

}


