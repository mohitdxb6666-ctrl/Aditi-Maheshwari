package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.ScreenDestination

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isDarkTheme by viewModel.isDarkMode.collectAsState()
            val systemDark = isSystemInDarkTheme()
            val effectiveDark = isDarkTheme

            MyApplicationTheme(darkTheme = effectiveDark) {
                MainAppScreen(
                    viewModel = viewModel,
                    isDarkTheme = effectiveDark,
                    onToggleDarkTheme = { viewModel.toggleDarkMode() },
                    onSendReminder = { viewModel.triggerExamReminderNotification() }
                )
            }
        }
    }
}

enum class NavigationTab(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val destination: ScreenDestination
) {
    HOME("Home", Icons.Filled.Home, Icons.Outlined.Home, ScreenDestination.Home),
    MOCK_TESTS("Mock Tests", Icons.Filled.Quiz, Icons.Outlined.Quiz, ScreenDestination.MockTestHub),
    AI_BOT("AI Doubt", Icons.Filled.Psychology, Icons.Outlined.Psychology, ScreenDestination.AiDoubtSolver),
    PEER_GROUPS("Peer Circles", Icons.Filled.Groups, Icons.Outlined.Groups, ScreenDestination.PeerStudyGroups),
    ANALYTICS("Analytics", Icons.Filled.Analytics, Icons.Outlined.Analytics, ScreenDestination.AnalyticsDashboard),
    TIMER("Timer", Icons.Filled.Timer, Icons.Outlined.Timer, ScreenDestination.RevisionTimer)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScreen(
    viewModel: MainViewModel,
    isDarkTheme: Boolean,
    onToggleDarkTheme: () -> Unit,
    onSendReminder: () -> Unit
) {
    val currentScreen by viewModel.currentScreen.collectAsState()

    val isTopLevel = currentScreen is ScreenDestination.Home ||
            currentScreen is ScreenDestination.MockTestHub ||
            currentScreen is ScreenDestination.AiDoubtSolver ||
            currentScreen is ScreenDestination.PeerStudyGroups ||
            currentScreen is ScreenDestination.AnalyticsDashboard ||
            currentScreen is ScreenDestination.RevisionTimer

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (currentScreen is ScreenDestination.Home) {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                text = "CBSE 12 Study Companion",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 18.sp
                            )
                            Text(
                                text = "Science & Maths • Class 12 Boards",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = onSendReminder,
                            modifier = Modifier.testTag("notification_reminder_btn")
                        ) {
                            Icon(
                                Icons.Default.NotificationsActive,
                                contentDescription = "Test Notification Reminder",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }

                        IconButton(
                            onClick = onToggleDarkTheme,
                            modifier = Modifier.testTag("dark_mode_toggle_btn")
                        ) {
                            Icon(
                                if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                                contentDescription = "Toggle Dark Mode"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )
            }
        },
        bottomBar = {
            if (isTopLevel) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 6.dp
                ) {
                    NavigationTab.values().forEach { tab ->
                        val isSelected = when (tab) {
                            NavigationTab.HOME -> currentScreen is ScreenDestination.Home
                            NavigationTab.MOCK_TESTS -> currentScreen is ScreenDestination.MockTestHub
                            NavigationTab.AI_BOT -> currentScreen is ScreenDestination.AiDoubtSolver
                            NavigationTab.PEER_GROUPS -> currentScreen is ScreenDestination.PeerStudyGroups
                            NavigationTab.ANALYTICS -> currentScreen is ScreenDestination.AnalyticsDashboard
                            NavigationTab.TIMER -> currentScreen is ScreenDestination.RevisionTimer
                        }

                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { viewModel.navigateTo(tab.destination) },
                            icon = {
                                Icon(
                                    if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                                    contentDescription = tab.title
                                )
                            },
                            label = {
                                Text(
                                    text = tab.title,
                                    fontSize = 10.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            modifier = Modifier.testTag("nav_tab_${tab.name.lowercase()}")
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val dest = currentScreen) {
                is ScreenDestination.Home -> HomeScreen(viewModel)
                is ScreenDestination.SubjectDetail -> SubjectDetailScreen(dest.subjectCode, viewModel)
                is ScreenDestination.ChapterStudy -> ChapterStudyScreen(dest.chapterId, viewModel)
                is ScreenDestination.MockTestHub -> MockTestHubScreen(viewModel)
                is ScreenDestination.ActiveMockTest -> ActiveMockTestScreen(dest.subjectCode, dest.durationMinutes, dest.testTitle, viewModel)
                is ScreenDestination.AiDoubtSolver -> AiDoubtSolverScreen(viewModel)
                is ScreenDestination.PeerStudyGroups -> PeerStudyGroupsScreen(viewModel)
                is ScreenDestination.AnalyticsDashboard -> AnalyticsDashboardScreen(viewModel)
                is ScreenDestination.RevisionTimer -> RevisionTimerScreen(viewModel)
            }
        }
    }
}
