package com.example.data.repository

import android.content.Context
import androidx.room.Room
import com.example.data.local.AppDatabase
import com.example.data.model.*
import com.example.data.remote.gemini.GeminiModel
import com.example.data.remote.gemini.GeminiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StudyRepository(context: Context) {

    private val db = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        "cbse_study_companion.db"
    ).build()

    private val chapterDao = db.chapterDao()
    private val flashcardDao = db.flashcardDao()
    private val testResultDao = db.testResultDao()
    private val studySessionDao = db.studySessionDao()
    private val peerDiscussionDao = db.peerDiscussionDao()
    private val doubtMessageDao = db.doubtMessageDao()

    private val geminiService = GeminiService()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            seedDatabaseIfEmpty()
        }
    }

    private suspend fun seedDatabaseIfEmpty() {
        val existingChapters = chapterDao.getAllChapters().first()
        if (existingChapters.isEmpty()) {
            chapterDao.insertChapters(CbseInitialData.chapters)
            flashcardDao.insertFlashcards(CbseInitialData.flashcards)
            for (discussion in CbseInitialData.peerDiscussions) {
                peerDiscussionDao.insertDiscussion(discussion)
            }
            // Seed sample initial test results for realistic progress analytics
            testResultDao.insertTestResult(
                TestResultEntity(
                    testTitle = "Maths Unit Test (Matrices & Determinants)",
                    subjectCode = "MATH",
                    totalQuestions = 15,
                    correctAnswers = 14,
                    scoreMarks = 36,
                    maxMarks = 40,
                    timeSpentSeconds = 1420,
                    timestamp = System.currentTimeMillis() - 86400000L * 2
                )
            )
            testResultDao.insertTestResult(
                TestResultEntity(
                    testTitle = "Physics Electrostatics & Current Electricity",
                    subjectCode = "PHYS",
                    totalQuestions = 20,
                    correctAnswers = 17,
                    scoreMarks = 42,
                    maxMarks = 50,
                    timeSpentSeconds = 2100,
                    timestamp = System.currentTimeMillis() - 86400000L * 4
                )
            )
            testResultDao.insertTestResult(
                TestResultEntity(
                    testTitle = "Chemistry Solutions & Electrochemistry Diagnostic",
                    subjectCode = "CHEM",
                    totalQuestions = 15,
                    correctAnswers = 11,
                    scoreMarks = 28,
                    maxMarks = 35,
                    timeSpentSeconds = 1650,
                    timestamp = System.currentTimeMillis() - 86400000L * 6
                )
            )
            // Seed a welcome message for doubt solver
            doubtMessageDao.insertMessage(
                DoubtChatMessage(
                    sender = "gemini",
                    messageText = "👋 Welcome to **CBSE 12 AI Study Companion**!\n\nI can help you with:\n• Step-by-step math proofs & calculus integrals with **High Thinking Mode**\n• Physics derivations (Gauss Law, Lens Maker's formula, AC resonance)\n• Organic reaction mechanisms & named reactions\n• Latest CBSE 2026/2025 marking schemes & exam tips\n\nWhat topic or problem would you like to solve today?",
                    modelUsed = "gemini-3.5-flash",
                    isThinkingResponse = false
                )
            )
        }
    }

    // Chapters
    fun getAllChapters(): Flow<List<ChapterEntity>> = chapterDao.getAllChapters()
    fun getChaptersBySubject(subjectCode: String): Flow<List<ChapterEntity>> = chapterDao.getChaptersBySubject(subjectCode)
    suspend fun getChapterById(chapterId: String): ChapterEntity? = chapterDao.getChapterById(chapterId)
    suspend fun updateChapterProgress(chapterId: String, mastery: Int, completed: Int) {
        chapterDao.updateChapterProgress(chapterId, mastery, completed, System.currentTimeMillis())
    }

    // Flashcards
    fun getAllFlashcards(): Flow<List<FlashcardEntity>> = flashcardDao.getAllFlashcards()
    fun getFlashcardsBySubject(subjectCode: String): Flow<List<FlashcardEntity>> = flashcardDao.getFlashcardsBySubject(subjectCode)
    fun getFlashcardsByChapter(chapterId: String): Flow<List<FlashcardEntity>> = flashcardDao.getFlashcardsByChapter(chapterId)
    suspend fun updateFlashcardMastery(flashcardId: String, isMastered: Boolean) {
        flashcardDao.updateFlashcardMastery(flashcardId, isMastered, System.currentTimeMillis())
    }

    // Tests & Results
    fun getAllTestResults(): Flow<List<TestResultEntity>> = testResultDao.getAllTestResults()
    suspend fun recordTestResult(result: TestResultEntity) {
        testResultDao.insertTestResult(result)
    }

    // Study Sessions
    fun getAllStudySessions(): Flow<List<StudySessionEntity>> = studySessionDao.getAllStudySessions()
    suspend fun logStudySession(session: StudySessionEntity) {
        studySessionDao.insertStudySession(session)
    }

    // Peer Discussions
    fun getAllDiscussions(): Flow<List<PeerDiscussionEntity>> = peerDiscussionDao.getAllDiscussions()
    fun getDiscussionsBySubject(subjectCode: String): Flow<List<PeerDiscussionEntity>> = peerDiscussionDao.getDiscussionsBySubject(subjectCode)
    suspend fun postDiscussion(discussion: PeerDiscussionEntity) {
        peerDiscussionDao.insertDiscussion(discussion)
    }
    suspend fun upvoteDiscussion(id: String) {
        peerDiscussionDao.upvoteDiscussion(id)
    }

    // Doubt Chat
    fun getAllDoubtMessages(): Flow<List<DoubtChatMessage>> = doubtMessageDao.getAllDoubtMessages()
    suspend fun saveDoubtMessage(message: DoubtChatMessage) {
        doubtMessageDao.insertMessage(message)
    }
    suspend fun clearDoubtHistory() {
        doubtMessageDao.clearHistory()
    }

    // AI Gemini Call
    suspend fun askAi(
        prompt: String,
        model: GeminiModel,
        enableSearchGrounding: Boolean = false
    ): Result<String> {
        return geminiService.askGemini(
            prompt = prompt,
            model = model,
            enableSearchGrounding = enableSearchGrounding
        )
    }

    fun getQuestionsForChapter(chapterId: String): List<PracticeQuestion> {
        return CbseInitialData.sampleQuestions[chapterId] ?: listOf(
            PracticeQuestion(
                questionText = "Which principle states that the total electric flux out of a closed surface is equal to the charge enclosed divided by the permittivity?",
                options = listOf("Gauss's Law", "Coulomb's Law", "Ampere's Law", "Faraday's Law"),
                correctOptionIndex = 0,
                explanation = "By definition, Gauss's law equates the surface integral of the electric field to enclosed charge over epsilon-0.",
                questionType = QuestionType.MCQ
            ),
            PracticeQuestion(
                questionText = "Assertion (A): Work done in moving a charge over a closed loop in an electrostatic field is zero.\nReason (R): Electrostatic force is a conservative force.",
                options = listOf(
                    "Both (A) and (R) are true and (R) is correct explanation of (A)",
                    "Both (A) and (R) are true but (R) is NOT correct explanation of (A)",
                    "(A) is true but (R) is false",
                    "(A) is false but (R) is true"
                ),
                correctOptionIndex = 0,
                explanation = "Electrostatic forces are conservative, meaning work done along any closed path is strictly zero.",
                questionType = QuestionType.ASSERTION_REASON
            )
        )
    }
}
