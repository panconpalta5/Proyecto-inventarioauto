package com.tuapp.inventarioauto.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ProveedorDao {
    @Query("SELECT * FROM proveedores ORDER BY nombreComercial ASC")
    fun obtenerProveedores(): Flow<List<ProveedorEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarProveedor(proveedor: ProveedorEntity)
}