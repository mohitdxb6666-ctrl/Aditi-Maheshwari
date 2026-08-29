package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SubjectType
import com.example.ui.components.FrostedGlassCard
import com.example.ui.components.FrostedPillBadge
import com.example.ui.components.FrostedProgressBar
import com.example.ui.components.FrostedQuickActionTile
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.ScreenDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: MainViewModel) {
    val context = LocalContext.current
    val isDark = isSystemInDarkTheme()

    val allChapters by viewModel.allChapters.collectAsState()
    val allFlashcards by viewModel.allFlashcards.collectAsState()
    val allTests by viewModel.allTestResults.collectAsState()
    val timerSecondsLeft by viewModel.timerSecondsLeft.collectAsState()
    val isTimerRunning by viewModel.isTimerRunning.collectAsState()

    val totalChapters = allChapters.size
    val completedChapters = allChapters.count { it.masteryPercentage >= 75 }
    val weakChapters = allChapters.filter { it.masteryPercentage < 65 }
    val masteredCards = allFlashcards.count { it.isMastered }

    val overallReadiness = if (allChapters.isNotEmpty()) {
        allChapters.map { it.masteryPercentage }.average()
    } else 78.4

    val mathsChapters = allChapters.filter { it.subjectCode == "MATH" }
    val physicsChapters = allChapters.filter { it.subjectCode == "PHY" }
    val mathsMastery = if (mathsChapters.isNotEmpty()) mathsChapters.map { it.masteryPercentage }.average().toFloat() / 100f else 0.85f
    val physicsMastery = if (physicsChapters.isNotEmpty()) physicsChapters.map { it.masteryPercentage }.average().toFloat() / 100f else 0.62f

    val formattedTimer = remember(timerSecondsLeft) {
        val mins = (timerSecondsLeft % 3600) / 60
        val secs = timerSecondsLeft % 60
        String.format("%02d:%02d", mins, secs)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("home_screen_content"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Section: Frosted Glass Overall Readiness Card (From Design HTML)
        item {
            FrostedGlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("hero_banner_card"),
                shape = RoundedCornerShape(32.dp),
                backgroundColor = if (isDark) Color(0x1AFFFFFF) else Color(0xD9FFFFFF),
                borderColor = if (isDark) Color(0x33FFFFFF) else Color(0x1F0F172A)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Top readiness row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column {
                            Text(
                                text = "Overall Readiness",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = if (isDark) NeonBlue.copy(alpha = 0.9f) else Slate600
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = String.format("%.1f%%", overallReadiness),
                                fontSize = 32.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = if (isDark) Color.White else Slate900
                            )
                        }

                        // Frosted Pill
                        FrostedPillBadge(
                            text = "+5.2% VS LAST WEEK",
                            accentColor = NeonEmerald,
                            containerColor = if (isDark) Color(0x2634D399) else Color(0x1A10B981),
                            borderColor = if (isDark) Color(0x4D34D399) else Color(0x3310B981)
                        )
                    }

                    // Progress breakdown
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            FrostedProgressBar(
                                progress = mathsMastery,
                                modifier = Modifier.weight(1f),
                                barColor = NeonBlue
                            )
                            Text(
                                text = "MATHS",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDark) Slate400 else Slate600,
                                letterSpacing = 1.sp
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            FrostedProgressBar(
                                progress = physicsMastery,
                                modifier = Modifier.weight(1f),
                                barColor = NeonIndigo
                            )
                            Text(
                                text = "PHYSICS",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDark) Slate400 else Slate600,
                                letterSpacing = 1.sp
                            )
                        }
                    }

                    // Bottom alert & export row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp)
                            .border(
                                BorderStroke(1.dp, if (isDark) Color(0x1AFFFFFF) else Color(0x1A000000)),
                                RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                Icons.Default.WarningAmber,
                                contentDescription = null,
                                tint = NeonAmber,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Review: ",
                                fontSize = 11.sp,
                                color = if (isDark) Slate300 else Slate600
                            )
                            Text(
                                text = if (weakChapters.isNotEmpty()) weakChapters.first().title else "Organic Chemistry",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDark) Color.White else Slate900,
                                maxLines = 1
                            )
                        }

                        TextButton(
                            onClick = {
                                val file = viewModel.exportAndSharePdfReport()
                                if (file != null) {
                                    Toast.makeText(context, "Performance Report Exported!", Toast.LENGTH_SHORT).show()
                                }
                            },
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                            modifier = Modifier.testTag("export_report_action_btn")
                        ) {
                            Text(
                                text = "EXPORT REPORT",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = NeonBlue,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }
                }
            }
        }

        // 2-Column Frosted Grid: Mock Tests & Flashcards (From Design HTML)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                FrostedQuickActionTile(
                    modifier = Modifier
                        .weight(1f)
                        .testTag("start_mock_btn"),
                    title = "Mock Tests",
                    subtitle = "${allTests.size + 3} Available",
                    icon = Icons.Default.Quiz,
                    iconColor = NeonBlue,
                    iconBgColor = NeonBlue.copy(alpha = 0.15f),
                    onClick = { viewModel.navigateTo(ScreenDestination.MockTestHub) }
                )

                FrostedQuickActionTile(
                    modifier = Modifier
                        .weight(1f)
                        .testTag("flashcards_quick_btn"),
                    title = "Flashcards",
                    subtitle = "$masteredCards Mastered",
                    icon = Icons.Default.Style,
                    iconColor = NeonPurple,
                    iconBgColor = NeonPurple.copy(alpha = 0.15f),
                    onClick = { viewModel.navigateTo(ScreenDestination.ChapterStudy("math_1")) }
                )
            }
        }

        // Frosted Active Study Session Banner (From Design HTML)
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(GlassActiveSessionGradient)
                    .border(
                        BorderStroke(1.dp, Color(0x33FFFFFF)),
                        RoundedCornerShape(24.dp)
                    )
                    .clickable { viewModel.navigateTo(ScreenDestination.RevisionTimer) }
                    .padding(16.dp)
                    .testTag("active_session_card")
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Circular glowing timer icon
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .border(BorderStroke(2.dp, NeonIndigo.copy(alpha = 0.6f)), CircleShape)
                                .background(Color(0x33312E81)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Timer,
                                contentDescription = "Active Timer",
                                tint = NeonBlue,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(
                                text = "ACTIVE SESSION",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = NeonBlue,
                                letterSpacing = 1.2.sp
                            )
                            Text(
                                text = if (isTimerRunning) formattedTimer else "25:00",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    // Frosted circular toggle button
                    IconButton(
                        onClick = { viewModel.toggleTimer() },
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .testTag("timer_quick_toggle_btn")
                    ) {
                        Icon(
                            if (isTimerRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = "Toggle Timer",
                            tint = Slate900,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }
        }

        // Frosted AI Doubt Solver Capsule (From Design HTML)
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(50.dp))
                    .background(if (isDark) Color(0x14FFFFFF) else Color(0xD9FFFFFF))
                    .border(
                        BorderStroke(1.dp, if (isDark) Color(0x26FFFFFF) else Color(0x1F0F172A)),
                        RoundedCornerShape(50.dp)
                    )
                    .clickable { viewModel.navigateTo(ScreenDestination.AiDoubtSolver) }
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .testTag("ai_doubt_btn")
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(GlassPinkOrangeGradient),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Psychology,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "AI DOUBT SOLVER",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (isDark) Slate400 else Slate600,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "How can I help you today?",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (isDark) Color.White else Slate900
                        )
                    }

                    Icon(
                        Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Ask Doubt",
                        tint = if (isDark) Slate400 else Slate600,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        // Weak Areas Alert (if any weak topics)
        if (weakChapters.isNotEmpty()) {
            item {
                FrostedGlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("weak_areas_alert_card"),
                    shape = RoundedCornerShape(24.dp),
                    backgroundColor = if (isDark) Color(0x26F43F5E) else Color(0x1AF43F5E),
                    borderColor = NeonRose.copy(alpha = 0.4f)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                Icons.Default.WarningAmber,
                                contentDescription = "Weak Areas Alert",
                                tint = NeonRose,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "Areas Needing Improvement",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = if (isDark) Color.White else Slate900
                            )
                        }

                        Text(
                            text = "Diagnostic analysis indicates lower proficiency (<65%) in high-weightage topics:",
                            fontSize = 11.sp,
                            color = if (isDark) Slate300 else Slate600
                        )

                        weakChapters.take(2).forEach { ch ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isDark) Color(0x26000000) else Color(0x66FFFFFF))
                                    .clickable { viewModel.navigateTo(ScreenDestination.ChapterStudy(ch.id)) }
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "[${ch.subjectCode}] ${ch.title}",
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 12.sp,
                                        color = if (isDark) Color.White else Slate900
                                    )
                                    Text(
                                        text = "Proficiency: ${ch.masteryPercentage}% • Weightage: ${ch.cbseWeightageMarks}M",
                                        fontSize = 10.sp,
                                        color = NeonRose
                                    )
                                }
                                TextButton(
                                    onClick = { viewModel.navigateTo(ScreenDestination.ChapterStudy(ch.id)) }
                                ) {
                                    Text("Revise", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = NeonBlue)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Core Curriculum Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Core Curriculum",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (isDark) Color.White else Slate900
                )
                FrostedPillBadge(
                    text = "100% OFFLINE",
                    accentColor = NeonEmerald
                )
            }
        }

        // Subject Cards
        items(SubjectType.values().toList()) { subject ->
            val subjectChapters = allChapters.filter { it.subjectCode == subject.code }
            val subjectMastery = if (subjectChapters.isNotEmpty()) {
                subjectChapters.map { it.masteryPercentage }.average().toInt()
            } else 0

            FrostedSubjectCard(
                subject = subject,
                chaptersCount = subjectChapters.size,
                mastery = subjectMastery,
                onClick = { viewModel.navigateTo(ScreenDestination.SubjectDetail(subject.code)) }
            )
        }
    }
}

