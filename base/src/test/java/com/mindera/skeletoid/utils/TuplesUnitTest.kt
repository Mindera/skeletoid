package com.mindera.skeletoid.utils

import com.mindera.skeletoid.utils.tuples.Quadruple
import com.mindera.skeletoid.utils.tuples.Quintuple
import org.junit.Assert
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
        Assert.assertEquals("1st", quadruple.first)
        Assert.assertEquals("2nd", quadruple.second)
        Assert.assertEquals(3, quadruple.third)
        Assert.assertEquals(4f, quadruple.fourth)
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
        Assert.assertEquals("1st", quintuple.first)
        Assert.assertEquals("2nd", quintuple.second)
        Assert.assertEquals(3, quintuple.third)
        Assert.assertEquals(4f, quintuple.fourth)
        Assert.assertEquals("5th", quintuple.fifth)
    }
}