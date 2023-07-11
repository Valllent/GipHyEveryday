package com.valllent.giphy.app.presentation.ui.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.valllent.giphy.R
import com.valllent.giphy.app.presentation.ui.wrappers.PreviewWrapper


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectTopAppBar(
    onDrawerButtonClick: () -> Unit,
    onLogoClick: () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        title = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                TitleOnSurface(
                    modifier = Modifier
                        .clickable { onLogoClick() },
                    text = stringResource(R.string.app_name_short),
                    textColor = MaterialTheme.colorScheme.onPrimary,
                    backgroundColor = MaterialTheme.colorScheme.primary,
                )
            }
        },
        navigationIcon = {
            ProjectIconButton(
                imageVector = Icons.Default.Menu,
                contentDescription = stringResource(id = R.string.menu),
                onClick = onDrawerButtonClick
            )
        },
        actions = actions,
    )
}

@Preview
@Composable
private fun PreviewProjectTopAppBar() {
    PreviewWrapper {
        ProjectTopAppBar(
            onDrawerButtonClick = {},
        )
    }
}