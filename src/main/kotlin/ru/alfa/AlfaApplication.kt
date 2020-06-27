package ru.alfa

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import ru.alfa.client.AlfaRestClient

@SpringBootApplication
@EnableFeignClients(basePackageClasses = [AlfaRestClient::class])
class AlfaApplication

fun main(args: Array<String>) {
	runApplication<AlfaApplication>(*args)
}
