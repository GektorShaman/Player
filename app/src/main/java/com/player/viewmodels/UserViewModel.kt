package com.player.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.player.data.LoginStateRepository
import com.player.data.UserRepository
import com.player.data.database.User
import kotlinx.coroutines.launch

enum class LoginResponse {
    WAITING_FOR_DATA,
    SUCCESSFUL,
    NOT_SUCCESSFUL,
}

class UserViewModel(private val userRepository: UserRepository, val loginStateRepository: LoginStateRepository): ViewModel() {
    private val _isLoginSuccessful = MutableLiveData(LoginResponse.WAITING_FOR_DATA)
    val isLoginSuccessful: LiveData<LoginResponse>
        get() = _isLoginSuccessful

    fun registerUser(user: User) {
        viewModelScope.launch {
            userRepository.addUser(user)
        }
        loginStateRepository.saveLoginState(true)
    }

    fun tryLoginUser(login: String, password: String) {
        viewModelScope.launch {
            val user = userRepository.getUser(login)
            if (user.password == password) {
                loginStateRepository.saveLoginState(true)
                _isLoginSuccessful.postValue(LoginResponse.SUCCESSFUL)
            } else {
                _isLoginSuccessful.postValue(LoginResponse.NOT_SUCCESSFUL)
            }
        }
    }

    fun resetLoginResponse() = _isLoginSuccessful.postValue(LoginResponse.WAITING_FOR_DATA)

}