package com.loudless.beaundmaxi.ui.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import me.saket.swipe.SwipeAction

fun CustomSwipeAction(
    onSwipe: () -> Unit,
    icon: Painter,
    background: Color,
    weight: Double = 1.0,
    isUndo: Boolean = false
): SwipeAction {
    return SwipeAction(
        icon = {
            Image(
                modifier = Modifier.padding(2.dp),
                painter = icon,
                contentDescription = null
            )
        },
        background = background,
        weight = weight,
        onSwipe = onSwipe,
        isUndo = isUndo
    )
}
