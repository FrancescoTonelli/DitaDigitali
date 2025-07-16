package com.ditadigitali.ui.components

import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.ditadigitali.ui.theme.Primary


@Composable
fun FloatingButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector,
    contentDescription: String
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier,
        containerColor = Primary
    ) { Icon(icon, contentDescription = contentDescription, tint = Color.White) }
}