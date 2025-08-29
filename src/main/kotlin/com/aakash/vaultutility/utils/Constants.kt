package com.aakash.vaultutility.utils

const val VAULT_TOKEN_HEADER = "X-Vault-Token"
const val ROLE_ID = "role_id"
const val SECRET_ID = "secret_id"
const val ENV_VAULT_URL = "vault.vault-url"
const val ENV_VAULT_LOGIN_BASE = "vault.login-base"
const val ENV_VAULT_SECRET_BASE = "vault.secret-base"
const val ENV_ROLE_ID = "vault.role-id"
const val ENV_SECRET_ID = "vault.secret-id"
const val VAULT_TOKEN = "vault.token"
const val ENCRYPT_DECRYPT_REQUEST_KEY = "data"
const val PRIMARY_DB = "Primary_DB"
const val SECONDARY_DB = "Secondary_DB"
const val CMS = "cms"

const val NA = "NA"

enum class Errors(val value: String) {
    UNKNOWN("UNKNOWN"),
    FAILED("FAILED"),
    INVALID_CREDENTIALS("INVALID_CREDENTIALS"),
    RESOURCE_NOT_FOUND("RESOURCE_NOT_FOUND"),
    ACCESS_DENIED("ACCESS_DENIED"),
    UNAUTHORIZED_ACCESS("UNAUTHORIZED_ACCESS"),
    DUPLICATE_RECORD("DUPLICATE_RECORD"),
    STRING_TOO_LONG("STRING_TOO_LONG"),
    JSON_PARSER_ERROR("JSON_PARSER_ERROR"),
    OPERATION_FAILED("OPERATION_FAILED"),
    INVALID_DATA("INVALID_DATA"),
    INCONLUSIVE("INCONLUSIVE");
}