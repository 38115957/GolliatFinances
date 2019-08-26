package com.example.golliatfinances.Modelo

import java.math.BigDecimal

class Cliente : Persona() {


    var sueldo = BigDecimal.ZERO

    private var creditos = arrayListOf<Credito>()

    fun addCredito(creditoAñadir: Credito): Boolean {

        if (creditos.filter { credito -> !credito.estaFinalizado() }.size < 3) {

            creditos.add(creditoAñadir)
            return true

        } else return false

    }

    fun estadoCreditos(): BigDecimal {
        var saldoActual = BigDecimal.ZERO
        for (credito in creditos){

            saldoActual += credito.obtenerSaldos().last().saldoImpago

        }
        return saldoActual
    }

    override fun toString(): String {
        return "Cliente(sueldo=$sueldo, creditos=$creditos, ${super.toString()})"
    }


}


//sos el mejor programador del mundo me encanta verte <3