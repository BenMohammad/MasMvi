package com.benmohammad.masmvi

import android.net.Uri
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.benmohammad.componentization.EventBusFactory
import com.benmohammad.masmvi.basic.components.LoadingComponent
import com.benmohammad.masmvi.basic.eventTypes.ScreenStateEvent
import com.benmohammad.masmvi.player.components.PlayerEvents
import com.benmohammad.masmvi.player.components.PrimaryControlsComponent
import com.benmohammad.masmvi.player.components.uiViews.PlayerUserInteractionsEvents
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.DefaultTrackNameProvider
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.*
import com.google.android.exoplayer2.util.Util
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

class PlayerActivity: AppCompatActivity(), LifecycleOwner {

    private var player: SimpleExoPlayer? = null
    private val playerView: PlayerView by lazy {findViewById<PlayerView>(R.id.player_view)}
    private lateinit var primaryControlsComponent: PrimaryControlsComponent
    private lateinit var lifecycleRegistry: LifecycleRegistry

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleRegistry = LifecycleRegistry(this)
        lifecycleRegistry.markState(Lifecycle.State.CREATED)
        setContentView(R.layout.activity_player)
        val container = findViewById<ConstraintLayout>(R.id.player_root)
        initializeUIComponents(container)
        layoutUIComponents(container)
    }

    private fun initializeUIComponents(container: ViewGroup) {
        LoadingComponent(container, EventBusFactory.get(this))
        primaryControlsComponent = PrimaryControlsComponent(container, EventBusFactory.get(this))
        primaryControlsComponent.getUserInteractionEvents()
            .subscribe{
                when(it) {
                    PlayerUserInteractionsEvents.IntentPlayPauseClicked -> {
                        player?.playWhenReady = !player?.playWhenReady!!
                    }
                    PlayerUserInteractionsEvents.IntentRwClicked -> {
                        player?.seekTo(player?.currentPosition!! / 2)
                    }
                    PlayerUserInteractionsEvents.IntentFwClicked -> {
                        player?.seekTo(player?.currentPosition!! * 2)
                    }
                }
            }
    }

    private fun layoutUIComponents(rootViewContainer: ConstraintLayout) {
        val mainContainerConstraintSet = ConstraintSet()
        mainContainerConstraintSet.clone(rootViewContainer)

        mainContainerConstraintSet.connect(
            primaryControlsComponent.getContainerId(),
            ConstraintSet.TOP,
            ConstraintSet.PARENT_ID,
            ConstraintSet.TOP
        )
        mainContainerConstraintSet.connect(
            primaryControlsComponent.getContainerId(),
            ConstraintSet.BOTTOM,
            ConstraintSet.PARENT_ID,
            ConstraintSet.BOTTOM
        )

        mainContainerConstraintSet.applyTo(rootViewContainer)
    }

    override fun onStart() {
        super.onStart()
        initializePlayer()
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    private fun initializePlayer() {
        playerView.requestFocus()
        player = ExoPlayerFactory.newSimpleInstance(
            this,
            DefaultTrackSelector(AdaptiveTrackSelection.Factory(DefaultBandwidthMeter()))
        )
        player?.let {
            playerView.player = it
            it.addListener(PlayerEventListener(EventBusFactory.get(this)))
        }

        Observable.just(Any())
            .delay(5, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                val mediaSource = ExtractorMediaSource.Factory(
                    DefaultDataSourceFactory(
                        this, Util.getUserAgent(this, "mediaPlayerSample"),
                        DefaultBandwidthMeter() as TransferListener<in DataSource>
                    )
                ).createMediaSource(Uri.parse("http://www.clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"))

                player?.prepare(mediaSource, false, false)
            }
            .subscribe()

        player?.playWhenReady = true
    }

    private fun releasePlayer() {
        player?.let {
            it.release()
            player = null
        }
    }

    private inner class PlayerEventListener(val eventBusFactory: EventBusFactory): Player.DefaultEventListener() {
        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            when(playbackState) {
                Player.STATE_IDLE -> {
                    eventBusFactory.emit(ScreenStateEvent::class.java, ScreenStateEvent.Loading)
                }
                Player.STATE_BUFFERING -> {
                    eventBusFactory.emit(ScreenStateEvent::class.java, ScreenStateEvent.Loading)
                    eventBusFactory.emit(PlayerEvents::class.java, PlayerEvents.Buffering)
                }
                Player.STATE_READY -> {
                    eventBusFactory.emit(ScreenStateEvent::class.java, ScreenStateEvent.Loaded)
                    if(playWhenReady) {
                        eventBusFactory.emit(PlayerEvents::class.java, PlayerEvents.PlayStarted)
                    } else {
                        eventBusFactory.emit(PlayerEvents::class.java, PlayerEvents.Paused)
                    }
                }
                Player.STATE_IDLE -> {

                }
            }
        }
    }

    override fun getLifecycle(): Lifecycle {
        return lifecycleRegistry
    }
}