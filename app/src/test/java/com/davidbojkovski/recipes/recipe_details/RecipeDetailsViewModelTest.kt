package com.davidbojkovski.recipes.recipe_details

import com.davidbojkovski.recipes.models.RecipeModel
import com.davidbojkovski.recipes.repository.RecipeRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test


class RecipeDetailsViewModelTest {

    private lateinit var repository: RecipeRepository
    private lateinit var viewModel: RecipeDetailsViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        repository = mockk()
        viewModel = RecipeDetailsViewModel(repository)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun getRecipe() = runTest(testDispatcher) {
        //GIVEN
        val id = 1
        val recipe = RecipeModel(
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
        coEvery { repository.getRecipe(id) } returns recipe

        //WHEN
        viewModel.getRecipe(id)
        testDispatcher.scheduler.advanceUntilIdle()

        //THEN
        assertEquals(recipe, viewModel.recipe.value)
        assertEquals(recipe.name, viewModel.recipe.value?.name)
    }

}