package org.d3if3102.ezyretail.ui.screen.transaksi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import org.d3if3102.ezyretail.R
import org.d3if3102.ezyretail.navigation.Screen
import org.d3if3102.ezyretail.ui.screen.authentication.MainViewModel
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Scanner

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RiwayatScreen(navController: NavHostController, viewModel: MainViewModel) {
    val auth = FirebaseAuth.getInstance()
    val userId = auth.currentUser?.uid
    val transaksiItems by viewModel.transaksi.collectAsState()

    // Mendapatkan data transaksi saat screen ini dibuka
    LaunchedEffect(userId) {
        if (userId != null) {
            viewModel.getTransaksi(userId)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(Screen.MainMenu.route) }) {
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
            if (transaksiItems.isEmpty()) {
                Text("Tidak ada transaksi.", modifier = Modifier.padding(16.dp))
            } else {
                LazyColumn {
                    items(transaksiItems) { item ->
                        TransaksiItemComposable(item)
                    }
                }
            }
        }
    }
}

@Composable
fun TransaksiItemComposable(item: MainViewModel.Transaksi) {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val formattedDate = item.tanggalPembelian?.toDate()?.let { dateFormat.format(it) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.White, shape = RoundedCornerShape(8.dp))
            .shadow(1.dp)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .padding(start = 8.dp)
        ) {
            Text(text = "Produk: ${item.namaProduk}", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(text = "Jumlah: ${item.jumlah}", fontSize = 16.sp)
            Row (
                modifier = Modifier.padding()
            ){
                Text(text = "Total Harga : ")
                Text(text = "Rp. ${item.totalHarga}", fontSize = 16.sp, color = Color.Green)
            }
            Text(text = "Tanggal: $formattedDate", fontSize = 14.sp, color = Color.Gray)
        }
    }
}

