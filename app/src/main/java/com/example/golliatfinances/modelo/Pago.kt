package com.example.golliatfinances.modelo
import java.math.BigDecimal

import org.threeten.bp.LocalDate

class Pago(val fecha: LocalDate, val montoPagado: BigDecimal){

    override fun toString(): String {
        return "\nPago(fecha=$fecha, montoPagado=$montoPagado)"
    }


}