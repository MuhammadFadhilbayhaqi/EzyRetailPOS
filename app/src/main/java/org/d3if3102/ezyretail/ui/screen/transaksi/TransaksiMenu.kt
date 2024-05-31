package org.d3if3102.ezyretail.ui.screen.transaksi

import Produk
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import org.d3if3102.ezyretail.R
import org.d3if3102.ezyretail.navigation.Screen
import org.d3if3102.ezyretail.ui.screen.authentication.MainViewModel
import org.d3if3102.ezyretail.ui.theme.EzyRetailTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransakiMenuScreen(navController: NavHostController, viewModel: MainViewModel) {
    val auth = FirebaseAuth.getInstance()
    val currentUser by viewModel.currentUser.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

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
                    containerColor = Color(0xFF6200EE),
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Screen.Keranjang.route) },
                containerColor = Color(0xFF6200EA),
                shape = RoundedCornerShape(35), // Fully rounded corners
                modifier = Modifier
                    .padding(16.dp)
                    .width(330.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Text(text = "Keranjang", color = Color.White)
//                    Row {
//                        Icon(
//                            imageVector = Icons.Filled.Clear,
//                            contentDescription = "Clear",
//                            tint = Color.White
//                        )
//                        Text(
//                            text = "Clear",
//                            color = Color.White,
//                        )
//                    }
//                    Spacer(modifier = Modifier.width(16.dp))
//                    Row {
//                        Icon(
//                            imageVector = Icons.Filled.ShoppingCart,
//                            contentDescription = "Cart",
//                            tint = Color.White
//                        )
//                        Text(
//                            text = "0 item",
//                            color = Color.White,
//                            modifier = Modifier.padding(start = 4.dp)
//                        )
//                    }
//                    Spacer(modifier = Modifier.width(16.dp))
//                    Text(
//                        text = "Rp.0",
//                        color = Color.White,
//                        modifier = Modifier.padding(start = 4.dp)
//                    )
                }
            }
        }
    ) { Padding ->
        Box (
            modifier = Modifier.padding(top = 16.dp)
        ){
            Column (
                modifier = Modifier.padding(Padding)
            ){
                SearchField(searchQuery = searchQuery, onQueryChange = { searchQuery = it })
                Divider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.padding(top = 16.dp))
                TransaksiMenuContent(modifier = Modifier.padding(top = 4.dp), navController, viewModel,  searchQuery = searchQuery)
            }
        }
    }
}

@Composable
fun TransaksiMenuContent(modifier: Modifier, navController: NavHostController, viewModel: MainViewModel, searchQuery: String) {
    val currentUser by viewModel.currentUser.collectAsState()
    val produkList by viewModel.produkList.collectAsState()

    LaunchedEffect(currentUser) {
        currentUser?.uid?.let { userId ->
            viewModel.getProdukList(userId)
        }
    }

    val filteredProdukList = produkList.filter {
        it.namaProduk?.contains(searchQuery, ignoreCase = true) == true
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 8.dp),
    ) {
        if (filteredProdukList.isNotEmpty()) {
            LazyColumn {
                items(filteredProdukList) { produk ->
                    StokItem(
                        produk = produk,
                        onItemClick = { produk ->
                            navController.navigate("transaksi_screen/${produk.id}") {
                                // Sertakan data produk sebagai argumen navigasi
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        } else {
            Text(
                text = "Stok Belum Tersedia",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                color = Color.Black,
            )
        }
    }
}

@Composable
fun StokItem(produk: Produk, onItemClick: (Produk) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick(produk) }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = produk.namaProduk ?: "Unknown",
            style = MaterialTheme.typography.titleLarge
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Rp. ${produk.hargaJual ?: 0}",
                style = MaterialTheme.typography.bodyLarge,
            )
            Text(
                text = "Stok : ${produk.stok ?: 0}",
                style = MaterialTheme.typography.bodyLarge)
        }
        Divider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.padding(top = 4.dp))
    }
}

@Composable
fun SearchField(searchQuery: String, onQueryChange: (String) -> Unit) {
    Column(
        modifier = Modifier.padding(10.dp)
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { onQueryChange(it) },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, shape = RoundedCornerShape(4.dp)),
            placeholder = {
                Text(text = stringResource(id = R.string.search))
            },
            trailingIcon = {
                Icon(Icons.Filled.Search, contentDescription = null)
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                capitalization = KeyboardCapitalization.None,
                autoCorrect = true,
                imeAction = ImeAction.Search
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TransaksiMenuScreenPreview() {
    EzyRetailTheme {
        TransakiMenuScreen(rememberNavController(), viewModel = MainViewModel())
    }
}
