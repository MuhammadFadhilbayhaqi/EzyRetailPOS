package org.d3if3102.ezyretail.ui.screen

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExitToApp
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.d3if3102.ezyretail.R
import org.d3if3102.ezyretail.navigation.Screen
import org.d3if3102.ezyretail.ui.screen.authentication.MainViewModel
import org.d3if3102.ezyretail.ui.theme.EzyRetailTheme

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainMenu(navController: NavHostController, viewModel: MainViewModel) {
    LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                actions ={
                    IconButton(onClick = { viewModel.logout(navController) }) {
                        Icon(Icons.Outlined.ExitToApp,
                            contentDescription = stringResource(R.string.logout),
                            tint = Color.Black,
                        )
                    }
//                    if (viewModel.currentUser.value != null) {
//                        LogoutButton(viewModel = viewModel, navController = navController)
//                    }
                }
            )
        }
    ) { innerPadding ->
        MainScreenContent(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            viewModel = viewModel
        )
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun MainScreenContent(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: MainViewModel
) {

        Column(
            modifier = modifier.fillMaxSize()
        ) {

            val currentUser by viewModel.currentUser.collectAsState()

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Store Logo",
                        modifier = Modifier.size(250.dp)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Selamat datang, ${currentUser?.username ?: "User"}",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = "di EazyRetail",
                        fontSize = 18.sp,
                        color = Color.Gray
                    )
                }
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(Color(0xFF6200EA),
                        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.Center,
                ) {
                    items(4) { index ->
                        when (index) {
                            0 -> MenuItem(
                                iconId = R.mipmap.produck_logo,
                                onClick = { navController.navigate(Screen.Produk.route) },
                                label = "Product"
                            )
                            1 -> MenuItem(
                                iconId = R.mipmap.manage_logo,
                                onClick = { navController.navigate(Screen.Stok.route) },
                                label = "Manage"
                            )
                            2 -> MenuItem(
                                iconId = R.mipmap.laporan_logo,
                                onClick = { navController.navigate(Screen.Riwayat.route) },
                                label = "Laporan"
                            )
                            3 -> MenuItem(
                                iconId = R.mipmap.transaction_logo,
                                onClick = { navController.navigate(Screen.TransaksiMenu.route) },
                                label = "Transaksi"
                            )
                        }
                    }
                }
            }
        }
    }

@Composable
fun MenuItem(iconId: Int, label: String, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .padding(8.dp)
            .height(100.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = iconId),
                    contentDescription = label,
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = label,
                    fontSize = 16.sp,
                    color = Color.White
                )
            }
        }
    }
}

//@Composable
//fun LogoutButton(viewModel: MainViewModel, navController: NavHostController) {
//    IconButton(onClick = { viewModel.logout(navController) }) {
//        Icon(Icons.Outlined.ExitToApp,
//            contentDescription = stringResource(R.string.logout),
//            tint = Color.Black,
//            )
//    }
//}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
fun ScreenPreview() {
    EzyRetailTheme {
        MainMenu(rememberNavController(), viewModel = MainViewModel())
    }
}
