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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
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
fun LoginScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel
) {
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
                modifier = Modifier.size(250.dp)
            )
            Text(
                text = "Silahkan Masukkan Username dan Password",
                style = TextStyle(fontSize = 16.sp),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(0.8f)
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(0.8f),
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.padding(1.dp))
            Button(
                onClick = { viewModel.loginUser(email, password, {
                    // Jika login berhasil, arahkan ke halaman Home
                    navController.navigate(Screen.MainMenu.route)
                }, {
                    Toast.makeText(context, R.string.invalid, Toast.LENGTH_SHORT).show()
                }) },
                modifier = Modifier
                    .fillMaxWidth(0.8f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6200EA),
                    contentColor = Color.White
                )
            ) {
                Text("Login")
            }
            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
            Button(
                onClick = { navController.navigate(Screen.Register.route) },
                modifier = Modifier.fillMaxWidth(0.8f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6200EA),
                    contentColor = Color.White
                )
            ) {
                Text(text = "Register")
            }
        }
    }
}

private suspend fun signInWithEmailAndPassword(email: String, password: String): Boolean {
    return withContext(Dispatchers.IO) {
        try {
            val authResult = FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).await()
            authResult.user != null
        } catch (e: Exception) {
            false
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
fun LoginScreenPreview() {
    val navController = rememberNavController()
    LoginScreen(navController = navController, viewModel = MainViewModel())
}