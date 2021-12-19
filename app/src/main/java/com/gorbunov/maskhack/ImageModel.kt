package com.gorbunov.maskhack

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class ImageModel(
    val path: Int,
    val hasNose: Boolean,
    val hasEye: HasEye,
    val hasEar: HasEar,
    val hasMouth: Boolean,
    val hasTop: Boolean,
    val hasBottom: Boolean,
    val requireSizePlus: Dp = 0.dp
)



data class HasEye(
    val has: Boolean,
    val left: Boolean,
    val right: Boolean
)

data class HasEar(
    val has: Boolean,
    val left: Boolean,
    val right: Boolean
)




