package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.PlanType
import com.example.ui.theme.*

@Composable
fun MembershipBadge(
    plan: PlanType,
    modifier: Modifier = Modifier
) {
    val (color, label, icon) = when (plan) {
        PlanType.FREE -> Triple(Color(0xFFCD7F32), "BRONZE", Icons.Default.Star) // Bronze
        PlanType.MONTHLY -> Triple(Color(0xFFC0C0C0), "SILVER", Icons.Default.WorkspacePremium) // Silver
        PlanType.YEARLY -> Triple(Color(0xFFC0C0C0), "SILVER", Icons.Default.WorkspacePremium) // Silver
        PlanType.LIFETIME_VIP -> Triple(GoldVip, "GOLD VIP", Icons.Default.Diamond) // Gold
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(color.copy(alpha = 0.15f))
            .border(1.dp, color.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = label,
                fontSize = 9.sp,
                fontWeight = FontWeight.ExtraBold,
                color = color,
                letterSpacing = 0.5.sp
            )
        }
    }
}
