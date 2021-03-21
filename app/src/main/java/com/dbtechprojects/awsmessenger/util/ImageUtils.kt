package com.dbtechprojects.awsmessenger.util

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.target.Target.SIZE_ORIGINAL

class ImageUtils(){

    fun loadImage(context: Context, ImageView: ImageView, url: String){
        Glide.with(context)
                .load(url)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .into(ImageView);
    }

    fun loadImagefromuri(context: Context, ImageView: ImageView, url: Uri){
        Glide.with(context)
            .load(url)
            .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
            .into(ImageView);
    }



}