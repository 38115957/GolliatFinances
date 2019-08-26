package com.example.golliatfinances.Modelo
import java.math.BigDecimal
import java.time.LocalDate

//import org.threeten.bp.LocalDateTime

class Pago(val fecha: LocalDate, val montoPagado: BigDecimal){

    override fun toString(): String {
        return "\nPago(fecha=$fecha, montoPagado=$montoPagado)"
    }
}