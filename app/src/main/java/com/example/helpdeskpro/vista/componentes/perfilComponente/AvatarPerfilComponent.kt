package com.example.helpdeskpro.vista.componentes.perfilComponente

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.helpdeskpro.R
import com.example.helpdeskpro.ui.theme.HelpdeskProTheme
import java.util.Locale

@Composable
fun AvatarPerfilComponent(
    nombre: String,
    apellidos: String,
    modifier: Modifier = Modifier,
    size: Dp = 96.dp
) {
    val primerNombre = nombre.trim().substringBefore(' ').lowercase(Locale.ROOT)
    val esNombreMujer = primerNombre in setOf(
        "ana", "marta", "maría", "lucía", "paula", "laura", "carmen",
        "sofía", "elena", "alba", "sara", "isabel", "irene", "patricia",
        "rocío", "noelia", "andrea"
    ) || primerNombre.endsWith('a')
    val icono = if (esNombreMujer) {
        R.drawable.ic_perfil_mujer
    } else {
        R.drawable.ic_perfil_hombre
    }

    Surface(
        modifier = modifier.size(size),
        shape = CircleShape,
        color = MaterialTheme.colorScheme.outlineVariant
    ) {
        Image(
            painter = painterResource(icono),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AvatarPerfilComponentPreview() {
    HelpdeskProTheme(dynamicColor = false) {
        AvatarPerfilComponent(nombre = "Ana", apellidos = "Pérez")
    }
}
