package com.example.ui.screens

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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SubjectType
import com.example.ui.components.FrostedGlassCard
import com.example.ui.components.FrostedPillBadge
import com.example.ui.components.FrostedProgressBar
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.ScreenDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectDetailScreen(
    subjectCode: String,
    viewModel: MainViewModel
) {
    val subject = SubjectType.values().firstOrNull { it.code == subjectCode } ?: SubjectType.MATHEMATICS
    val subjectColor = Color(subject.colorHex)
    val allChapters by viewModel.allChapters.collectAsState()
    val subjectChapters = allChapters.filter { it.subjectCode == subjectCode }
    val avgMastery = if (subjectChapters.isNotEmpty()) subjectChapters.map { it.masteryPercentage }.average().toInt() else 0
    val totalMarks = subjectChapters.sumOf { it.cbseWeightageMarks }
    val isDark = isSystemInDarkTheme()

    var showFormulaDialog by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            subject.displayName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = if (isDark) Color.White else Slate900
                        )
                        Text(
                            "CBSE Class 12 • 2026 Syllabus",
                            fontSize = 11.sp,
                            color = if (isDark) Slate400 else Slate600
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.navigateTo(ScreenDestination.Home) },
                        modifier = Modifier.testTag("back_button")
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = if (isDark) Color.White else Slate900
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { showFormulaDialog = true },
                        modifier = Modifier.testTag("formula_sheet_btn")
                    ) {
                        Icon(Icons.Default.Calculate, contentDescription = "Formula Sheet", tint = subjectColor)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .testTag("subject_detail_list"),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Subject Summary Header Card in Frosted Glass
            item {
                FrostedGlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    backgroundColor = if (isDark) subjectColor.copy(alpha = 0.15f) else Color(0xD9FFFFFF),
                    borderColor = subjectColor.copy(alpha = 0.4f)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "${subject.displayName} Mastery",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isDark) Color.White else Slate900
                                )
                                Text(
                                    text = "$totalMarks Marks Board Weightage in Curriculum",
                                    fontSize = 11.sp,
                                    color = if (isDark) Slate300 else Slate600
                                )
                            }
                            FrostedPillBadge(
                                text = "$avgMastery%",
                                accentColor = subjectColor
                            )
                        }

                        FrostedProgressBar(
                            progress = avgMastery / 100f,
                            fillBrush = Brush.horizontalGradient(listOf(subjectColor, subjectColor.copy(alpha = 0.6f))),
                            height = 8.dp
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            FrostedPillBadge(
                                text = "OFFLINE CACHED",
                                accentColor = NeonEmerald
                            )
                            FrostedPillBadge(
                                text = "${subjectChapters.size} CHAPTERS READY",
                                accentColor = subjectColor
                            )
                        }
                    }
                }
            }

            item {
                Text(
                    text = "All Chapters & Units",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDark) Color.White else Slate900
                )
            }

            items(subjectChapters) { chapter ->
                FrostedGlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.navigateTo(ScreenDestination.ChapterStudy(chapter.id)) }
                        .testTag("chapter_item_${chapter.id}"),
                    shape = RoundedCornerShape(20.dp),
                    backgroundColor = if (isDark) Color(0x14FFFFFF) else Color(0xD9FFFFFF),
                    borderColor = if (isDark) Color(0x26FFFFFF) else Color(0x1F0F172A)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(subjectColor.copy(alpha = 0.2f))
                                        .border(BorderStroke(1.dp, subjectColor.copy(alpha = 0.4f)), RoundedCornerShape(10.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "${chapter.chapterNumber}",
                                        fontWeight = FontWeight.Bold,
                                        color = subjectColor,
                                        fontSize = 13.sp
                                    )
                                }

                                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                    Text(
                                        text = chapter.title,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = if (isDark) Color.White else Slate900
                                    )
                                    Text(
                                        text = "CBSE Weightage: ${chapter.cbseWeightageMarks} Marks • ${chapter.totalQuestionsCount} Practice Problems",
                                        fontSize = 11.sp,
                                        color = if (isDark) Slate400 else Slate600
                                    )
                                }
                            }

                            FrostedPillBadge(
                                text = "${chapter.masteryPercentage}%",
                                accentColor = if (chapter.masteryPercentage >= 75) NeonEmerald else NeonAmber
                            )
                        }

                        FrostedProgressBar(
                            progress = chapter.completedQuestionsCount.toFloat() / chapter.totalQuestionsCount.coerceAtLeast(1),
                            fillBrush = Brush.horizontalGradient(listOf(subjectColor, subjectColor.copy(alpha = 0.6f))),
                            height = 6.dp
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Solved: ${chapter.completedQuestionsCount}/${chapter.totalQuestionsCount}",
                                fontSize = 11.sp,
                                color = if (isDark) Slate400 else Slate600
                            )

                            Button(
                                onClick = { viewModel.navigateTo(ScreenDestination.ChapterStudy(chapter.id)) },
                                colors = ButtonDefaults.buttonColors(containerColor = subjectColor),
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                            ) {
                                Text("Study & Practice", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }

    if (showFormulaDialog) {
        AlertDialog(
            onDismissRequest = { showFormulaDialog = false },
            title = {
                Text(
                    text = "${subject.displayName} Formula Sheet",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                LazyColumn(
                    modifier = Modifier.heightIn(max = 400.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(subjectChapters) { ch ->
                        FrostedGlassCard(
                            shape = RoundedCornerShape(12.dp),
                            backgroundColor = if (isDark) Color(0x1AFFFFFF) else Color(0xD9FFFFFF),
                            borderColor = if (isDark) Color(0x26FFFFFF) else Color(0x1F0F172A)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = ch.title,
                                    fontWeight = FontWeight.Bold,
                                    color = subjectColor,
                                    fontSize = 13.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = ch.keyFormulas,
                                    fontSize = 12.sp,
                                    lineHeight = 16.sp,
                                    color = if (isDark) Color.White else Slate900
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showFormulaDialog = false }) {
                    Text("Close")
                }
            }
        )
    }
}
