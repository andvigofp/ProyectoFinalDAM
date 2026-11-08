package com.example.helpdeskpro.data.remote

import android.content.Context
import com.example.helpdeskpro.BuildConfig
import com.google.gson.Gson
import kotlinx.coroutines.CancellationException
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import okhttp3.Interceptor
import okhttp3.OkHttpClient

class SessionTokenStore(context: Context) {
    private val preferences = context.applicationContext
        .getSharedPreferences("helpdesk_session", Context.MODE_PRIVATE)

    fun get(): String? = preferences.getString(TOKEN_KEY, null)

    fun save(token: String) {
        preferences.edit().putString(TOKEN_KEY, token).apply()
    }

    fun clear() {
        preferences.edit().remove(TOKEN_KEY).apply()
    }

    private companion object {
        const val TOKEN_KEY = "jwt"
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
