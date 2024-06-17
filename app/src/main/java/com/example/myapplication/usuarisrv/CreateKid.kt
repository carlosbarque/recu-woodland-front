package com.example.myapplication.usuarisrv

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.estructuresDades.CreateKid
import com.example.myapplication.estructuresDades.Rutes
import com.example.myapplication.retrofit.APIservice
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CreateKid : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.createuserkid)
    }

    fun postCreateKid(view: View) {
        val inputUsername = findViewById<EditText>(R.id.editTextTextUsername)
        val inputPass = findViewById<EditText>(R.id.editTextTextPassword)
        val inputEmail = findViewById<EditText>(R.id.editTextTextEmailAddress)
        val nomLogin = inputUsername.text.toString()
        val passLogin = inputPass.text.toString()
        val emailLogin = inputEmail.text.toString()

        if (nomLogin.isBlank() || passLogin.isBlank() || emailLogin.isBlank()) {
            Toast.makeText(this, "All fields must be filled out", Toast.LENGTH_SHORT).show()
            return
        }

        val token = getTokenFromStorage(this@CreateKid)
        CoroutineScope(Dispatchers.IO).launch {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val authInterceptor = Interceptor { chain ->
                val original = chain.request()
                val requestBuilder = original.newBuilder()
                    .header("Authorization", "Bearer $token")
                val request = requestBuilder.build()
                chain.proceed(request)
            }
            val client = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(authInterceptor)
                .build()

            val con = Retrofit.Builder().baseUrl(Rutes.baseUrl)
                .addConverterFactory(GsonConverterFactory.create()).client(client).build()

            val resposta = con.create(APIservice::class.java).postCreateKid("api", CreateKid(nomLogin, passLogin, emailLogin))
            if (resposta.isSuccessful) {
                println("La respuesta es exitosa")
                startActivity(Intent(this@CreateKid, PaginaPrincipal::class.java))

                runOnUiThread {
                    Toast.makeText(this@CreateKid, "The child has been successfully registered", Toast.LENGTH_SHORT).show()
                }
            } else {
                runOnUiThread {
                    Toast.makeText(this@CreateKid, "A user already exists with that email or username", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getTokenFromStorage(context: Context): String? {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("access_token", null)
    }
}
