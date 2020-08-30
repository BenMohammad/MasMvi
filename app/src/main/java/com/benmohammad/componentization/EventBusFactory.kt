package com.benmohammad.componentization

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

class EventBusFactory private constructor(val owner: LifecycleOwner) {

    companion object {
        private val buses = mutableMapOf<LifecycleOwner, EventBusFactory>()

        @JvmStatic
        fun get(lifeCycleOwner: LifecycleOwner): EventBusFactory {
            return with(lifeCycleOwner) {
                var bus = buses[lifeCycleOwner]
                if(bus == null) {
                    bus = EventBusFactory(lifeCycleOwner)
                    buses[lifeCycleOwner] = bus
                    lifeCycleOwner.lifecycle.addObserver(bus.observer)
                }
                bus
            }
        }
    }
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val map = HashMap<Class<*>, Subject<*>>()
    internal val observer = object: LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestroy() {
            map.forEach{entry -> entry.value.onComplete()}
            buses.remove(owner)
        }
    }

    private fun <T> create(clazz: Class<T>): Subject<T> {
        val subject = PublishSubject.create<T>().toSerialized()
        map[clazz] = subject
        return subject
    }

    fun <T: ComponentEvent> emit(clazz: Class<T>, event: T) {
        val subject = if(map[clazz] != null) map[clazz] else create(clazz)
        (subject as Subject<T>).onNext(event)
    }

    fun <T: ComponentEvent> getSafeManagedObservable(clazz: Class<T>): Observable<T> {
        return if(map[clazz] != null) map[clazz] as Observable<T> else create(clazz)
    }

    fun getDestroyObservable(): Observable<Unit> {
        return owner.createDestroyObservable()
    }

    inline fun <reified T: ComponentEvent> LifecycleOwner.emit(event: T) =
        with(EventBusFactory.get(this)) {
            getSafeManagedObservable(T::class.java)
            emit(T::class.java, event)
        }

    inline fun <reified T: ComponentEvent> LifecycleOwner.getSafeManagedObservable(): Observable<T> =
        EventBusFactory.get(this).getSafeManagedObservable(T::class.java)


    inline fun LifecycleOwner?.createDestroyObservable(): Observable<Unit> {
        return Observable.create{ emitter ->
            if(this == null||this.lifecycle.currentState == Lifecycle.State.DESTROYED) {
                emitter.onNext(Unit)
                emitter.onComplete()
                return@create
            }
            this.lifecycle.addObserver(object: LifecycleObserver {
                @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
                fun emitDestroy() {
                    if(emitter.isDisposed) {
                        emitter.onNext(Unit)
                        emitter.onComplete()
                    }
                }
            })
        }
    }
}