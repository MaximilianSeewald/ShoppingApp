package com.loudless.beaundmaxi.logic

import com.loudless.beaundmaxi.logic.model.RecipeListItem
import com.loudless.beaundmaxi.logic.model.ShoppingListItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class DataRepository(private val databaseManager: DatabaseManager) {

    val shoppingList = MutableStateFlow<MutableList<ShoppingListItem>>(mutableListOf())
    val recipeList = MutableStateFlow<MutableList<RecipeListItem>>(mutableListOf())

    fun init(){
        databaseManager.init(this)
    }

    fun addItemToDB(){
        val key = java.util.UUID.randomUUID().toString()
        val newItem = ShoppingListItem(name = "", key = key, marked = false)
        databaseManager.addItemToDatabase(newItem)
    }

    fun editItemToDB(key: String ,name: String, marked: Boolean){
        databaseManager.editItemToDatabase(key,name,marked)
    }

    fun removeItemFromDB(item: ShoppingListItem){
        databaseManager.removeItemFromDatabase(item)
    }

    fun removeAllMarked(){
        val markedList  = mutableListOf<String>()
        shoppingList.value.forEach{
            if(it.marked){
                markedList.add(it.key)
            }
        }
        databaseManager.removeAllMarkedFromDB(markedList)
    }

    fun reorderList(from: Int, to: Int){
        shoppingList.value = shoppingList.value.toMutableList().apply {
            add(to,removeAt(from))
        }
    }

    fun compareFromDBShopping(dbList: List<ShoppingListItem>){
        val duplicatedList = shoppingList.value.toMutableList()
        val keyListDb = mutableListOf<String>()
        val keyList = mutableListOf<String>()

        dbList.forEach{
            keyListDb.add(it.key)
        }
        duplicatedList.forEach{
            keyList.add(it.key)
        }
        duplicatedList.removeIf{
            !keyListDb.contains(it.key)
        }
        dbList.forEach {dbItem ->
            if(!keyList.contains(dbItem.key)){
                duplicatedList.add(dbItem)
            }else{
                val index = duplicatedList.indexOf(duplicatedList.find {
                    dbItem.key == it.key
                })
                duplicatedList.set(index,dbItem)
            }
        }

        shoppingList.update { duplicatedList }
    }

    fun compareFromDBRecipe(dbList: List<RecipeListItem>){
        val duplicatedList = recipeList.value.toMutableList()
        val keyListDb = mutableListOf<String>()
        val keyList = mutableListOf<String>()

        dbList.forEach{
            keyListDb.add(it.key)
        }
        duplicatedList.forEach{
            keyList.add(it.key)
        }
        duplicatedList.removeIf{
            !keyListDb.contains(it.key)
        }
        dbList.forEach {dbItem ->
            if(!keyList.contains(dbItem.key)){
                duplicatedList.add(dbItem)
            }else{
                val index = duplicatedList.indexOf(duplicatedList.find {
                    dbItem.key == it.key
                })
                duplicatedList.set(index,dbItem)
            }
        }
        recipeList.update { duplicatedList }
    }

    fun sortByMarked() {
        val duplicatedList = shoppingList.value.toMutableList()
        duplicatedList.sortBy { it.marked }
        shoppingList.update { duplicatedList }
    }

    fun addItemToDBRecipe() {
        val key = java.util.UUID.randomUUID().toString()
        val newItem = RecipeListItem(name = "", key = key, mutableListOf(), false)
        databaseManager.addItemToDatabaseRecipe(newItem)
    }

    fun editItemToDBRecipe(key: String, name: String, item: RecipeListItem, favourite: Boolean) {
        databaseManager.editItemToDatabaseRecipe(key,name, item, favourite)
    }

    fun removeItemFromDBRecipe(item: RecipeListItem) {
        databaseManager.removeItemFromDatabaseRecipe(item)
    }

    fun addItemToDBToRecipe(item: RecipeListItem) {
        val key = java.util.UUID.randomUUID().toString()
        val newItem = ShoppingListItem(name = "", key = key, false)
        val list = item.items.toMutableList()
        list.add(newItem)
        val newRecipeItem = item.copy(items = list)
        databaseManager.editItemToDatabaseToRecipe(newRecipeItem)
    }

    fun editItemToDBToRecipe(item: RecipeListItem,key: String ,name: String) {
        val list = item.items.toMutableList()
        val newItem = list.find { it.key == key }
        val index = list.indexOf(newItem)
        newItem?.name = name
        if (newItem != null) {
            list[index] = newItem
        }
        item.items = list
        databaseManager.editItemToDatabaseToRecipe(item)
    }

    fun removeItemFromDBFromRecipe(itemRecipe: RecipeListItem, itemShopping: ShoppingListItem) {
        val list = itemRecipe.items.toMutableList()
        val newItem = list.find { it.key == itemShopping.key }
        list.remove(newItem)
        val item = itemRecipe.copy(items = list)
        databaseManager.editItemToDatabaseToRecipe(item)
    }

}