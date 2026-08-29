package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FlashcardEntity
import com.example.data.model.PracticeQuestion
import com.example.data.model.SubjectType
import com.example.ui.components.FrostedGlassCard
import com.example.ui.components.FrostedPillBadge
import com.example.ui.components.FrostedProgressBar
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.ScreenDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChapterStudyScreen(
    chapterId: String,
    viewModel: MainViewModel
) {
    val allChapters by viewModel.allChapters.collectAsState()
    val chapter = allChapters.firstOrNull { it.id == chapterId }
    val isDark = isSystemInDarkTheme()

    val allFlashcards by viewModel.allFlashcards.collectAsState()
    val chapterFlashcards = allFlashcards.filter { it.chapterId == chapterId }

    val questions = remember(chapterId) { viewModel.getQuestionsForChapter(chapterId) }

    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Concept Notes", "Flashcards (${chapterFlashcards.size})", "Practice Quiz (${questions.size})")

    val subject = SubjectType.values().firstOrNull { it.code == chapter?.subjectCode } ?: SubjectType.MATHEMATICS
    val subjectColor = Color(subject.colorHex)

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = chapter?.title ?: "Chapter Study",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            maxLines = 1,
                            color = if (isDark) Color.White else Slate900
                        )
                        Text(
                            text = "${subject.displayName} • CBSE Weightage: ${chapter?.cbseWeightageMarks ?: 6} Marks",
                            fontSize = 11.sp,
                            color = if (isDark) Slate400 else Slate600
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (chapter != null) {
                                viewModel.navigateTo(ScreenDestination.SubjectDetail(chapter.subjectCode))
                            } else {
                                viewModel.navigateTo(ScreenDestination.Home)
                            }
                        },
                        modifier = Modifier.testTag("chapter_study_back_btn")
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
                            viewModel.navigateTo(ScreenDestination.AiDoubtSolver)
                        }
                    ) {
                        Icon(
                            Icons.Default.Psychology,
                            contentDescription = "Ask Doubt",
                            tint = NeonCyan
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
                .testTag("chapter_study_container")
        ) {
            // Frosted Tab Selector
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (isDark) Color(0x1AFFFFFF) else Color(0xD9FFFFFF))
                    .border(BorderStroke(1.dp, if (isDark) Color(0x26FFFFFF) else Color(0x1F0F172A)), RoundedCornerShape(14.dp))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                tabs.forEachIndexed { index, title ->
                    val isSelected = selectedTab == index
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                if (isSelected) {
                                    if (isDark) Color(0x33FFFFFF) else Color.White
                                } else Color.Transparent
                            )
                            .clickable { selectedTab = index }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = title,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) {
                                if (isDark) Color.White else Slate900
                            } else {
                                if (isDark) Slate400 else Slate600
                            }
                        )
                    }
                }
            }

            when (selectedTab) {
                0 -> ConceptNotesTab(chapter?.summaryNotes ?: "", chapter?.keyFormulas ?: "", subjectColor, viewModel, chapter?.title ?: "")
                1 -> FlashcardsTab(chapterFlashcards, subjectColor, onToggleMastery = { card -> viewModel.toggleFlashcardMastery(card) })
                2 -> PracticeQuizTab(chapterId, questions, subjectColor, onQuizComplete = { score, total ->
                    viewModel.completeChapterPractice(chapterId, score, total)
                })
            }
        }
    }
}

