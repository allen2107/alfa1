package ru.alfa.api.rest

import org.springframework.web.bind.annotation.*
import ru.alfa.service.AtmService

@RestController
@CrossOrigin
@RequestMapping("/atms")
class AtmController(
        private val atmService: AtmService
) {
    @RequestMapping(value = ["/nearest"], method = [RequestMethod.GET])
    fun getNearestAtm(
            @RequestParam(required = false, defaultValue = "") latitude: String,
            @RequestParam(required = false, defaultValue = "") longitude: String,
            @RequestParam(required = false, defaultValue = "false") payments: Boolean
    ) = atmService.getNearestAtm(latitude, longitude, payments)

    @RequestMapping(value = ["/nearest-with-alfik"], method = [RequestMethod.GET])
    fun getNearestAtmWithAlfik() =
        atmService.getNearestAtmWithAlfik()

    @RequestMapping(value = ["/{id}"], method = [RequestMethod.GET])
    fun signUp(@PathVariable id: String) =
        atmService.getAtmById(id)

}
