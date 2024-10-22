package app.pay.pandapro.helperclasses

import okhttp3.OkHttpClient
import java.security.KeyFactory
import java.security.PublicKey
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import android.util.Base64
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import java.io.IOException
import java.util.concurrent.TimeUnit
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
object SecureApiClient {


    private val key = "d31f454d72ca36e9c645c7ef2b29face23c6413a558dbef00a18fc2a58e4f8db"
    private val iv = "6bdf806c2ead6f3983e2042418434426"

    private val client: OkHttpClient

    init {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val originalRequest = chain.request()
                val originalBody = originalRequest.body

                // Assuming request data is in JSON format
                val bodyString = originalBody?.let {
                    val buffer = okio.Buffer()
                    it.writeTo(buffer)
                    buffer.readUtf8()
                } ?: ""

                // Encrypt the request body using AES
                val encryptedBody = encryptAES(bodyString, key, iv)

                // Replace the request body with encrypted data
                val newBody = RequestBody.create(
                    "application/json".toMediaType(),  // Use toMediaType() extension
                    JSONObject(mapOf("encode" to encryptedBody)).toString()
                )

                val newRequest = originalRequest.newBuilder()
                    .method(originalRequest.method, newBody)
                    .build()

                chain.proceed(newRequest)
            }
            .addInterceptor(loggingInterceptor)
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build()
    }

    // AES Encryption function
    private fun encryptAES(text: String, key: String, iv: String): String {
        val secretKey = SecretKeySpec(hexStringToByteArray(key), "AES")
        val ivSpec = IvParameterSpec(hexStringToByteArray(iv))

        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec)

        val encryptedBytes = cipher.doFinal(text.toByteArray(Charsets.UTF_8))
        return Base64.encodeToString(encryptedBytes, Base64.NO_WRAP)
    }

    // RSA Encryption function
    fun encryptRSA(plainText: String, publicKeyString: String): String {
        val publicKeyBytes = Base64.decode(publicKeyString, Base64.DEFAULT)
        val keySpec = X509EncodedKeySpec(publicKeyBytes)
        val keyFactory = KeyFactory.getInstance("RSA")
        val publicKey: PublicKey = keyFactory.generatePublic(keySpec)

        val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)

        val encryptedBytes = cipher.doFinal(plainText.toByteArray(Charsets.UTF_8))
        return Base64.encodeToString(encryptedBytes, Base64.NO_WRAP)
    }

    // Helper function to convert a hex string to byte array
    private fun hexStringToByteArray(s: String): ByteArray {
        val len = s.length
        val data = ByteArray(len / 2)
        var i = 0
        while (i < len) {
            data[i / 2] = ((Character.digit(s[i], 16) shl 4) + Character.digit(s[i + 1], 16)).toByte()
            i += 2
        }
        return data
    }

    // Example usage for making an API call
    fun makeApiRequest() {
        val request = Request.Builder()
            .url("https://your-api-url.com")
            .post(RequestBody.create(
                "application/json".toMediaType(),  // Use toMediaType() extension
                JSONObject(mapOf("yourKey" to "yourValue")).toString()
            ))
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace() // Handle failure
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    println("Response: $responseBody")
                }
            }
        })
    }

}