package com.example.golliatfinances.vistas

import android.graphics.Color
import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.golliatfinances.DatosPersistentes
import com.example.golliatfinances.databinding.PagarCuotaBinding
import com.example.golliatfinances.modelo.Cliente
import com.example.golliatfinances.modelo.Credito
import com.example.golliatfinances.utils.TextUtils

class VMPagarCuota : ViewModel() {

    var datosPersistentes = DatosPersistentes()
    var cliente = Cliente()
    lateinit var binding: PagarCuotaBinding
    var dni = ""
    val liveDataCliente: MutableLiveData<Cliente> = MutableLiveData()
    val liveDataCuotas: MutableLiveData<Credito> = MutableLiveData()

    fun init(owner: LifecycleOwner) {

        datosPersistentes.liveDataCliente.observe(owner, Observer { gestionarCliente(it) })

        binding.spinnerCredito.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {

                liveDataCuotas.postValue(cliente.credito(position))

            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
        datosPersistentes.read(dni, DatosPersistentes.Tipo.CLIENTE)
    }

    fun gestionarCliente(cliente: Cliente) {
        binding.nombreUsuario.setText(cliente.textoCliente())
        this.cliente = cliente
        liveDataCliente.postValue(cliente)
    }


    fun pagarCuota(view: View) {

        val monto = TextUtils.toBigDecimal(binding.montoAPagarCredito.text.toString())

        val posCredito = binding.spinnerCredito.selectedItemPosition

        if (cliente.credito(posCredito).grabarPago(monto)) {

            liveDataCuotas.postValue(cliente.credito(posCredito))
            datosPersistentes.write(cliente)

            binding.montoAPagarCredito.setHelperText("Monto total que desea pagar el cliente")
            binding.montoAPagarCredito.helperTextColor = Color.GRAY

        } else {
            binding.montoAPagarCredito.setHelperText(
                "El monto exede las deudas actuales! (" + cliente.credito(
                    posCredito
                ).determinarFaltanteAPagar() + ")"
            )
            binding.montoAPagarCredito.helperTextColor = Color.RED
        }


    }

}