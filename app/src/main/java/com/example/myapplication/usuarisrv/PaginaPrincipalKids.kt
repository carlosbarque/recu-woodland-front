package com.example.myapplication.usuarisrv

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.estructuresDades.EditarUsuari

class PaginaPrincipalKids : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.paginaprincipalkids)
    }
    fun postCreateTerea(view: View){
        startActivity(Intent(this@PaginaPrincipalKids, TereaActivity::class.java))
    }

    fun postCreatePremio(view: View){
        startActivity(Intent(this@PaginaPrincipalKids, CreatePremio::class.java))
    }

    fun postAñadirEnlace(view: View){
        startActivity(Intent(this@PaginaPrincipalKids, CreateKid::class.java))
    }

    fun getKidTask(view: View){
        startActivity(Intent(this@PaginaPrincipalKids, ListKidActivity::class.java))
    }



    fun postUpdatePremio(view: View){
        startActivity(Intent(this@PaginaPrincipalKids, TereaActivity::class.java))
    }

    fun postEditarUsuario(view: View){
        startActivity(Intent(this@PaginaPrincipalKids, EditarActivity::class.java))
    }
}