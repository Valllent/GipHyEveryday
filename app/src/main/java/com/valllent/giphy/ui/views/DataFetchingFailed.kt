package com.valllent.giphy.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.valllent.giphy.R
import com.valllent.giphy.ui.theme.ProjectTheme


@Composable
fun DataFetchingFailed(onRetryClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                stringResource(R.string.can_not_fetch_data),
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    onRetryClick()
                }
            ) {
                Text(
                    stringResource(R.string.retry),
                    fontSize = 24.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DataFetchingFailedPreview() {
    ProjectTheme {
        DataFetchingFailed {}
    }
}