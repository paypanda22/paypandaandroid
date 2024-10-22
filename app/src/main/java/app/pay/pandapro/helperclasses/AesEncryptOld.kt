package app.pay.pandapro.helperclasses
import android.util.Base64
import app.pay.pandapro.retrofit.Constant
import com.google.gson.Gson
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import javax.crypto.spec.IvParameterSpec
object AesEncryptOld {

    fun encryptObject(obj: Any, key: String, iv: String): String? {
        try {
            val serializedObject = Gson().toJson(obj) // Assuming obj has a proper toJson method
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
            return Base64.encodeToString(encryptedByteArray, Base64.DEFAULT)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }


    fun decryptObject(encryptedData: String, key: String, iv: String): Any? {
        try {
            val keyBytes = hexStringToByteArray(key)
            val ivBytes = hexStringToByteArray(iv)

            if (keyBytes.size != 32) {
                throw IllegalArgumentException("Invalid key length (must be 32 characters in hex)")
            }

            if (ivBytes.size != 16) {
                throw IllegalArgumentException("Invalid IV length (must be 16 characters in hex)")
            }

            val decodedData = Base64.decode(encryptedData, Base64.DEFAULT)
            val keySpec = SecretKeySpec(keyBytes, "AES")
            val ivSpec = IvParameterSpec(ivBytes)
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec)
            val decryptedByteArray = cipher.doFinal(decodedData)
            val decryptedString = String(decryptedByteArray, Charsets.UTF_8)
            return Gson().fromJson(decryptedString, Any::class.java) // Assuming your object type is unknown
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    private fun hexStringToByteArray(hexString: String): ByteArray {
        val len = hexString.length
        val data = ByteArray(len / 2)
        for (i in 0 until len / 2) {
            data[i] = ((Character.digit(hexString[i * 2], 16) shl 4) + Character.digit(hexString[i * 2 + 1], 16)).toByte()
        }
        return data
    }


    // AES encryption for general objects
    fun encodeObj(obj: Any): Any {
        val dataStr = AesEncryptOld.encryptObject(obj, Constant.AES_KEY, Constant.AES_IV)
        val encodeObj = hashMapOf<String, Any?>()
        encodeObj["encode"] = dataStr
        return encodeObj
    }


}