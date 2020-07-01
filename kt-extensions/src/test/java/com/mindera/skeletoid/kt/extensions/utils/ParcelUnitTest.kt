package com.mindera.skeletoid.kt.extensions.utils

import android.accounts.Account
import android.os.Parcel
import com.mindera.skeletoid.kt.extensions.BuildConfig
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.math.BigDecimal
import java.math.BigInteger
import java.util.Date
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class ParcelUnitTest {

    private lateinit var parcel: Parcel

    enum class HideAndSeek {
        ONE,
        TWO,
        THREE
    }

    @Before
    fun setUp() {
        parcel = Parcel.obtain()
    }

    @Test
    @Ignore
    fun testWriteReadBoolean() {
        parcel.writeBoolean(true)
        parcel.setDataPosition(0)

        assertTrue(parcel.readBoolean())
    }

    @Test
    fun testWriteReadNullable() {
        val nullableString: String? = "string"

        parcel.writeNullable(nullableString) { parcel.writeString(nullableString) }
        parcel.setDataPosition(0)

        assertEquals("string", parcel.readNullable { parcel.readString() })
    }

    @Test
    fun testWriteReadNullNullable() {
        val nullableString: String? = null

        parcel.writeNullable(nullableString) { parcel.writeString(nullableString) }
        parcel.setDataPosition(0)

        assertNull(parcel.readNullable { parcel.readString() })
    }

    @Test
    fun testWriteReadDate() {
        val date = Date()

        parcel.writeDate(date)
        parcel.setDataPosition(0)

        assertEquals(date, parcel.readDate())
    }

    @Test
    fun testWriteReadBigInteger() {
        val bigInt = BigInteger("45343234")

        parcel.writeBigInteger(bigInt)
        parcel.setDataPosition(0)

        assertEquals(bigInt, parcel.readBigInteger())
    }

    @Test
    fun testWriteReadBigDecimal() {
        val bigDecimal = BigDecimal("45343234.23")

        parcel.writeBigDecimal(bigDecimal)
        parcel.setDataPosition(0)

        assertEquals(bigDecimal, parcel.readBigDecimal())
    }

    @Test
    fun testWriteReadEnum() {
        val enum = HideAndSeek.ONE

        parcel.writeEnum(enum)
        parcel.setDataPosition(0)

        assertEquals(enum, parcel.readEnum<HideAndSeek>())
    }

    @Test
    fun testWriteReadTypedObjectCompat() {
        val typedObject = Account("name", "type")

        parcel.writeTypedObjectCompat(typedObject, typedObject.describeContents())
        parcel.setDataPosition(0)

        assertEquals(typedObject, parcel.readTypedObjectCompat(Account.CREATOR))
    }

    @Test
    fun testParcelableCreator() {
        val typedObject = Account("name", "type")

        parcel.writeTypedObjectCompat(typedObject, typedObject.describeContents())
        parcel.setDataPosition(0)

        assertEquals(typedObject, parcel.readTypedObjectCompat(parcelableCreator(::Account)))
    }
}