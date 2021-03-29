package com.dbtechprojects.awsmessenger.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dbtechprojects.awsmessenger.database.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivityViewModel @ViewModelInject constructor(
        private val repository: Repository
) : ViewModel() {

    //LiveDataGetters

    fun getSessionValue() : LiveData<Boolean> {
        return repository.isSignedin()
    }


    //Execution Functions
    fun checkSessionValue(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.checkSession()
        }

    }


}