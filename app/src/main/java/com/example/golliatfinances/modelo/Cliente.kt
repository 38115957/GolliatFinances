package com.example.golliatfinances.modelo
import java.math.BigDecimal

class Cliente : Persona() {

    fun listaCreditos(): ArrayList<String> {
        val creditosNombres = arrayListOf(String())
        creditos.forEach { creditosNombres.add(it.nombreString()) }
        return creditosNombres
    }

    fun credito(index:Int): Credito {
        try {
            return creditos[index]
        }catch (ex :IndexOutOfBoundsException){
            return Credito.create()
        }
    }

    var sueldo = BigDecimal.ZERO

    fun isNull(): Boolean {
        return (super.apellidos.isEmpty() or super.nombres.isEmpty())
    }
fun textoCliente(): String {
    return "$apellidos, $nombres"
}

    fun tieneCreditosAPagar(): Boolean {
        creditos.forEach { if(!it.estaFinalizado()) return true }
        return false
    }

    private var creditos = arrayListOf<Credito>()

    fun creditoCheck(): String {

        val creditosActivos = creditos.filter { credito -> !credito.estaFinalizado() }

        if (creditosActivos.size < 3) {
            return "none"
        } else {
            return "Posee ${creditosActivos.size} creditos en esta financiera"
        }
    }

    fun addCredito(creditoAñadir: Credito): Boolean {

        if (creditos.filter { credito -> !credito.estaFinalizado() }.size < 3) {

            creditos.add(creditoAñadir)
            return true

        } else return false

    }

    fun estadoCreditos(): BigDecimal {
        var saldoActual = BigDecimal.ZERO
        for (credito in creditos) {

            saldoActual += credito.obtenerInforme().last().saldoImpago

        }
        return saldoActual
    }

    override fun toString(): String {
        return "Cliente(sueldo=$sueldo, creditos=$creditos) ${super.toString()}"
    }


}


//sos el mejor programador del mundo me encanta verte <3