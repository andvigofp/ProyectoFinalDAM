package com.example.helpdeskpro.vista.componente

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

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
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = "Título",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
            OutlinedTextField(
                value = titulo,
                onValueChange = onTituloChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Resumen del problema…") },
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
                text = "Descripción",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
            OutlinedTextField(
                value = descripcion,
                onValueChange = onDescripcionChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(112.dp),
                placeholder = { Text("Describe el problema…") },
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
            label = "Categoría",
            value = categoria,
            placeholder = "Hardware / Software / Red …",
            options = categorias,
            onOptionSelected = onCategoriaChange,
            error = categoriaError,
            enabled = !isLoading
        )

        SelectorNuevaIncidencia(
            label = "Prioridad",
            value = prioridad,
            placeholder = "Baja / Media / Alta",
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
                text = if (isLoading) "Creando…" else "Crear incidencia",
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

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
                    .menuAnchor()
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
