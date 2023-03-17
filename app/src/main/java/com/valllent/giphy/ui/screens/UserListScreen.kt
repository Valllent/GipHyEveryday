package com.valllent.giphy.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.valllent.giphy.data.User
import com.valllent.giphy.ui.theme.ProjectTheme


@Composable
fun UserListScreen(
    userList: List<User>,
    onItemClick: ((User) -> Unit)? = null
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        LazyColumn {
            items(userList) { user ->
                UserListItem(user, onItemClick)
            }
        }
    }
}

@Composable
private fun UserListItem(
    user: User,
    onItemClick: ((User) -> Unit)? = null
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp, 4.dp)
            .clip(CircleShape)
            .border(BorderStroke(2.dp, MaterialTheme.colorScheme.primary), CircleShape)
            .let {
                if (onItemClick != null) {
                    it.clickable {
                        onItemClick(user)
                    }
                } else {
                    it
                }
            }
            .padding(8.dp)
    ) {
        ConstraintLayout {
            val (textsRef, ageRef) = createRefs()
            Column(
                modifier = Modifier
                    .constrainAs(textsRef) {
                        width = Dimension.fillToConstraints
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(ageRef.start)
                    }
                    .padding(start = 16.dp)
            ) {
                Text(
                    user.name,
                    fontSize = 28.sp,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )
                Text(
                    user.country,
                    fontSize = 20.sp,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    color = Color.Gray,
                )
            }
            Surface(
                modifier = Modifier
                    .constrainAs(ageRef) {
                        height = Dimension.fillToConstraints
                        centerVerticallyTo(parent)
                        end.linkTo(parent.end)
                    }
                    .aspectRatio(1f, true)
                    .border(BorderStroke(2.dp, MaterialTheme.colorScheme.primary), CircleShape)
                    .padding(4.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        user.age.toString(),
                        fontSize = 28.sp
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserListPreview() {
    ProjectTheme {
        UserListScreen(
            listOf(
                User("Valentuan", "Ukraine", 20),
                User("Great Big Cool Sebastian Tarantino", "Country", 1),
                User("Next One", "Lorem ipsum dolor sit amet, consectetur adipiscing elit", 100)
            )
        )
    }
}