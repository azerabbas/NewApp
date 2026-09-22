package com.gunlukmarsrut.courier.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val CardShape = RoundedCornerShape(Radius.card)
val ButtonShape = RoundedCornerShape(Radius.button)
val InputShape = RoundedCornerShape(Radius.input)
val BottomSheetShape = RoundedCornerShape(topStart = Radius.bottomSheet, topEnd = Radius.bottomSheet)
val PillShape = RoundedCornerShape(Radius.pill)

val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = InputShape,
    medium = ButtonShape,
    large = CardShape,
    extraLarge = RoundedCornerShape(28.dp),
)
