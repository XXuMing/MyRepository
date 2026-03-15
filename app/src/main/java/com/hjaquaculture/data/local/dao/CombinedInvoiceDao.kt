package com.hjaquaculture.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import com.hjaquaculture.data.local.entity.CombinedInvoiceView
import kotlinx.coroutines.flow.Flow

@Dao
interface CombinedInvoiceDao {

    @Query("""
        SELECT * 
        FROM combined_invoice_view
        WHERE (:symbol IS NULL OR symbol = :symbol)
        AND (:status IS NULL OR status = :status)
        AND (
            :query = '' OR
            creator_name LIKE '%' || :query || '%' OR
            partner_name LIKE '%' || :query || '%' OR
            sn LIKE '%' || :query || '%'
        )
        ORDER BY created_at DESC
    """)
    fun pagingSource(
        query: String,
        symbol: String?,
        status:String?
    ): PagingSource<Int, CombinedInvoiceView>

    @Query("SELECT COUNT(*) FROM combined_invoice_view")
    fun getCount(): Flow<Int>

}