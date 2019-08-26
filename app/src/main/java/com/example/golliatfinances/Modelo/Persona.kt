package com.example.golliatfinances.Modelo

abstract class Persona{

    var documentoUnico = 0
    var nombres = "nombre"
    var apellidos = "apellidos"
    var domicilio = Domicilio()
    var telefono = 0

    override fun toString(): String {
        return "Persona(documentoUnico=$documentoUnico, nombres='$nombres', apellidos='$apellidos', domicilio=$domicilio, telefono=$telefono)"
    }


}