package com.loudless.beaundmaxi.logic.viewModel

import androidx.lifecycle.ViewModel
import com.loudless.beaundmaxi.logic.DataRepository
import com.loudless.beaundmaxi.logic.model.RecipeListItem
import com.loudless.beaundmaxi.logic.model.ShoppingListItem

class RecipeViewModel(private val dataRepository: DataRepository): ViewModel() {
    val recipeList by lazy { dataRepository.recipeList }

    fun addItem() {
        dataRepository.addItemToDBRecipe()
    }

    fun editItem(key: String ,name: String){
        dataRepository.editItemToDBRecipe(key,name)
    }

    fun removeItem(item: RecipeListItem){
        dataRepository.removeItemFromDBRecipe(item)
    }

    fun addItemToRecipe(item: RecipeListItem){
        dataRepository.addItemToDBToRecipe(item)
    }

    fun editItemInRecipe(item: RecipeListItem,key: String ,name: String){
        dataRepository.editItemToDBToRecipe(item,key,name)
    }

    fun removeItemFromRecipe(itemRecipe: RecipeListItem,itemShopping: ShoppingListItem){
        dataRepository.removeItemFromDBFromRecipe(itemRecipe,itemShopping)
    }

}