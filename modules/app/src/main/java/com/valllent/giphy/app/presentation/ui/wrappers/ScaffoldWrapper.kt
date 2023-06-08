package com.valllent.giphy.app.presentation.ui.wrappers

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.valllent.giphy.app.presentation.ui.GlobalListeners
import com.valllent.giphy.app.presentation.ui.views.ProjectNavigationDrawer
import com.valllent.giphy.app.presentation.ui.views.ProjectTopAppBar
import kotlinx.coroutines.launch

@Composable
fun ScaffoldWrapper(
    topAppBarActions: @Composable RowScope.() -> Unit = {},
    onTopAppBarLogoClick: () -> Unit = {},
    globalListeners: GlobalListeners,
    content: @Composable () -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ProjectNavigationDrawer(drawerState, globalListeners) {
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
        ScaffoldWrapper(globalListeners = GlobalListeners({}, { TODO() })) {

        }
    }
}