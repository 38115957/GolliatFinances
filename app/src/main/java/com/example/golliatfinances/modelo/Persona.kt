package com.example.golliatfinances.modelo

abstract class Persona : Identificable() {

    var nombres = ""
    var apellidos = ""
    var domicilio = ""
    var telefono = ""

    override fun toString(): String {
        return "Persona(nombres='$nombres', apellidos='$apellidos', domicilio='$domicilio', telefono=$telefono) ${super.toString()}"
    }


}