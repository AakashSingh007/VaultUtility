package com.aakash.vaultutility.vaultConfig

import com.aakash.vaultutility.networking.VaultCommonNetworkingClient
import com.aakash.vaultutility.utils.*
import org.springframework.core.env.Environment
import org.json.JSONObject
import org.springframework.stereotype.Component

@Component
class TokenInitializer(
    val vaultCommonNetworkingClient: VaultCommonNetworkingClient,
    val env: Environment,
) {
    fun retrieveVaultToken(): String {
        val roleId = env.getProperty(ENV_ROLE_ID)!!
        val secretId = env.getProperty(ENV_SECRET_ID)!!
        val vaultUrl = env.getProperty(ENV_VAULT_URL)!!
        val loginBase = env.getProperty(ENV_VAULT_LOGIN_BASE)!!

        val jsonBody = JSONObject().apply {
            put(ROLE_ID, roleId)
            put(SECRET_ID, secretId)
        }

        val loginUrl = "$vaultUrl/$loginBase"
        val response = vaultCommonNetworkingClient.NewRequest()
            .postCall(loginUrl, jsonBody)
            .send()

        val responseBody = response.stringEntity
        val token = JSONObject(responseBody).optJSONObject("auth").optString("client_token")
        println("Token retrieved successfully")
        return token
    }
}