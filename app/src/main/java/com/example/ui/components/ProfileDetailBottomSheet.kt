package com.example.ui.components

import androidx.compose.foundation.Image
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
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.SampleDataRepository
import com.example.util.SubscriptionManager
import com.example.util.PremiumFeature
import com.example.model.DynamicKink
import com.example.model.UserProfile
import com.example.ui.components.MembershipBadge
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileDetailBottomSheet(
  profile: UserProfile,
  onDismiss: () -> Unit,
  onSendRequestClicked: (UserProfile) -> Unit,
  onOpenUpgrade: () -> Unit
) {
  val membership by SampleDataRepository.membershipState.collectAsState()
  val matchPrefs by SampleDataRepository.matchPreferences.collectAsState()

  val matchResult = remember(profile, matchPrefs) {
    SampleDataRepository.calculateMatchScore(profile, matchPrefs)
  }

  var selectedPhotoIndex by remember { mutableStateOf(0) }

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    containerColor = DesireDarkSurface,
    dragHandle = {
      BottomSheetDefaults.DragHandle(color = NeonMagenta.copy(alpha = 0.5f))
    }
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(0.92f)
        .verticalScroll(rememberScrollState())
        .padding(bottom = 24.dp)
    ) {
      // Hero Header Photo / Art
      val currentPhotoRes = if (profile.photos.isNotEmpty()) {
        profile.photos.getOrNull(selectedPhotoIndex)?.drawableRes ?: profile.mainPhotoRes
      } else {
        profile.mainPhotoRes
      }

      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(280.dp)
          .padding(horizontal = 16.dp)
          .clip(RoundedCornerShape(20.dp))
          .background(
            Brush.linearGradient(
              listOf(Color(profile.gradientColors.first), Color(profile.gradientColors.second))
            )
          )
      ) {
        if (currentPhotoRes != null) {
          Image(
            painter = painterResource(id = currentPhotoRes),
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
              Text(
                text = profile.userType.icon,
                fontSize = 64.sp
              )
              Spacer(modifier = Modifier.height(8.dp))
              Text(
                text = profile.name,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
              )
            }
          }
        }

        // Gradient shadow at bottom of photo
        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(
              Brush.verticalGradient(
                listOf(Color.Transparent, Color.Black.copy(alpha = 0.85f)),
                startY = 180f
              )
            )
        )

        // Identity & Verification Badge top right
        Row(
          modifier = Modifier
            .align(Alignment.TopEnd)
            .padding(14.dp),
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          MembershipBadge(plan = profile.membershipPlan)

          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(12.dp))
              .background(DesireBlack.copy(alpha = 0.75f))
              .border(1.dp, NeonMagenta, RoundedCornerShape(12.dp))
              .padding(horizontal = 10.dp, vertical = 5.dp)
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(text = profile.userType.icon, fontSize = 13.sp)
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = profile.userType.label,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
              )
            }
          }

          if (profile.verified) {
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(DesireBlack.copy(alpha = 0.75f))
                .border(1.dp, NeonCyan, RoundedCornerShape(12.dp))
                .padding(horizontal = 8.dp, vertical = 5.dp)
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  Icons.Default.Verified,
                  contentDescription = null,
                  tint = NeonCyan,
                  modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = "VERIFIED",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = NeonCyan
                )
              }
            }
          }
        }

        // Bottom info on photo
        Column(
          modifier = Modifier
            .align(Alignment.BottomStart)
            .padding(16.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = profile.name,
              fontSize = 24.sp,
              fontWeight = FontWeight.ExtraBold,
              color = Color.White
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = profile.age,
              fontSize = 20.sp,
              fontWeight = FontWeight.Medium,
              color = TextSecondary
            )
          }

          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              Icons.Default.LocationOn,
              contentDescription = null,
              tint = NeonMagenta,
              modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "${profile.distanceMiles} miles away • ${profile.subculture}",
              fontSize = 13.sp,
              color = TextSecondary
            )
          }
        }
      }

      // Multi-Photo Carousel Indicator if multiple photos exist
      if (profile.photos.size > 1) {
        Spacer(modifier = Modifier.height(10.dp))
        LazyRow(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
        ) {
          items(profile.photos.indices.toList()) { idx ->
            val p = profile.photos[idx]
            Box(
              modifier = Modifier
                .size(54.dp)
                .clip(RoundedCornerShape(10.dp))
                .border(
                  2.dp,
                  if (selectedPhotoIndex == idx) NeonCyan else DesireBorder,
                  RoundedCornerShape(10.dp)
                )
                .clickable { selectedPhotoIndex = idx }
            ) {
              if (p.drawableRes != null) {
                Image(
                  painter = painterResource(id = p.drawableRes),
                  contentDescription = p.caption,
                  contentScale = ContentScale.Crop,
                  modifier = Modifier.fillMaxSize()
                )
              } else {
                Box(
                  modifier = Modifier
                    .fillMaxSize()
                    .background(VelvetPurple),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(Icons.Default.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                }
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Content Body
      Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        // MATCH ALGORITHM COMPATIBILITY BANNER
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(
              Brush.horizontalGradient(
                listOf(Color(0xFF1E0A24), Color(0xFF0F1E29))
              )
            )
            .border(1.5.dp, Brush.horizontalGradient(listOf(NeonMagenta, NeonCyan)), RoundedCornerShape(14.dp))
            .padding(12.dp)
        ) {
          Column {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "DESIRE MATCH ALGORITHM",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  letterSpacing = 1.sp,
                  color = NeonCyan
                )
              }

              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(8.dp))
                  .background(Brush.horizontalGradient(listOf(NeonMagenta, NeonCyan)))
                  .padding(horizontal = 8.dp, vertical = 3.dp)
              ) {
                Text(
                  text = "${matchResult.compatibilityPercentage}% MATCH",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Black,
                  color = DesireBlack
                )
              }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
              for (reason in matchResult.reasonHighlights) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(Icons.Default.Check, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(13.dp))
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(text = reason, fontSize = 11.sp, color = TextPrimary)
                }
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Tagline
        Text(
          text = "\"${profile.tagline}\"",
          fontSize = 15.sp,
          fontWeight = FontWeight.SemiBold,
          fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
          color = NeonPinkLight
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Preferred Group Activities (Threesomes, Foursomes, Orgies, Spit Roasting)
        if (profile.preferredGroupActivities.isNotEmpty()) {
          Text(
            text = "PREFERRED GROUP ACTIVITIES",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
            color = VelvetPurple
          )
          Spacer(modifier = Modifier.height(6.dp))
          FlowRowDesireBadges(profile.preferredGroupActivities)
          Spacer(modifier = Modifier.height(14.dp))
        }

        // Desires & Kinks badges
        Text(
          text = "ALL DESIRES & KINKS",
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.sp,
          color = TextSecondary
        )
        Spacer(modifier = Modifier.height(6.dp))
        FlowRowDesireBadges(profile.desireTags)

        Spacer(modifier = Modifier.height(14.dp))
        
        // LIFESTYLE GALLERY
        LifestyleGallery(profile.lifestylePhotos)

        Spacer(modifier = Modifier.height(16.dp))

        // About / Bio
        Text(
          text = "ABOUT & BIO",
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.sp,
          color = TextSecondary
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = profile.bio,
          fontSize = 14.sp,
          color = TextPrimary,
          lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Looking for
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(DesireCardSurface)
            .padding(14.dp)
        ) {
          Column {
            Text(
              text = "LOOKING FOR IN MATCHES",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              letterSpacing = 1.sp,
              color = NeonCyan
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = profile.lookingFor,
              fontSize = 13.sp,
              color = TextPrimary
            )
            if (profile.targetGendersLookingFor.isNotEmpty()) {
              Spacer(modifier = Modifier.height(6.dp))
              Text(
                text = "Seeking: " + profile.targetGendersLookingFor.joinToString(", ") { it.label },
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = NeonPinkLight
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Boundaries & Safety
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(DesireCardSurface)
            .padding(14.dp)
        ) {
          Column {
            Text(
              text = "BOUNDARIES & SAFETY",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              letterSpacing = 1.sp,
              color = VelvetPurple
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = profile.boundaries,
              fontSize = 13.sp,
              color = TextPrimary
            )
          }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 18+ Private Vault Section
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(24.dp)
                .background(Brush.radialGradient(listOf(GoldVip, NeonMagenta)), CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.VpnKey, contentDescription = null, tint = DesireBlack, modifier = Modifier.size(14.dp))
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
              Text(
                text = "18+ PRIVATE VAULT",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                color = GoldVip
              )
              Text(
                text = if (SubscriptionManager.canView18PlusContent()) "VIP Access Active" else SubscriptionManager.getRestrictionReason(PremiumFeature.VAULT_18_PLUS),
                fontSize = 10.sp,
                color = if (SubscriptionManager.canView18PlusContent()) SuccessGreen else TextMuted
              )
            }
          }

          if (!SubscriptionManager.canView18PlusContent()) {
            Button(
              onClick = onOpenUpgrade,
              colors = ButtonDefaults.buttonColors(containerColor = GoldVip),
              contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
              shape = RoundedCornerShape(8.dp)
            ) {
              Text("Unlock ($15)", color = DesireBlack, fontWeight = FontWeight.Bold, fontSize = 11.sp)
            }
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (profile.vaultItems.isEmpty()) {
          Text("No 18+ vault items uploaded by this member yet.", fontSize = 12.sp, color = TextMuted)
        } else {
          Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            for (item in profile.vaultItems) {
              VaultPreviewCard(
                item = item,
                canView = SubscriptionManager.canView18PlusContent(),
                onUnlockClicked = onOpenUpgrade
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Action Buttons: Send Direct Sex Request & Send Message
        Button(
          onClick = {
            if (SubscriptionManager.canSendSexRequests()) {
              onSendRequestClicked(profile)
            } else {
              onOpenUpgrade()
            }
          },
          modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
          colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
          shape = RoundedCornerShape(14.dp),
          contentPadding = PaddingValues(0.dp)
        ) {
          Box(
            modifier = Modifier
              .fillMaxSize()
              .background(
                Brush.horizontalGradient(listOf(NeonMagenta, VelvetPurple)),
                RoundedCornerShape(14.dp)
              ),
            contentAlignment = Alignment.Center
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.Send, contentDescription = null, tint = Color.White)
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = if (SubscriptionManager.canSendSexRequests()) "SEND DIRECT SEX REQUEST" else "UNLOCK SEX REQUESTS ($15)",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = Color.White,
                letterSpacing = 1.sp
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedButton(
          onClick = { onSendRequestClicked(profile) },
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
          border = androidx.compose.foundation.BorderStroke(1.dp, NeonCyan),
          shape = RoundedCornerShape(14.dp)
        ) {
          Icon(Icons.Default.ChatBubbleOutline, contentDescription = null, tint = NeonCyan)
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "MESSAGE IN PRIVATE CHAT",
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = NeonCyan,
            letterSpacing = 1.sp
          )
        }
      }
    }
  }
}

@Composable
fun FlowRowDesireBadges(desires: List<DynamicKink>) {
  Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
    for (chunk in desires.chunked(2)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        for (kink in chunk) {
          Box(
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(8.dp))
              .background(Color(kink.badgeColorHex).copy(alpha = 0.15f))
              .border(1.dp, Color(kink.badgeColorHex).copy(alpha = 0.8f), RoundedCornerShape(8.dp))
              .padding(horizontal = 8.dp, vertical = 6.dp)
          ) {
            Text(
              text = kink.displayName,
              fontSize = 11.sp,
              fontWeight = FontWeight.SemiBold,
              color = Color(kink.badgeColorHex),
              maxLines = 1
            )
          }
        }
      }
    }
  }
}

@Composable
fun VaultPreviewCard(
  item: com.example.model.VaultMediaItem,
  canView: Boolean,
  onUnlockClicked: () -> Unit
) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .height(110.dp)
      .clip(RoundedCornerShape(12.dp))
      .background(
        Brush.horizontalGradient(
          listOf(Color(item.blurCoverGradient.first), Color(item.blurCoverGradient.second))
        )
      )
      .clickable(enabled = !canView, onClick = onUnlockClicked)
  ) {
    if (canView && item.previewRes != null) {
      Image(
        painter = painterResource(id = item.previewRes),
        contentDescription = item.title,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize()
      )
      Box(
        modifier = Modifier
          .fillMaxSize()
          .background(Color.Black.copy(alpha = 0.45f))
      )
    } else {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .background(Color.Black.copy(alpha = 0.6f))
          .blur(8.dp)
      )
    }

    Row(
      modifier = Modifier
        .fillMaxSize()
        .padding(14.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column(modifier = Modifier.weight(1f)) {
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(if (canView) NeonCyan else GoldVip)
            .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
          Text(
            text = if (canView) "UNLOCKED HD" else "18+ LOCKED",
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = DesireBlack
          )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = item.title,
          fontSize = 14.sp,
          fontWeight = FontWeight.Bold,
          color = Color.White
        )
        Text(
          text = item.description,
          fontSize = 11.sp,
          color = TextSecondary,
          maxLines = 2
        )
      }

      Column(horizontalAlignment = Alignment.End) {
        if (!canView) {
          Box(
            modifier = Modifier
              .size(32.dp)
              .background(GoldVip, CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Icon(Icons.Default.Lock, contentDescription = null, tint = DesireBlack, modifier = Modifier.size(16.dp))
          }
          Spacer(modifier = Modifier.height(4.dp))
          Text("Unlock ($15)", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = GoldVip)
        } else {
          Box(
            modifier = Modifier
              .size(32.dp)
              .background(NeonCyan, CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Icon(Icons.Default.PlayArrow, contentDescription = null, tint = DesireBlack, modifier = Modifier.size(20.dp))
          }
          Spacer(modifier = Modifier.height(4.dp))
          Text(item.durationOrCount, fontSize = 10.sp, color = NeonCyan)
        }
      }
    }
  }
}

@Composable
fun LifestyleGallery(photos: List<com.example.model.PhotoItem>) {
  if (photos.isEmpty()) return

  Column(modifier = Modifier.padding(vertical = 12.dp)) {
    Text(
      text = "CURATED LIFESTYLE GALLERY",
      fontSize = 11.sp,
      fontWeight = FontWeight.Bold,
      letterSpacing = 1.sp,
      color = TextSecondary,
      modifier = Modifier.padding(horizontal = 20.dp)
    )
    Spacer(modifier = Modifier.height(10.dp))
    LazyRow(
      contentPadding = PaddingValues(horizontal = 20.dp),
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      items(photos) { photo ->
        var isUnblurred by remember { mutableStateOf(false) }
        Box(
          modifier = Modifier
            .width(150.dp)
            .height(200.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(DesireCardSurface)
            .clickable { isUnblurred = !isUnblurred }
        ) {
          if (photo.drawableRes != null) {
            Image(
              painter = painterResource(id = photo.drawableRes),
              contentDescription = photo.caption,
              contentScale = ContentScale.Crop,
              modifier = Modifier
                .fillMaxSize()
                .blur(if (isUnblurred) 0.dp else 30.dp)
            )
          }

          if (!isUnblurred) {
            Box(
              modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.2f)),
              contentAlignment = Alignment.Center
            ) {
              Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                  Icons.Default.VisibilityOff,
                  contentDescription = null,
                  tint = Color.White.copy(alpha = 0.8f),
                  modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                  text = "TAP TO REVEAL",
                  color = Color.White,
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Black,
                  letterSpacing = 0.5.sp
                )
              }
            }
          } else {
            // Unblurred indicator
            Box(
              modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(8.dp)
                .size(24.dp)
                .background(Color.Black.copy(alpha = 0.6f), CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.Visibility, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
            }
          }
        }
      }
    }
  }
}
