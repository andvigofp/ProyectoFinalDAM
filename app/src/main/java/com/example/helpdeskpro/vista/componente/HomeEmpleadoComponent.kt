package com.example.helpdeskpro.vista.componente

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.helpdeskpro.R
import com.example.helpdeskpro.modelo.IncidenciaDTO
import com.example.helpdeskpro.ui.theme.HelpdeskProTheme

private val HomeSelectedColor = Color(0xFF637A99)
private val HomeUnselectedColor = Color(0xFFD8DDE4)
private val HomeSecondaryTextColor = Color(0xFF8A8F98)

/** Pestañas disponibles en la barra inferior del empleado. */
enum class HomeTab {
    INICIO,
    NUEVA,
    PERFIL
}

/** Tarjeta reutilizable para mostrar el resumen de una incidencia. */
@Composable
fun IncidenciaCard(
    incidencia: IncidenciaDTO,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFD4D4D8))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = incidencia.titulo,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.width(8.dp))
                EstadoIncidenciaBadge(estado = incidencia.estado)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(
                    R.string.incident_meta,
                    incidencia.categoria,
                    incidencia.prioridad,
                    incidencia.fecha
                ),
                color = HomeSecondaryTextColor,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun EstadoIncidenciaBadge(estado: String) {
    val normalizedState = estado.uppercase().replace(" ", "_")
    val backgroundColor = when (normalizedState) {
        "ABIERTA" -> Color(0xFFE5B85C)
        "EN_PROGRESO" -> Color(0xFF64748B)
        "RESUELTA" -> Color(0xFF68A380)
        else -> Color(0xFF8998AD)
    }

    Surface(
        shape = RoundedCornerShape(6.dp),
        color = backgroundColor,
        contentColor = Color.White
    ) {
        Text(
            text = estado.replace('_', ' ').uppercase(),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall
        )
    }
}

/** Barra inferior con las tres secciones principales del empleado. */
@Composable
fun HomeBottomBar(
    selectedTab: HomeTab,
    onInicio: () -> Unit,
    onNueva: () -> Unit,
    onPerfil: () -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier.fillMaxWidth(),
        containerColor = Color(0xFFF3F4F6)
    ) {
        HomeBottomBarItem(
            label = stringResource(R.string.home),
            selected = selectedTab == HomeTab.INICIO,
            onClick = onInicio
        )
        HomeBottomBarItem(
            label = stringResource(R.string.nav_new),
            selected = selectedTab == HomeTab.NUEVA,
            onClick = onNueva
        )
        HomeBottomBarItem(
            label = stringResource(R.string.profile),
            selected = selectedTab == HomeTab.PERFIL,
            onClick = onPerfil
        )
    }
}

@Composable
private fun androidx.compose.foundation.layout.RowScope.HomeBottomBarItem(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        icon = {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(
                        color = if (selected) HomeSelectedColor else HomeUnselectedColor,
                        shape = CircleShape
                    )
            )
        },
        label = { Text(label) },
        alwaysShowLabel = true,
        colors = NavigationBarItemDefaults.colors(
            selectedTextColor = MaterialTheme.colorScheme.onSurface,
            unselectedTextColor = HomeSecondaryTextColor,
            indicatorColor = Color.Transparent
        )
    )
}

@Preview(showBackground = true, widthDp = 390)
@Composable
private fun IncidenciaCardPreview() {
    HelpdeskProTheme(dynamicColor = false) {
        IncidenciaCard(
            incidencia = IncidenciaDTO(
                id = 1,
                titulo = "No enciende el ordenador",
                categoria = "Hardware",
                prioridad = "Alta",
                fecha = "15/10/2026",
                estado = "ABIERTA"
            ),
            onClick = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 390)
@Composable
private fun HomeBottomBarPreview() {
    HelpdeskProTheme(dynamicColor = false) {
        HomeBottomBar(
            selectedTab = HomeTab.INICIO,
            onInicio = {},
            onNueva = {},
            onPerfil = {}
        )
    }
}
