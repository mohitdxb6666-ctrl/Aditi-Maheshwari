package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
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
import com.example.data.remote.gemini.GeminiModel
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.ScreenDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiDoubtSolverScreen(viewModel: MainViewModel) {
    val messages by viewModel.doubtMessages.collectAsState()
    val isLoading by viewModel.isAiLoading.collectAsState()
    val selectedModel by viewModel.selectedGeminiModel.collectAsState()
    val isSearchGrounding by viewModel.enableSearchGrounding.collectAsState()

    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    val quickChips = listOf(
        "Derive Lens Maker's Formula step-by-step",
        "Explain Aldol Condensation mechanism",
        "Evaluate ∫ (sin x / (sin x + cos x)) dx from 0 to π/2",
        "Explain Lac Operon gene regulation",
        "What are top 5 high-weightage Physics questions for CBSE 12?"
    )

    LaunchedEffect(messages.size, isLoading) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text("AI Doubt Solver", fontWeight = FontWeight.Bold, fontSize = 17.sp)
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = AccentCyan.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = if (selectedModel == GeminiModel.PRO_THINKING) "High Thinking" else "Fast AI",
                                    color = AccentCyan,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text("CBSE 12 Science & Maths AI Assistant", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.navigateTo(ScreenDestination.Home) },
                        modifier = Modifier.testTag("ai_doubt_back_btn")
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = { viewModel.clearDoubtChat() },
                        modifier = Modifier.testTag("clear_chat_btn")
                    ) {
                        Icon(Icons.Default.DeleteOutline, contentDescription = "Clear Chat")
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
                .testTag("ai_doubt_chat_container")
        ) {
            // Model Selector Bar
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Model & Reasoning Engine:", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        FilterChip(
                            selected = selectedModel == GeminiModel.PRO_THINKING,
                            onClick = { viewModel.setGeminiModel(GeminiModel.PRO_THINKING) },
                            label = { Text("Pro (Thinking)", fontSize = 11.sp) },
                            leadingIcon = {
                                Icon(Icons.Default.Psychology, contentDescription = null, modifier = Modifier.size(14.dp))
                            }
                        )

                        FilterChip(
                            selected = selectedModel == GeminiModel.FLASH,
                            onClick = { viewModel.setGeminiModel(GeminiModel.FLASH) },
                            label = { Text("Flash (Search)", fontSize = 11.sp) },
                            leadingIcon = {
                                Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(14.dp))
                            }
                        )

                        FilterChip(
                            selected = selectedModel == GeminiModel.FLASH_LITE,
                            onClick = { viewModel.setGeminiModel(GeminiModel.FLASH_LITE) },
                            label = { Text("Lite", fontSize = 11.sp) },
                            leadingIcon = {
                                Icon(Icons.Default.Bolt, contentDescription = null, modifier = Modifier.size(14.dp))
                            }
                        )
                    }
                }
            }

            // Quick Prompt Chips
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(quickChips) { chip ->
                    SuggestionChip(
                        onClick = {
                            viewModel.sendDoubt(chip)
                        },
                        label = { Text(chip, fontSize = 11.sp, maxLines = 1) },
                        shape = RoundedCornerShape(8.dp)
                    )
                }
            }

            // Chat Messages List
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(messages) { msg ->
                    val isUser = msg.sender == "user"

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
                    ) {
                        if (!isUser) {
                            Surface(
                                shape = CircleShape,
                                color = PrimaryBlue.copy(alpha = 0.15f),
                                modifier = Modifier
                                    .size(32.dp)
                                    .align(Alignment.Top)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        Icons.Default.AutoAwesome,
                                        contentDescription = "AI",
                                        tint = PrimaryBlue,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                        }

                        Card(
                            shape = RoundedCornerShape(
                                topStart = 16.dp,
                                topEnd = 16.dp,
                                bottomStart = if (isUser) 16.dp else 4.dp,
                                bottomEnd = if (isUser) 4.dp else 16.dp
                            ),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isUser) PrimaryBlue else MaterialTheme.colorScheme.surfaceVariant
                            ),
                            modifier = Modifier.widthIn(max = 300.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                if (!isUser && msg.isThinkingResponse) {
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        color = AccentCyan.copy(alpha = 0.15f)
                                    ) {
                                        Text(
                                            text = "🧠 High Thinking Deep Solution",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = AccentCyan,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }

                                Text(
                                    text = msg.messageText,
                                    color = if (isUser) Color.White else MaterialTheme.colorScheme.onSurface,
                                    fontSize = 13.sp,
                                    lineHeight = 19.sp
                                )
                            }
                        }
                    }
                }

                if (isLoading) {
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(start = 40.dp, top = 4.dp)
                        ) {
                            CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                            Text(
                                text = if (selectedModel == GeminiModel.PRO_THINKING) "Reasoning step-by-step with high thinking..." else "Consulting CBSE syllabus...",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Input Bar
            Surface(
                tonalElevation = 3.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        placeholder = { Text("Ask any science/maths doubt or derivation...", fontSize = 13.sp) },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("ai_doubt_input_field"),
                        shape = RoundedCornerShape(24.dp),
                        maxLines = 4
                    )

                    IconButton(
                        onClick = {
                            if (inputText.isNotBlank()) {
                                val textToSend = inputText
                                inputText = ""
                                viewModel.sendDoubt(textToSend)
                            }
                        },
                        enabled = inputText.isNotBlank() && !isLoading,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(if (inputText.isNotBlank()) PrimaryBlue else MaterialTheme.colorScheme.surfaceVariant)
                            .testTag("send_doubt_btn")
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send",
                            tint = if (inputText.isNotBlank()) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
