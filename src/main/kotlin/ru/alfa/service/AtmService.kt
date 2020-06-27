package ru.alfa.service

import mu.KLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import ru.alfa.client.AlfaRestClient
import ru.alfa.dto.AtmDto
import ru.alfa.dto.SourceAtmDto
import ru.alfa.exception.AtmNotFoundException
import java.math.BigDecimal
import java.time.LocalDateTime
import kotlin.math.sqrt

@Service
class AtmService(
        private val alfaRestClient: AlfaRestClient
) {
    companion object: KLogging()

    @Value("\${application.client.alfa.client-id}")
    lateinit var clientId: String

    private var atmsMap: Map<Long, AtmDto> = mapOf()
    private var syncDt = LocalDateTime.now()

    fun fetchAtms(): Map<Long, AtmDto> {
        synchronized(syncDt) {
            if (atmsMap.isEmpty() || syncDt.plusMinutes(10L).isBefore(LocalDateTime.now())) {
                logger.info("fetching atms!")
                atmsMap = alfaRestClient.getAtms(clientId).data.atms
                        .map { it.deviceId to it.toAtmDto() }.toMap()
            } else {
                logger.info("atms cached!")
            }
        }
        logger.info{"SIZEATMS: $atmsMap.size"}
        return atmsMap
    }

    fun getNearestAtm(latitude: String, longitude: String, payments: Boolean): AtmDto {
        val numericLatitude = latitude.toDouble()
        val numericLongitude = longitude.toDouble()
        var nearest: AtmDto? = null
        var nearestDistance: Double? = null
        fetchAtms().values.forEach {
            if ((!payments || it.payments) && it.latitude != null && it.longitude != null){
                val distance = calculateAtmDistance(numericLatitude, numericLongitude, it)
                if (nearestDistance == null || distance < nearestDistance!!) {
                    nearest = it
                    nearestDistance = distance
                }
            }
        }
        return nearest!!
    }

    fun calculateAtmDistance(latitude: Double, longitude: Double, atm: AtmDto) =
            sqrt(sqr(latitude - atm.latitude!!.toDouble()) + sqr(longitude - atm.longitude!!.toDouble()))

    fun sqr(x: Double) = x * x

    fun getNearestAtmWithAlfik(): Any {
        throw UnsupportedOperationException("Not supported yet.")
    }

    fun getAtmById(id: String) =
            try {
                fetchAtms().getValue(id.toLong())
            } catch (e: Exception) {
                logger.error("ERROR", e)
                throw AtmNotFoundException()
            }

    fun SourceAtmDto.toAtmDto() =
            AtmDto(
                    deviceId,
                    coordinates["latitude"],
                    coordinates["longitude"],
                    address.getValue("city"),
                    address.getValue("location"),
                    services.getValue("payments") == "Y"
            )
}
