package com.example.golliatfinances

import android.app.Application
import com.example.golliatfinances.modelo.Credito
import com.example.golliatfinances.modelo.Pago
import com.example.golliatfinances.modelo.Plan
import org.junit.Assert
import org.junit.Test
import org.threeten.bp.LocalDate

class TestCredito : Application() {

    @Test
    fun testCreditoAdelantada10cuotas10000montoEvalPrimeraCuota() {
        //Configuración
        var plan = Plan();
        plan.numeroDeCuotas(10)
        plan.modalidadDePago = Plan.Modalidad.ADELANTADA
        plan.porcentajeInteresMensual = 0.02.toBigDecimal()
        plan.costoAdministrativo = 0.3.toBigDecimal()
        var credito = Credito(0.035.toBigDecimal(), plan, 10000.0);

        //Ejecución
        val resultado = credito.montoPrimeraCuota().toString();

        //Validación
        Assert.assertEquals("1000.00", resultado)
    }

    @Test
    fun testCreditoVENCIDA10cuotas10000montoMontoEntregado() {
        //Configuración
        var plan = Plan();
        plan.numeroDeCuotas(10)
        plan.modalidadDePago = Plan.Modalidad.VENCIDA
        plan.porcentajeInteresMensual = 0.02.toBigDecimal()
        plan.costoAdministrativo = 0.2.toBigDecimal()
        var credito = Credito(0.035.toBigDecimal(), plan, 10000.0);

        //Ejecución
        val resultado = credito.montoEntregado().toString();

        //Validación
        Assert.assertEquals("8000.00", resultado)
    }

    @Test
    fun testCreditoAdelantada10cuotas10000montoEvalTotalPagar() {
        //Configuración
        var plan = Plan();
        plan.numeroDeCuotas(10)
        plan.modalidadDePago = Plan.Modalidad.ADELANTADA
        plan.porcentajeInteresMensual = 0.02.toBigDecimal()
        plan.costoAdministrativo = 0.3.toBigDecimal()
        var credito = Credito(0.035.toBigDecimal(), plan, 10000.0);

        //Ejecución
        val resultado = credito.totalAPagar().toString();

        //Validación
        Assert.assertEquals("11220.00", resultado)

    }

    @Test
    fun testCreditoADELANTADA10cuotas10000montoMontoEntregado() {
        //Configuración
        var plan = Plan();
        plan.numeroDeCuotas(10)
        plan.modalidadDePago = Plan.Modalidad.ADELANTADA
        plan.porcentajeInteresMensual = 0.02.toBigDecimal()
        plan.costoAdministrativo = 0.4.toBigDecimal()
        var credito = Credito(0.035.toBigDecimal(), plan, 10000.0);

        //Ejecución
        val resultado = credito.montoEntregado().toString();

        //Validación
        Assert.assertEquals("9000.00", resultado)

    }

    @Test
    fun testCreditoEstadoMoroso() {
        //Configuración
        var plan = Plan();
        plan.numeroDeCuotas(10)
        plan.modalidadDePago = Plan.Modalidad.ADELANTADA
        plan.porcentajeInteresMensual = 0.02.toBigDecimal()
        plan.costoAdministrativo = 0.4.toBigDecimal()
        var credito = Credito(0.035.toBigDecimal(), plan, 10000.0);
        credito.timeStamp = org.threeten.bp.LocalDate.MIN;

        //Ejecución
        val resultado = credito.determinarSaldos();

        //Validación
        Assert.assertEquals("MOROSO", credito.estado.toString())

    }


/*

    @Test
    fun testCreditoADELANTADA() {


        val plan =
            Plan("plan1", 15, Plan.Modalidad.ADELANTADA, 0.015.toBigDecimal(), 156.toBigDecimal())

        val credito = Credito(0.035.toBigDecimal(), plan, 10000.00)

        Assert.assertEquals(Plan.Modalidad.ADELANTADA, credito.plan.modalidadDePago)

    }

    @Test
    fun testCreditoVENCIDA() {

        val plan =
            Plan("plan1", 10, Plan.Modalidad.VENCIDA, 0.015.toBigDecimal(), 156.toBigDecimal())

        val credito = Credito(0.035.toBigDecimal(), plan, 100000.00)

        Assert.assertEquals(Plan.Modalidad.VENCIDA, credito.plan.modalidadDePago)

    }

    @Test
    fun testCreditoSaldo() {

        val calendar = LocalDate.now()

        val plan =
            Plan("plan1", 3, Plan.Modalidad.VENCIDA, 0.015.toBigDecimal(), 500.toBigDecimal())

        val credito = Credito(0.035.toBigDecimal(), plan, 10000.00)
        credito.timeStamp = calendar.minusMonths(3)

        for (i in 1..10 step 2) {
            credito.pagos.add(
                Pago(
                    calendar.minusMonths(8).plusDays(i.toLong()),
                    1500.15.toBigDecimal()
                )
            )
        }

        var informe = credito.obtenerInforme()

        Assert.assertEquals(10879.3373, credito.determinarFaltanteAPagar().toDouble(), 0.1)

    }

    @Test
    fun testCreditoSaldoMoroso() {

        val calendar = LocalDate.now()

        val plan =
            Plan("plan1", 3, Plan.Modalidad.VENCIDA, 0.015.toBigDecimal(), 500.toBigDecimal())

        val credito = Credito(0.035.toBigDecimal(), plan, 10000.00)
        credito.timeStamp = calendar.minusMonths(3)

        credito.obtenerInforme()

        Assert.assertEquals(Credito.Estado.MOROSO, credito.estado)

    }

    @Test
    fun testCreditoSobrePago() {

        val plan =
            Plan("plan1", 3, Plan.Modalidad.VENCIDA, 0.015.toBigDecimal(), 500.toBigDecimal())

        val credito = Credito(0.035.toBigDecimal(), plan, 10000.00)

        Assert.assertEquals(false, credito.grabarPago(150000.toBigDecimal()))

    }

    @Test
    fun testCreditoFinalizado() {

        val calendar = LocalDate.now()

        val plan =
            Plan("plan1", 3, Plan.Modalidad.VENCIDA, 0.015.toBigDecimal(), 500.toBigDecimal())

        val credito = Credito(0.035.toBigDecimal(), plan, 10000.00)
        credito.timeStamp = calendar.minusMonths(3)

        for (i in 1..120) {
            credito.pagos.add(
                Pago(
                    calendar.minusMonths(8).plusDays(i.toLong()),
                    1500.15.toBigDecimal()
                )
            )
        }

        credito.obtenerInforme()

        Assert.assertEquals(Credito.Estado.PENDIENTE_DE_FINALIZACION, credito.estado)

    }

    @Test
    fun testPlanMenosDeDosCuotasAdelantada() {

        val plan =
            Plan("plan1", 1, Plan.Modalidad.ADELANTADA, 0.015.toBigDecimal(), 500.toBigDecimal())

        val credito = Credito(0.035.toBigDecimal(), plan, 10000.00)

        Assert.assertEquals(2, credito.plan.numeroDeCuotas())

    }
*/
}