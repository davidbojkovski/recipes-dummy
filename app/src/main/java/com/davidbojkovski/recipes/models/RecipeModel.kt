package com.davidbojkovski.recipes.models

import kotlinx.serialization.Serializable

@Serializable
data class RecipeModel(
    val id: Int,
    val name: String?,
    val image: String?,
    val ingredients: List<String?>?,
    val instructions: List<String?>?,
    val prepTimeMinutes: Int?,
    val cookTimeMinutes: Int?,
    val servings: Int?,
    val difficulty: String?,
    val cuisine: String?,
    val caloriesPerServing: Int?,
    val tags: List<String?>?,
    val userId: Int?,
    val rating: Double?,
    val reviewCount: Int?,
    val mealType: List<String?>?
)

/*
{
    "id": 1,
    "name": "Classic Margherita Pizza",
    "ingredients": [
    "Pizza dough",
    "Tomato sauce",
    "Fresh mozzarella cheese",
    "Fresh basil leaves",
    "Olive oil",
    "Salt and pepper to taste"
    ],
    "instructions": [
    "Preheat the oven to 475°F (245°C).",
    "Roll out the pizza dough and spread tomato sauce evenly.",
    "Top with slices of fresh mozzarella and fresh basil leaves.",
    "Drizzle with olive oil and season with salt and pepper.",
    "Bake in the preheated oven for 12-15 minutes or until the crust is golden brown.",
    "Slice and serve hot."
    ],
    "prepTimeMinutes": 20,
    "cookTimeMinutes": 15,
    "servings": 4,
    "difficulty": "Easy",
    "cuisine": "Italian",
    "caloriesPerServing": 300,
    "tags": [
    "Pizza",
    "Italian"
    ],
    "userId": 45,
    "image": "https://cdn.dummyjson.com/recipe-images/1.webp",
    "rating": 4.6,
    "reviewCount": 3,
    "mealType": [
    "Dinner"
    ]
}
*/
