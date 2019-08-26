package com.example.golliatfinances.Modelo

import java.math.BigDecimal

class Plan {

    var identificadorPlan ="idPlan"
    var numeroDeCuotas = 0
    var modalidadDePago = Modalidad.NOT_SET
    var porcentajeInteresMensual = BigDecimal.ZERO
    var costoAdministrativo = BigDecimal.ZERO

    constructor(
        identificadorPlan: String,
        numeroDeCuotas: Int,
        modalidadDePago: Modalidad,
        porcentajeInteresMensual: BigDecimal,
        costoAdministrativo: BigDecimal
    ) {
        this.identificadorPlan = identificadorPlan
        this.numeroDeCuotas = numeroDeCuotas
        this.modalidadDePago = modalidadDePago
        this.porcentajeInteresMensual = porcentajeInteresMensual
        this.costoAdministrativo = costoAdministrativo
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