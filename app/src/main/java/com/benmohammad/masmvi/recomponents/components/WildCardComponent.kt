package com.benmohammad.masmvi.recomponents.components

import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.VisibleForTesting
import androidx.core.view.contains
import com.benmohammad.masmvi.R
import com.benmohammad.masmvi.recomponents.models.WildCard
import com.netflix.recomponents.ComparableById
import com.netflix.recomponents.UIComponentForList
import kotlinx.android.synthetic.main.wildcard_list_item.view.*

data class WildCardComponentState(
    val wildCard: WildCard
): ComparableById {
    override fun getIdForComparison(): String {
        return wildCard.id
    }
}

class WildCardComponent(private val container: ViewGroup): UIComponentForList<WildCardComponentState>(container) {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val wildCardMessage by lazy {
        container.findViewById<TextView>(R.id.title)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val wildCardCreated by lazy {
        container.findViewById<TextView>(R.id.by)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    var wildCardDataModel: WildCardComponentState? = null

    override fun render(state: WildCardComponentState) {
        wildCardDataModel = state
        wildCardMessage.text = state.wildCard.message
        wildCardCreated.text = container.context.getString(R.string.wildcard_created_at, state.wildCard.createdBy)
    }
}