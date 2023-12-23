package com.loudless.beaundmaxi.ui.screens

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.kizitonwose.calendar.compose.WeekCalendar
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import com.kizitonwose.calendar.core.WeekDay
import com.kizitonwose.calendar.core.daysOfWeek
import com.loudless.beaundmaxi.logic.viewModel.HomeViewModel
import com.loudless.beaundmaxi.ui.elements.BottomBar
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.Locale

@Composable
fun HomeScreen(navController: NavHostController, viewModel: HomeViewModel) {
    Scaffold(
        bottomBar = { BottomBar(navController) }) { padding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(
                    top = 10.dp + padding.calculateTopPadding(),
                    bottom = padding.calculateBottomPadding()
                )
        ) {
            QuickAddFavourites(viewModel = viewModel)
            Calendar()
        }
    }
}

@Composable
fun ColumnScope.QuickAddFavourites(viewModel: HomeViewModel) {
    val recipeList = viewModel.recipeList.collectAsState()
    val context = LocalContext.current as Activity
    Text(text = "Lieblingsrezepte", fontSize = 24.sp, modifier = Modifier
        .padding(20.dp)
        .weight(0.1f))
    LazyColumn(modifier = Modifier
        .padding(20.dp)
        .weight(0.6f)) {
        items(recipeList.value.filter { it.favourite == true }, key = { it.key }) {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.tertiary)
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Icon(
                        imageVector = if (it.favourite) {
                            Icons.Filled.Favorite
                        } else {
                            Icons.Filled.FavoriteBorder
                        },
                        contentDescription = null,
                        modifier = Modifier
                            .weight(0.1f)
                            .padding(start = 10.dp)
                    )
                    Text(
                        text = it.name,
                        maxLines = 1,
                        modifier = Modifier
                            .weight(0.7f)
                            .fillMaxWidth()
                            .padding(start = 10.dp),
                        fontSize = 26.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = null,
                        modifier = Modifier
                            .weight(0.2f)
                            .padding(start = 10.dp)
                            .clickable {
                                viewModel.addShoppingListItems(it)
                                val toast = Toast.makeText(
                                    context,
                                    it.items.size.toString() + " Items wurden zur Einkaufsliste hinzugefügt",
                                    Toast.LENGTH_LONG
                                )
                                toast.show()
                            }
                    )

                }
            }
        }
    }
}

@Composable
fun ColumnScope.Calendar(){
    val calendarState = rememberWeekCalendarState(startDate = LocalDate.now())
    Text(text = "Terminübersicht", fontSize = 24.sp, modifier = Modifier
        .padding(20.dp)
        .weight(0.1f))
    Column (modifier = Modifier.weight(0.2f)) {
        DaysOfWeekTitle(daysOfWeek = daysOfWeek())
        WeekCalendar(state = calendarState, dayContent = { Day(it) })
    }
}

@Composable
fun Day(day: WeekDay){
    Box(
        modifier = Modifier
            .aspectRatio(1f),
        contentAlignment = Alignment.Center
    ) {
        Text(text = day.date.dayOfMonth.toString())
    }
}

@Composable
fun DaysOfWeekTitle(daysOfWeek: List<DayOfWeek>) {
    Row(modifier = Modifier.fillMaxWidth()) {
        for (dayOfWeek in daysOfWeek) {
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                text = dayOfWeek.getDisplayName(java.time.format.TextStyle.SHORT, Locale.GERMAN),
            )
        }
    }
}