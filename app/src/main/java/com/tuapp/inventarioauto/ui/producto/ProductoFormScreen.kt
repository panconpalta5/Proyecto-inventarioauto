package com.tuapp.inventarioauto.ui.producto

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductoFormScreen(
    viewModel: ProductoViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    val categorias = listOf("Motor", "Frenos", "Transmisión", "Eléctrico", "Líquidos", "Accesorios", "Repuestos")
    var expandido by remember { mutableStateOf(false) }

    var imagenBitmap by remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(uiState.imagenUri) {
        if (uiState.imagenUri.isNotBlank() && imagenBitmap == null) {
            try {
                val file = File(uiState.imagenUri)
                if (file.exists()) {
                    imagenBitmap = BitmapFactory.decodeFile(file.absolutePath)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            imagenBitmap = bitmap
            try {
                val filename = "producto_${System.currentTimeMillis()}.png"
                val file = File(context.filesDir, filename)
                file.outputStream().use { out ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                }
                viewModel.actualizarEstado(uiState.copy(imagenUri = file.absolutePath))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
        if (isGranted) {
            try { cameraLauncher.launch(null) } catch (e: Exception) { e.printStackTrace() }
        }
    }

    LaunchedEffect(uiState.guardadoExitosamente) {
        if (uiState.guardadoExitosamente) {
            viewModel.resetEstadoGuardado()
            onNavigateBack()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("Registrar Producto", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = uiState.codigo,
            onValueChange = { viewModel.actualizarEstado(uiState.copy(codigo = it, codigoError = null)) },
            label = { Text("Código del producto") },
            isError = uiState.codigoError != null,
            supportingText = {
                AnimatedVisibility(visible = uiState.codigoError != null) {
                    Text(uiState.codigoError ?: "")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = uiState.nombre,
            onValueChange = { viewModel.actualizarEstado(uiState.copy(nombre = it, nombreError = null)) },
            label = { Text("Nombre del producto") },
            isError = uiState.nombreError != null,
            supportingText = {
                AnimatedVisibility(visible = uiState.nombreError != null) {
                    Text(uiState.nombreError ?: "")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        ExposedDropdownMenuBox(
            expanded = expandido,
            onExpandedChange = { expandido = !expandido },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = uiState.categoria,
                onValueChange = {},
                readOnly = true,
                label = { Text("Categoría") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandido) },
                isError = uiState.categoriaError != null,
                supportingText = {
                    AnimatedVisibility(visible = uiState.categoriaError != null) {
                        Text(uiState.categoriaError ?: "")
                    }
                },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expandido,
                onDismissRequest = { expandido = false }
            ) {
                categorias.forEach { seleccion ->
                    DropdownMenuItem(
                        text = { Text(seleccion) },
                        onClick = {
                            viewModel.actualizarEstado(uiState.copy(categoria = seleccion, categoriaError = null))
                            expandido = false
                        }
                    )
                }
            }
        }

        OutlinedTextField(
            value = uiState.cantidad,
            onValueChange = { viewModel.actualizarEstado(uiState.copy(cantidad = it, cantidadError = null)) },
            label = { Text("Cantidad actual") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = uiState.cantidadError != null,
            supportingText = {
                AnimatedVisibility(visible = uiState.cantidadError != null) {
                    Text(uiState.cantidadError ?: "")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = uiState.stockMinimo,
            onValueChange = { viewModel.actualizarEstado(uiState.copy(stockMinimo = it, stockMinimoError = null)) },
            label = { Text("Stock mínimo") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = uiState.stockMinimoError != null,
            supportingText = {
                AnimatedVisibility(visible = uiState.stockMinimoError != null) {
                    Text(uiState.stockMinimoError ?: "")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = uiState.ubicacion,
            onValueChange = { viewModel.actualizarEstado(uiState.copy(ubicacion = it)) },
            label = { Text("Ubicación en bodega (Ej. Estante 2)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        imagenBitmap?.let { bmp ->
            Image(
                bitmap = bmp.asImageBitmap(),
                contentDescription = "Foto capturada",
                modifier = Modifier
                    .size(120.dp)
                    .padding(bottom = 8.dp)
            )
        }

        OutlinedButton(
            onClick = {
                if (hasCameraPermission) {
                    try { cameraLauncher.launch(null) } catch (e: Exception) { e.printStackTrace() }
                } else {
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Capturar Fotografía")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { viewModel.guardarProducto() },
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.guardando
        ) {
            Text(if (uiState.guardando) "Guardando..." else "Guardar Producto")
        }
    }
}