package com.martinz.myreminder.core

import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.martinz.myreminder.R

@Composable
fun hidePasswordIcon() = painterResource(id = R.drawable.lock)


@Composable
fun showPasswordIcon() = painterResource(id = R.drawable.unlock)


