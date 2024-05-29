package org.d3if3102.ezyretail.ui.screen.transaksi

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.d3if3102.ezyretail.R
import org.d3if3102.ezyretail.navigation.Screen
//import org.d3if3102.ezyretail.ui.screen.produck.ProduckContent
//import org.d3if3102.ezyretail.ui.screen.stok.CategoryContent
//import org.d3if3102.ezyretail.ui.screen.stok.ProdukContent
//import org.d3if3102.ezyretail.ui.screen.stok.StokScreen
import org.d3if3102.ezyretail.ui.theme.EzyRetailTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransakiMenu(navController: NavHostController) {
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
                        text = stringResource(id = R.string.transaction_menu),
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color(0xFF6200EA),
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                actions ={
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Filled.Info,
                            contentDescription = stringResource(id = R.string.info),
                            tint = Color.White
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Screen.AddProdukBaru.route) },
                containerColor = Color(0xFF6200EA)
            ) {
                Row(
                    modifier = Modifier.padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = stringResource(id = R.string.add_produck),
                        tint = Color.White
                    )
                    Text(
                        text = stringResource(id = R.string.add_produck),
                        color = Color.White
                    )
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            Column {
                SearchField( )
            }
        }
    }
}

@Composable
fun SearchField() {
    var searchQuery by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.padding(10.dp)
    ){
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
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
fun TransaksiScreenPreview() {
    EzyRetailTheme {
        TransakiMenu(rememberNavController())
    }
}
