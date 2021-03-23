package com.dbtechprojects.awsmessenger.ui.dialog

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.dbtechprojects.awsmessenger.R
import com.dbtechprojects.awsmessenger.util.ImageUtils
import kotlinx.android.synthetic.main.sendimage_dialog.*

class SendImageDialog (): DialogFragment() {

    var listener: ImagePicker ? = null
    var mImageUri: Uri? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as ImagePicker
    }
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val fragement_container = container
        val rootview = layoutInflater.inflate(R.layout.sendimage_dialog,fragement_container, false)

        Log.d("SendImageDia", arguments.toString())
        val sendbutton  =   rootview.findViewById<Button>(R.id.sendImage_SendBTN)
        val cancelBTN   =   rootview.findViewById<Button>(R.id.sendimage_CancelBTN)
        val imageview   =   rootview.findViewById<ImageView>(R.id.sendimage_dialog_iv)

        if(arguments?.getString("image") != null){
            val myUri = Uri.parse(arguments?.getString("image"))
            mImageUri = myUri
            ImageUtils().loadImagefromuri(requireContext(), imageview ,myUri )
        }


        sendbutton.setOnClickListener {
            // pass uri back to chatlog activity
            mImageUri?.let { it1 -> listener?.displayimage(it1) }
            this.dismiss()
        }

        cancelBTN.setOnClickListener {
            //close helpDialog
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        }

        return rootview
    }

    //interface implemented in chatlog activity

   interface ImagePicker{
       fun displayimage(uri: Uri)
   }
}