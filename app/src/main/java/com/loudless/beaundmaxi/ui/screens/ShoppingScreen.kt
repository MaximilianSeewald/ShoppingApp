package com.loudless.beaundmaxi.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.loudless.beaundmaxi.logic.model.ShoppingListItem
import com.loudless.beaundmaxi.ui.elements.CustomSwipeAction
import com.loudless.beaundmaxi.logic.viewModel.ShoppingViewModel
import com.loudless.beaundmaxi.ui.elements.BottomBar
import com.loudless.beaundmaxi.ui.elements.TopBarShopping
import me.saket.swipe.SwipeableActionsBox
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyColumnState


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ShoppingScreen(viewModel: ShoppingViewModel, navController: NavHostController) {
    val shoppingList by viewModel.shoppingList.collectAsState()
    val focusManager = LocalFocusManager.current
    var lastTextInput by remember { mutableStateOf("") }
    var lastKeyInput by remember { mutableStateOf("") }
    var lastBooleanInput by remember { mutableStateOf(false) }
    var focusDirection by remember { mutableStateOf<FocusDirection?>(null) }
    val lazyListState = rememberLazyListState()
    val state = rememberReorderableLazyColumnState(lazyListState) { from, to ->
        viewModel.reorder(from.index, to.index)
    }
    Scaffold(modifier = Modifier.pointerInput(Unit){detectTapGestures {
        focusManager.clearFocus()
        if(shoppingList.any{it.key == lastKeyInput}){
            viewModel.editItem(lastKeyInput,lastTextInput,lastBooleanInput)
        }
    } },floatingActionButton = {
    }, floatingActionButtonPosition = FabPosition.End, topBar = {
        TopBarShopping(
            title = "Einkaufsliste",
            onClickRemove = { viewModel.removeAllMarked() },
            onClickSort = { viewModel.sortByMarked() },
            navController = navController)
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
            LazyColumn(
                userScrollEnabled = true, state = lazyListState, modifier = Modifier
                    .weight(0.75f)
            ) {
                itemsIndexed(items = shoppingList, key = {index,item -> item.key } ) { index,item ->
                    ReorderableItem(state, key = item.key) {
                        ShoppingListItem(
                            item = item,
                            index = index,
                            viewModel =  viewModel,
                            modifier = Modifier.draggableHandle(),
                            isLast = index == shoppingList.size-1,
                            updateCurrentText = { name, key, marked ->
                                lastTextInput = name
                                lastKeyInput = key
                                lastBooleanInput = marked
                            },
                            onDone = {
                                focusDirection = FocusDirection.Down
                            }
                        )
                    }
                }
                item {
                    ShoppingListItemAdd(viewModel)
                }
            }
            LaunchedEffect(shoppingList.size){
                focusDirection?.let {
                    if(focusManager.moveFocus(it)){
                        focusDirection = null
                    }
                }
            }

        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ShoppingListItem(
    item: ShoppingListItem,
    viewModel: ShoppingViewModel,
    modifier: Modifier = Modifier,
    updateCurrentText: (String,String,Boolean) -> Unit,
    isLast: Boolean,
    index: Int,
    onDone: (Int) -> Unit
) {
    var textFieldValue by remember { mutableStateOf(item.name) }
    val focusManager = LocalFocusManager.current
    val width = LocalConfiguration.current.screenWidthDp
    val delete = CustomSwipeAction(
        icon = rememberVectorPainter(Icons.Filled.Delete),
        background = Color.Red,
        onSwipe = { viewModel.removeItem(item) }
    )
    textFieldValue = item.name
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
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    Icons.Filled.MoreVert,
                    contentDescription = null,
                    modifier = modifier.padding(start = 10.dp)
                )
                Checkbox(
                    checked = item.marked,
                    modifier = Modifier,
                    onCheckedChange = {
                        viewModel.editItem(
                            item.key,
                            item.name,
                            !item.marked
                        )
                    })
                BasicTextField(
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = {
                        viewModel.editItem(item.key,textFieldValue,item.marked)
                        if(isLast){
                            viewModel.addItem()
                            onDone(index)
                        }else{
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    }),
                    value = textFieldValue,
                    onValueChange = {
                        textFieldValue = it
                        updateCurrentText(it,item.key,item.marked)
                                    },
                    textStyle = if (item.marked) {
                        TextStyle(textDecoration = TextDecoration.LineThrough, fontSize = 26.sp, color = MaterialTheme.colorScheme.primary)
                    } else {
                        TextStyle(textDecoration = TextDecoration.None, fontSize = 26.sp, color = MaterialTheme.colorScheme.primary)
                    }
                )
            }
        }
    }
}

@Composable
fun ShoppingListItemAdd(viewModel: ShoppingViewModel) {
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
