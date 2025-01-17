package com.example.bookreadingapp.ui.components

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import com.example.bookreadingapp.R


@Composable
fun ImageFromDatabase(
    path: String?,
    modifier : Modifier = Modifier
){
    if (path != null){
        val bitmap : Bitmap = BitmapFactory.decodeFile(path)
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = "image",
            modifier = modifier)
    }
    else{
        Text(stringResource(R.string.no_image_found))
    }
}