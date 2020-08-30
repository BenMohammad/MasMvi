package com.benmohammad.masmvi.recomponents.redux.actions

import com.benmohammad.masmvi.recomponents.models.WildCard
import com.netflix.recomponents.Action

data class FetchData(val projectId: Int?): Action
data class DataFetched(val wildCards: List<WildCard>): Action