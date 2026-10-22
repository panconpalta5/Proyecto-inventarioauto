package com.tuapp.inventarioauto.ui.menu

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tuapp.inventarioauto.ui.producto.ProductoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProveedoresScreen(viewModel: ProductoViewModel, onNavigateToForm: () -> Unit, onNavigateBack: () -> Unit) {
    val proveedores by viewModel.proveedoresStream.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Proveedores") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToForm) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Proveedor")
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            if (proveedores.isEmpty()) {
                Text("No hay proveedores registrados.", modifier = Modifier.padding(16.dp))
            } else {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(proveedores) { prov ->
                        Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(text = prov.nombreComercial, style = MaterialTheme.typography.titleMedium)
                                Text(text = "Código: ${prov.codigo}", style = MaterialTheme.typography.bodyMedium)
                                Text(text = "Categorías: ${prov.categoriasSuministradas}", style = MaterialTheme.typography.bodySmall)
                                Text(text = "Contacto: ${prov.correo} - ${prov.telefono}", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }

            OutlinedButton(
                onClick = onNavigateBack,
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                Text("Volver")
            }
        }
    }
}