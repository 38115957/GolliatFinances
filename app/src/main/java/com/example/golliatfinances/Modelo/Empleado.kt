package com.example.golliatfinances.Modelo

class Empleado : Persona()  {

var legajo = 0

    override fun toString(): String {
        return "Empleado(legajo=$legajo) ${super.toString()}"
    }


}