package com.dbtechprojects.awsmessenger.ui.activities

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dbtechprojects.awsmessenger.database.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoginViewModel @ViewModelInject constructor(
        private val repository: Repository
) : ViewModel() {

    // LiveDataGetters

    fun getSessionValue() : LiveData<Boolean> {
        return repository.isSignedin()
    }

    fun getSignedInConfirmation(): LiveData<Boolean>{
        return repository.signInConfirmation()
    }

    // Execution Functions

    fun checkSessionValue(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.checkSession()
        }

    }

    fun signInUser(email: String, password: String){
        viewModelScope.launch(Dispatchers.IO) {
            repository.signIn(email, password)
        }
    }




}