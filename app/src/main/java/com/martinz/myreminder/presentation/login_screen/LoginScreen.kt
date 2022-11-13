package com.martinz.myreminder.presentation.login_screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.martinz.myreminder.R
import com.martinz.myreminder.presentation.login_screen.components.ReminderTextField
import com.martinz.myreminder.presentation.login_screen.ui.*


@Composable
fun LoginScreen(
    emailState : MutableState<String>,
    passwordState : MutableState<String>,
    isLoading : MutableState<Boolean>,
    onLogin : () -> Unit ,
    onSignInWithGoogle: () -> Unit,
    onRegister : () -> Unit

) {

    val value = remember { mutableStateOf(false) }


    LaunchedEffect(key1 = true) {
        value.value = true
    }


    BoxWithConstraints(modifier = Modifier
        .fillMaxSize()
        .background(VistaBlue)) {
        val width = constraints.maxWidth
        val height = constraints.maxHeight


        // Medium colored path

        val mediumColoredPoint2 = Offset(0f, -height.toFloat() )
        val mediumColoredPoint5 = Offset( width.toFloat(), height.toFloat())

        val mediumColoredPath = Path().apply {
            moveTo(mediumColoredPoint2.x, mediumColoredPoint2.y)
            relativeMoveTo(mediumColoredPoint5.x , mediumColoredPoint5.y)
            lineTo(width.toFloat() + 100f, height.toFloat() + 100f)
            lineTo(-100f, height.toFloat() + 100f)
            close()
        }



        Canvas(
            modifier = Modifier
                .fillMaxSize()
        ) {
            drawPath(
                path = mediumColoredPath,
                color = DarkJungleGreen
            )
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {




        AnimatedVisibility(
            visible = value.value,
            enter = expandHorizontally(
                animationSpec = tween(
                    durationMillis = 1000,
                    delayMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            ) { 0 }
        ) {


            Card(
                modifier = Modifier
                    .aspectRatio(1f)
                    .wrapContentSize()
                    .padding(horizontal = 20.dp),
                backgroundColor = Onyx
            ) {

                Column(modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 10.dp)) {

                    ReminderTextField(
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 5.dp),
                        textState = emailState,
                        label = "Email :"
                    )



                    ReminderTextField(
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 5.dp),
                        textState = passwordState,
                        label = "Password :",
                        isPasswordType = true
                    )


                    Button(
                        modifier = Modifier
                            .padding(horizontal = 20.dp, vertical = 10.dp)
                            .fillMaxWidth(),
                        onClick = { if (!isLoading.value) {onLogin() }},
                        colors = ButtonDefaults.buttonColors(backgroundColor = VistaBlue),
                        shape = RoundedCornerShape(16),
                        elevation = ButtonDefaults.elevation(defaultElevation = 5.dp)
                    ) {
                        Text(
                            modifier = Modifier.padding(5.dp),
                            text = if (!isLoading.value) "Login" else "Loading...",
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(start = 20.dp)
                                .padding(vertical = 5.dp),
                            text = "Need an account ?",
                            fontWeight = FontWeight.Light,
                            color = GamaAdjustedGray
                        )
                        Text(
                            modifier = Modifier.padding(5.dp)
                                .clickable { onRegister() },
                            text = "Register",
                            fontWeight = FontWeight.Light,
                            color = VividCerulean
                        )
                    }

                    Button(
                        modifier = Modifier
                            .padding(horizontal = 20.dp, vertical = 10.dp)
                            .fillMaxWidth(1f),
                        onClick = {
                                  onSignInWithGoogle()
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                        shape = RoundedCornerShape(16),
                        elevation = ButtonDefaults.elevation(defaultElevation = 5.dp)
                    ) {

                        Icon(
                            painter = painterResource(id = com.google.android.gms.base.R.drawable.googleg_standard_color_18),
                            contentDescription = "Email"
                        )
                        Text(
                            modifier = Modifier.padding(10.dp),
                            text = "SignIn With Google",
                            color = Color.Black,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }



                }


            }
        }

    }



}

