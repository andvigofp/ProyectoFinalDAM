package com.example.helpdeskpro.vista.componentes.editarPerfilComponente

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.helpdeskpro.R

@Composable
fun BotonGuardarEditarPerfilComponent(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean,
    isLoading: Boolean = false
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        enabled = enabled && !isLoading,
        shape = RoundedCornerShape(32.dp),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.padding(vertical = 2.dp),
                color = MaterialTheme.colorScheme.onPrimary,
                strokeWidth = 2.dp
            )
        } else {
            Text(
                text = stringResource(R.string.save),
                modifier = Modifier.padding(vertical = 8.dp),
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
