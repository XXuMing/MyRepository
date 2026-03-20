package com.hjaquaculture.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.DatabaseView
import com.hjaquaculture.common.base.StockUnit


//COALESCE 确保库存为空时显示 0 而不是 null。
@DatabaseView(
    viewName = "product_inventory_view",
    value = """
        SELECT 
            p.id,
            p.name,
            p.current_price,
            p.price_updated_at,
            p.variety_id,
            p.stock_unit,
            p.is_available,
            p.sort,
            COALESCE(i.amount, 0) AS stock_amount,
            COALESCE(i.min_stock, 0) AS min_stock,
            i.last_updated_at AS stock_updated_at,
            CASE 
                WHEN i.amount IS NULL THEN 'NO_RECORD'
                WHEN i.amount < 0 THEN 'NEGATIVE'
                WHEN i.min_stock > 0 AND i.amount < i.min_stock THEN 'LOW'
                ELSE 'NORMAL'
            END AS stock_status
        FROM product p
        LEFT JOIN inventory i ON p.id = i.product_id
    """
)
data class ProductInventoryView(
    @ColumnInfo("id")
    val id: Long,

    @ColumnInfo("name")
    val name: String,

    @ColumnInfo("current_price")
    val currentPrice: Long,

    @ColumnInfo(name = "price_updated_at")
    val priceUpdatedAt: Long,

    @ColumnInfo("variety_id")
    val varietyId: Long,

    @ColumnInfo("stock_unit")
    val stockUnit: StockUnit,

    @ColumnInfo("is_available")
    val isAvailable: Boolean,

    @ColumnInfo("sort")
    val sort: Int,

    @ColumnInfo("stock_amount")
    val stockAmount: Int,        // 库存数量，无库存记录时为 0

    @ColumnInfo("min_stock")
    val minStock: Int,

    @ColumnInfo("stock_updated_at")
    val stockUpdatedAt: Long?,   // 可能为 null（商品从未入库过）

    @ColumnInfo("stock_status")
    val stockStatus: String      // 库存状态标识
)