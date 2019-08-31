package com.example.golliatfinances.modelo

import java.math.BigDecimal

class Financiera {

    var nombreComercial = ""
    var razonSocial = ""
    var cuit = ""
    var domicilio = ""
    var interesMoroso = BigDecimal.ONE

    constructor(
        nombreComercial: String,
        razonSocial: String,
        cuit: String,
        domicilio: String,
        interesMoroso: BigDecimal?
    ) {
        this.nombreComercial = nombreComercial
        this.razonSocial = razonSocial
        this.cuit = cuit
        this.domicilio = domicilio
        this.interesMoroso = interesMoroso
    }
}