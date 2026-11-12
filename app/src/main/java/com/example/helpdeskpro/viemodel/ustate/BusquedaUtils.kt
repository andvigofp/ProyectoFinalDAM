package com.example.helpdeskpro.viemodel.ustate

import java.text.Normalizer
import java.util.Locale

internal fun normalizarTextoBusqueda(texto: String): String =
    Normalizer.normalize(texto.lowercase(Locale.ROOT), Normalizer.Form.NFD)
        .replace(Regex("\\p{Mn}+"), "")
        .trim()
        .replace(Regex("\\s+"), " ")
