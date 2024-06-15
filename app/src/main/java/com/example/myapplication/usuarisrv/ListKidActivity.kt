package com.example.myapplication.usuarisrv

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.estructuresDades.Rutes
import com.example.myapplication.retrofit.APIservice
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ListKidActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var taskkidRvAdapter: TaskkidRvAdapter
    private lateinit var apiService: APIservice

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.listkidtask)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val token = getTokenFromStorage(this)
        val client = createOkHttpClient(token)
        val retrofit = createRetrofit(client)
        apiService = retrofit.create(APIservice::class.java)

        fetchTasksForKid(2)
    }

    private fun fetchTasksForKid(kidId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = apiService.getKidTask("api", kidId)
            if (response.isSuccessful) {
                val taskList = response.body()
                withContext(Dispatchers.Main) {
                    if (taskList != null) {
                        taskkidRvAdapter = TaskkidRvAdapter(taskList) { task ->
                            // Handle item click
                            Toast.makeText(this@ListKidActivity, "Task: ${task.nombre}", Toast.LENGTH_SHORT).show()
                        }
                        recyclerView.adapter = taskkidRvAdapter
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ListKidActivity, "Failed to fetch tasks", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getTokenFromStorage(context: Context): String? {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("access_token", null)
    }

    private fun createOkHttpClient(token: String?): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val authInterceptor = Interceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
                .header("Authorization", "Bearer $token")
            val request = requestBuilder.build()
            chain.proceed(request)
        }
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(authInterceptor)
            .build()
    }

    private fun createRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Rutes.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

}