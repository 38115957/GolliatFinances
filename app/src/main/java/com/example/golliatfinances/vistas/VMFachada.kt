package com.example.golliatfinances.vistas

import android.net.Uri
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.golliatfinances.DatosPersistentes
import com.example.golliatfinances.activitys.CrearCredito
import com.example.golliatfinances.activitys.PagarCuota
import com.example.golliatfinances.databinding.ActivityMainBinding
import com.example.golliatfinances.modelo.Cliente
import com.example.golliatfinances.soapConnector.ResultadoEstadoCliente
import com.example.golliatfinances.utils.TextUtils


class VMFachada : ViewModel() {

    val liveDataActivity: MutableLiveData<Actividades> = MutableLiveData()
    val livedataBuscar: MutableLiveData<String> = MutableLiveData()
    val livedataGestionar: MutableLiveData<String> = MutableLiveData()
    var dni = ""

    lateinit var binding: ActivityMainBinding

    var sinDeudas = false
    var pocosCreditos = false
    var cuentaCreada = false
    var pocosCreditosLocales = false


    fun crearCredito(view: View) {

        liveDataActivity.postValue(Actividades.CrearCredito)

    }


    fun buscarPersona(view: View?) {

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

        //Obtiene el DNI como texto
        dni = binding.buscarDni.text.toString()

        livedataBuscar.postValue(dni)
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

    fun gestionarClienteCompletado() {
        cuentaCreada = true

        if (sinDeudas and pocosCreditos and cuentaCreada and pocosCreditosLocales) {
            binding.buttonCrearCredito.isEnabled = true
        }
    }

    fun gestionarCliente(view: View?) {
        livedataGestionar.postValue(binding.buscarDni.text.toString())
    }

    fun pagarCuota(view: View) {
        liveDataActivity.postValue(Actividades.PagarCuota)
    }

    fun gestionarPlanes(view: View) {

        //   liveDataActivity.postValue(CrearCredito::class.java)

    }

    enum class Actividades {
        CrearCredito, PagarCuota
    }

}