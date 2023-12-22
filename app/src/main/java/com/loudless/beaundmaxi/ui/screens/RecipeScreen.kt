package com.loudless.beaundmaxi.ui.screens

import android.app.Activity
import android.widget.Toast
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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.loudless.beaundmaxi.logic.model.RecipeListItem
import com.loudless.beaundmaxi.logic.viewModel.RecipeViewModel
import com.loudless.beaundmaxi.ui.elements.BottomBar
import com.loudless.beaundmaxi.ui.elements.CustomSwipeAction
import com.loudless.beaundmaxi.ui.elements.TopBarRecipe
import me.saket.swipe.SwipeableActionsBox

@Composable
fun RecipeScreen(viewModel: RecipeViewModel, navController: NavHostController) {
    val recipeList by viewModel.recipeList.collectAsState()
    val focusManager = LocalFocusManager.current
    var lastTextInput by remember { mutableStateOf("") }
    var lastKeyInput by remember { mutableStateOf("") }
    var lastFavouriteInput by remember { mutableStateOf(false) }
    Scaffold(modifier = Modifier.pointerInput(Unit){detectTapGestures {
        focusManager.clearFocus()
        if(recipeList.any{it.key == lastKeyInput}){
            viewModel.editItem(lastKeyInput,lastTextInput,recipeList.find{it.key == lastKeyInput}!!,lastFavouriteInput)
        }}},
        topBar = {
            TopBarRecipe(
                title = "Rezepte", navController = navController
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
                itemsIndexed(recipeList,key = {index,item -> item.key }) {index,item ->
                    RecipeListItem(
                        item = item,
                        index = index,
                        viewModel = viewModel,
                        navController = navController,
                        updateCurrentText =  { name, key, favourite ->
                            lastTextInput = name
                            lastKeyInput = key
                            lastFavouriteInput = favourite
                        }
                    )
                }
                item {
                    RecipeListItemAdd(viewModel)
                }
            }

        }
    }
}

@Composable
fun RecipeListItem(
    item: RecipeListItem,
    viewModel: RecipeViewModel,
    updateCurrentText: (String, String, Boolean) -> Unit,
    index: Int,
    navController: NavHostController
) {
    var textFieldValue by remember { mutableStateOf(item.name) }
    val width = LocalConfiguration.current.screenWidthDp
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current as Activity
    textFieldValue = item.name
    val delete = CustomSwipeAction(
        icon = rememberVectorPainter(Icons.Filled.Delete),
        background = Color.Red,
        onSwipe = { viewModel.removeItem(item) }
    )
    val detail = CustomSwipeAction(
        icon = rememberVectorPainter(Icons.Filled.Edit),
        background = Color.Green,
        onSwipe = { navController.navigate("recipeDetail/"+ index) }
    )
    SwipeableActionsBox(
        endActions = listOf(delete),
        startActions = listOf(detail),
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
                    imageVector = if(item.favourite){Icons.Filled.Favorite}else{Icons.Filled.FavoriteBorder},
                    contentDescription = null,
                    modifier = Modifier
                        .weight(0.1f)
                        .padding(start = 10.dp)
                        .clickable { viewModel.editItem(item.key,item.name,item,!item.favourite) }
                )
                BasicTextField(
                    maxLines = 1,
                    modifier = Modifier
                        .weight(0.7f)
                        .fillMaxWidth()
                        .padding(start = 10.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = {
                        viewModel.editItem(item.key,textFieldValue,item, item.favourite)
                        focusManager.clearFocus()
                    }),
                    value = textFieldValue,
                    onValueChange = {
                        textFieldValue = it
                        updateCurrentText(it, item.key, item.favourite)
                    },
                    textStyle = TextStyle(
                        fontSize = 26.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                )
                Icon(
                    Icons.Filled.Add,
                    contentDescription = null,
                    modifier = Modifier
                        .weight(0.2f)
                        .padding(start = 10.dp)
                        .clickable {
                            viewModel.addShoppingListItems(item)
                            val toast = Toast.makeText(
                                context,
                                item.items.size.toString() + " Items wurden zur Einkaufsliste hinzugefügt",
                                Toast.LENGTH_LONG
                            )
                            toast.show()
                        }
                )
            }
        }
    }
}

@Composable
fun RecipeListItemAdd(viewModel: RecipeViewModel) {
    Box(
        modifier = Modifier
            .background(Color.Transparent)
    )
    {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { viewModel.addItem() }
                .padding(top = 10.dp, bottom = 10.dp),
            horizontalArrangement = Arrangement.Center

        ) {
            Icon(Icons.Filled.Add, contentDescription = null)
            Text(text = "Neues Item hinzufügen", fontSize = 20.sp)
        }
    }
}
