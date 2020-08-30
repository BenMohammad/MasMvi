package com.benmohammad.masmvi.player.components.uiViews

import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import com.benmohammad.componentization.ComponentEvent
import com.benmohammad.componentization.EventBusFactory
import com.benmohammad.componentization.UIView
import com.benmohammad.masmvi.R
import com.benmohammad.masmvi.basic.eventTypes.UserInteractionEvent

class PrimaryControlsUIView(container: ViewGroup, eventBusFactory: EventBusFactory): UIView<UserInteractionEvent>(container) {

    private val uiView: View = LayoutInflater.from(container.context).inflate(R.layout.primary_controls, container, false)

    private val playPauseBtn: ImageButton = uiView.findViewById(R.id.pause_btn)
    private val rwBtn: ImageButton = uiView.findViewById(R.id.rw_btn)
    private val fwBtn: ImageButton = uiView.findViewById(R.id.fw_btn)

    init {
        container.addView(uiView)

        playPauseBtn.setOnClickListener{
            eventBusFactory.emit(
                PlayerUserInteractionsEvents::class.java,
                PlayerUserInteractionsEvents.IntentPlayPauseClicked
            )
        }
        rwBtn.setOnClickListener {
            eventBusFactory.emit(
                PlayerUserInteractionsEvents::class.java,
                PlayerUserInteractionsEvents.IntentRwClicked
            )
        }
        fwBtn.setOnClickListener {
            eventBusFactory.emit(
                PlayerUserInteractionsEvents::class.java,
                PlayerUserInteractionsEvents.IntentFwClicked
            )
        }
    }

    override val containerId: Int = uiView.id

    override fun show() {
        uiView.visibility = View.VISIBLE
    }

    override fun hide() {
        uiView.visibility = View.GONE
    }

    fun setPlayPauseImageResource(pauseImage: Boolean) {
        if(pauseImage) {
            playPauseBtn.setImageResource(R.drawable.ic_player_pause)
        } else {
            playPauseBtn.setImageResource(R.drawable.ic_player_play)
        }
    }
}

sealed class PlayerUserInteractionsEvents: ComponentEvent() {
    object IntentPlayPauseClicked: PlayerUserInteractionsEvents()
    object IntentRwClicked: PlayerUserInteractionsEvents()
    object IntentFwClicked: PlayerUserInteractionsEvents()
}