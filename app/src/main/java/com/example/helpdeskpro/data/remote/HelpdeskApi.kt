package com.example.helpdeskpro.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface HelpdeskApi {
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): TokenResponse

    @POST("api/auth/registro")
    suspend fun register(@Body request: RegisterRequest): UserResponse

    @POST("api/auth/recuperar-contrasena")
    suspend fun requestPasswordRecovery(
        @Body request: PasswordRecoveryRequest
    ): MessageResponse

    @POST("api/auth/restablecer-contrasena")
    suspend fun resetPassword(
        @Body request: ResetPasswordRequest
    ): MessageResponse

    @GET("api/auth/me")
    suspend fun currentUser(): UserResponse

    @PUT("api/auth/me")
    suspend fun updateCurrentUser(@Body request: ProfileRequest): UserResponse

    @GET("api/incidencias")
    suspend fun incidents(
        @Query("busqueda") busqueda: String = ""
    ): List<IncidentResponse>

    @GET("api/incidencias/usuario/{userId}")
    suspend fun incidentsForUser(
        @Path("userId") userId: Long,
        @Query("busqueda") busqueda: String = ""
    ): List<IncidentResponse>

    @GET("api/incidencias/tecnico/{userId}")
    suspend fun technicianIncidents(
        @Path("userId") userId: Long,
        @Query("busqueda") busqueda: String = ""
    ): List<IncidentResponse>

    @GET("api/incidencias/{id}")
    suspend fun incident(@Path("id") id: Long): IncidentResponse

    @POST("api/incidencias")
    suspend fun createIncident(@Body request: IncidentRequest): IncidentResponse

    @PUT("api/incidencias/{id}/estado/{stateId}")
    suspend fun updateIncidentState(
        @Path("id") id: Long,
        @Path("stateId") stateId: Long
    ): IncidentResponse

    @PUT("api/incidencias/{id}/solucion")
    suspend fun registerSolution(
        @Path("id") id: Long,
        @Body request: IncidentRequest
    ): IncidentResponse

    @GET("api/comentarios/incidencia/{incidentId}")
    suspend fun comments(@Path("incidentId") incidentId: Long): List<CommentResponse>

    @POST("api/comentarios")
    suspend fun addComment(@Body request: CommentRequest): CommentResponse

    @GET("api/historial/incidencia/{incidentId}")
    suspend fun history(@Path("incidentId") incidentId: Long): List<HistoryResponse>

    @GET("api/usuarios")
    suspend fun users(@Query("busqueda") busqueda: String = ""): List<UserResponse>

    @POST("api/usuarios")
    suspend fun createUser(@Body request: UserRequest): UserResponse

    @PUT("api/usuarios/{id}")
    suspend fun updateUser(
        @Path("id") id: Long,
        @Body request: UserRequest
    ): UserResponse

    @DELETE("api/usuarios/{id}")
    suspend fun deactivateUser(@Path("id") id: Long): Response<Unit>

    @GET("api/catalogos/{catalog}")
    suspend fun catalog(@Path("catalog") catalog: String): List<CatalogItem>
}

data class LoginRequest(val email: String, val password: String)
data class RegisterRequest(
    val nombre: String,
    val apellidos: String,
    val email: String,
    val password: String,
    val telefono: String,
    val departamentoId: Long?
)
data class PasswordRecoveryRequest(val email: String)
data class ResetPasswordRequest(
    val email: String,
    val codigo: String,
    val nuevaContrasena: String
)
data class MessageResponse(val mensaje: String)
data class ProfileRequest(
    val nombre: String,
    val apellidos: String,
    val email: String,
    val telefono: String,
    val password: String?
)
data class TokenResponse(val token: String, val tipo: String?, val usuario: UserResponse)
data class CatalogItem(val id: Long, val nombre: String)
data class UserResponse(
    val id: Long,
    val nombre: String,
    val apellidos: String,
    val email: String,
    val telefono: String?,
    val activo: Boolean?,
    val rolId: Long,
    val rolNombre: String,
    val departamentoId: Long?,
    val departamentoNombre: String?,
    val fechaRegistro: String? = null
)
data class UserRequest(
    val nombre: String,
    val apellidos: String,
    val email: String,
    val password: String?,
    val telefono: String,
    val activo: Boolean,
    val rolId: Long,
    val departamentoId: Long?
)
data class IncidentResponse(
    val id: Long,
    val titulo: String,
    val descripcion: String,
    val solucion: String?,
    val fechaCreacion: String?,
    val usuarioId: Long,
    val usuarioNombre: String,
    val departamentoNombre: String?,
    val tecnicoId: Long?,
    val tecnicoNombre: String?,
    val categoriaId: Long,
    val categoriaNombre: String,
    val prioridadId: Long,
    val prioridadNombre: String,
    val estadoId: Long,
    val estadoNombre: String
)
data class IncidentRequest(
    val titulo: String? = null,
    val descripcion: String? = null,
    val solucion: String? = null,
    val usuarioId: Long? = null,
    val categoriaId: Long? = null,
    val prioridadId: Long? = null,
    val estadoId: Long? = null
)
data class CommentRequest(
    val mensaje: String,
    val usuarioId: Long?,
    val incidenciaId: Long
)
data class CommentResponse(
    val mensaje: String,
    val fecha: String?,
    val usuarioNombre: String
)
data class HistoryResponse(val accion: String, val fecha: String?)
data class ErrorResponse(val mensaje: String?, val error: String?)
