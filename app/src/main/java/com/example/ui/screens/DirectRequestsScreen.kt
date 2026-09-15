package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.R
import com.example.data.SampleDataRepository
import com.example.model.*
import com.example.ui.theme.*

@Composable
fun DirectRequestsScreen(
  membership: MembershipState,
  onOpenUpgrade: () -> Unit
) {
  var selectedTab by remember { mutableStateOf(0) } // 0 = Sex Requests, 1 = 18+ Private Vault
  val requests by SampleDataRepository.sexRequests.collectAsState()
  val profiles by SampleDataRepository.profiles.collectAsState()

  val allVaultItems = remember(profiles) {
    profiles.flatMap { prof ->
      prof.vaultItems.map { item -> Pair(prof, item) }
    }
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(DesireBlack)
  ) {
    // Header Banner
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .background(
          Brush.verticalGradient(
            listOf(Color(0xFF260D20), DesireDarkSurface)
          )
        )
        .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
      Column {
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
                text = "PREMIUM 18+ HUB",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                color = GoldVip
              )
              Text(
                text = "Sex Requests & Vault",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
              )
            }
          }

          // Status Badge
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(12.dp))
              .background(if (membership.canSendSexRequests) GoldVip else DesireCardSurface)
              .border(1.dp, if (membership.canSendSexRequests) GoldVip else NeonMagenta, RoundedCornerShape(12.dp))
              .clickable(onClick = onOpenUpgrade)
              .padding(horizontal = 10.dp, vertical = 5.dp)
          ) {
            Text(
              text = if (membership.canSendSexRequests) "18+ VIP ACTIVE" else "UNLOCK ($15)",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = if (membership.canSendSexRequests) DesireBlack else NeonMagenta
            )
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Tab Selector: Sex Requests vs 18+ Content Feed
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(DesireCardSurface)
            .padding(4.dp)
        ) {
          TabButton(
            text = "Sex Requests (${requests.size})",
            isSelected = selectedTab == 0,
            onClick = { selectedTab = 0 },
            modifier = Modifier.weight(1f)
          )
          TabButton(
            text = "18+ Private Vault Feed",
            isSelected = selectedTab == 1,
            onClick = { selectedTab = 1 },
            modifier = Modifier.weight(1f)
          )
        }
      }
    }

    if (selectedTab == 0) {
      // Sex Requests Tab
      SexRequestsList(
        requests = requests,
        membership = membership,
        onOpenUpgrade = onOpenUpgrade,
        onStatusChange = { reqId, status ->
          SampleDataRepository.updateRequestStatus(reqId, status)
        }
      )
    } else {
      // 18+ Private Vault Feed
      VaultFeedList(
        vaultItems = allVaultItems,
        membership = membership,
        onOpenUpgrade = onOpenUpgrade
      )
    }
  }
}

@Composable
fun TabButton(
  text: String,
  isSelected: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .clip(RoundedCornerShape(10.dp))
      .background(
        if (isSelected) Brush.horizontalGradient(listOf(NeonMagenta, VelvetPurple))
        else Brush.linearGradient(listOf(Color.Transparent, Color.Transparent))
      )
      .clickable(onClick = onClick)
      .padding(vertical = 8.dp),
    contentAlignment = Alignment.Center
  ) {
    Text(
      text = text,
      fontSize = 12.sp,
      fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
      color = if (isSelected) Color.White else TextSecondary
    )
  }
}

@Composable
fun SexRequestsList(
  requests: List<DirectSexRequest>,
  membership: MembershipState,
  onOpenUpgrade: () -> Unit,
  onStatusChange: (String, RequestStatus) -> Unit
) {
  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 14.dp, bottom = 90.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // If not upgraded, show prominent prompt
    if (!membership.canSendSexRequests) {
      item {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
              Brush.horizontalGradient(
                listOf(Color(0xFF381500), Color(0xFF220935))
              )
            )
            .border(1.dp, GoldVip, RoundedCornerShape(16.dp))
            .padding(16.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(42.dp)
                .background(GoldVip, CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.Lock, contentDescription = null, tint = DesireBlack, modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "Sex Requests Locked ($15 Upgrade)",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = GoldVip
              )
              Text(
                text = "Upgrade for $15 to send explicit propositions & respond to fantasy requests.",
                fontSize = 12.sp,
                color = TextSecondary
              )
            }
            Button(
              onClick = onOpenUpgrade,
              colors = ButtonDefaults.buttonColors(containerColor = GoldVip),
              shape = RoundedCornerShape(10.dp),
              contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
            ) {
              Text("Upgrade", color = DesireBlack, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
          }
        }
      }
    }

    item {
      Text(
        text = "ACTIVE INTIMATE PROPOSITIONS",
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp,
        color = TextSecondary
      )
    }

    items(requests, key = { it.id }) { req ->
      SexRequestCard(
        request = req,
        onAccept = { onStatusChange(req.id, RequestStatus.ACCEPTED) },
        onDecline = { onStatusChange(req.id, RequestStatus.DECLINED) }
      )
    }
  }
}

