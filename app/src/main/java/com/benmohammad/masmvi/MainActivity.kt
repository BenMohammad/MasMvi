package com.benmohammad.masmvi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.benmohammad.componentization.EventBusFactory
import com.benmohammad.masmvi.basic.components.ErrorComponent
import com.benmohammad.masmvi.basic.components.LoadingComponent
import com.benmohammad.masmvi.basic.components.SuccessComponent
import com.benmohammad.masmvi.basic.eventTypes.ScreenStateEvent
import com.benmohammad.masmvi.basic.eventTypes.UserInteractionEvent
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), LifecycleOwner {

    private lateinit var lifecycleRegistry: LifecycleRegistry

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleRegistry = LifecycleRegistry(this)
        lifecycleRegistry.markState(Lifecycle.State.CREATED)
        setContentView(R.layout.activity_main)

        initComponents(findViewById(R.id.root))
        startSimulation()
    }

    private fun initComponents(rootViewContainer: ViewGroup) {
        LoadingComponent(rootViewContainer, EventBusFactory.get(this))
        ErrorComponent(rootViewContainer, EventBusFactory.get(this))
            .getUserInteractionEvents()
            .subscribe{
                when(it) {
                    UserInteractionEvent.IntentTapRetry -> {
                        startSimulation()
                    }
                }
                SuccessComponent(rootViewContainer, EventBusFactory.get(this))
            }
    }

    private fun startSimulation() {
        Observable.just(Any())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                EventBusFactory.get(this)
                    .emit(ScreenStateEvent::class.java, ScreenStateEvent.Loading)
            }
            .delay(2, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { EventBusFactory.get(this)
                .emit(ScreenStateEvent::class.java, ScreenStateEvent.Loaded)
            }
            .delay(2, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                EventBusFactory.get(this).emit(ScreenStateEvent::class.java, ScreenStateEvent.Error)
            }
            .subscribe()
    }

    override fun getLifecycle(): Lifecycle {
        return lifecycleRegistry
    }
}