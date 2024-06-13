package com.example.myapplication.estructuresDades

class UsuariProvider {
    companion object{
        var rutaImatges = "http://10.0.2.2/M13/img/"
        val usuaris = mutableListOf<Usuari> (
            Usuari("Maria", "Gimena", 35, ""),
            Usuari("Anton", "Guason", 40, ""),
            Usuari("Albert", "Despierto", 80, ""),
            Usuari("Calimero", "Entero", 25, ""),
            Usuari("Hernan", "Gran", 29, "")


        )
    }
}