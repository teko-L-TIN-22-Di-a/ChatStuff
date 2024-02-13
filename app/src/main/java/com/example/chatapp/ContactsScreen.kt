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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

data class Contact(val id: Int, val name: String, val excerpt: String, val profilePicture: Int)

@Composable
fun ContactCard(contact: Contact, onContactClick: (Contact) -> Unit = {}) {
    Row(modifier = Modifier.clickable { onContactClick(contact) }) {
        Image(
            painter = painterResource(contact.profilePicture), // replace with user icon
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
                text = contact.excerpt,
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
    contacts: List<Contact>,
    onContactClick: (Contact) -> Unit = {}
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(16.dp)) {
        items(contacts) {contact ->
            ContactCard(contact, onContactClick = {onContactClick(contact)})
        }
    }
}

@Preview
@Composable
fun ContactPreview() {
    ContactScreen(contacts = SampleDataContacts.contactsSample)
}