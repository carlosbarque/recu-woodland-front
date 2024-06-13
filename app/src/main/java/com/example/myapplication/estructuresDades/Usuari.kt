package com.example.myapplication.estructuresDades

import com.google.gson.annotations.SerializedName

data class Usuari(val nom:String, val cognom:String, val edat:Int, @SerializedName("img") val imatge:String)
