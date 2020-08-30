package com.benmohammad.masmvi.basic.components.uiViews

import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.benmohammad.componentization.EventBusFactory
import com.benmohammad.componentization.UIView
import com.benmohammad.masmvi.R
import com.benmohammad.masmvi.basic.eventTypes.UserInteractionEvent

class SuccessView(container: ViewGroup, eventBusFactory: EventBusFactory): UIView<UserInteractionEvent>(container) {

    private val view: View = LayoutInflater.from(container.context).inflate(R.layout.success, container, true)

    override val containerId: Int = view.id

    override fun show() {
        view.visibility = View.VISIBLE
    }

    override fun hide() {
        view.visibility = View.GONE
    }
}