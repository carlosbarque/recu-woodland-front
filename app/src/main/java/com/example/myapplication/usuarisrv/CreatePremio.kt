package com.example.myapplication.usuarisrv

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.estructuresDades.CreatePremioModel
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

class CreatePremio : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_premio)

        val nombres = intent.getStringArrayListExtra("nombres") ?: ArrayList()
        val ids = intent.getIntegerArrayListExtra("ids") ?: ArrayList()

        val spinner = findViewById<Spinner>(R.id.kid)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, nombres)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    fun postCreatePremio(view: View) {
        val inputTitle = findViewById<EditText>(R.id.editTextTitleRewards)
        val inputDescription = findViewById<EditText>(R.id.editTextDescripcionRewards)
        val inputMonedas = findViewById<EditText>(R.id.editTextMonedasRewards)

        val title = inputTitle.text.toString()
        val description = inputDescription.text.toString()
        val monedasText = inputMonedas.text.toString()

        if (title.isBlank() || description.isBlank() || monedasText.isBlank()) {
            Toast.makeText(this, "All fields must be filled out", Toast.LENGTH_SHORT).show()
            return
        }

        val monedas = monedasText.toInt()
        val inputKid = findViewById<Spinner>(R.id.kid)
        val selectedKidPosition = inputKid.selectedItemPosition
        val token = getTokenFromStorage(this@CreatePremio)

        val ids = intent.getIntegerArrayListExtra("ids") ?: ArrayList()
        val selectedKidId = if (selectedKidPosition in ids.indices) ids[selectedKidPosition] else null

        if (selectedKidId != null) {
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

                val resposta = con.create(APIservice::class.java).postCreatePremio(
                    "api",
                    CreatePremioModel(title, description, monedas, selectedKidId)
                )
                if (resposta.isSuccessful) {
                    println("La respuesta es exitosa")
                    startActivity(Intent(this@CreatePremio, PaginaPrincipal::class.java))
                } else {
                    println("Error en la respuesta: ${resposta.errorBody()?.string()}")
                }
            }
        } else {
            println("Error: selectedKidId es null")
        }
    }

    private fun getTokenFromStorage(context: Context): String? {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("access_token", null)
    }
}

