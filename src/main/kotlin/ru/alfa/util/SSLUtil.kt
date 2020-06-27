package ru.alfa.util

import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.InputStream
import java.security.*
import javax.net.ssl.*

class SSLUtil {

    fun getClientSSLSocketFactory(theTrustStoreFile: InputStream,
                                          theKeyStoreFile: InputStream, theKeyStorePassword: String): SSLSocketFactory {
        // This supports TLSv1.2
        val sslContext: SSLContext = SSLContext.getInstance("TLS")
        val tStore: KeyStore = KeyStore.getInstance(KeyStore.getDefaultType())
//        val tStorefile = getFileInputStream(theTrustStoreFile)
        val tStorefile = theTrustStoreFile
        tStore.load(tStorefile, theKeyStorePassword.toCharArray())
        val tmf: TrustManagerFactory = TrustManagerFactory
                .getInstance(TrustManagerFactory.getDefaultAlgorithm())
        tmf.init(tStore)
        val kStore: KeyStore = KeyStore.getInstance(KeyStore.getDefaultType())
        var keyManagers: Array<KeyManager?>?
//        val kStorefile = getFileInputStream(theKeyStoreFile)
        val kStorefile = theKeyStoreFile
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

    @Throws(PrivilegedActionException::class)
    private fun getFileInputStream(file: File): FileInputStream? {
        return AccessController.doPrivileged(PrivilegedExceptionAction<FileInputStream?> {
            try {
                return@PrivilegedExceptionAction if (file.exists()) {
                    FileInputStream(file)
                } else {
                    null
                }
            } catch (e: FileNotFoundException) {
                // couldn't find it, oh well.
                return@PrivilegedExceptionAction null
            }
        })
    }

}
