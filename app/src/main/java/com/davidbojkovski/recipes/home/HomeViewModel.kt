package com.davidbojkovski.recipes.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davidbojkovski.recipes.models.RecipeModel
import com.davidbojkovski.recipes.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val recipeRepository: RecipeRepository) :
    ViewModel() {

    companion object {
        val TAG = HomeViewModel::class.java.simpleName
        val ALL_TAG = "All"
    }

    private var _recipes = MutableStateFlow<List<RecipeModel?>?>(null)
    val recipes: StateFlow<List<RecipeModel?>?>
        get() = _recipes

    private var _tags = MutableStateFlow<List<String?>?>(null)
    val tags: StateFlow<List<String?>?>
        get() = _tags

    private var _selectedTag = MutableStateFlow(ALL_TAG)
    val selectedTag: StateFlow<String>
        get() = _selectedTag

    fun getRecipes() {
        viewModelScope.launch {
            val response = recipeRepository.getRecipes()
            Log.d(TAG, "$response")
            _recipes.value = response
        }
    }

    fun searchRecipe(query: String) {
        viewModelScope.launch {
            _selectedTag.value = ALL_TAG
            _recipes.value = recipeRepository.searchRecipes(query)
        }
    }

    fun getTags() {
        viewModelScope.launch {
            val tags = recipeRepository.getTags()
            val allTags = tags?.toMutableList()
            allTags?.add(0, ALL_TAG)
            _tags.value = allTags
        }
    }

    fun onTagClicked(tag: String) {
        if (tag != _selectedTag.value) {
            _selectedTag.value = tag

            viewModelScope.launch {
                if (tag != ALL_TAG) {
                    val response = recipeRepository.searchRecipesByTag(tag)
                    Log.d(TAG, "$response")
                    _recipes.value = response
                } else {
                    val response = recipeRepository.getRecipes()
                    Log.d(TAG, "$response")
                    _recipes.value = response
                }
            }
        }
    }

    fun onRefreshContent() {
        viewModelScope.launch {
            _recipes.value = null
            _tags.value = null
            _selectedTag.value = ALL_TAG

            recipeRepository.clearCaches()

            getRecipes()
            getTags()
        }
    }
}