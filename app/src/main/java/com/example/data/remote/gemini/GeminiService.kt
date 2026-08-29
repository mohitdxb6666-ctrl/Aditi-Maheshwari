package com.example.data.remote.gemini

import android.util.Log
import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

enum class GeminiModel(val modelId: String, val displayName: String) {
    FLASH("gemini-3.5-flash", "Gemini 3.5 Flash (General + Search Grounded)"),
    PRO_THINKING("gemini-3.1-pro-preview", "Gemini 3.1 Pro (High Thinking Mode)"),
    FLASH_LITE("gemini-3.1-flash-lite-preview", "Gemini 3.1 Flash Lite (Ultra Fast)")
}

class GeminiService {
    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

    suspend fun askGemini(
        prompt: String,
        model: GeminiModel = GeminiModel.FLASH,
        enableSearchGrounding: Boolean = false,
        systemInstructionText: String? = "You are an expert CBSE Class 12 Science and Mathematics educator. Provide comprehensive, accurate, step-by-step explanations according to the latest CBSE curriculum, marking schemes, formulas, derivations, and common exam pitfalls."
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            val apiKey = BuildConfig.GEMINI_API_KEY
            if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
                return@withContext Result.failure(
                    Exception("Gemini API Key is not configured in Secrets. Please add GEMINI_API_KEY to test live AI responses.")
                )
            }

            val requestJson = JSONObject()

            // Contents
            val contentsArray = JSONArray()
            val contentObj = JSONObject()
            val partsArray = JSONArray()
            val partObj = JSONObject()
            partObj.put("text", prompt)
            partsArray.put(partObj)
            contentObj.put("parts", partsArray)
            contentsArray.put(contentObj)
            requestJson.put("contents", contentsArray)

            // System Instruction
            if (!systemInstructionText.isNullOrBlank()) {
                val sysInstObj = JSONObject()
                val sysPartsArray = JSONArray()
                val sysPart = JSONObject()
                sysPart.put("text", systemInstructionText)
                sysPartsArray.put(sysPart)
                sysInstObj.put("parts", sysPartsArray)
                requestJson.put("systemInstruction", sysInstObj)
            }

            // Generation config
            val genConfig = JSONObject()
            if (model == GeminiModel.PRO_THINKING) {
                val thinkingConfig = JSONObject()
                thinkingConfig.put("thinkingLevel", "HIGH")
                genConfig.put("thinkingConfig", thinkingConfig)
            }
            genConfig.put("temperature", if (model == GeminiModel.PRO_THINKING) 0.2 else 0.7)
            requestJson.put("generationConfig", genConfig)

            // Tools (Search Grounding for gemini-3.5-flash if enabled)
            if (enableSearchGrounding && model == GeminiModel.FLASH) {
                val toolsArray = JSONArray()
                val toolObj = JSONObject()
                toolObj.put("googleSearch", JSONObject())
                toolsArray.put(toolObj)
                requestJson.put("tools", toolsArray)
            }

            val url = "https://generativelanguage.googleapis.com/v1beta/models/${model.modelId}:generateContent?key=$apiKey"
            val body = requestJson.toString().toRequestBody(jsonMediaType)
            val request = Request.Builder()
                .url(url)
                .post(body)
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string() ?: ""

            if (!response.isSuccessful) {
                Log.e("GeminiService", "Error ${response.code}: $responseBody")
                return@withContext Result.failure(Exception("API Error (${response.code}): $responseBody"))
            }

            val jsonResponse = JSONObject(responseBody)
            val candidates = jsonResponse.optJSONArray("candidates")
            if (candidates != null && candidates.length() > 0) {
                val candidate = candidates.getJSONObject(0)
                val content = candidate.optJSONObject("content")
                val parts = content?.optJSONArray("parts")
                val textBuilder = StringBuilder()
                if (parts != null) {
                    for (i in 0 until parts.length()) {
                        val part = parts.getJSONObject(i)
                        val text = part.optString("text", "")
                        textBuilder.append(text)
                    }
                }
                val resultText = textBuilder.toString().trim()
                if (resultText.isNotEmpty()) {
                    Result.success(resultText)
                } else {
                    Result.failure(Exception("Received empty response from Gemini."))
                }
            } else {
                Result.failure(Exception("No candidate responses returned."))
            }
        } catch (e: Exception) {
            Log.e("GeminiService", "Exception during Gemini call", e)
            Result.failure(e)
        }
    }
}
