package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.estructuresDades.Rutes
import com.example.myapplication.retrofit.APIservice
import com.example.myapplication.usuarisrv.PaginaPrincipal
import com.example.myapplication.usuarisrv.PaginaPrincipalKids
import com.example.myapplication.usuarisrv.RegistroActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun postLoginUsuari(view: View) {
        val inputLogin = findViewById<EditText>(R.id.editTextTextPassword)
        val inputPass = findViewById<EditText>(R.id.editTextTextEmailAddress)
        val nomLogin = inputLogin.text.toString()
        val passLogin = inputPass.text.toString()

        CoroutineScope(Dispatchers.IO).launch {
            val interceptor = Interceptor { chain ->
                val original = chain.request()
                val requestBuilder = original.newBuilder()
                    .header("Authorization", Credentials.basic("kotlinapp", "12345"))
                    .method(original.method, original.body)
                val request = requestBuilder.build()
                chain.proceed(request)
            }
            val client = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl(Rutes.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            val service = retrofit.create(APIservice::class.java)
            val response = service.postLogin(nomLogin, passLogin)
            if (response.isSuccessful) {
                val token = response.body()?.access_token
                val rol = response.body()?.rol
                val nombres = response.body()?.nombres
                val ids = response.body()?.kids

                if (token != null && rol != null && nombres != null && ids != null) {
                    saveDataToStorage(this@MainActivity, token, rol, nombres, ids)

                    val intent = if (rol == "ROLE_TUTOR") {

                        Intent(this@MainActivity, PaginaPrincipal::class.java)
                    } else {
                        Intent(this@MainActivity, PaginaPrincipalKids::class.java)
                    }
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "You have successfully logged in", Toast.LENGTH_SHORT).show()
                    }
                    startActivity(intent)
                }
            } else {
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "There is no user with those credentials", Toast.LENGTH_SHORT).show()
                }

                print("No te has podido Logear")
            }
        }
    }

    fun postRegisterUsuari(view: View) {
        startActivity(Intent(this@MainActivity, RegistroActivity::class.java))
    }



    private fun saveDataToStorage(context: Context, token: String, rol: String, nombres: List<String>, ids: List<Int>) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("access_token", token)
        editor.putString("rol", rol)
        editor.putStringSet("nombres", nombres.toSet())

        // Convert List<Int> to Set<String>
        val idsAsStringSet = ids.map { it.toString() }.toSet()
        editor.putStringSet("ids", idsAsStringSet)

        editor.apply()
    }

    private fun getTokenFromStorage(context: Context): String? {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("access_token", null)
    }
}
