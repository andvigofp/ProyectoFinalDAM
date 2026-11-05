package com.example.helpdeskpro.vista.componentes.homeEmpleadoComponete

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.helpdeskpro.R
import com.example.helpdeskpro.ui.theme.HelpdeskProTheme


@Composable
fun HomeBottomBarComponent(
    selectedTab: HomeTab,
    onInicio: () -> Unit,
    onNueva: () -> Unit,
    onPerfil: () -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(modifier = modifier.fillMaxWidth(), containerColor = MaterialTheme.colorScheme.surfaceVariant) {
        HomeBottomBarItemComponent(
            label = stringResource(R.string.home),
            selected = selectedTab == HomeTab.INICIO,
            onClick = onInicio
        )
        HomeBottomBarItemComponent(
            label = stringResource(R.string.nav_new),
            selected = selectedTab == HomeTab.NUEVA,
            onClick = onNueva
        )
        HomeBottomBarItemComponent(
            label = stringResource(R.string.profile),
            selected = selectedTab == HomeTab.PERFIL,
            onClick = onPerfil
        )
    }
}

@Composable
private fun RowScope.HomeBottomBarItemComponent(
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
                        color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                        shape = CircleShape
                    )
            )
        },
        label = { Text(label) },
        alwaysShowLabel = true,
        colors = NavigationBarItemDefaults.colors(
            selectedTextColor = MaterialTheme.colorScheme.onSurface,
            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
            indicatorColor = Color.Transparent
        )
    )
}

@Preview(showBackground = true, widthDp = 390)
@Composable
private fun HomeBottomBarComponentPreview() {
    HelpdeskProTheme(dynamicColor = false) {
        HomeBottomBarComponent(
            selectedTab = HomeTab.INICIO,
            onInicio = {},
            onNueva = {},
            onPerfil = {}
        )
    }
}
