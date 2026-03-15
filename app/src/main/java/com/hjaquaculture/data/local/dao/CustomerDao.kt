package com.hjaquaculture.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hjaquaculture.data.local.entity.CustomerEntity
import kotlinx.coroutines.flow.Flow

/**
 * 客户数据访问对象 (DAO)
 *
 * 提供了与客户表 ('customers_table') 进行交互的所有方法。
 * 请注意：
 * 1. 建议将实体类 Customers 重命名为单数形式 CustomerEntity，这更符合编程规范。
 * 2. 请将 'customers_table' 和主键 'id' 替换为您的实际表名和主键名。
 */
@Dao
interface CustomerDao {

    /**
     * 插入单个客户。如果发生冲突（例如，主键已存在），则替换旧数据。
     * @param customerEntity 要插入的客户对象
     * @return 返回新插入行的 Row ID
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(customerEntity: CustomerEntity): Long

    /**
     * 批量插入客户列表。
     * @param customerEntities 客户对象列表
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(customerEntities: List<CustomerEntity>)

    /**
     * 更新单个客户。
     * @param customerEntity 要更新的客户对象
     */
    @Update
    suspend fun update(customerEntity: CustomerEntity)

    /**
     * 删除单个客户。
     * @param customerEntity 要删除的客户对象
     */
    @Delete
    suspend fun delete(customerEntity: CustomerEntity)

    /**
     * 根据ID删除指定客户。
     * @param customerId 客户的ID
     */
    @Query("DELETE FROM customer WHERE id = :customerId")
    suspend fun deleteById(customerId: Long)

    /**
     * 统计表中客户的数量。
     * @return 返回客户数量
     */
    @Query("SELECT COUNT(*) FROM customer")
    suspend fun getCount(): Int

    /**
     * 删除表中的所有客户。
     */
    @Query("DELETE FROM customer")
    suspend fun deleteAll()

    /**
     * 【推荐】响应式查询：获取所有客户，并以 Flow 的形式返回。
     * 当客户数据发生变化时，Flow 会自动发射最新的数据列表，非常适合在UI层进行观察。
     * @return 返回包含所有客户列表的 Flow
     */
    @Query("SELECT * FROM customer ORDER BY id DESC")
    fun getAll(): Flow<List<CustomerEntity>>

    /**
     * 根据ID查询单个客户。
     * @param customerId 客户的ID
     * @return 返回一个包含单个客户或 null 的 Flow
     */
    @Query("SELECT * FROM customer WHERE id = :customerId")
    fun getById(customerId: Long): Flow<CustomerEntity?>

    /**
     * 一次性查询：根据名称模糊搜索客户。
     * @param name 搜索的客户名称关键词
     * @return 返回符合条件的客户列表
     */
    @Query("SELECT * FROM customer WHERE name LIKE '%' || :name || '%'")
    suspend fun findByName(name: String): List<CustomerEntity>

    /**
     * 为 Paging 3.0 提供数据源，用于实现分页加载。
     * @return 返回 PagingSource，用于分页列表
     */
    @Query("SELECT * FROM customer ORDER BY id DESC")
    fun getPagingSource(): PagingSource<Int, CustomerEntity>
}

