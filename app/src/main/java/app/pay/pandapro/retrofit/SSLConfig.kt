import android.content.Context
import app.pay.pandapro.R
import java.security.KeyStore
import java.security.cert.Certificate
import java.security.cert.CertificateFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

object SSLConfig {
    private lateinit var appContext: Context

    // Initialize this in your Application class
    fun init(context: Context) {
        appContext = context.applicationContext
    }

    fun getSSLSocketFactory(): SSLParams {
        val cf = CertificateFactory.getInstance("X.509")
        val caInput = appContext.resources.openRawResource(R.raw.your_certificate) // Use your certificate

        val ca: Certificate = caInput.use {
            cf.generateCertificate(it)
        }

        val keyStoreType = KeyStore.getDefaultType()
        val keyStore = KeyStore.getInstance(keyStoreType)
        keyStore.load(null, null)
        keyStore.setCertificateEntry("ca", ca)

        val tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm()
        val tmf = TrustManagerFactory.getInstance(tmfAlgorithm)
        tmf.init(keyStore)

        val trustManager = tmf.trustManagers[0] as X509TrustManager

        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, arrayOf(trustManager), null)

        return SSLParams(sslContext.socketFactory, trustManager)
    }

    // Data class to hold SSL parameters
    data class SSLParams(val sslSocketFactory: SSLSocketFactory, val trustManager: X509TrustManager)
}

