@file:OptIn(ExperimentalMaterial3Api::class)

package com.valllent.giphy.ui.wrappers

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.valllent.giphy.ui.views.ProjectNavigationDrawer
import com.valllent.giphy.ui.views.ProjectTopAppBar
import kotlinx.coroutines.launch

@Composable
fun ScaffoldWrapper(
    topAppBarActions: @Composable RowScope.() -> Unit = {},
    onTopAppBarLogoClick: () -> Unit = {},
    content: @Composable () -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ProjectNavigationDrawer(drawerState) {
        Scaffold(
            content = { paddingValues ->
                Box(
                    modifier = Modifier.padding(paddingValues)
                ) {
                    content()
                }
            },
            topBar = {
                ProjectTopAppBar(
                    onLogoClick = onTopAppBarLogoClick,
                    onDrawerButtonClick = {
                        scope.launch {
                            if (drawerState.isClosed) drawerState.open() else drawerState.close()
                        }
                    },
                    actions = topAppBarActions
                )
            },
            floatingActionButtonPosition = FabPosition.End,
            floatingActionButton = {
                // TODO: Add
            }
        )
    }

}


@Composable
@Preview
private fun PreviewScreenWrapper() {
    PreviewWrapper {
        ScaffoldWrapper {

        }
    }
}