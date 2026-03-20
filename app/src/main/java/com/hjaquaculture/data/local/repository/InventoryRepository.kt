package com.hjaquaculture.data.local.repository

import com.hjaquaculture.data.local.LocalDatabase
import com.hjaquaculture.data.local.dao.InventoryDao
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Singleton
class InventoryRepository @Inject constructor(
    private val database: LocalDatabase,
    private val dao: InventoryDao
) {


}