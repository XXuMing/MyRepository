package com.hjaquaculture.data.local

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

class DatabaseHelper(val context : Context ,name : String ,version: Int)
    : SQLiteOpenHelper(context,name,null,version){

    private val DATABASE_NAME = "MyDB"
    private val DATABASE_VERSION = 1
    private val str = """
            CREATE TABLE IF NOT EXISTS User(
                id integer primary key autoincrement,
                name text,
                email text,
                password text
            )
            """

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(str)
        Toast.makeText(context, "succeeded", Toast.LENGTH_SHORT).show()
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

}