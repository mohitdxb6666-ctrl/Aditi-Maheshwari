package com.example.ui.viewmodel

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.*
import com.example.data.remote.gemini.GeminiModel
import com.example.data.repository.StudyRepository
import com.example.utils.NotificationHelper
import com.example.utils.PdfReportGenerator
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File

sealed class ScreenDestination {
    object Home : ScreenDestination()
    data class SubjectDetail(val subjectCode: String) : ScreenDestination()
    data class ChapterStudy(val chapterId: String) : ScreenDestination()
    object MockTestHub : ScreenDestination()
    data class ActiveMockTest(val subjectCode: String, val durationMinutes: Int, val testTitle: String) : ScreenDestination()
    object AiDoubtSolver : ScreenDestination()
    object PeerStudyGroups : ScreenDestination()
    object AnalyticsDashboard : ScreenDestination()
    object RevisionTimer : ScreenDestination()
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = StudyRepository(application)

    // Current Screen Navigation
    private val _currentScreen = MutableStateFlow<ScreenDestination>(ScreenDestination.Home)
    val currentScreen: StateFlow<ScreenDestination> = _currentScreen.asStateFlow()

    // Dark Mode Override (null = follow system, true = force dark, false = force light)
    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    fun toggleDarkMode() {
        _isDarkMode.value = !_isDarkMode.value
    }

    // Data from Repository
    val allChapters: StateFlow<List<ChapterEntity>> = repository.getAllChapters()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allFlashcards: StateFlow<List<FlashcardEntity>> = repository.getAllFlashcards()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allTestResults: StateFlow<List<TestResultEntity>> = repository.getAllTestResults()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allStudySessions: StateFlow<List<StudySessionEntity>> = repository.getAllStudySessions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allDiscussions: StateFlow<List<PeerDiscussionEntity>> = repository.getAllDiscussions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val doubtMessages: StateFlow<List<DoubtChatMessage>> = repository.getAllDoubtMessages()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // AI Chat State
    private val _isAiLoading = MutableStateFlow(false)
    val isAiLoading: StateFlow<Boolean> = _isAiLoading.asStateFlow()

    private val _selectedGeminiModel = MutableStateFlow(GeminiModel.PRO_THINKING)
    val selectedGeminiModel: StateFlow<GeminiModel> = _selectedGeminiModel.asStateFlow()

    private val _enableSearchGrounding = MutableStateFlow(true)
    val enableSearchGrounding: StateFlow<Boolean> = _enableSearchGrounding.asStateFlow()

    fun setGeminiModel(model: GeminiModel) {
        _selectedGeminiModel.value = model
    }

    fun toggleSearchGrounding() {
        _enableSearchGrounding.value = !_enableSearchGrounding.value
    }

    fun sendDoubt(prompt: String) {
        if (prompt.isBlank()) return
        val userMsg = DoubtChatMessage(
            sender = "user",
            messageText = prompt.trim(),
            modelUsed = _selectedGeminiModel.value.displayName
        )
        viewModelScope.launch {
            repository.saveDoubtMessage(userMsg)
            _isAiLoading.value = true
            val result = repository.askAi(
                prompt = prompt,
                model = _selectedGeminiModel.value,
                enableSearchGrounding = _enableSearchGrounding.value
            )
            _isAiLoading.value = false

            val responseText = result.getOrElse { e ->
                "⚠️ Notice: ${e.localizedMessage}\n\n(Tip: When using direct AI query, ensure your Gemini API key is configured in the AI Studio Secrets panel. You can also explore the pre-loaded high-yield CBSE 12 notes and formula solutions offline!)"
            }

            val aiMsg = DoubtChatMessage(
                sender = "gemini",
                messageText = responseText,
                modelUsed = _selectedGeminiModel.value.displayName,
                isThinkingResponse = _selectedGeminiModel.value == GeminiModel.PRO_THINKING
            )
            repository.saveDoubtMessage(aiMsg)
        }
    }

    fun clearDoubtChat() {
        viewModelScope.launch {
            repository.clearDoubtHistory()
        }
    }

