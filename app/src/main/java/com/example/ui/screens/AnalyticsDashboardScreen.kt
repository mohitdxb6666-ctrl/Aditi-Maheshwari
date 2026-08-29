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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
fun AnalyticsDashboardScreen(viewModel: MainViewModel) {
    val context = LocalContext.current
    val allChapters by viewModel.allChapters.collectAsState()
    val allTests by viewModel.allTestResults.collectAsState()
    val allSessions by viewModel.allStudySessions.collectAsState()
    val isDark = isSystemInDarkTheme()

    val totalStudyMinutes = allSessions.sumOf { it.durationMinutes }
    val avgMastery = if (allChapters.isNotEmpty()) allChapters.map { it.masteryPercentage }.average().toInt() else 0
    val weakChapters = allChapters.filter { it.masteryPercentage < 65 }
    val strongChapters = allChapters.filter { it.masteryPercentage >= 80 }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "Performance & Analytics",
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = if (isDark) Color.White else Slate900
                        )
                        Text(
                            "CBSE Class 12 Diagnostic Tracking",
                            fontSize = 11.sp,
                            color = if (isDark) Slate400 else Slate600
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.navigateTo(ScreenDestination.Home) },
                        modifier = Modifier.testTag("analytics_back_btn")
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
                        onClick = {
                            val file = viewModel.exportAndSharePdfReport()
                            if (file != null) {
                                Toast.makeText(context, "PDF Report Exported Successfully!", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.testTag("export_pdf_top_btn")
                    ) {
                        Icon(Icons.Default.PictureAsPdf, contentDescription = "Export PDF", tint = NeonRose)
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
                .testTag("analytics_dashboard_content"),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // High Level Score Card in Frosted Glass
            item {
                FrostedGlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    backgroundColor = if (isDark) Color(0x1AFFFFFF) else Color(0xD9FFFFFF),
                    borderColor = if (isDark) Color(0x33FFFFFF) else Color(0x1F0F172A)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Board Readiness Index",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = if (isDark) Color.White else Slate900
                            )
                            FrostedPillBadge(
                                text = if (avgMastery >= 75) "TARGET 95%+" else "REVISION REQUIRED",
                                accentColor = if (avgMastery >= 75) NeonEmerald else NeonAmber
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "$avgMastery%",
                                    fontSize = 36.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (avgMastery >= 75) NeonEmerald else NeonAmber
                                )
                                Text(
                                    text = if (avgMastery >= 75) "Predicted Score: 92-98% in Boards" else "Predicted Score: 80-88% in Boards",
                                    fontSize = 11.sp,
                                    color = if (isDark) Slate300 else Slate600
                                )
                            }

                            Button(
                                onClick = {
                                    val file = viewModel.exportAndSharePdfReport()
                                    if (file != null) {
                                        Toast.makeText(context, "PDF Report generated!", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = NeonRose),
                                modifier = Modifier.testTag("export_pdf_button")
                            ) {
                                Icon(Icons.Default.PictureAsPdf, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.White)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Export PDF", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }

                        FrostedProgressBar(
                            progress = avgMastery / 100f,
                            fillBrush = if (avgMastery >= 75) GlassCyanBlueGradient else GlassPinkOrangeGradient,
                            height = 8.dp
                        )
                    }
                }
            }

            // Diagnostic: Areas Needing Improvement
            item {
                FrostedGlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("diagnostic_weak_areas_card"),
                    shape = RoundedCornerShape(20.dp),
                    backgroundColor = if (isDark) Color(0x1F3B1A22) else Color(0xFFFFF1F2),
                    borderColor = NeonRose.copy(alpha = 0.5f)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.ReportProblem, contentDescription = null, tint = NeonRose, modifier = Modifier.size(20.dp))
                            Text(
                                text = "Areas Needing Improvement (Diagnostic)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = if (isDark) Color.White else Slate900
                            )
                        }

                        if (weakChapters.isEmpty()) {
                            Text(
                                text = "🎉 All chapters have reached above 65% mastery! Maintain revision with flashcards.",
                                fontSize = 12.sp,
                                color = NeonEmerald
                            )
                        } else {
                            Text(
                                text = "Target these high-weightage chapters to quickly boost your score:",
                                fontSize = 11.sp,
                                color = if (isDark) Slate300 else Slate600
                            )

                            weakChapters.forEach { ch ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(if (isDark) Color(0x26000000) else Color.White)
                                        .border(BorderStroke(1.dp, if (isDark) Color(0x26FFFFFF) else Color(0x1A0F172A)), RoundedCornerShape(12.dp))
                                        .clickable { viewModel.navigateTo(ScreenDestination.ChapterStudy(ch.id)) }
                                        .padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            "[${ch.subjectCode}] ${ch.title}",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp,
                                            color = if (isDark) Color.White else Slate900
                                        )
                                        Text(
                                            "Weightage: ${ch.cbseWeightageMarks} Marks • Mastery: ${ch.masteryPercentage}%",
                                            fontSize = 11.sp,
                                            color = NeonRose
                                        )
                                    }
                                    Button(
                                        onClick = { viewModel.navigateTo(ScreenDestination.ChapterStudy(ch.id)) },
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                        shape = RoundedCornerShape(8.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = NeonRose)
                                    ) {
                                        Text("Revise", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Subject Mastery Breakdown
            item {
                FrostedGlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    backgroundColor = if (isDark) Color(0x14FFFFFF) else Color(0xD9FFFFFF),
                    borderColor = if (isDark) Color(0x26FFFFFF) else Color(0x1F0F172A)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        Text(
                            "Subject-by-Subject Mastery",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = if (isDark) Color.White else Slate900
                        )

                        SubjectType.values().forEach { sub ->
                            val subChs = allChapters.filter { it.subjectCode == sub.code }
                            val subMastery = if (subChs.isNotEmpty()) subChs.map { it.masteryPercentage }.average().toInt() else 0
                            val subColor = Color(sub.colorHex)

                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        sub.displayName,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 12.sp,
                                        color = if (isDark) Color.White else Slate900
                                    )
                                    Text("$subMastery%", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = subColor)
                                }
                                FrostedProgressBar(
                                    progress = subMastery / 100f,
                                    fillBrush = Brush.horizontalGradient(listOf(subColor, subColor.copy(alpha = 0.7f))),
                                    height = 6.dp
                                )
                            }
                        }
                    }
                }
            }

            // Study Sessions Log Summary
            item {
                FrostedGlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    backgroundColor = if (isDark) Color(0x14FFFFFF) else Color(0xD9FFFFFF),
                    borderColor = if (isDark) Color(0x26FFFFFF) else Color(0x1F0F172A)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            "Study Habit & Focus Log",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = if (isDark) Color.White else Slate900
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Total Focus Time:", fontSize = 12.sp, color = if (isDark) Slate400 else Slate600)
                            Text(
                                "${totalStudyMinutes / 60}h ${totalStudyMinutes % 60}m",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = if (isDark) Color.White else Slate900
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Mock Tests Attempted:", fontSize = 12.sp, color = if (isDark) Slate400 else Slate600)
                            Text(
                                "${allTests.size} Tests",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = if (isDark) Color.White else Slate900
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Strong Chapters (80%+):", fontSize = 12.sp, color = if (isDark) Slate400 else Slate600)
                            Text("${strongChapters.size} Chapters", fontWeight = FontWeight.Bold, color = NeonEmerald, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}
