package com.example.helpdeskpro.vista.paginas

import android.app.Application
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.helpdeskpro.R
import com.example.helpdeskpro.modelo.HomeAdminDTO
import com.example.helpdeskpro.ui.theme.HelpdeskProTheme
import com.example.helpdeskpro.viemodel.ustate.HomeAdminUiState
import com.example.helpdeskpro.viemodel.vm.HomeAdminViewModel
import com.example.helpdeskpro.viemodel.vm.HomeAdminViewModelFactory
import com.example.helpdeskpro.vista.componentes.homeAdminComponente.AccionAdminComponent
import com.example.helpdeskpro.vista.componentes.homeAdminComponente.AdminBottomBarComponent
import com.example.helpdeskpro.vista.componentes.homeAdminComponente.AdminTab
import com.example.helpdeskpro.vista.componentes.homeAdminComponente.ResumenIncidenciasAdminComponent

@Composable
fun HomeAdminPage(
    homeAdminViewModel: HomeAdminViewModel,
    cargarResumen: suspend () -> HomeAdminDTO,
    modifier: Modifier = Modifier,
    onIncidencias: () -> Unit = {},
    onUsuarios: () -> Unit = {},
    onPerfil: () -> Unit = {}
) {
    val uiState by homeAdminViewModel.uiState.collectAsState()

    LaunchedEffect(homeAdminViewModel.userId) {
        homeAdminViewModel.cargarResumen(cargarResumen)
    }

    HomeAdminPageContent(
        uiState = uiState,
        onIncidencias = onIncidencias,
        onUsuarios = onUsuarios,
        onPerfil = onPerfil,
        modifier = modifier
    )
}

@Composable
private fun HomeAdminPageContent(
    uiState: HomeAdminUiState,
    onIncidencias: () -> Unit,
    onUsuarios: () -> Unit,
    onPerfil: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surface,
        bottomBar = {
            AdminBottomBarComponent(
                selectedTab = AdminTab.PANEL,
                onPanel = {},
                onIncidencias = onIncidencias,
                onUsuarios = onUsuarios,
                onPerfil = onPerfil
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 22.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = stringResource(R.string.admin_panel),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(48.dp))

            when {
                uiState.isLoading -> CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                uiState.errorMessage != null -> Text(
                    text = uiState.errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge
                )
                uiState.resumen != null -> ResumenIncidenciasAdminComponent(
                    resumen = uiState.resumen,
                    etiquetas = listOf(
                        stringResource(R.string.filter_open),
                        stringResource(R.string.filter_in_progress),
                        stringResource(R.string.filter_resolved)
                    )
                )
            }

            Spacer(modifier = Modifier.height(30.dp))
            AccionAdminComponent(
                texto = stringResource(R.string.manage_incidents),
                onClick = onIncidencias
            )
            Spacer(modifier = Modifier.height(16.dp))
            AccionAdminComponent(
                texto = stringResource(R.string.manage_users),
                onClick = onUsuarios
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun HomeAdminPagePreview() {
    HelpdeskProTheme(dynamicColor = false) {
        Surface(color = MaterialTheme.colorScheme.surface) {
            HomeAdminPageContent(
                uiState = HomeAdminUiState(resumen = HomeAdminDTO(12, 5, 48)),
                onIncidencias = {},
                onUsuarios = {},
                onPerfil = {}
            )
        }
    }
}
