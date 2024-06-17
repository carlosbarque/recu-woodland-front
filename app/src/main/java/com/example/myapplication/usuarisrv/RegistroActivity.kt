package com.example.myapplication.usuarisrv

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.estructuresDades.RegistroUsuari
import com.example.myapplication.estructuresDades.Rutes
import com.example.myapplication.estructuresDades.Usuari
import com.example.myapplication.retrofit.APIservice
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RegistroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registro)
    }

    fun postRegistrarUsuario(view: View) {
        val inputName = findViewById<EditText>(R.id.editTextTextUsername)
        val inputLogin = findViewById<EditText>(R.id.editTextTextEmailAddress)
        val inputPass = findViewById<EditText>(R.id.editTextTextPassword)
        val username = inputName.text.toString()
        val email = inputLogin.text.toString()
        val password = inputPass.text.toString()

        if (username.isBlank() || email.isBlank() || password.isBlank()) {
            Toast.makeText(this, "All fields must be filled out", Toast.LENGTH_SHORT).show()
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
            val con = Retrofit.Builder().baseUrl(Rutes.baseUrl).addConverterFactory(GsonConverterFactory.create()).client(client).build()
            val resposta = con.create(APIservice::class.java).postRegistro("api", RegistroUsuari(username, email, password))

            if (resposta.isSuccessful) {
                startActivity(Intent(this@RegistroActivity, PaginaPrincipal::class.java))
                runOnUiThread {
                    Toast.makeText(this@RegistroActivity, "You have registered successfully", Toast.LENGTH_SHORT).show()
                }
            } else {
                runOnUiThread {
                    Toast.makeText(this@RegistroActivity, "A user already exists with that email or username", Toast.LENGTH_SHORT).show()
                }
                println(resposta.errorBody()?.string())
            }
        }
    }
}
