package com.example.helpdeskpro.vista.pagina

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.helpdeskpro.R
import com.example.helpdeskpro.ui.theme.HelpdeskProTheme
import com.example.helpdeskpro.vista.componente.NuevaIncidenciaComponent

/** Pantalla para que el empleado registre una nueva incidencia. */
@Composable
fun NuevaIncidenciaPage(
    modifier: Modifier = Modifier,
    titulo: String = "",
    descripcion: String = "",
    categoria: String = "",
    prioridad: String = "",
    onTituloChange: (String) -> Unit = {},
    onDescripcionChange: (String) -> Unit = {},
    onCategoriaChange: (String) -> Unit = {},
    onPrioridadChange: (String) -> Unit = {},
    onCrear: () -> Unit = {},
    onVolver: () -> Unit = {},
    tituloError: String? = null,
    descripcionError: String? = null,
    categoriaError: String? = null,
    prioridadError: String? = null,
    errorMessage: String? = null,
    isLoading: Boolean = false
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onVolver,
                enabled = !isLoading
            ) {
                Text(
                    text = "‹",
                    style = MaterialTheme.typography.headlineMedium
                )
            }
            Text(
                text = stringResource(R.string.new_incident),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        NuevaIncidenciaComponent(
            titulo = titulo,
            descripcion = descripcion,
            categoria = categoria,
            prioridad = prioridad,
            onTituloChange = onTituloChange,
            onDescripcionChange = onDescripcionChange,
            onCategoriaChange = onCategoriaChange,
            onPrioridadChange = onPrioridadChange,
            onCrear = onCrear,
            modifier = Modifier.fillMaxWidth(),
            tituloError = tituloError,
            descripcionError = descripcionError,
            categoriaError = categoriaError,
            prioridadError = prioridadError,
            errorMessage = errorMessage,
            isLoading = isLoading
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun NuevaIncidenciaPagePreview() {
    HelpdeskProTheme(dynamicColor = false) {
        NuevaIncidenciaPage()
    }
}
