package com.example.myapplication.usuarisrv

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.estructuresDades.CreateTutor
import com.example.myapplication.estructuresDades.RegistroUsuari
import com.example.myapplication.estructuresDades.Rutes
import com.example.myapplication.retrofit.APIservice
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CreateTutor : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registro)
        /*val buttonClick = findViewById<Button>(R.id.button)
        buttonClick.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }*/
    }

    fun postCreateTutor(view: View) {
        val inputUsername = findViewById<EditText>(R.id.editTextTextUsername)
        val inputPass = findViewById<EditText>(R.id.editTextTextPassword)
        val inputEmail = findViewById<EditText>(R.id.editTextTextEmailAddress)
        val nomLogin = inputUsername.text.toString()
        val passLogin = inputPass.text.toString()
        val emailLogin = inputEmail.text.toString()
        CoroutineScope(Dispatchers.IO).launch {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
            val con = Retrofit.Builder().baseUrl(Rutes.baseUrl)
                .addConverterFactory(GsonConverterFactory.create()).client(client).build()
            var resposta = con.create(APIservice::class.java).postRegistro("api",
                RegistroUsuari(nomLogin,passLogin,emailLogin)
            )
            if (resposta.isSuccessful){
                println("La respuesta es exitosa")
            }else{
                println("Error en la respuesta: ${resposta.errorBody()?.string()}")
            }
        }
    }
}