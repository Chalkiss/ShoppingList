package com.example.ShoppingLists

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import java.util.*
import android.database.sqlite.SQLiteDatabase

class ListManagementActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_management)
        val context = this

    val insertListButton : Button = findViewById<Button>(R.id.addListButton)
        insertListButton.setOnClickListener {
            val listName = findViewById<TextView>(R.id.listName)
            if (listName.text.toString().length >0){
               val currentDate = Date()
                var db = DBHelper(context)
                val nextId: Int = db.getNextIdInList()
                var shop = Shop(listName.text.toString(), currentDate.toString(),nextId)

                db.insertListData(shop)
                finish()
            }
        }
    }
}