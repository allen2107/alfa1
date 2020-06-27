package ru.alfa.config

import feign.Client
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.util.ResourceUtils
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
//                    ResourceUtils.getFile("classpath:truststore.jks"),
//                    ResourceUtils.getFile("classpath:alfabattle.p12"),
                    "alfapass"
            )
        } catch (exception: Exception) {
            throw RuntimeException(exception)
        }
    }
}
