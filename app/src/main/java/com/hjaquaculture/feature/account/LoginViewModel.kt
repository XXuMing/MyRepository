package com.hjaquaculture.feature.account

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.hjaquaculture.data.repository.UserRepository
import com.hjaquaculture.feature.LoginRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


// 1. 定义登录行为的瞬时状态
sealed interface LoginStatus {
    /**
     * 登录状态的初始状态
     */
    object Idle : LoginStatus

    /**
     * 登录请求中
     */
    object Loading : LoginStatus

    /**
     * 注册成功
     */
    data class RegisterSuccess(val msg: String) : LoginStatus

    /**
     * 登录成功
     */
    data class LoginSuccess(val msg: String) : LoginStatus

    /**
     * 登录失败（如密码错误）
     */
    data class Error(val message: String) : LoginStatus
}

// 2. 定义整体 UI 状态
/**
 * 登录 UI 状态
 * @property account 用户名
 * @property password 密码
 * @property isPasswordVisible 密码是否可见
 * @property loginStatus 登录状态
 * @constructor 创建空登录 ui 状态
 */
@Immutable // 性能优化提示：标记该类不可变
data class LoginUiState(
    val account: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val loginStatus: LoginStatus = LoginStatus.Idle
)

/**
 * 登录 ViewModel
 * @property userRepository 用户仓库
 * @property savedStateHandle 导航参数
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // 从导航参数中获取注册传来的初始用户名
    // 自动将导航参数映射回 Data Class
    private val loginArgs = savedStateHandle.toRoute<LoginRoute>()

    private val _uiState = MutableStateFlow(
        LoginUiState(
            // 如果是从注册页跳过来的，这里会自动填充用户名
            account = loginArgs.initialUsername ?: ""
        )
    )
    val uiState = _uiState.asStateFlow()

    // 更新用户名
    fun onUsernameChanged(name: String) {
        _uiState.update { it.copy(account = name, loginStatus = LoginStatus.Idle) }
    }

    // 更新密码
    fun onPasswordChanged(pass: String) {
        _uiState.update { it.copy(password = pass, loginStatus = LoginStatus.Idle) }
    }

    // 执行登录
    fun login() {
        val current = _uiState.value
        if (current.account.isBlank() || current.password.isBlank()) {
            _uiState.update { it.copy(loginStatus = LoginStatus.Error("账号或密码不能为空")) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(loginStatus = LoginStatus.Loading) }

            val result = userRepository.login(current.account, current.password)
            /**
            _uiState.update { state ->
                if (result.isSuccess) {
                    state.copy(loginStatus = LoginStatus.Success("欢迎回来"))
                } else {
                    state.copy(loginStatus = LoginStatus.Error(result.message))
                }
            }
            */
        }
    }
}