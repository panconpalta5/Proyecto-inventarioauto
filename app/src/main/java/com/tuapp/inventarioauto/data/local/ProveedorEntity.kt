package com.tuapp.inventarioauto.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "proveedores")
data class ProveedorEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val codigo: String,
    val nombreComercial: String,
    val correo: String,
    val telefono: String,
    val categoriasSuministradas: String
)