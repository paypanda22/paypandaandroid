import android.util.Base64
import app.pay.retailers.retrofit.Constant
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher

object RsaEncrypt {

    private const val RSA_ALGORITHM = "RSA/ECB/OAEPPadding"

    fun encryptTpinWithRSA(tpin: String): String? {
        return try {
            val publicKeyStr = Constant.RSA256_PUBLICKEY

            if (publicKeyStr.isNullOrBlank()) {
                throw IllegalArgumentException("Public key is null or empty")
            }

            // Clean and decode the public key string
            val cleanPublicKeyStr = publicKeyStr
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replace("\n", "")
            val publicKeyBytes = Base64.decode(cleanPublicKeyStr, Base64.NO_WRAP)  // Use NO_WRAP to avoid newlines

            // Generate the PublicKey object
            val keySpec = X509EncodedKeySpec(publicKeyBytes)
            val keyFactory = KeyFactory.getInstance("RSA")
            val publicKey: PublicKey = keyFactory.generatePublic(keySpec)

            // Encrypt the TPIN using the public key with OAEP padding
            val cipher = Cipher.getInstance(RSA_ALGORITHM)
            cipher.init(Cipher.ENCRYPT_MODE, publicKey)
            val encryptedBytes = cipher.doFinal(tpin.toByteArray(Charsets.UTF_8))

            // Encode encrypted bytes to Base64 string with NO_WRAP to avoid newlines
            Base64.encodeToString(encryptedBytes, Base64.NO_WRAP).trim()
        } catch (e: Exception) {
            e.printStackTrace()
            "Encryption Error: ${e.message}"
        }
    }

    fun decryptDataWithRSA(encryptedData: String, privateKeyStr: String): String? {
        return try {
            // Clean and decode the private key string
            val cleanPrivateKeyStr = privateKeyStr
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replace("\n", "")
            val privateKeyBytes = Base64.decode(cleanPrivateKeyStr, Base64.NO_WRAP)  // Use NO_WRAP to avoid newlines

            // Generate the PrivateKey object using PKCS8EncodedKeySpec
            val keySpec = PKCS8EncodedKeySpec(privateKeyBytes)
            val keyFactory = KeyFactory.getInstance("RSA")
            val privateKey: PrivateKey = keyFactory.generatePrivate(keySpec)

            // Decode the Base64 encoded encrypted data
            val encryptedBytes = Base64.decode(encryptedData, Base64.NO_WRAP)  // Use NO_WRAP to avoid newlines

            // Decrypt the data using the private key
            val cipher = Cipher.getInstance(RSA_ALGORITHM)
            cipher.init(Cipher.DECRYPT_MODE, privateKey)
            val decryptedBytes = cipher.doFinal(encryptedBytes)

            // Return decrypted data as a string
            String(decryptedBytes, Charsets.UTF_8)
        } catch (e: Exception) {
            e.printStackTrace()
            "Decryption Error: ${e.message}"
        }
    }
}
