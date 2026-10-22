package com.tuapp.inventarioauto.ui.producto

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductoDetalleScreen(
    viewModel: ProductoViewModel,
    onNavigateToEdit: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val producto by viewModel.productoSeleccionado.collectAsState()
    val movimientos by viewModel.movimientos.collectAsState()
    val usuarioActivo by viewModel.usuarioActual.collectAsState()

    var mostrarDialogoMovimiento by remember { mutableStateOf(false) }
    var tipoMovimiento by remember { mutableStateOf("Ingreso") }
    var cantidadMovimiento by remember { mutableStateOf("") }
    var motivoMovimiento by remember { mutableStateOf("") }
    var imagenBitmap by remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(producto?.imagenUri) {
        val uri = producto?.imagenUri
        if (!uri.isNullOrBlank()) {
            try {
                val file = File(uri)
                if (file.exists()) {
                    imagenBitmap = BitmapFactory.decodeFile(file.absolutePath)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            imagenBitmap = null
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de Producto") },
                actions = {
                    IconButton(onClick = onNavigateToEdit) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar Producto")
                    }
                }
            )
        }
    ) { paddingValues ->
        producto?.let { prod ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                Text("Nombre: ${prod.nombre}", style = MaterialTheme.typography.headlineSmall)
                Text("Código: ${prod.codigo}", style = MaterialTheme.typography.bodyLarge)
                Text("Categoría: ${prod.categoria}", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(16.dp))

                imagenBitmap?.let { bmp ->
                    Image(
                        bitmap = bmp.asImageBitmap(),
                        contentDescription = "Foto del producto",
                        modifier = Modifier
                            .size(150.dp)
                            .padding(bottom = 16.dp)
                    )
                }

                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Inventario", style = MaterialTheme.typography.titleMedium)
                        Text("Cantidad Actual: ${prod.cantidad}")
                        Text("Stock Mínimo: ${prod.stockMinimo}")
                        Text("Ubicación: ${if (prod.ubicacion.isNotBlank()) prod.ubicacion else "No asignada"}")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { mostrarDialogoMovimiento = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Registrar Movimiento (Ingreso/Salida)")
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text("Historial de Movimientos", style = MaterialTheme.typography.titleMedium)
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                if (movimientos.isEmpty()) {
                    Text("No hay movimientos recientes registrados.", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.weight(1f))
                } else {
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(movimientos) { mov ->
                            Card(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(text = mov.tipo, style = MaterialTheme.typography.titleMedium)
                                        Text(text = mov.fecha, style = MaterialTheme.typography.bodySmall)
                                    }
                                    Text(text = "Cantidad: ${mov.cantidad}", style = MaterialTheme.typography.bodyMedium)
                                    if (mov.motivo.isNotBlank()) {
                                        Text(text = "Motivo: ${mov.motivo}", style = MaterialTheme.typography.bodySmall)
                                    }
                                }
                            }
                        }
                    }
                }

                if (usuarioActivo?.rol == "admin") {
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            viewModel.eliminarProductoSeleccionado {
                                onNavigateBack()
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("Eliminar Producto")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(onClick = onNavigateBack, modifier = Modifier.fillMaxWidth()) {
                    Text("Volver")
                }
            }

            if (mostrarDialogoMovimiento) {
                AlertDialog(
                    onDismissRequest = { mostrarDialogoMovimiento = false },
                    title = { Text("Registrar Movimiento") },
                    text = {
                        Column {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                FilterChip(
                                    selected = tipoMovimiento == "Ingreso",
                                    onClick = { tipoMovimiento = "Ingreso" },
                                    label = { Text("Ingreso") }
                                )
                                FilterChip(
                                    selected = tipoMovimiento == "Salida",
                                    onClick = { tipoMovimiento = "Salida" },
                                    label = { Text("Salida") }
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = cantidadMovimiento,
                                onValueChange = { cantidadMovimiento = it },
                                label = { Text("Cantidad") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = motivoMovimiento,
                                onValueChange = { motivoMovimiento = it },
                                label = { Text("Motivo (Opcional)") },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    },
                    confirmButton = {
                        Button(onClick = {
                            val cantidad = cantidadMovimiento.toIntOrNull() ?: 0
                            if (cantidad > 0) {
                                viewModel.registrarMovimiento(prod, tipoMovimiento, cantidad, motivoMovimiento)
                                mostrarDialogoMovimiento = false
                                cantidadMovimiento = ""
                                motivoMovimiento = ""
                            }
                        }) {
                            Text("Guardar")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { mostrarDialogoMovimiento = false }) {
                            Text("Cancelar")
                        }
                    }
                )
            }
        }
    }
}