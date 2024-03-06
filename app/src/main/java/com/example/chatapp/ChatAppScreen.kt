package com.example.chatapp

import com.example.chatapp.data.SampleData
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
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
import androidx.compose.runtime.mutableStateOf
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
import com.example.chatapp.authentication.LoginScreen
import com.example.chatapp.authentication.SignUpScreen
import com.example.chatapp.models.Conversation
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

enum class ChatRoutes(@StringRes val title: Int) {
    Start(title = R.string.app_name),
    Login(title = R.string.contactTitle),
    SignUp(title = R.string.contactTitle),
    Contacts(title = R.string.contactTitle),
    Conversation(title = R.string.messagesTitle),
    Settings(title = R.string.optionsTitle)
}

@Composable
fun ChatAppTopBar(
    currentScreen: ChatRoutes,
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
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = null) },
            label = { Text("Home") },
            selected = selectedItem == 1,
            onClick = {
                navController.navigate(ChatRoutes.Start.name)
                selectedItem = 1;
            })
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = null) },
            label = { Text(text = "Kontakte") },
            selected = selectedItem == 2,
            onClick = {
                navController.navigate(ChatRoutes.Contacts.name)
                selectedItem = 2;
            })
        NavigationBarItem(
            icon = { Icon(Icons.Default.Settings, contentDescription = null) },
            label = { Text(text = "Einstellungen") },
            selected = selectedItem == 3,
            onClick = {
                navController.navigate(ChatRoutes.Settings.name)
                selectedItem = 3;
            })
    }
}

@Composable
fun ChatApp(
    navController: NavHostController = rememberNavController(),
    viewModel: ChatViewModel = viewModel(),
    dataBaseModel: DatabaseViewModel = viewModel()
) {
    val user by remember { mutableStateOf(Firebase.auth.currentUser) }
    val dbModel by dataBaseModel.dbState.collectAsState()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = ChatRoutes.valueOf(
        backStackEntry?.destination?.route ?: ChatRoutes.Start.name
    )

    if(user != null && dbModel.friendList.isEmpty()) {
        dataBaseModel.getFriendlist()
        dataBaseModel.getConversationList()
    }

    Scaffold(
        modifier = Modifier.imePadding(),
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
            startDestination = ChatRoutes.Start.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = ChatRoutes.Start.name) {
                StartScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(dimensionResource(R.dimen.padding_medium)),
                    navController = navController
                )
            }

            composable(route = ChatRoutes.Conversation.name) {
                var currentConversation =
                    dbModel.conversationList.filter {
                        if(it.users != null) {
                            it.users.contains(uiState.currentConversationContact.uid)
                        } else {
                            false
                        }
                    }.firstOrNull()

                if(currentConversation == null) {
                    currentConversation = dataBaseModel.createEmptyConversation("", uiState.currentConversationContact.uid)
                }

                ConversationScreen(currentConversation, uiState.currentConversationContact, dataBaseModel)
            }

            composable(route = ChatRoutes.Login.name) {
                LoginScreen(startScreen = { navController.navigate(ChatRoutes.Start.name) })
            }

            composable(route = ChatRoutes.SignUp.name) {
                SignUpScreen(startScreen = { navController.navigate(ChatRoutes.Start.name) })
            }

            composable(route = ChatRoutes.Contacts.name) {
                ContactScreen(
                    dataBaseModel,
                    onContactClick = {
                        viewModel.setCurrentConversation(it)
                        navController.navigate(ChatRoutes.Conversation.name)
                    })
            }

            composable(route = ChatRoutes.Settings.name) {
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
