@file:OptIn(ExperimentalMaterial3Api::class)

package com.valllent.giphy.ui.wrappers

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valllent.giphy.R
import com.valllent.giphy.ui.theme.ProjectTheme
import com.valllent.giphy.ui.views.Logo
import kotlinx.coroutines.launch

@Composable
fun ScaffoldWrapper(content: @Composable () -> Unit) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ProjectTheme {
        ProjectNavigationDrawer(drawerState) {
            Scaffold(
//                    contentColor = MaterialTheme.colorScheme.primary,
//                    containerColor = MaterialTheme.colorScheme.primary,
                content = { paddingValues ->
                    Box(
                        modifier = Modifier.padding(paddingValues)
                    ) {
                        content()
                    }
                },
                topBar = {
                    ProjectTopAppBar(
                        onDrawerButtonClick = {
                            scope.launch {
                                if (drawerState.isClosed) drawerState.open() else drawerState.close()
                            }
                        },
                        onSettingsClick = {
                            TODO()
                        }
                    )
                },
                floatingActionButtonPosition = FabPosition.End,
                floatingActionButton = {
                    // TODO: Add
                }
            )
        }
    }
}

@Composable
private fun ProjectTopAppBar(onDrawerButtonClick: () -> Unit, onSettingsClick: () -> Unit) {
    // TODO: Add dynamic actions!
    TopAppBar(
        title = {
            Text(
                stringResource(R.string.app_name),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        },
        navigationIcon = {
            IconButton(
                onClick = onDrawerButtonClick
            ) {
                Icon(
                    Icons.Default.Menu,
                    stringResource(R.string.menu)
                )
            }
        },
        actions = {
            IconButton(
                onClick = onSettingsClick
            ) {
                Icon(
                    Icons.Default.Settings,
                    stringResource(R.string.settings)
                )
            }
        },
    )
}

@Composable
private fun ProjectNavigationDrawer(drawerState: DrawerState, content: @Composable () -> Unit) {
    ModalNavigationDrawer(
        drawerState = drawerState,
        content = content,
        drawerContent = {
            Surface(
                modifier = Modifier.width(300.dp).fillMaxHeight(),
//                color = MaterialTheme.colorScheme.surface
            ) {
                Column {
                    Logo(modifier = Modifier.align(Alignment.CenterHorizontally))
                    arrayOf("Trending", "Artists", "Clips", "Stories").forEachIndexed { i, value ->
                        NavigationDrawerItem(
                            modifier = Modifier
                                .padding(8.dp, 4.dp)
                                .wrapContentHeight()
                                .fillMaxWidth(),
                            selected = i == 0,
                            label = {
                                Text(value)
                            },
                            onClick = {

                            }
                        )
                    }

                }
            }
        },
    )
}

@Composable
@Preview
private fun PreviewScreenWrapper() {
    ScaffoldWrapper {

    }
}

@Composable
@Preview
private fun PreviewProjectNavigationDrawer() {
    ProjectNavigationDrawer(DrawerState(DrawerValue.Open)) {

    }
}