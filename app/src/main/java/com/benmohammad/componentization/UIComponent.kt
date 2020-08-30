package com.benmohammad.componentization

import io.reactivex.Observable

interface UIComponent<T> {
    fun getContainerId(): Int
    fun getUserInteractionEvents(): Observable<T>
}