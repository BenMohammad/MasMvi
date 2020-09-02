package com.benmohammad.masmvi

import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.benmohammad.masmvi.recomponents.components.ProjectSelectorComponent
import com.benmohammad.masmvi.recomponents.components.ProjectSelectorComponentState
import com.benmohammad.masmvi.recomponents.components.WildCardListComponent
import com.benmohammad.masmvi.recomponents.components.WildCardListComponentState
import com.benmohammad.masmvi.recomponents.redux.middleware.appMiddleware
import com.benmohammad.masmvi.recomponents.redux.reducers.appReducer
import com.benmohammad.masmvi.recomponents.redux.state.AppState
import com.netflix.recomponents.StoreImpl
import com.netflix.recomponents.UIComponent
import com.netflix.recomponents.UIComponentManager


private val store = StoreImpl(
        reducer = ::appReducer,
        initialState = AppState(),
        middleware = listOf(::appMiddleware)
)
class ReComponentsSampleActivity: AppCompatActivity() {

    private val uiComponentManager by lazy {
        ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return UIComponentManager(store) as T
            }
        }
        )[UIComponentManager::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.linearlayout_main)
        initComponents(findViewById(R.id.root))
    }


    private fun initComponents(container: ViewGroup) {
        with(uiComponentManager) {
            subscribe(ProjectSelectorComponent(container)) {
                ProjectSelectorComponentState(
                    selectedProjected = (it as AppState).selectedProject,
                    projects = it.projects
                )
            }

            subscribe(WildCardListComponent(container)) {
                WildCardListComponentState(
                    isLoading = (it as AppState).isFetching,
                    wildCardList = it.wildcards
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        uiComponentManager.store.unsubscribeAll()
    }
}