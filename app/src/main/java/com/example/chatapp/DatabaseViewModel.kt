package com.example.chatapp

import androidx.lifecycle.ViewModel
import com.example.chatapp.data.DatabaseState
import com.example.chatapp.models.Conversation
import com.example.chatapp.models.Friend
import com.example.chatapp.models.Message
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@Suppress("UNCHECKED_CAST")
class DatabaseViewModel : ViewModel() {
    private val _dbState = MutableStateFlow(DatabaseState())
    val dbState: StateFlow<DatabaseState> = _dbState.asStateFlow()

    val firebaseDb = FirebaseFirestore.getInstance()

    fun getFriendlist() {
        firebaseDb.collection("contacts")
            .whereEqualTo("user.uid", dbState.value.auth.currentUser?.uid)
            .limit(1)
            .get()
            .addOnSuccessListener { contacts ->
                val friendList = mutableListOf<Friend>()
                val friends: List<Map<String, HashMap<String, String>>> = contacts.documents[0].get("user.friends") as List<Map<String, HashMap<String, String>>>

                for (friend in friends) {
                    friendList.add(
                        Friend(
                            friend["email"].toString(),
                            friend["name"].toString(),
                            friend["uid"].toString(),
                        )
                    )
                }

                _dbState.update { currentState ->
                    currentState.copy(
                        friendList = friendList
                    )
                }
            }
    }

    fun getConversationList() {
        firebaseDb.collection("conversation")
            .whereArrayContains("users", dbState.value.auth.currentUser?.uid.toString())
            .get()
            .addOnSuccessListener { contacts ->
                val conversationList = mutableListOf<Conversation>()
                val documents = contacts.documents

                for (document in documents) {

                    val convos = document.toObject<Conversation>()

                    val messages = convos?.messages
                    val users = convos?.users


                    conversationList.add(
                        Conversation(
                            messages,
                            users
                        )
                    )
                }

                _dbState.update { currentState ->
                    currentState.copy(
                        conversationList = conversationList
                    )
                }
            }
    }
}