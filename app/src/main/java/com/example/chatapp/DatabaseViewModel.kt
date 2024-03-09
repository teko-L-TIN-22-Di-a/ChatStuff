package com.example.chatapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatapp.data.DatabaseState
import com.example.chatapp.models.Conversation
import com.example.chatapp.models.Friend
import com.example.chatapp.models.Message
import com.example.chatapp.models.User
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.snapshots
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

    fun sendMessage(inputVal: String, uid: String, partnerUid: String) {
        val message: String = inputVal;
        if (message.isNotEmpty()) {
            firebaseDb.collection("conversation")
                .whereArrayContains("users", dbState.value.auth.currentUser?.uid.toString())
                .get()
                .addOnSuccessListener { contacts ->
                    val documents = contacts.documents

                    if (documents.isEmpty()) {
                        firebaseDb.collection("conversation").add(
                            Conversation(
                                listOf(Message(uid, Timestamp.now(), message)),
                                listOf(partnerUid, uid)
                            )
                        )
                    } else {
                        val currentDocument = documents.filter { document ->
                            document.toObject<Conversation>()?.users!!.containsAll(
                                listOf(uid, partnerUid)
                            )
                        }.firstOrNull()

                        if (currentDocument == null) {
                            firebaseDb.collection("conversation").add(
                                Conversation(
                                    listOf(Message(uid, Timestamp.now(), message)),
                                    listOf(partnerUid, uid)
                                )
                            )
                        } else {
                            currentDocument?.reference?.update(
                                "messages", FieldValue.arrayUnion(
                                    Message(uid, Timestamp.now(), message)
                                )
                            )
                        }


                    }


                }
        }
    }

    fun addFriend(email: String) {
        val myUser = Firebase.auth.currentUser

        firebaseDb.collection("users").whereEqualTo("email", email).limit(1).get()
            .addOnSuccessListener { friend ->

                if (!friend.isEmpty()) {
                    val document = friend.documents[0]
                    val foundFriend = document.toObject<Friend>()

                    if (foundFriend != null) {
                        firebaseDb.collection("contacts")
                            .whereEqualTo("user.uid", dbState.value.auth.currentUser?.uid).limit(1)
                            .get()
                            .addOnSuccessListener { contacts ->
                                val documents = contacts.documents

                                if (documents.isEmpty()) {
                                    firebaseDb.collection("contacts").add(
                                        mapOf(
                                            "user" to User(listOf(), myUser!!.uid)
                                        )
                                    ).addOnSuccessListener { document ->
                                        document.update(
                                            "user.friends", FieldValue.arrayUnion(
                                                Friend(
                                                    foundFriend.email,
                                                    foundFriend.name,
                                                    foundFriend.uid
                                                )
                                            )
                                        ).addOnSuccessListener { e ->
                                            //reverse add
                                            reverseAddFriend(myUser.email!!, foundFriend.uid)
                                        }
                                    }
                                } else {
                                    documents[0]?.reference?.update(
                                        "user.friends", FieldValue.arrayUnion(
                                            Friend(
                                                foundFriend.email,
                                                foundFriend.name,
                                                foundFriend.uid
                                            )
                                        )
                                    )
                                }
                            }
                    }
                }
            }
    }

    fun reverseAddFriend(emailToAddFriendTo: String, uidToAddFriendTo: String) {
        val myUser = Firebase.auth.currentUser

        firebaseDb.collection("users").whereEqualTo("email", emailToAddFriendTo).limit(1).get()
            .addOnSuccessListener { friend ->

                if (!friend.isEmpty()) {
                    val document = friend.documents[0]
                    val foundFriend = document.toObject<Friend>()

                    if (foundFriend != null) {
                        firebaseDb.collection("contacts")
                            .whereEqualTo("user.uid", uidToAddFriendTo).limit(1)
                            .get()
                            .addOnSuccessListener { contacts ->
                                val documents = contacts.documents

                                if (documents.isEmpty()) {
                                    firebaseDb.collection("contacts").add(
                                        mapOf(
                                            "user" to User(listOf(), uidToAddFriendTo)
                                        )
                                    ).addOnSuccessListener { document ->
                                        document.update(
                                            "user.friends", FieldValue.arrayUnion(
                                                Friend(
                                                    foundFriend.email,
                                                    foundFriend.name,
                                                    foundFriend.uid
                                                )
                                            )
                                        )
                                    }
                                } else {
                                    documents[0]?.reference?.update(
                                        "user.friends", FieldValue.arrayUnion(
                                            Friend(
                                                foundFriend.email,
                                                foundFriend.name,
                                                foundFriend.uid
                                            )
                                        )
                                    )
                                }
                            }
                    }
                }
            }
    }

    fun getFriendlist() {
        firebaseDb.collection("contacts")
            .whereEqualTo("user.uid", dbState.value.auth.currentUser?.uid).limit(1)
            .addSnapshotListener { contacts, e ->
                val friendList = mutableListOf<Friend>()
                if (contacts != null && contacts.documents.isNotEmpty()) {
                    val friends: List<Map<String, HashMap<String, String>>> =
                        contacts.documents[0].get("user.friends") as List<Map<String, HashMap<String, String>>>

                    for (friend in friends) {
                        friendList.add(
                            Friend(
                                friend["email"].toString(),
                                friend["name"].toString(),
                                friend["uid"].toString(),
                            )
                        )
                    }
                }

                _dbState.update { currentState ->
                    currentState.copy(
                        friendList = friendList
                    )
                }
            }
    }

    fun createEmptyConversation(myUid: String, partnerUid: String): Conversation {
        val newConvo = Conversation()

        return newConvo
    }

    fun getConversationList() {
        firebaseDb.collection("conversation")
            .whereArrayContains("users", dbState.value.auth.currentUser?.uid.toString())
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    return@addSnapshotListener
                }

                val conversationList = mutableListOf<Conversation>()
                val documents = snapshot!!.documents

                for (document in documents) {
                    val convos = document.toObject<Conversation>()
                    val messages = convos?.messages
                    val users = convos?.users

                    conversationList.add(
                        Conversation(
                            messages, users
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