package com.example.helpdeskpro.vista.componentes.adminUsuariosComponente

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.helpdeskpro.R
import com.example.helpdeskpro.modelo.AdminUsuarioDTO

@Composable
fun UsuarioAdminCardComponent(
    usuario: AdminUsuarioDTO,
    onEditar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        color = Color(0xFFFCFCFD),
        border = BorderStroke(1.dp, Color(0xFFAAB9D0))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 11.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0xFFD0D6DE), CircleShape)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = "${usuario.nombre} ${usuario.apellidos}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF414A5A)
                )
                Text(
                    text = "${usuario.rol} · ${usuario.departamento}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF8796AD)
                )
                Surface(
                    shape = RoundedCornerShape(5.dp),
                    color = if (usuario.activo) Color(0xFF72AA7C) else Color(0xFF9AA1AA)
                ) {
                    Text(
                        text = stringResource(
                            if (usuario.activo) R.string.active else R.string.inactive
                        ),
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White
                    )
                }
            }
            TextButton(onClick = onEditar) {
                Text(
                    text = stringResource(R.string.edit_user_action),
                    color = Color(0xFF596F91),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}
