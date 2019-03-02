package com.mindera.skeletoid.utils

import com.mindera.skeletoid.utils.tuples.Quadruple
import com.mindera.skeletoid.utils.tuples.Quintuple
import org.junit.Assert.assertEquals
import org.junit.Test

class TuplesUnitTest {

    @Test
    fun testQuadruple() {

        // having
        val quadruple = Quadruple(
            first = "1st",
            second = "2nd",
            third = 3,
            fourth = 4f
        )

        // then
        assertEquals("1st", quadruple.first)
        assertEquals("2nd", quadruple.second)
        assertEquals(3, quadruple.third)
        assertEquals(4f, quadruple.fourth)
    }

    @Test
    fun testQuintuple() {

        // having
        val quintuple = Quintuple(
            first = "1st",
            second = "2nd",
            third = 3,
            fourth = 4f,
            fifth = "5th"
        )

        // then
        assertEquals("1st", quintuple.first)
        assertEquals("2nd", quintuple.second)
        assertEquals(3, quintuple.third)
        assertEquals(4f, quintuple.fourth)
        assertEquals("5th", quintuple.fifth)
    }
}