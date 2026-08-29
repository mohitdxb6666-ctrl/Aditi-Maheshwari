package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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

    val totalStudyMinutes = allSessions.sumOf { it.durationMinutes }
    val avgMastery = if (allChapters.isNotEmpty()) allChapters.map { it.masteryPercentage }.average().toInt() else 0
    val weakChapters = allChapters.filter { it.masteryPercentage < 65 }
    val strongChapters = allChapters.filter { it.masteryPercentage >= 80 }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Performance & Analytics", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("CBSE Class 12 Diagnostic Tracking", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.navigateTo(ScreenDestination.Home) },
                        modifier = Modifier.testTag("analytics_back_btn")
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
                        Icon(Icons.Default.PictureAsPdf, contentDescription = "Export PDF", tint = AccentRose)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .testTag("analytics_dashboard_content"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // High Level Score Cards
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        Text("Board Readiness Index", fontWeight = FontWeight.Bold, fontSize = 16.sp)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "$avgMastery%",
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (avgMastery >= 75) AccentEmerald else AccentAmber
                                )
                                Text(
                                    text = if (avgMastery >= 75) "Predicted Score: 92-98% in Boards" else "Predicted Score: 80-88% in Boards",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Button(
                                onClick = {
                                    val file = viewModel.exportAndSharePdfReport()
                                    if (file != null) {
                                        Toast.makeText(context, "PDF Report generated!", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = AccentRose),
                                modifier = Modifier.testTag("export_pdf_button")
                            ) {
                                Icon(Icons.Default.PictureAsPdf, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Export PDF", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        LinearProgressIndicator(
                            progress = { avgMastery / 100f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = if (avgMastery >= 75) AccentEmerald else AccentAmber,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    }
                }
            }

            // Diagnostic: Areas Needing Improvement
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("diagnostic_weak_areas_card"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (MaterialTheme.colorScheme.surface == DarkSurface) Color(0xFF2B1519) else Color(0xFFFFF1F2)
                    ),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.horizontalGradient(listOf(AccentRose, AccentRose.copy(alpha = 0.5f)))
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.ReportProblem, contentDescription = null, tint = AccentRose, modifier = Modifier.size(20.dp))
                            Text(
                                text = "Areas Needing Improvement (Diagnostic)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        if (weakChapters.isEmpty()) {
                            Text(
                                text = "🎉 All chapters have reached above 65% mastery! Maintain revision with flashcards.",
                                fontSize = 13.sp,
                                color = AccentEmerald
                            )
                        } else {
                            Text(
                                text = "Target these high-weightage chapters to quickly boost your score:",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            weakChapters.forEach { ch ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(MaterialTheme.colorScheme.surface)
                                        .clickable { viewModel.navigateTo(ScreenDestination.ChapterStudy(ch.id)) }
                                        .padding(10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text("[${ch.subjectCode}] ${ch.title}", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                        Text("Weightage: ${ch.cbseWeightageMarks} Marks • Mastery: ${ch.masteryPercentage}%", fontSize = 11.sp, color = AccentRose)
                                    }
                                    FilledTonalButton(
                                        onClick = { viewModel.navigateTo(ScreenDestination.ChapterStudy(ch.id)) },
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text("Revise", fontSize = 11.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Subject Mastery Breakdown
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        Text("Subject-by-Subject Mastery", fontWeight = FontWeight.Bold, fontSize = 15.sp)

                        SubjectType.values().forEach { sub ->
                            val subChs = allChapters.filter { it.subjectCode == sub.code }
                            val subMastery = if (subChs.isNotEmpty()) subChs.map { it.masteryPercentage }.average().toInt() else 0
                            val subColor = Color(sub.colorHex)

                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(sub.displayName, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                                    Text("$subMastery%", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = subColor)
                                }
                                LinearProgressIndicator(
                                    progress = { subMastery / 100f },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(6.dp)
                                        .clip(RoundedCornerShape(3.dp)),
                                    color = subColor,
                                    trackColor = subColor.copy(alpha = 0.15f)
                                )
                            }
                        }
                    }
                }
            }

            // Study Sessions Log Summary
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("Study Habit & Focus Log", fontWeight = FontWeight.Bold, fontSize = 15.sp)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Total Focus Time:", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("${totalStudyMinutes / 60}h ${totalStudyMinutes % 60}m", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Mock Tests Attempted:", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("${allTests.size} Tests", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Strong Chapters (80%+):", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("${strongChapters.size} Chapters", fontWeight = FontWeight.Bold, color = AccentEmerald, fontSize = 13.sp)
                        }
                    }
                }
            }
        }
    }
}
