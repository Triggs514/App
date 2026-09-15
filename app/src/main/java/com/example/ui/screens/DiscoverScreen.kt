package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.SampleDataRepository
import com.example.model.*
import com.example.ui.components.MatchingPreferencesDialog
import com.example.ui.theme.*

@Composable
fun DiscoverScreen(
  profiles: List<UserProfile>,
  membership: MembershipState,
  onProfileClicked: (UserProfile) -> Unit,
  onSendRequestClicked: (UserProfile) -> Unit,
  onOpenUpgrade: () -> Unit,
  showFilterDialog: Boolean,
  onDismissFilterDialog: () -> Unit,
  onChatClicked: (UserProfile) -> Unit = {}
) {
  val matchPrefs by SampleDataRepository.matchPreferences.collectAsState()
  var showAlgorithmDialog by remember { mutableStateOf(false) }

  var selectedTypeFilter by remember { mutableStateOf<UserType?>(null) }
  var selectedKinkFilter by remember { mutableStateOf<DynamicKink?>(null) }
  var selectedSubcultureFilter by remember { mutableStateOf<SubcultureType?>(null) }
  var sortByAlgorithmScore by remember { mutableStateOf(true) }

  var likedProfileIds by remember { mutableStateOf(setOf<String>()) }
  var matchToastMessage by remember { mutableStateOf<String?>(null) }

  // Compute match results for each profile (filtering unverified fake profiles and enforcing 18-40 limits)
  val profileMatchResults = remember(profiles, matchPrefs, sortByAlgorithmScore, selectedTypeFilter, selectedKinkFilter, selectedSubcultureFilter) {
    val scoredList = profiles.map { prof ->
      SampleDataRepository.calculateMatchScore(prof, matchPrefs)
    }

    val filtered = scoredList.filter { result ->
      val matchesType = selectedTypeFilter == null || result.profile.userType == selectedTypeFilter
      val matchesKink = selectedKinkFilter == null || result.profile.desireTags.contains(selectedKinkFilter)
      val matchesSubculture = selectedSubcultureFilter == null || result.profile.subcultureTags.contains(selectedSubcultureFilter)
      matchesType && matchesKink && matchesSubculture && result.compatibilityPercentage > 0
    }

    if (sortByAlgorithmScore) {
      filtered.sortedByDescending { it.compatibilityPercentage }
    } else {
      filtered
    }
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(DesireBlack)
  ) {
    // Matching Algorithm Header & Tuner Bar
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .background(
          Brush.verticalGradient(
            listOf(Color(0xFF22082E), DesireBlack)
          )
        )
        .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              Icons.Default.AutoAwesome,
              contentDescription = null,
              tint = NeonCyan,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "DESIRE MATCH RADAR",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              letterSpacing = 1.sp,
              color = NeonCyan
            )
          }
          Text(
            text = if (sortByAlgorithmScore) "Ranked by Compatibility" else "All Recent Connections",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
          )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          // Tune Algorithm Button
          Button(
            onClick = { showAlgorithmDialog = true },
            colors = ButtonDefaults.buttonColors(containerColor = DesireCardSurfaceElevated),
            shape = RoundedCornerShape(10.dp),
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
          ) {
            Icon(Icons.Default.Tune, contentDescription = null, tint = NeonMagenta, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Tune Algorithm", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
          }
        }
      }
    }

    // Zero Fake Profiles & Unlimited Member Chat Banner
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 6.dp),
      shape = RoundedCornerShape(12.dp),
      colors = CardDefaults.cardColors(containerColor = Color(0xFF132219)),
      border = androidx.compose.foundation.BorderStroke(1.dp, SuccessGreen.copy(alpha = 0.6f))
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text("🛡️", fontSize = 16.sp)
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = "ZERO FAKE PROFILES • 100% REAL MEMBERS",
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold,
              letterSpacing = 0.5.sp,
              color = SuccessGreen
            )
            Spacer(modifier = Modifier.width(6.dp))
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(NeonCyan.copy(alpha = 0.2f))
                .padding(horizontal = 4.dp, vertical = 1.dp)
            ) {
              Text("AGES 18–40", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = NeonCyan)
            }
          }
          Text(
            text = "Strict age limits 18–40 • Free unlimited member-to-member chat active for all profiles",
            fontSize = 10.sp,
            color = TextSecondary
          )
        }
      }
    }

    // Subculture & Dynamic Quick-Filter Chips
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .background(DesireDarkSurface.copy(alpha = 0.5f))
        .padding(vertical = 8.dp)
    ) {
      // Row 1: Subculture Highlights (Goth, Emo, Alt)
      LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        item {
          FilterChip(
            selected = selectedSubcultureFilter == null && selectedKinkFilter == null && selectedTypeFilter == null,
            onClick = {
              selectedSubcultureFilter = null
              selectedKinkFilter = null
              selectedTypeFilter = null
            },
            label = { Text("All Matches", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = NeonMagenta,
              selectedLabelColor = Color.White,
              containerColor = DesireCardSurface,
              labelColor = TextSecondary
            ),
            border = FilterChipDefaults.filterChipBorder(
              enabled = true,
              selected = selectedSubcultureFilter == null && selectedKinkFilter == null && selectedTypeFilter == null,
              borderColor = DesireBorder,
              selectedBorderColor = NeonMagenta
            )
          )
        }

        // Subculture: Goth & Emo
        item {
          FilterChip(
            selected = selectedSubcultureFilter == SubcultureType.GOTH,
            onClick = {
              selectedSubcultureFilter = if (selectedSubcultureFilter == SubcultureType.GOTH) null else SubcultureType.GOTH
            },
            label = { Text("🖤 Date a Goth", fontSize = 11.sp) },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = Color(0xFF4361EE),
              selectedLabelColor = Color.White,
              containerColor = DesireCardSurface,
              labelColor = TextPrimary
            ),
            border = FilterChipDefaults.filterChipBorder(
              enabled = true,
              selected = selectedSubcultureFilter == SubcultureType.GOTH,
              borderColor = DesireBorder,
              selectedBorderColor = Color(0xFF4361EE)
            )
          )
        }

        item {
          FilterChip(
            selected = selectedSubcultureFilter == SubcultureType.EMO,
            onClick = {
              selectedSubcultureFilter = if (selectedSubcultureFilter == SubcultureType.EMO) null else SubcultureType.EMO
            },
            label = { Text("🥀 Emo Dates", fontSize = 11.sp) },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = Color(0xFF7209B7),
              selectedLabelColor = Color.White,
              containerColor = DesireCardSurface,
              labelColor = TextPrimary
            ),
            border = FilterChipDefaults.filterChipBorder(
              enabled = true,
              selected = selectedSubcultureFilter == SubcultureType.EMO,
              borderColor = DesireBorder,
              selectedBorderColor = Color(0xFF7209B7)
            )
          )
        }

        // Sexual Likes & Dynamics: Creampie, Breeding, Spit Roasting, Threesomes, etc.
        item {
          FilterChip(
            selected = selectedKinkFilter == DynamicKink.CREAMPIE,
            onClick = {
              selectedKinkFilter = if (selectedKinkFilter == DynamicKink.CREAMPIE) null else DynamicKink.CREAMPIE
            },
            label = { Text("💦 Creampie", fontSize = 11.sp) },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = NeonPinkLight,
              selectedLabelColor = Color.White,
              containerColor = DesireCardSurface,
              labelColor = TextPrimary
            ),
            border = FilterChipDefaults.filterChipBorder(
              enabled = true,
              selected = selectedKinkFilter == DynamicKink.CREAMPIE,
              borderColor = DesireBorder,
              selectedBorderColor = NeonPinkLight
            )
          )
        }

        item {
          FilterChip(
            selected = selectedKinkFilter == DynamicKink.BREEDING,
            onClick = {
              selectedKinkFilter = if (selectedKinkFilter == DynamicKink.BREEDING) null else DynamicKink.BREEDING
            },
            label = { Text("🍼 Breeding", fontSize = 11.sp) },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = Color(0xFFFF70A6),
              selectedLabelColor = Color.White,
              containerColor = DesireCardSurface,
              labelColor = TextPrimary
            ),
            border = FilterChipDefaults.filterChipBorder(
              enabled = true,
              selected = selectedKinkFilter == DynamicKink.BREEDING,
              borderColor = DesireBorder,
              selectedBorderColor = Color(0xFFFF70A6)
            )
          )
        }

        item {
          FilterChip(
            selected = selectedKinkFilter == DynamicKink.RAW_UNPROTECTED_FANTASY,
            onClick = {
              selectedKinkFilter = if (selectedKinkFilter == DynamicKink.RAW_UNPROTECTED_FANTASY) null else DynamicKink.RAW_UNPROTECTED_FANTASY
            },
            label = { Text("🔥 Bareback / Raw", fontSize = 11.sp) },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = Color(0xFFE63946),
              selectedLabelColor = Color.White,
              containerColor = DesireCardSurface,
              labelColor = TextPrimary
            ),
            border = FilterChipDefaults.filterChipBorder(
              enabled = true,
              selected = selectedKinkFilter == DynamicKink.RAW_UNPROTECTED_FANTASY,
              borderColor = DesireBorder,
              selectedBorderColor = Color(0xFFE63946)
            )
          )
        }

        item {
          FilterChip(
            selected = selectedKinkFilter == DynamicKink.SPIT_ROASTING,
            onClick = {
              selectedKinkFilter = if (selectedKinkFilter == DynamicKink.SPIT_ROASTING) null else DynamicKink.SPIT_ROASTING
            },
            label = { Text("🔥 Spit Roasting", fontSize = 11.sp) },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = Color(0xFFFF5400),
              selectedLabelColor = Color.White,
              containerColor = DesireCardSurface,
              labelColor = TextPrimary
            ),
            border = FilterChipDefaults.filterChipBorder(
              enabled = true,
              selected = selectedKinkFilter == DynamicKink.SPIT_ROASTING,
              borderColor = DesireBorder,
              selectedBorderColor = Color(0xFFFF5400)
            )
          )
        }

        item {
          FilterChip(
            selected = selectedKinkFilter == DynamicKink.THREESOME_MFF,
            onClick = {
              selectedKinkFilter = if (selectedKinkFilter == DynamicKink.THREESOME_MFF) null else DynamicKink.THREESOME_MFF
            },
            label = { Text("Threesomes (MFF)", fontSize = 11.sp) },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = Color(0xFFFF2A85),
              selectedLabelColor = Color.White,
              containerColor = DesireCardSurface,
              labelColor = TextPrimary
            ),
            border = FilterChipDefaults.filterChipBorder(
              enabled = true,
              selected = selectedKinkFilter == DynamicKink.THREESOME_MFF,
              borderColor = DesireBorder,
              selectedBorderColor = Color(0xFFFF2A85)
            )
          )
        }

        item {
          FilterChip(
            selected = selectedKinkFilter == DynamicKink.ORGIES,
            onClick = {
              selectedKinkFilter = if (selectedKinkFilter == DynamicKink.ORGIES) null else DynamicKink.ORGIES
            },
            label = { Text("Orgies & Play Parties", fontSize = 11.sp) },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = Color(0xFFFF0054),
              selectedLabelColor = Color.White,
              containerColor = DesireCardSurface,
              labelColor = TextPrimary
            ),
            border = FilterChipDefaults.filterChipBorder(
              enabled = true,
              selected = selectedKinkFilter == DynamicKink.ORGIES,
              borderColor = DesireBorder,
              selectedBorderColor = Color(0xFFFF0054)
            )
          )
        }

        item {
          FilterChip(
            selected = selectedKinkFilter == DynamicKink.GANG_BANGING,
            onClick = {
              selectedKinkFilter = if (selectedKinkFilter == DynamicKink.GANG_BANGING) null else DynamicKink.GANG_BANGING
            },
            label = { Text("Gang Banging", fontSize = 11.sp) },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = Color(0xFF7209B7),
              selectedLabelColor = Color.White,
              containerColor = DesireCardSurface,
              labelColor = TextPrimary
            ),
            border = FilterChipDefaults.filterChipBorder(
              enabled = true,
              selected = selectedKinkFilter == DynamicKink.GANG_BANGING,
              borderColor = DesireBorder,
              selectedBorderColor = Color(0xFF7209B7)
            )
          )
        }
      }

      Spacer(modifier = Modifier.height(6.dp))

      // Row 2: Gender Identity Filter
      LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        items(UserType.values()) { type ->
          val isSelected = selectedTypeFilter == type
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(if (isSelected) NeonMagenta.copy(alpha = 0.25f) else DesireCardSurface)
              .border(1.dp, if (isSelected) NeonMagenta else DesireBorder, RoundedCornerShape(8.dp))
              .clickable {
                selectedTypeFilter = if (isSelected) null else type
              }
              .padding(horizontal = 8.dp, vertical = 4.dp)
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(type.icon, fontSize = 11.sp)
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                type.label,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) Color.White else TextSecondary
              )
            }
          }
        }
      }
    }

    // Match Feedback Toast
    matchToastMessage?.let { msg ->
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 6.dp)
          .clip(RoundedCornerShape(10.dp))
          .background(SuccessGreen.copy(alpha = 0.25f))
          .border(1.dp, SuccessGreen, RoundedCornerShape(10.dp))
          .padding(horizontal = 12.dp, vertical = 8.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(msg, fontSize = 12.sp, color = TextPrimary, fontWeight = FontWeight.Bold)
          IconButton(onClick = { matchToastMessage = null }, modifier = Modifier.size(20.dp)) {
            Icon(Icons.Default.Close, contentDescription = null, tint = TextPrimary, modifier = Modifier.size(14.dp))
          }
        }
      }
    }

    // Feed of Scored Matches
    if (profileMatchResults.isEmpty()) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(32.dp),
        contentAlignment = Alignment.Center
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Icon(Icons.Default.Tune, contentDescription = null, tint = NeonMagenta, modifier = Modifier.size(48.dp))
          Spacer(modifier = Modifier.height(12.dp))
          Text("No matches under current filter criteria", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            "Try broadening your desire preferences or resetting filters to explore all available profiles.",
            fontSize = 13.sp,
            color = TextSecondary,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
          )
          Spacer(modifier = Modifier.height(16.dp))
          Button(
            onClick = {
              selectedSubcultureFilter = null
              selectedKinkFilter = null
              selectedTypeFilter = null
            },
            colors = ButtonDefaults.buttonColors(containerColor = NeonMagenta)
          ) {
            Text("Reset All Filters", color = Color.White)
          }
        }
      }
    } else {
      LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        items(profileMatchResults, key = { it.profile.id }) { matchResult ->
          val profile = matchResult.profile
          val isLiked = likedProfileIds.contains(profile.id)

          ProfileMatchCard(
            matchResult = matchResult,
            isLiked = isLiked,
            canSendRequests = membership.canSendSexRequests,
            onCardClicked = { onProfileClicked(profile) },
            onChatClicked = { onChatClicked(profile) },
            onLikeClicked = {
              likedProfileIds = if (isLiked) likedProfileIds - profile.id else likedProfileIds + profile.id
              if (!isLiked) {
                matchToastMessage = "Liked ${profile.name}! Sent mutual chemistry pulse 💖"
              }
            },
            onSendRequestClicked = {
              if (membership.canSendSexRequests) {
                onSendRequestClicked(profile)
              } else {
                onOpenUpgrade()
              }
            },
            onVaultClicked = {
              if (membership.canView18PlusContent) {
                onProfileClicked(profile)
              } else {
                onOpenUpgrade()
              }
            }
          )
        }
      }
    }
  }

  // Algorithm Preferences Dialog Modal
  if (showAlgorithmDialog || showFilterDialog) {
    MatchingPreferencesDialog(
      onDismiss = {
        showAlgorithmDialog = false
        onDismissFilterDialog()
      },
      onPreferencesApplied = { toast ->
        matchToastMessage = toast
        showAlgorithmDialog = false
        onDismissFilterDialog()
      }
    )
  }
}

