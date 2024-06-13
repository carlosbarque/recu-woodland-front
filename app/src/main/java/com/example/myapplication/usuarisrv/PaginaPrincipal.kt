package com.example.myapplication.usuarisrv

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R

class PaginaPrincipal : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.paginaprincipal)
    }

    fun postCreateTerea(view: View){
        val sharedPreferences: SharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val nombres = sharedPreferences.getStringSet("nombres", emptySet())?.toList() ?: emptyList()
        val ids = sharedPreferences.getStringSet("ids", emptySet())?.map { it.toInt() } ?: emptyList()

        val intent = Intent(this@PaginaPrincipal, TereaActivity::class.java).apply {
            putStringArrayListExtra("nombres", ArrayList(nombres))
            putIntegerArrayListExtra("ids", ArrayList(ids))
        }
        startActivity(intent)
    }

    fun postCreatePremio(view: View){
        val sharedPreferences: SharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val nombres = sharedPreferences.getStringSet("nombres", emptySet())?.toList() ?: emptyList()
        val ids = sharedPreferences.getStringSet("ids", emptySet())?.map { it.toInt() } ?: emptyList()

        val intent = Intent(this@PaginaPrincipal, CreatePremio::class.java).apply {
            putStringArrayListExtra("nombres", ArrayList(nombres))
            putIntegerArrayListExtra("ids", ArrayList(ids))
        }
        startActivity(intent)
    }

    fun postAñadirEnlace(view: View){
        startActivity(Intent(this@PaginaPrincipal, CreateKid::class.java))
    }

    fun postUpdateTask(view: View){
        startActivity(Intent(this@PaginaPrincipal, ModifyActivity::class.java))
    }

    fun postUpdatePremio(view: View){
        startActivity(Intent(this@PaginaPrincipal, TereaActivity::class.java))
    }

    fun postEditarUsuario(view: View){
        startActivity(Intent(this@PaginaPrincipal, EditarActivity::class.java))
    }
}
