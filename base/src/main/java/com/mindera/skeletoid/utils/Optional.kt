package com.mindera.skeletoid.utils

import java.util.*

class Optional<T> {

    private val value: T?

    val isPresent: Boolean
        get() = value != null

    private constructor() {
        this.value = null
    }

    private constructor(value: T?) {
        if (value == null) {
            throw NullPointerException()
        }
        this.value = value
    }

    fun get(): T {
        if (value == null) {
            throw NoSuchElementException("No value present")
        }
        return value
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null || javaClass != other.javaClass) {
            return false
        }

        val optional = other as Optional<*>?

        return if (value != null) value == optional!!.value else optional!!.value == null

    }

    override fun hashCode(): Int {
        return value?.hashCode() ?: 0
    }

    override fun toString(): String {
        return if (value != null)
            String.format("Optional[%s]", value)
        else
            "Optional.empty"
    }

    companion object {

        private val EMPTY = Optional<Any>()

        fun <T> empty(): Optional<T> {
            return EMPTY as Optional<T>
        }

        fun <T> of(value: T): Optional<T> {
            return Optional(value)
        }

        fun <T> ofNullable(value: T?): Optional<T> {
            return if (value == null) empty() else of(value)
        }
    }
}
