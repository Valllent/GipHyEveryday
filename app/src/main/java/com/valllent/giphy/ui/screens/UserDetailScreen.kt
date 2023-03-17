package com.valllent.giphy.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.valllent.giphy.data.User
import com.valllent.giphy.ui.theme.ProjectTheme

@Composable
fun UserDetailScreen(user: User) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.primary,
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "${user.name} (${user.age})",
                    textAlign = TextAlign.Center,
                    fontSize = 28.sp
                )
                Text(
                    user.country,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewUserDetail() {
    ProjectTheme {
        UserDetailScreen(
            User("Name", "Country", 25)
        )
    }
}