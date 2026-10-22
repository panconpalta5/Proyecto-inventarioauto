package com.tuapp.inventarioauto.repository

import com.tuapp.inventarioauto.data.local.MovimientoDao
import com.tuapp.inventarioauto.data.local.MovimientoEntity
import com.tuapp.inventarioauto.data.local.ProductoDao
import com.tuapp.inventarioauto.data.local.ProductoEntity
import com.tuapp.inventarioauto.data.local.ProveedorDao
import com.tuapp.inventarioauto.data.local.ProveedorEntity
import com.tuapp.inventarioauto.data.local.UsuarioDao
import com.tuapp.inventarioauto.data.local.UsuarioEntity
import kotlinx.coroutines.flow.Flow

class ProductoRepository(
    private val productoDao: ProductoDao,
    private val movimientoDao: MovimientoDao,
    private val proveedorDao: ProveedorDao,
    private val usuarioDao: UsuarioDao
) {
    val productosStream: Flow<List<ProductoEntity>> = productoDao.obtenerProductos()
    val proveedoresStream: Flow<List<ProveedorEntity>> = proveedorDao.obtenerProveedores()

    suspend fun getProducto(id: Int): ProductoEntity? {
        return productoDao.obtenerProductoPorId(id)
    }

    suspend fun insertProducto(producto: ProductoEntity) {
        productoDao.insertarProducto(producto)
    }

    suspend fun updateProducto(producto: ProductoEntity) {
        productoDao.actualizarProducto(producto)
    }

    suspend fun eliminarProducto(producto: ProductoEntity) {
        productoDao.eliminarProducto(producto)
    }

    suspend fun insertMovimiento(movimiento: MovimientoEntity) {
        movimientoDao.insertarMovimiento(movimiento)
    }

    fun getMovimientosPorProducto(productoId: Int): Flow<List<MovimientoEntity>> {
        return movimientoDao.obtenerMovimientosPorProducto(productoId)
    }

    suspend fun insertarProveedor(proveedor: ProveedorEntity) {
        proveedorDao.insertarProveedor(proveedor)
    }

    suspend fun login(usuario: String, contrasena: String): UsuarioEntity? {
        return usuarioDao.login(usuario, contrasena)
    }

    suspend fun registrarUsuario(usuario: UsuarioEntity) {
        usuarioDao.insertarUsuario(usuario)
    }
}