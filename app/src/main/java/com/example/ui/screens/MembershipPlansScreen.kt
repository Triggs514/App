package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun MembershipPlansScreen(
    onSelectPlan: (String) -> Unit = {}
) {
    var selectedTier by remember { mutableStateOf("Premium") }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DesireBlack)
            .verticalScroll(scrollState)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Text(
            text = "MEMBERSHIP PLANS",
            fontSize = 24.sp,
            fontWeight = FontWeight.Black,
            color = NeonMagenta,
            letterSpacing = 2.sp,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        Text(
            text = "Unlock exclusive experiences, unlimited connections & VIP privileges",
            fontSize = 13.sp,
            color = TextSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Tiers Cards Row / Column
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            // Basic Plan
            PlanCard(
                title = "Basic",
                price = "Free",
                subtitle = "Standard discovery and messaging",
                isPopular = false,
                borderColor = DesireBorder,
                containerColor = DesireCardSurface,
                buttonText = if (selectedTier == "Basic") "Current Plan" else "Select Basic",
                buttonContainerColor = DesireDarkSurface,
                buttonTextColor = TextPrimary,
                testTag = "basic_plan_card",
                onClick = { selectedTier = "Basic"; onSelectPlan("Basic") }
            )

            // Premium Plan
            PlanCard(
                title = "Premium",
                price = "$14.99 / mo",
                subtitle = "Priority matching & unlimited likes",
                isPopular = true,
                borderColor = NeonMagenta,
                containerColor = DesireCardSurface,
                buttonText = if (selectedTier == "Premium") "Active Plan" else "Upgrade to Premium",
                buttonContainerColor = NeonMagenta,
                buttonTextColor = Color.White,
                testTag = "premium_plan_card",
                onClick = { selectedTier = "Premium"; onSelectPlan("Premium") }
            )

            // Elite VIP Plan
            PlanCard(
                title = "Elite VIP",
                price = "$29.99 / mo",
                subtitle = "Full 18+ access, private events & VIP badge",
                isPopular = false,
                borderColor = GoldVip,
                containerColor = DesireCardSurface,
                buttonText = if (selectedTier == "Elite") "Active VIP" else "Go Elite VIP",
                buttonContainerColor = GoldVip,
                buttonTextColor = DesireBlack,
                testTag = "elite_plan_card",
                onClick = { selectedTier = "Elite"; onSelectPlan("Elite") }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Feature Comparison Table
        Text(
            text = "FEATURE COMPARISON",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            modifier = Modifier.align(Alignment.Start).padding(bottom = 12.dp)
        )

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DesireCardSurface),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("feature_comparison_table")
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                ComparisonHeaderRow()
                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider(color = DesireBorder)
                Spacer(modifier = Modifier.height(8.dp))

                ComparisonRow(feature = "Profile Discovery", basic = "Standard", premium = "Priority", elite = "Top Tier")
                ComparisonRow(feature = "Direct Messaging", basic = "Limited", premium = "Unlimited", elite = "Unlimited + VIP")
                ComparisonRow(feature = "See Who Liked You", basic = "Locked", premium = "Unlocked", elite = "Unlocked + Highlight")
                ComparisonBoolRow(feature = "Verified Badge", basic = false, premium = true, elite = true)
                ComparisonBoolRow(feature = "Exclusive 18+ Events", basic = false, premium = false, elite = true)
                ComparisonBoolRow(feature = "Incognito Browsing", basic = false, premium = true, elite = true)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun PlanCard(
    title: String,
    price: String,
    subtitle: String,
    isPopular: Boolean,
    borderColor: Color,
    containerColor: Color,
    buttonText: String,
    buttonContainerColor: Color,
    buttonTextColor: Color,
    testTag: String,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, borderColor, RoundedCornerShape(20.dp))
            .testTag(testTag)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                if (isPopular) {
                    Surface(
                        color = NeonMagenta.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "MOST POPULAR",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeonMagenta,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = price,
                fontSize = 22.sp,
                fontWeight = FontWeight.Black,
                color = if (title == "Elite VIP") GoldVip else NeonCyan
            )

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = subtitle,
                fontSize = 13.sp,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(containerColor = buttonContainerColor),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
            ) {
                Text(
                    text = buttonText,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = buttonTextColor
                )
            }
        }
    }
}

@Composable
fun ComparisonHeaderRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "Features", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextSecondary, modifier = Modifier.weight(1.5f))
        Text(text = "Basic", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextSecondary, textAlign = TextAlign.Center, modifier = Modifier.weight(1f))
        Text(text = "Premium", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = NeonMagenta, textAlign = TextAlign.Center, modifier = Modifier.weight(1f))
        Text(text = "Elite", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = GoldVip, textAlign = TextAlign.Center, modifier = Modifier.weight(1f))
    }
}

@Composable
fun ComparisonRow(feature: String, basic: String, premium: String, elite: String) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = feature, fontSize = 13.sp, color = TextPrimary, modifier = Modifier.weight(1.5f))
            Text(text = basic, fontSize = 12.sp, color = TextSecondary, textAlign = TextAlign.Center, modifier = Modifier.weight(1f))
            Text(text = premium, fontSize = 12.sp, color = NeonCyan, fontWeight = FontWeight.Medium, textAlign = TextAlign.Center, modifier = Modifier.weight(1f))
            Text(text = elite, fontSize = 12.sp, color = GoldVip, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.weight(1f))
        }
        HorizontalDivider(color = DesireBorder.copy(alpha = 0.3f), thickness = 0.5.dp)
    }
}

@Composable
fun ComparisonBoolRow(feature: String, basic: Boolean, premium: Boolean, elite: Boolean) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = feature, fontSize = 13.sp, color = TextPrimary, modifier = Modifier.weight(1.5f))
            
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                if (basic) Icon(Icons.Default.Check, contentDescription = "Yes", tint = SuccessGreen, modifier = Modifier.size(16.dp))
                else Icon(Icons.Default.Close, contentDescription = "No", tint = TextSecondary.copy(alpha = 0.5f), modifier = Modifier.size(16.dp))
            }
            
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                if (premium) Icon(Icons.Default.Check, contentDescription = "Yes", tint = NeonCyan, modifier = Modifier.size(16.dp))
                else Icon(Icons.Default.Close, contentDescription = "No", tint = TextSecondary.copy(alpha = 0.5f), modifier = Modifier.size(16.dp))
            }
            
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                if (elite) Icon(Icons.Default.Check, contentDescription = "Yes", tint = GoldVip, modifier = Modifier.size(16.dp))
                else Icon(Icons.Default.Close, contentDescription = "No", tint = TextSecondary.copy(alpha = 0.5f), modifier = Modifier.size(16.dp))
            }
        }
        HorizontalDivider(color = DesireBorder.copy(alpha = 0.3f), thickness = 0.5.dp)
    }
}