@Composable
fun ConceptNotesTab(
    summaryNotes: String,
    keyFormulas: String,
    subjectColor: Color,
    viewModel: MainViewModel,
    chapterTitle: String
) {
    val isDark = isSystemInDarkTheme()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            FrostedGlassCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                backgroundColor = if (isDark) subjectColor.copy(alpha = 0.15f) else Color(0xD9FFFFFF),
                borderColor = subjectColor.copy(alpha = 0.5f)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = subjectColor, modifier = Modifier.size(18.dp))
                        Text(
                            text = "High-Yield CBSE 2026 Core Notes",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = if (isDark) Color.White else Slate900
                        )
                    }
                    Text(
                        text = "Curated strictly as per latest NCERT rationalized guidelines & Board marking criteria.",
                        fontSize = 11.sp,
                        color = if (isDark) Slate300 else Slate600
                    )
                }
            }
        }

        item {
            FrostedGlassCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                backgroundColor = if (isDark) Color(0x14FFFFFF) else Color(0xD9FFFFFF),
                borderColor = if (isDark) Color(0x26FFFFFF) else Color(0x1F0F172A)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Chapter Summary & Derivations",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = subjectColor
                    )
                    Text(
                        text = summaryNotes,
                        fontSize = 13.sp,
                        lineHeight = 20.sp,
                        color = if (isDark) Color.White else Slate900
                    )
                }
            }
        }

        if (keyFormulas.isNotBlank()) {
            item {
                FrostedGlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    backgroundColor = if (isDark) Color(0x1AFFFFFF) else Color(0xD9FFFFFF),
                    borderColor = if (isDark) Color(0x33FFFFFF) else Color(0x1F0F172A)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(Icons.Default.Functions, contentDescription = null, tint = subjectColor, modifier = Modifier.size(20.dp))
                            Text(
                                text = "Essential Formulas & Equations",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = if (isDark) Color.White else Slate900
                            )
                        }
                        Text(
                            text = keyFormulas,
                            fontSize = 12.sp,
                            lineHeight = 18.sp,
                            color = if (isDark) Slate200 else Slate800
                        )
                    }
                }
            }
        }

        item {
            Button(
                onClick = { viewModel.navigateTo(ScreenDestination.AiDoubtSolver) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NeonBlue)
            ) {
                Icon(Icons.Default.Psychology, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Ask AI to Explain a Concept in this Chapter", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}

@Composable
fun FlashcardsTab(
    flashcards: List<FlashcardEntity>,
    subjectColor: Color,
    onToggleMastery: (FlashcardEntity) -> Unit
) {
    val isDark = isSystemInDarkTheme()
    if (flashcards.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No flashcards generated for this chapter yet.",
                textAlign = TextAlign.Center,
                color = if (isDark) Slate400 else Slate600
            )
        }
        return
    }

    var currentIndex by remember { mutableIntStateOf(0) }
    var isFlipped by remember { mutableStateOf(false) }

    val currentCard = flashcards[currentIndex.coerceIn(0, flashcards.size - 1)]

    // Reset flip when card changes
    LaunchedEffect(currentIndex) {
        isFlipped = false
    }

    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(durationMillis = 350),
        label = "flashcard_rotation"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Progress indicator
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Card ${currentIndex + 1} of ${flashcards.size}",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDark) Color.White else Slate900
            )
            FrostedPillBadge(
                text = if (currentCard.isMastered) "✓ MASTERED" else "NEEDS REVIEW",
                accentColor = if (currentCard.isMastered) NeonEmerald else NeonAmber
            )
        }

        FrostedProgressBar(
            progress = (currentIndex + 1).toFloat() / flashcards.size,
            fillBrush = Brush.horizontalGradient(listOf(subjectColor, subjectColor.copy(alpha = 0.6f))),
            height = 6.dp
        )

        // Interactive Flippable Flashcard in Frosted Glass
        FrostedGlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .graphicsLayer {
                    rotationY = rotation
                    cameraDistance = 12f * density
                }
                .clickable { isFlipped = !isFlipped }
                .testTag("interactive_flashcard"),
            shape = RoundedCornerShape(24.dp),
            backgroundColor = if (isFlipped) {
                if (isDark) Color(0x2610B981) else Color(0xFFECFDF5)
            } else {
                if (isDark) Color(0x1AFFFFFF) else Color(0xD9FFFFFF)
            },
            borderColor = if (isFlipped) NeonEmerald.copy(alpha = 0.6f) else if (isDark) Color(0x33FFFFFF) else Color(0x1F0F172A)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                if (rotation <= 90f) {
                    // Front side
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(subjectColor.copy(alpha = 0.2f))
                                .border(BorderStroke(1.dp, subjectColor.copy(alpha = 0.4f)), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.QuestionMark,
                                contentDescription = "Question",
                                tint = subjectColor,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Text(
                            text = currentCard.frontTitle,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            textAlign = TextAlign.Center,
                            color = subjectColor
                        )

                        Text(
                            text = currentCard.frontContent,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            lineHeight = 20.sp,
                            color = if (isDark) Color.White else Slate900
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        Text(
                            text = "💡 Tap card to flip & reveal answer",
                            fontSize = 11.sp,
                            color = if (isDark) Slate400 else Slate600
                        )
                    }
                } else {
                    // Back side
                    Column(
                        modifier = Modifier.graphicsLayer { rotationY = 180f },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(NeonEmerald.copy(alpha = 0.2f))
                                .border(BorderStroke(1.dp, NeonEmerald.copy(alpha = 0.4f)), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Check,
                                contentDescription = "Answer",
                                tint = NeonEmerald,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Text(
                            text = "Concept Breakdown / Formula:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = NeonEmerald
                        )

                        Text(
                            text = currentCard.backExplanation,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                            lineHeight = 17.sp,
                            color = if (isDark) Color.White else Slate900
                        )

                        if (currentCard.formulaOrKeyPoint.isNotBlank()) {
                            FrostedPillBadge(
                                text = "Key: ${currentCard.formulaOrKeyPoint}",
                                accentColor = subjectColor
                            )
                        }
                    }
                }
            }
        }

        // Action Buttons: Mastered vs Needs Review + Navigation
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = { onToggleMastery(currentCard) },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (currentCard.isMastered) NeonRose else NeonEmerald
                )
            ) {
                Icon(
                    if (currentCard.isMastered) Icons.Default.Cancel else Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    if (currentCard.isMastered) "Mark Review" else "Mark Mastered",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { if (currentIndex > 0) currentIndex-- },
                enabled = currentIndex > 0,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = subjectColor)
            ) {
                Icon(Icons.Default.ArrowBackIos, contentDescription = "Previous", modifier = Modifier.size(14.dp), tint = Color.White)
                Text("Previous", fontSize = 12.sp, color = Color.White)
            }

            Button(
                onClick = { if (currentIndex < flashcards.size - 1) currentIndex++ },
                enabled = currentIndex < flashcards.size - 1,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = subjectColor)
            ) {
                Text("Next", fontSize = 12.sp, color = Color.White)
                Icon(Icons.Default.ArrowForwardIos, contentDescription = "Next", modifier = Modifier.size(14.dp), tint = Color.White)
            }
        }
    }
}

