package com.loudless.beaundmaxi.logic.viewModel

import androidx.lifecycle.ViewModel
import com.loudless.beaundmaxi.logic.DataRepository
import com.loudless.beaundmaxi.logic.model.RecipeListItem

class HomeViewModel(private val dataRepository: DataRepository): ViewModel() {
    val recipeList by lazy { dataRepository.recipeList }

    fun addShoppingListItems(item: RecipeListItem) {
        item.items.forEach {
            val key = java.util.UUID.randomUUID().toString()
            dataRepository.editItemToDB(key,it.name,it.marked)
        }
    }
}