package com.gorbunov.maskhack

data class ImageModel(
    val path: Int,
    val hasNose: Boolean,
    val hasEye: HasEye,
    val hasEar: HasEar,
    val hasMouth: Boolean,
    val hasTop: Boolean,
    val hasBottom: Boolean,
)



data class HasEye(
    val has: Boolean,
    val isPair: Boolean
)

data class HasEar(
    val has: Boolean,
    val isPair: Boolean
)




