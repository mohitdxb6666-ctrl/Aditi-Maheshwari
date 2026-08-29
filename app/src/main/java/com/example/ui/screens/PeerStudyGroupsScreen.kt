package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SubjectType
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.ScreenDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PeerStudyGroupsScreen(viewModel: MainViewModel) {
    val allDiscussions by viewModel.allDiscussions.collectAsState()
    var selectedSubjectFilter by remember { mutableStateOf("ALL") }
    var showNewPostDialog by remember { mutableStateOf(false) }

    val filteredDiscussions = remember(allDiscussions, selectedSubjectFilter) {
        if (selectedSubjectFilter == "ALL") allDiscussions
        else allDiscussions.filter { it.subjectCode == selectedSubjectFilter }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Peer Study Circles", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("Collaborate & Share CBSE Class 12 Tricks", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.navigateTo(ScreenDestination.Home) },
                        modifier = Modifier.testTag("peer_groups_back_btn")
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showNewPostDialog = true },
                containerColor = PrimaryBlue,
                contentColor = Color.White,
                icon = { Icon(Icons.Default.AddComment, contentDescription = "Post Discussion") },
                text = { Text("Ask Group / Share Note", fontWeight = FontWeight.Bold) },
                modifier = Modifier.testTag("new_post_fab")
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .testTag("peer_groups_container")
        ) {
            // Subject Filter Chips
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    FilterChip(
                        selected = selectedSubjectFilter == "ALL",
                        onClick = { selectedSubjectFilter = "ALL" },
                        label = { Text("All Groups") }
                    )
                }
                items(SubjectType.values().toList()) { sub ->
                    FilterChip(
                        selected = selectedSubjectFilter == sub.code,
                        onClick = { selectedSubjectFilter = sub.code },
                        label = { Text(sub.displayName) }
                    )
                }
            }

            // Discussions List
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(filteredDiscussions) { discussion ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("discussion_item_${discussion.id}"),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        shape = CircleShape,
                                        color = PrimaryBlue.copy(alpha = 0.15f),
                                        modifier = Modifier.size(34.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(
                                                text = discussion.authorAvatarBadge,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 12.sp,
                                                color = PrimaryBlue
                                            )
                                        }
                                    }

                                    Column {
                                        Text(discussion.authorName, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                        Text(discussion.groupName, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }

                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = if (discussion.isSolved) AccentEmerald.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant
                                ) {
                                    Text(
                                        text = if (discussion.isSolved) "✓ Solved" else "[${discussion.subjectCode}]",
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (discussion.isSolved) AccentEmerald else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Text(
                                text = discussion.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                lineHeight = 20.sp
                            )

                            Text(
                                text = discussion.questionOrNote,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 18.sp
                            )

                            HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable { viewModel.upvotePost(discussion.id) }
                                        .padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        Icons.Default.ThumbUp,
                                        contentDescription = "Upvote",
                                        tint = PrimaryBlue,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = "${discussion.upvotes} Helpful",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = PrimaryBlue
                                    )
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        Icons.Default.ChatBubbleOutline,
                                        contentDescription = "Replies",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = "${discussion.repliesCount} peer answers",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showNewPostDialog) {
        var postSubject by remember { mutableStateOf("MATH") }
        var postGroup by remember { mutableStateOf("Calculus & 3D Geometry Club") }
        var postTitle by remember { mutableStateOf("") }
        var postContent by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showNewPostDialog = false },
            title = { Text("Post to Study Circle", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Select Subject:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(SubjectType.values().toList()) { sub ->
                            FilterChip(
                                selected = postSubject == sub.code,
                                onClick = {
                                    postSubject = sub.code
                                    postGroup = "${sub.displayName} Board Circle"
                                },
                                label = { Text(sub.displayName, fontSize = 11.sp) }
                            )
                        }
                    }

                    OutlinedTextField(
                        value = postTitle,
                        onValueChange = { postTitle = it },
                        label = { Text("Title or Short Doubt") },
                        placeholder = { Text("e.g. Shortcut for finding inverse of 3x3 matrix") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = postContent,
                        onValueChange = { postContent = it },
                        label = { Text("Description / Question Details") },
                        placeholder = { Text("Add formula steps or ask for clarification...") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (postTitle.isNotBlank() && postContent.isNotBlank()) {
                            viewModel.postNewDiscussion(postSubject, postGroup, postTitle, postContent)
                            showNewPostDialog = false
                        }
                    },
                    enabled = postTitle.isNotBlank() && postContent.isNotBlank()
                ) {
                    Text("Post to Group")
                }
            },
            dismissButton = {
                TextButton(onClick = { showNewPostDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
