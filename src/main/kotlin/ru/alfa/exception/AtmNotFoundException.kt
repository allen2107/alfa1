package ru.alfa.exception

import java.lang.RuntimeException

class AtmNotFoundException(msg: String = "atm not found"): RuntimeException(msg)
