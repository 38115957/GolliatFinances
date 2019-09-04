package com.example.golliatfinances.soapConnector

import androidx.lifecycle.MutableLiveData
import org.jetbrains.anko.doAsync
import org.ksoap2.SoapEnvelope
import org.ksoap2.serialization.SoapObject
import org.ksoap2.serialization.SoapSerializationEnvelope
import org.ksoap2.transport.HttpTransportSE

class ServicioPublicoCredito {

    val liveDataCliente: MutableLiveData<ResultadoEstadoCliente> = MutableLiveData()
    val liveDataResultadoOtorgado: MutableLiveData<ResultadoOperacion> = MutableLiveData()
    val liveDataResultadoFinalizado: MutableLiveData<ResultadoOperacion> = MutableLiveData()

    private val codigoFinanciera_name = "codigoFinanciera"
    private val dni_name = "dni"
    private val identificadorCredito_name = "identificadorCredito"
    private val montoOtorgado_name = "montoOtorgado"

    private val namespace = "http://ISTP1.Service.Contracts.Service"
    private val url_service = "http://ds.dyndns.org:9000/ServicioPublicoCredito"

    fun obtenerEstadoCliente(dni: Int, codigoFinanciera: String) {

        doAsync {

            /*
       ● string codigoFinanciera: código qué identifica a cada financiera. Se corresponde con el código de grupo (no confundir con el número de grupo).
       ● int dni: número de documento del cliente.
        */
            val resultadoEstadoCliente = ResultadoEstadoCliente(dni)

            val request = SoapObject(namespace, "ObtenerEstadoCliente")

            request.addProperty(codigoFinanciera_name, codigoFinanciera)
            request.addProperty(dni_name, dni)

            val envelope = SoapSerializationEnvelope(SoapEnvelope.VER11)
            envelope.dotNet = true
            envelope.setOutputSoapObject(request)
            envelope.addMapping(
                namespace,
                ResultadoEstadoCliente().getName(),
                ResultadoEstadoCliente().javaClass
            )

            val androidHttpTransport = HttpTransportSE(url_service, 2000)
            val response: SoapObject


            try {
                androidHttpTransport.call(
                    "http://ISTP1.Service.Contracts.Service/IServicioPublicoCredito/ObtenerEstadoCliente",
                    envelope
                )
                response = envelope.response as SoapObject

                for (i in 0..response.propertyCount - 1) {
                    if (response.getProperty(i) != null) {
                        resultadoEstadoCliente.setProperty(i, response.getProperty(i))
                    }
                }

            } catch (e: Exception) {

                liveDataCliente.postValue(
                    ResultadoEstadoCliente(
                        false,
                        -1,
                        false,
                        e.localizedMessage,
                        dni
                    )
                )

            }

            liveDataCliente.postValue(resultadoEstadoCliente)

        }

    }

    fun informarCreditoOtorgado(
        dni: Int,
        codigoFinanciera: String,
        identificadorCredito: String,
        montoOtorgado: Int
    ) {

        doAsync {

            /*
       string codigoFinanciera: código qué identifica a cada financiera. Se corresponde con el código de grupo (no confundir con el número de grupo).
       ● int dni: número de documento del cliente.
       ● string identificadorCredito: número o código que identifica al crédito en la financiera.
       ● double montoOtorgado: monto total del crédito, con intereses incluidos.
        */

            val resultadoOperacion = ResultadoOperacion()

            val request = SoapObject(namespace, "InformarCreditoOtorgado")

            request.addProperty(codigoFinanciera_name, codigoFinanciera)
            request.addProperty(dni_name, dni)
            request.addProperty(identificadorCredito_name, identificadorCredito)
            request.addProperty(montoOtorgado_name, montoOtorgado)

            val envelope = SoapSerializationEnvelope(SoapEnvelope.VER11)
            envelope.dotNet = true
            envelope.setOutputSoapObject(request)
            envelope.addMapping(
                namespace,
                ResultadoOperacion().getName(),
                ResultadoOperacion().javaClass
            )

            val androidHttpTransport = HttpTransportSE(url_service, 2000)
            val response: SoapObject

            try {
                androidHttpTransport.call(
                    "http://ISTP1.Service.Contracts.Service/IServicioPublicoCredito/InformarCreditoOtorgado",
                    envelope
                )
                response = envelope.response as SoapObject

                for (i in 0..response.propertyCount - 1) {
                    if (response.getProperty(i) != null) {
                        resultadoOperacion.setProperty(i, response.getProperty(i))
                    }
                }

            } catch (e: Exception) {
                liveDataResultadoOtorgado.postValue(ResultadoOperacion(e.localizedMessage, false))
            }
            liveDataResultadoOtorgado.postValue(resultadoOperacion)
        }

    }

    fun informarCreditoFinalizado(
        dni: Int,
        codigoFinanciera: String,
        identificadorCredito: String
    ) {
        doAsync {

            /*
                ● string codigoFinanciera: código qué identifica a cada financiera. Se corresponde con el código de grupo (no confundir con el número de grupo).
                ● int dni: número de documento del cliente.
                ● string identificadorCredito: número o código que identifica al crédito en la financiera.
                */

            val resultadoOperacion = ResultadoOperacion()

            val request = SoapObject(namespace, "InformarCreditoFinalizado")

            request.addProperty(codigoFinanciera_name, codigoFinanciera)
            request.addProperty(dni_name, dni)
            request.addProperty(identificadorCredito_name, identificadorCredito)

            val envelope = SoapSerializationEnvelope(SoapEnvelope.VER11)
            envelope.dotNet = true
            envelope.setOutputSoapObject(request)
            envelope.addMapping(
                namespace,
                ResultadoOperacion().getName(),
                ResultadoOperacion().javaClass
            )

            val androidHttpTransport = HttpTransportSE(url_service, 2000)
            val response: SoapObject

            try {
                androidHttpTransport.call(
                    "http://ISTP1.Service.Contracts.Service/IServicioPublicoCredito/InformarCreditoFinalizado",
                    envelope
                )
                response = envelope.response as SoapObject

                for (i in 0..response.propertyCount - 1) {
                    if (response.getProperty(i) != null) {
                        resultadoOperacion.setProperty(i, response.getProperty(i))
                    }
                }

            } catch (e: Exception) {
                liveDataResultadoFinalizado.postValue(ResultadoOperacion(e.localizedMessage, false))
            }

            liveDataResultadoFinalizado.postValue(resultadoOperacion)
        }

    }


}