package com.benmohammad.masmvi.basic.components

import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import com.benmohammad.componentization.EventBusFactory
import com.benmohammad.componentization.UIComponent
import com.benmohammad.masmvi.basic.components.uiViews.SuccessView
import com.benmohammad.masmvi.basic.eventTypes.ScreenStateEvent
import io.reactivex.Observable

open class SuccessComponent(container: ViewGroup, bus: EventBusFactory): UIComponent<Unit> {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val uiView = initView(container, bus)

    open fun initView(container: ViewGroup, bus: EventBusFactory): SuccessView {
        return SuccessView(container, bus)
    }

    override fun getContainerId(): Int {
        return uiView.containerId
    }

    override fun getUserInteractionEvents(): Observable<Unit> {
        return Observable.empty()
    }

    init {
        bus.getSafeManagedObservable(ScreenStateEvent::class.java)
            .subscribe{
                when(it) {
                    ScreenStateEvent.Loading -> {
                        uiView.hide()
                    }
                    ScreenStateEvent.Loaded -> {
                        uiView.show()
                    }
                    ScreenStateEvent.Error -> {
                        uiView.hide()
                    }
                }
            }
    }
}