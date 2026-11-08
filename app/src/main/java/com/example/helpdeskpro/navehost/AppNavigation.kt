package com.example.helpdeskpro.navehost

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.helpdeskpro.HelpdeskProApp
import com.example.helpdeskpro.R
import com.example.helpdeskpro.viemodel.vm.AdminUsuariosViewModel
import com.example.helpdeskpro.viemodel.vm.AdminUsuariosViewModelFactory
import com.example.helpdeskpro.viemodel.vm.CrearCuentaViewModel
import com.example.helpdeskpro.viemodel.vm.CrearCuentaViewModelFactory
import com.example.helpdeskpro.viemodel.vm.DetalleEmpleadoViewModel
import com.example.helpdeskpro.viemodel.vm.DetalleEmpleadoViewModelFactory
import com.example.helpdeskpro.viemodel.vm.EditarPerfilViewModel
import com.example.helpdeskpro.viemodel.vm.EditarPerfilViewModelFactory
import com.example.helpdeskpro.viemodel.vm.HomeEmpleadoViewModel
import com.example.helpdeskpro.viemodel.vm.HomeEmpleadoViewModelFactory
import com.example.helpdeskpro.viemodel.vm.HomeAdminViewModel
import com.example.helpdeskpro.viemodel.vm.HomeAdminViewModelFactory
import com.example.helpdeskpro.viemodel.vm.HomeTecnicoViewModel
import com.example.helpdeskpro.viemodel.vm.HomeTecnicoViewModelFactory
import com.example.helpdeskpro.viemodel.vm.LoginViewModel
import com.example.helpdeskpro.viemodel.vm.LoginViewModelFactory
import com.example.helpdeskpro.viemodel.vm.NuevaIncidenciaViewModel
import com.example.helpdeskpro.viemodel.vm.NuevaIncidenciaViewModelFactory
import com.example.helpdeskpro.viemodel.vm.PerfilViewModel
import com.example.helpdeskpro.viemodel.vm.PerfilViewModelFactory
import com.example.helpdeskpro.viemodel.vm.RecuperarContrasenaViewModel
import com.example.helpdeskpro.viemodel.vm.RecuperarContrasenaViewModelFactory
import com.example.helpdeskpro.vista.componentes.homeEmpleadoComponete.HomeTab
import com.example.helpdeskpro.vista.paginas.AdminCrearUsuarioCrearPage
import com.example.helpdeskpro.vista.paginas.AdminUsuariosPage
import com.example.helpdeskpro.vista.paginas.CrearCuentaPage
import com.example.helpdeskpro.vista.paginas.DetalleIncideciaTecnicoPage
import com.example.helpdeskpro.vista.paginas.EditarPerfilPage
import com.example.helpdeskpro.vista.paginas.HomeEmpleadoPage
import com.example.helpdeskpro.vista.paginas.HomeAdminPage
import com.example.helpdeskpro.vista.paginas.HomeTecnicoPage
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
    val application = LocalContext.current.applicationContext as HelpdeskProApp
    val authRepository = application.authRepository
    val incidenciasRepository = application.incidenciasRepository
    val perfilRepository = application.perfilRepository
    val administracionRepository = application.administracionRepository

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
                is RouteNavigation.HomeTecnico,
                is RouteNavigation.HomeAdmin,
                is RouteNavigation.AdminIncidencias,
                is RouteNavigation.AdminUsuarios,
                is RouteNavigation.AdminUsuario,
                is RouteNavigation.NuevaIncidencia,
                is RouteNavigation.DetalleIncidenciaTecnico,
                is RouteNavigation.Perfil,
                is RouteNavigation.EditarPerfil -> navController.navigate(route)
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
                factory = LoginViewModelFactory(application, authRepository)
            )

            LoginPage(
                loginViewModel = loginViewModel,
                onLoginSuccess = { userId, rol ->
                    when (rol.uppercase()) {
                        "TECNICO" -> navigate(RouteNavigation.HomeTecnico(userId))
                        "ADMINISTRADOR" -> navigate(RouteNavigation.HomeAdmin(userId))
                        else -> navigate(RouteNavigation.HomeEmpleado(userId))
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

        // ---------- RECUPERAR CONTRASEÑA ----------
        composable<RouteNavigation.RecuperarContrasena> {

            val recuperarViewModel: RecuperarContrasenaViewModel = viewModel(
                factory = RecuperarContrasenaViewModelFactory(application, authRepository)
            )

            RecuperarContrasenaPage(
                recuperarContrasenaViewModel = recuperarViewModel,
                onVolverLogin = ::volverALogin
            )
        }

        // ---------- CREAR CUENTA ----------
        composable<RouteNavigation.CrearCuenta> {

            val crearCuentaViewModel: CrearCuentaViewModel = viewModel(
                factory = CrearCuentaViewModelFactory(application, authRepository)
            )

            CrearCuentaPage(
                crearCuentaViewModel = crearCuentaViewModel,
                onVolverLogin = ::volverALogin,
                onCuentaCreada = ::volverALogin
            )
        }

        // ---------- HOME EMPLEADO ----------
        composable<RouteNavigation.HomeEmpleado> { backStackEntry ->

            val route = backStackEntry.toRoute<RouteNavigation.HomeEmpleado>()

            val homeEmpleadoViewModel: HomeEmpleadoViewModel = viewModel(
                factory = HomeEmpleadoViewModelFactory(
                    application = application,
                    userId = route.userId,
                    incidenciasRepository = incidenciasRepository
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

        // ---------- HOME TÉCNICO ----------
        composable<RouteNavigation.HomeTecnico> { backStackEntry ->

            val route = backStackEntry.toRoute<RouteNavigation.HomeTecnico>()
            val homeTecnicoViewModel: HomeTecnicoViewModel = viewModel(
                factory = HomeTecnicoViewModelFactory(
                    application = application,
                    userId = route.userId,
                    incidenciasRepository = incidenciasRepository
                )
            )

            HomeTecnicoPage(
                homeTecnicoViewModel = homeTecnicoViewModel,
                onIncidenciaClick = { incidencia ->
                    navigate(
                        RouteNavigation.DetalleIncidenciaTecnico(
                            userId = route.userId,
                            incidenciaId = incidencia.id,
                            titulo = incidencia.titulo,
                            categoria = incidencia.categoria,
                            prioridad = incidencia.prioridad,
                            fecha = incidencia.fecha,
                            estado = incidencia.estado,
                            desdeHomeTecnico = true
                        )
                    )
                },
                onPerfil = {
                    navigate(RouteNavigation.Perfil(route.userId, esTecnico = true))
                }
            )
        }

        // ---------- PANEL ADMINISTRADOR ----------
        composable<RouteNavigation.HomeAdmin> { backStackEntry ->
            val route = backStackEntry.toRoute<RouteNavigation.HomeAdmin>()
            val homeAdminViewModel: HomeAdminViewModel = viewModel(
                factory = HomeAdminViewModelFactory(
                    application,
                    route.userId,
                    administracionRepository
                )
            )

            HomeAdminPage(
                homeAdminViewModel = homeAdminViewModel,
                onIncidencias = {
                    navigate(RouteNavigation.AdminIncidencias(route.userId))
                },
                onUsuarios = {
                    navigate(RouteNavigation.AdminUsuarios(route.userId))
                },
                onPerfil = {
                    navigate(RouteNavigation.Perfil(route.userId, esAdmin = true))
                }
            )
        }

        // ---------- INCIDENCIAS DE ADMINISTRACIÓN ----------
        composable<RouteNavigation.AdminIncidencias> { backStackEntry ->
            val route = backStackEntry.toRoute<RouteNavigation.AdminIncidencias>()
            val homeTecnicoViewModel: HomeTecnicoViewModel = viewModel(
                factory = HomeTecnicoViewModelFactory(
                    application,
                    route.userId,
                    incidenciasRepository
                )
            )

            HomeTecnicoPage(
                homeTecnicoViewModel = homeTecnicoViewModel,
                onIncidenciaClick = { incidencia ->
                    navigate(
                        RouteNavigation.DetalleIncidenciaTecnico(
                            userId = route.userId,
                            incidenciaId = incidencia.id,
                            titulo = incidencia.titulo,
                            categoria = incidencia.categoria,
                            prioridad = incidencia.prioridad,
                            fecha = incidencia.fecha,
                            estado = incidencia.estado,
                            desdeHomeAdmin = true
                        )
                    )
                },
                onPerfil = {
                    navigate(RouteNavigation.Perfil(route.userId, esAdmin = true))
                },
                modoAdmin = true,
                onPanel = { navigate(RouteNavigation.HomeAdmin(route.userId)) },
                onUsuarios = { navigate(RouteNavigation.AdminUsuarios(route.userId)) },
                tituloPersonalizado = application.getString(R.string.incidents)
            )
        }

        // ---------- USUARIOS DE ADMINISTRACIÓN ----------
        composable<RouteNavigation.AdminUsuarios> { backStackEntry ->
            val route = backStackEntry.toRoute<RouteNavigation.AdminUsuarios>()
            val adminHomeEntry = remember(backStackEntry) {
                navController.getBackStackEntry<RouteNavigation.HomeAdmin>()
            }
            val adminUsuariosViewModel: AdminUsuariosViewModel = viewModel(
                viewModelStoreOwner = adminHomeEntry,
                factory = AdminUsuariosViewModelFactory(application, administracionRepository)
            )

            AdminUsuariosPage(
                adminUsuariosViewModel = adminUsuariosViewModel,
                onVolver = { navController.popBackStack() },
                onNuevoUsuario = {
                    navigate(RouteNavigation.AdminUsuario(route.userId))
                },
                onEditarUsuario = { usuario ->
                    navigate(RouteNavigation.AdminUsuario(route.userId, usuario))
                }
            )
        }

        composable<RouteNavigation.AdminUsuario> { backStackEntry ->
            val route = backStackEntry.toRoute<RouteNavigation.AdminUsuario>()
            val adminHomeEntry = remember(backStackEntry) {
                navController.getBackStackEntry<RouteNavigation.HomeAdmin>()
            }
            val adminUsuariosViewModel: AdminUsuariosViewModel = viewModel(
                viewModelStoreOwner = adminHomeEntry,
                factory = AdminUsuariosViewModelFactory(application, administracionRepository)
            )

            AdminCrearUsuarioCrearPage(
                usuario = route.usuario,
                idNuevoUsuario = adminUsuariosViewModel.siguienteId(),
                onVolver = { navController.popBackStack() },
                onGuardar = { usuario, password ->
                    adminUsuariosViewModel.guardarUsuario(usuario, password) {
                        navController.popBackStack()
                    }
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
                    incidenciaId = route.incidenciaId,
                    incidenciasRepository = incidenciasRepository
                )
            )

            DetalleIncideciaTecnicoPage(
                detalleEmpleadoViewModel = detalleViewModel,
                onVolver = {
                    navController.popBackStack()
                },
                onIncidencias = {
                    if (route.desdeHomeAdmin) {
                        navigate(RouteNavigation.AdminIncidencias(route.userId))
                    } else if (route.desdeHomeTecnico) {
                        navigate(RouteNavigation.HomeTecnico(route.userId))
                    } else {
                        navigate(RouteNavigation.HomeEmpleado(route.userId))
                    }
                },
                onPerfil = {
                    navigate(
                        RouteNavigation.Perfil(
                            route.userId,
                            esTecnico = route.desdeHomeTecnico,
                            esAdmin = route.desdeHomeAdmin
                        )
                    )
                }
            )
        }

        // ---------- NUEVA INCIDENCIA ----------
        composable<RouteNavigation.NuevaIncidencia> { backStackEntry ->

            val route = backStackEntry.toRoute<RouteNavigation.NuevaIncidencia>()

            val nuevaIncidenciaViewModel: NuevaIncidenciaViewModel = viewModel(
                factory = NuevaIncidenciaViewModelFactory(
                    application = application,
                    userId = route.userId,
                    incidenciasRepository = incidenciasRepository
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
            val perfilOwner = remember(backStackEntry) {
                when {
                    route.esAdmin -> navController.getBackStackEntry<RouteNavigation.HomeAdmin>()
                    route.esTecnico -> navController.getBackStackEntry<RouteNavigation.HomeTecnico>()
                    else -> navController.getBackStackEntry<RouteNavigation.HomeEmpleado>()
                }
            }

            val perfilViewModel: PerfilViewModel = viewModel(
                viewModelStoreOwner = perfilOwner,
                factory = PerfilViewModelFactory(
                    application = application,
                    userId = route.userId,
                    perfilRepository = perfilRepository
                )
            )

            PerfilPage(
                perfilViewModel = perfilViewModel,
                modoTecnico = route.esTecnico,
                modoAdmin = route.esAdmin,
                onVolver = {
                    if (!navController.popBackStack()) {
                        if (route.esAdmin) {
                            navigate(RouteNavigation.HomeAdmin(route.userId))
                        } else if (route.esTecnico) {
                            navigate(RouteNavigation.HomeTecnico(route.userId))
                        } else {
                            navigate(RouteNavigation.HomeEmpleado(route.userId))
                        }
                    }
                },
                onInicio = {
                    if (route.esAdmin) {
                        navigate(RouteNavigation.HomeAdmin(route.userId))
                    } else if (route.esTecnico) {
                        navigate(RouteNavigation.HomeTecnico(route.userId))
                    } else {
                        navigate(RouteNavigation.HomeEmpleado(route.userId))
                    }
                },
                onNueva = {
                    if (route.esAdmin) {
                        navigate(RouteNavigation.AdminIncidencias(route.userId))
                    } else if (route.esTecnico) {
                        navigate(RouteNavigation.HomeTecnico(route.userId))
                    } else {
                        navigate(RouteNavigation.NuevaIncidencia(route.userId))
                    }
                },
                onUsuarios = {
                    if (route.esAdmin) {
                        navigate(RouteNavigation.AdminUsuarios(route.userId))
                    }
                },
                onEditarPerfil = { perfil ->
                    navigate(
                        RouteNavigation.EditarPerfil(
                            userId = route.userId,
                            perfil = perfil,
                            esTecnico = route.esTecnico,
                            esAdmin = route.esAdmin
                        )
                    )
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

        // ---------- EDITAR PERFIL ----------
        composable<RouteNavigation.EditarPerfil> { backStackEntry ->
            val route = backStackEntry.toRoute<RouteNavigation.EditarPerfil>()
            val perfilOwner = remember(backStackEntry) {
                when {
                    route.esAdmin -> navController.getBackStackEntry<RouteNavigation.HomeAdmin>()
                    route.esTecnico -> navController.getBackStackEntry<RouteNavigation.HomeTecnico>()
                    else -> navController.getBackStackEntry<RouteNavigation.HomeEmpleado>()
                }
            }
            val perfilViewModel: PerfilViewModel = viewModel(
                viewModelStoreOwner = perfilOwner,
                factory = PerfilViewModelFactory(application, route.userId, perfilRepository)
            )
            val editarPerfilViewModel: EditarPerfilViewModel = viewModel(
                factory = EditarPerfilViewModelFactory(
                    application,
                    route.userId,
                    perfilRepository
                )
            )

            EditarPerfilPage(
                editarPerfilViewModel = editarPerfilViewModel,
                perfil = route.perfil,
                onVolver = { navController.popBackStack() },
                onGuardar = { datos ->
                    perfilViewModel.actualizarPerfil(
                        route.perfil.copy(
                            nombre = datos.nombre,
                            apellidos = datos.apellidos,
                            email = datos.email,
                            telefono = datos.telefono
                        )
                    )
                    navController.popBackStack()
                }
            )
        }
    }
}