package com.example.golliatfinances

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import com.example.golliatfinances.soapConnector.ResultadoEstadoCliente
import com.example.golliatfinances.soapConnector.ServicioPublicoCredito
import org.junit.Test

import org.junit.Assert.*

import org.ksoap2.serialization.SoapObject
import org.ksoap2.SoapEnvelope
import org.ksoap2.serialization.SoapSerializationEnvelope
import org.ksoap2.transport.HttpTransportSE
import org.mockito.Mockito.mock


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class TestApi {

    val servicioPublicoCredito = ServicioPublicoCredito()
    val lifecycle = LifecycleRegistry(mock(LifecycleOwner::class.java))


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

        val androidHttpTransport = HttpTransportSE(URL, 200)
        var response = SoapObject();

        try {
            androidHttpTransport.call(SOAP_ACTION, envelope)
            response = envelope.getResponse() as SoapObject


        } catch (e: Exception) {
            e.printStackTrace()
        }


        assert(response.toString().contains("ConsultaValida=true"))
    }




}
