package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.PlanType
import com.example.ui.theme.*

@Composable
fun MembershipTierView(
    onPlanSelected: (PlanType) -> Unit,
    selectedPlan: PlanType? = null,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false
) {
    val plans = listOf(PlanType.FREE, PlanType.MONTHLY, PlanType.YEARLY, PlanType.LIFETIME_VIP)

    Column(modifier = modifier.padding(16.dp)) {
        Text(
            text = "MEMBERSHIP TIERS",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp,
            color = NeonMagenta,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (isLoading) {
            MembershipTierSkeleton()
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(plans) { plan ->
                    MembershipTierCard(
                        plan = plan,
                        isSelected = selectedPlan == plan,
                        onClick = { onPlanSelected(plan) }
                    )
                }
            }
        }
    }
}

@Composable
fun MembershipTierSkeleton() {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(4) {
            ShimmerBox(modifier = Modifier.fillMaxWidth().height(220.dp))
        }
    }
}

@Composable
fun MembershipTierCard(
    plan: PlanType,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val isVip = plan == PlanType.LIFETIME_VIP
    val borderBrush = if (isSelected) {
        if (isVip) Brush.verticalGradient(listOf(GoldVip, NeonMagenta))
        else Brush.verticalGradient(listOf(NeonMagenta, VelvetPurple))
    } else {
        Brush.verticalGradient(listOf(DesireBorder, DesireBorder))
    }

    val benefits = when (plan) {
        PlanType.FREE -> listOf("Browse Profiles", "Public Feed", "Basic Algorithm")
        PlanType.MONTHLY -> listOf("Unlimited Chat", "Full Match Feed", "Party Invites")
        PlanType.YEARLY -> listOf("All Monthly Perks", "75% Discount", "Priority Support")
        PlanType.LIFETIME_VIP -> listOf("Permanent Access", "Zero Monthly Fees", "Founder Status")
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(2.dp, borderBrush, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) DesireCardSurfaceElevated else DesireCardSurface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                if (isVip) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = GoldVip,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Text(
                    text = plan.title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isVip) GoldVip else TextPrimary,
                    textAlign = TextAlign.Center,
                    lineHeight = 16.sp
                )
                
                if (plan.saveLabel != null) {
                    Box(
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(NeonCyan)
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = plan.saveLabel,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Black,
                            color = DesireBlack
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Column(horizontalAlignment = Alignment.Start) {
                    benefits.forEach { benefit ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 1.dp)
                        ) {
                            Icon(
                                Icons.Default.Check,
                                contentDescription = null,
                                tint = if (isVip) GoldVip else NeonMagenta,
                                modifier = Modifier.size(10.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = benefit,
                                fontSize = 9.sp,
                                color = TextSecondary,
                                maxLines = 1
                            )
                        }
                    }
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = plan.priceText,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = TextPrimary
                )
                Text(
                    text = plan.billingPeriod,
                    fontSize = 9.sp,
                    color = TextMuted
                )
            }
        }
    }
}
