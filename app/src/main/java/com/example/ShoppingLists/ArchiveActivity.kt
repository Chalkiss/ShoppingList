package com.example.ShoppingLists

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.*

class ArchiveActivity : AppCompatActivity(), ListAdapter.OnItemClickListener {

    private lateinit var newRecyclerView: RecyclerView
    private lateinit var newArrayList: ArrayList<Shop>
    lateinit var heading : Array<String>
    lateinit var day_created : Array<String>
    lateinit var shop_id : Array<Int>

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_archive)

        // data for recyclerview storage

        //recyclerview binding
        newRecyclerView = findViewById(R.id.shoppingListRecycler)
        newRecyclerView.layoutManager = LinearLayoutManager(this)
        newRecyclerView.setHasFixedSize(true)
        newArrayList = arrayListOf<Shop>()
        getData()

        //go to current button binding
        val goToCurrent = findViewById<Button>(R.id.go_to_current_button)
        goToCurrent.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
    private fun getData(){
        var db = DBHelper(this)
        var data = db.readArchivedData()
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
}


