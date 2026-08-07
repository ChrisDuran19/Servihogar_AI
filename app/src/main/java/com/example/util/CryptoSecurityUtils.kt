package com.example.util

import android.util.Base64
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

object CryptoSecurityUtils {
    private const val ALGORITHM = "AES"
    private const val SECRET_KEY_SEED = "ServiHogarAI_Banking_AES256_Secure_TokenKey_2026"

    private fun getSecretKey(): SecretKeySpec {
        val digest = MessageDigest.getInstance("SHA-256")
        val bytes = digest.digest(SECRET_KEY_SEED.toByteArray(Charsets.UTF_8))
        return SecretKeySpec(bytes, ALGORITHM)
    }

    /**
     * Encrypts sensitive plaintext (OTP code, Cédula ID, password, account token).
     */
    fun encryptData(plainText: String): String {
        if (plainText.isEmpty()) return ""
        return try {
            val cipher = Cipher.getInstance(ALGORITHM)
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey())
            val encryptedBytes = cipher.doFinal(plainText.toByteArray(Charsets.UTF_8))
            Base64.encodeToString(encryptedBytes, Base64.NO_WRAP)
        } catch (e: Exception) {
            plainText
        }
    }

    /**
     * Decrypts encrypted ciphertext back into original plaintext.
     */
    fun decryptData(cipherText: String): String {
        if (cipherText.isEmpty()) return ""
        return try {
            val cipher = Cipher.getInstance(ALGORITHM)
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey())
            val decodedBytes = Base64.decode(cipherText, Base64.NO_WRAP)
            val decryptedBytes = cipher.doFinal(decodedBytes)
            String(decryptedBytes, Charsets.UTF_8)
        } catch (e: Exception) {
            cipherText
        }
    }

    /**
     * Generates a random 6-digit verification code.
     */
    fun generateDynamicPin(): String {
        return (100000..999999).random().toString()
    }

    /**
     * Hashes string with SHA-256 for secure hashing verification.
     */
    fun sha256Hash(input: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(input.toByteArray(Charsets.UTF_8))
        return bytes.joinToString("") { "%02x".format(it) }
    }
}
