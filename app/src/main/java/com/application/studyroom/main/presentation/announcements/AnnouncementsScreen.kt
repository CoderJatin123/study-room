package com.application.studyroom.main.presentation.announcements

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.application.studyroom.R
import com.application.studyroom.ui.StudyRoomTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnnouncementsScreen(
    announcements: List<Announcement>,
    roomName: String,
    paddingValues: PaddingValues
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(paddingValues)
    ) {

        // Announcements List
        if (announcements.isEmpty()) {
            EmptyAnnouncementsState()
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(announcements) { announcement ->
                    AnnouncementCard(announcement = announcement)
                }
            }
        }
    }
}

@Composable
fun AnnouncementCard(
    announcement: Announcement,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 2.dp),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.2.dp, color = colorResource(R.color.green_dark)),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header with admin info and date
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Admin Avatar
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            color = Color(0xFF2E7D32).copy(alpha = 0.1f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (!announcement.adminPhotoUrl.isNullOrEmpty()) {
                        AsyncImage(
                            model = announcement.adminPhotoUrl,
                            contentDescription = "Admin photo",
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        // Fallback to initials or icon
                        Text(
                            text = announcement.adminName?.take(1)?.uppercase() ?: "A",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color(0xFF2E7D32),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Admin name and date
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = announcement.adminName ?: "Admin",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = formatDate(announcement.createdAt),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // More options
                IconButton(
                    onClick = { /* Handle more options */ },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More options",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Announcement title
            if (!announcement.title.isNullOrEmpty()) {
                Text(
                    text = announcement.title,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))
            }

            // Announcement description
            if (!announcement.description.isNullOrEmpty()) {
                Text(
                    text = announcement.description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 22.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Attachments (if any)
            if (!announcement.attachments.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                AttachmentsSection(attachments = announcement.attachments)
            }
        }
    }
}

@Composable
fun AttachmentsSection(
    attachments: List<Attachment>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Attachments",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium
        )

        attachments.forEach { attachment ->
            AttachmentItem(attachment = attachment)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AnnouncementsScreenPreview() {
    StudyRoomTheme {
        val list = listOf<Announcement>(
            Announcement(
                id = "1",
                title = "Welcome to StudyRoom!",
                description = "Hello everyone! Welcome to our new StudyRoom. This is where we'll share important updates, assignments, and resources throughout the semester. Please make sure to check this space regularly for announcements.",
                adminName = "Dr. Sarah Johnson",
                adminPhotoUrl = null,
                createdAt = System.currentTimeMillis() - 3600000, // 1 hour ago
                attachments = null
            ),
            Announcement(
                id = "2",
                title = "Assignment 1 Due Date Extended",
                description = "Due to the technical issues we experienced last week, I'm extending the deadline for Assignment 1 to Friday, March 15th at 11:59 PM. Please submit your work through the portal.",
                adminName = "Prof. Michael Chen",
                adminPhotoUrl = null,
                createdAt = System.currentTimeMillis() - 86400000, // 1 day ago
                attachments = null
            ),
            Announcement(
                id = "3",
                title = "Mid-term Exam Schedule",
                description = "The mid-term examination will be held on March 20th, 2024 from 2:00 PM to 4:00 PM in Room 301. Please bring your student ID and calculator. No mobile phones allowed during the exam.",
                adminName = "Dr. Emily Rodriguez",
                adminPhotoUrl = null,
                createdAt = System.currentTimeMillis() - 172800000, // 2 days ago
                attachments = null
            ),
            Announcement(
                id = "4",
                title = "Group Project Guidelines",
                description = "For the upcoming group project, teams should consist of 4-5 members. Each team needs to select a topic from the provided list and submit their proposal by March 10th. Detailed guidelines are available in the course materials.",
                adminName = "Prof. David Wilson",
                adminPhotoUrl = null,
                createdAt = System.currentTimeMillis() - 259200000, // 3 days ago
                attachments = null
            ),
            Announcement(
                id = "5",
                title = "Library Workshop Next Week",
                description = "There will be a special workshop on research methodologies next Tuesday at 3:00 PM in the main library. This is optional but highly recommended for all students working on their final projects.",
                adminName = "Dr. Lisa Thompson",
                adminPhotoUrl = null,
                createdAt = System.currentTimeMillis() - 432000000, // 5 days ago
                attachments = null
            ),
            Announcement(
                id = "6",
                title = "Class Cancelled - March 8th",
                description = "Due to unforeseen circumstances, tomorrow's class (March 8th) is cancelled. We will resume our regular schedule on Monday. Please review Chapter 5 before our next meeting.",
                adminName = "Prof. James Anderson",
                adminPhotoUrl = null,
                createdAt = System.currentTimeMillis() - 518400000, // 6 days ago
                attachments = null
            ),
            Announcement(
                id = "7",
                title = "Extra Credit Opportunity",
                description = "Students interested in earning extra credit can participate in the upcoming science fair. Submit your project proposal by March 25th. This is worth up to 5% additional marks toward your final grade.",
                adminName = "Dr. Rachel Green",
                adminPhotoUrl = null,
                createdAt = System.currentTimeMillis() - 604800000, // 1 week ago
                attachments = null
            )
        )
        AnnouncementsScreen(list,"MyRoom", paddingValues = PaddingValues())
    }
}


@Composable
fun AttachmentItem(
    attachment: Attachment,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF2E7D32).copy(alpha = 0.05f)
        ),
        onClick = { /* Handle attachment click */ }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
//            Icon(
//
//                painter = painterResource(getAttachmentIcon(attachment.type)),
//                contentDescription = "Attachment",
//                tint = Color(0xFF2E7D32),
//                modifier = Modifier.size(24.dp)
//            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = attachment.name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                if (attachment.size != null) {
                    Text(
                        text = formatFileSize(attachment.size),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Open",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun EmptyAnnouncementsState(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(R.drawable.icom_door_open), // You'll need this icon
            contentDescription = "No announcements",
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(64.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "No announcements yet",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Check back later for updates from your instructor",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

// Data classes
data class Announcement(
    val id: String,
    val title: String?,
    val description: String?,
    val adminName: String?,
    val adminPhotoUrl: String?,
    val createdAt: Long,
    val attachments: List<Attachment>?
)

data class Attachment(
    val id: String,
    val name: String,
    val url: String,
    val type: String,
    val size: Long?
)

// Helper functions
fun formatDate(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp

    return when {
        diff < 60_000 -> "Just now"
        diff < 3600_000 -> "${diff / 60_000}m ago"
        diff < 86400_000 -> "${diff / 3600_000}h ago"
        diff < 604800_000 -> "${diff / 86400_000}d ago"
        else -> {
            val formatter = SimpleDateFormat("MMM dd", Locale.getDefault())
            formatter.format(Date(timestamp))
        }
    }
}

fun formatFileSize(bytes: Long): String {
    val kb = bytes / 1024.0
    val mb = kb / 1024.0

    return when {
        mb >= 1 -> String.format("%.1f MB", mb)
        kb >= 1 -> String.format("%.1f KB", kb)
        else -> "$bytes B"
    }
}

//fun getAttachmentIcon(type: String): Int {
//    return when (type.lowercase()) {
//        "pdf" -> R.drawable.ic_pdf
//        "doc", "docx" -> R.drawable.ic_document
//        "jpg", "jpeg", "png", "gif" -> R.drawable.ic_image
//        "mp4", "avi", "mkv" -> R.drawable.ic_video
//        "mp3", "wav" -> R.drawable.ic_audio
//        else -> R.drawable.ic_file
//    }
//}