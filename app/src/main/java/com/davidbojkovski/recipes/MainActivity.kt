package com.davidbojkovski.recipes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.davidbojkovski.recipes.home.HomeScreen
import com.davidbojkovski.recipes.recipe_details.RecipeDetailsScreen
import com.davidbojkovski.recipes.ui.theme.RecipesTheme
import dagger.hilt.android.AndroidEntryPoint

@OptIn(ExperimentalMaterial3Api::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            RecipesTheme {
                val navController = rememberNavController()

                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    NavHost(
                        navController,
                        startDestination = RecipeNavigation.RECIPES.route,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        composable(RecipeNavigation.RECIPES.route) {
                            HomeScreen(
                                onRecipeClicked = { id ->
                                    navController.navigate("${RecipeNavigation.RECIPE_DETAILS.route}/${id}")
                                },
                                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)
                            )
                        }
                        composable(
                            route = "${RecipeNavigation.RECIPE_DETAILS.route}/{${DETAILS_ARG}}",
                            arguments = listOf(
                                navArgument(DETAILS_ARG) {
                                    type = NavType.IntType; defaultValue = 0
                                }
                            )
                        ) { backStackEntry ->
                            val id = backStackEntry.arguments?.getInt(DETAILS_ARG) ?: 0
                            RecipeDetailsScreen(
                                id,
                                Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }
        }
    }
}
