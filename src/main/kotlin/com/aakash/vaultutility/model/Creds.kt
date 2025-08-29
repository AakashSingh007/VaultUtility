package com.aakash.vaultutility.model

data class Creds(
    val partnerName: String? = null,
    val credType: String? = null,
    val username: String? = null,
    val password: String? = null,
    val memberId: String? = null,
    val memberPasscode: String? = null,
    val salt: String? = null,
    val apiKey: String? = null,
    val apiUrl: String? = null,
    val isValid: Boolean = true,
    val isEncrypted: Boolean = false
)
