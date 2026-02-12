package com.hjaquaculture.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hjaquaculture.data.local.LocalDatabase
import com.hjaquaculture.data.local.DatabaseCallback
import com.hjaquaculture.data.local.dao.CustomerDao
import com.hjaquaculture.data.local.dao.ProductCategoryDao
import com.hjaquaculture.data.local.dao.ProductDao
import com.hjaquaculture.data.local.dao.ProductPriceHistoryDao
import com.hjaquaculture.data.local.dao.PurchaseInvoiceDao
import com.hjaquaculture.data.local.dao.PurchaseOrderDao
import com.hjaquaculture.data.local.dao.PurchaseOrderItemDao
import com.hjaquaculture.data.local.dao.PurchasePaymentDao
import com.hjaquaculture.data.local.dao.SaleInvoiceDao
import com.hjaquaculture.data.local.dao.SaleOrderDao
import com.hjaquaculture.data.local.dao.SaleOrderItemDao
import com.hjaquaculture.data.local.dao.SalePaymentDao
import com.hjaquaculture.data.local.dao.SupplierDao
import com.hjaquaculture.data.local.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import java.util.concurrent.Executors

/**
 * Hilt 模块，提供数据库相关依赖
 */
@Module
@InstallIn(SingletonComponent::class) // 全局单例
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
        callback: DatabaseCallback // Hilt 会自动实例化上面的 Callback
    ): LocalDatabase {
        return Room.databaseBuilder(  // inMemoryDatabaseBuilder    databaseBuilder
            context,
            LocalDatabase::class.java,
            "HJA_database.db" // 生产环境建议用持久化存储
        )
        .setQueryCallback(RoomDatabase.QueryCallback { sqlQuery, bindArgs ->
            Log.d("RoomSQL", "SQL Query: $sqlQuery SQL Args: $bindArgs")
        }, Executors.newSingleThreadExecutor())
        //.addCallback(callback)
        .build()
    }

    @Provides
    fun provideUserDao (db: LocalDatabase): UserDao {
        return db.userDao()
    }

    @Provides
    fun provideCustomerDao (db: LocalDatabase): CustomerDao{
        return db.customerDao()
    }

    @Provides
    fun provideSupplierDao (db: LocalDatabase): SupplierDao {
        return db.supplierDao()
    }

    @Provides
    fun provideProductDao (db: LocalDatabase): ProductDao {
        return db.productDao()
    }

    @Provides
    fun provideProductCategoryDao (db: LocalDatabase): ProductCategoryDao {
        return db.productCategoryDao()
    }

    @Provides
    fun provideProductPriceHistoryDao (db: LocalDatabase): ProductPriceHistoryDao {
        return db.productPriceHistoryDao()
    }

    @Provides
    fun providePurchaseOrderDao (db: LocalDatabase): PurchaseOrderDao {
        return db.purchaseOrderDao()
    }

    @Provides
    fun providePurchaseOrderItemDao (db: LocalDatabase): PurchaseOrderItemDao {
        return db.purchaseOrderItemDao()
    }

    @Provides
    fun providePurchaseInvoiceDao (db: LocalDatabase): PurchaseInvoiceDao {
        return db.purchaseInvoiceDao()
    }

    @Provides
    fun providePurchasePaymentDao (db: LocalDatabase): PurchasePaymentDao {
        return db.purchasePaymentDao()
    }

    @Provides
    fun provideSaleOrderDao (db: LocalDatabase): SaleOrderDao {
        return db.saleOrderDao()
    }

    @Provides
    fun provideSaleOrderItemDao (db: LocalDatabase): SaleOrderItemDao {
        return db.saleOrderItemDao()
    }

    @Provides
    fun provideSaleInvoiceDao (db: LocalDatabase): SaleInvoiceDao {
        return db.saleInvoiceDao()
    }

    @Provides
    fun provideSalePaymentDao (db: LocalDatabase): SalePaymentDao {
        return db.salePaymentDao()
    }
}