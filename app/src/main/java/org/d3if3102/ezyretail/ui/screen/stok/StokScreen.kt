package org.d3if3102.ezyretail.ui.screen.stok

import Produk
import android.content.res.Configuration
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import org.d3if3102.ezyretail.R
import org.d3if3102.ezyretail.ui.screen.authentication.MainViewModel
import org.d3if3102.ezyretail.ui.theme.EzyRetailTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StokScreen(navController: NavHostController, viewModel: MainViewModel) {
    val auth = FirebaseAuth.getInstance()
    val currentUser by viewModel.currentUser.collectAsState()

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
                        text = stringResource(id = R.string.stok_menu),
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color(0xFF6200EE),
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
            )
        },
    ) { innerPadding ->
        StokScreenContent(modifier = Modifier.padding(innerPadding), navController, viewModel)
    }
}

@Composable
fun StokScreenContent(modifier: Modifier, navController: NavHostController, viewModel: MainViewModel) {
    val currentUser by viewModel.currentUser.collectAsState()
    val produkList by viewModel.produkList.collectAsState()

    LaunchedEffect(currentUser) {
        currentUser?.uid?.let { userId ->
            viewModel.getProdukList(userId)
        }
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
    ){
        if (produkList.isNotEmpty()) {
            LazyColumn {
                items(produkList) { produk ->
                    StokItem(
                        produk = produk,
                        onEditClick = { produk ->
                            navController.navigate("edit_stok_screen/${produk.id}") {
                                // Sertakan data produk sebagai argumen navigasi
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
        else {
            Text(
                text = "Stok Belum Tersedia",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxSize().padding(16.dp),
                color = Color.Black
            )
        }
    }
}

@Composable
fun StokItem(produk: Produk, onEditClick: (Produk) -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .border(2.dp, Color.Black, shape = RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(Color.White),
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = produk.namaProduk ?: "Unknown",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row {
                        Text(
                            text = "Cost : ${produk.hargaBeli ?: 0}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "Sell : ${produk.hargaJual ?: 0}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Stok : ${produk.stok ?: 0}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                //buatkan viewmodel untuk edit atau update beserta screen edit nya
                IconButton(
                    onClick = { onEditClick(produk) }
                ) {
                    // Sertakan produk saat memanggil onEditClick
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = stringResource(R.string.edit),
                        tint = Color.Black
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun StokScreenPreview() {
    EzyRetailTheme {
        StokScreen(rememberNavController(), viewModel = MainViewModel())
    }
}
