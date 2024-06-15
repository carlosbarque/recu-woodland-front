package com.example.myapplication.usuarisrv

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.estructuresDades.EditarTaskObject
import com.example.myapplication.estructuresDades.EditarUsuari
import com.example.myapplication.estructuresDades.Rutes
import com.example.myapplication.estructuresDades.Usuari
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
class EditarTarea : AppCompatActivity(){
    private var idTarea: Int = 0 // Inicializar ID de la tarea

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.editar_tarea)

        // Obtener el ID de la tarea desde el Intent
        idTarea = intent.getIntExtra("idTarea", 0)
    }

    fun postEditTask(view: View){
        val inputNombre = findViewById<EditText>(R.id.editTextTitle)
        val inputDescripcion = findViewById<EditText>(R.id.editTextDescription)
        val inputmonedas = findViewById<EditText>(R.id.editTextMonedas)

        val nombre = inputNombre.text.toString()
        val descripcion = inputDescripcion.text.toString()
        val monedas = inputmonedas.text.toString().toInt()

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
            if(resposta.isSuccessful){
                println("La respuesta es correcta!")
                startActivity(Intent(this@EditarTarea, PaginaPrincipal::class.java))
                println(resposta.body())
            } else{
                println(resposta.errorBody()?.string())
            }
        }
    }

    private fun getTokenFromStorage(context: Context): String? {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("access_token", null)
    }
}
