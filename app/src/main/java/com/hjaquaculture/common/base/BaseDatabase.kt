package com.hjaquaculture.common.base

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hjaquaculture.data.dao.UserDao
import com.hjaquaculture.data.model.User

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class BaseDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao // 抽象方法，返回 DAO 实例

    companion object {
        @Volatile
        private var INSTANCE: BaseDatabase? = null

        fun getDatabase(context: Context): BaseDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BaseDatabase::class.java,
                    "app_database" // 数据库文件名
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}