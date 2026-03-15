package com.hjaquaculture.data.local


import androidx.room.RoomDatabase
import androidx.room.withTransaction
import androidx.sqlite.db.SupportSQLiteDatabase
import jakarta.inject.Inject
import jakarta.inject.Provider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * 数据库回调，预填充数据
 */
class DatabaseCallback @Inject constructor(
    // 使用 Provider 解决 Hilt 循环依赖问题
    private val databaseProvider: Provider<LocalDatabase>
) : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)

        // 开启协程执行耗时操作
        CoroutineScope(Dispatchers.IO).launch {
            // 预载初始数据
            val database = databaseProvider.get()
            preloadInitialData(database)
        }
    }


    suspend fun preloadInitialData(database : LocalDatabase){
        database.withTransaction {

        }
    }

}