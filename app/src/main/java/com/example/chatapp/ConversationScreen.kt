package com.example.chatapp

import com.example.chatapp.data.SampleData
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chatapp.models.Contact
import com.example.chatapp.models.Conversation
import com.example.chatapp.models.Friend
import com.example.chatapp.models.Message
import com.example.chatapp.ui.theme.ChatAppTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.auth


@Composable
fun MessageCard(msg: Message, contact: Friend) {
    val myUser = Firebase.auth.currentUser

    var picture = if(myUser?.uid == msg.uid) painterResource(R.drawable.hito) else painterResource(R.drawable.chris)
    var name = if(myUser?.uid == msg.uid) "Ich" else contact.name
    var surfaceColor = if(myUser?.uid == msg.uid) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface

    Row(modifier = Modifier.padding(all = 8.dp)) {
        Image(
            painter = picture,
            contentDescription = "Contact profile picture",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
        )

        Spacer(modifier = Modifier.width(8.dp))

//        val surfaceColor by animateColorAsState(
//            if (isExpanded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
//            label = "",
//        )

        Column {
            Text(
                text = name,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleSmall)
            Spacer(modifier = Modifier.height(4.dp))

            Surface(
                shape = MaterialTheme.shapes.medium,
                shadowElevation = 1.dp,
                color = surfaceColor,
                modifier = Modifier
                    .animateContentSize()
                    .padding(1.dp)
            ) {
                Text(
                    text = msg.text,
                    modifier = Modifier.padding(all = 4.dp),
                    maxLines = Int.MAX_VALUE,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

        }

    }

}

@Composable
fun ConversationScreen(conversation: Conversation, contact: Friend) {
    LazyColumn {
        if(conversation.messages != null) {
            items(conversation.messages.sortedBy { x -> x.timestamp }) { message ->
                MessageCard(message, contact);
            }
        }
    }
}

//@Preview
//@Composable
//fun PreviewConversation() {
//    ChatAppTheme {
//        val vModel: ChatViewModel = viewModel()
//        val uiState by vModel.uiState.collectAsState()
//
//        Conversation(SampleData.conversationSample, uiState.currentConversationContact)
//    }
//}

//@Preview(name = "Light Mode")
//@Preview(
//    uiMode = Configuration.UI_MODE_NIGHT_YES,
//    showBackground = true,
//    name = "Dark Mode"
//)
//@Composable
//fun PreviewMessageCard() {
//    ChatAppTheme {
//        Surface {
//            MessageCard(
//                msg = Message(1,"Yamina", "Dies ist eine längere Nachricht")
//            )
//        }
//    }
//
//}