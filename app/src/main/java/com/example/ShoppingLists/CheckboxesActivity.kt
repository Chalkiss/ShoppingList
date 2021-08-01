package com.example.ShoppingLists

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CheckboxesActivity : AppCompatActivity(), CheckboxListAdapter.OnItemClickListener {

    private lateinit var newRecyclerView: RecyclerView
    private lateinit var newArrayList: ArrayList<Item>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkboxes)

        val passedID : Int = intent.getIntExtra("passedID",0)
        val listName :String = findListNameById(passedID)

        val titleText : TextView = findViewById<TextView>(R.id.textView)
        titleText.setText(listName)
        titleText.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            val inflater = layoutInflater
            val dialogLayout = inflater.inflate(R.layout.edit_text_layout,null)
            val editText = dialogLayout.findViewById<EditText>(R.id.et)

            with(builder){
                setTitle("Enter new name...")
                setPositiveButton("Submit"){dialog, which ->
                    titleText.text = editText.text.toString()
                    val db = DBHelper(this@CheckboxesActivity)
                    db.updateItem(passedID, titleText.text.toString())
                    show()
                }
                setNegativeButton("Discard"){dialog, which ->
                    Log.d("Main", "Negative button clicked")
                }
                setView(dialogLayout)
                show()
            }
        }

        val fabButton :View = findViewById<Button>(R.id.fab2)

        fabButton.setOnClickListener{
            val db = DBHelper(this)
            val builder = AlertDialog.Builder(this)
            val inflater = layoutInflater
            val dialogLayout = inflater.inflate(R.layout.edit_text_layout,null)
            val editText = dialogLayout.findViewById<EditText>(R.id.et)
            var newId = db.getNextItemId()
            Log.d("Insertion", "item to be inserted has itemid $newId and parentid $passedID")
            var newItemName: String
            with(builder){
                setTitle("Enter name of new item")
                setPositiveButton("Submit"){dialog, which ->
                    newItemName = editText.text.toString()
                    val item = Item(newItemName,newId,passedID)
                    db.inserItemData(item)
                    Log.d("Main","values passed: $newItemName,$newId,$passedID")
                    newArrayList.clear()
                    getData(passedID)
                }
                setNegativeButton("Discard"){dialog, which ->
                    Log.d("Main", "Negative button clicked")
                }
                setView(dialogLayout)
                show()
            }

        }
        newRecyclerView = findViewById(R.id.listRecycler)
        newRecyclerView.layoutManager = LinearLayoutManager(this)
        newRecyclerView.setHasFixedSize(true)

        newArrayList = arrayListOf<Item>()

        getData(passedID)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
    private fun getData(passedID: Int){
        newArrayList.clear()
        var db = DBHelper(this)
        var data = db.readItemData(passedID)
        for (i in 0..data.size-1){
            val item = Item(data[i].name,data[i].itemId, data[i].parentId)
            Log.d("Main", "values when read: ${item.name}, ${item.itemId},${item.parentId}")
            newArrayList.add(item)
        }
        newRecyclerView.adapter = CheckboxListAdapter(newArrayList, this)
    }

    override fun onItemClick(position: Int) {
        Log.d("Removal", "Item to be removed: ${newArrayList[position].parentId},${newArrayList[position].name},${newArrayList[position].itemId}")
        val id_to_remove = newArrayList[position].parentId
        val parentid = newArrayList[position].itemId
        val db = DBHelper(this)
        db.deleteItem(id_to_remove)
        getData(parentid)
    }

    override fun onDeleteClick(position: Int) {

    }

    fun findListNameById (id:Int): String {

        val db = DBHelper(this).readableDatabase
        val query = "Select * from " + TABLE_NAME_LIST + " WHERE " + LST_COL_ID + " = '$id'"
        val result = db.rawQuery(query,null)
        var name = ""
        if(result.moveToFirst()){
            name = result.getString(2)
        }
        result.close()
        return name
    }

}