package com.hjaquaculture.data.local.mapper

import com.hjaquaculture.data.local.entity.InventoryEntity
import com.hjaquaculture.data.local.entity.InventoryLogEntity
import com.hjaquaculture.data.local.entity.MeasureUnitEntity
import com.hjaquaculture.data.local.entity.ProductEntity
import com.hjaquaculture.data.local.entity.ProductPriceHistoryEntity
import com.hjaquaculture.data.local.entity.ProductVarietyEntity
import com.hjaquaculture.domain.model.Inventory
import com.hjaquaculture.domain.model.InventoryLog
import com.hjaquaculture.domain.model.MeasureUnit
import com.hjaquaculture.domain.model.PriceHistory
import com.hjaquaculture.domain.model.Product
import com.hjaquaculture.domain.model.ProductVariety

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
        if (fromUnit.dimension != toUnit.dimension) {
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
        dimension = this.dimension,
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
        dimension = this.dimension,
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
        varietyId = this.varietyId,
        measureDimension = this.measureDimension,
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
        varietyId = this.varietyId,
        measureDimension = this.measureDimension,
        isAvailable = this.isAvailable,
        sort = this.sort
    )
}
/**
 * ProductCategoryMapper
 */

// --- Entity -> Domain ---
fun ProductVarietyEntity.toDomain(): ProductVariety {
    return ProductVariety(
        id = this.id,
        name = this.name,
        sort = this.sort
    )
}

// --- Domain -> Entity ---
fun ProductVariety.toEntity(): ProductVarietyEntity {
    return ProductVarietyEntity(
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
        operatorId = this.operatorId,
        price = this.price,
        changedAt = this.changedAt,
        remark = this.remark
    )
}

// --- Domain -> Entity ---
fun PriceHistory.toEntity(): ProductPriceHistoryEntity {
    return ProductPriceHistoryEntity(
        id = this.id,
        productId = this.productId,
        operatorId = this.operatorId,
        price = this.price,
        changedAt = this.changedAt,
    )
}

/**
 * 实时库存映射器
 */
fun InventoryEntity.toDomain() = Inventory(
    productId = this.productId,
    amount = this.amount,
    minStock = this.minStock,
    lastUpdatedAt = this.lastUpdatedAt
)

fun Inventory.toEntity(id: Long = 0): InventoryEntity {
    return InventoryEntity(
        id = id,
        productId = this.productId,
        amount = this.amount,
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
        changeType = this.changeType,
        amount = this.amount,
        balanceSnapshot = this.balanceSnapshot,
        refOrderId = this.refOrderId,
        operatorId = this.operatorId,
        createdAt = this.createdAt,
        // 处理可空字段：如果数据库为 null，业务层转为空字符串
        remark = this.remark ?: ""
    )
}

fun InventoryLog.toEntity(): InventoryLogEntity {
    return InventoryLogEntity(
        // 如果是新记录，Room 会自动生成 id (id=0)
        // 如果是从数据库加载后修改的，则保留原 id
        id = this.id,
        productId = this.productId,
        changeType = this.changeType,
        amount = this.amount,
        balanceSnapshot = this.balanceSnapshot,
        refOrderId = this.refOrderId,
        operatorId = this.operatorId,
        createdAt = this.createdAt,
        remark = this.remark
    )
}