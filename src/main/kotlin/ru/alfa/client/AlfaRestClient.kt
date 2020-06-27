package ru.alfa.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import ru.alfa.config.FeignConfiguration
import ru.alfa.dto.GetAtmsResponse
import ru.alfa.dto.SourceAtmDto


@FeignClient(
        name="alfa-rest-client",
        url="\${application.client.alfa.url}",
        configuration = [FeignConfiguration::class]
)
interface AlfaRestClient {
    @GetMapping("/atm-service/atms")
//    fun getAtms(@RequestHeader("x-ibm-client-id") clientId: String): List<SourceAtmDto>
    fun getAtms(@RequestHeader("x-ibm-client-id") clientId: String): GetAtmsResponse
}
