package com.example.golliatfinances.Modelo

import java.math.BigDecimal

class Plan {

    var identificadorPlan = "idPlan"
    private var numeroDeCuotas = 2
    var modalidadDePago = Modalidad.NOT_SET
    var porcentajeInteresMensual = BigDecimal.ZERO
    var costoAdministrativo = BigDecimal.ZERO

    fun numeroDeCuotas(): Int {
        return numeroDeCuotas
    }

    fun numeroDeCuotas(num: Int): Boolean {
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

    constructor(
        identificadorPlan: String,
        numeroDeCuotas: Int,
        modalidadDePago: Modalidad,
        porcentajeInteresMensual: BigDecimal,
        costoAdministrativo: BigDecimal
    ) {

        this.identificadorPlan = identificadorPlan
        this.modalidadDePago = modalidadDePago
        this.porcentajeInteresMensual = porcentajeInteresMensual
        this.costoAdministrativo = costoAdministrativo

        numeroDeCuotas(numeroDeCuotas)
    }

    constructor(identificadorPlan: String) {
        this.identificadorPlan = identificadorPlan
    }

    constructor()


    enum class Modalidad {
        ADELANTADA, VENCIDA, NOT_SET
    }

    override fun toString(): String {
        return "Plan(identificadorPlan='$identificadorPlan', numeroDeCuotas=$numeroDeCuotas, modalidadDePago=$modalidadDePago, porcentajeInteresMensual=$porcentajeInteresMensual, costoAdministrativo=$costoAdministrativo)"
    }


}