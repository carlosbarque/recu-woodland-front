package com.example.myapplication.estructuresDades

class TereaProvider {
    companion object{
        val terea = mutableListOf<Task>(
            Task(1, "Deberes", "Esto es una terea de prueba", 25),
            Task(2, "Recogar Habitacion", "Hay que recoger la habitacion", 30),
            Task(3,"Bajar a comprar pan", "Hay que bajar a comprar antes a las 22", 60)
        )
    }
}