package com.tuapp.inventarioauto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.room.Room
import com.tuapp.inventarioauto.data.local.AppDatabase
import com.tuapp.inventarioauto.repository.ProductoRepository
import com.tuapp.inventarioauto.ui.navigation.AppNavigation
import com.tuapp.inventarioauto.ui.producto.ProductoViewModel
import com.tuapp.inventarioauto.ui.theme.InventarioautoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "inventario_database"
        )
            .fallbackToDestructiveMigration()
            .build()


        val repository = ProductoRepository(db.productoDao(), db.movimientoDao())
        val viewModel = ProductoViewModel(repository)

        setContent {
            InventarioautoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(repository = repository, viewModel = viewModel)
                }
            }
        }
    }
}