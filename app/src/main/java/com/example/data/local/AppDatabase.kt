package com.example.data.local

import androidx.room.*
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ChapterDao {
    @Query("SELECT * FROM chapters ORDER BY chapterNumber ASC")
    fun getAllChapters(): Flow<List<ChapterEntity>>

    @Query("SELECT * FROM chapters WHERE subjectCode = :subjectCode ORDER BY chapterNumber ASC")
    fun getChaptersBySubject(subjectCode: String): Flow<List<ChapterEntity>>

    @Query("SELECT * FROM chapters WHERE id = :chapterId")
    suspend fun getChapterById(chapterId: String): ChapterEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChapters(chapters: List<ChapterEntity>)

    @Update
    suspend fun updateChapter(chapter: ChapterEntity)

    @Query("UPDATE chapters SET masteryPercentage = :mastery, completedQuestionsCount = :completed, lastStudiedTimestamp = :timestamp WHERE id = :chapterId")
    suspend fun updateChapterProgress(chapterId: String, mastery: Int, completed: Int, timestamp: Long)
}

@Dao
interface FlashcardDao {
    @Query("SELECT * FROM flashcards WHERE chapterId = :chapterId")
    fun getFlashcardsByChapter(chapterId: String): Flow<List<FlashcardEntity>>

    @Query("SELECT * FROM flashcards WHERE subjectCode = :subjectCode")
    fun getFlashcardsBySubject(subjectCode: String): Flow<List<FlashcardEntity>>

    @Query("SELECT * FROM flashcards")
    fun getAllFlashcards(): Flow<List<FlashcardEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFlashcards(flashcards: List<FlashcardEntity>)

    @Query("UPDATE flashcards SET isMastered = :isMastered, reviewCount = reviewCount + 1, lastReviewedTimestamp = :timestamp WHERE id = :flashcardId")
    suspend fun updateFlashcardMastery(flashcardId: String, isMastered: Boolean, timestamp: Long)
}

@Dao
interface TestResultDao {
    @Query("SELECT * FROM test_results ORDER BY timestamp DESC")
    fun getAllTestResults(): Flow<List<TestResultEntity>>

    @Query("SELECT * FROM test_results WHERE subjectCode = :subjectCode ORDER BY timestamp DESC")
    fun getTestResultsBySubject(subjectCode: String): Flow<List<TestResultEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTestResult(result: TestResultEntity)
}

@Dao
interface StudySessionDao {
    @Query("SELECT * FROM study_sessions ORDER BY timestamp DESC")
    fun getAllStudySessions(): Flow<List<StudySessionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudySession(session: StudySessionEntity)

    @Query("SELECT SUM(durationMinutes) FROM study_sessions")
    fun getTotalStudyMinutes(): Flow<Int?>
}

@Dao
interface PeerDiscussionDao {
    @Query("SELECT * FROM peer_discussions ORDER BY timestamp DESC")
    fun getAllDiscussions(): Flow<List<PeerDiscussionEntity>>

    @Query("SELECT * FROM peer_discussions WHERE subjectCode = :subjectCode ORDER BY timestamp DESC")
    fun getDiscussionsBySubject(subjectCode: String): Flow<List<PeerDiscussionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDiscussion(discussion: PeerDiscussionEntity)

    @Query("UPDATE peer_discussions SET upvotes = upvotes + 1 WHERE id = :id")
    suspend fun upvoteDiscussion(id: String)
}

@Dao
interface DoubtMessageDao {
    @Query("SELECT * FROM doubt_chat_messages ORDER BY timestamp ASC")
    fun getAllDoubtMessages(): Flow<List<DoubtChatMessage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: DoubtChatMessage)

    @Query("DELETE FROM doubt_chat_messages")
    suspend fun clearHistory()
}

@Database(
    entities = [
        ChapterEntity::class,
        FlashcardEntity::class,
        TestResultEntity::class,
        StudySessionEntity::class,
        PeerDiscussionEntity::class,
        DoubtChatMessage::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun chapterDao(): ChapterDao
    abstract fun flashcardDao(): FlashcardDao
    abstract fun testResultDao(): TestResultDao
    abstract fun studySessionDao(): StudySessionDao
    abstract fun peerDiscussionDao(): PeerDiscussionDao
    abstract fun doubtMessageDao(): DoubtMessageDao
}