@Composable
fun SexRequestCard(
  request: DirectSexRequest,
  onAccept: () -> Unit,
  onDecline: () -> Unit
) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(16.dp))
      .background(DesireCardSurface)
      .border(
        1.dp,
        if (request.status == RequestStatus.ACCEPTED) SuccessGreen
        else if (request.status == RequestStatus.DECLINED) DesireBorder
        else NeonMagenta.copy(alpha = 0.5f),
        RoundedCornerShape(16.dp)
      )
      .padding(16.dp)
  ) {
    Column {
      // Header: Sender & Category
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = request.fromUserName,
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold,
              color = TextPrimary
            )
            Spacer(modifier = Modifier.width(6.dp))
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(Color(request.category.badgeColorHex).copy(alpha = 0.2f))
                .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
              Text(
                text = request.category.displayName,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Color(request.category.badgeColorHex)
              )
            }
          }
          Text(
            text = request.timeAgo,
            fontSize = 11.sp,
            color = TextMuted
          )
        }

        // Status Badge
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(
              when (request.status) {
                RequestStatus.ACCEPTED -> SuccessGreen.copy(alpha = 0.2f)
                RequestStatus.DECLINED -> SpicyRed.copy(alpha = 0.2f)
                RequestStatus.PENDING -> GoldVip.copy(alpha = 0.2f)
              }
            )
            .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
          Text(
            text = request.status.name,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = when (request.status) {
              RequestStatus.ACCEPTED -> SuccessGreen
              RequestStatus.DECLINED -> SpicyRed
              RequestStatus.PENDING -> GoldVip
            }
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      Text(
        text = request.proposalTitle,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = NeonPinkLight
      )
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = request.description,
        fontSize = 13.sp,
        color = TextPrimary,
        lineHeight = 18.sp
      )

      Spacer(modifier = Modifier.height(12.dp))

      // Venue & Experience pills
      Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(DesireCardSurfaceElevated)
            .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Place, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(12.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(request.preferredVenue, fontSize = 11.sp, color = TextSecondary)
          }
        }

        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(DesireCardSurfaceElevated)
            .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Star, contentDescription = null, tint = GoldVip, modifier = Modifier.size(12.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(request.experienceLevel, fontSize = 11.sp, color = TextSecondary)
          }
        }
      }

      if (request.status == RequestStatus.PENDING) {
        Spacer(modifier = Modifier.height(14.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          OutlinedButton(
            onClick = onDecline,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = TextSecondary),
            border = androidx.compose.foundation.BorderStroke(1.dp, DesireBorder),
            shape = RoundedCornerShape(10.dp)
          ) {
            Text("Decline", fontSize = 12.sp)
          }

          Button(
            onClick = onAccept,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(containerColor = NeonMagenta),
            shape = RoundedCornerShape(10.dp)
          ) {
            Text("Accept & Connect", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
          }
        }
      }
    }
  }
}

