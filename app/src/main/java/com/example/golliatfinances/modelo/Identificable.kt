package com.example.golliatfinances.modelo

abstract class Identificable {

    var identificador = "";

    override fun toString(): String {
        return "Identificable(identificador='$identificador')"
    }


}