@Composable
fun ProfileMatchCard(
  matchResult: MatchScoreResult,
  isLiked: Boolean,
  canSendRequests: Boolean,
  onCardClicked: () -> Unit,
  onChatClicked: () -> Unit,
  onLikeClicked: () -> Unit,
  onSendRequestClicked: () -> Unit,
  onVaultClicked: () -> Unit
) {
  val profile = matchResult.profile

  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(20.dp))
      .clickable(onClick = onCardClicked),
    colors = CardDefaults.cardColors(containerColor = DesireCardSurface),
    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
  ) {
    Column {
      // Photo Header with Scrim
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(240.dp)
          .background(
            Brush.linearGradient(
              listOf(Color(profile.gradientColors.first), Color(profile.gradientColors.second))
            )
          )
      ) {
        if (profile.mainPhotoRes != null) {
          Image(
            painter = painterResource(id = profile.mainPhotoRes),
            contentDescription = profile.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
          )
        } else {
          Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
          ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Text(text = profile.userType.icon, fontSize = 56.sp)
              Spacer(modifier = Modifier.height(6.dp))
              Text(text = profile.name, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
          }
        }

        // Dark gradient scrim
        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(
              Brush.verticalGradient(
                listOf(Color.Transparent, Color.Black.copy(alpha = 0.85f)),
                startY = 120f
              )
            )
        )

        // Top Badges: Algorithm Match % Badge & Identity
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          // Compatibility Badge from matching algorithm
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(12.dp))
              .background(Brush.horizontalGradient(listOf(NeonMagenta, NeonCyan)))
              .padding(horizontal = 10.dp, vertical = 4.dp)
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = DesireBlack, modifier = Modifier.size(13.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "${matchResult.compatibilityPercentage}% MATCH",
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                color = DesireBlack
              )
            }
          }

          Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(DesireBlack.copy(alpha = 0.8f))
                .border(1.dp, NeonMagenta, RoundedCornerShape(10.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(profile.userType.icon, fontSize = 12.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  profile.userType.label,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color.White
                )
              }
            }

            if (profile.has18PlusVault) {
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(10.dp))
                  .background(DesireBlack.copy(alpha = 0.85f))
                  .border(1.dp, GoldVip, RoundedCornerShape(10.dp))
                  .padding(horizontal = 8.dp, vertical = 4.dp)
                  .clickable(onClick = onVaultClicked)
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(Icons.Default.VpnKey, contentDescription = null, tint = GoldVip, modifier = Modifier.size(12.dp))
                  Spacer(modifier = Modifier.width(4.dp))
                  Text(
                    "18+ VAULT",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldVip
                  )
                }
              }
            }
          }
        }

        // Bottom Info
        Column(
          modifier = Modifier
            .align(Alignment.BottomStart)
            .padding(14.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = profile.name,
              fontSize = 20.sp,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = profile.age,
              fontSize = 18.sp,
              fontWeight = FontWeight.Medium,
              color = TextSecondary
            )
          }

          Text(
            text = "${profile.distanceMiles} miles away • ${profile.subculture}",
            fontSize = 12.sp,
            color = TextSecondary
          )
        }
      }

      // Card Content Body
      Column(modifier = Modifier.padding(14.dp)) {
        // Tagline
        Text(
          text = "\"${profile.tagline}\"",
          fontSize = 13.sp,
          fontWeight = FontWeight.SemiBold,
          color = NeonPinkLight
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Match Reasons (computed by algorithm)
        if (matchResult.reasonHighlights.isNotEmpty()) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            for (reason in matchResult.reasonHighlights.take(2)) {
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(6.dp))
                  .background(Color(0xFF1E142B))
                  .border(1.dp, VelvetPurple.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                  .padding(horizontal = 6.dp, vertical = 3.dp)
              ) {
                Text(reason, fontSize = 10.sp, color = TextSecondary, maxLines = 1)
              }
            }
          }
          Spacer(modifier = Modifier.height(10.dp))
        }

        // Desires & Kinks Chips
        LazyRow(
          horizontalArrangement = Arrangement.spacedBy(6.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          items(profile.desireTags.take(4)) { kink ->
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(Color(kink.badgeColorHex).copy(alpha = 0.15f))
                .border(1.dp, Color(kink.badgeColorHex).copy(alpha = 0.6f), RoundedCornerShape(6.dp))
                .padding(horizontal = 7.dp, vertical = 4.dp)
            ) {
              Text(
                text = kink.displayName,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Color(kink.badgeColorHex)
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Action Buttons Row
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          IconButton(
            onClick = onLikeClicked,
            modifier = Modifier
              .size(42.dp)
              .background(if (isLiked) NeonMagenta.copy(alpha = 0.3f) else DesireCardSurfaceElevated, CircleShape)
              .border(1.dp, if (isLiked) NeonMagenta else DesireBorder, CircleShape)
          ) {
            Icon(
              if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
              contentDescription = "Like",
              tint = if (isLiked) NeonMagenta else TextSecondary,
              modifier = Modifier.size(20.dp)
            )
          }

          Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            // Instant Unlimited Member Chat Button
            Button(
              onClick = onChatClicked,
              colors = ButtonDefaults.buttonColors(containerColor = NeonCyan.copy(alpha = 0.15f)),
              border = androidx.compose.foundation.BorderStroke(1.dp, NeonCyan),
              shape = RoundedCornerShape(12.dp),
              contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
            ) {
              Icon(
                Icons.Default.ChatBubbleOutline,
                contentDescription = null,
                modifier = Modifier.size(15.dp),
                tint = NeonCyan
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text("Chat", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = NeonCyan)
            }

            // Direct Sex Request Button
            Button(
              onClick = onSendRequestClicked,
              colors = ButtonDefaults.buttonColors(
                containerColor = if (canSendRequests) NeonMagenta else DesireBorder
              ),
              shape = RoundedCornerShape(12.dp),
              contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
            ) {
              Icon(
                if (canSendRequests) Icons.Default.Send else Icons.Default.Lock,
                contentDescription = null,
                modifier = Modifier.size(15.dp),
                tint = if (canSendRequests) Color.White else GoldVip
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = if (canSendRequests) "Direct Sex Request" else "Unlock Requests ($15)",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (canSendRequests) Color.White else GoldVip
              )
            }
          }
        }
      }
    }
  }
}
