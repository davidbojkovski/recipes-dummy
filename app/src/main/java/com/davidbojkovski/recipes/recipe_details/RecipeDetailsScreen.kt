package com.davidbojkovski.recipes.recipe_details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Dining
import androidx.compose.material.icons.outlined.HeatPump
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Scale
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.davidbojkovski.recipes.R
import com.davidbojkovski.recipes.models.RecipeModel
import com.davidbojkovski.recipes.cache_aware_image.CacheAwareImage
import com.davidbojkovski.recipes.common_components.IconWithDescription
import com.davidbojkovski.recipes.common_components.RecipeTagsComponent

@Composable
fun RecipeDetailsScreen(
    id: Int,
    modifier: Modifier = Modifier,
    viewModel: RecipeDetailsViewModel = hiltViewModel<RecipeDetailsViewModel>()
) {
    LaunchedEffect(Unit) {
        viewModel.getRecipe(id)
    }

    val recipe by viewModel.recipe.collectAsState()

    RecipeDetailsScreenContent(recipe, modifier)
}

@Composable
private fun RecipeDetailsScreenContent(recipe: RecipeModel?, modifier: Modifier = Modifier) {

    if (recipe != null) {
        Column(
            modifier = modifier
                .verticalScroll(rememberScrollState())
        ) {
            CacheAwareImage(
                recipe.image,
                recipe.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentScale = ContentScale.FillBounds
            )

            Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp)) {
                Text(
                    recipe.name ?: stringResource(R.string.not_available_text_placeholder),
                    modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
                    style = MaterialTheme.typography.headlineMedium
                )

                recipe.tags?.let { tags ->
                    RecipeTagsComponent(
                        tags = tags,
                        modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
                    )
                }

                Card {
                    Row(modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)) {
                        IconWithDescription(
                            icon = Icons.Outlined.HeatPump,
                            iconContentDescription = stringResource(R.string.recipe_cooking_time_icon_content_description),
                            description = "${recipe.cookTimeMinutes} mins",
                            modifier = Modifier.weight(1f)
                        )
                        IconWithDescription(
                            icon = Icons.Outlined.Scale,
                            iconContentDescription = stringResource(R.string.recipe_difficulty_icon_content_description),
                            description = recipe.difficulty
                                ?: stringResource(R.string.not_available_text_placeholder),
                            modifier = Modifier.weight(1f)
                        )
                        IconWithDescription(
                            icon = Icons.Outlined.Star,
                            iconContentDescription = stringResource(R.string.recipe_rating_icon_content_description),
                            description = "${recipe.rating} (${recipe.reviewCount})",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)) {
                        IconWithDescription(
                            icon = Icons.Outlined.ShoppingCart,
                            iconContentDescription = stringResource(R.string.recipe_preparation_time_icon__content_description),
                            description = "${recipe.prepTimeMinutes} mins",
                            modifier = Modifier.weight(1f)
                        )
                        IconWithDescription(
                            icon = Icons.Outlined.Person,
                            iconContentDescription = stringResource(R.string.recipe_servings_icon_content_description),
                            description = "${recipe.servings} servings",
                            modifier = Modifier.weight(1f)
                        )
                        IconWithDescription(
                            icon = Icons.Outlined.Dining,
                            iconContentDescription = stringResource(R.string.recipe_cuisine_icon_content_description),
                            description = recipe.cuisine
                                ?: stringResource(R.string.not_available_text_placeholder),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                recipe.ingredients?.let {
                    ExpandableComponentWithNumberedList(
                        stringResource(R.string.recipe_ingredients_section_title),
                        it,
                        modifier.padding(top = 8.dp, bottom = 8.dp)
                    )
                }

                recipe.instructions?.let {
                    ExpandableComponentWithNumberedList(
                        stringResource(R.string.recipe_instructions_section_title),
                        it,
                        modifier.padding(top = 8.dp, bottom = 8.dp)
                    )
                }
            }
        }
    } else {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(stringResource(R.string.recipe_details_not_available))
        }
    }
}

@Preview
@Composable
private fun RecipeDetailsScreenContentPreview() {
    val mockRecipe = RecipeModel(
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

    RecipeDetailsScreenContent(mockRecipe)
}

