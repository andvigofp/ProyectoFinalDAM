package com.example.helpdeskpro.vista.componentes.nuevaIncidenciaComponente

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.helpdeskpro.R
import com.example.helpdeskpro.ui.theme.HelpdeskProTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectorIncidenciaComponent(
    value: String,
    placeholder: String,
    options: List<String>,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String,
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
            onExpandedChange = { if (enabled) expanded = !expanded }
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = {},
                modifier = Modifier.menuAnchor(
                    type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                    enabled = enabled
                ).fillMaxWidth(),
                readOnly = true,
                placeholder = { Text(placeholder) },
                singleLine = true,
                isError = error != null,
                supportingText = { if (error != null) Text(error) },
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

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun SelectorIncidenciaComponentPreview() {
    HelpdeskProTheme(dynamicColor = false) {
        SelectorIncidenciaComponent(
            value = stringResource(R.string.hardware),
            placeholder = stringResource(R.string.select_category),
            options = listOf(
                stringResource(R.string.hardware),
                stringResource(R.string.software),
                stringResource(R.string.network),
                stringResource(R.string.email_category)
            ),
            onOptionSelected = {},
            label = stringResource(R.string.category)
        )
    }
}
