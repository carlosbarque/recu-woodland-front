package com.example.myapplication.usuarisrv

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
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

class VerificateTarea : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_task)
    }

    fun postVerificateTask(view: View) {
        val id = this.taskId
        val token = getTokenFromStorage(this@VerificateTarea)
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
            val retrofit = Retrofit.Builder()
                .baseUrl(Rutes.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            val service = retrofit.create(APIservice::class.java)
            val response = service.postVericateTask("api", id)
            if (response.isSuccessful) {
                val task = response.body()?.task
                if (task != null) {
                    val intent = Intent(this@VerificateTarea, PaginaPrincipal::class.java)
                    intent.putExtra("nombre", task.nombre)
                    intent.putExtra("description", task.descripcion)
                    intent.putExtra("monedas", task.monedas)
                    startActivity(intent)
                }
                println("Task verificated successfully")
                startActivity(Intent(this@VerificateTarea, PaginaPrincipal::class.java))

            } else {
                println("Error verificating task: ${response.errorBody()?.string()}")
            }
        }
    }


    fun postDeleteTask(view: View) {
        val id = this.taskId
        val token = getTokenFromStorage(this@VerificateTarea)
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
            val retrofit = Retrofit.Builder()
                .baseUrl(Rutes.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            val service = retrofit.create(APIservice::class.java)
            val response = service.deleteTask("api", id)
            if (response.isSuccessful) {

                println("Task removed successfully")
                startActivity(Intent(this@VerificateTarea, PaginaPrincipal::class.java))

            } else {
                println("Error removing task: ${response.errorBody()?.string()}")
            }
        }
    }

    private fun getTokenFromStorage(context: Context): String? {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("access_token", null)
    }
}