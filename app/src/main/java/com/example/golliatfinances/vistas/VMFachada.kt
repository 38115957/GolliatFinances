package com.example.golliatfinances.vistas

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.golliatfinances.DatosPersistentes
import com.example.golliatfinances.databinding.ActivityMainBinding
import com.example.golliatfinances.modelo.Cliente
import com.example.golliatfinances.soapConnector.ResultadoEstadoCliente
import com.example.golliatfinances.soapConnector.ServicioPublicoCredito
import com.example.golliatfinances.utils.TextUtils


class VMFachada : ViewModel() {

    val liveDataActivity: MutableLiveData<Actividades> = MutableLiveData()
    var dni = ""
    var codFinanciera = ""
    val datosPersistentes = DatosPersistentes()
    val servicioPublicoCredito = ServicioPublicoCredito()

    lateinit var binding: ActivityMainBinding

    var sinDeudas = false
    var pocosCreditos = false
    var cuentaCreada = false
    var pocosCreditosLocales = false

    fun init(lifecycleOwner: LifecycleOwner, codFinanciera: String) {
        datosPersistentes.createPlanes()

        datosPersistentes.liveDataCliente.observe(lifecycleOwner, Observer {
            mostrarCliente(it)
        })

        servicioPublicoCredito.liveDataCliente.observe(lifecycleOwner, Observer {
            estadoCliente(it)
        })

        this.codFinanciera = codFinanciera
    }

    fun crearCredito(view: View) {

        liveDataActivity.postValue(Actividades.CrearCredito)

    }

    fun limpiarCliente() {
        //Limpia el texto de estado
        binding.textEstado.setText("")

        //pone el estado de crear credito en inicial
        sinDeudas = false
        pocosCreditos = false
        cuentaCreada = false
        pocosCreditosLocales = false

        //Inhabilita los botones de opciones
        binding.buttonBuscar.isEnabled = false
        binding.buttonCrearCredito.isEnabled = false
        binding.buttonPagarCuota.isEnabled = false
        binding.buttonCliente.isEnabled = false
    }

    fun buscarPersona(view: View?) {

        limpiarCliente()

        //Obtiene el DNI como texto
        dni = binding.buscarDni.text.toString()

        //Busca si el cliente existe en esta financiera, cuando lo encuentra devuelve
        datosPersistentes.read(dni, DatosPersistentes.Tipo.CLIENTE)

        //Obtiene el estado del cliente desde el servicio web, cuando lo encuentra lo devuelve
        servicioPublicoCredito.obtenerEstadoCliente(
            TextUtils.toBigDecimal(dni).toInt(),
            codFinanciera
        )
    }

    fun estadoCliente(it: ResultadoEstadoCliente) {
        if (it.Error!!.isEmpty()) {

            binding.textEstado.append(
                "\n" +
                        String.format(
                            "El cliente posee %s creditos y %s posee deudas",
                            it.CantidadCreditosActivos.toString(),
                            TextUtils.boolSINO(it.TieneDeudas)
                        )
            )

            sinDeudas = (!it.TieneDeudas)
            pocosCreditos = (it.CantidadCreditosActivos < 3)

            if (sinDeudas and pocosCreditos and cuentaCreada and pocosCreditosLocales) {

                binding.buttonCrearCredito.isEnabled = true
            }
        } else {
            binding.textEstado.append("\n${it.Error}")

        }
    }

    fun mostrarCliente(cliente: Cliente) {
        binding.buttonBuscar.isEnabled = true
        if (cliente.isNull()) {//Si no existe, solo se puede gestionar el cliente
            binding.textEstado.append("\nEl usuario no se encuentra registrado")
            binding.buttonCliente.isEnabled = true
        } else if (cliente.tieneCreditosAPagar()) {//si tiene creditos, se habilitan todas las opciones
            cuentaCreada = true
            binding.textEstado.append(cliente.textoCliente())
            binding.buttonPagarCuota.isEnabled = true
            binding.buttonCliente.isEnabled = true
            if (cliente.creditoCheck() == "none") {
                pocosCreditosLocales = true
                if (sinDeudas and pocosCreditos and cuentaCreada and pocosCreditosLocales) {
                    binding.buttonCrearCredito.isEnabled = true
                }
            }

        } else {//si no tiene creditos para pagar, solo se habilita el crear credito
            cuentaCreada = true
            pocosCreditosLocales = true
            binding.textEstado.append("\n" + cliente.textoCliente())
            binding.buttonCliente.isEnabled = true
            if (cliente.creditoCheck() != "none") {
                if (sinDeudas and pocosCreditos and cuentaCreada) {
                    binding.buttonCrearCredito.isEnabled = true
                }
            }
        }
    }

    fun gestionarCliente(view: View?) {
        liveDataActivity.postValue(Actividades.GestionarCliente)
        limpiarCliente()
    }

    fun pagarCuota(view: View) {
        liveDataActivity.postValue(Actividades.PagarCuota)
    }


    enum class Actividades {
        CrearCredito, PagarCuota, GestionarCliente
    }

}