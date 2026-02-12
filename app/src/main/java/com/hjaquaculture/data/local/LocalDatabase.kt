package com.hjaquaculture.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
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
import com.hjaquaculture.common.utils.InvoiceStatusConverter
import com.hjaquaculture.common.utils.PurchaseOrderConverter
import com.hjaquaculture.common.utils.SaleOrderConverter

@Database(
    entities = [
        User::class, Customer::class, Supplier::class,
        ProductCategory::class, Product::class, ProductPriceHistory::class,
        PurchaseOrder::class, PurchaseOrderItem::class,PurchaseInvoice::class, PurchasePayment::class,
        SaleOrder::class, SaleOrderItem::class, SaleInvoice::class, SalePayment::class
               ],
    version = 1,
    exportSchema = true
)
@TypeConverters(
    InvoiceStatusConverter::class,
    PurchaseOrderConverter::class,
    SaleOrderConverter::class
)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun customerDao(): CustomerDao
    abstract fun supplierDao(): SupplierDao
    abstract fun productCategoryDao(): ProductCategoryDao
    abstract fun productDao(): ProductDao
    abstract fun productPriceHistoryDao() : ProductPriceHistoryDao
    abstract fun purchaseOrderDao(): PurchaseOrderDao
    abstract fun purchaseOrderItemDao(): PurchaseOrderItemDao
    abstract fun purchaseInvoiceDao(): PurchaseInvoiceDao
    abstract fun purchasePaymentDao(): PurchasePaymentDao
    abstract fun saleOrderDao(): SaleOrderDao
    abstract fun saleOrderItemDao(): SaleOrderItemDao
    abstract fun saleInvoiceDao(): SaleInvoiceDao
    abstract fun salePaymentDao(): SalePaymentDao

}