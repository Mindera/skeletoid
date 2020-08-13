package com.mindera.skeletoid.rxjava

import io.reactivex.Observable
import io.reactivex.Single
import org.hamcrest.Matchers.instanceOf
import org.junit.Assert.assertThat
import org.junit.Test

class AnyExtensionsTest {

    @Test
    fun testJustSingle() {
        val result = Any().justSingle()
        assertThat(result, instanceOf(Single::class.java))
    }

    @Test
    fun testJustObservable() {
        val result = Any().justObservable()
        assertThat(result, instanceOf(Observable::class.java))
    }
}