package com.hjaquaculture.di

import android.app.Application
import android.util.Log
import com.hjaquaculture.common.utils.DeliveryMethod
import com.hjaquaculture.common.utils.InvoiceStatus
import com.hjaquaculture.common.utils.OrderStatus
import com.hjaquaculture.common.utils.PaymentMethods
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
import com.hjaquaculture.data.local.entity.CustomerEntity
import com.hjaquaculture.data.local.entity.MeasureUnitEntity
import com.hjaquaculture.data.local.entity.ProductCategoryEntity
import com.hjaquaculture.data.local.entity.ProductEntity
import com.hjaquaculture.data.local.entity.ProductPriceHistoryEntity
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
import com.hjaquaculture.data.local.repository.PurchaseInvoiceRepository
import com.hjaquaculture.data.local.repository.PurchaseOrderRepository
import com.hjaquaculture.data.local.repository.PurchasePaymentRepository
import com.hjaquaculture.data.local.repository.SaleInvoiceRepository
import com.hjaquaculture.data.local.repository.SaleOrderRepository
import com.hjaquaculture.data.local.repository.SalePaymentRepository
import dagger.hilt.android.HiltAndroidApp
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

@HiltAndroidApp
class App : Application(){

    // 这里的大量注入会导致性能下降，正式版需要注释这些内容。
    @Inject
    lateinit var userDao: UserDao
    @Inject
    lateinit var customerDao: CustomerDao
    @Inject
    lateinit var supplierDao: SupplierDao

    @Inject
    lateinit var measureUnitDao: MeasureUnitDao
    @Inject
    lateinit var productDao: ProductDao
    @Inject
    lateinit var productCategoryDao: ProductCategoryDao
    @Inject
    lateinit var productPriceHistoryDao: ProductPriceHistoryDao
    @Inject
    lateinit var saleOrderDao: SaleOrderDao
    @Inject
    lateinit var saleOrderItemDao: SaleOrderItemDao
    @Inject
    lateinit var saleInvoiceDao: SaleInvoiceDao
    @Inject
    lateinit var salePaymentDao: SalePaymentDao
    @Inject
    lateinit var purchaseOrderDao: PurchaseOrderDao
    @Inject
    lateinit var purchaseOrderItemDao: PurchaseOrderItemDao
    @Inject
    lateinit var purchaseInvoiceDao: PurchaseInvoiceDao
    @Inject
    lateinit var purchasePaymentDao: PurchasePaymentDao
    @Inject
    lateinit var combinedInvoiceDao: CombinedInvoiceDao
    @Inject
    lateinit var combinedOrderDao: CombinedOrderDao
    @Inject
    lateinit var combinedPeopleDao: CombinedPeopleDao
    @Inject
    lateinit var saleOrderRepository: SaleOrderRepository
    @Inject
    lateinit var saleInvoiceRepository: SaleInvoiceRepository
    @Inject
    lateinit var salePaymentRepository: SalePaymentRepository
    @Inject
    lateinit var purchaseOrderRepository: PurchaseOrderRepository
    @Inject
    lateinit var purchaseInvoiceRepository: PurchaseInvoiceRepository
    @Inject
    lateinit var purchasePaymentRepository: PurchasePaymentRepository

    /**
     * 在应用程序启动时执行初始化操作
     */
    override fun onCreate() {
        super.onCreate()
        // 启动一个协程去检查并填充数据
        MainScope().launch(Dispatchers.IO) {
            initDefaultData()
        }
    }

