package com.example.helpdeskpro.vista.paginas

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.helpdeskpro.R
import com.example.helpdeskpro.ui.theme.HelpdeskProTheme
import com.example.helpdeskpro.viemodel.ustate.RestablecerContrasenaUiState
import com.example.helpdeskpro.viemodel.vm.RestablecerContrasenaViewModel
import com.example.helpdeskpro.vista.componentes.recuperarContrasenaComponente.CamposRestablecerContrasenaComponent
import kotlinx.coroutines.delay

@Composable
fun RestablecerContrasenaPage(
    viewModel: RestablecerContrasenaViewModel,
    onVolverLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.successMessage) {
        if (uiState.successMessage != null) {
            delay(2_000)
            onVolverLogin()
        }
    }

    RestablecerContrasenaPageContent(
        uiState = uiState,
        onContrasenaChange = viewModel::onNuevaContrasenaChange,
        onCodigoChange = viewModel::onCodigoChange,
        onRestablecer = viewModel::restablecerContrasena,
        onVolverLogin = onVolverLogin,
        modifier = modifier
    )
}

@Composable
private fun RestablecerContrasenaPageContent(
    uiState: RestablecerContrasenaUiState,
    onContrasenaChange: (String) -> Unit,
    onCodigoChange: (String) -> Unit,
    onRestablecer: () -> Unit,
    onVolverLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Surface(
            modifier = Modifier.size(104.dp),
            shape = CircleShape,
            color = Color.Transparent
        ) {
            Image(
                painter = painterResource(R.drawable.ic_logo),
                contentDescription = stringResource(R.string.logo),
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = stringResource(R.string.recover_password),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.reset_password_description),
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(24.dp))
        CamposRestablecerContrasenaComponent(
            uiState = uiState,
            onContrasenaChange = onContrasenaChange,
            onCodigoChange = onCodigoChange
        )
        if (uiState.errorMessage != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = uiState.errorMessage,
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        if (uiState.successMessage != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = uiState.successMessage,
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Spacer(modifier = Modifier.height(28.dp))
        if (uiState.successMessage == null) {
            Button(
                onClick = onRestablecer,
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator()
                } else {
                    Text(stringResource(R.string.reset_password_action))
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        TextButton(
            onClick = onVolverLogin,
            enabled = !uiState.isLoading
        ) {
            Text(stringResource(R.string.back_to_login))
        }
    }

}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun RestablecerContrasenaPagePreview() {
    HelpdeskProTheme(dynamicColor = false) {
        RestablecerContrasenaPageContent(
            uiState = RestablecerContrasenaUiState(
                email = "usuario@empresa.com",
                codigo = "123456",
                nuevaContrasena = "ContrasenaSegura"
            ),
            onContrasenaChange = {},
            onCodigoChange = {},
            onRestablecer = {},
            onVolverLogin = {}
        )
    }
}
