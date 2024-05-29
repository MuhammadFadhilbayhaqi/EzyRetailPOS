package org.d3if3102.ezyretail.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.firebase.auth.FirebaseAuth
import org.d3if3102.ezyretail.ui.screen.MainMenu
import org.d3if3102.ezyretail.ui.screen.authentication.LoginScreen
import org.d3if3102.ezyretail.ui.screen.authentication.MainViewModel
import org.d3if3102.ezyretail.ui.screen.authentication.RegisterScreen
import org.d3if3102.ezyretail.ui.screen.laporan.LaporanPenjualanScreen
import org.d3if3102.ezyretail.ui.screen.produck.AddProduckScreen
import org.d3if3102.ezyretail.ui.screen.produck.EditProdukScreen
import org.d3if3102.ezyretail.ui.screen.produck.ProdukScreen
//import org.d3if3102.ezyretail.ui.screen.produck.ProduckScreen
import org.d3if3102.ezyretail.ui.screen.stok.StokScreen
import org.d3if3102.ezyretail.ui.screen.stok.UpdateStokScreen
//import org.d3if3102.ezyretail.ui.screen.produck.ProdukScreen
//import org.d3if3102.ezyretail.ui.screen.stok.AddStokScreen
//import org.d3if3102.ezyretail.ui.screen.stok.StokScreen
import org.d3if3102.ezyretail.ui.screen.toko.AddTokoScreen

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    viewModel: MainViewModel,
    auth: FirebaseAuth
) {
    NavHost(
        navController = navController,
        startDestination = if (auth.currentUser != null) Screen.MainMenu.route else Screen.Login.route
    ){
        composable(route = Screen.Login.route) {
            LoginScreen(navController, viewModel = MainViewModel())
        }
        composable(route = Screen.Register.route) {
            RegisterScreen(navController, viewModel = MainViewModel())
        }
        composable(route = Screen.MainMenu.route) {
            MainMenu(navController, viewModel = MainViewModel())
        }
        composable(route = Screen.AddToko.route) {
            AddTokoScreen(navController)
        }
        composable(route = Screen.Produk.route) {
            ProdukScreen(navController, viewModel)
        }
        composable(route = Screen.AddProdukBaru.route) {
            AddProduckScreen(navController, viewModel)
        }

        composable(route = Screen.Stok.route) {
            StokScreen(navController, viewModel)
        }
        composable(route = Screen.LaporanPenjualan.route) {
            LaporanPenjualanScreen(navController)
        }
        composable("edit_produk_screen/{produkId}") { backStackEntry ->
            val produkId = backStackEntry.arguments?.getString("produkId") ?: return@composable
            val produk = viewModel.produkList.value.find { it.id == produkId } ?: return@composable

            EditProdukScreen(navController = navController, viewModel = viewModel, produk = produk)
        }
        composable("edit_stok_screen/{produkId}") { backStackEntry ->
            val produkId = backStackEntry.arguments?.getString("produkId") ?: return@composable
            val produk = viewModel.produkList.value.find { it.id == produkId } ?: return@composable

            UpdateStokScreen(navController = navController, viewModel = viewModel, produk = produk)
        }
    }
}