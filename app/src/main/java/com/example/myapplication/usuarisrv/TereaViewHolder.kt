package com.example.myapplication.usuarisrv

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.estructuresDades.Rutes
import com.example.myapplication.estructuresDades.Task
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

class TereaViewHolder(view: View, private val clickListener: TereaRvAdapter.ClickListener?)
    : RecyclerView.ViewHolder(view), View.OnClickListener {

    val tv_rv_nom: TextView = view.findViewById(R.id.tvNombre)
    val tv_rv_descripcion: TextView = view.findViewById(R.id.tvDescripcion)
    val tv_rv_monedas: TextView = view.findViewById(R.id.tvMonedas)
    val but1: Button = view.findViewById(R.id.btnMark)
    val but2: Button = view.findViewById(R.id.btnDelete)
    val but3: Button = view.findViewById(R.id.btnEdit)

    init {
        if (clickListener != null) {
            itemView.setOnClickListener(this)
        }
        itemView.setOnClickListener(this)
    }

    fun bindClickBtnMark(data: Task) {
        but1.setOnClickListener {
            postVerificateTask(data)
        }
    }

    fun bind2ClickBtnDelete(data: Task) {
        but2.setOnClickListener {
            postDeleteTask(data)
        }
    }

    fun bindClickBtnEdit(data: Task) {
        but3.setOnClickListener {
            val context = but3.context
            val intent = Intent(context, EditarTarea::class.java).apply {
                putExtra("idTarea", data.id)
                putExtra("nombreTarea", data.nombre)
                putExtra("descripcionTarea", data.descripcion)
                putExtra("monedasTarea", data.monedas)
            }
            context.startActivity(intent)
        }
    }

    private fun postVerificateTask(data: Task) {
        val context = but1.context
        val token = getTokenFromStorage(context)
        CoroutineScope(Dispatchers.IO).launch {
            val client = createOkHttpClient(token)
            val retrofit = createRetrofit(client)
            val service = retrofit.create(APIservice::class.java)
            val response = service.postVericateTask("api", data.id)
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Task verified successfully", Toast.LENGTH_SHORT).show()
                    val intent = Intent(context, PaginaPrincipal::class.java)
                    context.startActivity(intent)
                }
                Log.e("Resultado", "La llamada ha sido exitosa")
            } else {
                withContext(Dispatchers.Main) {
                    Log.e("Resultado", "Error en la llamada: ${response.errorBody()?.string()}")
                }
            }
        }
    }

    private fun postDeleteTask(data: Task) {
        val context = but1.context
        val token = getTokenFromStorage(context)
        CoroutineScope(Dispatchers.IO).launch {
            val client = createOkHttpClient(token)
            val retrofit = createRetrofit(client)
            val service = retrofit.create(APIservice::class.java)
            val response = service.deleteTask("api", data.id)
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Task deleted successfully", Toast.LENGTH_SHORT).show()
                    val intent = Intent(context, PaginaPrincipal::class.java)
                    context.startActivity(intent)
                }
                Log.e("Resultado", "La llamada ha sido exitosa")
            } else {
                withContext(Dispatchers.Main) {
                    Log.e("Resultado", "Error en la llamada: ${response.errorBody()?.string()}")
                }
            }
        }
    }

    override fun onClick(v: View?) {
        if (v != null) {
            clickListener?.onItemClick(v, adapterPosition)
        }
    }

    private fun getTokenFromStorage(context: Context): String? {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
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

    fun printTerea(task: Task) {
        tv_rv_nom.text = task.nombre
        tv_rv_descripcion.text = task.descripcion
        tv_rv_monedas.text = task.monedas.toString()
    }
}
