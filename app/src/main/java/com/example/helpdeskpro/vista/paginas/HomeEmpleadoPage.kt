package com.example.helpdeskpro.vista.paginas

import android.app.Application
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.helpdeskpro.modelo.IncidenciaDTO
import com.example.helpdeskpro.ui.theme.HelpdeskProTheme
import com.example.helpdeskpro.viemodel.vm.HomeEmpleadoViewModel
import com.example.helpdeskpro.vista.componentes.homeEmpleadoComponete.HomeBottomBarComponent
import com.example.helpdeskpro.vista.componentes.homeEmpleadoComponete.HomeTab
import com.example.helpdeskpro.vista.componentes.homeEmpleadoComponete.IncidenciaCardComponent
import com.example.helpdeskpro.vista.componentes.homeEmpleadoComponete.BotonNuevaIncidenciaFlotanteComponent



/**
 * Página principal del empleado con su listado de incidencias.
 * Recibe HomeEmpleadoViewModel y observa su estado.
 *
 * @param homeEmpleadoViewModel ViewModel que proporciona la lista y su estado de carga.
 * @param modifier modificador aplicado a la página.
 * @param selectedTab pestaña seleccionada en la navegación inferior.
 * @param onInicio acción de navegación a Inicio.
 * @param onNueva acción de navegación al formulario de nueva incidencia.
 * @param onPerfil acción de navegación al perfil.
 * @param onIncidenciaClick acción ejecutada al seleccionar una incidencia.
 */
@Composable
fun HomeEmpleadoPage(
    homeEmpleadoViewModel: HomeEmpleadoViewModel,
    modifier: Modifier = Modifier,
    selectedTab: HomeTab = HomeTab.INICIO,
    onInicio: () -> Unit = {},
    onNueva: () -> Unit = {},
    onPerfil: () -> Unit = {},
    onIncidenciaClick: (IncidenciaDTO) -> Unit = {}
) {

    val uiState by homeEmpleadoViewModel.uiState.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color.White,

        bottomBar = {
            HomeBottomBarComponent(
                selectedTab = selectedTab,
                onInicio = onInicio,
                onNueva = onNueva,
                onPerfil = onPerfil
            )
        },

        floatingActionButton = {
            BotonNuevaIncidenciaFlotanteComponent(onClick = onNueva)
        }

    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),

            contentPadding = PaddingValues(
                start = 16.dp,
                top = 20.dp,
                end = 16.dp,
                bottom = 24.dp
            ),

            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item {
                Text(
                    text = stringResource(R.string.my_incidents),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(20.dp))
            }

            when {

                uiState.isLoading -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 48.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }

                uiState.errorMessage != null -> {
                    item {
                        Text(
                            text = uiState.errorMessage.orEmpty(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 48.dp),
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                uiState.incidencias.isEmpty() -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 48.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(R.string.no_incidents),
                                color = Color(0xFF8A8F98),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }

                else -> {
                    items(
                        items = uiState.incidencias,
                        key = { incidencia -> incidencia.id }
                    ) { incidencia ->

                        IncidenciaCardComponent(
                            incidencia = incidencia,
                            onClick = {
                                onIncidenciaClick(incidencia)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun HomeEmpleadoPagePreview() {
    HelpdeskProTheme(dynamicColor = false) {
        val homeEmpleadoViewModel = remember {
            HomeEmpleadoViewModel(Application(), userId = "preview")
        }

        HomeEmpleadoPage(
            homeEmpleadoViewModel = homeEmpleadoViewModel,
            modifier = Modifier,
            selectedTab = HomeTab.INICIO,
            onInicio = {},
            onNueva = {},
            onPerfil = {},
            onIncidenciaClick = {}
        )
    }
}
