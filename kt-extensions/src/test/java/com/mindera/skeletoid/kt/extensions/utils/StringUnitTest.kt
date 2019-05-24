package com.mindera.skeletoid.kt.extensions.utils

import com.mindera.skeletoid.kt.extensions.BuildConfig
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class, manifest = Config.NONE)
class StringUnitTest {

    @Test
    fun testDigits() {
        val digitsString = "1243514"
        val expectedDigits = arrayListOf<Int>().apply {
            add(1)
            add(2)
            add(4)
            add(3)
            add(5)
            add(1)
            add(4)
        }

        val actualDigits = digitsString.digits()

        assertEquals(expectedDigits, actualDigits)
    }

    @Test
    fun testNegativeDigits() {
        val digitsString = "-1-243514"

        val actualDigits = digitsString.digits()

        assertEquals(9, actualDigits.size)
    }

    @Test
    fun testDigitsEmptyString() {
        val digitsString = ""

        val actualDigits = digitsString.digits()

        assertEquals(0, actualDigits.size)
    }

    @Test
    fun testDigitsNonDigitString() {
        val digitsString = "absdef"

        val actualDigits = digitsString.digits()

        assertEquals(6, actualDigits.size)
    }

    @Test
    fun testRemoveSpaces() {
        val spacedString = "hi There Mindera!  "
        val expectedString = "hiThereMindera!"

        val actualString = spacedString.removeSpaces()

        assertEquals(expectedString, actualString)
    }

    @Test
    fun testRemoveSpacesEmptyString() {
        val spacedString = ""
        val expectedString = ""

        val actualString = spacedString.removeSpaces()

        assertEquals(expectedString, actualString)
    }

    @Test
    fun testRemoveSpacesWhitespaced() {
        val spacedString = "   "
        val expectedString = ""

        val actualString = spacedString.removeSpaces()

        assertEquals(expectedString, actualString)
    }

    @Test
    fun testRemoveStringFromString() {
        val characterToRemove = "hi"
        val expectedString = ""

        val actualString = characterToRemove.removeCharacters(characterToRemove)

        assertEquals(expectedString, actualString)
    }

    @Test
    fun testRemoveCharacters() {
        val characterToRemove = "hi "
        val string = "hi there mindera!  "
        val expectedString = "teremndera!"

        val actualString = string.removeCharacters(characterToRemove)

        assertEquals(expectedString, actualString)
    }

    @Test
    fun testRemoveCharacter() {
        val characterToRemove = "h"
        val string = "hi there mindera!  "
        val expectedString = "i tere mindera!  "

        val actualString = string.removeCharacters(characterToRemove)

        assertEquals(expectedString, actualString)
    }

    @Test
    fun testRemoveCharactersEmptyString() {
        val characterToRemove = "h"
        val string = ""
        val expectedString = ""

        val actualString = string.removeCharacters(characterToRemove)

        assertEquals(expectedString, actualString)
    }

    @Test
    fun testRemoveCharactersNullString() {
        val characterToRemove = "h"
        val string: String? = null
        val expectedString = null

        val actualString = string.removeCharacters(characterToRemove)

        assertEquals(expectedString, actualString)
    }

    @Test
    fun testRemoveNoCharacters() {
        val characterToRemove = "o"
        val string = "hi there Mindera!  "

        val actualString = string.removeCharacters(characterToRemove)

        assertEquals(string, actualString)
    }

    @Test
    fun testMatchesEntireRegexEqual() {
        val characterToMatch = "hi"
        val string = "hi"

        assertTrue(string.matchesEntireRegex(characterToMatch.toRegex()))
    }

    @Test
    fun testMatchesEntireRegexHasMatch() {
        val characterToMatch = "hi .*"
        val string = "hi there Mindera!"

        assertTrue(string.matchesEntireRegex(characterToMatch.toRegex()))
    }

    @Test
    fun testMatchesEntireRegexNoMatch() {
        val characterToMatch = "hiya"
        val string = "hi there mindera!  "

        assertFalse(string.matchesEntireRegex(characterToMatch.toRegex()))
    }

    @Test
    fun testMatchesEntireRegexEmptyRegex() {
        val characterToMatch = ""
        val string = "hi there mindera!  "

        assertFalse(string.matchesEntireRegex(characterToMatch.toRegex()))
    }

    @Test
    fun testIsEmailValid() {
        val email = "email@email.com"

        assertTrue(email.isEmailValid())
    }

    @Test
    fun testIsEmailValidEmptyString() {
        val email = ""

        assertFalse(email.isEmailValid())
    }

    @Test
    fun testIsEmailValidNoDomain() {
        val email = "email@email"

        assertFalse(email.isEmailValid())
    }

    @Test
    fun testIsEmailValidDotNoDomain() {
        val email = "email@email."

        assertFalse(email.isEmailValid())
    }

    @Test
    fun testIsEmailValidNoAt() {
        val email = "emailemail.com"

        assertFalse(email.isEmailValid())
    }

    @Test
    fun testIsEmailValidNoName() {
        val email = "@email.com"

        assertFalse(email.isEmailValid())
    }

    @Test
    fun testIsEmailValidSpecialChars() {
        val email = "email*&@email.com"

        assertFalse(email.isEmailValid())
    }
}
