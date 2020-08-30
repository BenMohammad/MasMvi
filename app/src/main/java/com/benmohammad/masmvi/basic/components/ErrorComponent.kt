package com.benmohammad.masmvi.basic.components

import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import com.benmohammad.componentization.EventBusFactory
import com.benmohammad.componentization.UIComponent
import com.benmohammad.masmvi.basic.components.uiViews.ErrorView
import com.benmohammad.masmvi.basic.eventTypes.ScreenStateEvent
import com.benmohammad.masmvi.basic.eventTypes.UserInteractionEvent
import io.reactivex.Observable

open class ErrorComponent(container: ViewGroup, private val bus: EventBusFactory): UIComponent<UserInteractionEvent> {


    override fun getContainerId(): Int {
        return uiView.containerId
    }

    override fun getUserInteractionEvents(): Observable<UserInteractionEvent> {
        return bus.getSafeManagedObservable(UserInteractionEvent::class.java)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val uiView = initView(container, bus)

    open fun initView(container: ViewGroup, bus: EventBusFactory): ErrorView {
        return ErrorView(container, bus)
    }

    init {
        bus.getSafeManagedObservable(ScreenStateEvent::class.java)
            .subscribe{
                when(it) {
                    ScreenStateEvent.Loading -> {
                        uiView.hide()
                    }
                    ScreenStateEvent.Loaded -> {
                        uiView.hide()
                    }
                    ScreenStateEvent.Error -> {
                        uiView.show()
                    }
                }
            }
    }

}