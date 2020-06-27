package ru.alfa.service

import mu.KLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import ru.alfa.client.AlfaRestClient
import ru.alfa.dto.AtmDto
import ru.alfa.dto.SourceAtmDto
import ru.alfa.exception.AtmNotFoundException
import java.time.LocalDateTime

@Service
class AtmService(
        private val alfaRestClient: AlfaRestClient
) {
    companion object: KLogging()

    @Value("\${application.client.alfa.client-id}")
    lateinit var clientId: String

    private var atmsMap: Map<Long, SourceAtmDto> = mapOf()
    private var syncDt = LocalDateTime.now()

    fun fetchAtms(): Map<Long, SourceAtmDto> {
        synchronized(syncDt) {
            if (atmsMap.isEmpty() || syncDt.plusMinutes(10L).isBefore(LocalDateTime.now())) {
                logger.info("fetching atms!")
                atmsMap = alfaRestClient.getAtms(clientId).data.atms
                        .map { it.deviceId to it }.toMap()
            } else {
                logger.info("atms cached!")
            }
        }
        return atmsMap
    }

    fun getNearestAtm(): List<SourceAtmDto> {
        throw UnsupportedOperationException("Not supported yet.")
    }

    fun getNearestAtmWithAlfik(): Any {
        throw UnsupportedOperationException("Not supported yet.")
    }

    fun getAtmById(id: String) =
            try {
                fetchAtms().getValue(id.toLong()).toAtmDto()
            } catch (e: Exception) {
                throw AtmNotFoundException()
            }

    fun SourceAtmDto.toAtmDto() =
            AtmDto(
                    deviceId,
                    coordinates.getValue("latitude"),
                    coordinates.getValue("longitude"),
                    address.getValue("city"),
                    address.getValue("location"),
                    services.getValue("payments") == "Y"
            )
}
