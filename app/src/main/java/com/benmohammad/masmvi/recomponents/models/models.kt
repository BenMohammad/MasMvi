package com.benmohammad.masmvi.recomponents.models

data class WildCard(
    val createdAt: String? = null,
    val createdBy: String? = null,
    val id: String = "_id_",
    val message: String? = null,
    val startTime: String? = null
)

fun getWildCardMockList(projectId: Int?): List<WildCard> {
    val list =  mutableListOf<WildCard>()
    for(i in 0..1000) {
        list.add(
            WildCard(
                id = i.toString(),
                message = "Thread $projectId Message $1",
                createdBy = if(projectId == 1) "Ben" else "Dave"
            )
        )
    }
    return list
}

data class Project(
    val id: String = "_id_",
    val title: String = ""
)