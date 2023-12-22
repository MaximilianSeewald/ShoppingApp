package com.loudless.beaundmaxi.logic

import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.firestore
import com.loudless.beaundmaxi.logic.model.RecipeListItem
import com.loudless.beaundmaxi.logic.model.ShoppingListItem

class DatabaseManager {
    val db = Firebase.firestore
    var currentDBListShopping: List<ShoppingListItem>  = mutableListOf()
    var currentDBListRecipe: List<RecipeListItem> = mutableListOf()
    fun init(dataRepository: DataRepository){
        db.collection("Einkaufsliste").addSnapshotListener {snapshots, e ->
            currentDBListShopping = snapshots.let { createShoppingList(it?.documents ?: mutableListOf()) }
            dataRepository.compareFromDBShopping(currentDBListShopping)
        }
        db.collection("Rezepte").addSnapshotListener {snapshots,e ->
            currentDBListRecipe = snapshots.let {createRecipeList(it?.documents ?: mutableListOf())}
            dataRepository.compareFromDBRecipe(currentDBListRecipe)
        }
    }

    fun addItemToDatabase(shoppingItem: ShoppingListItem){
        val item = hashMapOf(
            "Name" to shoppingItem.name,
            "Marked" to shoppingItem.marked,
        )
        db.collection("Einkaufsliste").document(shoppingItem.key).set(item)
    }

    fun editItemToDatabase(key: String, name: String, marked: Boolean){
        val item = hashMapOf(
            "Name" to name,
            "Marked" to marked,
        )
        db.collection("Einkaufsliste").document(key).set(item)
    }

    fun removeItemFromDatabase(item: ShoppingListItem){
        db.collection("Einkaufsliste").document(item.key).delete()
    }

    fun removeAllMarkedFromDB(keyListMarked: MutableList<String>){
        currentDBListShopping.forEach{
            if(keyListMarked.contains(it.key)){
                db.collection("Einkaufsliste").document(it.key).delete()
            }
        }
    }

    fun createShoppingList(dbList: List<DocumentSnapshot>): MutableList<ShoppingListItem>{
        val shoppingList: MutableList<ShoppingListItem> = mutableListOf()
        if(dbList != null){
            dbList.forEach{
                if(it.contains("Name") && it.contains("Marked")){
                    val item = ShoppingListItem(it.get("Name").toString(), it.id,
                        it.get("Marked") as Boolean
                    )
                    shoppingList.add(item)
                }
            }
        }
        return shoppingList
    }

    fun createRecipeList(dbList: List<DocumentSnapshot>): MutableList<RecipeListItem>{
        val recipeList: MutableList<RecipeListItem> = mutableListOf()
        if(dbList != null){
            dbList.forEach{
                if(it.contains("Name") && it.contains("List")) {
                    val list = it.get("List") as MutableList<HashMap<String, String>>
                    val shoppingItemList = mutableListOf<ShoppingListItem>()
                    list.forEach {
                        val item = ShoppingListItem(
                            marked = false,
                            name = it.get("name").toString(),
                            key = it.get("key").toString()
                        )
                        shoppingItemList.add(item)
                    }
                    val item = RecipeListItem(it.get("Name").toString(), it.id, shoppingItemList, it.get("Favourite").toString().toBoolean())
                    recipeList.add(item)
                }
            }
        }
        return recipeList
    }

    fun addItemToDatabaseRecipe(newItem: RecipeListItem) {
        val item = hashMapOf(
            "Name" to newItem.name,
            "List" to mutableListOf<ShoppingListItem>(),
            "Favourite" to false
        )
        db.collection("Rezepte").document(newItem.key).set(item)
    }

    fun editItemToDatabaseRecipe(key: String, name: String, itemRecipe: RecipeListItem, favourite: Boolean) {
        val item = hashMapOf(
            "Name" to name,
            "List" to itemRecipe.items,
            "Favourite" to favourite
        )
        db.collection("Rezepte").document(key).set(item)
    }

    fun removeItemFromDatabaseRecipe(item: RecipeListItem) {
        db.collection("Rezepte").document(item.key).delete()
    }

    fun editItemToDatabaseToRecipe(itemRecipe: RecipeListItem) {
        val item = hashMapOf(
            "Name" to itemRecipe.name,
            "List" to itemRecipe.items,
            "Favourite" to itemRecipe.favourite
        )
        db.collection("Rezepte").document(itemRecipe.key).set(item)
    }
}