package com.hjaquaculture.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import com.hjaquaculture.data.local.entity.CombinedOrderView
import kotlinx.coroutines.flow.Flow

@Dao
interface CombinedOrderDao {

    @Query("""
        SELECT *
        FROM combined_order_view 
        WHERE (:symbol IS NULL OR symbol = :symbol)
        AND (:type IS NULL OR order_type = :type)
        AND (:status IS NULL OR order_status = :status)
        AND (
            :query = '' OR
            creator_name LIKE '%' || :query || '%' OR
            partner_name LIKE '%' || :query || '%' OR
            sn LIKE '%' || :query || '%'
        )
        ORDER BY created_at DESC
    """)
    fun getPagingSource(query: String, symbol: String?, type: String?, status:String?): PagingSource<Int, CombinedOrderView>

    // 此方法不如SaleOrderDao/PurchaseOrderDao中的getByInvoiceId
    @Query("""
        SELECT * 
        FROM combined_order_view
        WHERE (:symbol IS NULL OR symbol = :symbol)
        AND invoice_id = :invoiceId
    """)
    fun getByInvoiceId(symbol: String?,invoiceId: Long): Flow<List<CombinedOrderView>>

    @Query("SELECT COUNT(*) FROM combined_order_view")
    fun getCount(): Flow<Int>

}