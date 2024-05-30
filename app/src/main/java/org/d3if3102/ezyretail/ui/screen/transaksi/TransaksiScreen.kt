package org.d3if3102.ezyretail.ui.screen.transaksi

import Produk
import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.d3if3102.ezyretail.R
import org.d3if3102.ezyretail.navigation.Screen
import org.d3if3102.ezyretail.ui.screen.authentication.MainViewModel
import org.d3if3102.ezyretail.ui.screen.produck.saveProduk
import org.d3if3102.ezyretail.ui.theme.EzyRetailTheme
import java.util.UUID


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransaksiScreen(navController: NavHostController, viewModel: MainViewModel, produk: Produk) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.back),
                            tint = Color.White
                        )
                    }
                },
                title = {
                    Text(
                        text = "Transaksi",
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color(0xFF6200EA),
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding)
        ) {
            TransaksiScreenContent(navController, viewModel, produk)
        }
    }
}

@Composable
fun TransaksiScreenContent(navController: NavHostController, viewModel: MainViewModel, produk: Produk) {
    var jumlah by remember { mutableStateOf(1) }
    var catatan by remember { mutableStateOf(TextFieldValue("")) }
    val totalHarga = produk.hargaJual?.times(jumlah) ?: 0
    var namaP by remember { mutableStateOf(produk.namaProduk) }



    val auth = FirebaseAuth.getInstance()
    val currentUser by viewModel.currentUser.collectAsState()

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row {
            Column {
                Text(
                    text = "Harga per Item",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Text(
                    text = "Rp. ${produk.hargaJual ?: 0}",
                    fontSize = 20.sp,
                    color = Color.Green
                )
            }
        }
        Spacer(
            modifier = Modifier.height(8.dp)
        )
        Text(
            text = produk.namaProduk ?: "Unknown",
            fontSize = 24.sp,
        )
        Divider(
            color = Color.Gray,
            thickness = 1.dp,
            modifier = Modifier
                .padding(top = 16.dp, bottom = 16.dp)
        )
        Text(
            text = "Jumlah",
            style = MaterialTheme.typography.bodyLarge,
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.padding(4.dp))
        Text(
            text = "Stok: ${produk.stok ?: 0} buah",
            fontSize = 14.sp,
            color = Color.Black
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = jumlah.toString(),
                fontSize = 24.sp,
                modifier = Modifier.padding(16.dp)
            )
            Button(
                onClick = { if (jumlah > 1) jumlah-- },
                modifier = Modifier.padding(8.dp)
            ) {
                Text(text = "-")
            }
            Button(
                onClick = { if (jumlah < (produk.stok ?: 0)) jumlah++ },
                modifier = Modifier.padding(8.dp)
            ) {
                Text(text = "+")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("Catatan", fontSize = 18.sp)
        OutlinedTextField(
            value = catatan,
            onValueChange = { catatan = it },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent)
                .padding(top = 8.dp),
            placeholder = {
                Text("Contoh: Tidak pedas", color = Color.Gray)
            }
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )
        Text(
            text = "Total",
            fontSize = 18.sp
        )
        Text(
            text = "Rp. $totalHarga",
            fontSize = 24.sp,
            color = Color.Green
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                addKeranjang(
                    auth = auth,
                    produkId = produk.id.toString(),
                    namaProduk = namaP.toString(),
                    jumlah = jumlah,
                    totalHarga = totalHarga,
                    context = context
                ) { success, errorMessage ->
                    if (success) {
//                        navController.navigate(Screen.Produk.route)
                    } else {
                        errorMessage?.let { Log.e("FirestoreError", it) }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("SIMPAN")
        }
    }
}
fun addKeranjang(
    auth: FirebaseAuth,
    produkId: String,
    namaProduk: String,
    jumlah: Int,
    totalHarga: Int,
    context: Context,
    onComplete: (Boolean, String?) -> Unit
) {
    val userId = auth.currentUser?.uid
    if (userId != null) {
        val db = FirebaseFirestore.getInstance()

        val keranjangId = UUID.randomUUID().toString()

        val keranjangItem = Keranjang(
            id = keranjangId,
            namaProduk = namaProduk,
            jumlah = jumlah,
            totalHarga = totalHarga,
        )

        val produkRef = db.collection("users").document(userId)
            .collection("produk").document(produkId)

        db.runTransaction { transaction ->
            val snapshot = transaction.get(produkRef)
            if (snapshot.exists()) {
                val stokSaatIni = snapshot.getLong("stok") ?: 0L
                val stokBaru = stokSaatIni - jumlah
                if (stokBaru >= 0) {
                    transaction.update(produkRef, "stok", stokBaru)

                    val keranjangRef = db.collection("users").document(userId)
                        .collection("keranjang").document(keranjangId)
                    transaction.set(keranjangRef, keranjangItem)

                    // Return success
                    true
                } else {
                    throw Exception("Stok tidak mencukupi untuk produk $namaProduk")
                }
            } else {
                throw Exception("Produk $namaProduk tidak ditemukan.")
            }
        }.addOnSuccessListener {
            onComplete(true, null)
        }.addOnFailureListener { e ->
            onComplete(false, e.message)
        }
    } else {
        onComplete(false, "User not logged in")
    }
}


//fun addKeranjang(
//    auth: FirebaseAuth,
//    produk: String,
//    jumlah: Int,
//    totalHarga: Int,
//    context: Context,
//    onComplete: (Boolean, String?) -> Unit
//) {
//    val userId = auth.currentUser?.uid
//    if (userId != null) {
//        val db = FirebaseFirestore.getInstance()
//
//        val keranjangId = UUID.randomUUID().toString()
//
//        val tanaman = Keranjang(
//            id = keranjangId,
//            namaProduk = produk,
//            jumlah = jumlah,
//            totalHarga = totalHarga,
//        )
//
//        db.collection("users").document(userId).collection("keranjang") //path penyimpanan
//            .document(keranjangId)
//            .set(tanaman)
//            .addOnSuccessListener {
//                onComplete(true, null)
//            }
//            .addOnFailureListener { e ->
//                onComplete(false, e.message)
//            }
//    } else {
//        onComplete(false, "User not logged in")
//    }
//}

data class Keranjang(
    val id: String = "",
    val namaProduk: String = "",
    val jumlah: Int = 0,
    val totalHarga: Int = 0
)


@Preview(showBackground = true)
@Composable
fun TransaksiScreenPreview() {
    val dummyProduk = Produk(id = "1", namaProduk = "Aqua 600ML", hargaJual = 5000, stok = 97)
    EzyRetailTheme {
        TransaksiScreen(rememberNavController(), MainViewModel(), produk = dummyProduk)
    }
}

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun TransaksiScreen(navController: NavHostController, produk: Produk) {
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                navigationIcon = {
//                    IconButton(onClick = { navController.popBackStack() }) {
//                        Icon(
//                            imageVector = Icons.Filled.ArrowBack,
//                            contentDescription = stringResource(id = R.string.back),
//                            tint = Color.White
//                        )
//                    }
//                },
//                title = {
//                    Text(
//                        text = "Transaksi",
//                        color = Color.White
//                    )
//                },
//                colors = TopAppBarDefaults.mediumTopAppBarColors(
//                    containerColor = Color(0xFF6200EA),
//                    titleContentColor = MaterialTheme.colorScheme.primary,
//                ),
//            )
//        }
//    ) { Padding ->
//        Column (
//            modifier = Modifier.padding(Padding)
//        ){
//            TransaksiScreenContent(produk)
//        }
//    }
//}
//
//@Composable
//fun TransaksiScreenContent(produk: Produk) {
//    var jumlah by remember { mutableStateOf(1) }
//    var catatan by remember { mutableStateOf(TextFieldValue("")) }
//    val totalHarga = produk.hargaJual?.times(jumlah) ?: 0
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp)
//    ) {
//        Row {
//            Column {
//                Text(
//                    text = "Harga per Item",
//                    fontSize = 14.sp,
//                    color = Color.Gray
//                )
//                Text(
//                    text = "Rp. ${produk.hargaJual ?: 0}",
//                    fontSize = 20.sp,
//                    color = Color.Green
//                )
//            }
//        }
//        Spacer(
//            modifier = Modifier
//                .height(8.dp)
//        )
//        Text(
//            text = produk.namaProduk ?: "Unknown",
//            fontSize = 24.sp,
//        )
//        Divider(
//            color = Color.Gray,
//            thickness = 1.dp,
//            modifier = Modifier
//                .padding(top = 16.dp, bottom = 16.dp)
//        )
//        Text(
//            text = "Jumlah",
//            style = MaterialTheme.typography.bodyLarge,
//            fontSize = 18.sp
//        )
//        Spacer(modifier = Modifier.padding(4.dp))
//        Text(
//            text = "Stok: ${produk.stok ?: 0} buah",
//            fontSize = 14.sp,
//            color = Color.Black
//        )
//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            Text(
//                text = jumlah.toString(),
//                fontSize = 24.sp,
//                modifier = Modifier.padding(16.dp)
//            )
//            Button(
//                onClick = { if (jumlah > 1) jumlah-- },
//                modifier = Modifier.padding(8.dp)
//            ) {
//                Text(text = "-")
//            }
//            Button(
//                onClick = { if (jumlah < (produk.stok ?: 0)) jumlah++ },
//                modifier = Modifier.padding(8.dp)
//            ) {
//                Text(text = "+")
//            }
//        }
//        Spacer(modifier = Modifier.height(16.dp))
//        Text("Catatan", fontSize = 18.sp)
//        OutlinedTextField(
//            value = catatan,
//            onValueChange = { catatan = it },
//            modifier = Modifier
//                .fillMaxWidth()
//                .background(Color.Transparent)
//                .padding(top = 8.dp),
//            placeholder = {
//                Text("Contoh: Tidak pedas", color = Color.Gray)
//            }
//        )
//
//        Spacer(
//            modifier = Modifier
//                .height(16.dp)
//        )
//        Text(
//            text = "Total",
//            fontSize = 18.sp
//        )
//        Text(
//            text = "Rp. $totalHarga",
//            fontSize = 24.sp,
//            color = Color.Green
//        )
//        Spacer(modifier = Modifier.height(16.dp))
//        Button(
//            onClick = {
//                viewModel.addToKeranjang(produk, jumlah, totalHarga)
//                navController.navigate(Screen.Keranjang.route)
//            },
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Text("SIMPAN")
//        }
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun TransaksiScreenPreview() {
//    val dummyProduk = Produk(id = "1", namaProduk = "Aqua 600ML", hargaJual = 5000, stok = 97)
//    EzyRetailTheme {
//        TransaksiScreen(rememberNavController(), produk = dummyProduk)
//    }
//}
