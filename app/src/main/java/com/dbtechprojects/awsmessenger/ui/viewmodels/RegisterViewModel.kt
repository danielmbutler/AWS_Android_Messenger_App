package com.dbtechprojects.awsmessenger.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dbtechprojects.awsmessenger.database.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterViewModel @ViewModelInject constructor(
        private val repository: Repository
) : ViewModel() {

    // LiveDataGetters

    fun getSignUpValue(): LiveData<Boolean>{
        return repository.getSignUpValue()
    }

    fun getValidationCodeValue(): LiveData<Boolean>{
        return repository.getValidationCodeCheckValue()
    }

    fun getUserCreatedValue(): MutableLiveData<Boolean> {
        return repository.getUserCreatedValue()
    }

    // Execution Functions


    fun signUpUser(email: String, username: String, password: String){
        viewModelScope.launch(Dispatchers.IO) {
            repository.signUpUser(email,username,password)
        }
    }

    fun validatecode(username: String, code: String){
        viewModelScope.launch(Dispatchers.IO) {
            repository.validateConfirmationCode(username, code)
        }
    }

    fun createUser(username: String, email: String){
        viewModelScope.launch(Dispatchers.IO) {
            repository.createUser(username, email)
        }
    }
}