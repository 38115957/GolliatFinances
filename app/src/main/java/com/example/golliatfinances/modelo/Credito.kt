package com.example.golliatfinances.modelo

import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import java.math.BigDecimal
import java.math.RoundingMode
import org.threeten.bp.LocalDate
import org.threeten.bp.temporal.ChronoUnit
import kotlin.math.roundToInt

@SuppressLint("NewApi")
class Credito(val interesMoroso: BigDecimal, val plan: Plan, monto: Double) {

    var estado = Estado.NOT_SET
    val monto = monto.toBigDecimal()
    var timeStamp = LocalDate.now()
    //val cuotas = arrayListOf<Cuota>()
    val pagos = arrayListOf<Pago>()
    val legajoEmpleado = 0

    @Expose
    var lastInforme = arrayListOf<CuotaInforme>()


    init {
        primeraCuota(generarCuotas())
    }

    fun estaFinalizado(): Boolean {
        if (estado.equals(Estado.FINALIZADO) or estado.equals(Estado.PENDIENTE_DE_FINALIZACION)) {
            return true
        }
        return false
    }

    fun grabarPago(montoPagado: BigDecimal): Boolean {

        if (obtenerSaldos().last().saldoImpago < montoPagado) {
            return false
        }

        pagos.add(Pago(LocalDate.now(), montoPagado))
        determinarSaldos()
        return true
    }


    fun montoEntregado(): BigDecimal {
        if (plan.modalidadDePago == Plan.Modalidad.ADELANTADA) {

            return monto - montoPrimeraCuota()

        } else {//VENCIDA

            return monto - plan.costoAdministrativo.multiply(monto)

        }
    }

    fun primeraCuota(cuotas: ArrayList<Cuota>): ArrayList<Cuota> {

        estado = Estado.PENDIENTE

        if (plan.modalidadDePago == Plan.Modalidad.ADELANTADA) {

            pagos.add(Pago(timeStamp, cuotas[0].montoInicial))

        } else if (plan.modalidadDePago == Plan.Modalidad.VENCIDA) {

            pagos.add(Pago(timeStamp, plan.costoAdministrativo.multiply(monto)))

        }

        return cuotas

    }

    fun montoPrimeraCuota(): BigDecimal {

        try {
            return monto.divide(plan.numeroDeCuotas().toBigDecimal())
                .setScale(2, RoundingMode.HALF_EVEN)

        } catch (e: ArithmeticException) {

            return ((monto.toDouble() / plan.numeroDeCuotas()).times(100)).roundToInt().div(100)
                .toBigDecimal().setScale(2, RoundingMode.HALF_EVEN)
        }
    }

    fun totalAPagar(): BigDecimal {

        var montoCuotaBase = montoPrimeraCuota();

        var contador = BigDecimal.ZERO

        for (i in 0..plan.numeroDeCuotas()) {

            contador +=
                montoCuotaBase.multiply(plan.porcentajeInteresMensual.plus(BigDecimal.ONE))
                    .setScale(2, RoundingMode.HALF_EVEN)

        }

        return contador

    }

    fun generarCuotas(): ArrayList<Cuota> {

        var cuotas = arrayListOf<Cuota>()

        if (plan.modalidadDePago.equals(Plan.Modalidad.VENCIDA)) {
            cuotas.add(0, Cuota(timeStamp.plusDays(1), plan.costoAdministrativo.multiply(monto)))
        }

        var montoCuotaBase = montoPrimeraCuota();

        var calendar = timeStamp

        calendar = calendar.withDayOfMonth(10)

        for (i in 0..plan.numeroDeCuotas()) {

            calendar = calendar.plusMonths(1)

            cuotas.add(Cuota(calendar, montoCuotaBase))

            montoCuotaBase =
                montoCuotaBase.multiply(plan.porcentajeInteresMensual.plus(BigDecimal.ONE))
                    .setScale(2, RoundingMode.HALF_EVEN)

        }

        return cuotas

    }


    fun generarPago(saldos: BigDecimal, contadorPago: Int): Pago {
        if (menorZero(saldos)) { //si el pago es menor a 0, añade un pago parcial
            return Pago(
                pagos[contadorPago].fecha, pagos[contadorPago].montoPagado + saldos
            )

        } else {
            return pagos[contadorPago]
        }
    }

