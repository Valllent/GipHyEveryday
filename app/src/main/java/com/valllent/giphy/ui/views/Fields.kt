package com.valllent.giphy.ui.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.valllent.giphy.ui.wrappers.PreviewWrapper

@ExperimentalMaterial3Api
@Composable
fun SearchField(
    request: MutableState<String>,
    focusRequester: FocusRequester = FocusRequester()
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
            modifier = Modifier
                .focusRequester(focusRequester),
            value = request.value,
            onValueChange = {
                request.value = it
            },
            singleLine = true,
            textStyle = MaterialTheme.typography.titleLarge,
            label = {
                Text(stringResource(id = R.string.search))
            },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
        )
    }
}

@ExperimentalMaterial3Api
@Preview(showBackground = true)
@Composable
private fun PreviewSearchField() {
    PreviewWrapper {
        SearchField(request = remember { mutableStateOf("Cat") })
    }
}