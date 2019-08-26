package com.example.golliatfinances.soapConnector

import org.ksoap2.serialization.KvmSerializable
import org.ksoap2.serialization.PropertyInfo
import java.util.*

/*
    <xs:complexType name="ResultadoOperacion">
    <xs:sequence>
    <xs:element minOccurs="0" name="Error" nillable="true" type="xs:string"/>
    <xs:element minOccurs="0" name="OperacionValida" type="xs:boolean"/>
    </xs:sequence>
    </xs:complexType>
    <xs:element name="ResultadoOperacion" nillable="true" type="tns:ResultadoOperacion"/>
 */

class ResultadoOperacion : KvmSerializable {

    var error = ""
    var operacionValida = false

    fun getName(): String {
        return "ResultadoOperacion"
    }

    override fun getPropertyInfo(index: Int, arg1: Hashtable<*, *>, info: PropertyInfo) {
        when (index) {
            0 -> {
                info.type = PropertyInfo.STRING_CLASS
                info.name = "Error"
            }
            1 -> {
                info.type = PropertyInfo.BOOLEAN_CLASS
                info.name = "OperacionValida"
            }
            else -> {
            }
        }
    }

    override fun setProperty(index: Int, value: Any) {
        when (index) {
            0 -> error = value.toString()
            1 -> operacionValida = value.toString() == "true"
            else -> {
            }
        }
    }

    override fun getProperty(index: Int): Any? {
        when (index) {
            0 -> return error
            1 -> return operacionValida
        }
        return null
    }

    override fun getPropertyCount(): Int {
        return 2
    }


    override fun toString(): String {
        return "ResultadoOperacion(error='$error', operacionValida=$operacionValida)"
    }


}