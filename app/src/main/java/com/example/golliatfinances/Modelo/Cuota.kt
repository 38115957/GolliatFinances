package com.example.golliatfinances.Modelo

import java.math.BigDecimal
import org.threeten.bp.LocalDate

class Cuota(val fechaVencimiento: LocalDate, val montoInicial: BigDecimal) {

    var cantidadDeDiasMoroso = 0


    override fun toString(): String {
        return "Cuota(fechaVencimiento=$fechaVencimiento, montoInicial=$montoInicial, cantidadDeDiasMoroso=$cantidadDeDiasMoroso)"
    }


}