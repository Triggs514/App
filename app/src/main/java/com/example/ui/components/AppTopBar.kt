package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.MembershipState
import com.example.model.PlanType
import com.example.ui.theme.*

@Composable
fun AppTopBar(
  membership: MembershipState,
  onMembershipClicked: () -> Unit,
  onFilterClicked: () -> Unit,
  onAiGuardianClicked: () -> Unit,
  onAiToolsClicked: () -> Unit,
  onPaymentPortalClicked: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .background(DesireBlack)
      .statusBarsPadding()
      .padding(horizontal = 14.dp, vertical = 8.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    // Brand title
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.clickable(onClick = onMembershipClicked)
    ) {
      Box(
        modifier = Modifier
          .size(32.dp)
          .clip(CircleShape)
          .background(Brush.radialGradient(listOf(NeonMagenta, VelvetPurple))),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          Icons.Default.Favorite,
          contentDescription = null,
          tint = Color.White,
          modifier = Modifier.size(16.dp)
        )
      }

      Spacer(modifier = Modifier.width(8.dp))

      Column {
        Text(
          text = "CLUB DESIRE",
          fontSize = 15.sp,
          fontWeight = FontWeight.Black,
          letterSpacing = 1.5.sp,
          color = TextPrimary
        )
        Text(
          text = "Couples • Alt • Trans • Groups",
          fontSize = 9.sp,
          fontWeight = FontWeight.Medium,
          color = NeonPinkLight
        )
      }
    }

    // Right Action Buttons: AI Shield, AI Studio, Payment, Membership Pill, Filter
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
      // AI Sentinel Bot Shield Button
      IconButton(
        onClick = onAiGuardianClicked,
        modifier = Modifier
          .size(32.dp)
          .clip(CircleShape)
          .background(DesireCardSurface)
          .border(1.dp, NeonCyan.copy(alpha = 0.6f), CircleShape)
      ) {
        Icon(
          Icons.Default.Shield,
          contentDescription = "AI Guardian & Bot Shield",
          tint = NeonCyan,
          modifier = Modifier.size(16.dp)
        )
      }

      // AI Creative Tools Button
      IconButton(
        onClick = onAiToolsClicked,
        modifier = Modifier
          .size(32.dp)
          .clip(CircleShape)
          .background(DesireCardSurface)
          .border(1.dp, NeonMagenta.copy(alpha = 0.6f), CircleShape)
      ) {
        Icon(
          Icons.Default.AutoFixHigh,
          contentDescription = "AI Porno & Face Swap Studio",
          tint = NeonMagenta,
          modifier = Modifier.size(16.dp)
        )
      }

      // Payment Gateway Portal Icon
      IconButton(
        onClick = onPaymentPortalClicked,
        modifier = Modifier
          .size(32.dp)
          .clip(CircleShape)
          .background(DesireCardSurface)
          .border(1.dp, GoldVip.copy(alpha = 0.6f), CircleShape)
      ) {
        Icon(
          Icons.Default.CreditCard,
          contentDescription = "Stripe, PayPal, Mobile Payments & E-Transfer",
          tint = GoldVip,
          modifier = Modifier.size(16.dp)
        )
      }

      // Membership pill
      Box(
        modifier = Modifier
          .clip(RoundedCornerShape(16.dp))
          .background(
            if (membership.hasPremium18PlusUpgrade) {
              Brush.horizontalGradient(listOf(GoldVip.copy(alpha = 0.2f), Color(0xFFFFB703).copy(alpha = 0.3f)))
            } else if (membership.plan != PlanType.FREE) {
              Brush.horizontalGradient(listOf(VelvetPurple.copy(alpha = 0.25f), NeonMagenta.copy(alpha = 0.25f)))
            } else {
              Brush.horizontalGradient(listOf(NeonMagenta.copy(alpha = 0.3f), VelvetPurple.copy(alpha = 0.3f)))
            }
          )
          .border(
            1.dp,
            if (membership.hasPremium18PlusUpgrade) GoldVip
            else if (membership.plan != PlanType.FREE) VelvetPurple
            else NeonMagenta,
            RoundedCornerShape(16.dp)
          )
          .clickable(onClick = onMembershipClicked)
          .padding(horizontal = 8.dp, vertical = 4.dp)
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          if (membership.hasPremium18PlusUpgrade) {
            Icon(Icons.Default.VpnKey, contentDescription = null, tint = GoldVip, modifier = Modifier.size(12.dp))
            Spacer(modifier = Modifier.width(3.dp))
            Text(
              text = if (membership.isLifetimeVip) "VIP FREE" else "VIP 18+",
              fontSize = 10.sp,
              fontWeight = FontWeight.ExtraBold,
              color = GoldVip
            )
          } else if (membership.plan != PlanType.FREE) {
            Icon(Icons.Default.Star, contentDescription = null, tint = NeonMagenta, modifier = Modifier.size(12.dp))
            Spacer(modifier = Modifier.width(3.dp))
            Text(
              text = "MEMBER",
              fontSize = 10.sp,
              fontWeight = FontWeight.ExtraBold,
              color = NeonMagenta
            )
          } else {
            Text(
              text = "UPGRADE",
              fontSize = 10.sp,
              fontWeight = FontWeight.ExtraBold,
              color = NeonMagenta
            )
          }
        }
      }
    }
  }
}
