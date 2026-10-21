package com.tuapp.inventarioauto.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tuapp.inventarioauto.repository.ProductoRepository
import com.tuapp.inventarioauto.ui.login.LoginScreen
import com.tuapp.inventarioauto.ui.menu.MenuScreen
import com.tuapp.inventarioauto.ui.producto.ListaProductosScreen
import com.tuapp.inventarioauto.ui.producto.ProductoDetalleScreen
import com.tuapp.inventarioauto.ui.producto.ProductoFormScreen
import com.tuapp.inventarioauto.ui.producto.ScannerScreen
import com.tuapp.inventarioauto.ui.producto.ProductoViewModel

@Composable
fun AppNavigation(
    repository: ProductoRepository,
    viewModel: ProductoViewModel
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {

        composable("login") {
            LoginScreen(navController = navController)
        }

        composable("menu_principal") {
            MenuScreen(navController = navController)
        }

        composable("lista_productos") {
            ListaProductosScreen(
                productosStream = repository.productosStream,
                onNavigateToForm = {
                    viewModel.prepararNuevoProducto()
                    navController.navigate("formulario_producto")
                },
                onNavigateToScanner = {
                    navController.navigate("scanner")
                },
                onProductoSelected = { producto ->
                    viewModel.seleccionarProducto(producto)
                    navController.navigate("detalle_producto")
                }
            )
        }

        composable("detalle_producto") {
            ProductoDetalleScreen(
                viewModel = viewModel,
                onNavigateToEdit = {
                    navController.navigate("formulario_producto")
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable("formulario_producto") {
            ProductoFormScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("scanner") {
            ScannerScreen(navController = navController, viewModel = viewModel)
        }
    }
}