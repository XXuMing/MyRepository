package com.hjaquaculture.feature.home

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hjaquaculture.data.local.DatabaseHelper
import com.hjaquaculture.R
import com.hjaquaculture.ui.theme.HJAquacultureTheme

class HomeActivity : ComponentActivity() {

    val dbHelper = DatabaseHelper(this, "User.db", 1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HJAquacultureTheme {
                Box(
                    modifier = Modifier.fillMaxSize().imePadding(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = "欢迎使用", fontSize = 60.sp)
                        Spacer(modifier = Modifier.height(26.dp))
                        loginTextField()
                        Spacer(modifier = Modifier.height(16.dp))
                        passwordTextField()
                        Spacer(modifier = Modifier.height(16.dp))
                        loginButton()
                    }
                }
            }
        }

    }

    /**
     * 用户名输入框
     */
    @Preview
    @Composable
    fun loginTextField(){
        var text by remember{ mutableStateOf("")}
        TextField(
            value = text,
            onValueChange = {
                text = it
            },
            singleLine = true,
            leadingIcon = {
                Icon(Icons.Filled.AccountBox, "账户名")
            }
        )
    }

    /**
     * 密码输入框
     */
    @Preview
    @Composable
    fun passwordTextField(){
        var text by remember{mutableStateOf("")}
        var passwordHidden by remember{ mutableStateOf(true)}
        TextField(
            value = text,
            onValueChange = {
                text = it
            },
            singleLine = true,
            leadingIcon = {
                if(passwordHidden)
                    Icon(painterResource(R.drawable.password_2_off_24dp_e3e3e3_fill0_wght400_grad0_opsz24), "账户名")
                else
                    Icon(painterResource(R.drawable.password_2_24dp_e3e3e3_fill0_wght400_grad0_opsz24), "账户名")
            },
            trailingIcon = {
                IconButton(onClick = {
                    passwordHidden = !passwordHidden
                })
                {
                    if(passwordHidden)
                        Icon(painterResource(R.drawable.visibility_off_24dp_e3e3e3_fill0_wght400_grad0_opsz24), "显示密码")
                    else
                        Icon(painterResource(R.drawable.visibility_24dp_e3e3e3_fill0_wght400_grad0_opsz24), "显示密码")

                }
            },
            visualTransformation = if(passwordHidden){
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            }
        )
    }

    /**
     * 登录按钮
     */
    @Preview
    @Composable
    fun loginButton(){
        Button(
            onClick = {
                dbHelper.writableDatabase
            })
        {
            Spacer(modifier = Modifier.padding(start = 10.dp))
            //Text(text = "登录")
            //Spacer(modifier = Modifier.padding(start = 10.dp))
            Icon(painterResource(R.drawable.login_24dp_e3e3e3_fill0_wght400_grad0_opsz24),"登录")
            Spacer(modifier = Modifier.padding(start = 10.dp))
        }
    }
}
