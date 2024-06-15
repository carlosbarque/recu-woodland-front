package com.example.myapplication.usuarisrv

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.estructuresDades.Rutes
import com.example.myapplication.estructuresDades.Task
import com.example.myapplication.estructuresDades.TereaProvider
import com.example.myapplication.estructuresDades.createTask
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

class ModifyActivity : AppCompatActivity() {
    private lateinit var tareaAdapter: TereaRvAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.modifyterea)

        initRecyclerView()
    }


    private fun initRecyclerView() {
        val tareasList: MutableList<Task> = TereaProvider.terea
        val rey = findViewById<RecyclerView>(R.id.recyclerView)
        rey.layoutManager = LinearLayoutManager(this)
        tareaAdapter = TereaRvAdapter(tareasList)

        rey.adapter = tareaAdapter
        tareaAdapter.setOnItemClickListener(object : TereaRvAdapter.ClickListener {
            override fun onItemClick(v: View, position: Int) {
                println("onItemClick")
                Toast.makeText(
                    this@ModifyActivity, "Clic: ${tareaAdapter.getItemId(position)}",
                    Toast.LENGTH_LONG
                ).show()
            }
        })


        val token = getTokenFromStorage(this@ModifyActivity)
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
            val response = service.getModifyTask("api")
            if (response.isSuccessful) {
                val tasks = response.body() ?: emptyList()
                withContext(Dispatchers.Main) {
                    tareasList.clear()
                    tareasList.addAll(tasks)
                    tareaAdapter.notifyDataSetChanged()
                    println("show task list")
                    Log.e("Tareas", tareasList.toString())
                }
                //tareasList.clear()
                //tareasList.addAll(tasks)
                //tareaAdapter.notifyDataSetChanged()
                //println("Task display successfully")
            } else {
                println("Error creating task: ${response.errorBody()?.string()}")
            }
        }

    }


    private fun getTokenFromStorage(context: Context): String? {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("access_token", null)
    }
}