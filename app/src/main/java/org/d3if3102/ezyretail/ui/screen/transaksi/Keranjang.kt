package org.d3if3102.ezyretail.ui.screen.transaksi

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import org.d3if3102.ezyretail.R
import org.d3if3102.ezyretail.navigation.Screen
import org.d3if3102.ezyretail.ui.screen.authentication.MainViewModel
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KeranjangScreen(navController: NavHostController, viewModel: MainViewModel) {
    val auth = FirebaseAuth.getInstance()
    val userId = auth.currentUser?.uid
    val keranjangItems by viewModel.keranjang.collectAsState()

    // Mendapatkan data keranjang saat screen ini dibuka
    LaunchedEffect(userId) {
        if (userId != null) {
            viewModel.getKeranjangItems(userId)
        }
    }

    // Hitung total harga dari semua item dalam keranjang
    val totalHarga = keranjangItems.sumOf { it.totalHarga }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(Screen.TransaksiMenu.route) }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.back),
                            tint = Color.White
                        )
                    }
                },
                title = {
                    Text(
                        text = "Keranjang",
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color(0xFF6200EA),
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
            )
        },
        bottomBar = {
            if (keranjangItems.isNotEmpty()) {
                Button(
                    onClick = {
                        addTransaksi(
                            auth = auth,
                            keranjangItems = keranjangItems,
                            onComplete = { success, errorMessage ->
                                if (success) {
                                    // Penyimpanan transaksi berhasil, Anda dapat melakukan navigasi atau menampilkan pesan sukses di sini
                                } else {
                                    // Penyimpanan transaksi gagal, tampilkan pesan kesalahan
                                    errorMessage?.let { Log.e("FirestoreError", it) }
                                }
                            }
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text("BELI - Total: Rp. $totalHarga")
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding)
        ) {
            if (keranjangItems.isEmpty()) {
                Text("Keranjang Anda kosong.", modifier = Modifier.padding(16.dp))
            } else {
                LazyColumn {
                    items(keranjangItems) { item ->
                        KeranjangItemComposable(item, viewModel, userId!!)
                    }
                }
            }
        }
    }
}

@Composable
fun KeranjangItemComposable(item: Keranjang, viewModel: MainViewModel, userId: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.White, shape = RoundedCornerShape(8.dp))
            .shadow(1.dp)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .padding(start = 8.dp)
        ) {
            Text(text = "Produk: ${item.namaProduk}", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(text = "Jumlah: ${item.jumlah}", fontSize = 16.sp)
            Row {
                Text(text = "Total Harga : ")
                Text(text = "Rp. ${item.totalHarga}", fontSize = 16.sp, color = Color.Green)
            }
        }
        IconButton(onClick = {
            viewModel.deleteKeranjangItem(userId, item.id) { success, errorMessage ->
                if (success) {
                    // Handle success, e.g., show a message or update the UI
                } else {
                    // Handle failure, e.g., show an error message
                    errorMessage?.let { Log.e("FirestoreError", it) }
                }
            }
        }) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
        }
    }
}

fun addTransaksi(
    auth: FirebaseAuth,
    keranjangItems: List<Keranjang>,
    onComplete: (Boolean, String?) -> Unit
) {
    val userId = auth.currentUser?.uid
    if (userId != null) {
        val db = FirebaseFirestore.getInstance()
        val batch = db.batch()

        keranjangItems.forEach { item ->
            val transaksiId = UUID.randomUUID().toString()

            val transaksiData = hashMapOf(
                "tanggalPembelian" to FieldValue.serverTimestamp(),
                "namaProduk" to item.namaProduk,
                "jumlah" to item.jumlah,
                "totalHarga" to item.totalHarga
            )

            val transaksiRef = db.collection("users").document(userId)
                .collection("transaksi").document(transaksiId)

            batch.set(transaksiRef, transaksiData)

            // Kurangi stok produk yang dibeli
            val produkRef = db.collection("produk").document(item.id)
            produkRef.get().addOnSuccessListener { document ->
                if (document.exists()) {
                    // Jika produk ada, kurangi stok
                    val stokSaatIni = document.getLong("stok") ?: 0L
                    val stokBaru = stokSaatIni - item.jumlah
                    if (stokBaru >= 0) {
                        batch.update(produkRef, "stok", stokBaru)
                    } else {
                        onComplete(false, "Stok tidak mencukupi untuk produk ${item.namaProduk}")
                        return@addOnSuccessListener
                    }
                } else {
                    onComplete(false, "Produk ${item.namaProduk} tidak ditemukan.")
                    return@addOnSuccessListener
                }
            }.addOnFailureListener { e ->
                onComplete(false, e.message)
                return@addOnFailureListener
            }
        }

        // Commit batch operasi
        batch.commit()
            .addOnSuccessListener {
                // Hapus isi keranjang setelah transaksi berhasil disimpan
                val keranjangCollection = db.collection("users").document(userId).collection("keranjang")
                keranjangItems.forEach { item ->
                    val keranjangRef = keranjangCollection.document(item.id)
                    keranjangRef.delete().addOnFailureListener { e ->
                        Log.e("FirestoreError", "Error deleting cart item: ${item.id}", e)
                    }
                }
                onComplete(true, null)
            }
            .addOnFailureListener { e ->
                onComplete(false, e.message)
            }
    } else {
        onComplete(false, "User not logged in")
    }
}













