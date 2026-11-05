package com.example.helpdeskpro.vista.paginas

import androidx.compose.material3.MaterialTheme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.helpdeskpro.R
import com.example.helpdeskpro.modelo.AdminUsuarioDTO
import com.example.helpdeskpro.ui.theme.HelpdeskProTheme
import com.example.helpdeskpro.viemodel.ustate.AdminUsuariosUiState
import com.example.helpdeskpro.viemodel.vm.AdminUsuariosViewModel
import com.example.helpdeskpro.vista.componentes.adminUsuariosComponente.BotonNuevoUsuarioComponent
import com.example.helpdeskpro.vista.componentes.adminUsuariosComponente.UsuarioAdminCardComponent
import com.example.helpdeskpro.vista.componentes.perfilComponente.BotonVolverPerfilComponent

@Composable
fun AdminUsuariosPage(
    adminUsuariosViewModel: AdminUsuariosViewModel,
    cargarUsuarios: suspend () -> List<AdminUsuarioDTO>,
    modifier: Modifier = Modifier,
    onVolver: () -> Unit = {},
    onNuevoUsuario: () -> Unit = {},
    onEditarUsuario: (AdminUsuarioDTO) -> Unit = {}
) {
    val uiState by adminUsuariosViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        adminUsuariosViewModel.cargarUsuarios(cargarUsuarios)
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surface,
        floatingActionButton = { BotonNuevoUsuarioComponent(onClick = onNuevoUsuario) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(start = 16.dp, top = 14.dp, end = 16.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                androidx.compose.foundation.layout.Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BotonVolverPerfilComponent(onClick = onVolver)
                    Text(
                        text = stringResource(R.string.users),
                        modifier = Modifier.padding(start = 8.dp),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = androidx.compose.material3.MaterialTheme.typography.headlineSmall
                    )
                }
                androidx.compose.foundation.layout.Spacer(Modifier.height(86.dp))
            }

            when {
                uiState.isLoading -> item {
                    Box(Modifier.fillMaxWidth().padding(top = 36.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                uiState.errorMessage != null -> item {
                    Text(
                        text = uiState.errorMessage.orEmpty(),
                        modifier = Modifier.fillMaxWidth().padding(top = 36.dp),
                        color = androidx.compose.material3.MaterialTheme.colorScheme.error
                    )
                }
                uiState.usuarios.isEmpty() -> item {
                    Text(
                        text = stringResource(R.string.no_admin_users),
                        modifier = Modifier.fillMaxWidth().padding(top = 36.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                else -> items(uiState.usuarios, key = { it.id }) { usuario ->
                    UsuarioAdminCardComponent(
                        usuario = usuario,
                        onEditar = { onEditarUsuario(usuario) }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun AdminUsuariosPagePreview() {
    HelpdeskProTheme(dynamicColor = false) {
        AdminUsuariosPageContentPreview()
    }
}

@Composable
private fun AdminUsuariosPageContentPreview() {
    androidx.compose.material3.Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        floatingActionButton = { BotonNuevoUsuarioComponent(onClick = {}) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(start = 16.dp, top = 14.dp, end = 16.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                androidx.compose.foundation.layout.Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BotonVolverPerfilComponent(onClick = {})
                    Text(
                        text = stringResource(R.string.users),
                        modifier = Modifier.padding(start = 8.dp),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = androidx.compose.material3.MaterialTheme.typography.headlineSmall
                    )
                }
                androidx.compose.foundation.layout.Spacer(Modifier.height(86.dp))
            }
            items(
                listOf(
                    AdminUsuarioDTO(1, "Ana", "Pérez", "", "", "EMPLEADO", "Ventas", true),
                    AdminUsuarioDTO(2, "Luis", "Gómez", "", "", "TECNICO", "Sistemas", true),
                    AdminUsuarioDTO(3, "Marta", "Díaz", "", "", "ADMINISTRADOR", "Dirección", true),
                    AdminUsuarioDTO(4, "Pedro", "Sanz", "", "", "EMPLEADO", "RRHH", false)
                )
            ) {
                UsuarioAdminCardComponent(usuario = it, onEditar = {})
            }
        }
    }
}
