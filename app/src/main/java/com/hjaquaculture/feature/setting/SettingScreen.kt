package com.hjaquaculture.feature.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.hjaquaculture.feature.AuthAction

@Composable
fun SettingScreen(
    onAction: (AuthAction) -> Unit,
    scaffoldPadding: PaddingValues,
){
    Column(
        modifier = Modifier.padding(scaffoldPadding)
    ){
        Text(text = "Setting Screen", fontSize = 30.sp, textAlign = TextAlign.Center)
    }
}