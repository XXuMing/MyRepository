package com.hjaquaculture.data.local

import android.util.Log
import androidx.room.RoomDatabase
import androidx.room.withTransaction
import androidx.sqlite.db.SupportSQLiteDatabase
import com.hjaquaculture.data.local.entity.Customer
import com.hjaquaculture.data.local.entity.Product
import com.hjaquaculture.data.local.entity.ProductCategory
import com.hjaquaculture.data.local.entity.ProductPriceHistory
import com.hjaquaculture.data.local.entity.PurchaseInvoice
import com.hjaquaculture.data.local.entity.PurchaseOrder
import com.hjaquaculture.data.local.entity.PurchaseOrderItem
import com.hjaquaculture.data.local.entity.PurchasePayment
import com.hjaquaculture.data.local.entity.SaleInvoice
import com.hjaquaculture.data.local.entity.SaleOrder
import com.hjaquaculture.data.local.entity.SaleOrderItem
import com.hjaquaculture.data.local.entity.SalePayment
import com.hjaquaculture.data.local.entity.Supplier
import com.hjaquaculture.data.local.entity.User
import com.hjaquaculture.common.utils.PaymentMethods
import com.hjaquaculture.common.utils.PurchaseOrderType
import com.hjaquaculture.common.utils.SaleOrderStatus
import com.hjaquaculture.common.utils.SaleOrderType
import jakarta.inject.Inject
import jakarta.inject.Provider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * 数据库回调，预填充数据
 */
class DatabaseCallback @Inject constructor(
    // 使用 Provider 解决 Hilt 循环依赖问题
    private val databaseProvider: Provider<LocalDatabase>
) : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)

        // 开启协程执行耗时操作
        CoroutineScope(Dispatchers.IO).launch {
            // 预载初始数据
            val database = databaseProvider.get()
            preloadInitialData(database)
        }
    }


    suspend fun preloadInitialData(database : LocalDatabase){
        database.withTransaction {

        }
    }

}