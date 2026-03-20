package com.hjaquaculture.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import com.hjaquaculture.data.local.entity.CombinedPeopleView

@Dao
interface CombinedPeopleDao {

    @Query("""
        SELECT * 
        FROM combined_people_view
        WHERE (:symbol IS NULL OR symbol = :symbol)
        AND (
            :query = '' OR
            account LIKE '%' || :query || '%' OR
            name LIKE '%' || :query || '%' OR
            nick_name LIKE '%' || :query || '%' OR
            phone LIKE '%' || :query || '%' OR
            role LIKE '%' || :query || '%'
        )
        ORDER BY created_at DESC
        
    """)
    fun pagingSource(query: String, symbol: String?): PagingSource<Int, CombinedPeopleView>

    @Query("SELECT COUNT(*) FROM combined_people_view")
    suspend fun getCount(): Int

}