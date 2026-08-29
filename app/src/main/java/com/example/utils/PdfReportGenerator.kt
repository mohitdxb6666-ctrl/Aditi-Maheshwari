package com.example.utils

import android.content.Context
import android.content.Intent
import android.graphics.*
import android.graphics.pdf.PdfDocument
import androidx.core.content.FileProvider
import com.example.data.model.ChapterEntity
import com.example.data.model.TestResultEntity
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

object PdfReportGenerator {

    fun generateAndShareStudyReport(
        context: Context,
        chapters: List<ChapterEntity>,
        testResults: List<TestResultEntity>,
        overallMastery: Int,
        weakChapters: List<ChapterEntity>
    ): File? {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 standard (595x842 pt)
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas

        val paint = Paint().apply { isAntiAlias = true }
        val titlePaint = Paint().apply {
            isAntiAlias = true
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textSize = 20f
            color = Color.rgb(15, 23, 42) // Slate 900
        }
        val subheadPaint = Paint().apply {
            isAntiAlias = true
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textSize = 13f
            color = Color.rgb(37, 99, 235) // Blue 600
        }
        val bodyPaint = Paint().apply {
            isAntiAlias = true
            textSize = 10f
            color = Color.rgb(51, 65, 85) // Slate 700
        }
        val boldBodyPaint = Paint().apply {
            isAntiAlias = true
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textSize = 10f
            color = Color.rgb(30, 41, 59)
        }
        val mutedPaint = Paint().apply {
            isAntiAlias = true
            textSize = 8.5f
            color = Color.rgb(100, 116, 139)
        }

        // Header Background Banner
        val headerRect = Rect(0, 0, 595, 90)
        val headerBgPaint = Paint().apply {
            color = Color.rgb(15, 23, 42) // Dark Navy
        }
        canvas.drawRect(headerRect, headerBgPaint)

        // Accent line
        val accentPaint = Paint().apply {
            color = Color.rgb(59, 130, 246)
            strokeWidth = 3f
        }
        canvas.drawLine(0f, 90f, 595f, 90f, accentPaint)

        // Title text
        val headerTitlePaint = Paint().apply {
            isAntiAlias = true
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textSize = 18f
            color = Color.WHITE
        }
        val headerSubPaint = Paint().apply {
            isAntiAlias = true
            textSize = 10f
            color = Color.rgb(203, 213, 225)
        }
        canvas.drawText("CBSE CLASS 12 SCIENCE & MATHS COMPANION", 30f, 40f, headerTitlePaint)
        val sdf = SimpleDateFormat("dd MMMM yyyy, hh:mm a", Locale.getDefault())
        canvas.drawText("Personalized Academic Progress & Diagnostic Report • Generated on ${sdf.format(Date())}", 30f, 62f, headerSubPaint)

        var currentY = 115f

        // Overview Score Card
        val cardPaint = Paint().apply {
            color = Color.rgb(241, 245, 249)
            style = Paint.Style.FILL
        }
        canvas.drawRoundRect(30f, currentY, 565f, currentY + 65f, 8f, 8f, cardPaint)

        canvas.drawText("EXAM READINESS SCORE", 45f, currentY + 22f, subheadPaint)
        canvas.drawText("Average Mastery: $overallMastery%", 45f, currentY + 45f, boldBodyPaint)
        canvas.drawText("Mock Tests Attempted: ${testResults.size}", 230f, currentY + 45f, bodyPaint)
        val avgAccuracy = if (testResults.isNotEmpty()) testResults.map { it.accuracyPercentage }.average().toInt() else 0
        canvas.drawText("Avg Mock Accuracy: $avgAccuracy%", 400f, currentY + 45f, bodyPaint)

        currentY += 85f

        // Weak Areas & Priority Action Items
        canvas.drawText("1. PRIORITY AREAS NEEDING IMPROVEMENT", 30f, currentY, subheadPaint)
        currentY += 16f

        if (weakChapters.isEmpty()) {
            canvas.drawText("✓ Excellent! All chapters have achieved > 75% proficiency.", 35f, currentY, bodyPaint)
            currentY += 18f
        } else {
            for (ch in weakChapters.take(3)) {
                val warnBg = Paint().apply { color = Color.rgb(254, 242, 242) }
                canvas.drawRoundRect(30f, currentY - 12f, 565f, currentY + 20f, 6f, 6f, warnBg)
                canvas.drawText("• [${ch.subjectCode}] ${ch.title}", 40f, currentY + 4f, boldBodyPaint)
                canvas.drawText("Proficiency: ${ch.masteryPercentage}% | Board Weightage: ${ch.cbseWeightageMarks} Marks (Urgent Revision Required)", 40f, currentY + 16f, mutedPaint)
                currentY += 38f
            }
        }

        currentY += 10f

        // Chapter Breakdown Table
        canvas.drawText("2. SUBJECT-WISE CHAPTER MASTERY BREAKDOWN", 30f, currentY, subheadPaint)
        currentY += 18f

        // Table Header
        val thPaint = Paint().apply { color = Color.rgb(226, 232, 240) }
        canvas.drawRect(30f, currentY - 10f, 565f, currentY + 12f, thPaint)
        canvas.drawText("Subject", 40f, currentY + 5f, boldBodyPaint)
        canvas.drawText("Chapter Name", 100f, currentY + 5f, boldBodyPaint)
        canvas.drawText("Weightage", 360f, currentY + 5f, boldBodyPaint)
        canvas.drawText("Questions", 440f, currentY + 5f, boldBodyPaint)
        canvas.drawText("Mastery", 510f, currentY + 5f, boldBodyPaint)
        currentY += 22f

        for (ch in chapters.take(9)) {
            canvas.drawText(ch.subjectCode, 40f, currentY, bodyPaint)
            val truncatedTitle = if (ch.title.length > 38) ch.title.take(35) + "..." else ch.title
            canvas.drawText(truncatedTitle, 100f, currentY, bodyPaint)
            canvas.drawText("${ch.cbseWeightageMarks} Marks", 360f, currentY, bodyPaint)
            canvas.drawText("${ch.completedQuestionsCount}/${ch.totalQuestionsCount}", 440f, currentY, bodyPaint)
            canvas.drawText("${ch.masteryPercentage}%", 510f, currentY, boldBodyPaint)

            // subtle divider
            val divPaint = Paint().apply { color = Color.rgb(241, 245, 249); strokeWidth = 1f }
            canvas.drawLine(30f, currentY + 4f, 565f, currentY + 4f, divPaint)
            currentY += 18f
        }

        currentY += 10f

        // Recent Mock Tests History
        canvas.drawText("3. RECENT CBSE MOCK TESTS PERFORMANCE", 30f, currentY, subheadPaint)
        currentY += 16f

        if (testResults.isEmpty()) {
            canvas.drawText("No mock tests taken yet. Start with a 30-min unit test to gauge progress.", 35f, currentY, bodyPaint)
            currentY += 18f
        } else {
            for (test in testResults.take(3)) {
                canvas.drawText("• ${test.testTitle}", 40f, currentY, boldBodyPaint)
                canvas.drawText("Score: ${test.scoreMarks}/${test.maxMarks} (${test.accuracyPercentage}%) | Correct: ${test.correctAnswers}/${test.totalQuestions}", 40f, currentY + 12f, mutedPaint)
                currentY += 28f
            }
        }

        // Footer
        val footerPaint = Paint().apply {
            isAntiAlias = true
            textSize = 8.5f
            color = Color.rgb(148, 163, 184)
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText("Official CBSE Class 12 Science & Maths Study Companion • Consistent Practice Yields Board Excellence", 297.5f, 820f, footerPaint)

        pdfDocument.finishPage(page)

        // Save PDF file
        val reportsDir = File(context.cacheDir, "reports")
        if (!reportsDir.exists()) reportsDir.mkdirs()
        val pdfFile = File(reportsDir, "CBSE_12_Study_Report_${System.currentTimeMillis()}.pdf")

        try {
            val fos = FileOutputStream(pdfFile)
            pdfDocument.writeTo(fos)
            pdfDocument.close()
            fos.close()
            return pdfFile
        } catch (e: Exception) {
            e.printStackTrace()
            pdfDocument.close()
            return null
        }
    }

    fun sharePdf(context: Context, file: File) {
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, uri)
            putExtra(Intent.EXTRA_SUBJECT, "CBSE Class 12 Study & Analytics Report")
            putExtra(Intent.EXTRA_TEXT, "Here is my CBSE Class 12 personalized study analytics and progress report.")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, "Share CBSE 12 Study Report"))
    }
}
