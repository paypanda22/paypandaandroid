package app.pay.pandapro.retrofit

import app.pay.pandapro.retrofit.Constant.Image_Base_URL

import app.pay.pandapro.retrofit.Constant.base_url
import okhttp3.ConnectionPool
import okhttp3.Dns
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit


object RetrofitFactory {
    fun getRetrofitInstance(): Retrofit {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val newRequest = chain.request().newBuilder().build()
                chain.proceed(newRequest)
            }
            .addInterceptor(loggingInterceptor)
            .dns(Dns.SYSTEM)
            .connectionPool(ConnectionPool(5, 5, TimeUnit.MINUTES))
            .readTimeout(160, TimeUnit.SECONDS)
            .writeTimeout(160, TimeUnit.SECONDS)
            .connectTimeout(160, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(base_url)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }


    fun getRetrofitInstanceWithToken(token :String): Retrofit {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY

        }
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val newRequest = chain.request().newBuilder()
                    .addHeader("Authorization","Bearer $token")

                    .build()
                chain.proceed(newRequest)
            }

            .addInterceptor(loggingInterceptor)
            .readTimeout(160, TimeUnit.SECONDS)
            .writeTimeout(160, TimeUnit.SECONDS)
            .connectTimeout(160, TimeUnit.SECONDS)
            .build()
        return Retrofit.Builder()
            .baseUrl(base_url)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)

            .build()
    }

//    fun getRetrofitInstanceTest(context: Context): Retrofit? {
//        return try {
//            val certInputStream: InputStream = context.resources.openRawResource(R.raw.pp_cert)
//
//            val certificateFactory = CertificateFactory.getInstance("X.509")
//
//            val certificate = certificateFactory.generateCertificate(certInputStream)
//
//            val keyStore = KeyStore.getInstance(KeyStore.getDefaultType()).apply {
//                load(null, null)
//                setCertificateEntry("pp_cert", certificate)
//            }
//
//            val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm()).apply {
//                init(keyStore)
//            }
//
//            val trustManager = trustManagerFactory.trustManagers[0] as X509TrustManager
//
//            val sslContext = SSLContext.getInstance("TLS").apply {
//                init(null, arrayOf(trustManager), null)
//            }
//            val loggingInterceptor = HttpLoggingInterceptor().apply {
//                level = HttpLoggingInterceptor.Level.BODY
//            }
////            val cacheSize = (5 * 1024 * 1024).toLong() // 5 MB cache
////            val cache = Cache(context.cacheDir, cacheSize)
//            val okHttpClient = OkHttpClient.Builder()
//                .sslSocketFactory(sslContext.socketFactory, trustManager)
//                .hostnameVerifier { hostname, _ ->
//                    hostname.equals("devapi.paypanda.in", ignoreCase = true)
//                }
//                .addInterceptor { chain ->
//                    val newRequest = chain.request().newBuilder()
//                        .build()
//                    chain.proceed(newRequest)
//                }
//                .addInterceptor(loggingInterceptor)
//                .dns(Dns.SYSTEM)
//                .connectionPool(ConnectionPool(5, 5, TimeUnit.MINUTES))
//                .readTimeout(30, TimeUnit.SECONDS)
//                .writeTimeout(30, TimeUnit.SECONDS)
//                .connectTimeout(30, TimeUnit.SECONDS)
//                .build()
//
//            Retrofit.Builder()
//                .baseUrl(MainBase_URL)
//                .client(okHttpClient)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build()
//
//        } catch (e: Exception) {
//            e.printStackTrace()
//            null
//        }
//    }

}
