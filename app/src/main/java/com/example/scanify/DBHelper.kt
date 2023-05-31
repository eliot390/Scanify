package com.example.scanify

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VER){

    companion object{
        const val DATABASE_NAME = "wishlist.db"
        const val DATABASE_VER = 1
        const val PROD_TABLE = "product_table"
        const val ID = "id"
        const val NAME = "name"
        const val PRICE = "price"
        const val TITLE = "title"
    }
    override fun onCreate(db: SQLiteDatabase?) {
        val createProdTable = ("CREATE TABLE "
                + PROD_TABLE + "("
                + ID + " INTEGER PRIMARY KEY, "
                + NAME + " TEXT,"
                + PRICE + " TEXT,"
                + TITLE + " TEXT" + ")")
        db?.execSQL(createProdTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $PROD_TABLE")
        onCreate(db)
    }

    fun insertItem(std: ProductModel): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(ID, std.id)
        contentValues.put(NAME, std.name)
        contentValues.put(PRICE, std.price)
        contentValues.put(TITLE, std.title)

        val success = db.insert(PROD_TABLE, null, contentValues)
        db.close()

        return success
    }

    fun getItems(): ArrayList<ProductModel> {
        val stdList: ArrayList<ProductModel> = ArrayList()
        val selectQuery = "SELECT * FROM $PROD_TABLE"
        val db = this.readableDatabase
        val cursor: Cursor?

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: Exception) {
            db.execSQL(selectQuery)
            e.printStackTrace()
            return ArrayList()
        }

        var id: Int
        var name: String
        var price: String
        var title: String

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                price = cursor.getString(cursor.getColumnIndexOrThrow("price"))
                title = cursor.getString(cursor.getColumnIndexOrThrow("title"))

                val std = ProductModel(id = id, name = name, price = price, title = title)
                stdList.add(std)
            } while (cursor.moveToNext())
        }
        return stdList
    }

    fun deleteItemById(id:Int) : Int{
        val db= this.writableDatabase

        val contentValues=ContentValues()
        contentValues.put(ID,id)

        val success = db.delete(PROD_TABLE,"id=$id",null)
        db.close()
        return success
    }
}