package com.patienttracking.utilities

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DB_Helper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    // below is the method for creating a database by a sqlite query
    override fun onCreate(db: SQLiteDatabase) {
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
    }

    fun createTable(query : String){
        val db = this.writableDatabase
        db.execSQL(query)
    }

    fun add(values : ContentValues, table: String) {
        val db = this.writableDatabase
        db.insert(table, null, values)
        db.close()
    }

    fun retrieve(query: String): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery(query, null)
    }

    fun update(table: String, values : ContentValues, id: String, where: String) {
        val db = this.writableDatabase
        db.update(table, values, "$where=?", arrayOf(id))
        db.close()
    }

    companion object{
        private val DATABASE_NAME = "GEEKS_FOR_GEEKS"
        private val DATABASE_VERSION = 1
    }
}