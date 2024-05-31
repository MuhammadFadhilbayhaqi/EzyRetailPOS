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
import org.d3if3102.ezyretail.ui.screen.produck.AddProduckScreen
import org.d3if3102.ezyretail.ui.screen.produck.ProdukScreen
import org.d3if3102.ezyretail.ui.screen.manage.StokScreen
import org.d3if3102.ezyretail.ui.screen.manage.UpdateProdukScreen
import org.d3if3102.ezyretail.ui.screen.transaksi.KeranjangScreen
import org.d3if3102.ezyretail.ui.screen.transaksi.RiwayatScreen
import org.d3if3102.ezyretail.ui.screen.transaksi.TransakiMenuScreen
import org.d3if3102.ezyretail.ui.screen.transaksi.TransaksiScreen

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
        composable(route = Screen.Produk.route) {
            ProdukScreen(navController, viewModel)
        }
        composable(route = Screen.AddProdukBaru.route) {
            AddProduckScreen(navController, viewModel)
        }

        composable(route = Screen.Stok.route) {
            StokScreen(navController, viewModel)
        }
        composable("edit_produk_screen/{produkId}") { backStackEntry ->
            val produkId = backStackEntry.arguments?.getString("produkId") ?: return@composable
            val produk = viewModel.produkList.value.find { it.id == produkId } ?: return@composable

            UpdateProdukScreen(navController = navController, viewModel = viewModel, produk = produk)
        }
        composable("edit_stok_screen/{produkId}") { backStackEntry ->
            val produkId = backStackEntry.arguments?.getString("produkId") ?: return@composable
            val produk = viewModel.produkList.value.find { it.id == produkId } ?: return@composable

            UpdateProdukScreen(navController = navController, viewModel = viewModel, produk = produk)
        }
        composable(route = Screen.TransaksiMenu.route) {
            TransakiMenuScreen(navController, viewModel)
        }
        composable("transaksi_screen/{produkId}") { backStackEntry ->
            val produkId = backStackEntry.arguments?.getString("produkId") ?: return@composable
            val produk = viewModel.produkList.value.find { it.id == produkId } ?: return@composable

            TransaksiScreen(navController = navController, MainViewModel(),produk = produk)
        }
        composable(route = Screen.Keranjang.route) {
            KeranjangScreen(navController, viewModel)
        }

        composable(route = Screen.Riwayat.route) {
            RiwayatScreen(navController, viewModel)
        }
    }
}