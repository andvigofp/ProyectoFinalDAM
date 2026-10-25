package com.example.helpdeskpro.vista.pagina

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.helpdeskpro.modelo.PerfilDTO
import com.example.helpdeskpro.ui.theme.HelpdeskProTheme
import com.example.helpdeskpro.vista.componente.HomeBottomBar
import com.example.helpdeskpro.vista.componente.HomeTab
import com.example.helpdeskpro.vista.componente.PerfilComponent

/** Pantalla de consulta del perfil del usuario autenticado. */
@Composable
fun PerfilPage(
    perfil: PerfilDTO? = null,
    onInicio: () -> Unit = {},
    onNueva: () -> Unit = {},
    onEditarPerfil: () -> Unit = {},
    onCerrarSesion: () -> Unit = {},
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    errorMessage: String? = null
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color.White,
        bottomBar = {
            HomeBottomBar(
                selectedTab = HomeTab.PERFIL,
                onInicio = onInicio,
                onNueva = onNueva,
                onPerfil = {}
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 20.dp)
        ) {
            Text(
                text = "Mi perfil",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(20.dp))

            when {
                isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                errorMessage != null -> {
                    Text(
                        text = errorMessage,
                        modifier = Modifier.padding(top = 48.dp),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                perfil == null -> {
                    Text(
                        text = "No hay datos de perfil disponibles",
                        modifier = Modifier.padding(top = 48.dp),
                        color = Color(0xFF8A8F98),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                else -> {
                    PerfilComponent(
                        perfil = perfil,
                        onEditarPerfil = onEditarPerfil,
                        onCerrarSesion = onCerrarSesion,
                        isLoading = isLoading
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun PerfilPagePreview() {
    HelpdeskProTheme(dynamicColor = false) {
        PerfilPage(
            perfil = PerfilDTO(
                id = 1,
                nombre = "Ana",
                apellidos = "Pérez",
                email = "ana@empresa.com",
                telefono = "600 123 456",
                departamento = "Ventas",
                miembroDesde = "01/09/2026",
                rol = "EMPLEADO"
            )
        )
    }
}
