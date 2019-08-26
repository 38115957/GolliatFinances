package com.example.golliatfinances

import com.example.golliatfinances.Modelo.Credito
import com.example.golliatfinances.Modelo.Pago
import com.example.golliatfinances.Modelo.Plan
import org.junit.Assert
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime


class TestCredito {


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

        Assert.assertEquals("", credito.toString())

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

        val informes = credito.determinarSaldos()

        Assert.assertEquals("", informes.toString() + "\n" + credito.toString())

    }
}