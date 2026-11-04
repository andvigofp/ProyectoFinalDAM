package com.example.helpdeskpro.navehost

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.helpdeskpro.R
import com.example.helpdeskpro.modelo.DetalleIncidenciaTecnicoDTO
import com.example.helpdeskpro.viemodel.vm.CrearCuentaViewModel
import com.example.helpdeskpro.viemodel.vm.CrearCuentaViewModelFactory
import com.example.helpdeskpro.viemodel.vm.DetalleEmpleadoViewModel
import com.example.helpdeskpro.viemodel.vm.DetalleEmpleadoViewModelFactory
import com.example.helpdeskpro.viemodel.vm.HomeEmpleadoViewModel
import com.example.helpdeskpro.viemodel.vm.HomeEmpleadoViewModelFactory
import com.example.helpdeskpro.viemodel.vm.LoginViewModel
import com.example.helpdeskpro.viemodel.vm.LoginViewModelFactory
import com.example.helpdeskpro.viemodel.vm.NuevaIncidenciaViewModel
import com.example.helpdeskpro.viemodel.vm.NuevaIncidenciaViewModelFactory
import com.example.helpdeskpro.viemodel.vm.PerfilViewModel
import com.example.helpdeskpro.viemodel.vm.PerfilViewModelFactory
import com.example.helpdeskpro.viemodel.vm.RecuperarContrasenaViewModel
import com.example.helpdeskpro.viemodel.vm.RecuperarContrasenaViewModelFactory
import com.example.helpdeskpro.vista.componentes.homeEmpleadoComponete.HomeTab
import com.example.helpdeskpro.vista.paginas.CrearCuentaPage
import com.example.helpdeskpro.vista.paginas.DetalleIncideciaTecnicoPage
import com.example.helpdeskpro.vista.paginas.HomeEmpleadoPage
import com.example.helpdeskpro.vista.paginas.LoginPage
import com.example.helpdeskpro.vista.paginas.NuevaIncidenciaPage
import com.example.helpdeskpro.vista.paginas.PerfilPage
import com.example.helpdeskpro.vista.paginas.RecuperarContrasenaPage
import kotlinx.coroutines.flow.collectLatest

/**
 * Configura la navegación y conecta cada página con su ViewModel.
 *
 * @param modifier modificador aplicado al contenedor de navegación.
 */
@Composable
fun AppNavigation(modifier: Modifier = Modifier) {

    val navController = rememberNavController()
    val application = LocalContext.current.applicationContext as Application

    // ---------- CONTROL DE SESIÓN ----------
    LaunchedEffect(Unit) {
        SessionEvents.forbidden.collect {
            navController.navigate(RouteNavigation.Login) {
                popUpTo(navController.graph.id) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }
    }

// ---------- NAVEGACIÓN GLOBAL ----------
    LaunchedEffect(Unit) {
        SessionEvents.navigation.collectLatest { route ->
            when (route) {
                is RouteNavigation.Login,
                is RouteNavigation.CrearCuenta,
                is RouteNavigation.RecuperarContrasena,
                is RouteNavigation.HomeEmpleado,
                is RouteNavigation.NuevaIncidencia,
                is RouteNavigation.DetalleIncidenciaTecnico,
                is RouteNavigation.Perfil -> navController.navigate(route)
            }
        }
    }

    /**
     * Navega al destino indicado y evita duplicar el destino superior.
     *
     * @param route destino al que navegar.
     */
    fun navigate(route: RouteNavigation) {
        navController.navigate(route) {
            launchSingleTop = true
        }
    }

    /** Regresa al login o lo abre si no hay una pantalla anterior. */
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

            val loginViewModel: LoginViewModel = viewModel(
                factory = LoginViewModelFactory(application)
            )

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

            val recuperarViewModel: RecuperarContrasenaViewModel = viewModel(
                factory = RecuperarContrasenaViewModelFactory(application)
            )

            RecuperarContrasenaPage(
                recuperarContrasenaViewModel = recuperarViewModel,
                onVolverLogin = ::volverALogin
            )
        }

        // ---------- CREAR CUENTA ----------
        composable<RouteNavigation.CrearCuenta> {

            val crearCuentaViewModel: CrearCuentaViewModel = viewModel(
                factory = CrearCuentaViewModelFactory(application)
            )

            CrearCuentaPage(
                crearCuentaViewModel = crearCuentaViewModel,
                onVolverLogin = ::volverALogin
            )
        }

        // ---------- HOME EMPLEADO ----------
        composable<RouteNavigation.HomeEmpleado> { backStackEntry ->

            val route = backStackEntry.toRoute<RouteNavigation.HomeEmpleado>()

            val homeEmpleadoViewModel: HomeEmpleadoViewModel = viewModel(
                factory = HomeEmpleadoViewModelFactory(
                    application = application,
                    userId = route.userId
                )
            )

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
                onIncidenciaClick = { incidencia ->
                    navigate(
                        RouteNavigation.DetalleIncidenciaTecnico(
                            userId = route.userId,
                            incidenciaId = incidencia.id,
                            titulo = incidencia.titulo,
                            categoria = incidencia.categoria,
                            prioridad = incidencia.prioridad,
                            fecha = incidencia.fecha,
                            estado = incidencia.estado
                        )
                    )
                }
            )
        }

        // ---------- DETALLE DE INCIDENCIA ----------
        composable<RouteNavigation.DetalleIncidenciaTecnico> { backStackEntry ->

            val route = backStackEntry.toRoute<RouteNavigation.DetalleIncidenciaTecnico>()
            val detalleViewModel: DetalleEmpleadoViewModel = viewModel(
                factory = DetalleEmpleadoViewModelFactory(
                    application = application,
                    userId = route.userId,
                    incidenciaId = route.incidenciaId
                )
            )

            DetalleIncideciaTecnicoPage(
                detalleEmpleadoViewModel = detalleViewModel,
                cargarDetalle = {
                    DetalleIncidenciaTecnicoDTO(
                        id = route.incidenciaId,
                        titulo = route.titulo,
                        descripcion = application.getString(R.string.not_provided),
                        estado = route.estado,
                        creador = application.getString(R.string.not_provided),
                        departamento = application.getString(R.string.not_provided),
                        categoria = route.categoria,
                        prioridad = route.prioridad,
                        fechaCreacion = route.fecha
                    )
                },
                onVolver = {
                    navController.popBackStack()
                },
                onIncidencias = {
                    navigate(RouteNavigation.HomeEmpleado(route.userId))
                },
                onPerfil = {
                    navigate(RouteNavigation.Perfil(route.userId))
                }
            )
        }

        // ---------- NUEVA INCIDENCIA ----------
        composable<RouteNavigation.NuevaIncidencia> { backStackEntry ->

            val route = backStackEntry.toRoute<RouteNavigation.NuevaIncidencia>()

            val nuevaIncidenciaViewModel: NuevaIncidenciaViewModel = viewModel(
                factory = NuevaIncidenciaViewModelFactory(
                    application = application,
                    userId = route.userId
                )
            )

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

            val perfilViewModel: PerfilViewModel = viewModel(
                factory = PerfilViewModelFactory(
                    application = application,
                    userId = route.userId
                )
            )

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