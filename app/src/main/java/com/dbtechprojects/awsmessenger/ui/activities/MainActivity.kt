package com.dbtechprojects.awsmessenger.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.amplifyframework.core.Amplify
import com.dbtechprojects.awsmessenger.R
import com.dbtechprojects.awsmessenger.ui.viewmodels.MainActivityViewModel
import com.dbtechprojects.awsmessenger.util.SharedPref
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainActivityViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
         checkSignIn()

        setupBottomNavView()


    }

    override fun onResume() {
        super.onResume()
        checkSignIn()
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

    fun checkSignIn(){

        viewModel.checkSessionValue()
        viewModel.getSessionValue().observe(this, Observer { check ->
            if (check){
                Log.d("MainActivity", Amplify.Auth.currentUser.username)
                val pref = SharedPref(this).sharedPref
                val edit = pref.edit()
                edit.putString("username", Amplify.Auth.currentUser.username)
                edit.apply()
            } else {
                redirect()
            }

        })
    }


}