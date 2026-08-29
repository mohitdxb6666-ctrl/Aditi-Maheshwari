package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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

    val allFlashcards by viewModel.allFlashcards.collectAsState()
    val chapterFlashcards = allFlashcards.filter { it.chapterId == chapterId }

    val questions = remember(chapterId) { viewModel.getQuestionsForChapter(chapterId) }

    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Concept Notes", "Flashcards (${chapterFlashcards.size})", "Practice Quiz (${questions.size})")

    val subject = SubjectType.values().firstOrNull { it.code == chapter?.subjectCode } ?: SubjectType.MATHEMATICS
    val subjectColor = Color(subject.colorHex)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = chapter?.title ?: "Chapter Study",
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            maxLines = 1
                        )
                        Text(
                            text = "${subject.displayName} • CBSE Weightage: ${chapter?.cbseWeightageMarks ?: 6} Marks",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
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
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
                            tint = AccentCyan
                        )
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
                .testTag("chapter_study_container")
        ) {
            PrimaryTabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = subjectColor
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                fontSize = 13.sp,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
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
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = subjectColor.copy(alpha = 0.08f)),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(subjectColor, AccentCyan)))
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
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Text(
                        text = "Curated strictly as per latest NCERT rationalized guidelines & Board marking criteria.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Chapter Summary & Derivations",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = subjectColor
                    )
                    Text(
                        text = summaryNotes,
                        fontSize = 13.sp,
                        lineHeight = 20.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        if (keyFormulas.isNotBlank()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
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
                                fontSize = 15.sp
                            )
                        }
                        Text(
                            text = keyFormulas,
                            fontSize = 13.sp,
                            lineHeight = 20.sp,
                            color = MaterialTheme.colorScheme.onSurface
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
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
            ) {
                Icon(Icons.Default.Psychology, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Ask AI to Explain a Concept in this Chapter", fontSize = 13.sp, fontWeight = FontWeight.Bold)
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
                color = MaterialTheme.colorScheme.onSurfaceVariant
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
                fontWeight = FontWeight.Bold
            )
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = if (currentCard.isMastered) AccentEmerald.copy(alpha = 0.15f) else AccentAmber.copy(alpha = 0.15f)
            ) {
                Text(
                    text = if (currentCard.isMastered) "✓ Mastered" else "Needs Review",
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (currentCard.isMastered) AccentEmerald else AccentAmber
                )
            }
        }

        LinearProgressIndicator(
            progress = { (currentIndex + 1).toFloat() / flashcards.size },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = subjectColor,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )

        // Interactive Flippable Flashcard
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .graphicsLayer {
                    rotationY = rotation
                    cameraDistance = 12f * density
                }
                .clickable { isFlipped = !isFlipped }
                .testTag("interactive_flashcard"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isFlipped) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
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
                        Surface(
                            shape = CircleShape,
                            color = subjectColor.copy(alpha = 0.12f),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Default.QuestionMark,
                                    contentDescription = "Question",
                                    tint = subjectColor,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        Text(
                            text = currentCard.frontTitle,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            color = subjectColor
                        )

                        Text(
                            text = currentCard.frontContent,
                            fontSize = 15.sp,
                            textAlign = TextAlign.Center,
                            lineHeight = 22.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        Text(
                            text = "💡 Tap card to flip & reveal answer",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    // Back side (flipped 180 degrees, so un-invert graphicsLayer for reading)
                    Column(
                        modifier = Modifier.graphicsLayer { rotationY = 180f },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = AccentEmerald.copy(alpha = 0.15f),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Default.Check,
                                    contentDescription = "Answer",
                                    tint = AccentEmerald,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        Text(
                            text = "Concept Breakdown / Formula:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = AccentEmerald
                        )

                        Text(
                            text = currentCard.backExplanation,
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center,
                            lineHeight = 18.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        if (currentCard.formulaOrKeyPoint.isNotBlank()) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.surface
                            ) {
                                Text(
                                    text = "Key: ${currentCard.formulaOrKeyPoint}",
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = subjectColor
                                )
                            }
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
            OutlinedButton(
                onClick = { onToggleMastery(currentCard) },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    if (currentCard.isMastered) Icons.Default.Cancel else Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = if (currentCard.isMastered) AccentRose else AccentEmerald,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    if (currentCard.isMastered) "Mark Review" else "Mark Mastered",
                    fontSize = 12.sp
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
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.Default.ArrowBackIos, contentDescription = "Previous", modifier = Modifier.size(14.dp))
                Text("Previous", fontSize = 12.sp)
            }

            Button(
                onClick = { if (currentIndex < flashcards.size - 1) currentIndex++ },
                enabled = currentIndex < flashcards.size - 1,
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Next", fontSize = 12.sp)
                Icon(Icons.Default.ArrowForwardIos, contentDescription = "Next", modifier = Modifier.size(14.dp))
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
    if (questions.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.Center) {
            Text("Practice questions are loading for this unit.")
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
            .padding(16.dp),
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
                    fontSize = 14.sp
                )
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = subjectColor.copy(alpha = 0.12f)
                ) {
                    Text(
                        text = question.pyqYear,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = subjectColor
                    )
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = question.questionText,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        lineHeight = 22.sp
                    )
                }
            }
        }

        itemsIndexed(question.options) { index, option ->
            val isSelected = selectedOptionIndex == index
            val isCorrect = isSubmitted && index == question.correctOptionIndex
            val isWrong = isSubmitted && isSelected && index != question.correctOptionIndex

            val containerColor = when {
                isCorrect -> AccentEmerald.copy(alpha = 0.18f)
                isWrong -> AccentRose.copy(alpha = 0.18f)
                isSelected -> subjectColor.copy(alpha = 0.12f)
                else -> MaterialTheme.colorScheme.surface
            }

            val borderColor = when {
                isCorrect -> AccentEmerald
                isWrong -> AccentRose
                isSelected -> subjectColor
                else -> Color.Transparent
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(enabled = !isSubmitted) {
                        selectedOptionIndex = index
                    }
                    .testTag("option_$index"),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = containerColor),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(borderColor, borderColor)))
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
                        color = if (isSelected) subjectColor else MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = ('A' + index).toString(),
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 12.sp
                            )
                        }
                    }

                    Text(
                        text = option,
                        fontSize = 14.sp,
                        modifier = Modifier.weight(1f)
                    )

                    if (isCorrect) {
                        Icon(Icons.Default.CheckCircle, contentDescription = "Correct", tint = AccentEmerald)
                    } else if (isWrong) {
                        Icon(Icons.Default.Cancel, contentDescription = "Wrong", tint = AccentRose)
                    }
                }
            }
        }

        if (isSubmitted) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(Icons.Default.Lightbulb, contentDescription = null, tint = AccentAmber, modifier = Modifier.size(18.dp))
                            Text("Step-by-Step CBSE Solution", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                        Text(
                            text = question.explanation,
                            fontSize = 13.sp,
                            lineHeight = 18.sp,
                            color = MaterialTheme.colorScheme.onSurface
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
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = subjectColor)
                ) {
                    Text("Check Answer", fontWeight = FontWeight.Bold, fontSize = 14.sp)
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
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = subjectColor)
                ) {
                    Text(
                        if (currentQIndex < questions.size - 1) "Next Question" else "View Results & Finish",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
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
                    }
                ) {
                    Text("Practice Again")
                }
            }
        )
    }
}
