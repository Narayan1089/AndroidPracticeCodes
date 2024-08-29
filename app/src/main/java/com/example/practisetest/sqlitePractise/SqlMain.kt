package com.example.practisetest.sqlitePractise

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.practisetest.R


// Define a data class to represent the user
data class User(val id: Long, val name: String, val email: String)

class SqlMain : AppCompatActivity() {

    private inner class NewDB(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME,null, DATABASE_VERSION) {

        private val TABLE_NAME = "noo"
        private val COLUMN_ID = "noo"
        private val COLUMN_NAME = "noo"
        private val COLUMN_EMAIL = "noo"

        private val TABLE_CREATE = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT,
                $COLUMN_EMAIL TEXT
            )
        """


        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(TABLE_CREATE)
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
            onCreate(db)
        }

    }

    // Database helper class
    private inner class MyDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

        // Constants for database
        private val TABLE_NAME = "users"
        private val COLUMN_ID = "id"
        private val COLUMN_NAME = "name"
        private val COLUMN_EMAIL = "email"

        // SQL query to create table
        private val TABLE_CREATE = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT,
                $COLUMN_EMAIL TEXT
            )
        """

        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(TABLE_CREATE)
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
            onCreate(db)
        }

        // Add a new user
        fun addUser(name: String, email: String) {
            val db = writableDatabase
            val values = ContentValues().apply {
                put(COLUMN_NAME, name)
                put(COLUMN_EMAIL, email)
            }
            db.insert(TABLE_NAME, null, values)
            db.close()
        }

        // Get all users
        fun getAllUsers(): List<User> {
            val users = mutableListOf<User>()
            val db = readableDatabase
            val cursor: Cursor = db.query(TABLE_NAME, null, null, null, null, null, null)

            with(cursor) {
                while (moveToNext()) {
                    val id = getLong(getColumnIndexOrThrow(COLUMN_ID))
                    val name = getString(getColumnIndexOrThrow(COLUMN_NAME))
                    val email = getString(getColumnIndexOrThrow(COLUMN_EMAIL))
                    users.add(User(id, name, email))
                }
            }
            cursor.close()
            db.close()
            return users
        }

        // Update a user
        fun updateUser(id: Long, name: String, email: String) {
            val db = writableDatabase
            val values = ContentValues().apply {
                put(COLUMN_NAME, name)
                put(COLUMN_EMAIL, email)
            }
            db.update(TABLE_NAME, values, "$COLUMN_ID = ?", arrayOf(id.toString()))
            db.close()
        }

        // Delete a user
        fun deleteUser(id: Long) {
            val db = writableDatabase
            db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(id.toString()))
            db.close()
        }
    }

    // Instance of the database helper
    private lateinit var dbHelper: MyDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sql_main)

        dbHelper = MyDatabaseHelper(this)

        // Adding a new user
        dbHelper.addUser("John Doe", "john@example.com")

        // Getting all users
        val users = dbHelper.getAllUsers()
        for (user in users) {
            println("User: ${user.name}, Email: ${user.email}")
        }

        // Updating a user (assuming we have an id)
        val userIdToUpdate = 1L
        dbHelper.updateUser(userIdToUpdate, "Jane Doe", "jane@example.com")

        // Deleting a user (assuming we have an id)
        val userIdToDelete = 1L
        dbHelper.deleteUser(userIdToDelete)
    }


    companion object {
        private const val DATABASE_NAME = "mydatabase.db"
        private const val DATABASE_VERSION = 1
    }
}