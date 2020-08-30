package com.benmohammad.masmvi.recomponents.redux.reducers

import com.benmohammad.masmvi.recomponents.redux.actions.DataFetched
import com.benmohammad.masmvi.recomponents.redux.actions.FetchData
import com.benmohammad.masmvi.recomponents.redux.state.AppState
import com.netflix.recomponents.Action

fun appReducer(action: Action, state: AppState? = AppState()): AppState {
    var newState = state ?: AppState()
    when(action) {
        is FetchData -> {
            newState = newState.copy(isFetching = true)
        }
        is DataFetched -> {
            newState = newState.copy(isFetching = false, wildcards = action.wildCards)
        }
    }
    return newState
}