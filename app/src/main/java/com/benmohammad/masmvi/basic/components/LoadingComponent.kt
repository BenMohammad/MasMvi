package com.benmohammad.masmvi.basic.components

import android.provider.CalendarContract
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import com.benmohammad.componentization.EventBusFactory
import com.benmohammad.componentization.UIComponent
import com.benmohammad.masmvi.basic.components.uiViews.LoadingView
import com.benmohammad.masmvi.basic.eventTypes.ScreenStateEvent
import io.reactivex.Observable

open class LoadingComponent(container: ViewGroup, bus: EventBusFactory): UIComponent<Unit> {

    override fun getContainerId(): Int {
        return uiView.containerId
    }

    override fun getUserInteractionEvents(): Observable<Unit> {
        return Observable.empty()
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val uiView = initView(container)

    open fun initView(container: ViewGroup): LoadingView {
        return LoadingView(container)
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
                        uiView.hide()
                    }
                }
            }
    }

}