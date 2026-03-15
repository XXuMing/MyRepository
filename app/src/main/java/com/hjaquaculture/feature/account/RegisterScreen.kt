package com.hjaquaculture.feature.account

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.hjaquaculture.R
import com.hjaquaculture.feature.AuthAction
import com.hjaquaculture.ui.theme.HJAquacultureTheme

@Preview(showBackground = true)
@Composable
fun RegisterPreview(){
    HJAquacultureTheme() {
        RegisterScreen(hiltViewModel(),{})
    }
}
@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = hiltViewModel(),
    onAction: (AuthAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var accountName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rePassword by remember { mutableStateOf("") }

    val navController = rememberNavController()
    val titleColor = if (isSystemInDarkTheme()) Color.White else Color.Black
    var passwordVisibility by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize().imePadding(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "注册新账号", color = titleColor, fontSize = 40.sp)
            Spacer(modifier = Modifier.height(16.dp))
            //注册页面 用户名 文本框
            OutlinedTextField(
                value = accountName, label = { Text("用户名") },
                onValueChange = {
                    accountName = it
                    viewModel.onUsernameChanged(it)
                },
                singleLine = true,
                leadingIcon = { Icon(Icons.Filled.AccountBox, "用户名") }
            )
            //注册页面 密码 文本框
            OutlinedTextField(
                value = password, label = { Text("密码") },
                onValueChange = {
                    password = it
                    viewModel.onPasswordChanged(it)
                },
                placeholder = { Text("6~32位,字母/数字/符号") },
                singleLine = true,
                leadingIcon = { Icon(painterResource(R.drawable.lock_24px), "密码") },
                trailingIcon = {
                    if (passwordVisibility)
                        Icon(painterResource(R.drawable.visibility_24px), "显示密码")
                    else
                        Icon(painterResource(R.drawable.visibility_off_24px), "显示密码")
                }
            )
            //注册页面 确认密码 文本框
            OutlinedTextField(
                value = rePassword, label = { Text("确认密码") },
                onValueChange = {
                    rePassword = it
                    viewModel.onRePasswordChanged(it)
                },
                placeholder = { Text("6~32位,字母/数字/符号") },
                singleLine = true,
                leadingIcon = { Icon(painterResource(R.drawable.lock_24px), "确认密码") }
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.onRegisterClick()
                },
                enabled = state.actionStatus !is RegisterStatus.Loading
            ) {
                Text("注册", modifier = Modifier.padding(horizontal = 32.dp))
            }
            Spacer(modifier = Modifier.height(16.dp))
            when (val status = state.actionStatus) {
                is RegisterStatus.Loading -> {
                    //CircularProgressIndicator() // 显示加载圈
                }
                is RegisterStatus.Error -> {
                    Text(text = status.message, color = Color.Red) // 显示错误文案
                }
                is RegisterStatus.Success -> {
                    LocalSoftwareKeyboardController.current?.hide()
                    onAction(AuthAction.RegisterSuccess(state.username))
                }
                RegisterStatus.Idle -> {
                    // 闲置状态，可以显示提示语或者什么都不显示
                }
            }
        }
    }
}