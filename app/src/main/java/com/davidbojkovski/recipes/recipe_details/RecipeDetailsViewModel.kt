package com.davidbojkovski.recipes.recipe_details

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
class RecipeDetailsViewModel @Inject constructor(private val repository: RecipeRepository) :
    ViewModel() {

    private var _recipe = MutableStateFlow<RecipeModel?>(null)
    val recipe: StateFlow<RecipeModel?>
        get() = _recipe

    fun getRecipe(id: Int) {
        viewModelScope.launch {
            _recipe.value = repository.getRecipe(id)
        }
    }
}