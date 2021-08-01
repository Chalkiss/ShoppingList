package com.example.ShoppingLists

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast
import java.io.File

val DB_NAME = "ShoppingListDatabase"
val TABLE_NAME_LIST = "Lists"
val LST_COL_NAME = "List_Name"
val LST_COL_DATE = "List_Date"
val LST_COL_ID = "List_ID"
val LST_ARCHIVED = "List_Archived"
val TABLE_NAME_LIST_ITEM = "List_Item"
val LIST_ITEM_COL_NAME = "Item_Name"
val LIST_ITEM_PARENT_ID = "Parent_ID"
val LIST_ITEM_ID = "Item_ID"

class DBHelper(var context: Context) : SQLiteOpenHelper(context, DB_NAME, null, 1){
    override fun onCreate(db: SQLiteDatabase?) {
        val createListTable = "CREATE TABLE "+ TABLE_NAME_LIST + " (" +
                LST_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                LST_COL_DATE + " VARCHAR(12), " +
                LST_COL_NAME + " VARCHAR(50), " +
                LST_ARCHIVED + " INTEGER);"

        val createListItemTable = "CREATE TABLE "+ TABLE_NAME_LIST_ITEM + " ( " +
                LIST_ITEM_COL_NAME + " VARCHAR (50) , " +
                LIST_ITEM_PARENT_ID + " INTEGER, " +
                LIST_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT);"

        db?.execSQL(createListTable)
        db?.execSQL(createListItemTable)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }

    fun insertListData(shop: Shop) {
        val db = this.writableDatabase
        var cv = ContentValues()
        cv.put(LST_COL_DATE, shop.date)
        cv.put(LST_COL_NAME, shop.title)
        cv.put(LST_COL_ID, shop.itemId)
        cv.put(LST_ARCHIVED, shop.isArchived)
        var result = db.insert(TABLE_NAME_LIST, null, cv)
        if (result == -1.toLong()) {
            Toast.makeText(context, "failed to insert", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(context, "inserted", Toast.LENGTH_SHORT).show()
        }
    }

    fun inserItemData(item: Item) {
        val db = this.writableDatabase
        var cv = ContentValues()
        cv.put(LIST_ITEM_COL_NAME, item.name)
        cv.put(LIST_ITEM_PARENT_ID, item.parentId)
        cv.put(LIST_ITEM_ID, item.itemId)
        var result = db.insert(TABLE_NAME_LIST_ITEM, null, cv)
        if(result == -1.toLong()){
            Toast.makeText(context, "failed to insert", Toast.LENGTH_SHORT).show()
    }else{
        Toast.makeText(context, "inserted", Toast.LENGTH_SHORT).show()
        }
    }

    fun readListData() : MutableList<Shop>{
        var list : MutableList<Shop> = ArrayList()

        val db = this.readableDatabase
        val query = "Select * from " + TABLE_NAME_LIST + " WHERE " + LST_ARCHIVED + "= 0"
        val result = db.rawQuery(query, null)
        if(result.moveToFirst()){
            do{
                var shop = Shop()
                shop.itemId = result.getString(0).toInt()
                shop.title = result.getString(2)
                shop.date = result.getString(1)
                list.add(shop)
            }while(result.moveToNext())
        }
        result.close()
        return list
    }

    fun readArchivedData() : MutableList<Shop>{
        var list : MutableList<Shop> = ArrayList()

        val db = this.readableDatabase
        val query = "Select * from " + TABLE_NAME_LIST + " WHERE " + LST_ARCHIVED + "= 1"
        val result = db.rawQuery(query, null)
        if(result.moveToFirst()){
            do{
                var shop = Shop()
                shop.itemId = result.getString(0).toInt()
                shop.title = result.getString(2)
                shop.date = result.getString(1)
                list.add(shop)
            }while(result.moveToNext())
        }
        result.close()
        return list
    }

    fun readItemData(parentId : Int): MutableList<Item>{
        var list : MutableList<Item> = ArrayList()
        val db = this.readableDatabase
        val query = "Select * from " + TABLE_NAME_LIST_ITEM + " WHERE " + LIST_ITEM_PARENT_ID + "= $parentId"
        val result = db.rawQuery(query, null)
        if(result.moveToFirst()){
            do{
                var item = Item()
                item.itemId = result.getInt(1)
                item.name = result.getString(0)
                item.parentId = result.getInt(2)
                list.add(item)
            }while(result.moveToNext())
        }
        result.close()
        return list
    }


    fun getNextIdInList() :Int{
        val db = this.readableDatabase
        var nextId :Int = 0
        val query : String = "SELECT MAX(" + LST_COL_ID+") AS max_id FROM "+ TABLE_NAME_LIST
        val result = db.rawQuery(query,null)
        if(result.moveToFirst())
        {
            do
            {
                nextId = result.getInt(0)
            }while(result.moveToNext())
        }
        return nextId+1
    }

    fun getNextItemId(): Int{
        val db = this.readableDatabase
        var nextId = 0
        val query : String = "SELECT MAX(" + LIST_ITEM_ID +") AS max_id FROM " + TABLE_NAME_LIST_ITEM
        val result = db.rawQuery(query,null)
        if(result.moveToFirst())
        {
            do
            {
                nextId = result.getInt(0)
            }while(result.moveToNext())
        }
        return nextId+1
    }

    fun deleteItem(id:Int){
        val db = this.writableDatabase
        db.delete(TABLE_NAME_LIST_ITEM, LIST_ITEM_ID+"=?",arrayOf(id.toString()))
        db.close()
    }

    fun updateItem(id:Int, name:String){
        val db = this.writableDatabase
        var cv = ContentValues()
        cv.put(LST_COL_NAME,name)
        db.update(TABLE_NAME_LIST, cv, LST_COL_ID+"=?", arrayOf(id.toString()))
    }

    fun archivize(id:Int){
        val db = this.writableDatabase
        var cv = ContentValues()
        cv.put(LST_ARCHIVED,1)
        db.update(TABLE_NAME_LIST, cv, LST_COL_ID+"=?", arrayOf(id.toString()))
    }



}