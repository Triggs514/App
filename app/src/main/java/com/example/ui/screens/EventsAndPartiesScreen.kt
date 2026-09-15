package com.example.ui.screens

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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.SampleDataRepository
import com.example.model.DynamicKink
import com.example.model.GroupEvent
import com.example.model.MembershipState
import com.example.ui.theme.*

@Composable
fun EventsAndPartiesScreen(
  membership: MembershipState,
  onOpenUpgrade: () -> Unit
) {
  val events by SampleDataRepository.events.collectAsState()
  var rsvpEventIds by remember { mutableStateOf(setOf<String>()) }
  var rsvpMessage by remember { mutableStateOf<String?>(null) }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(DesireBlack)
  ) {
    // Header
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .background(
          Brush.verticalGradient(
            listOf(Color(0xFF26102D), DesireDarkSurface)
          )
        )
        .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "EXCLUSIVE PLAY SPACES",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
            color = VelvetPurple
          )
          Text(
            text = "Parties & Group Encounters",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
          )
          Text(
            text = "Orgies, gangbang showcases, threesome & goth mixers.",
            fontSize = 12.sp,
            color = TextSecondary
          )
        }

        Box(
          modifier = Modifier
            .size(42.dp)
            .clip(CircleShape)
            .background(Brush.radialGradient(listOf(VelvetPurple, NeonMagenta))),
          contentAlignment = Alignment.Center
        ) {
          Icon(Icons.Default.Groups, contentDescription = null, tint = Color.White, modifier = Modifier.size(22.dp))
        }
      }
    }

    if (rsvpMessage != null) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .padding(12.dp)
          .clip(RoundedCornerShape(12.dp))
          .background(SuccessGreen.copy(alpha = 0.2f))
          .border(1.dp, SuccessGreen, RoundedCornerShape(12.dp))
          .padding(12.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = SuccessGreen)
            Spacer(modifier = Modifier.width(8.dp))
            Text(rsvpMessage ?: "", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
          }
          IconButton(
            onClick = { rsvpMessage = null },
            modifier = Modifier.size(24.dp)
          ) {
            Icon(Icons.Default.Close, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(16.dp))
          }
        }
      }
    }

    LazyColumn(
      modifier = Modifier.fillMaxSize(),
      contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 90.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      items(events, key = { it.id }) { event ->
        val isRsvpd = rsvpEventIds.contains(event.id)
        EventCard(
          event = event,
          isRsvpd = isRsvpd,
          onRsvpClick = {
            rsvpEventIds = if (isRsvpd) rsvpEventIds - event.id else rsvpEventIds + event.id
            rsvpMessage = if (!isRsvpd) "RSVP confirmed for ${event.title}! Hosts will verify your profile." else "RSVP cancelled."
          }
        )
      }
    }
  }
}

@Composable
fun EventCard(
  event: GroupEvent,
  isRsvpd: Boolean,
  onRsvpClick: () -> Unit
) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(18.dp))
      .background(DesireCardSurface)
      .border(
        1.dp,
        if (isRsvpd) SuccessGreen else DesireBorder,
        RoundedCornerShape(18.dp)
      )
      .padding(16.dp)
  ) {
    Column {
      // Top row: Kink Badge & Date
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color(event.kinkType.badgeColorHex).copy(alpha = 0.2f))
            .border(1.dp, Color(event.kinkType.badgeColorHex).copy(alpha = 0.6f), RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
          Text(
            text = event.kinkType.displayName,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color(event.kinkType.badgeColorHex)
          )
        }

        if (event.isVipOnly) {
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(Color(0xFF381C08))
              .border(1.dp, GoldVip, RoundedCornerShape(8.dp))
              .padding(horizontal = 8.dp, vertical = 4.dp)
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.VpnKey, contentDescription = null, tint = GoldVip, modifier = Modifier.size(12.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("VIP VETTED", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = GoldVip)
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      Text(
        text = event.title,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = TextPrimary
      )

      Spacer(modifier = Modifier.height(6.dp))

      // Date & Location Info
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.CalendarToday, contentDescription = null, tint = NeonMagenta, modifier = Modifier.size(14.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = event.dateText, fontSize = 12.sp, color = TextSecondary)
      }

      Spacer(modifier = Modifier.height(4.dp))

      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.Place, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(14.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = event.locationArea, fontSize = 12.sp, color = TextSecondary)
      }

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = event.description,
        fontSize = 13.sp,
        color = TextSecondary,
        lineHeight = 18.sp
      )

      Spacer(modifier = Modifier.height(14.dp))

      // Host & RSVP
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(text = "Hosted by ${event.hostName}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
          Text(
            text = "${event.attendeesCount + if (isRsvpd) 1 else 0} / ${event.maxAttendees} Attending",
            fontSize = 11.sp,
            color = TextMuted
          )
        }

        Button(
          onClick = onRsvpClick,
          colors = ButtonDefaults.buttonColors(
            containerColor = if (isRsvpd) SuccessGreen else NeonMagenta
          ),
          shape = RoundedCornerShape(10.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              if (isRsvpd) Icons.Default.Check else Icons.Default.Add,
              contentDescription = null,
              tint = Color.White,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = if (isRsvpd) "RSVP'D" else "REQUEST RSVP",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
          }
        }
      }
    }
  }
}
