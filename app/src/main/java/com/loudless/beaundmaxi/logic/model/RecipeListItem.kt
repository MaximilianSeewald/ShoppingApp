package com.loudless.beaundmaxi.logic.model

data class RecipeListItem(val name: String,val key: String ,var items: List<ShoppingListItem>, var favourite: Boolean)