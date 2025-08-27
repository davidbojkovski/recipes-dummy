package com.davidbojkovski.recipes.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.davidbojkovski.recipes.models.RecipeModel

@Composable
fun HomeScreen(
    onRecipeClicked: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel<HomeViewModel>()
) {
    var searchQuery by remember { mutableStateOf("") }
    val recipes by viewModel.recipes.collectAsState()
    val tags by viewModel.tags.collectAsState()
    val selectedTag by viewModel.selectedTag.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getRecipes()
        viewModel.getTags()
    }

    HomeScreenContent(
        searchQuery = searchQuery,
        onSearchChange = { searchQuery = it },
        onSearchClicked = { viewModel.searchRecipe(it) },
        onSearchClear = {
            searchQuery = ""
            viewModel.getRecipes()
        },
        recipes = recipes,
        tags = tags,
        onTagClicked = { viewModel.onTagClicked(it) },
        selectedTag = selectedTag,
        onRecipeClicked = { onRecipeClicked(it) },
        onRefresh = { viewModel.onRefreshContent() },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreenContent(
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    onSearchClicked: (String) -> Unit,
    onSearchClear: () -> Unit,
    recipes: List<RecipeModel?>?,
    tags: List<String?>?,
    onTagClicked: (String) -> Unit,
    selectedTag: String,
    onRecipeClicked: (Int) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        SearchRecipeComponent(
            searchQuery = searchQuery,
            onSearchChange = { onSearchChange(it) },
            onSearchClicked = { onSearchClicked(searchQuery) },
            onSearchClear = { onSearchClear() },
            modifier = Modifier.fillMaxWidth()
        )

        tags?.let {
            TagsHomeComponent(it, selectedTag, { onTagClicked(it) })
        }

        PullToRefreshBox(
            isRefreshing = recipes == null,
            onRefresh = { onRefresh() },
        ) {
            recipes?.let {
                LazyColumn {
                    items(it, key = { it?.id ?: -1 }) { recipe ->
                        recipe?.let {
                            RecipeListItem(
                                recipe,
                                onRecipeClicked = { onRecipeClicked(it) },
                                onTagClicked = { onTagClicked(it) },
                                Modifier.padding(top = 8.dp, bottom = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
