package com.example.helpdeskpro.vista.componente.detalleIncidenciaTecnicoComponente

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.helpdeskpro.R
import com.example.helpdeskpro.ui.theme.HelpdeskProTheme



/**
 * Selector desplegable del estado de una incidencia.
 *
 * @param estado estado seleccionado actualmente.
 * @param estados opciones disponibles.
 * @param onEstadoChange callback cuando cambia la selección.
 * @param modifier modificador aplicado al selector.
 * @param enabled indica si el selector está habilitado.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectorEstadoComponent(
    estado: String,
    estados: List<String>,
    onEstadoChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    var expanded by rememberSaveable {
        mutableStateOf(false)
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = stringResource(R.string.change_status),
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 14.sp,
                lineHeight = 20.sp
            ),
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF414A5A)
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                if (enabled) expanded = !expanded
            }
        ) {

            OutlinedTextField(
                value = estado,
                onValueChange = {},
                modifier = Modifier
                    .menuAnchor(
                        type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                        enabled = enabled
                    )
                    .fillMaxWidth()
                    .height(48.dp),
                readOnly = true,
                placeholder = {
                    Text(
                        text = stringResource(R.string.all_statuses),
                        color = Color(0xFF8796AD),
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                },
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = Color(0xFF8796AD),
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                ),
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Filled.ArrowDropDown,
                        contentDescription = null,
                        tint = Color(0xFF8796AD),
                        modifier = Modifier.size(18.dp)
                    )
                },
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFAAB8CC),
                    unfocusedBorderColor = Color(0xFFAAB8CC),
                    focusedTextColor = Color(0xFF8796AD),
                    unfocusedTextColor = Color(0xFF8796AD),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledBorderColor = Color(0xFFAAB8CC),
                    disabledTextColor = Color(0xFF8796AD),
                    disabledContainerColor = Color.White
                ),
                enabled = enabled,
                singleLine = true
            )


            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                }
            ) {
                estados.forEach { opcion ->
                    DropdownMenuItem(
                        text = {
                            Text(opcion)
                        },
                        onClick = {
                            onEstadoChange(opcion)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}



@Preview(showBackground = true, widthDp = 390)
@Composable
private fun SelectorEstadoComponentPreview() {
    HelpdeskProTheme(dynamicColor = false) {
        SelectorEstadoComponent(
            estado = "",
            estados = listOf(
                stringResource(R.string.open),
                stringResource(R.string.in_process),
                stringResource(R.string.resolved)
            ),
            onEstadoChange = {}
        )
    }
}
