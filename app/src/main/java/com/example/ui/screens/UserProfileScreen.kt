package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
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
import com.example.ui.theme.*

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun UserProfileScreen(
    userName: String = "Alex Vance",
    userAge: Int = 27,
    userBio: String = "Exploring alternative connections, deep conversations, and night life.",
    initialLifestyleTags: List<String> = listOf("Polyamorous", "Goth", "BDSM", "Alternative", "Queer", "Night Owl"),
    initialInterests: List<String> = listOf("Electronic Music", "Cyberpunk", "Art & Design", "Vinyl Records", "Tattoo Art", "Philosophy"),
    onSaveProfile: (String, String, List<String>, List<String>) -> Unit = { _, _, _, _ -> }
) {
    var name by remember { mutableStateOf(userName) }
    var age by remember { mutableStateOf(userAge.toString()) }
    var bio by remember { mutableStateOf(userBio) }
    
    var lifestyleTags by remember { mutableStateOf(initialLifestyleTags) }
    var interests by remember { mutableStateOf(initialInterests) }
    
    var newLifestyleTag by remember { mutableStateOf("") }
    var newInterest by remember { mutableStateOf("") }
    
    var isEditing by remember { mutableStateOf(false) }
    var successMessage by remember { mutableStateOf<String?>(null) }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DesireBlack)
            .verticalScroll(scrollState)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header Title
        Text(
            text = "USER PROFILE",
            fontSize = 24.sp,
            fontWeight = FontWeight.Black,
            color = NeonMagenta,
            letterSpacing = 2.sp,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Profile Photo Placeholder Section
        Box(
            modifier = Modifier
                .size(130.dp)
                .clip(CircleShape)
                .background(
                    Brush.verticalGradient(
                        listOf(DesireDarkSurface, DesireCardSurface)
                    )
                )
                .border(2.dp, NeonMagenta, CircleShape)
                .clickable {
                    // Photo placeholder tap action
                }
                .testTag("profile_photo_placeholder"),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "Upload Profile Photo",
                    tint = NeonCyan,
                    modifier = Modifier.size(36.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Add Photo",
                    color = TextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Name & Age / Status
        if (isEditing) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedBorderColor = NeonMagenta,
                    unfocusedBorderColor = DesireBorder
                ),
                modifier = Modifier.fillMaxWidth().testTag("edit_name_input")
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = age,
                onValueChange = { age = it },
                label = { Text("Age") },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedBorderColor = NeonMagenta,
                    unfocusedBorderColor = DesireBorder
                ),
                modifier = Modifier.fillMaxWidth().testTag("edit_age_input")
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = bio,
                onValueChange = { bio = it },
                label = { Text("Bio") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedBorderColor = NeonMagenta,
                    unfocusedBorderColor = DesireBorder
                ),
                modifier = Modifier.fillMaxWidth().testTag("edit_bio_input")
            )
        } else {
            Text(
                text = "$name, ${age.toIntOrNull() ?: 27}",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = bio,
                fontSize = 14.sp,
                color = TextSecondary,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Lifestyle Tags Section
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DesireCardSurface),
            modifier = Modifier.fillMaxWidth().testTag("lifestyle_tags_section")
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalFireDepartment,
                        contentDescription = "Lifestyle",
                        tint = NeonMagenta,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Lifestyle Tags",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))

                // Tags chips
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    lifestyleTags.forEach { tag ->
                        AssistChip(
                            onClick = {
                                if (isEditing) {
                                    lifestyleTags = lifestyleTags - tag
                                }
                            },
                            label = { Text(tag) },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = DesireDarkSurface,
                                labelColor = NeonCyan
                            ),
                            border = AssistChipDefaults.assistChipBorder(
                                enabled = true,
                                borderColor = NeonCyan.copy(alpha = 0.5f)
                            ),
                            trailingIcon = if (isEditing) {
                                { Icon(Icons.Default.Add, contentDescription = "Remove", tint = NeonCyan, modifier = Modifier.size(16.dp)) }
                            } else null
                        )
                    }
                }

                if (isEditing) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = newLifestyleTag,
                            onValueChange = { newLifestyleTag = it },
                            placeholder = { Text("Add tag (e.g. BDSM)") },
                            singleLine = true,
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary,
                                focusedBorderColor = NeonCyan,
                                unfocusedBorderColor = DesireBorder
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                if (newLifestyleTag.isNotBlank() && !lifestyleTags.contains(newLifestyleTag.trim())) {
                                    lifestyleTags = lifestyleTags + newLifestyleTag.trim()
                                    newLifestyleTag = ""
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = NeonCyan)
                        ) {
                            Text("Add", color = DesireBlack, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Interests Section
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DesireCardSurface),
            modifier = Modifier.fillMaxWidth().testTag("interests_section")
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Interests",
                        tint = NeonMagenta,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Interests & Passions",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    interests.forEach { interest ->
                        FilterChip(
                            selected = true,
                            onClick = {
                                if (isEditing) {
                                    interests = interests - interest
                                }
                            },
                            label = { Text(interest) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = NeonMagenta.copy(alpha = 0.2f),
                                selectedLabelColor = NeonMagenta
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = true,
                                borderColor = NeonMagenta
                            )
                        )
                    }
                }

                if (isEditing) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = newInterest,
                            onValueChange = { newInterest = it },
                            placeholder = { Text("Add interest...") },
                            singleLine = true,
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary,
                                focusedBorderColor = NeonMagenta,
                                unfocusedBorderColor = DesireBorder
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                if (newInterest.isNotBlank() && !interests.contains(newInterest.trim())) {
                                    interests = interests + newInterest.trim()
                                    newInterest = ""
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = NeonMagenta)
                        ) {
                            Text("Add", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        if (successMessage != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = successMessage!!,
                color = SuccessGreen,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Edit / Save Action Button
        Button(
            onClick = {
                if (isEditing) {
                    onSaveProfile(name, bio, lifestyleTags, interests)
                    successMessage = "Profile updated successfully!"
                } else {
                    successMessage = null
                }
                isEditing = !isEditing
            },
            colors = ButtonDefaults.buttonColors(containerColor = NeonMagenta),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("edit_save_profile_button")
        ) {
            Icon(
                imageVector = if (isEditing) Icons.Default.CheckCircle else Icons.Default.Edit,
                contentDescription = null,
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (isEditing) "Save Profile" else "Edit Profile",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}
