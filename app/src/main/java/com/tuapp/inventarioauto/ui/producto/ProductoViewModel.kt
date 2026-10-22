package com.tuapp.inventarioauto.ui.producto

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tuapp.inventarioauto.data.local.MovimientoEntity
import com.tuapp.inventarioauto.data.local.ProductoEntity
import com.tuapp.inventarioauto.data.local.ProveedorEntity
import com.tuapp.inventarioauto.data.local.UsuarioEntity
import com.tuapp.inventarioauto.repository.ProductoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ProductoViewModel(private val repository: ProductoRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(ProductoUiState())
    val uiState: StateFlow<ProductoUiState> = _uiState.asStateFlow()

    private val _productoSeleccionado = MutableStateFlow<ProductoEntity?>(null)
    val productoSeleccionado: StateFlow<ProductoEntity?> = _productoSeleccionado.asStateFlow()

    private val _movimientos = MutableStateFlow<List<MovimientoEntity>>(emptyList())
    val movimientos: StateFlow<List<MovimientoEntity>> = _movimientos.asStateFlow()

    private val _usuarioActual = MutableStateFlow<UsuarioEntity?>(null)
    val usuarioActual: StateFlow<UsuarioEntity?> = _usuarioActual.asStateFlow()

    val proveedoresStream = repository.proveedoresStream

    fun actualizarEstado(nuevoEstado: ProductoUiState) {
        _uiState.value = nuevoEstado
    }

    fun resetEstadoGuardado() {
        _uiState.value = _uiState.value.copy(guardadoExitosamente = false)
    }

    fun login(usuario: String, contrasena: String, onSuccess: () -> Unit, onError: () -> Unit) {
        viewModelScope.launch {
            val user = repository.login(usuario, contrasena)
            if (user != null) {
                _usuarioActual.value = user
                onSuccess()
            } else {
                onError()
            }
        }
    }

    fun registrarOperador(usuario: String, contrasena: String) {
        viewModelScope.launch {
            repository.registrarUsuario(UsuarioEntity(usuario = usuario, contrasena = contrasena, rol = "operador"))
        }
    }

    fun guardarProveedor(codigo: String, nombre: String, correo: String, telefono: String, categorias: String) {
        viewModelScope.launch {
            repository.insertarProveedor(ProveedorEntity(codigo = codigo, nombreComercial = nombre, correo = correo, telefono = telefono, categoriasSuministradas = categorias))
        }
    }

    fun guardarProducto() {
        val estado = _uiState.value
        val cantidadInt = estado.cantidad.toIntOrNull()
        val stockMinimoInt = estado.stockMinimo.toIntOrNull()

        val codigoErr = if (estado.codigo.isBlank()) "Campo obligatorio" else null
        val nombreErr = if (estado.nombre.isBlank()) "Campo obligatorio" else null
        val categoriaErr = if (estado.categoria.isBlank()) "Campo obligatorio" else null
        val cantidadErr = if (cantidadInt == null || cantidadInt < 0) "Debe ser un número >= 0" else null
        val stockErr = if (stockMinimoInt == null || stockMinimoInt < 0) "Debe ser un número >= 0" else null

        if (codigoErr != null || nombreErr != null || categoriaErr != null || cantidadErr != null || stockErr != null) {
            _uiState.value = estado.copy(
                codigoError = codigoErr,
                nombreError = nombreErr,
                categoriaError = categoriaErr,
                cantidadError = cantidadErr,
                stockMinimoError = stockErr
            )
            return
        }

        _uiState.value = estado.copy(guardando = true)

        viewModelScope.launch {
            val producto = ProductoEntity(
                id = estado.id,
                codigo = estado.codigo,
                nombre = estado.nombre,
                categoria = estado.categoria,
                cantidad = cantidadInt ?: 0,
                stockMinimo = stockMinimoInt ?: 0,
                imagenUri = estado.imagenUri,
                descripcionBreve = estado.descripcionBreve,
                marca = estado.marca,
                unidadMedida = estado.unidadMedida,
                precioCompra = estado.precioCompra.toDoubleOrNull() ?: 0.0,
                precioVenta = estado.precioVenta.toDoubleOrNull() ?: 0.0,
                proveedorPrincipal = estado.proveedorPrincipal,
                ubicacion = estado.ubicacion
            )

            if (estado.id == 0) {
                repository.insertProducto(producto)
            } else {
                repository.updateProducto(producto)
            }

            _uiState.value = ProductoUiState(guardadoExitosamente = true)
        }
    }

    fun seleccionarProducto(producto: ProductoEntity) {
        _productoSeleccionado.value = producto
        _uiState.value = ProductoUiState(
            id = producto.id,
            codigo = producto.codigo,
            nombre = producto.nombre,
            categoria = producto.categoria,
            cantidad = producto.cantidad.toString(),
            stockMinimo = producto.stockMinimo.toString(),
            imagenUri = producto.imagenUri,
            descripcionBreve = producto.descripcionBreve,
            marca = producto.marca,
            unidadMedida = producto.unidadMedida,
            precioCompra = producto.precioCompra.toString(),
            precioVenta = producto.precioVenta.toString(),
            proveedorPrincipal = producto.proveedorPrincipal,
            ubicacion = producto.ubicacion
        )

        viewModelScope.launch {
            repository.getMovimientosPorProducto(producto.id).collect { lista ->
                _movimientos.value = lista
            }
        }
    }

    fun prepararNuevoProducto() {
        _productoSeleccionado.value = null
        _movimientos.value = emptyList()
        _uiState.value = ProductoUiState()
    }

    fun eliminarProductoSeleccionado(onSuccess: () -> Unit) {
        val producto = _productoSeleccionado.value
        if (producto != null && _usuarioActual.value?.rol == "admin") {
            viewModelScope.launch {
                repository.eliminarProducto(producto)
                onSuccess()
            }
        }
    }

    fun registrarMovimiento(producto: ProductoEntity, tipo: String, cantidadMov: Int, motivo: String) {
        viewModelScope.launch {
            val cantidadLimpia = kotlin.math.abs(cantidadMov)
            val esIngreso = tipo.trim().equals("Ingreso", ignoreCase = true)

            val nuevaCantidad = if (esIngreso) {
                producto.cantidad + cantidadLimpia
            } else {
                producto.cantidad - cantidadLimpia
            }

            val productoActualizado = producto.copy(cantidad = nuevaCantidad)
            repository.updateProducto(productoActualizado)

            val fechaActual = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
            val movimiento = MovimientoEntity(
                productoId = producto.id,
                tipo = tipo.trim(),
                cantidad = cantidadLimpia,
                fecha = fechaActual,
                motivo = motivo
            )
            repository.insertMovimiento(movimiento)

            _productoSeleccionado.value = productoActualizado
        }
    }
}