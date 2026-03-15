package com.hjaquaculture.feature.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hjaquaculture.data.local.repository.UserRepository
import com.hjaquaculture.feature.home.RegisterResult
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// 1. 定义动作状态（瞬时）
sealed interface RegisterStatus {
    object Idle : RegisterStatus                      // 空闲状态（初始状态）
    object Loading : RegisterStatus                   // 加载中
    data class Success(val message: String) : RegisterStatus // 注册成功
    data class Error(val message: String) : RegisterStatus   // 注册失败/验证失败
}
// 2. 定义整体 UI 状态（持有所有输入数据）
data class RegisterUiState(
    val username: String = "",
    val password: String = "",
    val rePassword: String = "",
    val actionStatus: RegisterStatus = RegisterStatus.Idle // 初始为闲置
)

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    // 处理输入框文字变化
    fun onUsernameChanged(newValue: String) {
        _uiState.update { it.copy(username = newValue) }
    }

    fun onPasswordChanged(newValue: String) {
        _uiState.update { it.copy(password = newValue) }
    }

    fun onRePasswordChanged(newValue: String) {
        _uiState.update { it.copy(rePassword = newValue) }
    }

    fun onRegisterClick() {
        val current = _uiState.value

        // 1. 首先进行输入验证
        val validationError = getValidationError(current.username, current.password, current.rePassword)
        if (validationError != null) {
            _uiState.update { it.copy(actionStatus = RegisterStatus.Error(validationError)) }
            return
        }

        viewModelScope.launch {
            // 切换状态为 Loading，此时 username 等数据依然保留在 state 中
            _uiState.update { it.copy(actionStatus = RegisterStatus.Loading) }

            val result = repository.registerUser(current.username, current.password)

            _uiState.update { state ->
                when (result) {
                    is RegisterResult.Success ->
                        state.copy(actionStatus = RegisterStatus.Success("注册成功"))
                    is RegisterResult.AlreadyExists ->
                        state.copy(actionStatus = RegisterStatus.Error("用户名已存在"))
                    is RegisterResult.Error ->
                        state.copy(actionStatus = RegisterStatus.Error("注册失败"))
                }
            }
        }

    }
/*
    fun onMessageShown() {
        _uiState.update { it.copy(message = "") }
    }
*/
    /**
     * 内部验证逻辑，返回错误信息，若为null则通过
     */
    private fun getValidationError( username: String, password: String, rePassword: String ): String? {
        return when {
            username.isBlank() || password.isBlank() || rePassword.isBlank() -> "输入框内不能为空"
            password.length !in 6..32 -> "密码长度需在6~32位之间"
            password != rePassword -> "两次输入的密码不一致"
            else -> null
        }
    }
}
