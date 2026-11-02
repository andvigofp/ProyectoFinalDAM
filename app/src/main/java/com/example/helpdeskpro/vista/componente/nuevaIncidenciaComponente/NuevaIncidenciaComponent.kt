package com.example.helpdeskpro.vista.componente.nuevaIncidenciaComponente

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.helpdeskpro.R
import com.example.helpdeskpro.ui.theme.HelpdeskProTheme

private val NuevaIncidenciaButtonColor = Color(0xFF8998AD)

/**
 * Formulario reutilizable para registrar una incidencia.
 *
 * @param titulo título actual de la incidencia.
 * @param descripcion descripción actual del problema.
 * @param categoria categoría seleccionada.
 * @param prioridad prioridad seleccionada.
 * @param onTituloChange callback para cambiar el título.
 * @param onDescripcionChange callback para cambiar la descripción.
 * @param onCategoriaChange callback para cambiar la categoría.
 * @param onPrioridadChange callback para cambiar la prioridad.
 * @param onCrear callback para enviar el formulario.
 * @param modifier modificador aplicado al formulario.
 * @param categorias opciones disponibles de categoría.
 * @param prioridades opciones disponibles de prioridad.
 * @param tituloError error de validación del título.
 * @param descripcionError error de validación de la descripción.
 * @param categoriaError error de validación de la categoría.
 * @param prioridadError error de validación de la prioridad.
 * @param errorMessage error general del formulario.
 * @param isLoading indica si hay una operación en curso.
 */
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
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = stringResource(R.string.incident_title),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
            OutlinedTextField(
                value = titulo,
                onValueChange = onTituloChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(stringResource(R.string.incident_title_placeholder)) },
                singleLine = true,
                isError = tituloError != null,
                supportingText = {
                    if (tituloError != null) Text(tituloError)
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                enabled = !isLoading
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = stringResource(R.string.description),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
            OutlinedTextField(
                value = descripcion,
                onValueChange = onDescripcionChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(112.dp),
                placeholder = { Text(stringResource(R.string.incident_description_placeholder)) },
                isError = descripcionError != null,
                supportingText = {
                    if (descripcionError != null) Text(descripcionError)
                },
                minLines = 4,
                maxLines = 6,
                enabled = !isLoading
            )
        }

        SelectorNuevaIncidencia(
            label = stringResource(R.string.category),
            value = categoria,
            placeholder = stringResource(R.string.category_placeholder),
            options = categorias,
            onOptionSelected = onCategoriaChange,
            error = categoriaError,
            enabled = !isLoading
        )

        SelectorNuevaIncidencia(
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
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            enabled = !isLoading,
            colors = ButtonDefaults.buttonColors(
                containerColor = NuevaIncidenciaButtonColor
            )
        ) {
            Text(
                text = stringResource(if (isLoading) R.string.creating else R.string.create_incident),
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

/**
 * Selector desplegable reutilizable para el formulario de incidencias.
 *
 * @param label etiqueta del selector.
 * @param value valor seleccionado.
 * @param placeholder texto de ayuda cuando no hay selección.
 * @param options opciones disponibles.
 * @param onOptionSelected callback al elegir una opción.
 * @param modifier modificador aplicado al selector.
 * @param error mensaje de validación que se muestra, si existe.
 * @param enabled indica si el selector está habilitado.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SelectorNuevaIncidencia(
    label: String,
    value: String,
    placeholder: String,
    options: List<String>,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    error: String? = null,
    enabled: Boolean = true
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold
        )
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                if (enabled) expanded = !expanded
            }
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = {},
                modifier = Modifier
                    .menuAnchor(
                        type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                        enabled = enabled
                    )
                    .fillMaxWidth(),
                readOnly = true,
                placeholder = { Text(placeholder) },
                singleLine = true,
                isError = error != null,
                supportingText = {
                    if (error != null) Text(error)
                },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                enabled = enabled
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onOptionSelected(option)
                            expanded = false
                        }
                    )
                }
            }
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