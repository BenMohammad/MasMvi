package com.benmohammad.masmvi.basic.components.uiViews

import android.view.ViewGroup
import com.benmohammad.componentization.EventBusFactory
import com.benmohammad.componentization.UIView
import com.benmohammad.masmvi.basic.eventTypes.UserInteractionEvent

class ErrorView(container: ViewGroup, eventBusFactory: EventBusFactory): UIView<UserInteractionEvent>(container) {

    override val containerId: Int
        get() = TODO("Not yet implemented")

    override fun show() {
        TODO("Not yet implemented")
    }

    override fun hide() {
        TODO("Not yet implemented")
    }
}