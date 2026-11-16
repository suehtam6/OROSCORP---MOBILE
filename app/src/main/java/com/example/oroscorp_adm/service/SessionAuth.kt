package com.example.oroscorp_adm.service

/** Token mantido somente em memória. Nunca salve senha ou token no log. */
object SessionAuth {
    @Volatile var token: String? = null
        private set
    fun setToken(value: String?) { token = value?.takeIf { it.isNotBlank() } }
    fun clear() { token = null }
}
