package com.tuapp.inventarioauto.ui.producto

data class ProductoUiState(
    val id: Int = 0,
    val codigo: String = "",
    val nombre: String = "",
    val categoria: String = "",
    val cantidad: String = "",
    val stockMinimo: String = "",
    val imagenUri: String = "",
    val descripcionBreve: String = "",
    val marca: String = "",
    val unidadMedida: String = "",
    val precioCompra: String = "",
    val precioVenta: String = "",
    val proveedorPrincipal: String = "",
    val ubicacion: String = "",

    val codigoError: String? = null,
    val nombreError: String? = null,
    val categoriaError: String? = null,
    val cantidadError: String? = null,
    val stockMinimoError: String? = null,

    val guardando: Boolean = false,
    val guardadoExitosamente: Boolean = false
)