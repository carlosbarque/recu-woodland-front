package com.example.myapplication.usuarisrv

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.estructuresDades.Task

class TereaRvAdapter(private val tasks: MutableList<Task>) : RecyclerView.Adapter<TereaViewHolder>() {
    private var clickListener: ClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TereaViewHolder {
        val layoutInflate = LayoutInflater.from(parent.context)
        return TereaViewHolder(layoutInflate.inflate(R.layout.item_task, parent, false), clickListener)
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    override fun onBindViewHolder(holder: TereaViewHolder, position: Int) {
        holder.printTerea(tasks[position])
        val item = tasks[position]
        holder.bindClickBtnMark(item)
        //holder.bind2ClickBtnDelete(item)
    }

    fun setOnItemClickListener(clickListener: ClickListener) {
        this.clickListener = clickListener
    }

    interface ClickListener {
        fun onItemClick(v: View, position: Int)
    }
}
