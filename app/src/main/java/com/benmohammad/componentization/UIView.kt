package com.benmohammad.componentization

import android.view.ViewGroup
import androidx.annotation.IdRes

abstract class UIView<T>(val container: ViewGroup) {
    @get:IdRes
    abstract val containerId: Int

    abstract fun show()

    abstract fun hide()
}