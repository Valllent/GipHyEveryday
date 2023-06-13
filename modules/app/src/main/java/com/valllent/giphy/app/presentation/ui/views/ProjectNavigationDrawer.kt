package com.valllent.giphy.app.presentation.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valllent.giphy.app.presentation.data.view.DrawerItemUiModel
import com.valllent.giphy.app.presentation.ui.GlobalListeners
import com.valllent.giphy.app.presentation.ui.wrappers.PreviewWrapper
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectNavigationDrawer(
    drawerState: DrawerState,
    globalListeners: GlobalListeners,
    content: @Composable () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        content = content,
        drawerContent = {
            Surface(
                modifier = Modifier
                    .width(300.dp)
                    .fillMaxHeight(),
            ) {
                Column {
                    Logo(modifier = Modifier.align(Alignment.CenterHorizontally))

                    DrawerItemUiModel.getInOrder().forEachIndexed { i, drawerItem ->
                        if (drawerItem == DrawerItemUiModel.SEPARATOR) {
                            Divider(
                                modifier = Modifier.padding(
                                    horizontal = 16.dp,
                                    vertical = 8.dp
                                )
                            )
                            return@forEachIndexed
                        }


                        val currentScreenRoute =
                            globalListeners.getNavigationController().currentBackStackEntry?.destination?.route
                        val isButtonSelected = drawerItem.screenRoute == currentScreenRoute
                        NavigationDrawerItem(
                            modifier = Modifier
                                .padding(4.dp, 2.dp)
                                .wrapContentHeight()
                                .fillMaxWidth(),
                            selected = isButtonSelected,
                            label = {
                                Text(stringResource(drawerItem.titleResId))
                            },
                            onClick = {
                                globalListeners.onDrawerItemSelected(drawerItem)
                                coroutineScope.launch {
                                    drawerState.close()
                                }
                            }
                        )
                    }
                }
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
private fun PreviewProjectNavigationDrawer() {
    PreviewWrapper {
        ProjectNavigationDrawer(DrawerState(DrawerValue.Open), GlobalListeners({}, { throw NullPointerException() })) {

        }
    }
}