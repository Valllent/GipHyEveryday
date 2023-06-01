package com.valllent.giphy.app.presentation.ui.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valllent.giphy.R
import com.valllent.giphy.app.presentation.ui.wrappers.PreviewWrapper

@ExperimentalMaterial3Api
@Composable
fun SearchField(
    request: String,
    enabled: Boolean,
    onSearchRequestChange: (String) -> Unit,
    onSearchClick: () -> Unit,
    focusRequester: FocusRequester = FocusRequester(),
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 8.dp, 16.dp, 0.dp)
            .clip(MaterialTheme.shapes.large)
            .border(
                BorderStroke(3.dp, SolidColor(MaterialTheme.colorScheme.primary)),
                MaterialTheme.shapes.large
            )
    ) {
        TextField(
            value = request,
            onValueChange = {
                onSearchRequestChange(it)
            },
            modifier = Modifier
                .focusRequester(focusRequester),
            singleLine = true,
            textStyle = MaterialTheme.typography.titleLarge,
            label = {
                Text(stringResource(id = R.string.search))
            },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions {
                onSearchClick()
            },
            trailingIcon = {
                IconButton(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .height(40.dp)
                        .width(40.dp)
                        .clip(CircleShape),
                    onClick = {
                        onSearchClick()
                    },
                    enabled = enabled
                ) {
                    Icon(
                        Icons.Default.Search,
                        stringResource(R.string.search),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                    )
                }
            }
        )
    }
}

@ExperimentalMaterial3Api
@Preview(showBackground = true)
@Composable
private fun PreviewSearchField() {
    PreviewWrapper {
        SearchField(request = "Cat", true, {}, {})
    }
}