package com.davidbojkovski.recipes.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.HeatPump
import androidx.compose.material.icons.outlined.Scale
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.davidbojkovski.recipes.models.RecipeModel
import com.davidbojkovski.recipes.cache_aware_image.CacheAwareImage
import com.davidbojkovski.recipes.common_components.IconWithDescription
import com.davidbojkovski.recipes.common_components.RecipeTagsComponent
import com.davidbojkovski.recipes.ui.theme.RecipesTheme
import com.davidbojkovski.recipes.R

@Composable
fun RecipeListItem(
    recipe: RecipeModel,
    onRecipeClicked: (Int) -> Unit,
    onTagClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Card(
            onClick = {
                onRecipeClicked(recipe.id)
            },
            modifier = Modifier
                .fillMaxWidth()
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
                    style = MaterialTheme.typography.headlineSmall
                )

                recipe.tags?.let { tags ->
                    RecipeTagsComponent(
                        tags = tags,
                        onTagClicked = { onTagClicked(it) }
                    )
                }

                Row(modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)) {
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
            }
        }
    }
}

@Preview
@Composable
private fun RecipeListItemPreview() {
    RecipesTheme {
        val item = RecipeModel(
            id = 1,
            name = "Classic Margherita Pizza",
            image = "https://picsum.photos/id/237/200/300",
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
        RecipeListItem(item, {}, {})
    }
}