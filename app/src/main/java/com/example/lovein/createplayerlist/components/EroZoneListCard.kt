package com.example.lovein.createplayerlist.components

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lovein.R
import com.example.lovein.common.data.EroZone
import com.example.lovein.common.data.Gender
import com.example.lovein.common.models.EroZoneMutable
import com.example.lovein.common.models.NoRippleTheme
import com.example.lovein.common.models.Player
import com.example.lovein.common.objects.LocalizationManager
import com.example.lovein.ui.theme.BackgroundEroZoneUnselectColor
import com.example.lovein.ui.theme.BackgroundLightBlueColor
import com.example.lovein.ui.theme.BackgroundLightPinkColor
import com.example.lovein.ui.theme.helveticaFontFamily
import com.example.lovein.utils.convertEroZoneToEroZoneMutable
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment
import com.google.accompanist.flowlayout.SizeMode

@Composable
fun EroZoneListCard(
    player: MutableState<Player>,
    rotateX: State<Float>,
    alpha: State<Float>
) {
    val context: Context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                transformOrigin = TransformOrigin(0.5f, 0f)
                rotationX = rotateX.value
            }
            .alpha(alpha = alpha.value),
        shape = RoundedCornerShape(8.dp)
    )
    {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = player.value.color.value)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 16.dp,
                        top = 16.dp,
                        end = 16.dp,
                        bottom = 0.dp
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = LocalizationManager.getLocalizedString(
                        context = context,
                        resourceId = R.string.erogenous_zones
                    ),
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = helveticaFontFamily
                )

                CompositionLocalProvider(LocalRippleTheme provides NoRippleTheme()) {
                    ExtendedFloatingActionButton(
                        text = {
                            Text(
                                text = LocalizationManager.getLocalizedString(
                                    context = context,
                                    resourceId = R.string.clear
                                ),
                                color = Color.White,
                                fontSize = 16.sp,
                                fontStyle = FontStyle.Italic,
                                fontWeight = FontWeight.Bold,
                                fontFamily = helveticaFontFamily
                            )
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Cancel,
                                contentDescription = "cancel_icon",
                                tint = Color.White
                            )
                        },
                        onClick = {
                            player.value.selectedEroZones.clear()
                        },
                        containerColor = Color.Transparent,
                        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                FlowRow(
                    mainAxisSize = SizeMode.Expand,
                    mainAxisAlignment = MainAxisAlignment.Start,
                    mainAxisSpacing = 8.dp,
                    crossAxisSpacing = 12.dp
                ) {
                    buildGenderErogenousZoneList(player.value.gender.value).forEach { eroZone ->
                        val eroZoneMutable: EroZoneMutable = convertEroZoneToEroZoneMutable(eroZone)

                        Row(
                            modifier = Modifier
                                .background(
                                    color = if (player.value.selectedEroZones.contains(eroZoneMutable)) {
                                        if (player.value.gender.value == Gender.MALE)
                                            BackgroundLightBlueColor
                                        else
                                            BackgroundLightPinkColor
                                    } else
                                        BackgroundEroZoneUnselectColor,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable {
                                    if (player.value.selectedEroZones.contains(eroZoneMutable)) {
                                        player.value.selectedEroZones.remove(eroZoneMutable)
                                    } else {
                                        player.value.selectedEroZones.add(eroZoneMutable)
                                    }
                                },
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = LocalizationManager.getLocalizedString(
                                    context = context,
                                    resourceId = eroZone.resourceId
                                ),
                                modifier = Modifier
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = helveticaFontFamily
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun buildGenderErogenousZoneList(gender: Gender): List<EroZone> {
    return EroZone.values()
        .filter { erogenousZone -> erogenousZone.gender.contains(gender) }
        .toList()
}
