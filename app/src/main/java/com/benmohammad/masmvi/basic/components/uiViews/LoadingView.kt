package com.benmohammad.masmvi.basic.components.uiViews

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.benmohammad.componentization.UIView
import com.benmohammad.masmvi.R
import com.benmohammad.masmvi.basic.eventTypes.UserInteractionEvent


class LoadingView(container: ViewGroup): UIView<UserInteractionEvent>(container) {
    private val view: View = LayoutInflater.from(container.context).inflate(R.layout.loading, container, true)
        .findViewById(R.id.loadingSpinner)

    override val containerId: Int = view.id

    override fun show() {
        view.visibility = View.VISIBLE
    }

    override fun hide() {
        view.visibility = View.GONE
    }
}