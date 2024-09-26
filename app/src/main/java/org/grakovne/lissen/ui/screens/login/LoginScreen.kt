package org.grakovne.lissen.ui.screens.login

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import org.grakovne.lissen.viewmodel.ServerConnectionViewModel
import org.grakovne.lissen.viewmodel.ServerConnectionViewModel.LoginState


@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: ServerConnectionViewModel = hiltViewModel()
) {

    val loginState by viewModel.loginState.collectAsState()
    val loginError by viewModel.loginError.observeAsState()

    val host by viewModel.host.observeAsState("")
    val username by viewModel.username.observeAsState("")
    val password by viewModel.password.observeAsState("")

    var showPassword by remember { mutableStateOf(false) }

    val context = LocalContext.current

    LaunchedEffect(loginState) {
        when (loginState) {
            is LoginState.Success -> {
                navController
                    .navigate("library_screen") {
                        popUpTo("login_screen") { inclusive = true }
                    }
            }

            is LoginState.Error -> loginError
                ?.let {
                    if (it.isNotBlank()) {
                        Toast.makeText(context, loginError, Toast.LENGTH_SHORT).show()
                    }
                }

            is LoginState.Idle -> {}
            is LoginState.Loading -> {}
        }

        viewModel.readyToLogin()
    }

    Scaffold(
        modifier = Modifier
            .systemBarsPadding()
            .fillMaxSize(),
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Connect to Server",
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp,
                        textAlign = TextAlign.Start,
                    ),
                    modifier = Modifier
                        .padding(vertical = 32.dp)
                )

                OutlinedTextField(
                    value = host,
                    onValueChange = {
                        viewModel.setHost(it)
                    },
                    label = { Text("Server URL") },
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(vertical = 4.dp)
                )

                OutlinedTextField(
                    value = username,
                    onValueChange = {
                        viewModel.setUsername(it)
                    },
                    label = { Text("Login") },
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(vertical = 12.dp)
                )

                OutlinedTextField(
                    value = password,
                    visualTransformation = if (!showPassword) PasswordVisualTransformation() else VisualTransformation.None,
                    onValueChange = {
                        viewModel.setPassword(it)
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = { showPassword = !showPassword }
                        ) {
                            Icon(
                                imageVector = if (showPassword) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                contentDescription = "Show Password"
                            )
                        }
                    },
                    label = { Text("Password") },
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(vertical = 4.dp)
                )

                Button(
                    onClick = {
                        viewModel.login()
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(vertical = 32.dp)
                ) {
                    Text(text = "Connect")
                }
            }
        }
    )


}
