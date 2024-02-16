package com.example.chatapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.chatapp.data.DatabaseState
import com.example.chatapp.models.Friend

@Composable
fun ContactCard(contact: Friend, onContactClick: (Friend) -> Unit = {}) {
    Row(modifier = Modifier.clickable { onContactClick(contact) }) {
        Image(
            painter = painterResource(R.drawable.hito), // replace with user icon
            contentDescription = "Contact profile picture", //idc
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Text(
                text = contact.name,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleSmall)
            Spacer(modifier = Modifier.height(4.dp))


            Text(
                text = "friend.excerpt",
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(all = 4.dp),
                maxLines = 1,
                style = MaterialTheme.typography.bodyMedium
            )

        }
    }
}

@Composable
fun ContactScreen(
    dbModel: DatabaseState,
    onContactClick: (Friend) -> Unit = {}
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(16.dp)) {
        items(dbModel.friendList) {contact ->
            ContactCard(contact, onContactClick = {onContactClick(contact)})
        }
    }
}

//@Preview
//@Composable
//fun ContactPreview() {
//    ContactScreen(contacts = SampleDataContacts.contactsSample)
//}