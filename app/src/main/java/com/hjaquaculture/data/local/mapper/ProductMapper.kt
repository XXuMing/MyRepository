package com.hjaquaculture.data.local.mapper

import com.hjaquaculture.data.local.entity.InventoryEntity
import com.hjaquaculture.data.local.entity.InventoryLogEntity
import com.hjaquaculture.data.local.entity.MeasureUnitEntity
import com.hjaquaculture.data.local.entity.ProductCategoryEntity
import com.hjaquaculture.data.local.entity.ProductEntity
import com.hjaquaculture.data.local.entity.ProductPriceHistoryEntity
import com.hjaquaculture.domain.model.Inventory
import com.hjaquaculture.domain.model.InventoryLog
import com.hjaquaculture.domain.model.MeasureUnit
import com.hjaquaculture.domain.model.PriceHistory
import com.hjaquaculture.domain.model.Product
import com.hjaquaculture.domain.model.ProductCategory
import com.hjaquaculture.domain.model.StockChangeType

/**
 * 单位转换工具（领域服务）
 */
object UnitConverter {
    /**
     * 在同分类的两个单位间转换数值
     * 逻辑：先转为基准单位，再由基准单位转为目标单位
     */
    fun convert(
        value: Double,
        fromUnit: MeasureUnit,
        toUnit: MeasureUnit
    ): Double {
        if (fromUnit.category != toUnit.category) {
            throw IllegalArgumentException("不能在不同分类间转换单位")
        }

        val baseValue = fromUnit.toBaseValue(value)
        return toUnit.fromBaseValue(baseValue)
    }
}
/**
 * Entity -> Domain
 */
fun MeasureUnitEntity.toDomain(): MeasureUnit {
    return MeasureUnit(
        id = this.id,
        name = this.name,
        category = this.category,
        conversionRate = this.conversionRate,
        precision = this.precision,
        isBase = this.isBase,
        sort = this.sort
    )
}

/**
 * Domain -> Entity
 */
fun MeasureUnit.toEntity(): MeasureUnitEntity {
    return MeasureUnitEntity(
        id = this.id,
        name = this.name,
        category = this.category,
        conversionRate = this.conversionRate,
        precision = this.precision,
        isBase = this.isBase,
        sort = this.sort
    )
}
/**
 * ProductMapper
 */

// --- Entity -> Domain ---
fun ProductEntity.toDomain(): Product {
    return Product(
        id = this.id,
        name = this.name,
        currentPrice = this.currentPrice,
        categoryId = this.categoryId,
        isAvailable = this.isAvailable,
        sort = this.sort
    )
}

// --- Domain -> Entity ---
fun Product.toEntity(): ProductEntity {
    return ProductEntity(
        id = this.id,
        name = this.name,
        currentPrice = this.currentPrice,
        categoryId = this.categoryId,
        isAvailable = this.isAvailable,
        sort = this.sort
    )
}
/**
 * ProductCategoryMapper
 */

// --- Entity -> Domain ---
fun ProductCategoryEntity.toDomain(): ProductCategory {
    return ProductCategory(
        id = this.id,
        name = this.name,
        sort = this.sort
    )
}

// --- Domain -> Entity ---
fun ProductCategory.toEntity(): ProductCategoryEntity {
    return ProductCategoryEntity(
        id = this.id,
        name = this.name,
        sort = this.sort
    )
}

/**
 * PriceHistoryMapper
 */

// --- Entity -> Domain ---
fun ProductPriceHistoryEntity.toDomain(): PriceHistory {
    return PriceHistory(
        id = this.id,
        productId = this.productId,
        userId = this.userId,
        originalPrice = this.originalPrice,
        originalPriceDate = this.originalPriceDate,
        newPrice = this.newPrice,
        newPriceDate = this.newPriceDate
    )
}

// --- Domain -> Entity ---
fun PriceHistory.toEntity(): ProductPriceHistoryEntity {
    return ProductPriceHistoryEntity(
        id = this.id,
        productId = this.productId,
        userId = this.userId,
        originalPrice = this.originalPrice,
        originalPriceDate = this.originalPriceDate,
        newPrice = this.newPrice,
        newPriceDate = this.newPriceDate
    )
}

/**
 * 实时库存映射器
 */
fun InventoryEntity.toDomain(): Inventory {
    return Inventory(
        productId = this.productId,
        quantity = this.quantity,
        minStock = this.minStock
    )
}

fun Inventory.toEntity(id: Long = 0): InventoryEntity {
    return InventoryEntity(
        id = id,
        productId = this.productId,
        quantity = this.quantity,
        minStock = this.minStock,
        lastUpdatedAt = System.currentTimeMillis()
    )
}

/**
 * 库存流水映射器
 */
fun InventoryLogEntity.toDomain(): InventoryLog {
    return InventoryLog(
        id = this.id,
        productId = this.productId,
        changeType = StockChangeType.fromInt(this.changeType),
        amount = this.amount,
        balanceSnapshot = this.balanceSnapshot,
        refOrderSn = this.refOrderSn ?: "",
        remark = this.remark ?: "",
        createdAt = this.createdAt
    )
}

fun InventoryLog.toEntity(): InventoryLogEntity {
    return InventoryLogEntity(
        id = this.id,
        productId = this.productId,
        changeType = this.changeType.value,
        amount = this.amount,
        balanceSnapshot = this.balanceSnapshot,
        refOrderSn = this.refOrderSn,
        remark = this.remark,
        createdAt = this.createdAt
    )
}