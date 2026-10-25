package com.example.helpdeskpro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.helpdeskpro.ui.theme.HelpdeskProTheme
import com.example.helpdeskpro.viemodel.vm.LoginViewModel
import com.example.helpdeskpro.vista.pagina.LoginPage

class MainActivity : ComponentActivity() {

    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HelpdeskProTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LoginPage(
                        state = loginViewModel.uiState,
                        onEmailChange = loginViewModel::onEmailChange,
                        onPasswordChange = loginViewModel::onPasswordChange,
                        onLogin = { loginViewModel.iniciarSesion() },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}