    // Navigation Helper
    fun navigateTo(screen: ScreenDestination) {
        _currentScreen.value = screen
    }

    // Flashcard Mastery
    fun toggleFlashcardMastery(flashcard: FlashcardEntity) {
        viewModelScope.launch {
            repository.updateFlashcardMastery(flashcard.id, !flashcard.isMastered)
        }
    }

    // Chapter Progress
    fun completeChapterPractice(chapterId: String, score: Int, total: Int) {
        viewModelScope.launch {
            val mastery = ((score.toFloat() / total.coerceAtLeast(1)) * 100).toInt()
            repository.updateChapterProgress(chapterId, mastery, total)
        }
    }

    // Active Mock Test State
    private val _mockQuestions = MutableStateFlow<List<PracticeQuestion>>(emptyList())
    val mockQuestions: StateFlow<List<PracticeQuestion>> = _mockQuestions.asStateFlow()

    private val _mockAnswers = MutableStateFlow<Map<Int, Int>>(emptyMap()) // index -> selected option index
    val mockAnswers: StateFlow<Map<Int, Int>> = _mockAnswers.asStateFlow()

    private val _mockTimeRemainingSeconds = MutableStateFlow(0)
    val mockTimeRemainingSeconds: StateFlow<Int> = _mockTimeRemainingSeconds.asStateFlow()

    private var mockTimer: CountDownTimer? = null

    fun startMockTest(subjectCode: String, durationMinutes: Int, testTitle: String) {
        val chapterQuestions = when (subjectCode) {
            "MATH" -> repository.getQuestionsForChapter("math_ch1") + repository.getQuestionsForChapter("math_ch3")
            "PHYS" -> repository.getQuestionsForChapter("phys_ch1")
            "CHEM" -> repository.getQuestionsForChapter("chem_ch1")
            "BIO" -> repository.getQuestionsForChapter("bio_ch1")
            else -> repository.getQuestionsForChapter("math_ch1") + repository.getQuestionsForChapter("phys_ch1")
        }
        _mockQuestions.value = chapterQuestions
        _mockAnswers.value = emptyMap()
        _mockTimeRemainingSeconds.value = durationMinutes * 60

        mockTimer?.cancel()
        mockTimer = object : CountDownTimer((durationMinutes * 60 * 1000).toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                _mockTimeRemainingSeconds.value = (millisUntilFinished / 1000).toInt()
            }

            override fun onFinish() {
                _mockTimeRemainingSeconds.value = 0
            }
        }.start()

