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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Tune
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
import com.example.model.DynamicKink
import com.example.model.MatchPreferences
import com.example.model.RelationshipStatus
import com.example.model.SexualOrientation
import com.example.model.SubcultureType
import com.example.model.UserType
import com.example.ui.theme.*

@Composable
fun MatchingPreferencesDialog(
  onDismiss: () -> Unit,
  onPreferencesApplied: (String) -> Unit
) {
  val currentPrefs by SampleDataRepository.matchPreferences.collectAsState()

  var selectedGenders by remember { mutableStateOf(currentPrefs.targetGenders) }
  var selectedOrientations by remember { mutableStateOf(currentPrefs.targetOrientations) }
  var selectedRelationshipStatuses by remember { mutableStateOf(currentPrefs.targetRelationshipStatuses) }
  var selectedDynamics by remember { mutableStateOf(currentPrefs.targetDynamics) }
  var selectedSubcultures by remember { mutableStateOf(currentPrefs.targetSubcultures) }
  var onlyBigCocks by remember { mutableStateOf(currentPrefs.onlyBigCocks) }
  var onlyDiscreet by remember { mutableStateOf(currentPrefs.onlyDiscreet) }
  var onlyRealVerifiedMembers by remember { mutableStateOf(currentPrefs.onlyRealVerifiedMembers) }
  var minAge by remember { mutableStateOf(currentPrefs.minAge.toFloat().coerceAtLeast(18f)) }
  var maxAge by remember { mutableStateOf(currentPrefs.maxAge.toFloat().coerceAtMost(40f)) }
  var maxDistance by remember { mutableStateOf(currentPrefs.maxDistanceMiles.toFloat()) }

  val relationshipDynamics = listOf(
    DynamicKink.THREESOME_MFF,
    DynamicKink.THREESOME_MMF,
    DynamicKink.FOURSOME,
    DynamicKink.ORGIES,
    DynamicKink.HERMAPHRODITE_INTERSEX_DATING,
    DynamicKink.BISEXUAL_PLAY
  )

  val specificKinks = listOf(
    DynamicKink.CREAMPIE,
    DynamicKink.BREEDING,
    DynamicKink.RAW_UNPROTECTED_FANTASY,
    DynamicKink.DEEPTHROAT_ORAL,
    DynamicKink.CUM_TRIBUTE_FACIAL,
    DynamicKink.BIG_COCKS_FOCUS,
    DynamicKink.SPIT_ROASTING,
    DynamicKink.GANG_BANGING,
    DynamicKink.DISCREET_CHEATING,
    DynamicKink.CUCKOLD_HOTWIFE,
    DynamicKink.PEGGING_PROSTATE,
    DynamicKink.LESBIAN_TRIB,
    DynamicKink.BDSM_KINK,
    DynamicKink.OPEN_POLY
  )

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      modifier = Modifier
        .fillMaxWidth(0.95f)
        .fillMaxHeight(0.94f)
        .clip(RoundedCornerShape(24.dp))
        .border(1.5.dp, Brush.verticalGradient(listOf(NeonCyan, NeonMagenta)), RoundedCornerShape(24.dp)),
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
                .background(Brush.radialGradient(listOf(NeonCyan, VelvetPurple)), CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "ALGORITHM TUNER",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                color = NeonCyan
              )
              Text(
                text = "Desire Matching Engine",
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

        Text(
          text = "Tailor every gender, orientation, relationship status, kink, big cocks section, and discreet preference to power your real-time matches.",
          fontSize = 12.sp,
          color = TextSecondary,
          lineHeight = 17.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // EXCLUSIVE SECTIONS TOGGLES (Big Cocks Only & Discreet Affair)
        Text(
          text = "EXCLUSIVE DATING SECTIONS",
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.sp,
          color = GoldVip
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Big Cocks Only Filter
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(if (onlyBigCocks) Color(0xFFFF2A85).copy(alpha = 0.2f) else DesireCardSurface)
            .border(1.dp, if (onlyBigCocks) NeonMagenta else DesireBorder, RoundedCornerShape(14.dp))
            .clickable { onlyBigCocks = !onlyBigCocks }
            .padding(12.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text("🍆", fontSize = 20.sp)
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Text(
                  text = "BIG COCKS ONLY SECTION (8\"+ HUNG)",
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold,
                  color = if (onlyBigCocks) NeonMagenta else TextPrimary
                )
                Text(
                  text = "Filter matches for verified hung partners & bulls only",
                  fontSize = 11.sp,
                  color = TextSecondary
                )
              }
            }
            Switch(
              checked = onlyBigCocks,
              onCheckedChange = { onlyBigCocks = it },
              colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = NeonMagenta
              )
            )
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Discreet / Cheating Section Filter
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(if (onlyDiscreet) Color(0xFF00F0FF).copy(alpha = 0.2f) else DesireCardSurface)
            .border(1.dp, if (onlyDiscreet) NeonCyan else DesireBorder, RoundedCornerShape(14.dp))
            .clickable { onlyDiscreet = !onlyDiscreet }
            .padding(12.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text("🤫", fontSize = 20.sp)
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Text(
                  text = "DISCREET & SECRET AFFAIRS ONLY",
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold,
                  color = if (onlyDiscreet) NeonCyan else TextPrimary
                )
                Text(
                  text = "Confidential, incognito, cheating & open marriage rendezvous",
                  fontSize = 11.sp,
                  color = TextSecondary
                )
              }
            }
            Switch(
              checked = onlyDiscreet,
              onCheckedChange = { onlyDiscreet = it },
              colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = NeonCyan
              )
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 1. Gender Identities (including Hermaphrodite / Intersex)
        Text(
          text = "1. TARGET GENDER IDENTITIES & INTERSEX",
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.sp,
          color = TextSecondary
        )
        Spacer(modifier = Modifier.height(8.dp))

        for (row in UserType.values().toList().chunked(2)) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 3.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            for (type in row) {
              val isSelected = selectedGenders.contains(type)
              Box(
                modifier = Modifier
                  .weight(1f)
                  .clip(RoundedCornerShape(12.dp))
                  .background(if (isSelected) NeonMagenta.copy(alpha = 0.25f) else DesireCardSurface)
                  .border(1.dp, if (isSelected) NeonMagenta else DesireBorder, RoundedCornerShape(12.dp))
                  .clickable {
                    selectedGenders = if (isSelected) selectedGenders - type else selectedGenders + type
                  }
                  .padding(horizontal = 10.dp, vertical = 8.dp)
              ) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(type.icon, fontSize = 13.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                      text = type.label,
                      fontSize = 11.sp,
                      fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                      color = if (isSelected) Color.White else TextSecondary
                    )
                  }
                  if (isSelected) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = NeonMagenta, modifier = Modifier.size(14.dp))
                  }
                }
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 2. Sexual Orientations (Bisexual, Gay, Lesbian, Straight, Pansexual, Queer)
        Text(
          text = "2. SEXUAL ORIENTATION PREFERENCE",
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.sp,
          color = TextSecondary
        )
        Spacer(modifier = Modifier.height(8.dp))

        for (row in SexualOrientation.values().toList().chunked(2)) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 3.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            for (orientation in row) {
              val isSelected = selectedOrientations.contains(orientation)
              Box(
                modifier = Modifier
                  .weight(1f)
                  .clip(RoundedCornerShape(12.dp))
                  .background(if (isSelected) VelvetPurple.copy(alpha = 0.35f) else DesireCardSurface)
                  .border(1.dp, if (isSelected) VelvetPurple else DesireBorder, RoundedCornerShape(12.dp))
                  .clickable {
                    selectedOrientations = if (isSelected) selectedOrientations - orientation else selectedOrientations + orientation
                  }
                  .padding(horizontal = 10.dp, vertical = 8.dp)
              ) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(orientation.badge, fontSize = 13.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                      text = orientation.label,
                      fontSize = 11.sp,
                      fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                      color = if (isSelected) Color.White else TextSecondary
                    )
                  }
                  if (isSelected) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(14.dp))
                  }
                }
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 3. Relationship Status (Open Marriage, Open Relationship, Single, Cheating, Poly)
        Text(
          text = "3. RELATIONSHIP TYPE (OPEN MARRIAGE, CHEATING, SINGLE...)",
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.sp,
          color = TextSecondary
        )
        Spacer(modifier = Modifier.height(8.dp))

        for (status in RelationshipStatus.values()) {
          val isSelected = selectedRelationshipStatuses.contains(status)
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 3.dp)
              .clip(RoundedCornerShape(12.dp))
              .background(if (isSelected) Color(0xFF4361EE).copy(alpha = 0.25f) else DesireCardSurface)
              .border(1.dp, if (isSelected) Color(0xFF4361EE) else DesireBorder, RoundedCornerShape(12.dp))
              .clickable {
                selectedRelationshipStatuses = if (isSelected) selectedRelationshipStatuses - status else selectedRelationshipStatuses + status
              }
              .padding(horizontal = 12.dp, vertical = 9.dp)
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(status.icon, fontSize = 14.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                  Text(
                    text = status.label,
                    fontSize = 12.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if (isSelected) Color.White else TextPrimary
                  )
                  Text(
                    text = status.description,
                    fontSize = 10.sp,
                    color = TextSecondary
                  )
                }
              }
              if (isSelected) {
                Icon(Icons.Default.Check, contentDescription = null, tint = Color(0xFF4361EE), modifier = Modifier.size(16.dp))
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 4. Dynamics & Group Activities
        Text(
          text = "4. GROUP DYNAMICS & INTIMATE PREFERENCES",
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.sp,
          color = TextSecondary
        )
        Spacer(modifier = Modifier.height(8.dp))

        for (dynamic in relationshipDynamics) {
          val isSelected = selectedDynamics.contains(dynamic)
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 3.dp)
              .clip(RoundedCornerShape(12.dp))
              .background(if (isSelected) VelvetPurple.copy(alpha = 0.35f) else DesireCardSurface)
              .border(1.dp, if (isSelected) VelvetPurple else DesireBorder, RoundedCornerShape(12.dp))
              .clickable {
                selectedDynamics = if (isSelected) selectedDynamics - dynamic else selectedDynamics + dynamic
              }
              .padding(horizontal = 12.dp, vertical = 9.dp)
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = dynamic.displayName,
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) Color.White else TextPrimary
              )
              if (isSelected) {
                Icon(Icons.Default.Check, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(16.dp))
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 5. Specific Kinks & Fantasies
        Text(
          text = "5. SPECIFIC KINKS & FETISHES",
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.sp,
          color = TextSecondary
        )
        Spacer(modifier = Modifier.height(8.dp))

        for (kink in specificKinks) {
          val isSelected = selectedDynamics.contains(kink)
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 3.dp)
              .clip(RoundedCornerShape(12.dp))
              .background(if (isSelected) Color(kink.badgeColorHex).copy(alpha = 0.2f) else DesireCardSurface)
              .border(1.dp, if (isSelected) Color(kink.badgeColorHex) else DesireBorder, RoundedCornerShape(12.dp))
              .clickable {
                selectedDynamics = if (isSelected) selectedDynamics - kink else selectedDynamics + kink
              }
              .padding(horizontal = 12.dp, vertical = 9.dp)
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = kink.displayName,
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) Color(kink.badgeColorHex) else TextPrimary
              )
              if (isSelected) {
                Icon(Icons.Default.Check, contentDescription = null, tint = Color(kink.badgeColorHex), modifier = Modifier.size(16.dp))
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 6. Subculture Interests (Goth, Emo, Alt)
        Text(
          text = "6. SUBCULTURE (GOTH, EMO, ALTERNATIVE)",
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.sp,
          color = TextSecondary
        )
        Spacer(modifier = Modifier.height(8.dp))

        for (row in SubcultureType.values().toList().chunked(2)) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 3.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            for (sub in row) {
              val isSelected = selectedSubcultures.contains(sub)
              Box(
                modifier = Modifier
                  .weight(1f)
                  .clip(RoundedCornerShape(12.dp))
                  .background(if (isSelected) Color(0xFF4361EE).copy(alpha = 0.3f) else DesireCardSurface)
                  .border(1.dp, if (isSelected) Color(0xFF4361EE) else DesireBorder, RoundedCornerShape(12.dp))
                  .clickable {
                    selectedSubcultures = if (isSelected) selectedSubcultures - sub else selectedSubcultures + sub
                  }
                  .padding(horizontal = 10.dp, vertical = 8.dp)
              ) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(sub.emoji, fontSize = 13.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                      text = sub.label,
                      fontSize = 11.sp,
                      fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                      color = if (isSelected) Color.White else TextSecondary
                    )
                  }
                  if (isSelected) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = Color(0xFF4361EE), modifier = Modifier.size(14.dp))
                  }
                }
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 7. REAL MEMBERS & ANTI-FAKE PROFILES FILTER
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = DesireCardSurface),
          border = androidx.compose.foundation.BorderStroke(1.dp, if (onlyRealVerifiedMembers) SuccessGreen else DesireBorder)
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text("🛡️", fontSize = 16.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "REMOVE FAKE PROFILES (VERIFIED ONLY)",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  letterSpacing = 0.5.sp,
                  color = SuccessGreen
                )
              }
              Spacer(modifier = Modifier.height(2.dp))
              Text(
                text = "Show 100% genuine, photo-verified real members. Blocks simulated/bot accounts completely.",
                fontSize = 10.sp,
                color = TextSecondary
              )
            }
            Switch(
              checked = onlyRealVerifiedMembers,
              onCheckedChange = { onlyRealVerifiedMembers = it },
              colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = SuccessGreen)
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 8. AGE LIMITS: STRICT 18 TO 40 EXCLUSIVE
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = DesireCardSurface),
          border = androidx.compose.foundation.BorderStroke(1.dp, NeonMagenta)
        ) {
          Column(modifier = Modifier.padding(12.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column {
                Text(
                  text = "AGE RESTRICTION: 18 TO 40 ONLY",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  letterSpacing = 0.5.sp,
                  color = NeonMagenta
                )
                Text(
                  text = "Target age window: ${minAge.toInt()} – ${maxAge.toInt()} years old",
                  fontSize = 10.sp,
                  color = TextSecondary
                )
              }
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(6.dp))
                  .background(NeonMagenta.copy(alpha = 0.2f))
                  .padding(horizontal = 6.dp, vertical = 2.dp)
              ) {
                Text("${minAge.toInt()}–${maxAge.toInt()} yrs", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = NeonMagenta)
              }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text("Min: 18", fontSize = 10.sp, color = TextMuted)
              Text("Max Limit: 40", fontSize = 10.sp, color = TextMuted)
            }
            Slider(
              value = maxAge,
              onValueChange = { maxAge = it.coerceIn(21f, 40f) },
              valueRange = 21f..40f,
              colors = SliderDefaults.colors(
                thumbColor = NeonMagenta,
                activeTrackColor = NeonMagenta,
                inactiveTrackColor = DesireBorder
              )
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Distance Slider
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text("MAX DISTANCE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
          Text("${maxDistance.toInt()} miles", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = NeonCyan)
        }
        Slider(
          value = maxDistance,
          onValueChange = { maxDistance = it },
          valueRange = 5f..150f,
          colors = SliderDefaults.colors(
            thumbColor = NeonCyan,
            activeTrackColor = NeonCyan,
            inactiveTrackColor = DesireBorder
          )
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Apply Button
        Button(
          onClick = {
            val updated = currentPrefs.copy(
              targetGenders = selectedGenders,
              targetOrientations = selectedOrientations,
              targetRelationshipStatuses = selectedRelationshipStatuses,
              targetDynamics = selectedDynamics,
              targetSubcultures = selectedSubcultures,
              onlyBigCocks = onlyBigCocks,
              onlyDiscreet = onlyDiscreet,
              onlyRealVerifiedMembers = onlyRealVerifiedMembers,
              minAge = minAge.toInt().coerceAtLeast(18),
              maxAge = maxAge.toInt().coerceAtMost(40),
              maxDistanceMiles = maxDistance.toInt()
            )
            SampleDataRepository.updateMatchPreferences(updated)
            onPreferencesApplied("Preferences updated: Real members only (18–40 age limit applied)!")
            onDismiss()
          },
          modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
          colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
          shape = RoundedCornerShape(14.dp),
          contentPadding = PaddingValues(0.dp)
        ) {
          Box(
            modifier = Modifier
              .fillMaxSize()
              .background(
                Brush.horizontalGradient(listOf(NeonCyan, NeonMagenta)),
                RoundedCornerShape(14.dp)
              ),
            contentAlignment = Alignment.Center
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.Tune, contentDescription = null, tint = DesireBlack)
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "APPLY DESIRE ALGORITHM",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
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