@Composable
fun VaultFeedList(
  vaultItems: List<Pair<UserProfile, VaultMediaItem>>,
  membership: MembershipState,
  onOpenUpgrade: () -> Unit
) {
  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 14.dp, bottom = 90.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Banner header
    item {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(160.dp)
          .clip(RoundedCornerShape(16.dp))
          .background(
            Brush.linearGradient(
              listOf(Color(0xFF3A0623), Color(0xFF1E0736))
            )
          )
      ) {
        Image(
          painter = painterResource(id = R.drawable.vip_vault_banner),
          contentDescription = "VIP 18+ Vault",
          contentScale = ContentScale.Crop,
          modifier = Modifier
            .fillMaxSize()
            .blur(if (!membership.canView18PlusContent) 8.dp else 0.dp)
        )

        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.65f))
            .padding(16.dp),
          contentAlignment = Alignment.CenterStart
        ) {
          Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.VpnKey, contentDescription = null, tint = GoldVip, modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "18+ EXCLUSIVE VAULT FEED",
                fontWeight = FontWeight.Black,
                fontSize = 12.sp,
                letterSpacing = 1.sp,
                color = GoldVip
              )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = if (membership.canView18PlusContent) "VIP Unlocked • Uncensored Media" else "Requires $15 Premium 18+ Upgrade",
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "Couple intimacy reels, private goth shoots & group highlights.",
              fontSize = 12.sp,
              color = TextSecondary
            )
            if (!membership.canView18PlusContent) {
              Spacer(modifier = Modifier.height(8.dp))
              Button(
                onClick = onOpenUpgrade,
                colors = ButtonDefaults.buttonColors(containerColor = GoldVip),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
              ) {
                Text("Unlock for $15", color = DesireBlack, fontWeight = FontWeight.Bold, fontSize = 11.sp)
              }
            }
          }
        }
      }
    }

    items(vaultItems) { (profile, item) ->
      VaultFeedCard(
        profile = profile,
        item = item,
        isUnlocked = membership.canView18PlusContent,
        onUnlockClicked = onOpenUpgrade
      )
    }
  }
}

@Composable
fun VaultFeedCard(
  profile: UserProfile,
  item: VaultMediaItem,
  isUnlocked: Boolean,
  onUnlockClicked: () -> Unit
) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(18.dp))
      .background(DesireCardSurface)
      .border(1.dp, if (isUnlocked) GoldVip.copy(alpha = 0.5f) else DesireBorder, RoundedCornerShape(18.dp))
  ) {
    Column {
      // Header: Profile info
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(36.dp)
              .clip(CircleShape)
              .background(Brush.radialGradient(listOf(NeonMagenta, VelvetPurple))),
            contentAlignment = Alignment.Center
          ) {
            Text(profile.userType.icon, fontSize = 16.sp)
          }
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Text(
              text = profile.name,
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold,
              color = TextPrimary
            )
            Text(
              text = "${profile.userType.label} • ${profile.subculture}",
              fontSize = 11.sp,
              color = TextSecondary
            )
          }
        }

        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(Color(0xFF381C08))
            .padding(horizontal = 8.dp, vertical = 3.dp)
        ) {
          Text(item.durationOrCount, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = GoldVip)
        }
      }

      // Media Preview Box
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(200.dp)
          .background(
            Brush.linearGradient(
              listOf(Color(item.blurCoverGradient.first), Color(item.blurCoverGradient.second))
            )
          )
          .clickable {
            if (!isUnlocked) onUnlockClicked()
          }
      ) {
        if (item.previewRes != null) {
          Image(
            painter = painterResource(id = item.previewRes),
            contentDescription = item.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
              .fillMaxSize()
              .then(if (!isUnlocked) Modifier.blur(20.dp) else Modifier)
          )
        }

        // Overlay Scrim
        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(
              if (!isUnlocked) Color.Black.copy(alpha = 0.72f) else Color.Black.copy(alpha = 0.3f)
            )
        )

        if (!isUnlocked) {
          Column(
            modifier = Modifier
              .fillMaxSize()
              .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
          ) {
            Icon(Icons.Default.Lock, contentDescription = null, tint = GoldVip, modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = "18+ PRIVATE VAULT CONTENT",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              letterSpacing = 1.sp,
              color = GoldVip
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "Unlock with $15 Premium Upgrade",
              fontSize = 11.sp,
              color = TextSecondary
            )
            Spacer(modifier = Modifier.height(10.dp))
            Button(
              onClick = onUnlockClicked,
              colors = ButtonDefaults.buttonColors(containerColor = GoldVip),
              shape = RoundedCornerShape(8.dp),
              contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
            ) {
              Text("Unlock Vault ($15)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DesireBlack)
            }
          }
        } else {
          // Play indicator / Unlocked tag
          Box(
            modifier = Modifier
              .align(Alignment.Center)
              .size(52.dp)
              .background(Color.Black.copy(alpha = 0.6f), CircleShape)
              .border(1.5.dp, GoldVip, CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Icon(Icons.Default.PlayArrow, contentDescription = "Play", tint = Color.White, modifier = Modifier.size(30.dp))
          }
        }
      }

      // Media details
      Column(modifier = Modifier.padding(14.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = item.title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
          )
          Text(
            text = item.mediaCategory,
            fontSize = 11.sp,
            color = NeonPinkLight
          )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = item.description,
          fontSize = 12.sp,
          color = TextSecondary
        )
      }
    }
  }
}
