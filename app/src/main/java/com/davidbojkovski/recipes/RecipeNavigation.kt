package com.davidbojkovski.recipes


/**
 * Helper class for the Jetpack compose navigation
 */
enum class RecipeNavigation(val route: String) {
    RECIPES("recipes"),
    RECIPE_DETAILS("recipes/details")
}

const val DETAILS_ARG = "id"
