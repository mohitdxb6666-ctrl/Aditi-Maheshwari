package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.ScreenDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MockTestHubScreen(viewModel: MainViewModel) {
    val allTests by viewModel.allTestResults.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("CBSE Exam Mock Tests", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("Simulate CBSE Class 12 Board Pattern with Timers", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.navigateTo(ScreenDestination.Home) },
                        modifier = Modifier.testTag("mock_hub_back_btn")
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
                .testTag("mock_test_hub_list"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Banner
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Brush.linearGradient(listOf(Navy900, Navy800)))
                            .padding(18.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(Icons.Default.Timer, contentDescription = null, tint = AccentCyan, modifier = Modifier.size(20.dp))
                                Text(
                                    text = "CBSE 2026 Board Exam Pattern",
                                    color = AccentCyan,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Text(
                                text = "Practice with Real Exam Conditions",
                                color = Color.White,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                text = "Includes Section A (MCQs & Assertion-Reason), negative mark prevention, and instant answer evaluations.",
                                color = Color(0xFFCBD5E1),
                                fontSize = 12.sp,
                                lineHeight = 17.sp
                            )
                        }
                    }
                }
            }

            item {
                Text(
                    text = "Select Mock Test",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Subject Mock Test Options
            itemsIndexed(SubjectType.values().toList()) { _, subject ->
                val subjectColor = Color(subject.colorHex)

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("mock_card_${subject.code.lowercase()}"),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = subjectColor.copy(alpha = 0.15f),
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(subject.code, fontWeight = FontWeight.Bold, color = subjectColor, fontSize = 12.sp)
                                    }
                                }
                                Column {
                                    Text(
                                        text = "${subject.displayName} Board Mock",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    )
                                    Text(
                                        text = "Full Syllabus Chapter Mix",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = AccentAmber.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = "30 Mins",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AccentAmber,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
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
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = subjectColor)
                            ) {
                                Text("Start 30-Min Drill", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }

                            OutlinedButton(
                                onClick = {
                                    viewModel.startMockTest(subject.code, 60, "${subject.displayName} 1-Hour Full Mock")
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("Start 60-Min Mock", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }

            // Past Mock History
            item {
                Text(
                    text = "Recent Mock Test History",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            if (allTests.isEmpty()) {
                item {
                    Text(
                        text = "No mock tests recorded yet. Take your first test above!",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 13.sp
                    )
                }
            } else {
                itemsIndexed(allTests) { _, test ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Text(test.testTitle, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text(
                                    text = "Score: ${test.scoreMarks}/${test.maxMarks} • Correct: ${test.correctAnswers}/${test.totalQuestions}",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (test.accuracyPercentage >= 75) AccentEmerald.copy(alpha = 0.2f) else AccentAmber.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = "${test.accuracyPercentage}%",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 13.sp,
                                    color = if (test.accuracyPercentage >= 75) AccentEmerald else AccentAmber,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
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

    var currentQIndex by remember { mutableIntStateOf(0) }
    var showSubmitConfirmDialog by remember { mutableStateOf(false) }
    var showResultsDialog by remember { mutableStateOf(false) }

    val formattedTime = remember(secondsLeft) {
        val mins = secondsLeft / 60
        val secs = secondsLeft % 60
        String.format("%02d:%02d", mins, secs)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(testTitle, fontWeight = FontWeight.Bold, fontSize = 16.sp, maxLines = 1)
                        Text(
                            text = "Time Remaining: $formattedTime",
                            fontSize = 12.sp,
                            color = if (secondsLeft < 300) AccentRose else AccentEmerald,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                actions = {
                    Button(
                        onClick = { showSubmitConfirmDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = AccentEmerald),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                        modifier = Modifier.testTag("submit_mock_test_btn")
                    ) {
                        Text("Submit", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { padding ->
        if (questions.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
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
                        isCurrent -> PrimaryBlue
                        isAnswered -> AccentEmerald
                        else -> MaterialTheme.colorScheme.surfaceVariant
                    }

                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(bg)
                            .clickable { currentQIndex = idx },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${idx + 1}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = if (isCurrent || isAnswered) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Question Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Question ${currentQIndex + 1} of ${questions.size}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = PrimaryBlue
                        )
                        Text(
                            text = "+4 Marks / 0 Negative",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Text(
                        text = question.questionText,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        lineHeight = 22.sp
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

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.selectMockAnswer(currentQIndex, optIdx)
                            }
                            .testTag("mock_option_$optIdx"),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) PrimaryBlue.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surface
                        ),
                        border = CardDefaults.outlinedCardBorder().copy(
                            brush = Brush.horizontalGradient(listOf(if (isSelected) PrimaryBlue else Color.Transparent, if (isSelected) PrimaryBlue else Color.Transparent))
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = if (isSelected) PrimaryBlue else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier.size(28.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = ('A' + optIdx).toString(),
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                            Text(
                                text = optionText,
                                fontSize = 14.sp,
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
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Previous")
                }

                Button(
                    onClick = {
                        if (currentQIndex < questions.size - 1) {
                            currentQIndex++
                        } else {
                            showSubmitConfirmDialog = true
                        }
                    },
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(if (currentQIndex < questions.size - 1) "Next" else "Review & Submit")
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
                    colors = ButtonDefaults.buttonColors(containerColor = AccentEmerald)
                ) {
                    Text("Yes, Submit")
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
                        color = PrimaryBlue
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
                    }
                ) {
                    Text("Back to Hub")
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
