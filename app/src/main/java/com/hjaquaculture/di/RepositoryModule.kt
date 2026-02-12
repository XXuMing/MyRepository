package com.hjaquaculture.di

import com.hjaquaculture.domain.repository.ProductPriceHistoryRepository
import com.hjaquaculture.data.local.repository.ProductPriceHistoryRepositoryImpl
import com.hjaquaculture.domain.repository.ProductRepository
import com.hjaquaculture.data.local.repository.ProductRepositoryImpl
import com.hjaquaculture.domain.repository.PurchaseInvoiceRepository
import com.hjaquaculture.data.local.repository.PurchaseInvoiceRepositoryImpl
import com.hjaquaculture.domain.repository.PurchaseOrderRepository
import com.hjaquaculture.data.local.repository.PurchaseOrderRepositoryImpl
import com.hjaquaculture.domain.repository.PurchasePaymentRepository
import com.hjaquaculture.data.local.repository.PurchasePaymentRepositoryImpl
import com.hjaquaculture.domain.repository.SaleInvoiceRepository
import com.hjaquaculture.data.local.repository.SaleInvoiceRepositoryImpl
import com.hjaquaculture.domain.repository.SaleOrderRepository
import com.hjaquaculture.data.local.repository.SaleOrderRepositoryImpl
import com.hjaquaculture.domain.repository.SalePaymentRepository
import com.hjaquaculture.data.local.repository.SalePaymentRepositoryImpl
import com.hjaquaculture.domain.repository.UserRepository
import com.hjaquaculture.data.local.repository.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

/**
 * 绑定 Repository 接口和实现类的模块
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    /**
     * 绑定 UserRepositoryImpl 实现到 UserRepository 接口
     */
    @Binds
    @Singleton
    abstract fun bindUserRepository(
        impl: UserRepositoryImpl
    ): UserRepository

    /**
     * 绑定 ProductRepositoryImpl 实现到 ProductRepository 接口
     */
    @Binds
    @Singleton
    abstract fun bindProductRepository(
        impl: ProductRepositoryImpl
    ): ProductRepository

    /**
     * 绑定 ProductPriceHistoryRepositoryImpl 实现到 ProductPriceHistoryRepository 接口
     */
    @Binds
    @Singleton
    abstract fun bindProductPriceHistoryRepository(
        impl: ProductPriceHistoryRepositoryImpl
    ): ProductPriceHistoryRepository

    /**
     * 绑定 PurchaseOrderRepositoryImpl 实现到 PurchaseOrderRepository 接口
     */
    @Binds
    @Singleton
    abstract fun bindPurchaseOrderRepository(
        impl: PurchaseOrderRepositoryImpl
    ): PurchaseOrderRepository

    /**
     * 绑定 PurchaseInvoiceRepositoryImpl 实现到 PurchaseInvoiceRepository 接口
     */
    @Binds
    @Singleton
    abstract fun bindPurchaseInvoiceRepository(
        impl: PurchaseInvoiceRepositoryImpl
    ): PurchaseInvoiceRepository

    /**
     * 绑定 PurchasePaymentRepositoryImpl 实现到 PurchasePaymentRepository 接口
     */
    @Binds
    @Singleton
    abstract fun bindPurchasePaymentRepository(
        impl: PurchasePaymentRepositoryImpl
    ): PurchasePaymentRepository

    /**
     * 绑定 SaleOrderRepositoryImpl 实现到 SaleOrderRepository 接口
     */
    @Binds
    @Singleton
    abstract fun bindSaleOrderRepository(
        impl: SaleOrderRepositoryImpl
    ): SaleOrderRepository

    /**
     * 绑定 SaleInvoiceRepositoryImpl 实现到 SaleInvoiceRepository 接口
     */
    @Binds
    @Singleton
    abstract fun bindSaleInvoiceRepository(
        impl: SaleInvoiceRepositoryImpl
    ): SaleInvoiceRepository

    /**
     * 绑定 SalePaymentRepositoryImpl 实现到 SalePaymentRepository 接口
     */
    @Binds
    @Singleton
    abstract fun bindSalePaymentRepository(
        impl: SalePaymentRepositoryImpl
    ): SalePaymentRepository

}