package com.example.golliatfinances.modelo

class Domicilio{

    var calle = ""
    var numero = 0
    var piso = 0
    var departamento = ""
    var codigoPostal = 0

    override fun toString(): String {
        return "Domicilio(calle='$calle', numero=$numero, piso=$piso, departamento='$departamento', codigoPostal=$codigoPostal)"
    }


}