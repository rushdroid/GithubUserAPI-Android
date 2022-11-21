package com.example.rushabhtawkto.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
fun DefaultPreview() {
    UserDetailScreen(
        followersCount = "Followers: 123",
        followingCount = "Following: 123",
        name = "John Snow",
        company = "Snow",
        blog = "Find Me",
        note = "Fire and Ice",
        profileImage = "Profile Image",
        onNoteChanged = {},
        onSaveClicked = {}
    )
}

@Composable
private fun MyText(text: String) {
    Text(text = text)
}

@Composable
fun UserDetailScreen(
    followersCount: String,
    followingCount: String,
    name: String,
    company: String,
    blog: String,
    note: String,
    profileImage: String,
    onNoteChanged: (String) -> Unit,
    onSaveClicked: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(all = 20.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(state = rememberScrollState()),
            horizontalAlignment = Alignment.Start,
        ) {
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .background(Color.Gray)
                    .align(Alignment.CenterHorizontally)
            ) {
                AsyncImage(
                    model = profileImage,
                    contentDescription = "",
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                MyText(text = followersCount)
                Text(text = followingCount)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .border(shape = RoundedCornerShape(10.dp), width = 2.dp, color = Color.Gray)
                    .padding(all = 10.dp)
            ) {
                Text(text = name)
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = company)
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = blog)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(text = "Note")
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = note,
                onValueChange = onNoteChanged,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            TextButton(
                onClick = onSaveClicked,
                modifier = Modifier
                    .background(Color.Black)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(text = "Save")
            }
        }
    }
}

