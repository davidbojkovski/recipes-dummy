package com.davidbojkovski.recipes.repository

import android.util.Log
import com.davidbojkovski.recipes.models.RecipeModel
import com.davidbojkovski.recipes.models.RecipesModel
import com.davidbojkovski.recipes.cache.MemoryCache
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class RecipeRepositoryImpl(
    private val httpClient: HttpClient,
    private val recipesMemoryCache: MemoryCache<String, List<RecipeModel?>?>,
    private val recipeMemoryCache: MemoryCache<String, RecipeModel?>,
    private val tagsMemoryCache: MemoryCache<String, List<String?>?>
) : RecipeRepository {

    companion object {
        const val BASE_URL = "https://dummyjson.com"
        val TAG: String = RecipeRepositoryImpl::class.java.simpleName
    }

    override suspend fun getRecipes(): List<RecipeModel?>? {
        try {
            val url = "$BASE_URL/recipes/"

            recipesMemoryCache.get(url)?.let {
                Log.d(TAG, "returning recipes from memory cache")
                return it
            }

            val response = httpClient.get(url)
            val body = response.body<RecipesModel?>()
            val recipes = body?.recipes
            recipesMemoryCache.put(url, recipes)

            return recipes
        } catch (e: Exception) {
            Log.d(TAG, "exception: ${e.localizedMessage}")
            Log.d(TAG, "message: ${e.message}")
            Log.d(TAG, "cause: ${e.cause}")
            Log.d(TAG, e.toString())
            return null
        }
    }

    override suspend fun getRecipe(id: Int): RecipeModel? {
        try {
            val url = "$BASE_URL/recipes/${id}"

            recipeMemoryCache.get(url)?.let {
                Log.d(TAG, "returning recipe with id: ${id} from memory cache")
                return it
            }

            val response = httpClient.get(url)
            val recipe = response.body<RecipeModel?>()
            recipeMemoryCache.put(url, recipe)

            return recipe
        } catch (e: Exception) {
            Log.d(TAG, "exception: ${e.localizedMessage}")
            return null
        }
    }

    override suspend fun searchRecipes(query: String): List<RecipeModel?>? {
        try {
            val url = "$BASE_URL/recipes/search?q=${query}"

            recipesMemoryCache.get(url)?.let {
                Log.d(TAG, "returning search recipes from memory cache")
                return it
            }

            val response = httpClient.get(url)
            val body = response.body<RecipesModel?>()
            val recipes = body?.recipes
            recipesMemoryCache.put(url, recipes)

            return recipes
        } catch (e: Exception) {
            Log.d(TAG, "exception: ${e.localizedMessage}")
            return null
        }
    }

    override suspend fun getTags(): List<String?>? {
        try {
            val url = "$BASE_URL/recipes/tags"

            tagsMemoryCache.get(url)?.let {
                Log.d(TAG, "returning tags from memory cache")
                return it
            }
            val response = httpClient.get(url)
            val tags = response.body<List<String?>?>()
            tagsMemoryCache.put(url, tags)

            return tags
        } catch (e: Exception) {
            Log.d(TAG, "exception: ${e.localizedMessage}")
            return null
        }
    }

    override suspend fun searchRecipesByTag(tag: String): List<RecipeModel?>? {
        try {
            val url = "$BASE_URL/recipes/tag/${tag}"

            recipesMemoryCache.get(url)?.let {
                Log.d(TAG, "returning tag search recipes from memory cache")
                return it
            }

            val response = httpClient.get(url)
            val body = response.body<RecipesModel?>()
            val recipes = body?.recipes
            recipesMemoryCache.put(url, recipes)

            return recipes
        } catch (e: Exception) {
            Log.d(TAG, "exception: ${e.localizedMessage}")
            return null
        }
    }

    override suspend fun clearCaches() {
        try {
            recipesMemoryCache.clear()
            recipeMemoryCache.clear()
            tagsMemoryCache.clear()
        } catch (e: Exception) {
            Log.d(TAG, "exception: ${e.localizedMessage}")
        }
    }
}