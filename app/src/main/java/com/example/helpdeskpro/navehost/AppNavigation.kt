package com.example.helpdeskpro.navehost

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.helpdeskpro.viemodel.vm.CrearCuentaViewModel
import com.example.helpdeskpro.viemodel.vm.HomeEmpleadoViewModel
import com.example.helpdeskpro.viemodel.vm.LoginViewModel
import com.example.helpdeskpro.viemodel.vm.NuevaIncidenciaViewModel
import com.example.helpdeskpro.viemodel.vm.PerfilViewModel
import com.example.helpdeskpro.viemodel.vm.RecuperarContrasenaViewModel
import com.example.helpdeskpro.vista.pagina.CrearCuentaPage
import com.example.helpdeskpro.vista.pagina.HomeEmpleadoPage
import com.example.helpdeskpro.vista.pagina.LoginPage
import com.example.helpdeskpro.vista.pagina.NuevaIncidenciaPage
import com.example.helpdeskpro.vista.pagina.PerfilPage
import com.example.helpdeskpro.vista.pagina.RecuperarContrasenaPage
import kotlinx.coroutines.flow.collectLatest

/**
 * Configura la navegación y conecta cada página con su ViewModel.
 */
@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val loginViewModel: LoginViewModel = viewModel()
    val recuperarViewModel: RecuperarContrasenaViewModel = viewModel()
    val crearCuentaViewModel: CrearCuentaViewModel = viewModel()
    val homeEmpleadoViewModel: HomeEmpleadoViewModel = viewModel()
    val nuevaIncidenciaViewModel: NuevaIncidenciaViewModel = viewModel()
    val perfilViewModel: PerfilViewModel = viewModel()

    fun navigate(route: RouteNavigation) {
        navController.navigate(route) {
            launchSingleTop = true
        }
    }

    fun volverALogin() {
        if (!navController.popBackStack()) {
            navigate(RouteNavigation.Login)
        }
    }

    LaunchedEffect(Unit) {
        SessionEvents.navigation.collectLatest { route ->
            navigate(route)
        }
    }

    LaunchedEffect(Unit) {
        SessionEvents.forbidden.collectLatest {
            navController.navigate(RouteNavigation.Login) {
                popUpTo(0) { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = RouteNavigation.Login,
        modifier = modifier
    ) {
        composable<RouteNavigation.Login> {
            LoginPage(
                state = loginViewModel.uiState,
                onEmailChange = loginViewModel::onEmailChange,
                onPasswordChange = loginViewModel::onPasswordChange,
                onLogin = {
                    loginViewModel.iniciarSesion {
                        navigate(RouteNavigation.HomeEmpleado)
                    }
                },
                onForgotPassword = {
                    navigate(RouteNavigation.RecuperarContrasena)
                },
                onCreateAccount = {
                    navigate(RouteNavigation.CrearCuenta)
                }
            )
        }

        composable<RouteNavigation.RecuperarContrasena> {
            val state = recuperarViewModel.uiState
            RecuperarContrasenaPage(
                email = state.email,
                onEmailChange = recuperarViewModel::onEmailChange,
                onEnviar = { recuperarViewModel.recuperarContrasena() },
                onVolverLogin = ::volverALogin,
                emailError = state.emailError,
                errorMessage = state.errorMessage,
                successMessage = state.successMessage,
                isLoading = state.isLoading
            )
        }

        composable<RouteNavigation.CrearCuenta> {
            val state = crearCuentaViewModel.uiState
            CrearCuentaPage(
                nombre = state.nombre,
                apellidos = state.apellidos,
                email = state.email,
                telefono = state.telefono,
                password = state.password,
                rol = state.rol,
                departamento = state.departamento,
                onNombreChange = crearCuentaViewModel::onNombreChange,
                onApellidosChange = crearCuentaViewModel::onApellidosChange,
                onEmailChange = crearCuentaViewModel::onEmailChange,
                onTelefonoChange = crearCuentaViewModel::onTelefonoChange,
                onPasswordChange = crearCuentaViewModel::onPasswordChange,
                onRolChange = crearCuentaViewModel::onRolChange,
                onDepartamentoChange = crearCuentaViewModel::onDepartamentoChange,
                onGuardar = { crearCuentaViewModel.crearCuenta() },
                onVolverLogin = ::volverALogin,
                nombreError = state.nombreError,
                apellidosError = state.apellidosError,
                emailError = state.emailError,
                telefonoError = state.telefonoError,
                passwordError = state.passwordError,
                rolError = state.rolError,
                departamentoError = state.departamentoError,
                errorMessage = state.errorMessage,
                isLoading = state.isLoading
            )
        }

        composable<RouteNavigation.HomeEmpleado> {
            val state = homeEmpleadoViewModel.uiState
            HomeEmpleadoPage(
                incidencias = state.incidencias,
                isLoading = state.isLoading,
                errorMessage = state.errorMessage,
                onNueva = { navigate(RouteNavigation.NuevaIncidencia) },
                onPerfil = { navigate(RouteNavigation.Perfil) }
            )
        }

        composable<RouteNavigation.NuevaIncidencia> {
            val state = nuevaIncidenciaViewModel.uiState
            NuevaIncidenciaPage(
                titulo = state.titulo,
                descripcion = state.descripcion,
                categoria = state.categoria,
                prioridad = state.prioridad,
                onTituloChange = nuevaIncidenciaViewModel::onTituloChange,
                onDescripcionChange = nuevaIncidenciaViewModel::onDescripcionChange,
                onCategoriaChange = nuevaIncidenciaViewModel::onCategoriaChange,
                onPrioridadChange = nuevaIncidenciaViewModel::onPrioridadChange,
                onCrear = { nuevaIncidenciaViewModel.crearIncidencia() },
                onVolver = { navController.popBackStack() },
                tituloError = state.tituloError,
                descripcionError = state.descripcionError,
                categoriaError = state.categoriaError,
                prioridadError = state.prioridadError,
                errorMessage = state.errorMessage,
                isLoading = state.isLoading
            )
        }

        composable<RouteNavigation.Perfil> {
            val state = perfilViewModel.uiState
            PerfilPage(
                perfil = state.perfil,
                isLoading = state.isLoading,
                errorMessage = state.errorMessage,
                onInicio = { navigate(RouteNavigation.HomeEmpleado) },
                onNueva = { navigate(RouteNavigation.NuevaIncidencia) },
                onCerrarSesion = {
                    perfilViewModel.limpiarPerfil()
                    navController.navigate(RouteNavigation.Login) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}
