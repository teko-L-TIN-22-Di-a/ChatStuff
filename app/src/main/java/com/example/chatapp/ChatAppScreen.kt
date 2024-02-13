package com.example.chatapp

import SampleData
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

enum class ChatScreen(@StringRes val title: Int) {
    Start(title = R.string.app_name),
    Contacts(title = R.string.contactTitle),
    Conversation(title = R.string.messagesTitle),
    Settings(title = R.string.optionsTitle)
}


@Composable
fun ChatAppTopBar(
    currentScreen: ChatScreen,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(stringResource(currentScreen.title)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
    )
}

@Composable
fun ChatAppBottomBar(
    navController: NavHostController,
) {
    var selectedItem by remember { mutableIntStateOf(0) }
    val icons = listOf(
        Icons.Default.Home,
        Icons.Default.Person,
        Icons.Default.Email,
        Icons.Default.Settings
    )

    NavigationBar {
        ChatScreen.entries.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { Icon(icons[index], contentDescription = null) },
                label = { stringResource(id = item.title) },
                selected = selectedItem == index,
                onClick = {
                    navController.navigate(item.name)
                    selectedItem = index;
                }
            )
        }
    }
}

@Composable
fun ChatApp(
    navController: NavHostController = rememberNavController(),
    viewModel: ChatViewModel = viewModel(),
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = ChatScreen.valueOf(
        backStackEntry?.destination?.route ?: ChatScreen.Start.name
    )

    Scaffold(
        topBar = {
            ChatAppTopBar(
                currentScreen = currentScreen
            )
        },
        bottomBar = {
            ChatAppBottomBar(
                navController = navController
            )
        }
    ) { innerPadding ->
        val uiState by viewModel.uiState.collectAsState()
        NavHost(
            navController = navController,
            startDestination = ChatScreen.Start.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = ChatScreen.Start.name) {
                StartScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(dimensionResource(R.dimen.padding_medium)),
                )
            }

            composable(route = ChatScreen.Conversation.name) {
                val currentMessages = SampleData.conversationSample.filter { it.id == uiState.currentConversationContact.id}

                Conversation(currentMessages, uiState.currentConversationContact)
            }

            composable(route = ChatScreen.Contacts.name) {
                ContactScreen(
                    SampleDataContacts.contactsSample,
                    onContactClick = {
                        viewModel.setCurrentConversation(it)
                        navController.navigate(ChatScreen.Conversation.name)
                    })
            }

            composable(route = ChatScreen.Settings.name) {
                SettingsScreen()
            }
        }
    }
}

@Preview
@Composable
fun PreviewApp() {
    ChatApp()
}