@Composable
fun PracticeQuizTab(
    chapterId: String,
    questions: List<PracticeQuestion>,
    subjectColor: Color,
    onQuizComplete: (Int, Int) -> Unit
) {
    val isDark = isSystemInDarkTheme()
    if (questions.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.Center) {
            Text("Practice questions are loading for this unit.", color = if (isDark) Slate400 else Slate600)
        }
        return
    }

    var currentQIndex by remember { mutableIntStateOf(0) }
    var selectedOptionIndex by remember { mutableStateOf<Int?>(null) }
    var isSubmitted by remember { mutableStateOf(false) }
    var score by remember { mutableIntStateOf(0) }
    var showResultsDialog by remember { mutableStateOf(false) }

    val question = questions[currentQIndex]

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Question ${currentQIndex + 1} of ${questions.size}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = if (isDark) Color.White else Slate900
                )
                FrostedPillBadge(
                    text = question.pyqYear,
                    accentColor = subjectColor
                )
            }
        }

        item {
            FrostedGlassCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                backgroundColor = if (isDark) Color(0x14FFFFFF) else Color(0xD9FFFFFF),
                borderColor = if (isDark) Color(0x26FFFFFF) else Color(0x1F0F172A)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = question.questionText,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        lineHeight = 20.sp,
                        color = if (isDark) Color.White else Slate900
                    )
                }
            }
        }

        itemsIndexed(question.options) { index, option ->
            val isSelected = selectedOptionIndex == index
            val isCorrect = isSubmitted && index == question.correctOptionIndex
            val isWrong = isSubmitted && isSelected && index != question.correctOptionIndex

            val containerColor = when {
                isCorrect -> if (isDark) Color(0x2610B981) else Color(0xFFECFDF5)
                isWrong -> if (isDark) Color(0x26F43F5E) else Color(0xFFFFF1F2)
                isSelected -> if (isDark) subjectColor.copy(alpha = 0.2f) else subjectColor.copy(alpha = 0.12f)
                else -> if (isDark) Color(0x14FFFFFF) else Color(0xD9FFFFFF)
            }

            val borderColor = when {
                isCorrect -> NeonEmerald
                isWrong -> NeonRose
                isSelected -> subjectColor
                else -> if (isDark) Color(0x26FFFFFF) else Color(0x1F0F172A)
            }

            FrostedGlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(enabled = !isSubmitted) {
                        selectedOptionIndex = index
                    }
                    .testTag("option_$index"),
                shape = RoundedCornerShape(16.dp),
                backgroundColor = containerColor,
                borderColor = borderColor
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
                            .background(if (isSelected) subjectColor else if (isDark) Color(0x26FFFFFF) else Color(0x1A0F172A)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = ('A' + index).toString(),
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color.White else if (isDark) Slate300 else Slate700,
                            fontSize = 12.sp
                        )
                    }

                    Text(
                        text = option,
                        fontSize = 13.sp,
                        modifier = Modifier.weight(1f),
                        color = if (isDark) Color.White else Slate900
                    )

                    if (isCorrect) {
                        Icon(Icons.Default.CheckCircle, contentDescription = "Correct", tint = NeonEmerald)
                    } else if (isWrong) {
                        Icon(Icons.Default.Cancel, contentDescription = "Wrong", tint = NeonRose)
                    }
                }
            }
        }

        if (isSubmitted) {
            item {
                FrostedGlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    backgroundColor = if (isDark) Color(0x1AFFFFFF) else Color(0xD9FFFFFF),
                    borderColor = if (isDark) Color(0x26FFFFFF) else Color(0x1F0F172A)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(Icons.Default.Lightbulb, contentDescription = null, tint = NeonAmber, modifier = Modifier.size(18.dp))
                            Text("Step-by-Step CBSE Solution", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = if (isDark) Color.White else Slate900)
                        }
                        Text(
                            text = question.explanation,
                            fontSize = 12.sp,
                            lineHeight = 17.sp,
                            color = if (isDark) Slate300 else Slate700
                        )
                    }
                }
            }
        }

        item {
            if (!isSubmitted) {
                Button(
                    onClick = {
                        if (selectedOptionIndex != null) {
                            isSubmitted = true
                            if (selectedOptionIndex == question.correctOptionIndex) {
                                score++
                            }
                        }
                    },
                    enabled = selectedOptionIndex != null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = subjectColor)
                ) {
                    Text("Check Answer", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.White)
                }
            } else {
                Button(
                    onClick = {
                        if (currentQIndex < questions.size - 1) {
                            currentQIndex++
                            selectedOptionIndex = null
                            isSubmitted = false
                        } else {
                            onQuizComplete(score, questions.size)
                            showResultsDialog = true
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = subjectColor)
                ) {
                    Text(
                        if (currentQIndex < questions.size - 1) "Next Question" else "View Results & Finish",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = Color.White
                    )
                }
            }
        }
    }

    if (showResultsDialog) {
        val percentage = ((score.toFloat() / questions.size) * 100).toInt()
        AlertDialog(
            onDismissRequest = { showResultsDialog = false },
            title = { Text("Chapter Practice Completed!", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Your Score: $score / ${questions.size} ($percentage% Accuracy)")
                    Text(
                        if (percentage >= 75) "🎉 Excellent understanding! Progress updated."
                        else "Keep reviewing the flashcards and concept notes for this unit."
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showResultsDialog = false
                        currentQIndex = 0
                        selectedOptionIndex = null
                        isSubmitted = false
                        score = 0
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = subjectColor)
                ) {
                    Text("Practice Again")
                }
            }
        )
    }
}

