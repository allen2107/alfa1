package ru.alfa.api.rest.error

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import ru.alfa.dto.ErrorResponse
import ru.alfa.exception.AtmNotFoundException


@ControllerAdvice
class RestResponseEntityExceptionHandler: ResponseEntityExceptionHandler() {

    @ExceptionHandler(value = [AtmNotFoundException::class])
    protected fun handleConflict(ex: AtmNotFoundException, request: WebRequest) =
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse(ex.message!!))
}
