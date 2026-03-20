package com.hjaquaculture.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hjaquaculture.data.local.entity.InventoryEntity
import kotlinx.coroutines.flow.Flow

// 库存更新最忌讳“先查再改”（容易产生并发死锁或覆盖更新）。在 Room 中，我们通常使用 UPDATE 语句配合加减法来保证原子性。
// 在插入新商品时，记得同步在 inventory 表插入一条 amount = 0 的初始化记录，否则后续的 UPDATE 语句会因为找不到行而失效。
@Dao
interface InventoryDao {

    // --- 增加 ---

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(inventory: InventoryEntity): Long

    // --- 修改 ---

    // 核心操作：原子性增减，避免"先查再改"的并发问题
    @Query("UPDATE inventory SET amount = amount + :delta, last_updated_at = :now WHERE product_id = :productId")
    suspend fun adjustAmount(productId: Long, delta: Int, now: Long = System.currentTimeMillis()): Int

    // 盘点校准：直接设定库存量
    @Query("UPDATE inventory SET amount = :amount, last_updated_at = :now WHERE product_id = :productId")
    suspend fun setAmount(productId: Long, amount: Int, now: Long = System.currentTimeMillis()): Int

    @Query("UPDATE inventory SET min_stock = :minStock WHERE product_id = :productId")
    suspend fun setMinStock(productId: Long, minStock: Int): Int

    // --- 查询 ---

    @Query("SELECT * FROM inventory WHERE product_id = :productId")
    fun getByProductId(productId: Long): Flow<InventoryEntity?>

    @Query("SELECT * FROM inventory")
    suspend fun getAllSnapshot(): List<InventoryEntity>
    @Query("SELECT * FROM inventory")
    fun getAll(): Flow<List<InventoryEntity>>

    // 低于预警水位（amount > 0 但低于 minStock）
    @Query("SELECT * FROM inventory WHERE min_stock > 0 AND amount < min_stock AND amount > 0")
    fun getLowStockItems(): Flow<List<InventoryEntity>>

    // 负库存预警
    @Query("SELECT * FROM inventory WHERE amount < 0")
    fun getNegativeStockItems(): Flow<List<InventoryEntity>>

    @Query("SELECT COUNT(*) FROM inventory")
    suspend fun getCount(): Int
}