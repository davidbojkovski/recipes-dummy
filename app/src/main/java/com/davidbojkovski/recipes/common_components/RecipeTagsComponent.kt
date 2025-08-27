package com.davidbojkovski.recipes.common_components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RecipeTagsComponent(
    tags: List<String?>,
    modifier: Modifier = Modifier,
    onTagClicked: (String) -> Unit = {},
) {
    LazyRow(modifier = modifier) {
        items(tags) { tag ->
            tag?.let {
                SuggestionChip(
                    onClick = { onTagClicked(tag) },
                    label = { Text(tag) },
                    modifier = Modifier.padding(end = 8.dp),
                )
            }
        }
    }
}