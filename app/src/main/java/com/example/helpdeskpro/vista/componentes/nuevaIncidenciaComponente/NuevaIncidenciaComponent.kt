package com.example.helpdeskpro.vista.componentes.nuevaIncidenciaComponente

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.helpdeskpro.R
import com.example.helpdeskpro.ui.theme.HelpdeskProTheme

/** Formulario reutilizable para registrar una incidencia. */
@Composable
fun NuevaIncidenciaComponent(
    titulo: String,
    descripcion: String,
    categoria: String,
    prioridad: String,
    onTituloChange: (String) -> Unit,
    onDescripcionChange: (String) -> Unit,
    onCategoriaChange: (String) -> Unit,
    onPrioridadChange: (String) -> Unit,
    onCrear: () -> Unit,
    categorias: List<String>,
    prioridades: List<String>,
    modifier: Modifier = Modifier,
    tituloError: String? = null,
    descripcionError: String? = null,
    categoriaError: String? = null,
    prioridadError: String? = null,
    errorMessage: String? = null,
    isLoading: Boolean = false
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CampoTituloIncidenciaComponent(
            titulo = titulo,
            onTituloChange = onTituloChange,
            error = tituloError,
            enabled = !isLoading
        )
        DescripcionIncidenciaComponent(
            descripcion = descripcion,
            onDescripcionChange = onDescripcionChange,
            error = descripcionError,
            enabled = !isLoading
        )
        SelectorIncidenciaComponent(
            label = stringResource(R.string.category),
            value = categoria,
            placeholder = stringResource(R.string.category_placeholder),
            options = categorias,
            onOptionSelected = onCategoriaChange,
            error = categoriaError,
            enabled = !isLoading
        )
        SelectorIncidenciaComponent(
            label = stringResource(R.string.priority),
            value = prioridad,
            placeholder = stringResource(R.string.priority_placeholder),
            options = prioridades,
            onOptionSelected = onPrioridadChange,
            error = prioridadError,
            enabled = !isLoading
        )

        if (errorMessage != null) {
            Text(
                text = errorMessage,
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        BotonCrearIncidenciaComponent(
            onClick = onCrear,
            modifier = Modifier.fillMaxWidth(),
            isLoading = isLoading
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 700)
@Composable
private fun NuevaIncidenciaComponentPreview() {
    HelpdeskProTheme(dynamicColor = false) {
        NuevaIncidenciaComponent(
            titulo = "No enciende el ordenador",
            descripcion = "El ordenador no responde al pulsar el botón de encendido.",
            categoria = "Hardware",
            prioridad = "Alta",
            onTituloChange = {},
            onDescripcionChange = {},
            onCategoriaChange = {},
            onPrioridadChange = {},
            onCrear = {},
            categorias = listOf(
                stringResource(R.string.hardware),
                stringResource(R.string.software),
                stringResource(R.string.network)
            ),
            prioridades = listOf(
                stringResource(R.string.low),
                stringResource(R.string.medium),
                stringResource(R.string.high)
            )
        )
    }
}
