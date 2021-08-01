package com.example.ShoppingLists

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.*

class MainActivity : AppCompatActivity(), ListAdapter.OnItemClickListener {
    private lateinit var newRecyclerView: RecyclerView
    private lateinit var newArrayList: ArrayList<Shop>
    lateinit var heading : Array<String>
    lateinit var day_created : Array<String>
    lateinit var shop_id : Array<Int>
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //recycler setup
        newRecyclerView = findViewById(R.id.shoppingListRecycler)
        newRecyclerView.layoutManager = LinearLayoutManager(this)
        newRecyclerView.setHasFixedSize(true)

        newArrayList = arrayListOf<Shop>()

       // dropDatabase()
       // recreateDatabase()
        getData()

        //archive button binding
        val goToArchive = findViewById<Button>(R.id.go_to_archive_button)
        goToArchive.setOnClickListener {
            val intent = Intent(this, ArchiveActivity::class.java)
            startActivity(intent)
        }

        val addListButton : View = findViewById<Button>(R.id.fab)
        addListButton.setOnClickListener{
            val intent = Intent(this, ListManagementActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        getData()
    }

    private fun getData(){
        newArrayList.clear()
        var db = DBHelper(this)
        var data = db.readListData()
        for (i in 0..data.size-1){
            val shop = Shop(data[i].title,data[i].date,data[i].itemId)
            newArrayList.add(shop)
        }
        newRecyclerView.adapter = ListAdapter(newArrayList, this)
    }

    override fun onItemClick(position: Int) {
        val intent = Intent(this, CheckboxesActivity::class.java)
        intent.putExtra("passedID", newArrayList[position].itemId)
        startActivity(intent)
    }

    fun dropDatabase(){
        val db = DBHelper(this).writableDatabase
        val query = "DROP TABLE IF EXISTS "+ TABLE_NAME_LIST
        db.execSQL(query)
        db.close()
    }

    fun recreateDatabase(){
        val db = DBHelper(this).writableDatabase
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
}


