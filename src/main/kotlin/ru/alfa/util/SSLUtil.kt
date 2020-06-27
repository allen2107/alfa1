package ru.alfa.util

import java.io.InputStream
import java.security.KeyStore
import javax.net.ssl.*

class SSLUtil {

    fun getClientSSLSocketFactory(tStorefile: InputStream,
                                  kStorefile: InputStream, theKeyStorePassword: String): SSLSocketFactory
    {
        // This supports TLSv1.2
        val sslContext: SSLContext = SSLContext.getInstance("TLS")
        val tStore: KeyStore = KeyStore.getInstance(KeyStore.getDefaultType())
        tStore.load(tStorefile, theKeyStorePassword.toCharArray())
        val tmf: TrustManagerFactory = TrustManagerFactory
                .getInstance(TrustManagerFactory.getDefaultAlgorithm())
        tmf.init(tStore)
        val kStore: KeyStore = KeyStore.getInstance(KeyStore.getDefaultType())
        var keyManagers: Array<KeyManager?>?
        kStore.load(kStorefile, theKeyStorePassword.toCharArray())
        val kmf: KeyManagerFactory = KeyManagerFactory
                .getInstance(KeyManagerFactory.getDefaultAlgorithm())
        kmf.init(kStore, theKeyStorePassword.toCharArray())
        keyManagers = kmf.keyManagers
        if (keyManagers == null) {
            // 2-way TLS not required. Let JVM uses its default
            keyManagers = arrayOf()
        }
        sslContext.init(keyManagers, tmf.trustManagers, null)
        return sslContext.socketFactory
    }
}
