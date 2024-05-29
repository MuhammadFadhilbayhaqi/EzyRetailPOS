package org.d3if3102.ezyretail.ui.screen.authentication

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.d3if3102.ezyretail.R
import org.d3if3102.ezyretail.navigation.Screen

@Composable
fun RegisterScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel
) {
    var username by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var emailError by rememberSaveable { mutableStateOf(false) }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var errorMessage by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null,
                modifier = Modifier.size(300.dp)
            )
            Text(
                text = stringResource(id = R.string.register_info),
                style = TextStyle(fontSize = 16.sp),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text( "Username" ) },
                modifier = Modifier.fillMaxWidth(0.8f)
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text( stringResource(id = R.string.email)) },
                modifier = Modifier.fillMaxWidth(0.8f)
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(0.8f),
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.padding())
            Button(
                onClick = {
                    viewModel.registerUser(email, password, username,
                        onSuccess = { navController.navigate(Screen.MainMenu.route) },
                        onFailure = {
                            Toast.makeText(context, "Gagal mendaftar", Toast.LENGTH_SHORT).show()
                        }
                    )
                          },
                modifier = Modifier
                    .fillMaxWidth(0.8f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6200EA),
                    contentColor = Color.White
                )
            ) {
                Text("Daftar")
            }
            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
            Text(
                text = "Sudah Punya Akun?",
                style = TextStyle(color = Color.Blue, textDecoration = TextDecoration.Underline),
                modifier = Modifier.clickable {navController.navigate(Screen.Login.route)}
            )
        }
    }
}

private suspend fun createUserWithEmailAndPassword(email: String, password: String): Boolean {
    return withContext(Dispatchers.IO) {
        try {
            val authResult = FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).await()
            authResult.user != null
        } catch (e: Exception) {
            false
        }
    }
}

private fun isValidCredentials(email: String, password: String): Boolean {
    return email.isNotEmpty() && password.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}
@Preview(showBackground = true)
@Composable
fun RegisScreenPreview() {
    RegisterScreen(rememberNavController(), viewModel = MainViewModel())
}