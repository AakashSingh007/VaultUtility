package com.aakash.vaultutility.vaultConfig

import com.aakash.vaultutility.networking.VaultCommonNetworkingClient
import com.fasterxml.jackson.databind.ObjectMapper
import com.aakash.vaultutility.model.Creds
import com.aakash.vaultutility.utils.*
import org.json.JSONObject
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component

@Component
class CredentialInitializer(
    val env: Environment,
    val vaultCommonNetworkingClient: VaultCommonNetworkingClient,
    val mapper: ObjectMapper,
    val tokenManager: TokenManager,
    val vaultProperties: VaultProperties
) {

    fun getCredentials(partnerName: String, credType: String): Creds? {

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
        val json = JSONObject(responseBody).optJSONObject(ENCRYPT_DECRYPT_REQUEST_KEY).optString(ENCRYPT_DECRYPT_REQUEST_KEY)

        val cred = mapper.readValue(json, Creds::class.java)

        return cred
    }
}
