package org.d3if3102.ezyretail.ui.screen.toko

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import org.d3if3102.ezyretail.ui.theme.EzyRetailTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTokoScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.back),
                            tint = Color.Black
                        )
                    }
                },
                title = {
                    Text(
                        text = stringResource(id = R.string.produck_menu),
                        color = Color.Black
                    )
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color(0xFF6200EA),
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            AddTokoContent()
        }
    }
}

@Composable
fun AddTokoContent() {
    var shop_name by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        TextField(
            value = shop_name,
            onValueChange = { shop_name = it },
            label = { Text(text = stringResource(id = R.string.shop_name)) },
            singleLine = false,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White
            )
        )
        Spacer(modifier = Modifier.padding(16.dp))
        TextField(
            value = shop_name,
            onValueChange = { shop_name = it },
            label = { Text(text = stringResource(id = R.string.shop_address)) },
            singleLine = false,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White
            )
        )
        Spacer(modifier = Modifier.padding(16.dp))
        TextField(
            value = shop_name,
            onValueChange = { shop_name = it },
            label = { Text(text = stringResource(id = R.string.shop_phone)) },
            singleLine = false,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White
            )
        )
        Spacer(modifier = Modifier.padding(16.dp))
        TextField(
            value = shop_name,
            onValueChange = { shop_name = it },
            label = { Text(text = stringResource(id = R.string.shop_deskripsion)) },
            singleLine = false,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White
            )
        )
        Spacer(modifier = Modifier.padding(16.dp))
        Button(
            onClick = {  },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF6200EA)
            )
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

@Composable
@Preview(showBackground = true)
fun AddTokoPreview () {
    EzyRetailTheme {
        AddTokoScreen(rememberNavController())
    }
}