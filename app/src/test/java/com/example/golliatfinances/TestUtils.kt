package com.example.golliatfinances

import com.example.golliatfinances.soapConnector.ServicioPublicoCredito
import com.example.golliatfinances.utils.TextUtils
import org.junit.Assert
import org.junit.Test
import java.math.BigDecimal
import java.time.LocalDateTime

class TestUtils {


    @Test
    fun testBoolToText() {

        Assert.assertEquals("SI", TextUtils.boolSINO(true))

    }

    @Test
    fun testStringToBigDec1() {

        Assert.assertEquals(BigDecimal.ZERO, TextUtils.toBigDecimal("0"))

    }

    @Test
    fun testStringToBigDec2() {

        Assert.assertEquals(BigDecimal.ZERO, TextUtils.toBigDecimal(""))

    }

    @Test
    fun testStringToBigDec3() {

        Assert.assertEquals(BigDecimal.ZERO, TextUtils.toBigDecimal("saaddsa"))

    }

    @Test
    fun testStringToBigDec5() {

        Assert.assertEquals(123312.4123.toBigDecimal(), TextUtils.toBigDecimal("123312.4123"))

    }

    @Test
    fun testStringToBigDec6() {

        Assert.assertEquals(123312.4123.toBigDecimal(), TextUtils.toBigDecimal("123312,4123"))

    }

    @Test
    fun testStringToBigDec7() {

        Assert.assertEquals(123312.4123.toBigDecimal(), TextUtils.toBigDecimal("123.312,4123"))

    }

    @Test
    fun testStringToBigDec8() {

        Assert.assertEquals(123312.4123.toBigDecimal(), TextUtils.toBigDecimal("123,312.4123"))

    }

}