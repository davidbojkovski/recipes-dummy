package com.davidbojkovski.recipes.recipe_details

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.davidbojkovski.recipes.R

@Composable
fun ExpandableComponentWithNumberedList(
    sectionTitle: String,
    sectionItems: List<String?>,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        onClick = { isExpanded = !isExpanded },
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(sectionTitle, style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.weight(1f))
                Text("${sectionItems.size}")
                IconButton(onClick = {
                    isExpanded = !isExpanded
                }) {
                    val icon =
                        if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore
                    val contentDescription =
                        if (isExpanded) stringResource(R.string.expandable_section_collapse_icon_content_description) else stringResource(
                            R.string.expandable_section_expand_icon_content_description
                        )
                    Icon(icon, contentDescription = contentDescription)
                }
            }

            AnimatedVisibility(isExpanded) {
                Column {
                    sectionItems.forEachIndexed { index, item ->
                        item?.let {
                            Text("${index + 1}. ${item}")
                        }
                    }
                }
            }
        }
    }
}