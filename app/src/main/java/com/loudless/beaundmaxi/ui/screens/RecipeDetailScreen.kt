package com.loudless.beaundmaxi.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.loudless.beaundmaxi.logic.model.RecipeListItem
import com.loudless.beaundmaxi.logic.model.ShoppingListItem
import com.loudless.beaundmaxi.logic.viewModel.RecipeViewModel
import com.loudless.beaundmaxi.ui.elements.BottomBar
import com.loudless.beaundmaxi.ui.elements.CustomSwipeAction
import com.loudless.beaundmaxi.ui.elements.TopBarRecipeDetail
import me.saket.swipe.SwipeableActionsBox

@Composable
fun RecipeDetailScreen(index: Int = 0, viewModel: RecipeViewModel, navController: NavHostController){
    val recipeList by viewModel.recipeList.collectAsState()
    val focusManager = LocalFocusManager.current
    var lastTextInput by remember { mutableStateOf("") }
    var lastKeyInput by remember { mutableStateOf("") }
    Scaffold(modifier = Modifier.pointerInput(Unit){detectTapGestures {
        focusManager.clearFocus()
        if(recipeList[index].items.any{it.key == lastKeyInput}){
            viewModel.editItemInRecipe(recipeList[index],lastKeyInput,lastTextInput)
        }}},
        topBar = {
            TopBarRecipeDetail(
                title = recipeList[index].name, navController = navController
            )
        },
        bottomBar = { BottomBar(navController) }) { padding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(
                    top = 10.dp + padding.calculateTopPadding(),
                    bottom = padding.calculateBottomPadding()
                )
        ) {
            LazyColumn {
                itemsIndexed(recipeList[index].items, key = {index,item -> item.key }) {indexInternal,item ->
                    RecipeListItemDetail(
                        item = item,
                        viewModel = viewModel,
                        recipeListItem = recipeList[index],
                        updateCurrentText = { name, key ->
                                lastTextInput = name
                                lastKeyInput = key
                        }
                    )
                }
                item {
                    RecipeListItemDetailAdd(viewModel = viewModel,recipeList[index])
                }
            }

        }
    }
}

@Composable
fun RecipeListItemDetail(
    item: ShoppingListItem,
    viewModel: RecipeViewModel,
    recipeListItem: RecipeListItem,
    updateCurrentText: (String, String) -> Unit
) {
    var textFieldValue by remember { mutableStateOf(item.name) }
    val width = LocalConfiguration.current.screenWidthDp
    val focusManager = LocalFocusManager.current
    textFieldValue = item.name
    val delete = CustomSwipeAction(
        icon = rememberVectorPainter(Icons.Filled.Delete),
        background = Color.Red,
        onSwipe = { viewModel.removeItemFromRecipe(recipeListItem,item) }
    )
    SwipeableActionsBox(
        endActions = listOf(delete),
        swipeThreshold = (width / 3).dp,
        backgroundUntilSwipeThreshold = Color.LightGray,
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .focusable(false)
            .padding(bottom = 3.dp, start = 3.dp, end = 3.dp)
    ) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.tertiary)
        )
        {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Icon(
                    Icons.Filled.MoreVert,
                    contentDescription = null,
                    modifier = Modifier.padding(start = 10.dp)
                )
                BasicTextField(
                    maxLines = 1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = {
                        focusManager.clearFocus()
                        viewModel.editItemInRecipe(recipeListItem,item.key,textFieldValue)
                    }),
                    value = textFieldValue,
                    onValueChange = {
                        textFieldValue = it
                        updateCurrentText(it, item.key)
                    },
                    textStyle = TextStyle(
                        fontSize = 26.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                )
            }
        }
    }
}

@Composable
fun RecipeListItemDetailAdd(viewModel: RecipeViewModel, item: RecipeListItem) {
    Box(
        modifier = Modifier
            .background(Color.Transparent)
    )
    {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { viewModel.addItemToRecipe(item) }
                .padding(top = 10.dp, bottom = 10.dp),
            horizontalArrangement = Arrangement.Center

        ) {
            Icon(Icons.Filled.Add, contentDescription = null)
            Text(text = "Neues Item hinzufügen", fontSize = 20.sp)
        }
    }
}
