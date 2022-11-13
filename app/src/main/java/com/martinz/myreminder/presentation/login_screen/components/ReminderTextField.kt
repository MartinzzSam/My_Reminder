package com.martinz.myreminder.presentation.login_screen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.martinz.myreminder.core.hidePasswordIcon
import com.martinz.myreminder.core.showPasswordIcon
import com.martinz.myreminder.presentation.login_screen.ui.GamaAdjustedGray

@Composable
fun ReminderTextField(
    modifier: Modifier = Modifier,
    textState: MutableState<String>,
    label: String,
    isPasswordType: Boolean = false
) {

    var passwordVisible by remember { mutableStateOf(false) }


    Column(
        modifier = modifier
    ) {
        Text(
            modifier = Modifier.padding(5.dp),
            text = label,
            fontWeight = FontWeight.ExtraBold,
            color = GamaAdjustedGray
        )
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16)),
            value = textState.value,
            onValueChange = { textState.value = it },
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.White,
                cursorColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            trailingIcon = {
                if (isPasswordType) {


                    Icon(
                        modifier = Modifier
                            .padding(5.dp)
                            .clickable { passwordVisible = !passwordVisible },
                        painter = if (passwordVisible) showPasswordIcon() else hidePasswordIcon(),
                        contentDescription = "Hide Password",
                        tint = GamaAdjustedGray
                    )


                }
            },
            visualTransformation = if (!passwordVisible && isPasswordType) PasswordVisualTransformation(
                mask = '●'
            ) else VisualTransformation.None,
            keyboardOptions = if (!isPasswordType) KeyboardOptions(keyboardType = KeyboardType.Email) else KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            textStyle = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.SemiBold ),
            singleLine = true

        )
    }

}