package com.example.golliatfinances.modelo

class Empleado : Persona()  {

var legajo = 0

    override fun toString(): String {
        return "Empleado(legajo=$legajo) ${super.toString()}"
    }


}