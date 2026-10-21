package com.tuapp.inventarioauto.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MovimientoDao {
    @Insert
    suspend fun insertarMovimiento(movimiento: MovimientoEntity)

    @Query("SELECT * FROM movimientos WHERE productoId = :productoId ORDER BY id DESC")
    fun obtenerMovimientosPorProducto(productoId: Int): Flow<List<MovimientoEntity>>
}