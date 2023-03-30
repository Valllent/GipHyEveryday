package com.valllent.giphy.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valllent.giphy.ui.wrappers.PreviewWrapper


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectNavigationDrawer(drawerState: DrawerState, content: @Composable () -> Unit) {
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
                    arrayOf("Trending", "Artists", "Clips", "Stories").forEachIndexed { i, value ->
                        NavigationDrawerItem(
                            modifier = Modifier
                                .padding(4.dp, 4.dp)
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

                    Spacer(modifier = Modifier.height(8.dp))
                    Divider(modifier = Modifier.padding(horizontal = 16.dp))
                    Spacer(modifier = Modifier.height(8.dp))

                    arrayOf("Settings").forEachIndexed { i, value ->
                        NavigationDrawerItem(
                            modifier = Modifier
                                .padding(4.dp, 4.dp)
                                .wrapContentHeight()
                                .fillMaxWidth(),
                            selected = false,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
private fun PreviewProjectNavigationDrawer() {
    PreviewWrapper {
        ProjectNavigationDrawer(DrawerState(DrawerValue.Open)) {

        }
    }
}