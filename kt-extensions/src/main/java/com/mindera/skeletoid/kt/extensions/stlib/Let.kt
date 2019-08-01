package com.mindera.skeletoid.kt.extensions.stlib

fun <T1 : Any, T2 : Any, R : Any> multipleLet(p1: T1?, p2: T2?, block: (T1, T2) -> R?): R? {
    return if (p1 != null && p2 != null) block(p1, p2) else null
}

fun <T1 : Any, T2 : Any, T3 : Any, R : Any> multipleLet(p1: T1?, p2: T2?, p3: T3?, block: (T1, T2, T3) -> R?): R? {
    return if (p1 != null && p2 != null && p3 != null) block(p1, p2, p3) else null
}

fun <T1 : Any, T2 : Any, T3 : Any, T4 : Any, R : Any> multipleLet(p1: T1?, p2: T2?, p3: T3?, p4: T4?, block: (T1, T2, T3, T4) -> R?): R? {
    return if (p1 != null && p2 != null && p3 != null && p4 != null) block(p1, p2, p3, p4) else null
}

fun <T1 : Any, T2 : Any, T3 : Any, T4 : Any, T5 : Any, R : Any> multipleLet(p1: T1?, p2: T2?, p3: T3?, p4: T4?, p5: T5?, block: (T1, T2, T3, T4, T5) -> R?): R? {
    return if (p1 != null && p2 != null && p3 != null && p4 != null && p5 != null) block(p1, p2, p3, p4, p5) else null
}

fun <T: Any> String?.letNotNullOrBlank(action: (String) -> T?): T? {
    return if (!this.isNullOrBlank()) action(this) else null
}

fun <T : Any> multipleLetNotNullOrBlank(str1: String?, str2: String?, action: (String, String) -> T?): T? {
    return if (!str1.isNullOrBlank() && !str2.isNullOrBlank()) action(str1, str2) else null
}

fun <T : Any> multipleLetNotNullOrBlank(str1: String?, str2: String?, str3: String?, action: (String, String, String) -> T?): T? {
    return if (!str1.isNullOrBlank() && !str2.isNullOrBlank() && !str3.isNullOrBlank()) {
        action(str1, str2, str3)
    } else {
        null
    }
}