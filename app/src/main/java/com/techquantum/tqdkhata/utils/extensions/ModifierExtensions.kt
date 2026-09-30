package com.techquantum.tqdkhata.utils.extensions

import androidx.compose.ui.Modifier

inline fun Modifier.applyIf(condition: Boolean, crossinline transform: Modifier.() -> Modifier): Modifier {
    return if (condition) this.transform() else this
}