@Composable
fun FrostedSubjectCard(
    subject: SubjectType,
    chaptersCount: Int,
    mastery: Int,
    onClick: () -> Unit
) {
    val isDark = isSystemInDarkTheme()
    val subjectColor = Color(subject.colorHex)

    FrostedGlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("subject_card_${subject.code.lowercase()}"),
        shape = RoundedCornerShape(20.dp),
        backgroundColor = if (isDark) Color(0x14FFFFFF) else Color(0xD9FFFFFF),
        borderColor = if (isDark) Color(0x26FFFFFF) else Color(0x1F0F172A),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(subjectColor.copy(alpha = 0.2f))
                        .border(BorderStroke(1.dp, subjectColor.copy(alpha = 0.4f)), RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = subject.code,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 13.sp,
                        color = subjectColor
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                    Text(
                        text = subject.displayName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = if (isDark) Color.White else Slate900
                    )
                    Text(
                        text = "$chaptersCount Key Chapters • NCERT & PYQs",
                        fontSize = 11.sp,
                        color = if (isDark) Slate400 else Slate600
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    FrostedProgressBar(
                        progress = mastery / 100f,
                        modifier = Modifier.width(120.dp),
                        barColor = subjectColor
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(subjectColor.copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "$mastery%",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = subjectColor
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Icon(
                    Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "View Subject",
                    tint = if (isDark) Slate400 else Slate600,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

