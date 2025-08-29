package com.aakash.vaultutility.vaultConfig

import com.aakash.vaultutility.utils.LoggerUtils
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.concurrent.atomic.AtomicReference

@Component
class TokenManager(
    val tokenInitializer: TokenInitializer
) {
    private val tokenRef = AtomicReference<String?>()
    private val expiryRef = AtomicReference<Instant?>()
    private val ttlMinutes: Long = 4

    fun getToken(): String {
        val expiry = expiryRef.get()
        val now = Instant.now()

        return if (expiry != null && now.isBefore(expiry) && tokenRef.get() != null) {
            tokenRef.get()!!
        } else {
            LoggerUtils.log("Token missing or expired. Fetching new token...")
            val newToken = tokenInitializer.retrieveVaultToken()
            setToken(newToken)
            newToken
        }
    }

    private fun setToken(token: String) {
        tokenRef.set(token)
        expiryRef.set(Instant.now().plusSeconds(ttlMinutes * 60))
        LoggerUtils.log("New token set. Expires at: ${expiryRef.get()}")
    }
}