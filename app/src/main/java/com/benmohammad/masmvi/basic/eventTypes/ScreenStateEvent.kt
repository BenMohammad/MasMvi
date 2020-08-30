package com.benmohammad.masmvi.basic.eventTypes

import com.benmohammad.componentization.ComponentEvent

sealed class ScreenStateEvent: ComponentEvent() {

    object Loading: ScreenStateEvent()
    object Loaded: ScreenStateEvent()
    object Error: ScreenStateEvent()
}