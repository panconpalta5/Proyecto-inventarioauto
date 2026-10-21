package com.tuapp.inventarioauto.ui.producto

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tuapp.inventarioauto.data.local.ProductoEntity
import com.tuapp.inventarioauto.repository.ProductoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductoViewModel(private val repository: ProductoRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductoUiState())
    val uiState: StateFlow<ProductoUiState> = _uiState.asStateFlow()

    fun actualizarEstado(nuevoEstado: ProductoUiState) {
        _uiState.update { nuevoEstado }
    }

    fun guardarProducto() {
        val estadoActual = _uiState.value


        val codigoValido = estadoActual.codigo.isNotBlank()
        val nombreValido = estadoActual.nombre.isNotBlank()
        val categoriaValida = estadoActual.categoria.isNotBlank()

        val cantidadNum = estadoActual.cantidad.toIntOrNull()
        val cantidadValida = cantidadNum != null && cantidadNum >= 0

        val stockMinimoNum = estadoActual.stockMinimo.toIntOrNull()
        val stockMinimoValido = stockMinimoNum != null && stockMinimoNum >= 0


        _uiState.update {
            it.copy(
                codigoError = if (codigoValido) null else "Campo obligatorio",
                nombreError = if (nombreValido) null else "Campo obligatorio",
                categoriaError = if (categoriaValida) null else "Campo obligatorio",
                cantidadError = if (cantidadValida) null else "Debe ser número >= 0",
                stockMinimoError = if (stockMinimoValido) null else "Debe ser número >= 0"
            )
        }


        if (codigoValido && nombreValido && categoriaValida && cantidadValida && stockMinimoValido) {
            _uiState.update { it.copy(guardando = true) }

            viewModelScope.launch {
                val nuevoProducto = ProductoEntity(
                    codigo = estadoActual.codigo,
                    nombre = estadoActual.nombre,
                    categoria = estadoActual.categoria,
                    cantidad = cantidadNum!!,
                    stockMinimo = stockMinimoNum!!
                )
                repository.insertProducto(nuevoProducto)
                _uiState.update { it.copy(guardando = false, guardadoExitosamente = true) }
            }
        }
    }

    fun resetEstadoGuardado() {
        _uiState.update { ProductoUiState() }
    }
}