package com.example.helpdeskpro.vista.componentes.crearCuentaComponente

import androidx.compose.material3.MaterialTheme

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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

@Composable
fun BotonGuardarCuentaComponent(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth().height(52.dp),
        enabled = !isLoading,
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Text(
            text = stringResource(if (isLoading) R.string.saving else R.string.save),
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun BotonGuardarCuentaComponentPreview() {
    HelpdeskProTheme(dynamicColor = false) {
        BotonGuardarCuentaComponent(onClick = {})
    }
}
