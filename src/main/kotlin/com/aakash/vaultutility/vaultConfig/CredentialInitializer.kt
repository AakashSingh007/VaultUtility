package com.aakash.vaultutility.vaultConfig

import com.aakash.vaultutility.networking.VaultCommonNetworkingClient
import com.aakash.vaultutility.utils.*
import org.json.JSONObject
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component

@Component
class CredentialInitializer(
    val env: Environment,
    val vaultCommonNetworkingClient: VaultCommonNetworkingClient,
    val tokenManager: TokenManager,
    val vaultProperties: VaultProperties
) {

    fun getCredentials(partnerName: String, credType: String): String? {

        val vaultUrl = env.getProperty(ENV_VAULT_URL)!!
        val secretBase = env.getProperty(ENV_VAULT_SECRET_BASE)!!
        val vaultToken = tokenManager.getToken()
        val addr = vaultProperties.partnerPathMap[partnerName]

        if (vaultToken.isEmpty()) {
            LoggerUtils.log("getCredentials - No token available")
            return null
        }

        val credApiUrl = "$vaultUrl/$secretBase/$addr/$partnerName/$credType"

        val response = vaultCommonNetworkingClient.NewRequest()
            .addHeader(VAULT_TOKEN_HEADER, vaultToken)
            .getCall(credApiUrl)
            .send()

        if (!response.isSuccess) {
            LoggerUtils.log("getCredentials - Failed to fetch credentials: ${response.statusCode} ${response.message}")
            return null
        }

        val responseBody = response.stringEntity
        val json = JSONObject(responseBody).optJSONObject(DATA).optString(DATA)

        return json
    }
}
