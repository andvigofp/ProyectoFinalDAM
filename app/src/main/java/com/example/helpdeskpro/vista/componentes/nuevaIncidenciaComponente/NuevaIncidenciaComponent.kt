package com.example.helpdeskpro.vista.componentes.nuevaIncidenciaComponente

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.helpdeskpro.R
import com.example.helpdeskpro.ui.theme.HelpdeskProTheme

private val NuevaIncidenciaButtonColor = Color(0xFF8998AD)

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
    modifier: Modifier = Modifier,
    categorias: List<String> = listOf("Hardware", "Software", "Red"),
    prioridades: List<String> = listOf("Baja", "Media", "Alta"),
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

        Button(
            onClick = onCrear,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            enabled = !isLoading,
            colors = ButtonDefaults.buttonColors(containerColor = NuevaIncidenciaButtonColor)
        ) {
            Text(
                text = stringResource(if (isLoading) R.string.creating else R.string.create_incident),
                fontWeight = FontWeight.SemiBold
            )
        }
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
            onCrear = {}
        )
    }
}
