package com.appcake.modsforvalheim.core.db

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.appcake.modsforvalheim.core.model.ArmorData

class DbHelper(
    context: Context
) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        val DATABASE_NAME = "APL.db"
        val DATABASE_VERSION = 1
    }

    var Database: SQLiteDatabase? = null
    override fun onCreate(db: SQLiteDatabase?) {

        //TODO ============== Create Table Start ====================
        if (db != null) {
            db.execSQL(
                """CREATE TABLE IF NOT EXISTS  `ValheimTable` (
      `id` Integer NOT NULL PRIMARY KEY AUTOINCREMENT,
      `type` Text(500) DEFAULT NULL,
      `title` Text(500) DEFAULT NULL,
      `description` Text(5000) DEFAULT NULL,
      `imageId` Text(5000) DEFAULT NULL,
      `category` Text(5000) DEFAULT NULL,
      `favorite` Integer DEFAULT NULL,
      `imageUrl` Text(5000) DEFAULT NULL
    ) ;"""
            )
        }
    }

    fun initDB() {
        val db = this.readableDatabase
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        // TODO Auto-generated method stub
        onCreate(p0)
    }

    //TODO: Armor
    fun insertInToTable(data: ArmorData): Long? {
        var idInsert: Long = -1
        val db = this.readableDatabase
        try {
            val contentValues = ContentValues()
            contentValues.put("title", data.itemTitle)
            contentValues.put("type", data.itemType)
            contentValues.put("description", data.itemDes)
            contentValues.put("imageId", data.imageId)
            contentValues.put("category", data.category)
            contentValues.put("favorite", data.favorite)
            contentValues.put("imageUrl", data.imageUrl)
            idInsert = db!!.insert("ValheimTable", null, contentValues)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("_mLog", "insert ValheimTable error = " + e.message)
        } finally {
            db?.close()
        }
        return idInsert
    }

    fun deleteAll(): Int {
        var idInsert = -1
        val sqlDeletePossibleConditions = "DELETE FROM ValheimTable"
        val db = this.readableDatabase
        try {
            db!!.execSQL(sqlDeletePossibleConditions)
            idInsert = 1
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            Log.d("_mLog", "ValheimTable error = " + e.message)
        } finally {
            db?.close()
        }
        return idInsert
    }

    fun deleteSpecficScreenData(type: String): Int {
        var idInsert = -1
        val sqlDeletePossibleConditions = "DELETE FROM ValheimTable WHERE type='" + type + "'"
        val db = this.readableDatabase
        try {
            db!!.execSQL(sqlDeletePossibleConditions)
            idInsert = 1
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            Log.d("_mLog", "ValheimTable error = " + e.message)
        } finally {
            db?.close()
        }
        return idInsert
    }

    fun deleteSpecficArmor(id: Int): Int {
        var idInsert = -1
        val sqlDeletePossibleConditions = "DELETE FROM ValheimTable where id= '" + id + "'";
        val db = this.readableDatabase
        try {
            db!!.execSQL(sqlDeletePossibleConditions)
            idInsert = 1
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            Log.d("_mLog", "ValheimTable error = " + e.message)
        } finally {
            db?.close()
        }
        return idInsert
    }


    @SuppressLint("Range")
    fun getAllData(type: String): ArrayList<ArmorData?>? {
        var res: Cursor? = null
        var list: ArrayList<ArmorData?>? = null
        var db: SQLiteDatabase? = null
        try {
            list = ArrayList<ArmorData?>()
            db = this.readableDatabase
            res = db.rawQuery("select * from ValheimTable WHERE type='" + type + "'", null)
            res.moveToFirst()
            while (res.isAfterLast == false) {
                val model = ArmorData(
                    res.getInt(res.getColumnIndex("id")),
                    res.getString(res.getColumnIndex("type")),
                    res.getString(res.getColumnIndex("title")),
                    res.getString(res.getColumnIndex("description")),
                    res.getString(res.getColumnIndex("imageId")),
                    res.getString(res.getColumnIndex("category")),
                    res.getInt(res.getColumnIndex("favorite")),
                    res.getString(res.getColumnIndex("imageUrl"))
                )
                list!!.add(model)
                res.moveToNext()
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        } finally {
            res?.close()
            db!!.close()
        }
        return list
    }

    @SuppressLint("Range")
    fun getFavorite(type: String): ArrayList<ArmorData?>? {
        var res: Cursor? = null
        var list: ArrayList<ArmorData?>? = null
        var db: SQLiteDatabase? = null
        try {
            list = ArrayList<ArmorData?>()
            db = this.readableDatabase
            res = db.rawQuery(
                "select * from ValheimTable where type ='" + type + "' AND favorite = 1",
                null
            )
            res.moveToFirst()
            while (res.isAfterLast == false) {
                val model = ArmorData(
                    res.getInt(res.getColumnIndex("id")),
                    res.getString(res.getColumnIndex("type")),
                    res.getString(res.getColumnIndex("title")),
                    res.getString(res.getColumnIndex("description")),
                    res.getString(res.getColumnIndex("imageId")),
                    res.getString(res.getColumnIndex("category")),
                    res.getInt(res.getColumnIndex("favorite")),
                    res.getString(res.getColumnIndex("imageUrl"))
                )
                list!!.add(model)
                res.moveToNext()
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        } finally {
            res?.close()
            db!!.close()
        }
        return list
    }


    fun updateFavorite(favorite: Int, id: Int): Int {
        val db = this.readableDatabase
        var idInsert = -1
        try {
            val contentValues = ContentValues()
            contentValues.put("favorite", favorite)
            idInsert = db!!.update("ValheimTable", contentValues, "id='" + id + "'", null)
        } catch (e: SQLException) {
            Log.d("SYNC", e.message!!)
            e.printStackTrace()
        } finally {
            db?.close()
        }
        return idInsert
    }

    fun updateImageUrl(id: Int,url: String): Int {
        val db = this.readableDatabase
        var idInsert = -1
        try {
            val contentValues = ContentValues()
            contentValues.put("imageUrl", url)
            idInsert = db!!.update("ValheimTable", contentValues, "id='" + id + "'", null)
        } catch (e: SQLException) {
            Log.d("SYNC", e.message!!)
            e.printStackTrace()
        } finally {
            db?.close()
        }
        return idInsert
    }

}