package com.azat_sabirov.mynoteskotlin_new.db

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.azat_sabirov.mynoteskotlin_new.EditActivity
import com.azat_sabirov.mynoteskotlin_new.R

class MyAdapter(listMain: ArrayList<ListItem>, context: Context) :
    RecyclerView.Adapter<MyAdapter.MyHolder>() {

    private val listArray = listMain
    private val context = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rc_item, parent, false)
        return MyHolder(view, context)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.setData(listArray[position])
    }

    override fun getItemCount(): Int {
        return listArray.size
    }

    fun upgradeAdapter(newList: List<ListItem>) {
        listArray.clear()
        listArray.addAll(newList)
        notifyDataSetChanged()
    }

    fun removeItem(pos: Int, dbManager: MyDbManager) {
        dbManager.removeItemFromDb(listArray[pos].id.toString())
        listArray.removeAt(pos)
        notifyItemRangeChanged(0, listArray.size)
        notifyItemRemoved(pos)

    }

    class MyHolder(itemView: View, context: Context) : RecyclerView.ViewHolder(itemView) {
        val context = context
        private val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
        private val tvTime = itemView.findViewById<TextView>(R.id.tvTime)
        fun setData(item: ListItem) {
            tvTitle.text = item.title
            tvTime.text = item.time
            itemView.setOnClickListener {
                val i = Intent(context, EditActivity::class.java).apply {
                    putExtra(MyIntentConstants.I_TITLE_KEY, item.title)
                    putExtra(MyIntentConstants.I_CONTENT_KEY, item.content)
                    putExtra(MyIntentConstants.I_URI_KEY, item.uri)
                    putExtra(MyIntentConstants.I_ID_KEY, item.id)
                }
                context.startActivity(i)
            }
        }
    }
}