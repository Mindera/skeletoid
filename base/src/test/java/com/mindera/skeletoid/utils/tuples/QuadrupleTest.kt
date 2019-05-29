package com.mindera.skeletoid.utils.tuples

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class QuadrupleTest {

    @Test
    fun testQuadruple() {
        val quadruple = Quadruple(1, "Mindera", 123456L, 3.14F)

        assertTrue(quadruple.first is Int)
        assertTrue(quadruple.second is String)
        assertTrue(quadruple.third is Long)
        assertTrue(quadruple.fourth is Float)

        assertEquals(1, quadruple.first)
        assertEquals("Mindera", quadruple.second)
        assertEquals(123456L, quadruple.third)
        assertEquals(3.14F, quadruple.fourth)
    }

    @Test
    fun testNullableQuadruple() {
        val quadruple = Quadruple<Int?, String?, Long?, Float?>(null, null, null, null)

        assertEquals(null, quadruple.first)
        assertEquals(null, quadruple.second)
        assertEquals(null, quadruple.third)
        assertEquals(null, quadruple.fourth)
    }

    @Test
    fun testQuintuple() {
        val quintuple = Quintuple(1, "Mindera", 123456L, 3.14F, arrayOf("hi", "there", "Skeletoid"))

        assertTrue(quintuple.first is Int)
        assertTrue(quintuple.second is String)
        assertTrue(quintuple.third is Long)
        assertTrue(quintuple.fourth is Float)
        assertTrue(quintuple.fifth is Array<String>)

        assertEquals(1, quintuple.first)
        assertEquals("Mindera", quintuple.second)
        assertEquals(123456L, quintuple.third)
        assertEquals(3.14F, quintuple.fourth)
        assertTrue(arrayOf("hi", "there", "Skeletoid").contentEquals(quintuple.fifth))
    }
    
    @Test
    fun testNullableQuintuple() {
        val quintuple = Quintuple<Int?, String?, Long?, Float?, Array<String>?>(null, null, null, null, null)

        assertEquals(null, quintuple.first)
        assertEquals(null, quintuple.second)
        assertEquals(null, quintuple.third)
        assertEquals(null, quintuple.fourth)
        assertEquals(null, quintuple.fourth)
    }
}
