import com.example.chatapp.Message

/**
 * SampleData for Jetpack Compose Tutorial 
 */
object SampleData {
    // Sample conversation data
    val conversationSample = listOf(
        Message(
            1,
            "Tobia",
            "Test...Test...Test..."
        ),
        Message(1,
            "Tobia",
            "Testnachricht die Zweite"
        ),
        Message(1,
            "Lexi",
            """I think Kotlin is my favorite programming language.
            |It's so much fun!""".trim()
        ),
        Message(1,
            "Lexi",
            "Searching for alternatives to XML layouts..."
        ),
        Message(2,
            "Lexi",
            """Hey, take a look at Jetpack Compose, it's great!
            |It's the Android's modern toolkit for building native UI.
            |It simplifies and accelerates UI development on Android.
            |Less code, powerful tools, and intuitive Kotlin APIs :)""".trim()
        ),
        Message(2,
            "Lexi",
            "It's available from API 21+ :)"
        ),
        Message(2,
            "Lexi",
            "Writing Kotlin for UI seems so natural, Compose where have you been all my life?"
        ),
        Message(2,
            "Lexi",
            "Android Studio next version's name is Arctic Fox"
        ),
        Message(2,
            "Lexi",
            "Android Studio Arctic Fox tooling for Compose is top notch ^_^"
        ),
        Message(3,
            "Lexi",
            "I didn't know you can now run the emulator directly from Android Studio"
        ),
        Message(3,
            "Lexi",
            "Compose Previews are great to check quickly how a composable layout looks like"
        ),
        Message(3,
            "Lexi",
            "Previews are also interactive after enabling the experimental setting"
        ),
        Message(3,
            "Lexi",
            "Have you tried writing build.gradle with KTS?"
        ),
    )
}
