package com.dbtechprojects.awsmessenger.ui.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.amplifyframework.core.Amplify
import com.amplifyframework.core.model.query.Where
import com.amplifyframework.datastore.generated.model.User
import com.dbtechprojects.awsmessenger.R
import com.dbtechprojects.awsmessenger.database.AmplifyAuth
import com.dbtechprojects.awsmessenger.database.DatabaseHandler
import com.dbtechprojects.awsmessenger.util.ImageUtils
import com.dbtechprojects.awsmessenger.util.SharedPref
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        GlobalScope.launch(Dispatchers.IO) { checkSignIn() }

        setupBottomNavView()


    }

    override fun onResume() {
        super.onResume()
        GlobalScope.launch(Dispatchers.IO) { checkSignIn() }
    }

    private fun setupBottomNavView(){
        val navView: BottomNavigationView = findViewById(R.id.Bottomnavigation)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.Menu_Item_UsersFragment,
                R.id.Menu_Item_MessagesFragment,
                R.id.Menu_Item_SettingsFragment
            )
        )
        //setupActionBarWithNavController(navController, appBarConfiguration)

        navView.setupWithNavController(navController)
    }

    private fun redirect(){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    suspend fun checkSignIn(){

        val check = AmplifyAuth().checkSignIn()
        if (check){
            Log.d("MainActivity", Amplify.Auth.currentUser.username)
            val pref = SharedPref(this).sharedPref
            val edit = pref.edit()
            edit.putString("username", Amplify.Auth.currentUser.username)
            edit.apply()
        } else {
            redirect()
        }

    }


}