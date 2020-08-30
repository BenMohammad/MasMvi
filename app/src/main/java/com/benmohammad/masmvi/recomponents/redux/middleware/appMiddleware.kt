package com.benmohammad.masmvi.recomponents.redux.middleware

import com.benmohammad.masmvi.recomponents.models.getWildCardMockList
import com.benmohammad.masmvi.recomponents.redux.actions.DataFetched
import com.benmohammad.masmvi.recomponents.redux.actions.FetchData
import com.benmohammad.masmvi.recomponents.redux.state.AppState
import com.netflix.recomponents.Action
import com.netflix.recomponents.DispatchFunction
import com.netflix.recomponents.Next
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

fun appMiddleware(
    dispatch: DispatchFunction,
    state: AppState,
    action: Action,
    next: Next<AppState>
): Action {
    if(action is FetchData) {
        Observable.just(Any())
            .observeOn(AndroidSchedulers.mainThread())
            .delay(2, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext{
                dispatch(
                    DataFetched(
                        getWildCardMockList(action.projectId)
                    )
                )
            }.subscribe()
    }

    return next(state, action, dispatch)
}