package com.azat_sabirov.mynoteskotlin_new.db

import android.provider.BaseColumns

object MyDbNameClass {
    const val TABLE_NAME = "my_table"
    const val COLUMN_NAME_TITLE = "title"
    const val COLUMN_NAME_CONTENT = "content"
    const val COLUMN_NAME_URI = "uri"
    const val COLUMN_NAME_TIME = "time"

    const val DATABASE_VERSION = 1
    const val DATABASE_NAME = "MyDb.db"

    const val SQL_CREATE_ENTRIES =
        "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY, $COLUMN_NAME_TITLE TEXT, $COLUMN_NAME_CONTENT TEXT, $COLUMN_NAME_URI TEXT, $COLUMN_NAME_TIME TEXT)"

    const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS $TABLE_NAME"
}