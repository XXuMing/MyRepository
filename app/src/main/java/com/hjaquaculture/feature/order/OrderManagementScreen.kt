package com.hjaquaculture.feature.order

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.hjaquaculture.feature.AuthAction

@Composable
fun OrderManagementScreen(
    onAction: (AuthAction) -> Unit,
    scaffoldPadding: PaddingValues
) {
    Column(
        modifier = Modifier.padding(scaffoldPadding)
    ) {
        Text("Order Management Screen")
    }

}