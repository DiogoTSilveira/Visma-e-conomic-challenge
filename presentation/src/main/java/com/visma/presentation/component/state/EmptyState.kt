package com.visma.presentation.component.state

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.visma.presentation.R
import com.visma.presentation.theme.VismaEConomicTheme

@Composable
fun EmptyState(
    image: Painter,
    title: String,
    message: String? = null,
    buttonText: String? = null,
    onButtonClick: () -> Unit = {},
) {
    Surface {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Image(
                    modifier = Modifier.size(100.dp),
                    contentScale = ContentScale.FillHeight,
                    painter = image,
                    colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0f) }),
                    contentDescription = null
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = title,
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    message?.let {
                        Text(
                            text = it,
                            textAlign = TextAlign.Center,
                            style = TextStyle(fontSize = 12.sp)
                        )
                    }
                }

                buttonText?.let {
                    OutlinedButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        onClick = onButtonClick,
                        content = {
                            Text(
                                modifier = Modifier.padding(8.dp),
                                text = it
                            )
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EmptyStatePreview() {
    VismaEConomicTheme {
        EmptyState(
            image = painterResource(id = R.drawable.illustration_receipt),
            title = "No receipts registered",
            message = "You will see your receipts here once you start register them.",
            buttonText = "Register receipt",
            onButtonClick = {}
        )
    }
}