package com.hjaquaculture.di

import android.app.Application
import android.util.Log
import android.util.Log.i
import com.hjaquaculture.common.utils.InvoiceStatus
import com.hjaquaculture.common.utils.PaymentMethods
import com.hjaquaculture.common.utils.PurchaseOrderStatus
import com.hjaquaculture.common.utils.PurchaseOrderType
import com.hjaquaculture.common.utils.SaleOrderStatus
import com.hjaquaculture.common.utils.SaleOrderType
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
import com.hjaquaculture.domain.repository.PurchaseInvoiceRepository
import com.hjaquaculture.domain.repository.PurchaseOrderRepository
import com.hjaquaculture.domain.repository.PurchasePaymentRepository
import com.hjaquaculture.domain.repository.SaleInvoiceRepository
import com.hjaquaculture.domain.repository.SaleOrderRepository
import com.hjaquaculture.domain.repository.SalePaymentRepository
import com.hjaquaculture.feature.AuthAction
import dagger.hilt.android.HiltAndroidApp
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

@HiltAndroidApp
class App : Application(){

    @Inject
    lateinit var userDao: UserDao
    @Inject
    lateinit var customerDao: CustomerDao
    @Inject
    lateinit var supplierDao: SupplierDao
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
        // 调用 Repository 的方法，它会自动处理事务和单据号生成
        // 记得在这里加个判断，防止每次启动都插入
        if(userDao.getCount() == 0) {
            for(i in 1..5){
                val user = User(account ="测试用户名$i",username="测试用户名$i", passwordHash = "123456", role = "test_user")
                userDao.insert(user)
            }
            Log.d("DB_Callback", "User测试数据填充完毕")
        }

        if(customerDao.getCount() == 0) {
            for(i in 1..5){
                val customer = Customer( name="测试客户$i", nikeName =  "测试客户昵称$i", passwordHash = "123456", phone = "1334567890$i")
                customerDao.insert(customer)
            }
            Log.d("DB_Callback", "Customer测试数据填充完毕")
        }

        if(supplierDao.getCount() == 0) {
            for (i in 1..5) {
                val supplier = Supplier(name="测试供应商$i", phone = "1334567890$i")
                supplierDao.insert(supplier)
            }
            Log.d("DB_Callback", "Supplier测试数据填充完毕")
        }
        if(productCategoryDao.getCount() == 0) {
            for (i in 1..5) {
                val productCategory = ProductCategory(name="测试分类$i")
                productCategoryDao.insert(productCategory)
            }
            Log.d("DB_Callback", "ProductCategory测试数据填充完毕")
        }

        if(productDao.getCount() == 0) {
            var num : Int = 1
            for (i in 1..5) {
                for (j in 1..5) {
                    val product = Product(name = "测试商品$num", currentPrice =  100, categoryId = i.toLong())
                    num++
                    productDao.insert(product)
                }
            }
            Log.d("DB_Callback", "Product测试数据填充完毕")
        }

        if(productPriceHistoryDao.getCount() == 0) {
            for (i in 1..5) {
                val productPriceHistory = ProductPriceHistory(userId = 1, productId = 1, originalPrice = 100, newPrice = 200, originalPriceDate = "2025-10-10")
                productPriceHistoryDao.insert(productPriceHistory)
            }
            Log.d("DB_Callback", "ProductPriceHistory测试数据填充完毕")
        }

        if(saleOrderDao.getCount() == 0 && saleOrderItemDao.getCount() == 0){
            for (i in 1..5) {
                val saleOrder = SaleOrder(
                    userId = 1,
                    customerId = 1,
                    orderType = SaleOrderType.SHIPPING,
                    status = SaleOrderStatus.PICKUP_UNPAID,
                    totalPrice = 100,
                    totalQuantity = 10,
                    remark = "测试订单"
                )
                saleOrderRepository.addSaleOrder(saleOrder)
                for (j in 1..5){
                    val saleOrderItem = SaleOrderItem(orderId = i.toLong(), productId = i.toLong(), productName = "商品快照名", quantity = 10, unitPrice = 100, subtotal = 1000)
                    saleOrderItemDao.insert(saleOrderItem)
                }
            }
            Log.d("DB_Callback", "SaleOrder测试数据填充完毕")
            Log.d("DB_Callback", "SaleOrderItem测试数据填充完毕")
        }

        if(saleInvoiceDao.getCount() == 0 && salePaymentDao.getCount() == 0){
            for (i in 1..5) {
                val saleInvoice = SaleInvoice(
                    userId = i.toLong(),
                    customerId = i.toLong(),
                    customerName = "测试客户",
                    userName = "测试用户",
                    orderSn = "测试订单编号",
                    orderId = i.toLong(),
                    status = InvoiceStatus.UNPAID,
                    amountDue = 100,
                    amountPaid = 100,
                    amountRem = 0)
                saleInvoiceRepository.add(saleInvoice)
                for (j in 1..5){
                    val salePayment = SalePayment(invoiceId = i.toLong(), customerId = i.toLong(), amount = 100, paymentMethods = PaymentMethods.CASH)
                    salePaymentDao.insert(salePayment)
                }
            }
            Log.d("DB_Callback", "SaleInvoice测试数据填充完毕")
            Log.d("DB_Callback", "SalePayment测试数据填充完毕")
        }
        if(purchaseOrderDao.getCount() == 0 && purchaseOrderItemDao.getCount() == 0){
            for (i in 1..5) {
                val purchaseOrder = PurchaseOrder(
                    userId = 1,
                    supplierId = 1,
                    orderType = PurchaseOrderType.SELF_PURCHASE,
                    status = PurchaseOrderStatus.AUDITED,
                    supplierName = "测试供应商",
                    totalPrice = 200,
                    totalQuantity = 3,
                    orderDate = System.currentTimeMillis(),
                    remark = "测试订单")
                purchaseOrderRepository.addPurchaseOrder(purchaseOrder)
                for (j in 1..5){
                    val purchaseOrderItem = PurchaseOrderItem(
                        orderId = i.toLong(),
                        productId = i.toLong(),
                        productName = "商品快照名",
                        quantity = 10,
                        unitPrice = 100,
                        subtotal = 1000,
                        unit = "件",
                        weight = 10,)
                    purchaseOrderItemDao.insert(purchaseOrderItem)
                }
            }
            Log.d("DB_Callback", "PurchaseOrder测试数据填充完毕")
            Log.d("DB_Callback", "PurchaseOrderItem测试数据填充完毕")
        }
        if(purchaseInvoiceDao.getCount() == 0 && purchasePaymentDao.getCount() == 0){
            for (i in 1..5) {
                val purchaseInvoice = PurchaseInvoice(
                    userId = i.toLong(),
                    userName = "测试用户",
                    orderSn = "测试订单编号",
                    supplierId = i.toLong(),
                    supplierName = "测试供应商",
                    orderId = i.toLong(),
                    amountPayable = 100,
                    amountPaid = 100,
                    amountRem = 0,
                    status = InvoiceStatus.UNPAID
                )
                purchaseInvoiceRepository.add(purchaseInvoice)
                for (j in 1..5){
                    val purchasePayment = PurchasePayment(invoiceId = i.toLong(), supplierId = i.toLong(), amount = 100, paymentMethods = PaymentMethods.CASH)
                    purchasePaymentDao.insert(purchasePayment)
                }
                Log.d("DB_Callback", "PurchaseInvoice测试数据填充完毕")
                Log.d("DB_Callback", "PurchasePayment测试数据填充完毕")
            }
        }
        Log.d("DB_Callback", "------- 所有数据检查完成")
    }
}