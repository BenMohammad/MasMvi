package com.benmohammad.masmvi.recomponents.redux.state

import com.benmohammad.masmvi.recomponents.models.Project
import com.benmohammad.masmvi.recomponents.models.WildCard
import com.netflix.recomponents.StateType

data class AppState(
    var isFetching: Boolean = false,
    val wildcards: List<WildCard>? = listOf(),
    val projects: List<Project> = listOf(
        Project(
            id = "1",
            title = "Message Thread 1"
        ),
        Project(
            id = "2",
            title = "Message Thread 2"
        )
    ),
    val selectedProject: Project? = null
): StateType