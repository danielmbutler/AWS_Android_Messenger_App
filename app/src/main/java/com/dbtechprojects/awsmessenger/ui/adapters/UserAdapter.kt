package com.dbtechprojects.awsmessenger.ui.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dbtechprojects.awsmessenger.R
import com.amplifyframework.datastore.generated.model.User
import com.dbtechprojects.awsmessenger.util.Constants
import com.dbtechprojects.awsmessenger.util.ImageUtils
import kotlinx.android.synthetic.main.user_item.view.*

class UserAdapter(
    var items: List<User>,
    private val context: Context,

    ): RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private var onClickListener: OnClickListener? = null

    inner class UserViewHolder(itemview: View): RecyclerView.ViewHolder(itemview)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent,false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val User = items[position]

        holder.itemView.user_item_emailtxt.text = User.email
        holder.itemView.user_item_usernametxt.text = User.username
        holder.itemView.user_item_time.text = ""

        if (User.profilePhotoUrl != null){
            if (User.profilePhotoUrl.isNotEmpty()){
                Log.d("UserAdapter", User.profilePhotoUrl)
                ImageUtils().loadImage(context,holder.itemView.user_item_imageView, Constants.S3_LINK + User.profilePhotoUrl)
            }
        } else {
            // s3 profile pic link is empty then load default image
            holder.itemView.user_item_imageView.setImageResource(R.drawable.ic_baseline_people_24)
        }




        holder.itemView.setOnClickListener {

            if (onClickListener != null) {
                onClickListener!!.onClick(position, User, holder.itemView)
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
        fun onClick(position: Int, model: com.amplifyframework.datastore.generated.model.User, view: View)
    }
}

