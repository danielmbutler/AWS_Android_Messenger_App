package com.dbtechprojects.awsmessenger.util

import android.content.Context
import android.content.SharedPreferences

class SharedPref(context: Context) {

    // used for username in settings view
    val sharedPref = context.getSharedPreferences("Messenger", Context.MODE_PRIVATE)

}