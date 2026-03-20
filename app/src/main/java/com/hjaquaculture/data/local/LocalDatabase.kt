package com.hjaquaculture.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hjaquaculture.common.base.DeliveryMethodConverter
import com.hjaquaculture.common.base.InvoiceStatusConverter
import com.hjaquaculture.common.base.InvoiceSymbolConverters
import com.hjaquaculture.common.base.OrderStatusConverter
import com.hjaquaculture.common.base.OrderSymbolConverters
import com.hjaquaculture.common.base.PaymentMethodsConverter
import com.hjaquaculture.common.base.PeopleSymbolConverters
import com.hjaquaculture.common.base.StockChangeTypeConverters
import com.hjaquaculture.common.base.StockUnitCategoryConverter
import com.hjaquaculture.data.local.dao.CombinedInvoiceDao
import com.hjaquaculture.data.local.dao.CombinedOrderDao
import com.hjaquaculture.data.local.dao.CombinedPeopleDao
import com.hjaquaculture.data.local.dao.CustomerDao
import com.hjaquaculture.data.local.dao.MeasureUnitDao
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
import com.hjaquaculture.data.local.entity.CombinedInvoiceView
import com.hjaquaculture.data.local.entity.CombinedOrderView
import com.hjaquaculture.data.local.entity.CombinedPeopleView
import com.hjaquaculture.data.local.entity.CustomerEntity
import com.hjaquaculture.data.local.entity.InventoryEntity
import com.hjaquaculture.data.local.entity.InventoryLogEntity
import com.hjaquaculture.data.local.entity.MeasureUnitEntity
import com.hjaquaculture.data.local.entity.ProductEntity
import com.hjaquaculture.data.local.entity.ProductPriceHistoryEntity
import com.hjaquaculture.data.local.entity.ProductVarietyEntity
import com.hjaquaculture.data.local.entity.PurchaseInvoiceEntity
import com.hjaquaculture.data.local.entity.PurchaseOrderEntity
import com.hjaquaculture.data.local.entity.PurchaseOrderItemEntity
import com.hjaquaculture.data.local.entity.PurchasePaymentEntity
import com.hjaquaculture.data.local.entity.SaleInvoiceEntity
import com.hjaquaculture.data.local.entity.SaleOrderEntity
import com.hjaquaculture.data.local.entity.SaleOrderItemEntity
import com.hjaquaculture.data.local.entity.SalePaymentEntity
import com.hjaquaculture.data.local.entity.SupplierEntity
import com.hjaquaculture.data.local.entity.UserEntity

@Database(
    entities = [
        UserEntity::class, CustomerEntity::class, SupplierEntity::class, MeasureUnitEntity::class,
        ProductVarietyEntity::class, ProductEntity::class, ProductPriceHistoryEntity::class,
        PurchaseOrderEntity::class, PurchaseOrderItemEntity::class,PurchaseInvoiceEntity::class, PurchasePaymentEntity::class,
        SaleOrderEntity::class, SaleOrderItemEntity::class, SaleInvoiceEntity::class, SalePaymentEntity::class,
        InventoryEntity::class, InventoryLogEntity::class,
               ],
    views = [ CombinedInvoiceView::class, CombinedOrderView::class, CombinedPeopleView::class ],
    version = 1,
    exportSchema = true
)
@TypeConverters(
    OrderSymbolConverters::class,
    InvoiceSymbolConverters::class,
    PeopleSymbolConverters::class,
    StockChangeTypeConverters::class,
    StockUnitCategoryConverter::class,
    InvoiceStatusConverter::class,
    PaymentMethodsConverter::class,
    DeliveryMethodConverter::class,
    OrderStatusConverter::class,
)
abstract class LocalDatabase : RoomDatabase() {

    abstract fun measureUnitDao(): MeasureUnitDao
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
    abstract fun combinedInvoiceDao(): CombinedInvoiceDao
    abstract fun combinedOrderDao(): CombinedOrderDao
    abstract fun combinedPeopleDao(): CombinedPeopleDao



}