package com.hjaquaculture.feature.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@Composable
fun SettingScreen(
    modifier: Modifier = Modifier
){
    Column{
        Text(text = "Setting Screen", fontSize = 70.sp, textAlign = TextAlign.Center)
    }
}