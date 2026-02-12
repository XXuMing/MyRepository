package com.hjaquaculture.feature.account

import android.util.Log
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
import androidx.compose.material.icons.outlined.Add
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
import com.hjaquaculture.feature.AuthAction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onAction: (AuthAction.Login) -> Unit,
    modifier: Modifier = Modifier
) {
    val titleColor = if (isSystemInDarkTheme()) Color.White else Color.Black
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var text by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }

    val state by viewModel.uiState.collectAsState()

    // 状态机监听：一旦状态变为 Success，触发导航 Action
    LaunchedEffect(state.loginStatus) {
        if (state.loginStatus is LoginStatus.LoginSuccess) {
            onAction(AuthAction.Login.LoginSuccess)
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onAction(AuthAction.Login.GoToRegister)
                }
            ){
                Icon(Icons.Outlined.Add, contentDescription = "注册")
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
                    value = state.account,
                    label = {Text("用户名")},
                    onValueChange = {
                        viewModel.onUsernameChanged(it)
                    },
                    singleLine = true,
                    leadingIcon = {
                        Icon(Icons.Filled.AccountBox, "用户名")
                    }
                )


                OutlinedTextField(
                    value = state.password, label = {Text("密码")},
                    onValueChange = {
                        viewModel.onPasswordChanged(it)
                    },
                    singleLine = true,
                    leadingIcon = {
                        Icon(
                            painterResource(R.drawable.lock_24px),
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

                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { onAction(AuthAction.Login.LoginSuccess) }
                )
                {
                    Text(text = "登录", modifier = Modifier.padding(horizontal = 32.dp))
                }
                Button({onAction(AuthAction.Login.GotoTestScreen)}) {
                    Text("TestScreen")
                }
            }
        }
    }

}