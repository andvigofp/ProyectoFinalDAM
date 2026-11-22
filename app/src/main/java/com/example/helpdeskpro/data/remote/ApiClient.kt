package com.example.helpdeskpro.data.remote

import android.content.Context
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.helpdeskpro.BuildConfig
import com.google.gson.Gson
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient

private val Context.sessionDataStore by preferencesDataStore(
    name = "helpdesk_session",
    produceMigrations = { context ->
        listOf(SharedPreferencesMigration(context, "helpdesk_session"))
    }
)

class SessionTokenStore(context: Context) {
    private val dataStore = context.applicationContext.sessionDataStore
    private val tokenKey = stringPreferencesKey(TOKEN_KEY)
    private val cipher = AndroidKeystoreTokenCipher()

    fun get(): String? = runBlocking {
        val storedToken = dataStore.data.first()[tokenKey] ?: return@runBlocking null
        if (storedToken.startsWith(ENCRYPTED_TOKEN_PREFIX)) {
            cipher.decrypt(storedToken)
        } else {
            val encryptedToken = cipher.encrypt(storedToken)
            dataStore.edit { preferences ->
                if (preferences[tokenKey] == storedToken) {
                    preferences[tokenKey] = encryptedToken
                }
            }
            storedToken
        }
    }

    suspend fun save(token: String) {
        val encryptedToken = cipher.encrypt(token)
        dataStore.edit { preferences ->
            preferences[tokenKey] = encryptedToken
        }
    }

    suspend fun clear() {
        dataStore.edit { preferences ->
            preferences.remove(tokenKey)
        }
    }

    private companion object {
        const val TOKEN_KEY = "jwt"
        const val ENCRYPTED_TOKEN_PREFIX = "enc:v1:"
    }
}

class HelpdeskApiClient(context: Context) {
    val tokenStore = SessionTokenStore(context)
    val api: HelpdeskApi = Retrofit.Builder()
        .baseUrl(BuildConfig.API_BASE_URL)
        .client(
            OkHttpClient.Builder()
                // Respaldo automático: si la URL principal no responde
                // (error de red, no de la API), reintenta contra la URL
                // de fallback (la nube). Así la misma app funciona en local
                // con Docker y sin él contra Railway.
                // - La URL principal solo espera unos segundos a conectar
                //   (por defecto OkHttp espera 10 s).
                // - Si la principal falla, durante un rato todas las peticiones
                //   van directas al respaldo, sin volver a esperar cada vez.
                .addInterceptor(Interceptor { chain ->
                    val request = chain.request()
                    val fallback = BuildConfig.API_FALLBACK_URL
                        .takeIf { it.isNotBlank() }
                        ?.let(HttpUrl::parse)
                    if (fallback == null || request.url().host() == fallback.host()) {
                        return@Interceptor chain.proceed(request)
                    }
                    val retryRequest = request.newBuilder()
                        .url(
                            request.url().newBuilder()
                                .scheme(fallback.scheme())
                                .host(fallback.host())
                                .port(fallback.port())
                                .build()
                        )
                        .build()
                    if (System.currentTimeMillis() < usarRespaldoHasta) {
                        return@Interceptor chain.proceed(retryRequest)
                    }
                    try {
                        chain.withConnectTimeout(ESPERA_PRINCIPAL_SEGUNDOS, TimeUnit.SECONDS)
                            .proceed(request)
                    } catch (exception: IOException) {
                        usarRespaldoHasta = System.currentTimeMillis() + DURACION_RESPALDO_MS
                        chain.proceed(retryRequest)
                    }
                })
                .addInterceptor(Interceptor { chain ->
                    val request = chain.request().newBuilder().apply {
                        tokenStore.get()?.let { header("Authorization", "Bearer $it") }
                    }.build()
                    chain.proceed(request)
                })
                .build()
        )
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(HelpdeskApi::class.java)

    private companion object {
        /** Segundos que se espera a conectar con la URL principal (Docker local). */
        const val ESPERA_PRINCIPAL_SEGUNDOS = 2

        /** Tiempo que se usa directamente el respaldo tras un fallo de la principal. */
        const val DURACION_RESPALDO_MS = 60_000L

        /** Hasta cuándo (en ms) las peticiones van directas a la URL de respaldo. */
        @Volatile
        var usarRespaldoHasta = 0L
    }
}

class ApiException(message: String, cause: Throwable? = null) : Exception(message, cause)

suspend fun <T> apiCall(block: suspend () -> T): T = try {
    block()
} catch (exception: CancellationException) {
    throw exception
} catch (exception: HttpException) {
    val response = exception.response()?.errorBody()?.string()
    val parsed = response?.let {
        runCatching { Gson().fromJson(it, ErrorResponse::class.java) }.getOrNull()
    }
    throw ApiException(
        parsed?.mensaje?.takeIf(String::isNotBlank)
            ?: parsed?.error?.takeIf(String::isNotBlank)
            ?: "La API respondió con el código ${exception.code()}.",
        exception
    )
} catch (exception: IOException) {
    throw ApiException(
        "No se pudo conectar con la API. Comprueba que Spring Boot está iniciado y que la URL es correcta.",
        exception
    )
}
