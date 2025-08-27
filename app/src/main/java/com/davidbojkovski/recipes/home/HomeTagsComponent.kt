package com.davidbojkovski.recipes.home

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.davidbojkovski.recipes.R

@Composable
fun TagsHomeComponent(
    tags: List<String?>,
    selectedTag: String,
    onTagClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(modifier = modifier) {
        items(tags) { tag ->
            tag?.let {
                FilterChip(
                    label = { Text(tag) },
                    onClick = { onTagClicked(tag) },
                    modifier = Modifier.padding(end = 8.dp),
                    selected = tag == selectedTag,
                    leadingIcon = {
                        if (tag == selectedTag) {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = stringResource(R.string.tags_component_selected_tag_content_description),
                            )
                        }
                    },
                )
            }
        }
    }
}