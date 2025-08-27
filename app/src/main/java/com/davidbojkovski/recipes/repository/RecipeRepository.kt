package com.davidbojkovski.recipes.repository

import com.davidbojkovski.recipes.models.RecipeModel

interface RecipeRepository {
    suspend fun getRecipes(): List<RecipeModel?>?
    suspend fun getRecipe(id: Int): RecipeModel?
    suspend fun searchRecipes(query: String): List<RecipeModel?>?
    suspend fun getTags(): List<String?>?
    suspend fun searchRecipesByTag(tag: String): List<RecipeModel?>?
    suspend fun clearCaches()
}