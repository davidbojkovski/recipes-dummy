package com.davidbojkovski.recipes.recipe_details

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.davidbojkovski.recipes.ui.theme.RecipesTheme
import org.junit.Rule
import org.junit.Test


class ExpandableComponentWithNumberedListKtTest {

    @get:Rule val composeTestRule = createComposeRule()

    @Test
    fun ExpandableComponentWithNumberedListKtTest_onExpandClicked() {
        //GIVEN
        composeTestRule.setContent {
            RecipesTheme {
                ExpandableComponentWithNumberedList(
                    sectionTitle = "Heading",
                    sectionItems = listOf("Test 1", "Test 2", "Test 3"),
                )
            }
        }

        //WHEN
        //expand the component
        composeTestRule.onNodeWithText("Heading").performClick()

        //THEN
        composeTestRule.onNodeWithContentDescription("collapse section").assertExists()
        composeTestRule.onNodeWithText("1. Test 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("2. Test 2").assertIsDisplayed()
        composeTestRule.onNodeWithText("3. Test 3").assertIsDisplayed()
    }
}