package app.pay.panda.helperclasses

import android.util.Log
import app.pay.panda.retrofit.Constant
import com.google.gson.Gson
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import javax.crypto.spec.IvParameterSpec

object AesEncrypt {

    // Encrypt object using AES with the given key and IV
    private fun encryptObject(obj: Any, key: String, iv: String): String? {
        return try {
            val serializedObject = Gson().toJson(obj)
            Log.e("TAG", "encryptObject: => $serializedObject")

            val keyBytes = hexStringToByteArray(key)
            val ivBytes = hexStringToByteArray(iv)

            if (keyBytes.size != 32) {
                throw IllegalArgumentException("Invalid key length (must be 32 characters in hex)")
            }

            if (ivBytes.size != 16) {
                throw IllegalArgumentException("Invalid IV length (must be 16 characters in hex)")
            }

            val keySpec = SecretKeySpec(keyBytes, "AES")
            val ivSpec = IvParameterSpec(ivBytes)
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec)

            val encryptedByteArray = cipher.doFinal(serializedObject.toByteArray(Charsets.UTF_8))
            // Convert encrypted bytes to hex string
            byteArrayToHexString(encryptedByteArray)
        } catch (e: Exception) {
            Log.e("TAG", "Encryption error: ${e.message}")
            e.printStackTrace()
            null
        }
    }

    // Decrypt object using AES with the given key and IV
    fun decryptObject(encryptedData: String, key: String, iv: String): Any? {
        return try {
            val keyBytes = hexStringToByteArray(key)
            val ivBytes = hexStringToByteArray(iv)

            if (keyBytes.size != 32) {
                throw IllegalArgumentException("Invalid key length (must be 32 characters in hex)")
            }

            if (ivBytes.size != 16) {
                throw IllegalArgumentException("Invalid IV length (must be 16 characters in hex)")
            }

            val encryptedBytes = hexStringToByteArray(encryptedData)
            val keySpec = SecretKeySpec(keyBytes, "AES")
            val ivSpec = IvParameterSpec(ivBytes)
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec)

            val decryptedByteArray = cipher.doFinal(encryptedBytes)
            val decryptedString = String(decryptedByteArray, Charsets.UTF_8)

            // Deserialize JSON back into an object
            Gson().fromJson(decryptedString, Any::class.java)
        } catch (e: Exception) {
            Log.e("TAG", "Decryption error: ${e.message}")
            e.printStackTrace()
            null
        }
    }

    // Convert hex string to byte array
    private fun hexStringToByteArray(hexString: String): ByteArray {
        val len = hexString.length
        val data = ByteArray(len / 2)
        for (i in 0 until len / 2) {
            data[i] = ((Character.digit(hexString[i * 2], 16) shl 4) + Character.digit(hexString[i * 2 + 1], 16)).toByte()
        }
        return data
    }

    // Convert byte array to hex string
    private fun byteArrayToHexString(bytes: ByteArray): String {
        val sb = StringBuilder()
        for (b in bytes) {
            sb.append(String.format("%02x", b))
        }
        return sb.toString()
    }

    // AES encryption for general objects
    fun encodeObj(obj: Any): Any {
        val dataStr = encryptObject(obj, Constant.AES_KEY, Constant.AES_IV)
        val encodeObj = hashMapOf<String, Any?>()
        encodeObj["encode"] = dataStr
        return encodeObj
    }

    // AES encryption for TPIN
    fun encodeaesObj(obj: Any): Any {
        val dataStr = encryptObject(obj, Constant.AES256CBC_KEY_TPIN, Constant.AES256CBC_IV_TPIN)
        val encodeObj = hashMapOf<String, Any?>()
        encodeObj["enData"] = dataStr
        return encodeObj
    }
}