        navigateTo(ScreenDestination.ActiveMockTest(subjectCode, durationMinutes, testTitle))
    }

    fun selectMockAnswer(questionIndex: Int, optionIndex: Int) {
        val updated = _mockAnswers.value.toMutableMap()
        updated[questionIndex] = optionIndex
        _mockAnswers.value = updated
    }

    fun submitMockTest(testTitle: String, subjectCode: String, durationMinutes: Int) {
        mockTimer?.cancel()
        val questions = _mockQuestions.value
        val answers = _mockAnswers.value
        var correct = 0
        questions.forEachIndexed { index, q ->
            if (answers[index] == q.correctOptionIndex) {
                correct++
            }
        }
        val total = questions.size
        val score = correct * 4 // CBSE standard 4 marks per correct
        val maxMarks = total * 4
        val timeSpent = (durationMinutes * 60) - _mockTimeRemainingSeconds.value

        val result = TestResultEntity(
            testTitle = testTitle,
            subjectCode = subjectCode,
            totalQuestions = total,
            correctAnswers = correct,
            scoreMarks = score,
            maxMarks = maxMarks,
            timeSpentSeconds = timeSpent.toLong().coerceAtLeast(1L)
        )

        viewModelScope.launch {
            repository.recordTestResult(result)
            repository.logStudySession(
                StudySessionEntity(
                    subjectCode = subjectCode,
                    chapterTitle = testTitle,
                    durationMinutes = (timeSpent / 60).coerceAtLeast(1),
                    sessionType = "Mock Test"
                )
            )
        }
    }

    // Revision Study Timer
    private val _timerDurationSeconds = MutableStateFlow(25 * 60) // Default 25 min Pomodoro
    val timerDurationSeconds: StateFlow<Int> = _timerDurationSeconds.asStateFlow()

    private val _timerSecondsLeft = MutableStateFlow(25 * 60)
    val timerSecondsLeft: StateFlow<Int> = _timerSecondsLeft.asStateFlow()

    private val _isTimerRunning = MutableStateFlow(false)
    val isTimerRunning: StateFlow<Boolean> = _isTimerRunning.asStateFlow()

    private var studyTimer: CountDownTimer? = null

    fun setTimerPreset(minutes: Int) {
        studyTimer?.cancel()
        _isTimerRunning.value = false
        _timerDurationSeconds.value = minutes * 60
        _timerSecondsLeft.value = minutes * 60
    }

    fun startPauseTimer(subjectCode: String = "ALL") {
        if (_isTimerRunning.value) {
            studyTimer?.cancel()
            _isTimerRunning.value = false
        } else {
            _isTimerRunning.value = true
            val totalMillis = (_timerSecondsLeft.value * 1000).toLong()
            studyTimer = object : CountDownTimer(totalMillis, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    _timerSecondsLeft.value = (millisUntilFinished / 1000).toInt()
                }

                override fun onFinish() {
                    _isTimerRunning.value = false
                    _timerSecondsLeft.value = 0
                    NotificationHelper.showStudyReminder(
                        getApplication(),
                        "🎉 Study Session Completed!",
                        "Great job completing your revision block! Take a 5-minute breather before the next chapter."
                    )
                    viewModelScope.launch {
                        repository.logStudySession(
                            StudySessionEntity(
                                subjectCode = subjectCode,
                                chapterTitle = "Focused Revision Block",
                                durationMinutes = _timerDurationSeconds.value / 60,
                                sessionType = "Pomodoro"
                            )
                        )
                    }
                }
            }.start()
        }
    }

    fun resetTimer() {
        studyTimer?.cancel()
        _isTimerRunning.value = false
        _timerSecondsLeft.value = _timerDurationSeconds.value
    }

    // Peer Discussions
    fun postNewDiscussion(subjectCode: String, groupName: String, title: String, question: String) {
        if (title.isBlank() || question.isBlank()) return
        val newPost = PeerDiscussionEntity(
            groupName = groupName,
            subjectCode = subjectCode,
            authorName = "You (Class 12 Aspirant)",
            authorAvatarBadge = "ME",
            title = title.trim(),
            questionOrNote = question.trim(),
            upvotes = 1,
            repliesCount = 0,
            isSolved = false
        )
        viewModelScope.launch {
            repository.postDiscussion(newPost)
        }
    }

    fun upvotePost(id: String) {
        viewModelScope.launch {
            repository.upvoteDiscussion(id)
        }
    }

    // PDF Export
    fun exportAndSharePdfReport(): File? {
        val chs = allChapters.value
        val tests = allTestResults.value
        val overallMastery = if (chs.isNotEmpty()) chs.map { it.masteryPercentage }.average().toInt() else 75
        val weakChapters = chs.filter { it.masteryPercentage < 65 }
        val file = PdfReportGenerator.generateAndShareStudyReport(
            context = getApplication(),
            chapters = chs,
            testResults = tests,
            overallMastery = overallMastery,
            weakChapters = weakChapters
        )
        if (file != null) {
            PdfReportGenerator.sharePdf(getApplication(), file)
        }
        return file
    }

    // Trigger Notification Simulator
    fun triggerExamReminderNotification() {
        NotificationHelper.showStudyReminder(
            getApplication(),
            "⏰ CBSE Class 12 Boards Revision Reminder",
            "Target for today: Complete 20 Calculus integrals & Ray Optics lens maker formula derivations!"
        )
    }

    fun getQuestionsForChapter(chapterId: String): List<PracticeQuestion> {
        return repository.getQuestionsForChapter(chapterId)
    }
}
