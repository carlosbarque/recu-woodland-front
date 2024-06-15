package com.example.myapplication.usuarisrv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.estructuresDades.Task

class TaskkidRvAdapter(private val taskList: List<Task>, private val clickListener: (Task) -> Unit) : RecyclerView.Adapter<TaskkidViewholder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskkidViewholder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_taskkid, parent, false)
        return TaskkidViewholder(view)
    }

    override fun onBindViewHolder(holder: TaskkidViewholder, position: Int) {
        holder.bind(taskList[position], clickListener)
    }

    override fun getItemCount(): Int = taskList.size
}
