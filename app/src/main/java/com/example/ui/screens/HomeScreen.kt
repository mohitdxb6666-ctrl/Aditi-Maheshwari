package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
fun HomeScreen(viewModel: MainViewModel) {
    val allChapters by viewModel.allChapters.collectAsState()
    val allFlashcards by viewModel.allFlashcards.collectAsState()
    val allTests by viewModel.allTestResults.collectAsState()

    val totalChapters = allChapters.size
    val completedChapters = allChapters.count { it.masteryPercentage >= 75 }
    val weakChapters = allChapters.filter { it.masteryPercentage < 65 }
    val masteredCards = allFlashcards.count { it.isMastered }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("home_screen_content"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // Hero Banner: CBSE 12 Countdown & Daily Streak
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("hero_banner_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    Navy900,
                                    Navy800,
                                    PrimaryBlueDark
                                )
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                color = Color(0x3338BDF8),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        Icons.Default.School,
                                        contentDescription = "CBSE Class 12",
                                        tint = AccentCyan,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = "CBSE CLASS 12 BOARD 2026",
                                        color = AccentCyan,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Surface(
                                color = Color(0x33F59E0B),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        Icons.Default.LocalFireDepartment,
                                        contentDescription = "Streak",
                                        tint = AccentAmber,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Text(
                                        text = "7 Day Streak",
                                        color = AccentAmber,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        Text(
                            text = "Target: 95%+ in Science & Maths",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold
                        )

                        Text(
                            text = "Comprehensive NCERT notes, formulas, step-by-step AI thinking doubt solving, and full CBSE mock tests.",
                            color = Color(0xFFCBD5E1),
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = { viewModel.navigateTo(ScreenDestination.MockTestHub) },
                                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp)
                                    .testTag("start_mock_btn")
                            ) {
                                Icon(Icons.Default.Quiz, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Mock Test", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                            }

                            OutlinedButton(
                                onClick = { viewModel.navigateTo(ScreenDestination.AiDoubtSolver) },
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                                border = ButtonDefaults.outlinedButtonBorder.copy(brush = Brush.horizontalGradient(listOf(AccentCyan, PrimaryBlue))),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp)
                                    .testTag("ai_doubt_btn")
                            ) {
                                Icon(Icons.Default.Psychology, contentDescription = null, tint = AccentCyan, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("AI Doubt Bot", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
            }
        }

        // Quick Stats Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                QuickStatCard(
                    modifier = Modifier.weight(1f),
                    title = "Chapters",
                    value = "$completedChapters/$totalChapters",
                    subtitle = "Mastered",
                    icon = Icons.Default.MenuBook,
                    iconTint = PrimaryBlue
                )
                QuickStatCard(
                    modifier = Modifier.weight(1f),
                    title = "Flashcards",
                    value = "$masteredCards/${allFlashcards.size}",
                    subtitle = "Memorized",
                    icon = Icons.Default.Style,
                    iconTint = AccentEmerald
                )
                QuickStatCard(
                    modifier = Modifier.weight(1f),
                    title = "Mock Tests",
                    value = "${allTests.size}",
                    subtitle = "Completed",
                    icon = Icons.Default.Analytics,
                    iconTint = AccentAmber
                )
            }
        }

        // Areas Needing Improvement Banner (if weak chapters exist)
        if (weakChapters.isNotEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("weak_areas_alert_card"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (MaterialTheme.colorScheme.surface == DarkSurface) Color(0xFF2C1318) else Color(0xFFFEF2F2)
                    ),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.horizontalGradient(listOf(AccentRose, AccentAmber))
                    )
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
                                tint = AccentRose,
                                modifier = Modifier.size(22.dp)
                            )
                            Text(
                                text = "Areas Needing Improvement",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Text(
                            text = "Diagnostic analysis indicates lower proficiency (<65%) in ${weakChapters.size} high-weightage topics:",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        weakChapters.take(2).forEach { ch ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.6f))
                                    .clickable { viewModel.navigateTo(ScreenDestination.ChapterStudy(ch.id)) }
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "[${ch.subjectCode}] ${ch.title}",
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 13.sp
                                    )
                                    Text(
                                        text = "Proficiency: ${ch.masteryPercentage}% • CBSE Weightage: ${ch.cbseWeightageMarks}M",
                                        fontSize = 11.sp,
                                        color = AccentRose
                                    )
                                }
                                TextButton(
                                    onClick = { viewModel.navigateTo(ScreenDestination.ChapterStudy(ch.id)) }
                                ) {
                                    Text("Revise Now", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Subject Exploration Section
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Core Subjects (Class 12)",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "100% Offline Ready",
                    fontSize = 12.sp,
                    color = AccentEmerald,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        items(SubjectType.values().toList()) { subject ->
            val subjectChapters = allChapters.filter { it.subjectCode == subject.code }
            val subjectMastery = if (subjectChapters.isNotEmpty()) {
                subjectChapters.map { it.masteryPercentage }.average().toInt()
            } else 0

            SubjectCard(
                subject = subject,
                chaptersCount = subjectChapters.size,
                mastery = subjectMastery,
                onClick = { viewModel.navigateTo(ScreenDestination.SubjectDetail(subject.code)) }
            )
        }

        // Quick Revision Timer Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.navigateTo(ScreenDestination.RevisionTimer) }
                    .testTag("revision_timer_shortcut"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = PrimaryBlue.copy(alpha = 0.15f),
                            modifier = Modifier.size(44.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Default.Timer,
                                    contentDescription = "Study Timer",
                                    tint = PrimaryBlue
                                )
                            }
                        }
                        Column {
                            Text(
                                text = "CBSE Revision Timer",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                            Text(
                                text = "25m Pomodoro & 3-Hour Board Mock Mode",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Go to timer",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
fun QuickStatCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    subtitle: String,
    icon: ImageVector,
    iconTint: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                icon,
                contentDescription = title,
                tint = iconTint,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = value,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 16.sp
            )
            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun SubjectCard(
    subject: SubjectType,
    chaptersCount: Int,
    mastery: Int,
    onClick: () -> Unit
) {
    val subjectColor = Color(subject.colorHex)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("subject_card_${subject.code.lowercase()}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = subjectColor.copy(alpha = 0.15f),
                    modifier = Modifier.size(50.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = subject.code,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 14.sp,
                            color = subjectColor
                        )
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = subject.displayName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "$chaptersCount Key Chapters • NCERT + PYQs",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = { mastery / 100f },
                        modifier = Modifier
                            .width(130.dp)
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = subjectColor,
                        trackColor = subjectColor.copy(alpha = 0.2f)
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = subjectColor.copy(alpha = 0.1f)
                ) {
                    Text(
                        text = "$mastery%",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = subjectColor
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Icon(
                    Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "View Subject",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
