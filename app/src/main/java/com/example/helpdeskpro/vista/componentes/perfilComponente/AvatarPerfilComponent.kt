package com.example.helpdeskpro.vista.componentes.perfilComponente

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.helpdeskpro.ui.theme.HelpdeskProTheme

@Composable
fun AvatarPerfilComponent(
    nombre: String,
    apellidos: String,
    modifier: Modifier = Modifier
) {
    val iniciales = listOf(nombre, apellidos)
        .mapNotNull { it.trim().firstOrNull() }
        .take(2)
        .joinToString("")
        .uppercase()
        .ifBlank { "?" }

    Surface(
        modifier = modifier.size(96.dp),
        shape = CircleShape,
        color = Color(0xFFD7DEE8)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = iniciales,
                color = Color(0xFF4E5F78),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun AvatarPerfilComponentPreview() {
    HelpdeskProTheme(dynamicColor = false) {
        AvatarPerfilComponent(nombre = "Ana", apellidos = "Pérez")
    }
}
