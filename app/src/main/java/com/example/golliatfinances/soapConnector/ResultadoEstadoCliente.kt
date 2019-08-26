package com.example.golliatfinances.soapConnector

import org.ksoap2.serialization.KvmSerializable
import org.ksoap2.serialization.PropertyInfo
import java.util.*


class ResultadoEstadoCliente : KvmSerializable {

    /*
        <xs:complexType name="ResultadoEstadoCliente">
        <xs:sequence>
        <xs:element minOccurs="0" name="CantidadCreditosActivos" type="xs:int"/>
        <xs:element minOccurs="0" name="ConsultaValida" type="xs:boolean"/>
        <xs:element minOccurs="0" name="Error" nillable="true" type="xs:string"/>
        <xs:element minOccurs="0" name="TieneDeudas" type="xs:boolean"/>
        </xs:sequence>
        </xs:complexType>
        <xs:element name="ResultadoEstadoCliente" nillable="true" type="tns:ResultadoEstadoCliente"/>
     */

    fun getName(): String {
        return "ResultadoEstadoCliente"
    }

    var TieneDeudas = false
    var CantidadCreditosActivos = 0
    var ConsultaValida = false
    var Error: String? = ""
    var dni: Int = 0

    constructor(
        TieneDeudas: Boolean,
        CantidadCreditosActivos: Int,
        ConsultaValida: Boolean,
        Error: String
    ) {
        this.TieneDeudas = TieneDeudas
        this.CantidadCreditosActivos = CantidadCreditosActivos
        this.ConsultaValida = ConsultaValida
        this.Error = Error
    }

    constructor()

    constructor(dni: Int) {
        this.dni = dni
    }


    override fun getProperty(arg0: Int): Any? {
        when (arg0) {
            0 -> return CantidadCreditosActivos
            1 -> return ConsultaValida
            2 -> return Error
            3 -> return TieneDeudas
        }
        return null
    }

    override fun getPropertyCount(): Int {
        return 4
    }

    override fun getPropertyInfo(index: Int, arg1: Hashtable<*, *>, info: PropertyInfo) {
        when (index) {
            0 -> {
                info.type = PropertyInfo.INTEGER_CLASS
                info.name = "CantidadCreditosActivos"
            }
            1 -> {
                info.type = PropertyInfo.BOOLEAN_CLASS
                info.name = "ConsultaValida"
            }
            2 -> {
                info.type = PropertyInfo.STRING_CLASS
                info.name = "Error"
            }
            3 -> {
                info.type = PropertyInfo.BOOLEAN_CLASS
                info.name = "TieneDeudas"
            }
            else -> {
            }
        }
    }

    override fun setProperty(index: Int, value: Any) {
        when (index) {
            0 -> CantidadCreditosActivos = Integer.parseInt(value.toString())
            1 -> ConsultaValida = value.toString() == "true"
            2 -> Error = value.toString()
            3 -> TieneDeudas = value.toString() == "true"
            else -> {
            }
        }
    }

    override fun toString(): String {
        return "ResultadoEstadoCliente(TieneDeudas=$TieneDeudas, CantidadCreditosActivos=$CantidadCreditosActivos, ConsultaValida=$ConsultaValida, Error=$Error, dni=$dni)"
    }


}