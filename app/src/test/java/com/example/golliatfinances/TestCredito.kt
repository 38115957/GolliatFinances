package com.example.golliatfinances

import android.app.Application
import com.example.golliatfinances.Modelo.Credito
import com.example.golliatfinances.Modelo.Pago
import com.example.golliatfinances.Modelo.Plan
import org.junit.Assert
import org.junit.Test
import org.threeten.bp.LocalDate

class TestCredito : Application() {

    override fun onCreate() {
        super.onCreate()
    }


    fun init() {

    }

    @Test
    fun testCreditoADELANTADA() {

        val calendar = LocalDate.now()

        val plan =
            Plan("plan1", 15, Plan.Modalidad.ADELANTADA, 0.015.toBigDecimal(), 156.toBigDecimal())

        val credito = Credito(0.035.toBigDecimal(), plan, 10000.00)

        for (i in 1..100) {
            val pago = Pago(calendar.plusDays(i.toLong()), 150.15.toBigDecimal())
            credito.pagos.add(pago)

        }

        Assert.assertEquals(Plan.Modalidad.ADELANTADA, credito.plan.modalidadDePago)

    }

    @Test
    fun testCreditoVENCIDA() {

        val calendar = LocalDate.now()

        val plan =
            Plan("plan1", 10, Plan.Modalidad.VENCIDA, 0.015.toBigDecimal(), 156.toBigDecimal())

        val credito = Credito(0.035.toBigDecimal(), plan, 100000.00)
        credito.timeStamp = calendar.minusMonths(8)
        credito.init()

        for (i in 1..250 step 4) {
            credito.pagos.add(
                Pago(
                    calendar.minusMonths(8).plusDays(i.toLong()),
                    1500.15.toBigDecimal()
                )
            )
        }
        Assert.assertEquals(Plan.Modalidad.VENCIDA, credito.plan.modalidadDePago)

    }

    @Test
    fun testCreditoSaldo() {

        val calendar = LocalDate.now()

        val plan =
            Plan("plan1", 3, Plan.Modalidad.VENCIDA, 0.015.toBigDecimal(), 500.toBigDecimal())

        val credito = Credito(0.035.toBigDecimal(), plan, 10000.00)
        credito.timeStamp = calendar.minusMonths(3)
        credito.init()

        for (i in 1..10 step 2) {
            credito.pagos.add(
                Pago(
                    calendar.minusMonths(8).plusDays(i.toLong()),
                    1500.15.toBigDecimal()
                )
            )
        }

        var informe = credito.obtenerSaldos()

        Assert.assertEquals(11793.77788, informe.last().saldoImpago.toDouble(),0.1)

    }

    @Test
    fun testCreditoSaldoMoroso() {

        val calendar = LocalDate.now()

        val plan =
            Plan("plan1", 3, Plan.Modalidad.VENCIDA, 0.015.toBigDecimal(), 500.toBigDecimal())

        val credito = Credito(0.035.toBigDecimal(), plan, 10000.00)
        credito.timeStamp = calendar.minusMonths(3)
        credito.init()
        
        credito.obtenerSaldos()

        Assert.assertEquals(Credito.Estado.MOROSO, credito.estado)

    }


}