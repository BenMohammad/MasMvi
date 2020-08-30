package com.benmohammad.masmvi.player.components

import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import com.benmohammad.componentization.ComponentEvent
import com.benmohammad.componentization.EventBusFactory
import com.benmohammad.componentization.UIComponent
import com.benmohammad.masmvi.player.components.uiViews.PlayerUserInteractionsEvents
import com.benmohammad.masmvi.player.components.uiViews.PrimaryControlsUIView
import io.reactivex.Observable

open class PrimaryControlsComponent(container: ViewGroup, private val bus: EventBusFactory): UIComponent<PlayerUserInteractionsEvents> {
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val uiView = initView(container, bus)

    open fun initView(container: ViewGroup, bus: EventBusFactory): PrimaryControlsUIView {
        return PrimaryControlsUIView(container, bus)
    }

    override fun getContainerId(): Int = uiView.containerId

    override fun getUserInteractionEvents(): Observable<PlayerUserInteractionsEvents> {
        return bus.getSafeManagedObservable(PlayerUserInteractionsEvents::class.java)
    }

    init {
        bus.getSafeManagedObservable(PlayerEvents::class.java)
            .subscribe{
                when(it) {
                    PlayerEvents.Buffering -> {
                        uiView.hide()
                    }
                    PlayerEvents.PlayStarted -> {
                        uiView.show()
                        uiView.setPlayPauseImageResource(true)
                    }
                    PlayerEvents.Paused -> {
                        uiView.show()
                        uiView.setPlayPauseImageResource(false)
                    }
                }
            }
    }
}

sealed class PlayerEvents: ComponentEvent() {
    object Buffering: PlayerEvents()
    object PlayStarted: PlayerEvents()
    object Paused: PlayerEvents()
}