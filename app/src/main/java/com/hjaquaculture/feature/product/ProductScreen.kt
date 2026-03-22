package com.hjaquaculture.feature.product

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.hjaquaculture.feature.AuthAction

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProductScreen(
    vm: ProductViewModel = hiltViewModel(),
    onAction: (AuthAction) -> Unit,
    scaffoldPadding: PaddingValues,
) {

}