package com.hjaquaculture

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.hjaquaculture.data.local.BaseDatabase
import com.hjaquaculture.data.local.dao.UserDao
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.hjaquaculture", appContext.packageName)
    }
}

@RunWith(AndroidJUnit4::class)
class UserDaoTest {
    private lateinit var db: BaseDatabase
    private lateinit var userDao: UserDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        // 创建内存数据库，数据仅存在于进程生命周期内
        db = Room.inMemoryDatabaseBuilder(context, BaseDatabase::class.java)
            // 允许在主线程执行查询（仅用于测试，简化代码）
            .allowMainThreadQueries()
            .build()
        userDao = db.userDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }
/*
    @Test
    @Throws(Exception::class)
    fun writeUserAndReadInList() = runTest {
        val user = User(id = 1, name = "Gemini")
        userDao.insert(user)
        val byName = userDao.findByName("Gemini")
        assertThat(byName, equalTo(user))
    }
*/
}