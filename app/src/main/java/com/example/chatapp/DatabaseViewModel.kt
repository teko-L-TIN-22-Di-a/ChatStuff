package com.example.chatapp

import androidx.lifecycle.ViewModel
import com.example.chatapp.data.DatabaseState
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class ContactList(val uid: String, val friends: List<Friend>)
data class Friend(val email: String, val name: String, val uid: String)
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
}