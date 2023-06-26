package com.example.lovein.createplayerlist.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.ExpandCircleDown
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lovein.common.data.Gender
import com.example.lovein.common.models.Player
import com.example.lovein.ui.theme.helveticaFontFamily

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PlayerInputRow(
    playerList: MutableList<MutableState<Player>>,
    index: Int,
    isExpanded: MutableState<Boolean>
) {
    val keyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current
    val focusManager: FocusManager = LocalFocusManager.current

    val focusRequester: FocusRequester = remember { FocusRequester() }

    var player: MutableState<Player> = playerList[index]

    Row(modifier = Modifier.fillMaxWidth()) {
        CustomFloatingActionButton(
            onClick = {
                if (player.value.gender.value == Gender.MALE) {
                    player = mutableStateOf(Player(Gender.FEMALE))
                    playerList[index] = player
                } else {
                    player = mutableStateOf(Player(Gender.MALE))
                    playerList[index] = player
                }
            },
            leftShape = true,
            rightShape = false,
            containerColor = player.value.color,
            icon = player.value.icon,
            iconContentDescriptor = "male_female_icon"
        )

        TextField(
            value = player.value.name.value,
            onValueChange = {
                if (it.length <= 20) player.value.name.value = it
            },
            modifier = Modifier
                .weight(0.7f)
                .padding(top = 8.dp)
                .focusRequester(focusRequester),
            textStyle = TextStyle.Default.copy(
                fontSize = 16.sp,
                fontFamily = helveticaFontFamily
            ),
            placeholder = {
                Text(
                    text = "Player's name",
                    fontSize = 16.sp,
                    fontFamily = helveticaFontFamily
                )
            },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = {
                keyboardController?.hide()
                focusManager.clearFocus()
            }),
            maxLines = 1,
            shape = RoundedCornerShape(0.dp),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )

        CustomFloatingActionButton(
            onClick = { isExpanded.value = !isExpanded.value },
            leftShape = false,
            rightShape = false,
            containerColor = player.value.color,
            icon = remember { mutableStateOf(Icons.Default.ExpandCircleDown) },
            iconContentDescriptor = "expand_circle_down_icon"
        )

        CustomFloatingActionButton(
            onClick = { if (playerList.size > 1) playerList.remove(player) },
            leftShape = false,
            rightShape = true,
            containerColor = player.value.color,
            icon = remember { mutableStateOf(Icons.Default.DeleteForever) },
            iconContentDescriptor = "delete_forever_icon"
        )

        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
    }
}
