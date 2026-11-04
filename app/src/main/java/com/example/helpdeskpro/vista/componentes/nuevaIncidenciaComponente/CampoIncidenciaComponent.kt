package com.example.helpdeskpro.vista.componentes.nuevaIncidenciaComponente

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.helpdeskpro.R
import com.example.helpdeskpro.ui.theme.HelpdeskProTheme

@Composable
fun CampoTituloIncidenciaComponent(
    titulo: String,
    onTituloChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    error: String? = null,
    enabled: Boolean = true
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
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
            isError = error != null,
            supportingText = { if (error != null) Text(error) },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            enabled = enabled
        )
    }
}

@Composable
fun DescripcionIncidenciaComponent(
    descripcion: String,
    onDescripcionChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    error: String? = null,
    enabled: Boolean = true
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = stringResource(R.string.description),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold
        )
        OutlinedTextField(
            value = descripcion,
            onValueChange = onDescripcionChange,
            modifier = Modifier.fillMaxWidth().height(112.dp),
            placeholder = { Text(stringResource(R.string.incident_description_placeholder)) },
            isError = error != null,
            supportingText = { if (error != null) Text(error) },
            minLines = 4,
            maxLines = 6,
            enabled = enabled
        )
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun CampoTituloIncidenciaComponentPreview() {
    HelpdeskProTheme(dynamicColor = false) {
        CampoTituloIncidenciaComponent(
            titulo = "No enciende el ordenador",
            onTituloChange = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun DescripcionIncidenciaComponentPreview() {
    HelpdeskProTheme(dynamicColor = false) {
        DescripcionIncidenciaComponent(
            descripcion = "El ordenador no responde al pulsar el botón de encendido.",
            onDescripcionChange = {}
        )
    }
}
