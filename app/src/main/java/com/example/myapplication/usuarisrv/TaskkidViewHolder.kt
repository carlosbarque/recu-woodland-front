package com.example.myapplication.usuarisrv

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.estructuresDades.Task

class TaskkidViewholder(view: View) : RecyclerView.ViewHolder(view) {
    private val tvNombre: TextView = view.findViewById(R.id.tvNombre)
    private val tvDescripcion: TextView = view.findViewById(R.id.tvDescripcion)
    private val tvMonedas: TextView = view.findViewById(R.id.tvMonedas)
    //private val btnMark: Button = view.findViewById(R.id.btnMark)

    fun bind(task: Task, clickListener: (Task) -> Unit) {
        tvNombre.text = task.nombre
        tvDescripcion.text = task.descripcion
        tvMonedas.text = task.monedas.toString()

        /*btnMark.setOnClickListener {
            clickListener(task)
        }*/
    }
}
