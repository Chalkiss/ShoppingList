package com.example.ShoppingLists

import android.content.ContentValues
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class ListAdapter(
        private val shoppingList: ArrayList<Shop>,
        private val listener: OnItemClickListener
        ) :
        RecyclerView.Adapter<ListAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.shopping_list_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = shoppingList[position]
        holder.heading.text = currentItem.title
        holder.day_created.text = currentItem.date

    }

    override fun getItemCount(): Int {
        return shoppingList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val archivize_button : Button = itemView.findViewById(R.id.archivize_button)
        val heading: TextView = itemView.findViewById(R.id.shopping_list_title)
        val day_created: TextView = itemView.findViewById(R.id.shopping_list_date)

        init {
            itemView.setOnClickListener(this)
            archivize_button.setOnClickListener {
                archivize(shoppingList[position].itemId, super.itemView.context)
                notifyDataSetChanged()
            }
        }

        override fun onClick(p0: View?) {
            val position :Int = adapterPosition
            if(position!= RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }

    }

    fun archivize(id:Int,context: Context){
        val db = DBHelper(context).writableDatabase
        var cv = ContentValues()
        cv.put(LST_ARCHIVED,1)
        db.update(TABLE_NAME_LIST, cv, LST_COL_ID+"=?", arrayOf(id.toString()))
    }


    interface OnItemClickListener {
        fun onItemClick(position: Int)

    }
}