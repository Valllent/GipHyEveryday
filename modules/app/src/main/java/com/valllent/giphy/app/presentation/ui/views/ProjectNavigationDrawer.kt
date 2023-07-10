package com.valllent.giphy.app.presentation.ui.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.valllent.giphy.R
import com.valllent.giphy.app.presentation.data.view.DrawerItemUiModel
import com.valllent.giphy.app.presentation.ui.GlobalListeners
import com.valllent.giphy.app.presentation.ui.wrappers.PreviewWrapper
import kotlinx.coroutines.launch

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

                    Text(
                        stringResource(R.string.developed_by),
                        modifier = Modifier.align(Alignment.End).padding(16.dp),
                        color = MaterialTheme.colorScheme.outline,
                        fontSize = 14.sp
                    )
                }
            }
        },
    )
}

@Composable
@Preview
private fun PreviewProjectNavigationDrawer() {
    PreviewWrapper {
        ProjectNavigationDrawer(DrawerState(DrawerValue.Open), GlobalListeners({}, { throw NullPointerException() })) {

        }
    }
}