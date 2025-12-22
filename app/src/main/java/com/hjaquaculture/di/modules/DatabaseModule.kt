package com.hjaquaculture.di.modules

import android.content.Context
import androidx.room.Room
import com.hjaquaculture.data.local.BaseDatabase
import com.hjaquaculture.data.local.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // 全局单例
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): BaseDatabase {
        return Room.inMemoryDatabaseBuilder(
            context,
            BaseDatabase::class.java,
            //"my_db"
        ).build()
    }

    @Provides
    fun provideUserDao(db: BaseDatabase): UserDao {
        return db.userDao()
    }
}