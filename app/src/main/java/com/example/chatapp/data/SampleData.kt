package com.example.chatapp.data

import com.example.chatapp.models.Message
import com.google.firebase.Timestamp

/**
 * com.example.chatapp.data.SampleData for Jetpack Compose Tutorial
 */
object SampleData {
    // Sample conversation data
    val conversationSample = listOf(
        Message(
            "1",
            Timestamp(Timestamp.now().toDate()),
            "Test...Test...Test..."
        ),
        Message("1",
            Timestamp(Timestamp.now().toDate()),
            "Testnachricht die Zweite"
        ),
        Message("1",
            Timestamp(Timestamp.now().toDate()),
            """I think Kotlin is my favorite programming language.
            |It's so much fun!""".trim()
        ),
        Message("1",
            Timestamp(Timestamp.now().toDate()),
            "Searching for alternatives to XML layouts..."
        )
    )
}
