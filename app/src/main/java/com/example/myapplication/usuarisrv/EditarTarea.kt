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
import com.example.myapplication.estructuresDades.Rutes
import com.example.myapplication.estructuresDades.createTask
import com.example.myapplication.retrofit.APIservice
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class EditarTarea : AppCompatActivity() {
    private var idTarea: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.editar_tarea)


        val nombreTarea = intent.getStringExtra("nombreTarea")
        val descripcionTarea = intent.getStringExtra("descripcionTarea")
        val monedasTarea = intent.getIntExtra("monedasTarea", 0)


        findViewById<EditText>(R.id.editTextTitle).setText(nombreTarea)
        findViewById<EditText>(R.id.editTextDescription).setText(descripcionTarea)
        findViewById<EditText>(R.id.editTextMonedas).setText(monedasTarea.toString())


        idTarea = intent.getIntExtra("idTarea", 0)
    }

    fun postEditTask(view: View) {
        val inputNombre = findViewById<EditText>(R.id.editTextTitle)
        val inputDescripcion = findViewById<EditText>(R.id.editTextDescription)
        val inputmonedas = findViewById<EditText>(R.id.editTextMonedas)

        val nombre = inputNombre.text.toString()
        val descripcion = inputDescripcion.text.toString()
        val monedasText = inputmonedas.text.toString()


        if (nombre.isEmpty() || descripcion.isEmpty() || monedasText.isEmpty()) {
            runOnUiThread {
                Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
            }
            return
        }

        val monedas = monedasText.toInt()

        val token = getTokenFromStorage(this@EditarTarea)

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

            val resposta = con.create(APIservice::class.java).putEditarTask("api", createTask(nombre, descripcion, monedas, idTarea))
            if (resposta.isSuccessful) {
                runOnUiThread {
                    Toast.makeText(this@EditarTarea, "Task updated successfully", Toast.LENGTH_SHORT).show()
                }
                startActivity(Intent(this@EditarTarea, PaginaPrincipal::class.java))
            } else {
                runOnUiThread {
                    Toast.makeText(this@EditarTarea, "Error al actualizar la tarea", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getTokenFromStorage(context: Context): String? {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("access_token", null)
    }
}
