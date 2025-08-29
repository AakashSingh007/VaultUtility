package com.aakash.vaultutility.utils

import java.util.logging.Logger

object LoggerUtils {

    private val logger: Logger = Logger.getLogger(LoggerUtils::class.java.simpleName)

    fun log(value: String) {
        logger.info("\n\nVaultUtility - Value --> $value\n\n")
    }

    fun logBody(body: String) {
        logger.info("\n\nVaultUtility - Received body --> $body\n\n")
    }

    fun logMethodCall(value: String) {
        logger.info("\nVaultUtility -\n----------------------\n  Method --> $value  \n----------------------\n\n")
    }

    fun printLog(value: String) {
        println("\n\nVaultUtility - Value --> $value\n\n")
    }

}