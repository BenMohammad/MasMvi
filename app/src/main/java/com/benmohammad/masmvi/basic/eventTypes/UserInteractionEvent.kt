package com.benmohammad.masmvi.basic.eventTypes

import com.benmohammad.componentization.ComponentEvent

sealed class UserInteractionEvent: ComponentEvent() {

    object IntentTapRetry: UserInteractionEvent()
}