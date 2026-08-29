package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
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
fun MockTestHubScreen(viewModel: MainViewModel) {
    val allTests by viewModel.allTestResults.collectAsState()
    val isDark = isSystemInDarkTheme()

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "CBSE Exam Mock Tests",
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = if (isDark) Color.White else Slate900
                        )
                        Text(
                            "Simulate Class 12 Board Pattern with Timers",
                            fontSize = 11.sp,
                            color = if (isDark) Slate400 else Slate600
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.navigateTo(ScreenDestination.Home) },
                        modifier = Modifier.testTag("mock_hub_back_btn")
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .testTag("mock_test_hub_list"),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header Banner in Frosted Glass
            item {
                FrostedGlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    backgroundColor = if (isDark) Color(0x1AFFFFFF) else Color(0xD9FFFFFF),
                    borderColor = if (isDark) Color(0x33FFFFFF) else Color(0x1F0F172A)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    Icons.Default.Timer,
                                    contentDescription = null,
                                    tint = NeonBlue,
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = "CBSE 2026 Board Pattern",
                                    color = NeonBlue,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            FrostedPillBadge(text = "OFFLINE READY", accentColor = NeonEmerald)
                        }

                        Text(
                            text = "Practice with Real Exam Conditions",
                            color = if (isDark) Color.White else Slate900,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = "Includes Section A (MCQs & Assertion-Reason), negative mark prevention, and instant answer evaluations.",
                            color = if (isDark) Slate300 else Slate600,
                            fontSize = 12.sp,
                            lineHeight = 17.sp
                        )
                    }
                }
            }

            item {
                Text(
                    text = "Select Mock Test",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDark) Color.White else Slate900
                )
            }

            // Subject Mock Test Options
            itemsIndexed(SubjectType.values().toList()) { _, subject ->
                val subjectColor = Color(subject.colorHex)

                FrostedGlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("mock_card_${subject.code.lowercase()}"),
                    shape = RoundedCornerShape(20.dp),
                    backgroundColor = if (isDark) Color(0x14FFFFFF) else Color(0xD9FFFFFF),
                    borderColor = if (isDark) Color(0x26FFFFFF) else Color(0x1F0F172A)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(42.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(subjectColor.copy(alpha = 0.2f))
                                        .border(BorderStroke(1.dp, subjectColor.copy(alpha = 0.4f)), RoundedCornerShape(12.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        subject.code,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = subjectColor,
                                        fontSize = 12.sp
                                    )
                                }
                                Column {
                                    Text(
                                        text = "${subject.displayName} Board Mock",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = if (isDark) Color.White else Slate900
                                    )
                                    Text(
                                        text = "Full Syllabus Chapter Mix",
                                        fontSize = 11.sp,
                                        color = if (isDark) Slate400 else Slate600
                                    )
                                }
                            }

                            FrostedPillBadge(text = "30 MINS", accentColor = NeonAmber)
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    viewModel.startMockTest(subject.code, 30, "${subject.displayName} 30-Min Drill")
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = subjectColor)
                            ) {
                                Text("30-Min Drill", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }

                            OutlinedButton(
                                onClick = {
                                    viewModel.startMockTest(subject.code, 60, "${subject.displayName} 1-Hour Full Mock")
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, if (isDark) Color(0x33FFFFFF) else Color(0x330F172A))
                            ) {
                                Text(
                                    "60-Min Mock",
                                    fontSize = 12.sp,
                                    color = if (isDark) Color.White else Slate900
                                )
                            }
                        }
                    }
                }
            }

            // Past Mock History
            item {
                Text(
                    text = "Recent Mock Test History",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDark) Color.White else Slate900
                )
            }

            if (allTests.isEmpty()) {
                item {
                    FrostedGlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = "No mock tests recorded yet. Take your first test above!",
                            color = if (isDark) Slate400 else Slate600,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            } else {
                itemsIndexed(allTests) { _, test ->
                    FrostedGlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        backgroundColor = if (isDark) Color(0x14FFFFFF) else Color(0xD9FFFFFF),
                        borderColor = if (isDark) Color(0x26FFFFFF) else Color(0x1F0F172A)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Text(
                                    test.testTitle,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = if (isDark) Color.White else Slate900
                                )
                                Text(
                                    text = "Score: ${test.scoreMarks}/${test.maxMarks} • Correct: ${test.correctAnswers}/${test.totalQuestions}",
                                    fontSize = 11.sp,
                                    color = if (isDark) Slate400 else Slate600
                                )
                            }
                            FrostedPillBadge(
                                text = "${test.accuracyPercentage}%",
                                accentColor = if (test.accuracyPercentage >= 75) NeonEmerald else NeonAmber
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActiveMockTestScreen(
    subjectCode: String,
    durationMinutes: Int,
    testTitle: String,
    viewModel: MainViewModel
) {
    val questions by viewModel.mockQuestions.collectAsState()
    val answers by viewModel.mockAnswers.collectAsState()
    val secondsLeft by viewModel.mockTimeRemainingSeconds.collectAsState()
    val isDark = isSystemInDarkTheme()

    var currentQIndex by remember { mutableIntStateOf(0) }
    var showSubmitConfirmDialog by remember { mutableStateOf(false) }
    var showResultsDialog by remember { mutableStateOf(false) }

    val formattedTime = remember(secondsLeft) {
        val mins = secondsLeft / 60
        val secs = secondsLeft % 60
        String.format("%02d:%02d", mins, secs)
    }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            testTitle,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            maxLines = 1,
                            color = if (isDark) Color.White else Slate900
                        )
                        Text(
                            text = "Time Remaining: $formattedTime",
                            fontSize = 11.sp,
                            color = if (secondsLeft < 300) NeonRose else NeonEmerald,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                actions = {
                    Button(
                        onClick = { showSubmitConfirmDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = NeonEmerald),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                        modifier = Modifier.testTag("submit_mock_test_btn")
                    ) {
                        Text("Submit", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { padding ->
        if (questions.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = NeonBlue)
            }
            return@Scaffold
        }

        val question = questions[currentQIndex.coerceIn(0, questions.size - 1)]

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Question Selector Strip
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(questions.size) { idx ->
                    val isCurrent = currentQIndex == idx
                    val isAnswered = answers.containsKey(idx)

                    val bg = when {
                        isCurrent -> NeonBlue
                        isAnswered -> NeonEmerald
                        else -> if (isDark) Color(0x26FFFFFF) else Color(0x1F0F172A)
                    }

                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(bg)
                            .border(
                                BorderStroke(1.dp, if (isCurrent) Color.White else Color.Transparent),
                                CircleShape
                            )
                            .clickable { currentQIndex = idx },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${idx + 1}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = if (isCurrent || isAnswered) Color.White else (if (isDark) Slate300 else Slate600)
                        )
                    }
                }
            }

            // Question Card in Frosted Glass
            FrostedGlassCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                backgroundColor = if (isDark) Color(0x1AFFFFFF) else Color(0xD9FFFFFF),
                borderColor = if (isDark) Color(0x33FFFFFF) else Color(0x1F0F172A)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Question ${currentQIndex + 1} of ${questions.size}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = NeonBlue
                        )
                        FrostedPillBadge(text = "+4 MARKS", accentColor = NeonEmerald)
                    }

                    Text(
                        text = question.questionText,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        lineHeight = 22.sp,
                        color = if (isDark) Color.White else Slate900
                    )
                }
            }

            // Options
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                itemsIndexed(question.options) { optIdx, optionText ->
                    val isSelected = answers[currentQIndex] == optIdx

                    FrostedGlassCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("mock_option_$optIdx"),
                        shape = RoundedCornerShape(16.dp),
                        backgroundColor = if (isSelected) {
                            if (isDark) NeonBlue.copy(alpha = 0.25f) else NeonBlue.copy(alpha = 0.15f)
                        } else {
                            if (isDark) Color(0x14FFFFFF) else Color(0xD9FFFFFF)
                        },
                        borderColor = if (isSelected) NeonBlue else (if (isDark) Color(0x26FFFFFF) else Color(0x1F0F172A)),
                        onClick = { viewModel.selectMockAnswer(currentQIndex, optIdx) }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(30.dp)
                                    .clip(CircleShape)
                                    .background(if (isSelected) NeonBlue else (if (isDark) Color(0x26FFFFFF) else Color(0x1A0F172A))),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = ('A' + optIdx).toString(),
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color.White else (if (isDark) Slate300 else Slate600),
                                    fontSize = 12.sp
                                )
                            }
                            Text(
                                text = optionText,
                                fontSize = 13.sp,
                                color = if (isDark) Color.White else Slate900,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            // Navigation Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { if (currentQIndex > 0) currentQIndex-- },
                    enabled = currentQIndex > 0,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = if (isDark) Slate700 else Slate300)
                ) {
                    Text("Previous", color = if (isDark) Color.White else Slate900)
                }

                Button(
                    onClick = {
                        if (currentQIndex < questions.size - 1) {
                            currentQIndex++
                        } else {
                            showSubmitConfirmDialog = true
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NeonBlue)
                ) {
                    Text(
                        if (currentQIndex < questions.size - 1) "Next" else "Review & Submit",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }

    if (showSubmitConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showSubmitConfirmDialog = false },
            title = { Text("Submit Mock Test?", fontWeight = FontWeight.Bold) },
            text = {
                Text("You have answered ${answers.size} out of ${questions.size} questions. Do you want to finalize and calculate your score?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSubmitConfirmDialog = false
                        viewModel.submitMockTest(testTitle, subjectCode, durationMinutes)
                        showResultsDialog = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NeonEmerald),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Yes, Submit", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showSubmitConfirmDialog = false }) {
                    Text("Continue Test")
                }
            }
        )
    }

    if (showResultsDialog) {
        var correctCount = 0
        questions.forEachIndexed { index, q ->
            if (answers[index] == q.correctOptionIndex) correctCount++
        }
        val score = correctCount * 4
        val maxMarks = questions.size * 4
        val accuracy = ((correctCount.toFloat() / questions.size) * 100).toInt()

        AlertDialog(
            onDismissRequest = {},
            title = { Text("🎉 Mock Test Completed!", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Score: $score / $maxMarks Marks ($accuracy%)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = NeonBlue
                    )
                    Text("Correct Answers: $correctCount out of ${questions.size}")
                    Text(
                        text = if (accuracy >= 80) "Outstanding! You are on track for a 95+ in CBSE Class 12 Boards."
                        else "Good attempt! Review detailed solutions and revise weak topics in the Analytics dashboard."
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showResultsDialog = false
                        viewModel.navigateTo(ScreenDestination.MockTestHub)
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NeonBlue)
                ) {
                    Text("Back to Hub", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showResultsDialog = false
                        viewModel.navigateTo(ScreenDestination.AnalyticsDashboard)
                    }
                ) {
                    Text("View Analytics")
                }
            }
        )
    }
}
