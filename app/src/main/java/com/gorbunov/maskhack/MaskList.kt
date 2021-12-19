package com.gorbunov.maskhack

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SnapshotMutationPolicy
import androidx.compose.runtime.snapshots.SnapshotMutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp


@Composable
fun MaskList(list: List<ImageModel>, modifier: Modifier, onClick: (Int) -> Unit){
    LazyRow(horizontalArrangement = Arrangement.SpaceBetween ,  contentPadding = PaddingValues(8.dp)){

        list.forEachIndexed {i, it ->
            item {
                Image(painter = painterResource(id = it.path), contentDescription = "", modifier = modifier.clickable {
                    onClick(i)
                })
                
                if(i != list.size - 1){
                    Spacer(modifier = Modifier.padding(8.dp))
                }
            }
            
        }
    }
}