    /**
     * 初始化默认数据
     */
    private suspend fun initDefaultData() {
        if(measureUnitDao.getCount() == 0){
            val unitEntities = listOf(
                MeasureUnitEntity(
                    id = 1,
                    name = "件",
                    category = "数量",
                    conversionRate = 1.0,
                    precision = 0,
                    isBase = true,
                    sort = 0
                ),
                MeasureUnitEntity(id = 2, name = "箱", category = "数量", conversionRate = 10.0, precision = 0, isBase = false, sort = 1),
                MeasureUnitEntity(id = 3, name = "袋", category = "数量", conversionRate = 100.0, precision = 0, isBase = false, sort = 2),
                MeasureUnitEntity(id = 4, name = "斤", category = "重量", conversionRate = 1.0, precision = 0, isBase = true, sort = 0),
                MeasureUnitEntity(id = 5, name = "公斤", category = "重量", conversionRate = 1000.0, precision = 0, isBase = false, sort = 1),
                MeasureUnitEntity(id = 6, name = "吨", category = "重量", conversionRate = 1000000.0, precision = 0, isBase = false, sort = 2),
            )
            measureUnitDao.insertAll(unitEntities)
            Log.d("DB_Callback", "Unit测试数据填充完毕")
        }


        // 调用 Repository 的方法，它会自动处理事务和单据号生成
        // 记得在这里加个判断，防止每次启动都插入
        if(userDao.getCount() == 0) {
            for(i in 1..5){
                val userEntity = UserEntity(account ="测试用户名$i",name="测试用户名$i", passwordHash = "123456", role = "test_user")
                userDao.insert(userEntity)
            }
            Log.d("DB_Callback", "User测试数据填充完毕")
        }

        if(customerDao.getCount() == 0) {
            for(i in 1..5){
                val customerEntity = CustomerEntity( name="测试客户$i", nickName =  "测试客户昵称$i", passwordHash = "123456", phone = "1334567890$i")
                customerDao.insert(customerEntity)
            }
            Log.d("DB_Callback", "Customer测试数据填充完毕")
        }

        if(supplierDao.getCount() == 0) {
            for (i in 1..5) {
                val supplierEntity = SupplierEntity(name="测试供应商$i", phone = "1334567890$i")
                supplierDao.insert(supplierEntity)
            }
            Log.d("DB_Callback", "Supplier测试数据填充完毕")
        }
        if(productCategoryDao.getCount() == 0) {
            for (i in 1..5) {
                val productCategory = ProductCategoryEntity(name = "测试分类$i")
                productCategoryDao.insert(productCategory)
            }
            Log.d("DB_Callback", "ProductCategory测试数据填充完毕")
        }

        if(productDao.getCount() == 0) {
            var num : Int = 1
            for (i in 1..5) {
                for (j in 1..5) {
                    val product = ProductEntity(name = "测试商品$num", currentPrice =  100, categoryId = i.toLong())
                    num++
                    productDao.insert(product)
                }
            }
            Log.d("DB_Callback", "Product测试数据填充完毕")
        }

        if(productPriceHistoryDao.getCount() == 0) {
            for (i in 1..5) {
                val productPriceHistoryEntity = ProductPriceHistoryEntity(userId = 1, productId = 1, originalPrice = 100, newPrice = 200, originalPriceDate = "2025-10-10")
                productPriceHistoryDao.insert(productPriceHistoryEntity)
            }
            Log.d("DB_Callback", "ProductPriceHistory测试数据填充完毕")
        }

        if(saleInvoiceDao.getCount() == 0 && salePaymentDao.getCount() == 0){
            for (i in 1..5) {
                val saleInvoiceEntity = SaleInvoiceEntity(
                    creatorId = i.toLong(),
                    customerId = i.toLong(),
                    customerName = "测试客户",
                    creatorName = "测试用户",
                    status = InvoiceStatus.UNPAID,
                    amountDue = 100,
                    amountPaid = 100,
                    amountRem = 0, remark = "测试账单备注")
                saleInvoiceRepository.add(saleInvoiceEntity)
                for (j in 1..5){
                    val salePaymentEntity = SalePaymentEntity(invoiceId = i.toLong(), sn = "TEST1234567${i}${j}", customerId = i.toLong(), amount = 100, paymentMethods = PaymentMethods.CASH)
                    salePaymentDao.insert(salePaymentEntity)
                }
            }
            Log.d("DB_Callback", "SaleInvoice测试数据填充完毕")
            Log.d("DB_Callback", "SalePayment测试数据填充完毕")
        }

        if(saleOrderDao.getCount() == 0 && saleOrderItemDao.getCount() == 0){
            for (i in 1..5) {
                val saleOrderEntity = SaleOrderEntity(
                    creatorId = 1,
                    customerId = 1,
                    invoiceId = 1,
                    invoiceSn = "测试SN",
                    deliveryMethod = DeliveryMethod.FREIGHT,
                    orderStatus = OrderStatus.CONFIRMED,
                    totalPrice = 100,
                    totalQuantity = 10,
                    remark = "测试订单"
                )
                saleOrderRepository.add(saleOrderEntity)
                for (j in 1..5){
                    val saleOrderItemEntity = SaleOrderItemEntity(
                        orderId = i.toLong(),
                        productId = i.toLong(),
                        productName = "商品快照名",
                        quantity = 10,
                        unitPrice = 100,
                        subtotal = 1000,
                        quantityUnitId = 1,
                        weight = 10,
                        weightUnitId = 1
                    )
                    saleOrderItemDao.insert(saleOrderItemEntity)
                }
            }
            Log.d("DB_Callback", "SaleOrder测试数据填充完毕")
            Log.d("DB_Callback", "SaleOrderItem测试数据填充完毕")
        }

        if(purchaseInvoiceDao.getCount() == 0 && purchasePaymentDao.getCount() == 0){
            for (i in 1..5) {
                val purchaseInvoiceEntity = PurchaseInvoiceEntity(
                    creatorId = i.toLong(),
                    creatorName = "测试用户",
                    supplierId = i.toLong(),
                    supplierName = "测试供应商",
                    amountPayable = 100,
                    amountPaid = 100,
                    amountRem = 0,
                    status = InvoiceStatus.UNPAID
                )
                purchaseInvoiceRepository.add(purchaseInvoiceEntity)
                for (j in 1..5){
                    val purchasePaymentEntity = PurchasePaymentEntity(invoiceId = i.toLong(), sn = "TEST1234567${i}${j}", supplierId = i.toLong(), amount = 100, paymentMethods = PaymentMethods.CASH)
                    purchasePaymentDao.insert(purchasePaymentEntity)
                }
                Log.d("DB_Callback", "PurchaseInvoice测试数据填充完毕")
                Log.d("DB_Callback", "PurchasePayment测试数据填充完毕")
            }
        }
        if(purchaseOrderDao.getCount() == 0 && purchaseOrderItemDao.getCount() == 0){
            for (i in 1..5) {
                val purchaseOrderEntity = PurchaseOrderEntity(
                    creatorId = 1,
                    supplierId = 1,
                    invoiceId = 1,
                    invoiceSn = "测试SN",
                    deliveryMethod = DeliveryMethod.FREIGHT,
                    orderStatus = OrderStatus.CONFIRMED,
                    supplierName = "测试供应商",
                    totalPrice = 200,
                    totalQuantity = 3,
                    createdAt = System.currentTimeMillis(),
                    remark = "测试订单")
                purchaseOrderRepository.addPurchaseOrder(purchaseOrderEntity)
                for (j in 1..5){
                    val purchaseOrderItemEntity = PurchaseOrderItemEntity(
                        orderId = i.toLong(),
                        productId = i.toLong(),
                        productName = "商品快照名",
                        quantity = 10,
                        unitPrice = 100,
                        subtotal = 1000,
                        quantityUnitId = 1,
                        weight = 10,
                        weightUnitId = 1
                    )
                    purchaseOrderItemDao.insert(purchaseOrderItemEntity)
                }
            }
            Log.d("DB_Callback", "PurchaseOrder测试数据填充完毕")
            Log.d("DB_Callback", "PurchaseOrderItem测试数据填充完毕")
        }

        Log.d("DB_Callback", "------- 所有数据检查完成")
    }
}