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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.hjaquaculture.R
import com.hjaquaculture.feature.LoginAction

/*
@Composable
@Preview(showBackground = true)
fun LoginPreview(){
    HJAquacultureTheme() {
        LoginScreen(hiltViewModel(),{})
    }
}
*/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onAction: (LoginAction) -> Unit
) {
    val titleColor = if (isSystemInDarkTheme()) Color.White else Color.Black
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val state by viewModel.uiState.collectAsState()

    // 状态机监听：一旦状态变为 Success，触发导航 Action
    LaunchedEffect(state.loginStatus) {
        if (state.loginStatus is LoginStatus.Success) {
            onAction(LoginAction.LoginSuccess)
        }
    }


    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAction(LoginAction.GoToRegister) }
            ){
                Icon(Icons.Filled.Add, contentDescription = "注册")
            }
        }
    ) { contentPadding ->
        Box(
            modifier = Modifier.fillMaxSize().imePadding().padding(contentPadding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "欢迎使用", color = titleColor , fontSize = 70.sp)
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = state.username,
                    label = {Text("用户名")},
                    onValueChange = {
                        viewModel.onUsernameChanged(it)
                    },
                    singleLine = true,
                    leadingIcon = {
                        Icon(Icons.Filled.AccountBox, "用户名")
                    }
                )
                PasswordTextField()
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { onAction(LoginAction.LoginSuccess) })
                {
                    Text(text = "登录", modifier = Modifier.padding(horizontal = 32.dp))
                }
            }
        }
    }

}

/**
 * 密码输入框
 */
@Composable
fun PasswordTextField() {
    var text by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = text , label = {Text("密码")},
        onValueChange = {
            text = it
        },
        singleLine = true,
        leadingIcon = {
            Icon(
                painterResource(R.drawable.password_2_24px),
                "密码"
            )
        },
        trailingIcon = {
            IconButton(onClick = { passwordVisibility = !passwordVisibility })
            {
                if (passwordVisibility)
                    Icon(painterResource(R.drawable.visibility_24px),"显示密码")
                else
                    Icon(painterResource(R.drawable.visibility_off_24px), "显示密码")
            }
        },
        visualTransformation = if (passwordVisibility) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        }
    )
}