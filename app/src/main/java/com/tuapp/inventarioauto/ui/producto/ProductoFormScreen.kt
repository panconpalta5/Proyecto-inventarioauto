package com.tuapp.inventarioauto.ui.producto

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun ProductoFormScreen(
    viewModel: ProductoViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()


    if (uiState.guardadoExitosamente) {
        viewModel.resetEstadoGuardado()
        onNavigateBack()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = "Registrar Producto", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = uiState.codigo,
            onValueChange = { viewModel.actualizarEstado(uiState.copy(codigo = it)) },
            label = { Text("Código") },
            isError = uiState.codigoError != null,
            supportingText = { uiState.codigoError?.let { Text(it) } },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = uiState.nombre,
            onValueChange = { viewModel.actualizarEstado(uiState.copy(nombre = it)) },
            label = { Text("Nombre") },
            isError = uiState.nombreError != null,
            supportingText = { uiState.nombreError?.let { Text(it) } },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = uiState.categoria,
            onValueChange = { viewModel.actualizarEstado(uiState.copy(categoria = it)) },
            label = { Text("Categoría") },
            isError = uiState.categoriaError != null,
            supportingText = { uiState.categoriaError?.let { Text(it) } },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = uiState.cantidad,
            onValueChange = { viewModel.actualizarEstado(uiState.copy(cantidad = it)) },
            label = { Text("Cantidad") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = uiState.cantidadError != null,
            supportingText = { uiState.cantidadError?.let { Text(it) } },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = uiState.stockMinimo,
            onValueChange = { viewModel.actualizarEstado(uiState.copy(stockMinimo = it)) },
            label = { Text("Stock Mínimo") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = uiState.stockMinimoError != null,
            supportingText = { uiState.stockMinimoError?.let { Text(it) } },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = { viewModel.guardarProducto() },
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.guardando
        ) {
            Text(if (uiState.guardando) "Guardando..." else "Guardar Producto")
        }
    }
}