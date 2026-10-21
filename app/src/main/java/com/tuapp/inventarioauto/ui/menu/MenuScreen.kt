package com.tuapp.inventarioauto.ui.menu

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun MenuScreen(navController: NavController) {
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
            onClick = { /* TODO: Implementar en el examen transversal */ },
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text("Proveedores y Reposición")
        }
    }
}
