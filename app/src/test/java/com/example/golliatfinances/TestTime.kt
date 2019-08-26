package com.example.golliatfinances

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDateTime

class TestTime {

    @Test
    fun testTimeNow() {

        val calendar = LocalDateTime.now()

        assertEquals(2019, calendar.year)

    }

    @Test
    fun testTimeSetDay10() {

        val calendar = LocalDateTime.now()

        assert(calendar.withDayOfMonth(10).toLocalDate().toString().contains("-10"))

    }

    @Test
    fun testTimeSetDay10andNextMonth() {

        var calendar = LocalDateTime.now()

        calendar = calendar.plusMonths(1)

        calendar = calendar.withDayOfMonth(10)

        assertEquals("2019-09-10", calendar.toLocalDate().toString())

    }


}