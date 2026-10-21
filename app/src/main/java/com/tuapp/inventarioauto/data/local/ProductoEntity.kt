package com.tuapp.inventarioauto.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "productos")
data class ProductoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val codigo: String,
    val nombre: String,
    val categoria: String,
    val cantidad: Int,
    val stockMinimo: Int,
    val descripcionBreve: String = "",
    val marca: String = "",
    val unidadMedida: String = "",
    val cantidadSugerida: Int = 0,
    val precioCompra: Double = 0.0,
    val precioVenta: Double = 0.0,
    val estado: String = "Activo",
    val fechaUltimoIngreso: String = "",
    val fechaUltimaSalida: String = "",
    val proveedorPrincipal: String = "",
    val ubicacion: String = "",
    val imagenUri: String = ""
)