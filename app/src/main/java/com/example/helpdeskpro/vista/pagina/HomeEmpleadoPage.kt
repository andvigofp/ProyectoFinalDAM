package com.example.helpdeskpro.vista.pagina

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
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
import com.example.helpdeskpro.modelo.IncidenciaDTO
import com.example.helpdeskpro.ui.theme.HelpdeskProTheme
import com.example.helpdeskpro.vista.componente.HomeBottomBar
import com.example.helpdeskpro.vista.componente.HomeTab
import com.example.helpdeskpro.vista.componente.IncidenciaCard

/**
 * Página principal del empleado con su listado de incidencias.
 */
@Composable
fun HomeEmpleadoPage(
    incidencias: List<IncidenciaDTO> = emptyList(),
    selectedTab: HomeTab = HomeTab.INICIO,
    onInicio: () -> Unit = {},
    onNueva: () -> Unit = {},
    onPerfil: () -> Unit = {},
    onIncidenciaClick: (IncidenciaDTO) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color.White,
        bottomBar = {
            HomeBottomBar(
                selectedTab = selectedTab,
                onInicio = onInicio,
                onNueva = onNueva,
                onPerfil = onPerfil
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNueva,
                shape = CircleShape,
                containerColor = Color(0xFF637A99),
                contentColor = Color.White
            ) {
                Text(
                    text = "+",
                    style = MaterialTheme.typography.headlineMedium
                )
            }
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
                    text = "Mis incidencias",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(20.dp))
            }

            if (incidencias.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No tienes incidencias todavía",
                            color = Color(0xFF8A8F98),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            } else {
                items(
                    items = incidencias,
                    key = { incidencia -> incidencia.id }
                ) { incidencia ->
                    IncidenciaCard(
                        incidencia = incidencia,
                        onClick = { onIncidenciaClick(incidencia) }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun HomeEmpleadoPagePreview() {
    HelpdeskProTheme(dynamicColor = false) {
        HomeEmpleadoPage(
            incidencias = listOf(
                IncidenciaDTO(
                    id = 1,
                    titulo = "No enciende el ordenador",
                    categoria = "Hardware",
                    prioridad = "Alta",
                    fecha = "15/10/2026",
                    estado = "ABIERTA"
                ),
                IncidenciaDTO(
                    id = 2,
                    titulo = "No puedo entrar al correo",
                    categoria = "Software",
                    prioridad = "Media",
                    fecha = "10/10/2026",
                    estado = "EN_PROGRESO"
                ),
                IncidenciaDTO(
                    id = 3,
                    titulo = "Instalar VPN en portátil",
                    categoria = "Red",
                    prioridad = "Baja",
                    fecha = "02/10/2026",
                    estado = "RESUELTA"
                )
            )
        )
    }
}
