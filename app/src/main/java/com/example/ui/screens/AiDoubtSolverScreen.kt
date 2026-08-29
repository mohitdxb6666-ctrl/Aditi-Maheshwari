package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import com.example.ui.components.FrostedGlassCard
import com.example.ui.components.FrostedPillBadge
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.ScreenDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiDoubtSolverScreen(viewModel: MainViewModel) {
    val messages by viewModel.doubtMessages.collectAsState()
    val isLoading by viewModel.isAiLoading.collectAsState()
    val selectedModel by viewModel.selectedGeminiModel.collectAsState()
    val isDark = isSystemInDarkTheme()

    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    val quickChips = listOf(
        "Derive Lens Maker's Formula step-by-step",
        "Explain Aldol Condensation mechanism",
        "Evaluate ∫ (sin x / (sin x + cos x)) dx from 0 to π/2",
        "Explain Lac Operon gene regulation",
        "Top 5 high-weightage Physics questions for CBSE 12"
    )

    LaunchedEffect(messages.size, isLoading) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                "AI Doubt Solver",
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp,
                                color = if (isDark) Color.White else Slate900
                            )
                            FrostedPillBadge(
                                text = if (selectedModel == GeminiModel.PRO_THINKING) "THINKING PRO" else "FAST AI",
                                accentColor = NeonBlue
                            )
                        }
                        Text(
                            "CBSE 12 Science & Maths AI Assistant",
                            fontSize = 11.sp,
                            color = if (isDark) Slate400 else Slate600
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.navigateTo(ScreenDestination.Home) },
                        modifier = Modifier.testTag("ai_doubt_back_btn")
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
                        onClick = { viewModel.clearDoubtChat() },
                        modifier = Modifier.testTag("clear_chat_btn")
                    ) {
                        Icon(
                            Icons.Default.DeleteOutline,
                            contentDescription = "Clear Chat",
                            tint = if (isDark) Slate400 else Slate600
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
                .testTag("ai_doubt_chat_container")
        ) {
            // Model Selector in Frosted Glass Card
            FrostedGlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 6.dp),
                shape = RoundedCornerShape(18.dp),
                backgroundColor = if (isDark) Color(0x14FFFFFF) else Color(0xD9FFFFFF),
                borderColor = if (isDark) Color(0x26FFFFFF) else Color(0x1F0F172A)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        "AI Reasoning Engine:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) Slate300 else Slate600
                    )
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
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = NeonBlue.copy(alpha = 0.25f),
                                selectedLabelColor = NeonBlue
                            )
                        )

                        FilterChip(
                            selected = selectedModel == GeminiModel.FLASH,
                            onClick = { viewModel.setGeminiModel(GeminiModel.FLASH) },
                            label = { Text("Flash (Search)", fontSize = 11.sp) },
                            leadingIcon = {
                                Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(14.dp))
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = NeonIndigo.copy(alpha = 0.25f),
                                selectedLabelColor = NeonIndigo
                            )
                        )

                        FilterChip(
                            selected = selectedModel == GeminiModel.FLASH_LITE,
                            onClick = { viewModel.setGeminiModel(GeminiModel.FLASH_LITE) },
                            label = { Text("Lite", fontSize = 11.sp) },
                            leadingIcon = {
                                Icon(Icons.Default.Bolt, contentDescription = null, modifier = Modifier.size(14.dp))
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = NeonEmerald.copy(alpha = 0.25f),
                                selectedLabelColor = NeonEmerald
                            )
                        )
                    }
                }
            }

            // Quick Prompt Chips
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(quickChips) { chip ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isDark) Color(0x1AFFFFFF) else Color(0xB3FFFFFF))
                            .border(BorderStroke(1.dp, if (isDark) Color(0x26FFFFFF) else Color(0x1F0F172A)), RoundedCornerShape(20.dp))
                            .clickable { viewModel.sendDoubt(chip) }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            chip,
                            fontSize = 11.sp,
                            maxLines = 1,
                            color = if (isDark) Slate200 else Slate700
                        )
                    }
                }
            }

            // Chat Messages List
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(messages) { msg ->
                    val isUser = msg.sender == "user"

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
                    ) {
                        if (!isUser) {
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(GlassPinkOrangeGradient),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.Psychology,
                                    contentDescription = "AI",
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                        }

                        FrostedGlassCard(
                            shape = RoundedCornerShape(
                                topStart = 18.dp,
                                topEnd = 18.dp,
                                bottomStart = if (isUser) 18.dp else 4.dp,
                                bottomEnd = if (isUser) 4.dp else 18.dp
                            ),
                            backgroundColor = if (isUser) {
                                NeonBlue.copy(alpha = 0.35f)
                            } else {
                                if (isDark) Color(0x1AFFFFFF) else Color(0xE6FFFFFF)
                            },
                            borderColor = if (isUser) NeonBlue.copy(alpha = 0.6f) else (if (isDark) Color(0x33FFFFFF) else Color(0x1F0F172A)),
                            modifier = Modifier.widthIn(max = 300.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                if (!isUser && msg.isThinkingResponse) {
                                    FrostedPillBadge(text = "🧠 DEEP STEP REASONING", accentColor = NeonCyan)
                                }

                                Text(
                                    text = msg.messageText,
                                    color = if (isUser) Color.White else (if (isDark) Color.White else Slate900),
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
                            modifier = Modifier.padding(start = 42.dp, top = 4.dp)
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp,
                                color = NeonBlue
                            )
                            Text(
                                text = if (selectedModel == GeminiModel.PRO_THINKING) "Reasoning step-by-step with high thinking..." else "Consulting CBSE syllabus...",
                                fontSize = 12.sp,
                                color = if (isDark) Slate400 else Slate600
                            )
                        }
                    }
                }
            }

            // Frosted Input Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(if (isDark) Color(0x1A000000) else Color(0x40FFFFFF))
                    .border(BorderStroke(1.dp, if (isDark) Color(0x1AFFFFFF) else Color(0x1A000000)))
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        placeholder = { Text("Ask any science/maths doubt or derivation...", fontSize = 12.sp) },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("ai_doubt_input_field"),
                        shape = RoundedCornerShape(24.dp),
                        maxLines = 4,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonBlue,
                            unfocusedBorderColor = if (isDark) Color(0x33FFFFFF) else Color(0x33000000),
                            focusedContainerColor = if (isDark) Color(0x14FFFFFF) else Color.White,
                            unfocusedContainerColor = if (isDark) Color(0x0AFFFFFF) else Color.White
                        )
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
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(if (inputText.isNotBlank()) NeonBlue else (if (isDark) Color(0x26FFFFFF) else Color(0x26000000)))
                            .testTag("send_doubt_btn")
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send",
                            tint = if (inputText.isNotBlank()) Color.White else (if (isDark) Slate500 else Slate400),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

