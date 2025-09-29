package com.example.lovein.createpartnerlist

import android.net.Uri
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.lovein.R
import com.example.lovein.common.components.CommonContainer
import com.example.lovein.common.components.CommonNavigationButton
import com.example.lovein.common.components.CustomAlertDialog
import com.example.lovein.common.constants.CommonConstants
import com.example.lovein.common.constants.DescriptionConstants
import com.example.lovein.common.constants.NavigationConstants
import com.example.lovein.common.data.NavigationScreens
import com.example.lovein.common.models.Partner
import com.example.lovein.common.objects.LocalizationManager
import com.example.lovein.createpartnerlist.components.PartnerCard
import com.example.lovein.createpartnerlist.validation.validatePartners
import com.example.lovein.ui.theme.helveticaFontFamily
import com.example.lovein.utils.addRandomPartner
import com.example.lovein.utils.cleanupActionFeedback
import com.example.lovein.utils.convertPartnerListToPartnerDTOList
import com.example.lovein.utils.openPdf
import com.example.lovein.utils.showAlertDialog
import kotlinx.coroutines.CoroutineScope

@Composable
fun CreatePartnerListScreen(
    navController: NavController,
    partnerList: MutableList<MutableState<Partner>>
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    val backStackEntry by navController.currentBackStackEntryAsState()
    val savedStateHandle = backStackEntry?.savedStateHandle

    val messageFlow = remember(savedStateHandle) {
        savedStateHandle?.getStateFlow(CommonConstants.SNACKBAR_MESSAGE, null as String?)
    }
    val actionFlow = remember(savedStateHandle) {
        savedStateHandle?.getStateFlow(CommonConstants.SNACKBAR_ACTION, null as String?)
    }
    val uriFlow = remember(savedStateHandle) {
        savedStateHandle?.getStateFlow(CommonConstants.SNACKBAR_URI, null as Uri?)
    }

    val message by (messageFlow?.collectAsState() ?: remember { mutableStateOf<String?>(null) })
    val action by (actionFlow?.collectAsState() ?: remember { mutableStateOf<String?>(null) })
    val uri by (uriFlow?.collectAsState() ?: remember { mutableStateOf<Uri?>(null) })

    LaunchedEffect(Unit) { cleanupActionFeedback(partnerList) }

    LaunchedEffect(message, action, uri) {
        val msg = message
        if (!msg.isNullOrBlank()) {
            val result = snackbarHostState.showSnackbar(
                message = msg,
                actionLabel = action,
                duration = SnackbarDuration.Short
            )
            if (result == SnackbarResult.ActionPerformed) {
                uri?.let { openPdf(context, it) }
            }

            savedStateHandle?.set(CommonConstants.SNACKBAR_MESSAGE, null)
            savedStateHandle?.set(CommonConstants.SNACKBAR_ACTION, null)
            savedStateHandle?.set(CommonConstants.SNACKBAR_URI, null)
        }
    }

    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val listState: LazyListState = rememberLazyListState()

    val isAlertDialogOpen: MutableState<Boolean> = remember { mutableStateOf(false) }
    val alertDialogTitle: MutableState<String> = remember { mutableStateOf("") }
    val alertDialogText: MutableState<String> = remember { mutableStateOf("") }

    CommonContainer(
        navController = navController,
        snackbarHostState = snackbarHostState
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 16.dp,
                    top = innerPadding.calculateTopPadding(),
                    end = 16.dp,
                    bottom = 16.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(size = 8.dp)),
                state = listState,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                itemsIndexed(items = partnerList) { index, partner ->
                    PartnerCard(partner = partner, index = index, partnerList = partnerList)
                }

                item {
                    ExtendedFloatingActionButton(
                        modifier = Modifier
                            .align(alignment = Alignment.Start)
                            .indication(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ),
                        text = {
                            Text(
                                text = LocalizationManager.getLocalizedString(
                                    context = context,
                                    resourceId = R.string.add_partner
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
                                imageVector = Icons.Default.GroupAdd,
                                contentDescription = DescriptionConstants.GROUP_ADD_ICON,
                                tint = Color.White
                            )
                        },
                        onClick = { addRandomPartner(partnerList, coroutineScope, listState) },
                        containerColor = Color.Transparent,
                        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                    )
                }
            }

            CommonNavigationButton(
                text = LocalizationManager.getLocalizedString(
                    context = context,
                    resourceId = R.string.start_button
                ),
                icon = Icons.Default.PlayCircle,
                iconContentDescription = DescriptionConstants.PLAY_CIRCLE_ICON,
                onClick = {
                    val result = validatePartners(partnerList)

                    if (!result.isValid) {
                        showAlertDialog(
                            context,
                            result,
                            isAlertDialogOpen,
                            alertDialogTitle,
                            alertDialogText
                        )
                    } else {
                        navController.currentBackStackEntry?.savedStateHandle?.set(
                            key = NavigationConstants.PARTNER_DTO_LIST_KEY,
                            value = convertPartnerListToPartnerDTOList(partnerList)
                        )

                        navController.navigate(route = NavigationScreens.ERO_ZONE_EXPLORER_SCREEN.name)
                    }
                },
                modifier = Modifier
                    .padding(top = 16.dp)
                    .imePadding()
            )

            if (isAlertDialogOpen.value) {
                CustomAlertDialog(
                    isAlertDialogOpen = isAlertDialogOpen,
                    title = alertDialogTitle.value,
                    text = alertDialogText.value,
                    navController = navController
                )
            }
        }
    }
}
