package com.example.golliatfinances

import com.example.golliatfinances.soapConnector.ResultadoEstadoCliente
import com.example.golliatfinances.soapConnector.ServicioPublicoCredito
import org.junit.Test

import org.junit.Assert.*

import org.ksoap2.serialization.SoapObject
import org.ksoap2.SoapEnvelope
import org.ksoap2.serialization.SoapSerializationEnvelope
import org.ksoap2.transport.HttpTransportSE


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class TestApi {


    @Test
    fun testObtenerEstadoCliente() {
        val NAMESPACE = "http://ISTP1.Service.Contracts.Service"
        val METHOD_NAME = "ObtenerEstadoCliente"
        val SOAP_ACTION =
            "http://ISTP1.Service.Contracts.Service/IServicioPublicoCredito/ObtenerEstadoCliente"
        val URL = "http://ds.dyndns.org:9000/ServicioPublicoCredito"

        val request2 = SoapObject(NAMESPACE, METHOD_NAME)

        request2.addProperty("codigoFinanciera", "67f583b3-8858-422f-b207-611634105a90")
        request2.addProperty("dni", 33000004)

        val envelope = SoapSerializationEnvelope(SoapEnvelope.VER11)
        envelope.dotNet = true
        envelope.setOutputSoapObject(request2)
        envelope.addMapping(NAMESPACE, "ResultadoEstadoCliente", ResultadoEstadoCliente().javaClass)

        val androidHttpTransport = HttpTransportSE(URL)
        var response = SoapObject();

        try {
            androidHttpTransport.call(SOAP_ACTION, envelope)
            response = envelope.getResponse() as SoapObject


        } catch (e: Exception) {
            e.printStackTrace()
        }


        assert(response.toString().contains("ConsultaValida=true"))
    }

    @Test
    fun testObtenerEstadoClienteService() {

        val reponse = ServicioPublicoCredito().obtenerEstadoCliente(33000013, "67f583b3-8858-422f-b207-611634105a90")

        assert(reponse.ConsultaValida)

    }

    @Test
    fun InformarCreditoOtorgadoService() {

        val reponse = ServicioPublicoCredito().informarCreditoOtorgado(33000013, "67f583b3-8858-422f-b207-611634105a90","codCred",10000)

        assert(reponse.operacionValida)

    }

    @Test
    fun InformarCreditoFinalizado() {

        val reponse = ServicioPublicoCredito().informarCreditoFinalizado(33000013, "67f583b3-8858-422f-b207-611634105a90","codCred")

        assert(reponse.operacionValida)

    }




}
