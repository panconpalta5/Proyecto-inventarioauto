package com.tuapp.inventarioauto.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductoDao {

    @Query("SELECT * FROM productos ORDER BY nombre ASC")
    fun obtenerProductos(): Flow<List<ProductoEntity>>

    @Query("SELECT * FROM productos WHERE id = :id")
    suspend fun obtenerProductoPorId(id: Int): ProductoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarProducto(producto: ProductoEntity)

    @Update
    suspend fun actualizarProducto(producto: ProductoEntity)
}