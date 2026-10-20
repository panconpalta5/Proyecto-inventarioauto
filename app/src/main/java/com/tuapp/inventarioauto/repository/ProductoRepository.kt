
package com.tuapp.inventarioauto.repository

import com.tuapp.inventarioauto.data.local.ProductoDao
import com.tuapp.inventarioauto.data.local.ProductoEntity
import kotlinx.coroutines.flow.Flow

class ProductoRepository(private val productoDao: ProductoDao) {

    // Flujo reactivo para la lista de productos
    val productosStream: Flow<List<ProductoEntity>> = productoDao.obtenerProductos()

    suspend fun getProducto(id: Int): ProductoEntity? {
        return productoDao.obtenerProductoPorId(id)
    }

    suspend fun insertProducto(producto: ProductoEntity) {
        productoDao.insertarProducto(producto)
    }

    suspend fun updateProducto(producto: ProductoEntity) {
        productoDao.actualizarProducto(producto)
    }
}