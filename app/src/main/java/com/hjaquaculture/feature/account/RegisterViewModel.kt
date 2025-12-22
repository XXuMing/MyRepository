package com.hjaquaculture.feature.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hjaquaculture.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    //var uiState by mutableStateOf(RegisterUiState())
    //private set

    // 1. 使用 StateFlow 代替 mutableStateOf
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun onRegisterClick(username: String, password: String,rePassword: String) {
        viewModelScope.launch {
            // 2. 使用 .update 方法，它是线程安全的
            _uiState.update { it.copy(isLoading = true) }

            val result = repository.registerUser(username, password)

            _uiState.update { currentState ->
                when (result) {
                    is UserRepository.RegisterResult.Success ->
                        currentState.copy(isLoading = false, message = "注册成功！")
                    is UserRepository.RegisterResult.AlreadyExists ->
                        currentState.copy(isLoading = false, message = "用户名已存在")
                    is UserRepository.RegisterResult.Error ->
                        currentState.copy(isLoading = false, message = "错误: ${result.message}")
                }
            }
        }
    }
}

data class RegisterUiState(
    val isLoading: Boolean = false,
    val message: String = ""
)