package com.hjaquaculture.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hjaquaculture.common.utils.Converters
import com.hjaquaculture.data.local.dao.UserDao
import com.hjaquaculture.data.local.model.UserEntity

@Database(entities = [UserEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class BaseDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao // 抽象方法，返回 DAO 实例

    companion object {
        @Volatile
        private var INSTANCE: BaseDatabase? = null

        fun getDB(context: Context): BaseDatabase {
            var dataBaseName = "HJA_database"
            // 如果 INSTANCE 不为空，直接返回；
            // 如果为空，则同步锁创建数据库
            return INSTANCE ?: synchronized(this) {
                val instance = Room.inMemoryDatabaseBuilder(
                    context.applicationContext,
                    BaseDatabase::class.java,
                    //dataBaseName // 数据库文件名
                )
                    // 策略：如果没有匹配的迁移路径，重建数据库（生产环境慎用，建议开发阶段使用）
                    //.fallbackToDestructiveMigration(false)

                    //多实例失效机制
                    //.enableMultiInstanceInvalidation()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}