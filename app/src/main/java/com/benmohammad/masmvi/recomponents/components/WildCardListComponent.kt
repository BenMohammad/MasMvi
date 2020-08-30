package com.benmohammad.masmvi.recomponents.components

import android.animation.LayoutTransition
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.VisibleForTesting
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.benmohammad.masmvi.R
import com.benmohammad.masmvi.recomponents.models.WildCard
import com.netflix.recomponents.ListComponentAdapter
import com.netflix.recomponents.UIComponent
import com.netflix.recomponents.UIComponentForList
import io.reactivex.internal.disposables.ListCompositeDisposable
import kotlinx.android.synthetic.main.wildcard_list.view.*
import java.io.LineNumberReader

data class WildCardListComponentState(
    val isLoading: Boolean  = false,
    val wildCardList: List<WildCard>?
)

class WildCardListComponent(
    container: ViewGroup
): UIComponent<WildCardListComponentState>() {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val wildCardDataSetList: MutableList<WildCardComponentState> = mutableListOf()

    private val rootView = LayoutInflater.from(container.context).inflate(
        R.layout.wildcard_list,
        container,
        true
    ) as ViewGroup

    private val recyclerView by lazy {
        rootView.findViewById<RecyclerView>(R.id.wildcardList)
    }

    private val swipeRefreshLayout: SwipeRefreshLayout by lazy {
        rootView.findViewById<SwipeRefreshLayout>(R.id.generic_list_srl)
    }

    private val adapter = object :
    ListComponentAdapter<WildCardComponentState>() {
        override fun getComponentForList(viewType: Int): UIComponentForList<WildCardComponentState> {
            return WildCardComponent(
                LayoutInflater.from(container.context).inflate(R.layout.wildcard_list_item, container, false) as ViewGroup
            )
        }
    }

    init {
        recyclerView.layoutManager = LinearLayoutManager(container.context)
        recyclerView.adapter = adapter
        recyclerView.visibility = View.VISIBLE
    }



    override fun render(state: WildCardListComponentState) {
        if(state.wildCardList.isNullOrEmpty()) {
            recyclerView.visibility = View.GONE
        } else {
            recyclerView.visibility = View.VISIBLE
        }

        swipeRefreshLayout.isRefreshing = state.isLoading
        wildCardDataSetList.clear()
        state.wildCardList?.map {
            wildCardDataSetList.add(
                WildCardComponentState(
                    wildCard = it
                )
            )
        }
        adapter.update(wildCardDataSetList)
    }
}


fun getRootView(rootView: ViewGroup, layoutRes: Int, forceInflation: Boolean = false): ViewGroup {
    return if(rootView.childCount > 0 && !forceInflation) {
        rootView
    } else {
        LayoutInflater.from(rootView.context).inflate(
            layoutRes,
            rootView,
            true
        ) as ViewGroup
    }
}