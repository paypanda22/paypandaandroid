package app.pay.pandapro.helperclasses

import android.app.Activity
import android.util.Base64
import com.google.gson.Gson

import javax.crypto.spec.SecretKeySpec
import javax.crypto.Mac


class GetKeyHash(val activity: Activity) {
    private val key = "325566674364381861A37D259D5F1";

    /*private fun printKeyHash(context: Context): String {
        var key = ""
        try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, PackageManager.GET_SIGNATURES)
            for (signature in packageInfo.signatures) {
                val messageDigest = MessageDigest.getInstance("SHA")
                messageDigest.update(signature.toByteArray())

                // Getting SHA1 key
                key = Base64.encodeToString(messageDigest.digest(), Base64.DEFAULT)
                // Log.e("KeyHash:", "SHA1: $sha1Key")

//                // For SHA256, you just need to change the instance from "SHA" to "SHA-256"
//                val sha256Digest = MessageDigest.getInstance("SHA-256")
//                val sha256Key = Base64.encodeToString(sha256Digest.digest(signature.toByteArray()), Base64.DEFAULT)
//                Log.e("KeyHash:", "SHA256: $sha256Key")
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            key = ""
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            key = ""
        }
        return key
    }*/

   /* fun printKeyHashHex(context: Context) {
        try {
            val packageInfo = context.packageManager.getPackageInfo(
                context.packageName,
                PackageManager.GET_SIGNATURES
            )
            for (signature in packageInfo.signatures) {
                val sha1Digest = MessageDigest.getInstance("SHA")
                val sha1Hash = sha1Digest.digest(signature.toByteArray())
                val sha1Hex = sha1Hash.joinToString(separator = ":") { byte -> "%02X".format(byte) }

                //Log.d("KeyHash:", "SHA1: $sha1Hex")

                val sha256Digest = MessageDigest.getInstance("SHA-256")
                val sha256Hash = sha256Digest.digest(signature.toByteArray())
                val sha256Hex = sha256Hash.joinToString(separator = ":") { byte -> "%02X".format(byte) }

               // Log.d("KeyHash:", "SHA256: $sha256Hex")
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
    }*/

    fun getHash(requestData:HashMap<String,Any?>): HashMap<String,Any?> {
        val gson = Gson()
        val json = gson.toJson(requestData)
       // Log.e("TAG", "PayLoad: $json")
        val base64EncodedString = Base64.encodeToString(json.toByteArray(Charsets.UTF_8), Base64.NO_WRAP)
       // Log.e("TAG", "Base64: $base64EncodedString" )

        val secretKeySpec = SecretKeySpec(key.toByteArray(Charsets.UTF_8), "HmacSHA256")
        val mac = Mac.getInstance("HmacSHA256").apply {
            init(secretKeySpec)
        }

        val hashBytes = mac.doFinal(base64EncodedString.toByteArray(Charsets.UTF_8))
        val checkSumData=hashMapOf<String,Any?>()
        checkSumData["base64"]=base64EncodedString
        checkSumData["checkSum"]=Base64.encodeToString(hashBytes, Base64.NO_WRAP)
        return checkSumData
    }

}