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

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.example.ui.components.FrostedBackground
import com.example.ui.theme.*

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isDarkTheme by viewModel.isDarkMode.collectAsState()
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
    HOME("Dashboard", Icons.Filled.Home, Icons.Outlined.Home, ScreenDestination.Home),
    MOCK_TESTS("Mock Tests", Icons.Filled.Quiz, Icons.Outlined.Quiz, ScreenDestination.MockTestHub),
    AI_BOT("AI Doubt", Icons.Filled.Psychology, Icons.Outlined.Psychology, ScreenDestination.AiDoubtSolver),
    PEER_GROUPS("Peers", Icons.Filled.Groups, Icons.Outlined.Groups, ScreenDestination.PeerStudyGroups),
    ANALYTICS("Stats", Icons.Filled.Analytics, Icons.Outlined.Analytics, ScreenDestination.AnalyticsDashboard),
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

    FrostedBackground(isDarkTheme = isDarkTheme) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            topBar = {
                if (currentScreen is ScreenDestination.Home) {
                    TopAppBar(
                        title = {
                            Row(
                                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(GlassIndigoBlueGradient),
                                    contentAlignment = androidx.compose.ui.Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Default.School,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                                Column {
                                    Text(
                                        text = "StudyFlow AI",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 17.sp,
                                        color = if (isDarkTheme) Color.White else Slate900
                                    )
                                    Text(
                                        text = "CBSE CLASS 12 SCIENCE",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.sp,
                                        color = if (isDarkTheme) Slate400 else Slate600
                                    )
                                }
                            }
                        },
                        actions = {
                            IconButton(
                                onClick = onSendReminder,
                                modifier = Modifier
                                    .testTag("notification_reminder_btn")
                                    .padding(end = 4.dp)
                                    .clip(CircleShape)
                                    .background(if (isDarkTheme) Color(0x331E293B) else Color(0x1A0F172A))
                                    .border(
                                        BorderStroke(1.dp, if (isDarkTheme) Color(0x26FFFFFF) else Color(0x1A000000)),
                                        CircleShape
                                    )
                            ) {
                                Icon(
                                    Icons.Default.NotificationsActive,
                                    contentDescription = "Test Notification Reminder",
                                    tint = NeonBlue,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            IconButton(
                                onClick = onToggleDarkTheme,
                                modifier = Modifier
                                    .testTag("dark_mode_toggle_btn")
                                    .clip(CircleShape)
                                    .background(if (isDarkTheme) Color(0x331E293B) else Color(0x1A0F172A))
                                    .border(
                                        BorderStroke(1.dp, if (isDarkTheme) Color(0x26FFFFFF) else Color(0x1A000000)),
                                        CircleShape
                                    )
                            ) {
                                Icon(
                                    if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                                    contentDescription = "Toggle Dark Mode",
                                    tint = if (isDarkTheme) NeonAmber else Slate700,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = if (isDarkTheme) Slate950.copy(alpha = 0.65f) else LightSurface.copy(alpha = 0.75f)
                        )
                    )
                }
            },
            bottomBar = {
                if (isTopLevel) {
                    NavigationBar(
                        containerColor = if (isDarkTheme) Slate950.copy(alpha = 0.85f) else LightSurface.copy(alpha = 0.9f),
                        tonalElevation = 0.dp,
                        modifier = Modifier.border(
                            BorderStroke(1.dp, if (isDarkTheme) Color(0x1AFFFFFF) else Color(0x1A000000)),
                            RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                        ).clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
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
                                        text = tab.title.uppercase(),
                                        fontSize = 9.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        letterSpacing = 0.5.sp
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = NeonBlue,
                                    selectedTextColor = NeonBlue,
                                    indicatorColor = if (isDarkTheme) Color(0x3338BDF8) else Color(0x2638BDF8),
                                    unselectedIconColor = if (isDarkTheme) Slate400 else Slate600,
                                    unselectedTextColor = if (isDarkTheme) Slate400 else Slate600
                                ),
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
}

