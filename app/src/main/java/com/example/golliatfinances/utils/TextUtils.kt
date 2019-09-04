package com.example.golliatfinances.utils

import java.math.BigDecimal

class TextUtils {

    companion object Factory {

        fun boolSINO(bool: Boolean): String {
            if (bool) {
                return "SI"
            } else {
                return "NO"
            }
        }

        fun toBigDecimal(text: String): BigDecimal {

            var changed = text

            var lastComa = 0
            var lastDot = 0

            val charList = text.toList()

            for (i in 0..charList.size-1) {

                if (charList[i] == ',')
                    lastComa = i
                if (charList[i] == '.')
                    lastDot = i
            }

            if (lastComa < lastDot) {
                changed = text.replace(",","")
            }else{
                changed = text.replace(".","")
                changed = changed.replace(",",".")
            }

            return try {
                changed.toBigDecimal()
            } catch (ex: NumberFormatException) {
                BigDecimal.ZERO
            }


        }
    }


}