package com.aakash.vaultutility.utils
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "vault")
class VaultProperties {
    lateinit var partnerPathMap: Map<String, String>
}