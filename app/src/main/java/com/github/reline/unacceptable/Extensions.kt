package com.github.reline.unacceptable

import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.SparseArray

fun <T> SparseArray<T>.getOrPut(key: Int, defaultValue: T): T {
    return this.get(key)
            ?: defaultValue.also { default -> this.put(key, default) }
}

fun Vibrator.vibrate(milliseconds: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        this.vibrate(VibrationEffect.createOneShot(milliseconds.toLong(), VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
        @Suppress("DEPRECATION")
        this.vibrate(milliseconds.toLong())
    }
}
