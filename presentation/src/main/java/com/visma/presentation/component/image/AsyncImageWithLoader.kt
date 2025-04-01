package com.visma.presentation.component.image

import android.graphics.Bitmap
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy.DISABLED
import coil.request.ImageRequest
import coil.size.Size
import com.visma.presentation.theme.ErrorColor

data class IconConfiguration(
    val icon: ImageVector,
    val onIconClick: (() -> Unit)? = null
)

@Composable
fun AsyncImageWithLoader(
    modifier: Modifier = Modifier,
    iconConfiguration: IconConfiguration? = null,
    image: Bitmap?,
    height: Dp = 150.dp,
    width: Dp = 100.dp
) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(image)
            .crossfade(true)
            .size(Size.ORIGINAL)
            .diskCachePolicy(DISABLED)
            .memoryCachePolicy(DISABLED)
            .build()
    )

    ConstraintLayout(modifier = modifier) {
        val (coverRef, iconRef) = createRefs()

        when (val result = painter.state) {
            is AsyncImagePainter.State.Loading -> Box(
                modifier = Modifier
                    .height(height)
                    .width(width)
                    .constrainAs(coverRef) { centerTo(parent) }
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

            is AsyncImagePainter.State.Success -> Box(
                modifier = modifier
                    .constrainAs(coverRef) { centerTo(parent) }
                    .clip(CardDefaults.shape)
                    .height(height)
                    .width(width)
                    .width(width),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    modifier = modifier.fillMaxSize(),
                    contentScale = ContentScale.FillHeight,
                    painter = result.painter,
                    contentDescription = null,
                )
            }

            else -> Card(
                modifier = modifier
                    .height(height)
                    .width(width),
                border = BorderStroke(1.dp, Color.White),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                Box(modifier = Modifier.fillMaxSize())
            }
        }

        iconConfiguration?.let {
            val iconSize = it.icon.defaultWidth

            IconButton(
                modifier = Modifier
                    .constrainAs(iconRef) {
                        start.linkTo(coverRef.end, -iconSize)
                        bottom.linkTo(coverRef.top, -iconSize)
                    },
                onClick = it.onIconClick ?: {},
                enabled = it.onIconClick != null
            ) {
                Icon(
                    imageVector = it.icon,
                    tint = ErrorColor,
                    contentDescription = null
                )
            }
        }
    }
}