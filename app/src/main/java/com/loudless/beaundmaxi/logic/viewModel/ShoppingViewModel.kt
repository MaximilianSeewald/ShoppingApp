package com.loudless.beaundmaxi.logic.viewModel

import androidx.lifecycle.ViewModel
import com.loudless.beaundmaxi.logic.DataRepository
import com.loudless.beaundmaxi.logic.model.ShoppingListItem

class ShoppingViewModel(private val dataRepository: DataRepository): ViewModel() {

    val shoppingList by lazy { dataRepository.shoppingList }

    fun addItem(){
        dataRepository.addItemToDB()
    }

    fun reorder(from: Int, to: Int){
        dataRepository.reorderList(from,to)
    }

    fun editItem(key: String ,name: String, marked: Boolean){
        dataRepository.editItemToDB(key,name,marked)
    }

    fun removeItem(item: ShoppingListItem){
        dataRepository.removeItemFromDB(item)
    }

    fun removeAllMarked(){
        dataRepository.removeAllMarked()
    }

    fun sortByMarked() {
        dataRepository.sortByMarked()
    }
}