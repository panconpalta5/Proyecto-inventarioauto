    package com.tuapp.inventarioauto.ui.navigation

    import androidx.compose.runtime.Composable
    import androidx.navigation.compose.NavHost
    import androidx.navigation.compose.composable
    import androidx.navigation.compose.rememberNavController
    import com.tuapp.inventarioauto.repository.ProductoRepository
    import com.tuapp.inventarioauto.ui.producto.ListaProductosScreen
    import com.tuapp.inventarioauto.ui.producto.ProductoFormScreen
    import com.tuapp.inventarioauto.ui.producto.ProductoViewModel

    @Composable
    fun AppNavigation(
        repository: ProductoRepository,
        viewModel: ProductoViewModel
    ) {
        val navController = rememberNavController()

        NavHost(navController = navController, startDestination = "lista_productos") {


            composable("lista_productos") {
                ListaProductosScreen(
                    productosStream = repository.productosStream,
                    onNavigateToForm = { navController.navigate("formulario_producto") },
                    onNavigateToScanner = {

                    }
                )
            }


            composable("formulario_producto") {
                ProductoFormScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }