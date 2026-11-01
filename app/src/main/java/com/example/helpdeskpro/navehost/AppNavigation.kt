package com.example.helpdeskpro.navehost

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.helpdeskpro.viemodel.vm.CrearCuentaViewModel
import com.example.helpdeskpro.viemodel.vm.HomeEmpleadoViewModel
import com.example.helpdeskpro.viemodel.vm.LoginViewModel
import com.example.helpdeskpro.viemodel.vm.NuevaIncidenciaViewModel
import com.example.helpdeskpro.viemodel.vm.PerfilViewModel
import com.example.helpdeskpro.viemodel.vm.RecuperarContrasenaViewModel
import com.example.helpdeskpro.vista.componente.HomeTab
import com.example.helpdeskpro.vista.pagina.CrearCuentaPage
import com.example.helpdeskpro.vista.pagina.HomeEmpleadoPage
import com.example.helpdeskpro.vista.pagina.LoginPage
import com.example.helpdeskpro.vista.pagina.NuevaIncidenciaPage
import com.example.helpdeskpro.vista.pagina.PerfilPage
import com.example.helpdeskpro.vista.pagina.RecuperarContrasenaPage

/**
 * Configura la navegación y conecta cada página con su ViewModel.
 */
@Composable
fun AppNavigation(modifier: Modifier = Modifier) {

    val navController = rememberNavController()

    // ViewModels
    val loginViewModel: LoginViewModel = viewModel()
    val recuperarViewModel: RecuperarContrasenaViewModel = viewModel()
    val crearCuentaViewModel: CrearCuentaViewModel = viewModel()
    val homeEmpleadoViewModel: HomeEmpleadoViewModel = viewModel()
    val nuevaIncidenciaViewModel: NuevaIncidenciaViewModel = viewModel()
    val perfilViewModel: PerfilViewModel = viewModel()

    // Navegación
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

    NavHost(
        navController = navController,
        startDestination = RouteNavigation.Login,
        modifier = modifier
    ) {

        // ---------- LOGIN ----------
        composable<RouteNavigation.Login> {

            LoginPage(
                loginViewModel = loginViewModel,
                onLoginSuccess = { userId ->
                    navigate(RouteNavigation.HomeEmpleado(userId))
                },
                onForgotPassword = {
                    navigate(RouteNavigation.RecuperarContrasena)
                },
                onCreateAccount = {
                    navigate(RouteNavigation.CrearCuenta)
                }
            )
        }

        // ---------- RECUPERAR CONTRASEÑA ----------
        composable<RouteNavigation.RecuperarContrasena> {

            RecuperarContrasenaPage(
                recuperarContrasenaViewModel = recuperarViewModel,
                onVolverLogin = ::volverALogin
            )
        }

        // ---------- CREAR CUENTA ----------
        composable<RouteNavigation.CrearCuenta> {

            CrearCuentaPage(
                crearCuentaViewModel = crearCuentaViewModel,
                onVolverLogin = ::volverALogin
            )
        }

        // ---------- HOME EMPLEADO ----------
        composable<RouteNavigation.HomeEmpleado> { backStackEntry ->

            val route = backStackEntry.toRoute<RouteNavigation.HomeEmpleado>()

            HomeEmpleadoPage(
                homeEmpleadoViewModel = homeEmpleadoViewModel,
                selectedTab = HomeTab.INICIO,
                onInicio = {},
                onNueva = {
                    navigate(RouteNavigation.NuevaIncidencia(route.userId))
                },
                onPerfil = {
                    navigate(RouteNavigation.Perfil(route.userId))
                },
                onIncidenciaClick = {}
            )
        }

        // ---------- NUEVA INCIDENCIA ----------
        composable<RouteNavigation.NuevaIncidencia> { backStackEntry ->

            val route = backStackEntry.toRoute<RouteNavigation.NuevaIncidencia>()

            NuevaIncidenciaPage(
                nuevaIncidenciaViewModel = nuevaIncidenciaViewModel,
                onVolver = {
                    navController.popBackStack()
                },
                onIncidenciaCreada = {
                    navigate(RouteNavigation.HomeEmpleado(route.userId))
                }
            )
        }

        // ---------- PERFIL ----------
        composable<RouteNavigation.Perfil> { backStackEntry ->

            val route = backStackEntry.toRoute<RouteNavigation.Perfil>()

            PerfilPage(
                perfilViewModel = perfilViewModel,
                onInicio = {
                    navigate(RouteNavigation.HomeEmpleado(route.userId))
                },
                onNueva = {
                    navigate(RouteNavigation.NuevaIncidencia(route.userId))
                },
                onCerrarSesion = {
                    perfilViewModel.limpiarPerfil()

                    navController.navigate(RouteNavigation.Login) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}