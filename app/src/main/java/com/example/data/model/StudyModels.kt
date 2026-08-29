package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

enum class SubjectType(val displayName: String, val code: String, val colorHex: Long) {
    MATHEMATICS("Mathematics", "MATH", 0xFF2563EB),
    PHYSICS("Physics", "PHYS", 0xFF7C3AED),
    CHEMISTRY("Chemistry", "CHEM", 0xFF059669),
    BIOLOGY("Biology", "BIO", 0xFFDC2626)
}

enum class QuestionType {
    MCQ,
    ASSERTION_REASON,
    SHORT_ANSWER,
    CASE_BASED
}

data class PracticeQuestion(
    val id: String = UUID.randomUUID().toString(),
    val questionText: String,
    val options: List<String> = emptyList(),
    val correctOptionIndex: Int = 0,
    val explanation: String,
    val questionType: QuestionType = QuestionType.MCQ,
    val assertionText: String? = null,
    val reasonText: String? = null,
    val marks: Int = 1,
    val isPreviousYearQuestion: Boolean = true,
    val pyqYear: String = "CBSE 2024"
)

@Entity(tableName = "flashcards")
data class FlashcardEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val chapterId: String,
    val subjectCode: String,
    val frontTitle: String,
    val frontContent: String,
    val backExplanation: String,
    val formulaOrKeyPoint: String,
    val isMastered: Boolean = false,
    val reviewCount: Int = 0,
    val lastReviewedTimestamp: Long = 0L
)

@Entity(tableName = "chapters")
data class ChapterEntity(
    @PrimaryKey val id: String,
    val subjectCode: String,
    val chapterNumber: Int,
    val title: String,
    val cbseWeightageMarks: Int,
    val summaryNotes: String,
    val keyFormulas: String,
    val totalQuestionsCount: Int,
    val completedQuestionsCount: Int = 0,
    val masteryPercentage: Int = 0,
    val isOfflineReady: Boolean = true,
    val lastStudiedTimestamp: Long = 0L
)

@Entity(tableName = "test_results")
data class TestResultEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val testTitle: String,
    val subjectCode: String,
    val totalQuestions: Int,
    val correctAnswers: Int,
    val scoreMarks: Int,
    val maxMarks: Int,
    val timeSpentSeconds: Long,
    val timestamp: Long = System.currentTimeMillis(),
    val accuracyPercentage: Int = ((correctAnswers.toFloat() / totalQuestions.coerceAtLeast(1)) * 100).toInt()
)

@Entity(tableName = "study_sessions")
data class StudySessionEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val subjectCode: String,
    val chapterTitle: String,
    val durationMinutes: Int,
    val sessionType: String, // "Pomodoro", "Mock Test", "Flashcard Revision", "Notes"
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "peer_discussions")
data class PeerDiscussionEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val groupName: String,
    val subjectCode: String,
    val authorName: String,
    val authorAvatarBadge: String,
    val title: String,
    val questionOrNote: String,
    val upvotes: Int = 0,
    val repliesCount: Int = 0,
    val isSolved: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "doubt_chat_messages")
data class DoubtChatMessage(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val sender: String, // "user" or "gemini"
    val messageText: String,
    val modelUsed: String = "gemini-3.5-flash",
    val isThinkingResponse: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)
