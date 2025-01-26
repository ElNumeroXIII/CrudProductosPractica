package com.example.crudproductospractica.models

import java.io.Serializable

data class Articulo(
    val id: String = "",
    val nombre: String = "",
    val descripcion: String = "",
    val precio: Double = 0.0
) : Serializable
