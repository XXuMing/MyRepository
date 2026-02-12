package com.hjaquaculture.common.utils

import com.hjaquaculture.feature.AuthAction
import com.hjaquaculture.feature.Screen
import jakarta.inject.Inject
import jakarta.inject.Singleton
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

/**
 * 为了在MainActivity当中实现点击跳转前判断当前页面与目标页面是否一致，再进行跳转.
 * 由于当前页面是路由类，目标页面是AuthAction类，所以需要映射转换.
 */
fun AuthAction.Global.toScreen(): Any {
    return when (this) {
        AuthAction.Global.Back -> Screen.Login
        AuthAction.Global.Reload -> Screen.Login
        AuthAction.Global.Home -> Screen.Home
        AuthAction.Global.Sale -> Screen.Sale
        AuthAction.Global.Purchase -> Screen.Purchase
        //AuthAction.Global.InvoiceManage -> Screen.Invoice
        AuthAction.Global.SaleInvoice -> Screen.SaleInvoice
        AuthAction.Global.PurchaseInvoice -> Screen.PurchaseInvoice
        AuthAction.Global.PriceManage -> Screen.PriceManage
        AuthAction.Global.ProductManage -> Screen.Product
        AuthAction.Global.RelationshipManage -> Screen.Relationship
        AuthAction.Global.Setting -> Screen.Setting
    }
}