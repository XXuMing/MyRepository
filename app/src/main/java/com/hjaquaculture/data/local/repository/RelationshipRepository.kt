package com.hjaquaculture.data.local.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.hjaquaculture.data.local.dao.CombinedPeopleDao
import com.hjaquaculture.data.local.dao.CustomerDao
import com.hjaquaculture.data.local.dao.UserDao
import com.hjaquaculture.data.local.entity.CombinedPeopleView
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.Flow

@Singleton
data class RelationshipRepository @Inject constructor(
    private val combinedDao: CombinedPeopleDao,
    private val uDao: UserDao,
    private val cDao: CustomerDao,
) {
    fun getCombinedPeople(query: String, symbol: String?): Flow<PagingData<CombinedPeopleView>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
                initialLoadSize = 20
            ),
            pagingSourceFactory = { combinedDao.pagingSource(query, symbol) }
        ).flow
    }
}