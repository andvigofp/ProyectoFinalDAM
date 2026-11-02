package com.example.helpdeskpro.vista.pagina

import android.app.Application
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.helpdeskpro.R
import com.example.helpdeskpro.modelo.PerfilDTO
import com.example.helpdeskpro.ui.theme.HelpdeskProTheme
import com.example.helpdeskpro.viemodel.vm.PerfilViewModel
import com.example.helpdeskpro.vista.componente.homeEmpleadoComponete.HomeBottomBar
import com.example.helpdeskpro.vista.componente.homeEmpleadoComponete.HomeTab
import com.example.helpdeskpro.vista.componente.perfilComponente.PerfilComponent

/**
 * Pantalla de consulta del perfil del usuario autenticado.
 *
 * @param perfilViewModel ViewModel que carga y mantiene los datos del perfil.
 * @param modifier modificador aplicado a la página.
 * @param onInicio acción de navegación a Inicio.
 * @param onNueva acción de navegación al formulario de nueva incidencia.
 * @param onEditarPerfil acción para iniciar la edición del perfil.
 * @param onCerrarSesion acción para cerrar la sesión.
 */
@Composable
fun PerfilPage(
    perfilViewModel: PerfilViewModel,
    modifier: Modifier = Modifier,
    onInicio: () -> Unit = {},
    onNueva: () -> Unit = {},
    onEditarPerfil: () -> Unit = {},
    onCerrarSesion: () -> Unit = {}
) {

    val uiState by perfilViewModel.uiState.collectAsState()

    LaunchedEffect(perfilViewModel.userId) {
        perfilViewModel.cargarPerfil {
            PerfilDTO(
                id = perfilViewModel.userId.toLongOrNull() ?: 1L,
                nombre = "Ana",
                apellidos = "Pérez",
                email = "ana@empresa.com",
                telefono = "600 123 456",
                departamento = "Ventas",
                miembroDesde = "01/09/2026",
                rol = "EMPLEADO"
            )
        }
    }

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
                text = stringResource(R.string.my_profile),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(20.dp))

            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                uiState.errorMessage != null -> {
                    Text(
                        text = uiState.errorMessage!!,
                        modifier = Modifier.padding(top = 48.dp),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                uiState.perfil == null -> {
                    Text(
                        text = stringResource(R.string.no_profile_data),
                        modifier = Modifier.padding(top = 48.dp),
                        color = Color(0xFF8A8F98),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                else -> {
                    PerfilComponent(
                        perfil = uiState.perfil!!,
                        onEditarPerfil = onEditarPerfil,
                        onCerrarSesion = onCerrarSesion,
                        isLoading = uiState.isLoading
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
        val perfilViewModel = remember {
            PerfilViewModel(Application(), userId = "preview")
        }

        PerfilPage(
            perfilViewModel = perfilViewModel,
            modifier = Modifier,
            onInicio = {},
            onNueva = {},
            onEditarPerfil = {},
            onCerrarSesion = {}
        )
    }
}