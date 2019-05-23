package com.mindera.skeletoid.kt.extensions.utils

import android.accounts.Account
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.text.style.URLSpan
import com.mindera.skeletoid.kt.extensions.BuildConfig
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.math.BigDecimal
import java.math.BigInteger
import java.util.Date
import kotlin.test.assertEquals
import kotlin.test.assertFalse

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class, manifest = Config.NONE)
class ParcelUnitTest {

    @Test
    fun testCreateDefaultParcel() {
        val testParcel = TestParcel()
        assertFalse(testParcel.isTest)
        assertEquals("string", testParcel.nullable)
        assertEquals(HideAndSeek.TWO, testParcel.hideAndSeek)
        assertEquals(BigInteger("4"), testParcel.bigIntegr)
        assertEquals(BigDecimal("12.2"), testParcel.bigDec)
        assertEquals(Account("name", "type").toString(), testParcel.account.toString())

        val parcel = Parcel.obtain()
        testParcel.writeToParcel(parcel, testParcel.describeContents())
        parcel.setDataPosition(0)

        val resultTestParcel = TestParcel.CREATOR.createFromParcel(parcel)

        assertFalse(resultTestParcel.isTest)
        assertEquals("string", resultTestParcel.nullable)
        assertEquals(HideAndSeek.TWO, resultTestParcel.hideAndSeek)
        assertEquals(BigInteger("4"), resultTestParcel.bigIntegr)
        assertEquals(BigDecimal("12.2"), resultTestParcel.bigDec)
        assertEquals(Account("name", "type").toString(), resultTestParcel.account.toString())
    }

    enum class HideAndSeek {
        ONE,
        TWO,
        THREE
    }

    class TestParcel() : Parcelable {

        var isTest = false
        var hideAndSeek: HideAndSeek? = HideAndSeek.TWO
        var integr: Int = 1
        var nullable: String? = "string"
        var date: Date? = Date()
        var bigIntegr: BigInteger? = BigInteger("4")
        var bigDec: BigDecimal? = BigDecimal("12.2")
        var account: Account? = Account("name", "type")

        constructor(parcel: Parcel) : this() {
            isTest = parcel.readBoolean()
            hideAndSeek = parcel.readEnum<HideAndSeek>()
            integr = parcel.readInt()
            nullable = parcel.readNullable { parcel.readString() }
            date = parcel.readDate()
            bigIntegr = parcel.readBigInteger()
            bigDec = parcel.readBigDecimal()
            account = parcel.readTypedObjectCompat(Account.CREATOR)
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeBoolean(isTest)
            parcel.writeEnum(hideAndSeek)
            parcel.writeInt(integr)
            parcel.writeNullable(nullable) { parcel.writeString(nullable) }
            parcel.writeDate(date)
            parcel.writeBigInteger(bigIntegr)
            parcel.writeBigDecimal(bigDec)
            parcel.writeTypedObjectCompat(account, account?.describeContents() ?: 0)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object {
            @JvmField
            val CREATOR = parcelableCreator(::TestParcel)
        }
    }
}