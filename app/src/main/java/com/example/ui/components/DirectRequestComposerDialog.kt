package com.example.ui.components

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
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.SampleDataRepository
import com.example.model.*
import com.example.ui.theme.*
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DirectRequestComposerDialog(
  targetProfile: UserProfile,
  onDismiss: () -> Unit,
  onRequestSent: (String) -> Unit,
  onOpenUpgrade: () -> Unit
) {
  val membership by SampleDataRepository.membershipState.collectAsState()
  val currentUser by SampleDataRepository.currentUserProfile.collectAsState()

  var selectedCategory by remember {
    mutableStateOf(
      targetProfile.desireTags.firstOrNull() ?: DynamicKink.THREESOME_MFF
    )
  }
  var proposalTitle by remember {
    mutableStateOf("${selectedCategory.displayName} Proposition")
  }
  var description by remember {
    mutableStateOf("Hey ${targetProfile.name}! We love your profile. Would you be interested in exploring with us for an unforgettable evening?")
  }
  var venue by remember {
    mutableStateOf("Private Penthouse Suite")
  }
  var experienceLevel by remember {
    mutableStateOf("Experienced & Communicative")
  }

  val venueOptions = listOf(
    "Private Penthouse Suite",
    "Luxury Hotel Suite",
    "Our Dungeon / Playroom",
    "Alt Club Lounge (Drinks First)",
    "Private Residence"
  )

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      modifier = Modifier
        .fillMaxWidth(0.94f)
        .fillMaxHeight(0.92f)
        .clip(RoundedCornerShape(24.dp))
        .border(1.5.dp, Brush.verticalGradient(listOf(GoldVip, NeonMagenta)), RoundedCornerShape(24.dp)),
      color = DesireDarkSurface
    ) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(20.dp)
          .verticalScroll(rememberScrollState())
      ) {
        // Top Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(36.dp)
                .background(Brush.radialGradient(listOf(GoldVip, NeonMagenta)), CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.VpnKey, contentDescription = null, tint = DesireBlack, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "18+ DIRECT PROPOSAL",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                color = GoldVip
              )
              Text(
                text = "Propose to ${targetProfile.name}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
              )
            }
          }

          IconButton(
            onClick = onDismiss,
            modifier = Modifier
              .size(34.dp)
              .background(DesireCardSurface, CircleShape)
          ) {
            Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Check if user has the $15 Premium 18+ upgrade!
        if (!membership.canSendSexRequests) {
          // Locked State Banner
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(16.dp))
              .background(
                Brush.verticalGradient(
                  listOf(Color(0xFF381A05), Color(0xFF1E1005))
                )
              )
              .border(1.5.dp, GoldVip, RoundedCornerShape(16.dp))
              .padding(16.dp)
          ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Icon(
                Icons.Default.Lock,
                contentDescription = null,
                tint = GoldVip,
                modifier = Modifier.size(36.dp)
              )
              Spacer(modifier = Modifier.height(8.dp))
              Text(
                text = "Premium 18+ Upgrade Required",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = GoldVip
              )
              Spacer(modifier = Modifier.height(6.dp))
              Text(
                text = "Sending Direct Sex Requests and propositions requires the $15 Premium 18+ Upgrade.",
                fontSize = 13.sp,
                color = TextSecondary,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
              )
              Spacer(modifier = Modifier.height(14.dp))

              Button(
                onClick = onOpenUpgrade,
                colors = ButtonDefaults.buttonColors(containerColor = GoldVip),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
              ) {
                Text(
                  text = "UPGRADE TO PREMIUM ($15)",
                  fontWeight = FontWeight.Bold,
                  color = DesireBlack,
                  letterSpacing = 1.sp
                )
              }
            }
          }
          Spacer(modifier = Modifier.height(16.dp))
        }

        // Category Selector (Threesome, Foursome, Spit Roast, Orgy, Gangbang, Goth Emo date, etc.)
        Text(
          text = "CHOOSE FANTASY / DYNAMIC CATEGORY",
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.sp,
          color = TextSecondary
        )
        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          items(DynamicKink.values()) { kink ->
            val isSelected = kink == selectedCategory
            FilterChip(
              selected = isSelected,
              onClick = {
                selectedCategory = kink
                proposalTitle = "${kink.displayName} Proposition"
              },
              label = {
                Text(
                  text = kink.displayName,
                  fontSize = 12.sp,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
              },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = NeonMagenta,
                selectedLabelColor = Color.White,
                containerColor = DesireCardSurface,
                labelColor = TextSecondary
              ),
              border = FilterChipDefaults.filterChipBorder(
                borderColor = if (isSelected) NeonMagenta else DesireBorder,
                selectedBorderColor = NeonMagenta,
                enabled = true,
                selected = isSelected
              )
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Proposal Title
        Text(
          text = "PROPOSAL TITLE",
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.sp,
          color = TextSecondary
        )
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
          value = proposalTitle,
          onValueChange = { proposalTitle = it },
          modifier = Modifier.fillMaxWidth(),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = NeonMagenta,
            unfocusedBorderColor = DesireBorder,
            focusedContainerColor = DesireCardSurface,
            unfocusedContainerColor = DesireCardSurface,
            focusedTextColor = TextPrimary,
            unfocusedTextColor = TextPrimary
          ),
          shape = RoundedCornerShape(12.dp),
          singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Details / Fantasy note
        Text(
          text = "YOUR INTIMATE MESSAGE / FANTASY DETAILS",
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.sp,
          color = TextSecondary
        )
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
          value = description,
          onValueChange = { description = it },
          modifier = Modifier
            .fillMaxWidth()
            .height(110.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = NeonMagenta,
            unfocusedBorderColor = DesireBorder,
            focusedContainerColor = DesireCardSurface,
            unfocusedContainerColor = DesireCardSurface,
            focusedTextColor = TextPrimary,
            unfocusedTextColor = TextPrimary
          ),
          shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Venue
        Text(
          text = "SUGGESTED RENDEZVOUS VENUE",
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.sp,
          color = TextSecondary
        )
        Spacer(modifier = Modifier.height(6.dp))

        LazyRow(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          items(venueOptions) { option ->
            val isSelected = option == venue
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(if (isSelected) VelvetPurple else DesireCardSurface)
                .border(1.dp, if (isSelected) VelvetPurple else DesireBorder, RoundedCornerShape(10.dp))
                .clickable { venue = option }
                .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
              Text(
                text = option,
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) Color.White else TextSecondary
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Experience Level
        Text(
          text = "YOUR EXPERIENCE LEVEL",
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.sp,
          color = TextSecondary
        )
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
          value = experienceLevel,
          onValueChange = { experienceLevel = it },
          modifier = Modifier.fillMaxWidth(),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = NeonMagenta,
            unfocusedBorderColor = DesireBorder,
            focusedContainerColor = DesireCardSurface,
            unfocusedContainerColor = DesireCardSurface,
            focusedTextColor = TextPrimary,
            unfocusedTextColor = TextPrimary
          ),
          shape = RoundedCornerShape(12.dp),
          singleLine = true
        )

        Spacer(modifier = Modifier.height(22.dp))

        // Send Button
        Button(
          onClick = {
            if (!membership.canSendSexRequests) {
              onOpenUpgrade()
            } else {
              val newReq = DirectSexRequest(
                id = "req_${UUID.randomUUID().toString().take(6)}",
                fromUserId = currentUser.id,
                fromUserName = currentUser.name,
                toUserId = targetProfile.id,
                toUserName = targetProfile.name,
                category = selectedCategory,
                proposalTitle = proposalTitle,
                description = description,
                experienceLevel = experienceLevel,
                preferredVenue = venue,
                timeAgo = "Just now",
                status = RequestStatus.PENDING
              )
              SampleDataRepository.sendSexRequest(newReq)
              onRequestSent("Direct Request successfully sent to ${targetProfile.name}!")
              onDismiss()
            }
          },
          modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
          colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
          shape = RoundedCornerShape(16.dp),
          contentPadding = PaddingValues(0.dp)
        ) {
          Box(
            modifier = Modifier
              .fillMaxSize()
              .background(
                Brush.horizontalGradient(listOf(GoldVip, NeonMagenta)),
                RoundedCornerShape(16.dp)
              ),
            contentAlignment = Alignment.Center
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.Send, contentDescription = null, tint = DesireBlack)
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = if (membership.canSendSexRequests) "TRANSMIT PROPOSITION" else "UNLOCK 18+ TO SEND ($15)",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = DesireBlack,
                letterSpacing = 1.sp
              )
            }
          }
        }
      }
    }
  }
}
