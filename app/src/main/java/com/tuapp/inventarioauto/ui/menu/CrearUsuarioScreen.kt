package com.tuapp.inventarioauto.ui.menu

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.tuapp.inventarioauto.ui.producto.ProductoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearUsuarioScreen(viewModel: ProductoViewModel, onNavigateBack: () -> Unit) {
    var nuevoUsuario by remember { mutableStateOf("") }
    var nuevaContrasena by remember { mutableStateOf("") }

    Scaffold(topBar = { TopAppBar(title = { Text("Registrar Operador") }) }) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).padding(16.dp).fillMaxSize()) {
            OutlinedTextField(
                value = nuevoUsuario,
                onValueChange = { nuevoUsuario = it },
                label = { Text("Nombre de usuario") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = nuevaContrasena,
                onValueChange = { nuevaContrasena = it },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = {
                if (nuevoUsuario.isNotBlank() && nuevaContrasena.isNotBlank()) {
                    viewModel.registrarOperador(nuevoUsuario, nuevaContrasena)
                    onNavigateBack()
                }
            }, modifier = Modifier.fillMaxWidth()) {
                Text("Crear Usuario Operador")
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(onClick = onNavigateBack, modifier = Modifier.fillMaxWidth()) {
                Text("Cancelar")
            }
        }
    }
}