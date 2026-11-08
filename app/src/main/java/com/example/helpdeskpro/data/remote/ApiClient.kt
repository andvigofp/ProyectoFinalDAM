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
