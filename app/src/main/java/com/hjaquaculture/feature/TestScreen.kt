package com.hjaquaculture.feature

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview


@Preview(showBackground = true)
@Composable
fun TestScreenPreview(){
    TestScreen({})
}



@Composable
fun TestScreen(onAction: (AuthAction.Test) -> Unit){
    Column {
        Button({onAction(AuthAction.Test.Home)}) {
            Text("home")
        }
        Button({onAction(AuthAction.Test.TempSale)}) {
            Text("temp_Sale")
        }
        Button({onAction(AuthAction.Test.Filter01)}) {
            Text("filter_01")
        }
        Button({onAction(AuthAction.Test.SwipeToReveal01)}) {
            Text("swipe_to_reveal_01")
        }
        Button({onAction(AuthAction.Test.SwipeToReveal02)}) {
            Text("swipe_to_reveal_02")
        }
        Button({onAction(AuthAction.Test.AdaptiveApp)}) {
            Text("adaptive_app")
        }
        Button({onAction(AuthAction.Test.ThreePaneAdaptiveApp)}) {
            Text("three_pane_adaptive_app")
        }
        Button({onAction(AuthAction.Test.MyAdaptiveApp2)}) {
            Text("my_adaptive_app_2")
        }
        Button({onAction(AuthAction.Test.Sale)}) {
            Text("sale")
        }
        Button({onAction(AuthAction.Test.Sale3)}) {
            Text("sale_3")
        }

    }
}