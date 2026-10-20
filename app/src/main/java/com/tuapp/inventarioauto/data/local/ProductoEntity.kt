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
    val stockMinimo: Int
)