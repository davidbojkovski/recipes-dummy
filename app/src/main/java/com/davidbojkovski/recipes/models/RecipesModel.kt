package com.davidbojkovski.recipes.models

import kotlinx.serialization.Serializable

@Serializable
data class RecipesModel(
    val recipes: List<RecipeModel?>?,
    val total: Int?,
    val skip: Int?,
    val limit: Int?
)