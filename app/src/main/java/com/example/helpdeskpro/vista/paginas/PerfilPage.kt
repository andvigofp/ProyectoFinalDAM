package com.example.helpdeskpro.vista.paginas

import android.app.Application
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.helpdeskpro.R
import com.example.helpdeskpro.modelo.PerfilDTO
import com.example.helpdeskpro.ui.theme.HelpdeskProTheme
import com.example.helpdeskpro.viemodel.vm.PerfilViewModel
import com.example.helpdeskpro.vista.componentes.detalleIncidenciaTecnicoAdminComponente.NavegacionInferiorDetalleComponent
import com.example.helpdeskpro.vista.componentes.homeAdminComponente.AdminBottomBarComponent
import com.example.helpdeskpro.vista.componentes.homeAdminComponente.AdminTab
import com.example.helpdeskpro.vista.componentes.homeEmpleadoComponete.HomeBottomBarComponent
import com.example.helpdeskpro.vista.componentes.homeEmpleadoComponete.HomeTab
import com.example.helpdeskpro.vista.componentes.perfilComponente.AccionesPerfilComponent
import com.example.helpdeskpro.vista.componentes.perfilComponente.AvatarPerfilComponent
import com.example.helpdeskpro.vista.componentes.perfilComponente.CamposPerfilComponent
import com.example.helpdeskpro.vista.componentes.toolbarComponenete.ToolbarComponent

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
    modoTecnico: Boolean = false,
    modoAdmin: Boolean = false,
    onInicio: () -> Unit = {},
    onNueva: () -> Unit = {},
    onUsuarios: () -> Unit = {},
    onVolver: () -> Unit = onInicio,
    onEditarPerfil: (PerfilDTO) -> Unit = {},
    onCerrarSesion: () -> Unit = {}
) {

    val uiState by perfilViewModel.uiState.collectAsState()

    LaunchedEffect(perfilViewModel.userId) {
        perfilViewModel.cargarPerfil()
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surface,
        bottomBar = {
            if (modoAdmin) {
                AdminBottomBarComponent(
                    selectedTab = AdminTab.PERFIL,
                    onPanel = onInicio,
                    onIncidencias = onNueva,
                    onUsuarios = onUsuarios,
                    onPerfil = {}
                )
            } else if (modoTecnico) {
                NavegacionInferiorDetalleComponent(
                    onIncidencias = onInicio,
                    onPerfil = {},
                    perfilSeleccionado = true
                )
            } else {
                HomeBottomBarComponent(
                    selectedTab = HomeTab.PERFIL,
                    onInicio = onInicio,
                    onNueva = onNueva,
                    onPerfil = {}
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 20.dp)
        ) {
            ToolbarComponent(
                titulo = stringResource(R.string.my_profile),
                onVolver = onVolver
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
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                else -> {
                    val perfil = uiState.perfil!!

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AvatarPerfilComponent(
                            nombre = perfil.nombre,
                            apellidos = perfil.apellidos
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = stringResource(
                                R.string.full_name,
                                perfil.nombre,
                                perfil.apellidos
                            ).trim(),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ) {
                            Text(
                                text = perfil.rol.replace('_', ' ').uppercase(),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(28.dp))
                    CamposPerfilComponent(perfil = perfil)
                    Spacer(modifier = Modifier.height(32.dp))
                    AccionesPerfilComponent(
                        onEditarPerfil = { onEditarPerfil(perfil) },
                        onCerrarSesion = onCerrarSesion
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