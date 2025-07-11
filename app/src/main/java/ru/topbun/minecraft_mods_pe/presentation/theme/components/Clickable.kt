package ru.topbun.minecraft_mods_pe.presentation.theme.components

import android.annotation.SuppressLint
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@SuppressLint("ModifierFactoryUnreferencedReceiver")
@Composable
fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier = clickable(
    interactionSource = remember { MutableInteractionSource() },
    indication = null,
    onClick = onClick
)

@SuppressLint("ModifierFactoryUnreferencedReceiver")
@Composable
fun Modifier.rippleClickable(onClick: () -> Unit): Modifier = clickable(
    interactionSource = remember { MutableInteractionSource() },
    indication = null,
    onClick = onClick
)
