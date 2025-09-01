package com.aakash.vaultutility.vaultConfig

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.aakash.vaultutility.utils.*
import com.aakash.vaultutility.networking.VaultCommonNetworkingClient
import org.json.JSONObject
import org.springframework.boot.SpringApplication
import org.springframework.boot.env.EnvironmentPostProcessor
import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.core.env.PropertiesPropertySource
import java.util.*

class PostProcessor : EnvironmentPostProcessor {
    private val mapper = jacksonObjectMapper()
    private val vaultCommonNetworkingClient = VaultCommonNetworkingClient(mapper)

    override fun postProcessEnvironment(env: ConfigurableEnvironment, application: SpringApplication) {
        try {

            val useVault = env.getProperty("use_vault")?.toBoolean() ?: false
            println("Vault Utility useVault flag: $useVault")

            if (!useVault) {
                println("[Vault PostProcessor] Skipping Vault integration due to USE_VAULT=false")
                return
            }

            val tokenInitializer = TokenInitializer(vaultCommonNetworkingClient, env )

            val vaultUrl = env.getProperty(ENV_VAULT_URL)!!
            val vaultSecretBase = env.getProperty(ENV_VAULT_SECRET_BASE)!!
            val activeProfile = env.activeProfiles.first().uppercase()
            val appName = env.getProperty("vault.appName")!!

            val token = tokenInitializer.retrieveVaultToken()

            val props = Properties()

            val configuredPaths = env.getProperty("vault.secret-paths")?.split(",")?.map { it.trim() } ?: emptyList()

            val secretPaths = configuredPaths.map { "$it/$activeProfile" }

//            val secretPaths = listOf("$PRIMARY_DB/$activeProfile", "$SECONDARY_DB/$activeProfile")
            for (path in secretPaths) {
                val url = "$vaultUrl/$vaultSecretBase/$appName/$path"
                val secrets = fetchSecretFromVault(url, token)
                secrets.forEach { (key, value) -> props[key] = value }
            }

            env.propertySources.addFirst(PropertiesPropertySource("vaultSecrets", props))

        } catch (ex: Exception) {
            println("Error in Vault PostProcessor: ${ex.message}")
        }
    }

    private fun fetchSecretFromVault(url: String, token: String): Map<String, String> {
        val response = vaultCommonNetworkingClient.NewRequest()
            .addHeader("X-Vault-Token", token)
            .getCall(url)
            .send()

        val responseBody = response.stringEntity
        val json = JSONObject(responseBody).getJSONObject(DATA).getJSONObject(DATA)

        return mapper.readValue(json.toString(), object : TypeReference<Map<String, String>>() {})
    }
}
