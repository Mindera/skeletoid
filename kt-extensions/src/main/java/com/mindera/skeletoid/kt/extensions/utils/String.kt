package com.mindera.skeletoid.kt.extensions.utils

fun String.removeSpaces() = this.replace(" ", "")

fun String.digits() = this.map(Character::getNumericValue)

fun String?.removeCharacters(charactersToRemove: String): String? {
    return this?.replace(Regex("[$charactersToRemove]"), "") ?: this
}

fun String.matchesEntireRegex(regex: Regex): Boolean {
    return regex.matchEntire(this)?.value?.isNotEmpty() == true
}

fun String.isEmailValid(): Boolean {
    return this.isNotBlank() && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}