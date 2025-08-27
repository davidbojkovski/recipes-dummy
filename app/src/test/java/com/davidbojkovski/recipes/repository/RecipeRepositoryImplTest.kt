package com.davidbojkovski.recipes.repository

import android.util.Log
import com.davidbojkovski.recipes.models.RecipeModel
import com.davidbojkovski.recipes.cache.MemoryCache
import com.davidbojkovski.recipes.repository.RecipeRepositoryImpl.Companion.BASE_URL
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.serialization.json.Json
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RecipeRepositoryImplTest {
    private lateinit var httpClient: HttpClient
    private lateinit var recipesMemoryCache: MemoryCache<String, List<RecipeModel?>?>
    private lateinit var recipeMemoryCache: MemoryCache<String, RecipeModel?>
    private lateinit var tagsMemoryCache: MemoryCache<String, List<String?>?>
    private lateinit var repository: RecipeRepository

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0

        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun getRecipes_emptyCache_networkCall() = runTest(testDispatcher) {
        //GIVEN
        val url = "$BASE_URL/recipes/"
        val jsonResponse = """{
            "recipes": [
                {
                    "id": 1,
                    "name": "Classic Margherita Pizza",
                    "ingredients": ["Pizza dough"],
                    "instructions": ["Bake it"],
                    "prepTimeMinutes": 20,
                    "cookTimeMinutes": 15,
                    "servings": 4,
                    "difficulty": "Easy",
                    "cuisine": "Italian",
                    "caloriesPerServing": 300,
                    "tags": ["Pizza"],
                    "userId": 166,
                    "image": "https://cdn.dummyjson.com/recipe-images/1.webp",
                    "rating": 4.6,
                    "reviewCount": 98,
                    "mealType": ["Dinner"]
                }
            ],
            "total": 50,
            "skip": 0,
            "limit": 30
        }"""

        recipesMemoryCache = mockk()
        recipeMemoryCache = MemoryCache()
        tagsMemoryCache = MemoryCache()

        coEvery { recipesMemoryCache.get(url) } returns null
        coEvery { recipesMemoryCache.put(any(), any()) } returns Unit

        httpClient = HttpClient(MockEngine) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
            engine {
                addHandler { _ ->
                    respond(
                        content = jsonResponse,
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }
            }
        }
        repository =
            RecipeRepositoryImpl(httpClient, recipesMemoryCache, recipeMemoryCache, tagsMemoryCache)

        //WHEN
        val result = repository.getRecipes()
        advanceUntilIdle()

        //THEN
        assertEquals(1, result?.size)
        assertEquals("Classic Margherita Pizza", result?.first()?.name)

        coVerify(exactly = 1) { recipesMemoryCache.put(url, any()) }
    }

    @Test
    fun getRecipes_returnCache() = runTest(testDispatcher) {
        val url = "${BASE_URL}/recipes/"
        val cachedRecipes = listOf(
            RecipeModel(
                id = 1,
                name = "Classic Margherita Pizza",
                image = "https://cdn.dummyjson.com/recipe-images/1.webp",
                ingredients = listOf(
                    "Pizza dough",
                    "Tomato sauce",
                    "Fresh mozzarella cheese",
                    "Fresh basil leaves",
                    "Olive oil",
                    "Salt and pepper to taste"
                ),
                instructions = listOf(
                    "Preheat the oven to 475°F (245°C).",
                    "Roll out the pizza dough and spread tomato sauce evenly.",
                    "Top with slices of fresh mozzarella and fresh basil leaves.",
                    "Drizzle with olive oil and season with salt and pepper.",
                    "Bake in the preheated oven for 12-15 minutes or until the crust is golden brown.",
                    "Slice and serve hot."
                ),
                prepTimeMinutes = 20,
                cookTimeMinutes = 15,
                servings = 4,
                difficulty = "Easy",
                cuisine = "Italian",
                caloriesPerServing = 300,
                tags = listOf(
                    "Pizza",
                    "Italian"
                ),
                userId = 45,
                rating = 4.6,
                reviewCount = 3,
                mealType = listOf("Dinner")
            )
        )

        httpClient = mockk()
        recipesMemoryCache = mockk()
        recipeMemoryCache = MemoryCache()
        tagsMemoryCache = MemoryCache()

        coEvery { recipesMemoryCache.get(url) } returns cachedRecipes

        repository =
            RecipeRepositoryImpl(httpClient, recipesMemoryCache, recipeMemoryCache, tagsMemoryCache)

        // WHEN
        val result = repository.getRecipes()

        // THEN
        assertEquals(cachedRecipes, result)
        coVerify(exactly = 0) { recipesMemoryCache.put(any(), any()) }
    }
}