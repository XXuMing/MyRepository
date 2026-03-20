package com.hjaquaculture.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.hjaquaculture.data.local.entity.InventoryEntity
import kotlinx.coroutines.flow.Flow

// 库存更新最忌讳“先查再改”（容易产生并发死锁或覆盖更新）。在 Room 中，我们通常使用 UPDATE 语句配合加减法来保证原子性。
// 在插入新商品时，记得同步在 inventory 表插入一条 amount = 0 的初始化记录，否则后续的 UPDATE 语句会因为找不到行而失效。
@Dao
interface InventoryDao {
    // 增加库存（入库/盘盈）
    @Query("UPDATE inventory SET amount = amount + :increment, last_updated_at = :now WHERE product_id = :productId")
    suspend fun increaseStock(productId: Long, increment: Int, now: Long = System.currentTimeMillis())

    // 减少库存（出库/盘亏）
    @Query("UPDATE inventory SET amount = amount - :decrement, last_updated_at = :now WHERE product_id = :productId")
    suspend fun decreaseStock(productId: Long, decrement: Int, now: Long = System.currentTimeMillis())

    // 获取所有低于预警水位的库存
    @Query("SELECT * FROM inventory WHERE amount <= min_stock")
    fun getLowStockItems(): Flow<List<InventoryEntity>>
}