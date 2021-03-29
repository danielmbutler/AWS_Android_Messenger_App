package com.dbtechprojects.awsmessenger.ui.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amplifyframework.datastore.generated.model.LatestMessage
import com.dbtechprojects.awsmessenger.R
import com.amplifyframework.datastore.generated.model.User
import com.dbtechprojects.awsmessenger.database.DatabaseHandler
import com.dbtechprojects.awsmessenger.util.Constants
import com.dbtechprojects.awsmessenger.util.ImageUtils
import kotlinx.android.synthetic.main.user_item.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LatestMessageAdapter(
    var items: List<LatestMessage>,
    var currentuser: User,
    private val context: Context,


    ): RecyclerView.Adapter<LatestMessageAdapter.UserViewHolder>() {

    private var onClickListener: OnClickListener? = null
    private var db = DatabaseHandler()


    inner class UserViewHolder(itemview: View): RecyclerView.ViewHolder(itemview)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent,false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val LatestMessage = items[position]

        if (LatestMessage.fromid != currentuser.id){

            //LatestMessage is not from current user setup recycler view accordingly

            GlobalScope.launch(Dispatchers.IO) {
                val user = db.getUserFromId(LatestMessage.fromid)
                withContext(Dispatchers.Main){

                    holder.itemView.user_item_usernametxt.text = user.username
                    if (user.profilePhotoUrl != null){
                        if (user.profilePhotoUrl.isNotEmpty()){
                            Log.d("UserAdapter", user.profilePhotoUrl)
                            ImageUtils().loadImage(context,holder.itemView.user_item_imageView, Constants.S3_LINK + user.profilePhotoUrl)
                        }
                    } else{
                        holder.itemView.user_item_imageView.setImageResource(R.drawable.ic_baseline_people_24) // load default image
                    }

                    holder.itemView.user_item_emailtxt.text = "${user.username}: ${LatestMessage.messageTxt}"
                    // convert unix timestamp string to readable
                    val sdf = java.text.SimpleDateFormat("h:mm a")
                    val date = java.util.Date(LatestMessage.timestamp.toLong() * 1000)
                    val datetxt = sdf.format(date)

                    holder.itemView.user_item_time.setText("$datetxt")

                    // setup notification if message is unread

                    if (LatestMessage.readReceipt == "unread"){
                        holder.itemView.user_item_notification.visibility = View.VISIBLE
                    }

                    holder.itemView.setOnClickListener {

                        if (onClickListener != null) {
                            onClickListener!!.onClick(position, user, LatestMessage, holder.itemView)
                        }
                    }
                }

            }
        } else {
            GlobalScope.launch(Dispatchers.IO) {
                val user = db.getUserFromId(LatestMessage.toid)
                withContext(Dispatchers.Main) {
                    holder.itemView.user_item_usernametxt.text = user.username
                    if (user.profilePhotoUrl != null){
                        if (user.profilePhotoUrl.isNotEmpty()){
                            Log.d("UserAdapter", user.profilePhotoUrl)
                            ImageUtils().loadImage(context,holder.itemView.user_item_imageView, Constants.S3_LINK + user.profilePhotoUrl)
                        }
                    } else{
                        holder.itemView.user_item_imageView.setImageResource(R.drawable.ic_baseline_people_24)
                    }

                    holder.itemView.user_item_emailtxt.text = "you: ${LatestMessage.messageTxt}"
                    val sdf = java.text.SimpleDateFormat("h:mm a")
                    val date = java.util.Date(LatestMessage.timestamp.toLong() * 1000)
                    val datetxt = sdf.format(date)
                    holder.itemView.user_item_time.setText("$datetxt")

                    holder.itemView.setOnClickListener {

                        if (onClickListener != null) {
                            onClickListener!!.onClick(position, user, LatestMessage, holder.itemView)
                        }
                    }
                    }
                }
            }

    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(position: Int, model: User, message: LatestMessage, view: View)
    }
}

