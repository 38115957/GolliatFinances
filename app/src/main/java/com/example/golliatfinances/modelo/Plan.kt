package com.example.golliatfinances.modelo

import java.math.BigDecimal

class Plan : Identificable() {

    private var numeroDeCuotas = 2
    var modalidadDePago = Modalidad.NOT_SET
    var porcentajeInteresMensual = BigDecimal.ZERO
    var costoAdministrativo = BigDecimal.ZERO

    fun numeroDeCuotas(): Int {
        return numeroDeCuotas
    }

    fun numeroDeCuotas(num: Int): Boolean {
        porcentajeInteresMensual.toString()
        if (!modalidadDePago.equals(Modalidad.VENCIDA)) {
            if (num > 1) {
                numeroDeCuotas = num
                return true
            }
            numeroDeCuotas = 2
            return false
        } else {
            numeroDeCuotas = num
            return true

        }
    }


    override fun toString(): String {
        return "Plan(numeroDeCuotas=$numeroDeCuotas, modalidadDePago=$modalidadDePago, porcentajeInteresMensual=$porcentajeInteresMensual, costoAdministrativo=$costoAdministrativo) ${super.toString()}"
    }


    enum class Modalidad {
        ADELANTADA, VENCIDA, NOT_SET
    }


}