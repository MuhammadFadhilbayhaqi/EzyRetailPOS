package org.d3if3102.ezyretail.ui.screen.produck

//import org.d3if3102.ezyretail.model.Produk
//import org.d3if3102.ezyretail.database.ProdukDb
import Produk
import android.content.Context
import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.runtime.collectAsState
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.d3if3102.ezyretail.R
import org.d3if3102.ezyretail.model.User
import org.d3if3102.ezyretail.navigation.Screen
import org.d3if3102.ezyretail.ui.screen.authentication.MainViewModel
import org.d3if3102.ezyretail.ui.theme.EzyRetailTheme
import java.util.UUID

const val KEY_ID_PRODUK = "idProduk"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProduckScreen(navController: NavHostController, viewModel: MainViewModel) {
    val auth = FirebaseAuth.getInstance()
    val currentUser by viewModel.currentUser.collectAsState()

    LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(Screen.Produk.route) }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.back),
                            tint = Color.White
                        )
                    }
                },
                title = {
                    Text(
                        text = stringResource(id = R.string.produck_menu),
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
        AddProduckContent(
            modifier = Modifier.padding(innerPadding),
            auth = auth,
            navController = navController,
            viewModel = viewModel,
            currentUser = currentUser
        )
    }
}

@Composable
fun AddProduckContent(
    modifier: Modifier,
    auth: FirebaseAuth,
    navController: NavHostController,
    viewModel: MainViewModel,
    currentUser: User?
) {

    val context = LocalContext.current

    var namaProduk by remember { mutableStateOf("") }
    var hargaBeli by remember { mutableIntStateOf(0) }
    var hargaJual by remember { mutableIntStateOf(0) }
    var deskripsi by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
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
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        TextField(
            value = hargaBeli.toString(), // Convert Int to String for display
            onValueChange = { newValue ->
                hargaBeli = newValue.toIntOrNull() ?: 0 // Convert String to Int, default to 0 if invalid
            },
            label = { Text(text = stringResource(id = R.string.buy_price)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        TextField(
            value = hargaJual.toString(), // Convert Int to String for display
            onValueChange = { newValue ->
                hargaJual = newValue.toIntOrNull() ?: 0 // Convert String to Int, default to 0 if invalid
            },
            label = { Text(text = stringResource(id = R.string.sale_price)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
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
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        Button(
            onClick = {
                saveProduk(
                    auth = auth,
                    namaProduk = namaProduk,
                    hargaBeli = hargaBeli,
                    hargaJual = hargaJual,
                    deskripsi = deskripsi,
                    context = context
                ) { success, errorMessage ->
                    if (success) {
                        navController.navigate(Screen.Produk.route)
                    } else {
                        errorMessage?.let { Log.e("FirestoreError", it) }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFF6200EA))
        ) {
            Text(
                text = stringResource(id = R.string.simpan),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}




        fun saveProduk(
            auth: FirebaseAuth,
            namaProduk: String,
            hargaBeli: Int,
            hargaJual: Int,
            deskripsi: String,
            context: Context,
            onComplete: (Boolean, String?) -> Unit
        ) {
            val userId = auth.currentUser?.uid
            if (userId != null) {
                val db = FirebaseFirestore.getInstance()

                // buat id dokumen unik (primary key)
                val produkId = UUID.randomUUID().toString()

                val tanaman = Produk(
                    id = produkId,
                    namaProduk = namaProduk,
                    hargaBeli = hargaBeli,
                    hargaJual = hargaJual,
                    deskripsi = deskripsi
                )

                db.collection("users").document(userId).collection("produk") //path penyimpanan
                    .document(produkId)
                    .set(tanaman)
                    .addOnSuccessListener {
                        onComplete(true, null)
                    }
                    .addOnFailureListener { e ->
                        onComplete(false, e.message)
                    }
            } else {
                onComplete(false, "User not logged in")
            }
        }

        @Preview(showBackground = true)
    @Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
fun AddScreenPreview() {
    EzyRetailTheme {
        AddProduckScreen(rememberNavController(), viewModel = MainViewModel())
    }
}
