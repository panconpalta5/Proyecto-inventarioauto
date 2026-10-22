package com.tuapp.inventarioauto.ui.menu

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tuapp.inventarioauto.ui.producto.ProductoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProveedorFormScreen(viewModel: ProductoViewModel, onNavigateBack: () -> Unit) {
    var codigo by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var categorias by remember { mutableStateOf("") }

    Scaffold(topBar = { TopAppBar(title = { Text("Nuevo Proveedor") }) }) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).padding(16.dp).fillMaxSize()) {
            OutlinedTextField(value = codigo, onValueChange = { codigo = it }, label = { Text("Código") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre Comercial") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = correo, onValueChange = { correo = it }, label = { Text("Correo") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = telefono, onValueChange = { telefono = it }, label = { Text("Teléfono") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = categorias, onValueChange = { categorias = it }, label = { Text("Categorías Suministradas") }, modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = {
                if (codigo.isNotBlank() && nombre.isNotBlank()) {
                    viewModel.guardarProveedor(codigo, nombre, correo, telefono, categorias)
                    onNavigateBack()
                }
            }, modifier = Modifier.fillMaxWidth()) {
                Text("Guardar Proveedor")
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(onClick = onNavigateBack, modifier = Modifier.fillMaxWidth()) {
                Text("Cancelar")
            }
        }
    }
}