    fun comprobarTamaño(contadorPago: Int): Boolean {
        return pagos.size - 1 > contadorPago
    }

    fun comprobarFechas(contadorPago: Int, cuota: Cuota): Boolean {
        return pagos[contadorPago].fecha.isBefore(cuota.fechaVencimiento)
    }

    fun mayorZero(comparar: BigDecimal): Boolean {
        return comparar > BigDecimal.ZERO
    }

    fun menorZero(comparar: BigDecimal): Boolean {
        return comparar < BigDecimal.ZERO
    }

    fun compararTamañoFechaZero(contadorPago: Int, cuota: Cuota, comparar: BigDecimal): Boolean {

        if (comprobarTamaño(contadorPago)) {
            return comprobarFechas(
                contadorPago,
                cuota
            ) and mayorZero(comparar)
        } else {
            return false
        }


    }

    fun compararTamañoZero(contadorPago: Int, comparar: BigDecimal): Boolean {

        return comprobarTamaño(contadorPago) and mayorZero(comparar)

    }

    fun obtenerSaldos(): ArrayList<CuotaInforme> {
        if (lastInforme.isEmpty()) {
            return determinarSaldos()
        }
        return lastInforme
    }


    fun determinarSaldos(): ArrayList<CuotaInforme> {

        val cuotas = generarCuotas()
        var contadorPago = 0
        var saldoCuota = BigDecimal.ZERO
        lastInforme.clear()
        var interesFueraDeTermino = BigDecimal.ZERO

        for (cuota in cuotas) {

            val informe = CuotaInforme()

            if (menorZero(saldoCuota + interesFueraDeTermino)) {

                informe.pagos.add(
                    Pago(
                        pagos[contadorPago - 1].fecha,
                        -saldoCuota - interesFueraDeTermino
                    )
                )
                //Si hubiese saldos restantes del último pago, los añade como un pago parcial en esta cuota

                saldoCuota = cuota.montoInicial + saldoCuota + interesFueraDeTermino
                //Añade el saldo restante del pago sobre la última cuota pagada, si es que hubiese

            } else {
                saldoCuota = cuota.montoInicial
                //Si el saldo es mayor de cero se descarta

            }



            interesFueraDeTermino = BigDecimal.ZERO //reinicia el monto extra por pago a destiempo

            while (compararTamañoFechaZero(contadorPago, cuota, saldoCuota)) {
                //Mientras existan pagos, el saldo sea mayor a cero y el pago sea anterior al vencimiento de la cuota

                saldoCuota -= pagos[contadorPago].montoPagado //se resta de la cuota el monto pagado

                informe.pagos.add(
                    generarPago(
                        saldoCuota,
                        contadorPago
                    )
                ) //añade el pago al historial
                contadorPago += 1

            }

            if (mayorZero(saldoCuota)) { //si existe saldo de la cuota

                var fechaInicial = cuota.fechaVencimiento;

                while (compararTamañoZero(contadorPago, saldoCuota + interesFueraDeTermino)) {
                    //mientras existan pagos y el saldo + intereses sean mayores a cero

                    val diferenciaDias =
                        ChronoUnit.DAYS.between(fechaInicial, pagos[contadorPago].fecha)
                    fechaInicial = pagos[contadorPago].fecha
                    //determina la diferencia de dias entre el pago y el vencimiento de la cuota (O el pago anterior)
                    //La fecha inicial pasa a ser la del pago actual

                    if (mayorZero(saldoCuota)) {
                        //se van calculando los intereses por moroso en base al saldo

                        if (cuota.fechaVencimiento.isBefore(LocalDate.now())) {//Solo se calcula el saldo moroso si la fecha de vencimiento es anterior a la actual

                            interesFueraDeTermino += saldoCuota.multiply((interesMoroso) * diferenciaDias.toBigDecimal())

                            informe.montoMoroso = interesFueraDeTermino
                            informe.cantidadDeDiasMoroso = diferenciaDias.toInt()
                            //se va colocando en el informe el último monto calculado de interesFueraDeTermino de pago

                        }


                        saldoCuota -= pagos[contadorPago].montoPagado
                        //se quita al saldo el pago actual

                    } else { //cuando el saldo es menor a cero

                        interesFueraDeTermino =
                            interesFueraDeTermino + saldoCuota - pagos[contadorPago].montoPagado
                        saldoCuota = BigDecimal.ZERO
                        //se resta al monto de interes y se vuelve a cero el saldo
                    }

                    informe.pagos.add(
                        generarPago(
                            saldoCuota + interesFueraDeTermino, contadorPago
                        )
                    )
                    //añade el pago al historial
                    contadorPago += 1

                }

                if (mayorZero(saldoCuota)) {//Si aun sigue quedando saldo
                    val diferenciaDias =
                        ChronoUnit.DAYS.between(cuota.fechaVencimiento, LocalDate.now())

                    if (cuota.fechaVencimiento.isBefore(LocalDate.now())) {

                        interesFueraDeTermino += saldoCuota.multiply((interesMoroso) * diferenciaDias.toBigDecimal())
                        informe.montoMoroso =
                            informe.montoMoroso + saldoCuota.multiply((interesMoroso) * diferenciaDias.toBigDecimal())
                        informe.cantidadDeDiasMoroso = diferenciaDias.toInt()

                        if (diferenciaDias > 14) {
                            estado = Estado.MOROSO
                        }

                    } else {

                        informe.cantidadDeDiasMoroso = 0

                    }
                }
            }

            when {
                saldoCuota <= BigDecimal.ZERO -> informe.estado = CuotaInforme.Estado.PAGADA
                saldoCuota < cuota.montoInicial -> informe.estado = CuotaInforme.Estado.PARCIAL
                saldoCuota > BigDecimal.ZERO -> informe.estado = CuotaInforme.Estado.VENCIDA
                else -> informe.estado = CuotaInforme.Estado.VENCIDA
            }

            if (cuota.fechaVencimiento.isAfter(LocalDate.now()) and !informe.estado.equals(
                    CuotaInforme.Estado.PAGADA
                )
            ) {
                informe.estado = CuotaInforme.Estado.NO_VENCIDA
            }

            if ((saldoCuota + interesFueraDeTermino <= BigDecimal.ZERO) and (contadorPago > 0)) {

                informe.saldadaElDia = pagos[contadorPago - 1].fecha

            }

            informe.montoInicial = cuota.montoInicial
            informe.fechaVencimiento = cuota.fechaVencimiento

            if (menorZero(saldoCuota + interesFueraDeTermino)) {
                informe.saldoImpago = BigDecimal.ZERO
            } else {
                informe.saldoImpago = saldoCuota + interesFueraDeTermino
            }

            lastInforme.add(informe)

        }

        if (lastInforme.last().estado == CuotaInforme.Estado.PAGADA) {
            estado = Estado.PENDIENTE_DE_FINALIZACION
        }

        return lastInforme

    }

