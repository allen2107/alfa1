package ru.alfa.config

import feign.Client
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import ru.alfa.util.SSLUtil
import javax.net.ssl.SSLSocketFactory

@Configuration
class FeignConfiguration {
    @Bean
    fun getfeignClient(): Client {
        return Client.Default(
                getSSLSocketFactory(),
                null
        )
    }

    private fun getSSLSocketFactory(): SSLSocketFactory? {
        return try {
            SSLUtil().getClientSSLSocketFactory(
                    ClassPathResource("truststore.jks").inputStream,
                    ClassPathResource("alfabattle.p12").inputStream,
                    "alfapass"
            )
        } catch (exception: Exception) {
            throw RuntimeException(exception)
        }
    }
}
