package com.loudless.beaundmaxi.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.loudless.beaundmaxi.logic.DataRepository
import com.loudless.beaundmaxi.logic.viewModel.RecipeViewModel
import com.loudless.beaundmaxi.logic.viewModel.ShoppingViewModel
import com.loudless.beaundmaxi.ui.screens.HomeScreen
import com.loudless.beaundmaxi.ui.screens.RecipeDetailScreen
import com.loudless.beaundmaxi.ui.screens.RecipeScreen
import com.loudless.beaundmaxi.ui.screens.ShoppingScreen

@Composable
fun NavGraphCustom(navController: NavHostController, dataRepository: DataRepository){
    val shoppingViewModel = ShoppingViewModel(dataRepository)
    val recipeViewModel = RecipeViewModel(dataRepository)
    NavHost(navController = navController, startDestination = "shopping") {
        composable("shopping") { ShoppingScreen(viewModel = shoppingViewModel, navController = navController) }
        composable("recipe") { RecipeScreen(viewModel = recipeViewModel, navController = navController) }
        composable("home") { HomeScreen(navController = navController) }
        composable("recipeDetail/{index}") {backStackEntry ->
            backStackEntry.arguments?.getString("index")?.toInt()
                ?.let {
                    RecipeDetailScreen(viewModel = recipeViewModel,index = it, navController = navController)
                }
        }
    }
}