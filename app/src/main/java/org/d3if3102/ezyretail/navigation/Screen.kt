package org.d3if3102.ezyretail.navigation

//import org.d3if3102.ezyretail.ui.screen.produck.KEY_ID_PRODUK

sealed class Screen (val route: String) {
    data object Login : Screen("login")
    data object Register : Screen("register")
    data object MainMenu : Screen("mainmenu")
    data object AddToko : Screen("addtoko")
    data object Produk : Screen("produck")
    data object AddProdukBaru : Screen("addproduk")
    data object Stok : Screen("stok")
    data object UpdateStok : Screen("addstokscreen")
    data object TransaksiMenu : Screen("transaksimenu")
    data object Transaksi : Screen("transaksi")
    data object LaporanPenjualan : Screen("laporanpenjualan")
    data object Keranjang : Screen("keranjang")

    data object Riwayat : Screen("riwayat")
}