    fun determinarFaltanteAPagar(): BigDecimal {

        obtenerSaldos()

        var saldo = BigDecimal.ZERO

        for (cuota in lastInforme) {
            saldo += cuota.saldoImpago
        }
        return saldo
    }

    override fun toString(): String {

        var resultado = ""
        var cuotas = ""

        generarCuotas().forEach { cuota -> cuotas += "Vencimiento: " + cuota.fechaVencimiento + " - Monto: " + cuota.montoInicial + "\n" }

        resultado += "Interes: $interesMoroso \n"
        resultado += "Monto otorgado: $monto \n"
        resultado += "Monto entregado:  ${montoEntregado()}  \n"
        resultado += "Legajo del empleado: $legajoEmpleado \n"
        resultado += "Total a pagar: " + totalAPagar() + " \n"
        resultado += "\nPlan: $plan \n"
        resultado += "Estado: $estado \n"
        resultado += "Fecha creación: $timeStamp \n"
        resultado += "\nCuotas: \n"
        resultado += cuotas
        resultado += "Pagos: \n"

        for (pago in pagos) {
            resultado += "Monto: " + pago.montoPagado + " Fecha del pago: " + pago.fecha.toString() + " \n"
        }

        return resultado
    }


    enum class Estado {
        PENDIENTE, ACTIVO, FINALIZADO, MOROSO, PENDIENTE_DE_FINALIZACION, NOT_SET
    }

    companion object Factory {
        fun create(): Credito = Credito(BigDecimal.ONE, Plan(), -1.00)
    }

}
