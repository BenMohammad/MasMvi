package com.benmohammad.masmvi.recomponents.components

import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.annotation.VisibleForTesting
import androidx.core.content.res.FontResourcesParserCompat
import com.benmohammad.masmvi.R
import com.benmohammad.masmvi.recomponents.models.Project
import com.benmohammad.masmvi.recomponents.redux.actions.FetchData
import com.netflix.recomponents.UIComponent
import kotlinx.android.synthetic.main.project_selector.view.*

data class ProjectSelectorComponentState(
    val projects: List<Project>,
    val selectedProjected: Project?
)

class ProjectSelectorComponent(
    container: ViewGroup
): UIComponent<ProjectSelectorComponentState>() {

    companion object {
        private const val TAG = "ProjectSelector"
    }

    private val rootView = getRootView(container, R.layout.project_selector)

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val list = mutableListOf<String>()

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val spinnerAdapter by lazy { ArrayAdapter(rootView.context, R.layout.simple_spinner_item, list)}

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val selectorView: Spinner by lazy {
        rootView.findViewById<Spinner>(R.id.projectSelector)
            .apply {
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                adapter = spinnerAdapter

                onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        dispatchFunction(FetchData(position + 1))
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {

                    }
                }
            }
    }

    override fun render(state: ProjectSelectorComponentState) {
        if(state.projects.isNotEmpty()) {
            var selectedProjectIndex = 0

            list.clear().apply {
                state.projects.map {
                    list.add(it.title)
                    if(it.id == state.selectedProjected?.id) {
                        selectedProjectIndex = list.size - 1
                    }
                }
            }
            spinnerAdapter.notifyDataSetChanged()
            selectorView.tag = selectedProjectIndex
            selectorView.setSelection(selectedProjectIndex)
            selectorView.visibility = View.VISIBLE
        }
    }
}