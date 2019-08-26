package com.example.golliatfinances.Modelo

import java.math.BigDecimal
import java.math.RoundingMode
import org.threeten.bp.LocalDate

class CuotaInforme() {


    var montoInicial = BigDecimal.ZERO
    var cantidadDeDiasMoroso = 0
    var montoMoroso = BigDecimal.ZERO
    var estado = Estado.NO_VENCIDA
    var saldadaElDia: LocalDate? = null
    var fechaVencimiento: LocalDate? = null
    var saldoImpago = BigDecimal.ZERO
    var pagos = arrayListOf<Pago>()

    enum class Estado {
        PAGADA, PARCIAL, NO_VENCIDA, VENCIDA
    }

    override fun toString(): String {
        var totalPagado = BigDecimal.ZERO
        pagos.forEach { pago: Pago -> totalPagado = totalPagado.plus(pago.montoPagado) }

        return "\nCuotaInforme(montoInicial=${montoInicial.setScale(2, RoundingMode.HALF_EVEN)}," +
                " cantidadDeDiasMoroso=$cantidadDeDiasMoroso," +
                " montoMoroso=${montoMoroso.setScale(2, RoundingMode.HALF_EVEN)}," +
                " estado=$estado," +
                " saldadaElDia=$saldadaElDia," +
                " fechaVencimiento=$fechaVencimiento," +
                " saldoImpago=${saldoImpago.setScale(2, RoundingMode.HALF_EVEN)}, pagos: $pagos)" +
                " \nTotal pagado: $totalPagado"

    }


}