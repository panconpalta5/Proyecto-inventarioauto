package com.tuapp.inventarioauto.ui.menu

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tuapp.inventarioauto.ui.producto.ProductoViewModel

@Composable
fun MenuScreen(navController: NavController, viewModel: ProductoViewModel) {
    val usuarioActivo by viewModel.usuarioActual.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Menú Principal", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { navController.navigate("lista_productos") },
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text("Gestionar Inventario")
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = { navController.navigate("lista_proveedores") },
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text("Proveedores y Reposición")
        }

        if (usuarioActivo?.rol == "admin") {
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(
                onClick = { navController.navigate("crear_usuario") },
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text("Crear Operador")
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        TextButton(
            onClick = {
                navController.navigate("login") {
                    popUpTo("menu_principal") { inclusive = true }
                }
            }
        ) {
            Text("Cerrar Sesión", color = MaterialTheme.colorScheme.error)
        }
    }
}