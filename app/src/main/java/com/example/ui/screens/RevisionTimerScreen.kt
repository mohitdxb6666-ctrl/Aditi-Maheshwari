package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
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
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.ScreenDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RevisionTimerScreen(viewModel: MainViewModel) {
    val totalDurationSeconds by viewModel.timerDurationSeconds.collectAsState()
    val secondsLeft by viewModel.timerSecondsLeft.collectAsState()
    val isRunning by viewModel.isTimerRunning.collectAsState()

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
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Revision Study Timer", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("Disciplined Practice & Exam Simulation", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.navigateTo(ScreenDestination.Home) },
                        modifier = Modifier.testTag("revision_timer_back_btn")
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
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
            // Preset Chips
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text("Select Study Mode Preset", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    FilterChip(
                        selected = totalDurationSeconds == 25 * 60,
                        onClick = { viewModel.setTimerPreset(25) },
                        label = { Text("25m Pomodoro") },
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                    FilterChip(
                        selected = totalDurationSeconds == 45 * 60,
                        onClick = { viewModel.setTimerPreset(45) },
                        label = { Text("45m Deep Block") },
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                    FilterChip(
                        selected = totalDurationSeconds == 180 * 60,
                        onClick = { viewModel.setTimerPreset(180) },
                        label = { Text("3-Hour Board Mock") },
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }
            }

            // Subject Tag Selection
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text("Tag Subject for Session Logging:", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
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

            // Circular Visual Timer
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(260.dp)
                    .testTag("circular_timer_display")
            ) {
                CircularProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxSize(),
                    strokeWidth = 14.dp,
                    color = if (isRunning) PrimaryBlue else MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    strokeCap = StrokeCap.Round
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = formattedTime,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = if (isRunning) "🔥 Session Active" else "Ready to Focus",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isRunning) AccentEmerald else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedIconButton(
                    onClick = { viewModel.resetTimer() },
                    modifier = Modifier
                        .size(54.dp)
                        .testTag("reset_timer_btn")
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = "Reset", modifier = Modifier.size(24.dp))
                }

                Spacer(modifier = Modifier.width(20.dp))

                Button(
                    onClick = { viewModel.startPauseTimer(selectedSubject) },
                    modifier = Modifier
                        .height(58.dp)
                        .width(160.dp)
                        .testTag("start_pause_timer_btn"),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isRunning) AccentAmber else PrimaryBlue
                    )
                ) {
                    Icon(
                        if (isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isRunning) "Pause" else "Start"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isRunning) "Pause" else "Start Focus",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Tip Banner
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.Psychology, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(18.dp))
                    Text(
                        text = "CBSE Class 12 Topper Tip: Do 45 mins uninterrupted derivation writing followed by 5 mins flashcard retrieval.",
                        fontSize = 11.sp,
                        lineHeight = 15.sp
                    )
                }
            }
        }
    }
}
