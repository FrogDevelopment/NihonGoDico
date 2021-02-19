package fr.frogdevelopment.nihongo.dico.data.rest

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class RestServiceFactory private constructor() {
    private val retrofit: Retrofit

    companion object {
        private const val BASE_URL = "https://gateway.nihongo.frog-development.com/api/"
        private val INSTANCE = RestServiceFactory()

        private fun <C> getClient(client: Class<C>): C {
            return INSTANCE.retrofit.create(client)
        }

        val entriesClient: EntriesClient get() = getClient(EntriesClient::class.java)

        val sentencesClient: SentencesClient get() = getClient(SentencesClient::class.java)

        // Create a trust manager that does not validate certificate chains
        private val unsafeOkHttpClient:
        // Install the all-trusting trust manager
        // Create an ssl socket factory with our all-trusting manager
                OkHttpClient.Builder
            get() = try { // Create a trust manager that does not validate certificate chains
                val trustAllCerts = arrayOf<TrustManager>(
                        object : X509TrustManager {
                            @Throws(CertificateException::class)
                            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
                            }

                            @Throws(CertificateException::class)
                            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
                            }

                            override fun getAcceptedIssuers(): Array<X509Certificate> {
                                return arrayOf()
                            }
                        }
                )
                // Install the all-trusting trust manager
                val sslContext = SSLContext.getInstance("SSL")
                sslContext.init(null, trustAllCerts, SecureRandom())
                // Create an ssl socket factory with our all-trusting manager
                val sslSocketFactory = sslContext.socketFactory
                val builder = OkHttpClient.Builder()
                builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
                builder.hostnameVerifier { _, _ -> true }
                builder
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
    }

    init {
        val objectMapper = jacksonObjectMapper()
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .client(unsafeOkHttpClient.build())
                .build()
    }
}