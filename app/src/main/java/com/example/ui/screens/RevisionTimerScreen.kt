package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SubjectType
import com.example.ui.components.FrostedGlassCard
import com.example.ui.components.FrostedPillBadge
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.ScreenDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RevisionTimerScreen(viewModel: MainViewModel) {
    val totalDurationSeconds by viewModel.timerDurationSeconds.collectAsState()
    val secondsLeft by viewModel.timerSecondsLeft.collectAsState()
    val isRunning by viewModel.isTimerRunning.collectAsState()
    val isDark = isSystemInDarkTheme()

    var selectedSubject by remember { mutableStateOf("ALL") }

    val formattedTime = remember(secondsLeft) {
        val hours = secondsLeft / 3600
        val mins = (secondsLeft % 3600) / 60
        val secs = secondsLeft % 60
        if (hours > 0) {
            String.format("%02d:%02d:%02d", hours, mins, secs)
        } else {
            String.format("%02d:%02d", mins, secs)
        }
    }

    val progress = remember(secondsLeft, totalDurationSeconds) {
        if (totalDurationSeconds > 0) {
            secondsLeft.toFloat() / totalDurationSeconds
        } else 0f
    }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "Revision Study Timer",
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = if (isDark) Color.White else Slate900
                        )
                        Text(
                            "Disciplined Practice & Exam Simulation",
                            fontSize = 11.sp,
                            color = if (isDark) Slate400 else Slate600
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.navigateTo(ScreenDestination.Home) },
                        modifier = Modifier.testTag("revision_timer_back_btn")
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = if (isDark) Color.White else Slate900
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp)
                .testTag("revision_timer_container"),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Preset Chips in Frosted Glass Container
            FrostedGlassCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                backgroundColor = if (isDark) Color(0x14FFFFFF) else Color(0xD9FFFFFF),
                borderColor = if (isDark) Color(0x26FFFFFF) else Color(0x1F0F172A)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        "Study Mode Preset",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = if (isDark) Color.White else Slate900
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        FilterChip(
                            selected = totalDurationSeconds == 25 * 60,
                            onClick = { viewModel.setTimerPreset(25) },
                            label = { Text("25m Pomodoro", fontSize = 11.sp) },
                            modifier = Modifier.padding(horizontal = 4.dp),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = NeonBlue.copy(alpha = 0.25f),
                                selectedLabelColor = NeonBlue
                            )
                        )
                        FilterChip(
                            selected = totalDurationSeconds == 45 * 60,
                            onClick = { viewModel.setTimerPreset(45) },
                            label = { Text("45m Block", fontSize = 11.sp) },
                            modifier = Modifier.padding(horizontal = 4.dp),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = NeonIndigo.copy(alpha = 0.25f),
                                selectedLabelColor = NeonIndigo
                            )
                        )
                        FilterChip(
                            selected = totalDurationSeconds == 180 * 60,
                            onClick = { viewModel.setTimerPreset(180) },
                            label = { Text("3-Hr Mock", fontSize = 11.sp) },
                            modifier = Modifier.padding(horizontal = 4.dp),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = NeonCyan.copy(alpha = 0.25f),
                                selectedLabelColor = NeonCyan
                            )
                        )
                    }
                }
            }

            // Subject Tag Selection
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    "Tag Subject for Session Logging:",
                    fontSize = 11.sp,
                    color = if (isDark) Slate400 else Slate600
                )
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    item {
                        FilterChip(
                            selected = selectedSubject == "ALL",
                            onClick = { selectedSubject = "ALL" },
                            label = { Text("General", fontSize = 11.sp) }
                        )
                    }
                    items(SubjectType.values().toList()) { sub ->
                        FilterChip(
                            selected = selectedSubject == sub.code,
                            onClick = { selectedSubject = sub.code },
                            label = { Text(sub.displayName, fontSize = 11.sp) }
                        )
                    }
                }
            }

            // Circular Visual Timer in Frosted Glass Glow Container
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(260.dp)
                    .testTag("circular_timer_display")
            ) {
                // Frosted background circular backdrop
                Box(
                    modifier = Modifier
                        .size(240.dp)
                        .clip(CircleShape)
                        .background(if (isDark) Color(0x14FFFFFF) else Color(0xCCFFFFFF))
                        .border(BorderStroke(1.dp, if (isDark) Color(0x26FFFFFF) else Color(0x1F0F172A)), CircleShape)
                )

                CircularProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.size(230.dp),
                    strokeWidth = 12.dp,
                    color = if (isRunning) NeonBlue else NeonIndigo.copy(alpha = 0.6f),
                    trackColor = if (isDark) Color(0x1AFFFFFF) else Color(0x1F0F172A),
                    strokeCap = StrokeCap.Round
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = formattedTime,
                        fontSize = 38.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (isDark) Color.White else Slate900
                    )
                    FrostedPillBadge(
                        text = if (isRunning) "🔥 SESSION ACTIVE" else "READY TO FOCUS",
                        accentColor = if (isRunning) NeonEmerald else NeonAmber
                    )
                }
            }

            // Controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { viewModel.resetTimer() },
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(if (isDark) Color(0x1AFFFFFF) else Color(0xCCFFFFFF))
                        .border(BorderStroke(1.dp, if (isDark) Color(0x33FFFFFF) else Color(0x1F0F172A)), CircleShape)
                        .testTag("reset_timer_btn")
                ) {
                    Icon(
                        Icons.Default.Refresh,
                        contentDescription = "Reset",
                        modifier = Modifier.size(22.dp),
                        tint = if (isDark) Color.White else Slate900
                    )
                }

                Spacer(modifier = Modifier.width(20.dp))

                Button(
                    onClick = { viewModel.startPauseTimer(selectedSubject) },
                    modifier = Modifier
                        .height(54.dp)
                        .width(160.dp)
                        .testTag("start_pause_timer_btn"),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isRunning) NeonAmber else NeonBlue
                    )
                ) {
                    Icon(
                        if (isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isRunning) "Pause" else "Start",
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isRunning) "Pause" else "Start Focus",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            // Tip Banner in Frosted Glass
            FrostedGlassCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                backgroundColor = if (isDark) Color(0x14FFFFFF) else Color(0xD9FFFFFF),
                borderColor = if (isDark) Color(0x26FFFFFF) else Color(0x1F0F172A)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        Icons.Default.Psychology,
                        contentDescription = null,
                        tint = NeonBlue,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "CBSE Class 12 Topper Tip: Do 45 mins uninterrupted derivation writing followed by 5 mins flashcard retrieval.",
                        fontSize = 11.sp,
                        lineHeight = 15.sp,
                        color = if (isDark) Slate300 else Slate700
                    )
                }
            }
        }
    }
}

