package com.example.helpdeskpro.viemodel.ustate

/**
 * Estado único de la pantalla de registro de una nueva cuenta.
 *
 * @param nombre nombre introducido en el formulario.
 * @param apellidos apellidos introducidos.
 * @param email correo electrónico introducido.
 * @param telefono teléfono introducido.
 * @param password contraseña introducida.
 * @param rol rol seleccionado.
 * @param departamento departamento seleccionado.
 * @param nombreError error de validación del nombre.
 * @param apellidosError error de validación de los apellidos.
 * @param emailError error de validación del correo.
 * @param telefonoError error de validación del teléfono.
 * @param passwordError error de validación de la contraseña.
 * @param rolError error de validación del rol.
 * @param departamentoError error de validación del departamento.
 * @param isLoading indica si hay una operación en curso.
 * @param errorMessage error general del formulario o de la operación.
 * @param cuentaCreada indica si la cuenta se ha creado.
 */
data class CrearCuentaUiState(
    val nombre: String="",
    val apellidos: String="",
    val email: String="",
    val telefono: String="",
    val password: String="",
    val rol: String="",
    val departamento: String="",
    val nombreError: String? = null,
    val apellidosError: String? = null,
    val emailError: String? = null,
    val telefonoError: String? = null,
    val passwordError: String? = null,
    val rolError: String? = null,
    val departamentoError: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val cuentaCreada: Boolean = false
)