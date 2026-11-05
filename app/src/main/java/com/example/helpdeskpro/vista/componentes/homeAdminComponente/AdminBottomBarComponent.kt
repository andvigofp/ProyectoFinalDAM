package com.example.helpdeskpro.vista.componentes.homeAdminComponente

import androidx.compose.material3.MaterialTheme

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.helpdeskpro.R

enum class AdminTab { PANEL, INCIDENCIAS, USUARIOS, PERFIL }

@Composable
fun AdminBottomBarComponent(
    selectedTab: AdminTab,
    onPanel: () -> Unit,
    onIncidencias: () -> Unit,
    onUsuarios: () -> Unit,
    onPerfil: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(66.dp)
            .background(MaterialTheme.colorScheme.secondaryContainer),
        verticalAlignment = Alignment.CenterVertically
    ) {
        listOf(
            Triple(AdminTab.PANEL, stringResource(R.string.admin_nav_panel), onPanel),
            Triple(AdminTab.INCIDENCIAS, stringResource(R.string.incidents), onIncidencias),
            Triple(AdminTab.USUARIOS, stringResource(R.string.users), onUsuarios),
            Triple(AdminTab.PERFIL, stringResource(R.string.profile), onPerfil)
        ).forEach { (tab, label, onClick) ->
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable(onClick = onClick),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            if (selectedTab == tab) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                            CircleShape
                        )
                )
                Text(
                    text = label,
                    color = if (selectedTab == tab) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
