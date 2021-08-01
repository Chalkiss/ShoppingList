package com.example.ShoppingLists

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class CheckboxListAdapter(
        private val itemList: ArrayList<Item>,
        private val listener: OnItemClickListener
        ) :
        RecyclerView.Adapter<CheckboxListAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.heading.text = currentItem.name
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val delete_button : Button = itemView.findViewById(R.id.delete_button)
        val heading: TextView = itemView.findViewById(R.id.checkboxdescription)

        init {
            itemView.setOnClickListener(this)
            delete_button.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val position :Int = adapterPosition
            if(position!= RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
        fun onDeleteClick(p0: View?){
            fun onDeleteClick(p0: View?){
                val position :Int = adapterPosition
                if(position!= RecyclerView.NO_POSITION) {
                    listener.onDeleteClick(position)
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
        fun onDeleteClick(position: Int)
    }
}