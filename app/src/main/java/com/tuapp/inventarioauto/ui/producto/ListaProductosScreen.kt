package com.tuapp.inventarioauto.ui.producto

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.print.PrintHelper
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import com.tuapp.inventarioauto.data.local.ProductoEntity
import kotlinx.coroutines.flow.Flow
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaProductosScreen(
    productosStream: Flow<List<ProductoEntity>>,
    onNavigateToForm: () -> Unit,
    onNavigateToScanner: () -> Unit,
    onProductoSelected: (ProductoEntity) -> Unit
) {
    val productos by productosStream.collectAsState(initial = emptyList())
    var qrCodeVisible by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Inventario del Taller") },
                actions = {
                    IconButton(onClick = onNavigateToScanner) {
                        Icon(Icons.Default.QrCodeScanner, contentDescription = "Escanear QR")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToForm) {
                Icon(Icons.Default.Add, contentDescription = "Agregar")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier.fillMaxSize()
        ) {
            items(productos) { producto ->
                ProductoItem(
                    producto = producto,
                    onClick = { onProductoSelected(producto) },
                    onGenerateQr = { qrCodeVisible = producto.codigo }
                )
            }
        }

        qrCodeVisible?.let { codigo ->
            Dialog(onDismissRequest = { qrCodeVisible = null }) {
                val context = LocalContext.current

                Card(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp).fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Código QR del Producto", style = MaterialTheme.typography.titleLarge)
                        Spacer(modifier = Modifier.height(16.dp))

                        val qrBitmap = generarQrBitmap(codigo)
                        if (qrBitmap != null) {
                            Image(
                                bitmap = qrBitmap.asImageBitmap(),
                                contentDescription = "QR de $codigo",
                                modifier = Modifier.size(200.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        Text(codigo, style = MaterialTheme.typography.bodyLarge)
                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = {
                                if (qrBitmap != null) {
                                    val printHelper = PrintHelper(context)
                                    printHelper.scaleMode = PrintHelper.SCALE_MODE_FIT
                                    printHelper.printBitmap("QR_Producto_$codigo", qrBitmap)
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Imprimir / Exportar QR")
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedButton(
                            onClick = { qrCodeVisible = null },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Cerrar")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductoItem(producto: ProductoEntity, onClick: () -> Unit, onGenerateQr: () -> Unit) {
    // Variable para cargar la foto miniatura
    var imagenBitmap by remember(producto.imagenUri) { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(producto.imagenUri) {
        if (producto.imagenUri.isNotBlank()) {
            try {
                val file = File(producto.imagenUri)
                if (file.exists()) {
                    imagenBitmap = BitmapFactory.decodeFile(file.absolutePath)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // FOTO (Si tiene foto la muestra, si no, muestra el icono gris)
            if (imagenBitmap != null) {
                Image(
                    bitmap = imagenBitmap!!.asImageBitmap(),
                    contentDescription = "Foto de ${producto.nombre}",
                    modifier = Modifier.size(80.dp)
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Image, contentDescription = "Foto ficticia", tint = Color.DarkGray)
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Nombre: ${producto.nombre}", style = MaterialTheme.typography.titleMedium)
                Text(text = "Tipo: ${producto.categoria}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Stock: ${producto.cantidad}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Código: ${producto.codigo}", style = MaterialTheme.typography.bodySmall)
            }

            IconButton(onClick = onGenerateQr) {
                Icon(Icons.Default.QrCode, contentDescription = "Generar QR")
            }
        }
    }
}

fun generarQrBitmap(contenido: String): Bitmap? {
    return try {
        val size = 512
        val bitMatrix = QRCodeWriter().encode(contenido, BarcodeFormat.QR_CODE, size, size)
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565)
        for (x in 0 until size) {
            for (y in 0 until size) {
                bitmap.setPixel(x, y, if (bitMatrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
            }
        }
        bitmap
    } catch (e: Exception) {
        null
    }
}