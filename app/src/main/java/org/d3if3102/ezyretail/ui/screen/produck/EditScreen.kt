package org.d3if3102.ezyretail.ui.screen.produck

import Produk
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.d3if3102.ezyretail.R
import org.d3if3102.ezyretail.navigation.Screen
import org.d3if3102.ezyretail.ui.screen.authentication.MainViewModel
import org.d3if3102.ezyretail.ui.theme.EzyRetailTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProdukScreen(navController: NavHostController, viewModel: MainViewModel, produk: Produk) {
    LocalContext.current

    var namaProduk by remember { mutableStateOf(produk.namaProduk ?: "") }
    var hargaBeli by remember { mutableIntStateOf(produk.hargaBeli ?: 0) }
    var hargaJual by remember { mutableIntStateOf(produk.hargaJual ?: 0) }
    var deskripsi by remember { mutableStateOf(produk.deskripsi ?: "") }

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
                        text = stringResource(id = R.string.edit_produck),
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color(0xFF6200EA),
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            TextField(
                value = namaProduk,
                onValueChange = { namaProduk = it },
                label = { Text(text = stringResource(id = R.string.produck_name)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Next
                ),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            )

            TextField(
                value = hargaBeli.toString(),
                onValueChange = { hargaBeli = it.toIntOrNull() ?: 0 },
                label = { Text(text = stringResource(id = R.string.buy_price)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            )

            TextField(
                value = hargaJual.toString(),
                onValueChange = { hargaJual = it.toIntOrNull() ?: 0 },
                label = { Text(text = stringResource(id = R.string.sale_price)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            )

                TextField(
                    value = deskripsi,
                    onValueChange = { deskripsi = it },
                    label = { Text(text = stringResource(id = R.string.produck_description)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Done
                    ),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth().padding(8.dp)
                )

            Button(
                onClick = {
                    val updatedProduk = produk.copy(
                        namaProduk = namaProduk,
                        hargaBeli = hargaBeli,
                        hargaJual = hargaJual,
                        deskripsi = deskripsi
                    )
                    viewModel.updateProduk(updatedProduk) { success, errorMessage ->
                        if (success) {
                            navController.navigate(Screen.MainMenu.route)
                        } else {
                            errorMessage?.let { Log.e("FirestoreError", it) }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFF6200EA))
            ) {
                Text(
                    text = stringResource(id = R.string.simpan),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditScreenPreview() {
    EzyRetailTheme {
        EditProdukScreen(rememberNavController(), viewModel = MainViewModel(), produk = Produk())
    }
}