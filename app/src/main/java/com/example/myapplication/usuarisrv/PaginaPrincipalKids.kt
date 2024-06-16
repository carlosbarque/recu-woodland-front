package com.example.myapplication.usuarisrv

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.estructuresDades.EditarUsuari

class PaginaPrincipalKids : AppCompatActivity() {
    private lateinit var editTextKidId: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.paginaprincipalkids)
        editTextKidId = findViewById(R.id.editTextKidId)
        println(editTextKidId)
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

    /*fun getKidTask(view: View){
        val kidIdStr = editTextKidId.text.toString()
        /*kidId
        val intent = Intent(this, ListKidActivity::class.java)
        intent.putExtra("KID_ID", kidId)  // Pass the kidId dynamically
        startActivity(intent)*/
        startActivity(Intent(this@PaginaPrincipalKids, ListKidActivity::class.java))
    }*/
    fun getKidTask(view: View){
        val kidIdStr = editTextKidId.text.toString().trim()
        if (kidIdStr.isNotEmpty()) {
            val kidId = kidIdStr.toIntOrNull()
            if (kidId != null) {
                openListKidActivity(kidId)
            } else {
                Toast.makeText(this, "Invalid Kid ID", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Please enter Kid ID", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openListKidActivity(kidId: Int) {
        val intent = Intent(this@PaginaPrincipalKids, ListKidActivity::class.java)
        intent.putExtra("KID_ID", kidId)
        startActivity(intent)
    }

    fun postUpdateTask(view: View){
        startActivity(Intent(this@PaginaPrincipalKids, EditarActivity::class.java))
    }



    fun postUpdatePremio(view: View){
        startActivity(Intent(this@PaginaPrincipalKids, TereaActivity::class.java))
    }

    fun postEditarUsuario(view: View){
        startActivity(Intent(this@PaginaPrincipalKids, EditarActivity::class.java))
    }
    fun logout(view: View){
        startActivity(Intent(this@PaginaPrincipalKids, MainActivity::class.java))
    }


}