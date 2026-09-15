package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.SampleDataRepository
import com.example.model.LIFETIME_VIP_EMAILS
import com.example.model.PlanType
import com.example.ui.theme.*

@Composable
fun MembershipUpgradeDialog(
  onDismiss: () -> Unit,
  onSubscribedSuccess: (String) -> Unit = {}
) {
  val currentMembership by SampleDataRepository.membershipState.collectAsState()

  var inputEmail by remember(currentMembership.userEmail) {
    mutableStateOf(currentMembership.userEmail)
  }
  var selectedPlan by remember {
    mutableStateOf(
      if (currentMembership.plan == PlanType.FREE) PlanType.MONTHLY else currentMembership.plan
    )
  }
  var include18PlusUpgrade by remember {
    mutableStateOf(currentMembership.hasPremium18PlusUpgrade)
  }
  var showPaymentPortal by remember { mutableStateOf(false) }

  val isVipEmail = remember(inputEmail) {
    LIFETIME_VIP_EMAILS.any { it.equals(inputEmail.trim(), ignoreCase = true) }
  }

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      modifier = Modifier
        .fillMaxWidth(0.95f)
        .fillMaxHeight(0.94f)
        .clip(RoundedCornerShape(24.dp))
        .border(
          1.5.dp,
          if (isVipEmail || currentMembership.isLifetimeVip) Brush.verticalGradient(listOf(GoldVip, NeonMagenta))
          else Brush.verticalGradient(listOf(NeonMagenta, VelvetPurple)),
          RoundedCornerShape(24.dp)
        ),
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
          Column {
            Text(
              text = "CLUB DESIRE PRIVILEGES",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              letterSpacing = 2.sp,
              color = if (isVipEmail || currentMembership.isLifetimeVip) GoldVip else NeonMagenta
            )
            Text(
              text = "Subscriptions & 18+ Access",
              fontSize = 19.sp,
              fontWeight = FontWeight.Bold,
              color = TextPrimary
            )
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

        // VIP Founder Email Recognition Box (Direct user requirement)
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
              if (isVipEmail || currentMembership.isLifetimeVip) {
                Brush.linearGradient(listOf(Color(0xFF381E05), Color(0xFF1E0A24)))
              } else {
                Brush.linearGradient(listOf(DesireCardSurface, DesireCardSurfaceElevated))
              }
            )
            .border(
              1.dp,
              if (isVipEmail || currentMembership.isLifetimeVip) GoldVip else DesireBorder,
              RoundedCornerShape(16.dp)
            )
            .padding(14.dp)
        ) {
          Column {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  Icons.Default.VerifiedUser,
                  contentDescription = null,
                  tint = if (isVipEmail || currentMembership.isLifetimeVip) GoldVip else NeonCyan,
                  modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = if (isVipEmail || currentMembership.isLifetimeVip) "LIFETIME VIP FOUNDER" else "ACCOUNT EMAIL VERIFICATION",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = if (isVipEmail || currentMembership.isLifetimeVip) GoldVip else NeonCyan,
                  letterSpacing = 1.sp
                )
              }

              if (isVipEmail || currentMembership.isLifetimeVip) {
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(GoldVip)
                    .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                  Text("$0 FOR LIFE", fontSize = 10.sp, fontWeight = FontWeight.Black, color = DesireBlack)
                }
              }
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
              value = inputEmail,
              onValueChange = {
                inputEmail = it
                SampleDataRepository.setUserEmail(it)
              },
              label = { Text("Account Email") },
              modifier = Modifier.fillMaxWidth(),
              singleLine = true,
              colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = DesireCardSurface,
                unfocusedContainerColor = DesireCardSurface,
                focusedBorderColor = if (isVipEmail) GoldVip else NeonMagenta,
                unfocusedBorderColor = if (isVipEmail) GoldVip.copy(alpha = 0.6f) else DesireBorder,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary
              ),
              shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Quick Switcher for triggz2905@gmail.com and LaurenCartier6@gmail.com
            Text(
              text = "Lifetime VIP Founder Accounts (Free for life):",
              fontSize = 11.sp,
              color = TextMuted
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Button(
                onClick = {
                  inputEmail = "triggz2905@gmail.com"
                  SampleDataRepository.grantLifetimeVipToEmail("triggz2905@gmail.com")
                },
                colors = ButtonDefaults.buttonColors(
                  containerColor = if (inputEmail.equals("triggz2905@gmail.com", true)) GoldVip else DesireCardSurfaceElevated
                ),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                modifier = Modifier.weight(1f)
              ) {
                Text(
                  "triggz2905@...",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = if (inputEmail.equals("triggz2905@gmail.com", true)) DesireBlack else GoldVip
                )
              }

              Button(
                onClick = {
                  inputEmail = "LaurenCartier6@gmail.com"
                  SampleDataRepository.grantLifetimeVipToEmail("LaurenCartier6@gmail.com")
                },
                colors = ButtonDefaults.buttonColors(
                  containerColor = if (inputEmail.equals("LaurenCartier6@gmail.com", true)) GoldVip else DesireCardSurfaceElevated
                ),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                modifier = Modifier.weight(1f)
              ) {
                Text(
                  "LaurenCartier6@...",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = if (inputEmail.equals("LaurenCartier6@gmail.com", true)) DesireBlack else GoldVip
                )
              }
            }

            if (isVipEmail || currentMembership.isLifetimeVip) {
              Spacer(modifier = Modifier.height(8.dp))
              Text(
                text = "✨ You have Lifetime VIP Founder status. All standard membership ($15/mo, $45/yr) and the $15 premium 18+ upgrade are unlocked forever for $0.",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = GoldVip
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Step 1: Standard Membership Tier
        Text(
          text = "1. CHOOSE STANDARD MEMBERSHIP TIER",
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.sp,
          color = TextSecondary
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Monthly $15 vs Annual $45 cards
        PlanOptionCard(
          plan = PlanType.MONTHLY,
          price = "$15",
          period = "per month",
          subtitle = "Full chat, match feed & party RSVPs",
          badge = null,
          isSelected = selectedPlan == PlanType.MONTHLY,
          onClick = { selectedPlan = PlanType.MONTHLY }
        )

        Spacer(modifier = Modifier.height(10.dp))

        PlanOptionCard(
          plan = PlanType.YEARLY,
          price = "$45",
          period = "per year ($3.75/mo)",
          subtitle = "Save 75% compared to monthly. Best value for couples & alt lovers",
          badge = "SAVE 75%",
          isSelected = selectedPlan == PlanType.YEARLY,
          onClick = { selectedPlan = PlanType.YEARLY }
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Step 2: $15 Premium 18+ Upgrade (Direct user requirement)
        Text(
          text = "2. PREMIUM 18+ SEX REQUESTS & VAULT UPGRADE",
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.sp,
          color = GoldVip
        )
        Spacer(modifier = Modifier.height(8.dp))

        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
              if (include18PlusUpgrade || isVipEmail) {
                Brush.linearGradient(listOf(Color(0xFF331500), Color(0xFF22082E)))
              } else {
                Brush.linearGradient(listOf(DesireCardSurface, DesireCardSurface))
              }
            )
            .border(
              1.5.dp,
              if (include18PlusUpgrade || isVipEmail) GoldVip else DesireBorder,
              RoundedCornerShape(16.dp)
            )
            .clickable {
              if (!isVipEmail) {
                include18PlusUpgrade = !include18PlusUpgrade
              }
            }
            .padding(16.dp)
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
                    .size(24.dp)
                    .background(if (include18PlusUpgrade || isVipEmail) GoldVip else DesireBorder, RoundedCornerShape(6.dp)),
                  contentAlignment = Alignment.Center
                ) {
                  if (include18PlusUpgrade || isVipEmail) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = DesireBlack, modifier = Modifier.size(18.dp))
                  }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                  Text(
                    text = "18+ Premium Upgrade",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (include18PlusUpgrade || isVipEmail) GoldVip else TextPrimary
                  )
                  Text(
                    text = if (isVipEmail) "$0 Included (Lifetime VIP)" else "+$15 Upgrade Fee",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isVipEmail) SuccessGreen else NeonMagenta
                  )
                }
              }

              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(8.dp))
                  .background(if (include18PlusUpgrade || isVipEmail) GoldVip else DesireBorder)
                  .padding(horizontal = 8.dp, vertical = 4.dp)
              ) {
                Text(
                  text = if (isVipEmail) "VIP UNLOCKED" else if (include18PlusUpgrade) "SELECTED" else "ADD (+$15)",
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold,
                  color = if (include18PlusUpgrade || isVipEmail) DesireBlack else TextSecondary
                )
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
              FeatureCheckRow(
                text = "Submit & receive Direct Sex Requests (threesomes, spit roasting, orgies, gang bangs)",
                isUnlocked = include18PlusUpgrade || isVipEmail
              )
              FeatureCheckRow(
                text = "Full access to 18+ Private Vault Feed (uncensored couple reels & photo galleries)",
                isUnlocked = include18PlusUpgrade || isVipEmail
              )
              FeatureCheckRow(
                text = "Gold 'VIP 18+' Badge displayed prominently on your profile & propositions",
                isUnlocked = include18PlusUpgrade || isVipEmail
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Feature Comparison Table: Free vs Standard vs Premium 18+
        Text(
          text = "3. PLAN COMPARISON MATRIX",
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.sp,
          color = TextSecondary
        )
        Spacer(modifier = Modifier.height(8.dp))

        ComparisonTable()

        Spacer(modifier = Modifier.height(24.dp))

        // Action Button: Subscribe or Activate
        val totalDue = when {
          isVipEmail || currentMembership.isLifetimeVip -> "$0 (Lifetime VIP Active)"
          selectedPlan == PlanType.YEARLY && include18PlusUpgrade -> "$60 ($45/yr + $15 18+)"
          selectedPlan == PlanType.YEARLY && !include18PlusUpgrade -> "$45/year"
          selectedPlan == PlanType.MONTHLY && include18PlusUpgrade -> "$30 ($15/mo + $15 18+)"
          selectedPlan == PlanType.MONTHLY && !include18PlusUpgrade -> "$15/month"
          else -> "$0"
        }

        // Action Button: Open Payment Portal (Stripe, PayPal, Mobile Pay, E-Transfer)
        Button(
          onClick = {
            showPaymentPortal = true
          },
          modifier = Modifier
            .fillMaxWidth()
            .height(54.dp),
          colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
          shape = RoundedCornerShape(16.dp),
          contentPadding = PaddingValues(0.dp)
        ) {
          Box(
            modifier = Modifier
              .fillMaxSize()
              .background(
                if (isVipEmail || include18PlusUpgrade) Brush.horizontalGradient(listOf(GoldVip, NeonMagenta))
                else Brush.horizontalGradient(listOf(NeonCyan, NeonMagenta)),
                RoundedCornerShape(16.dp)
              ),
            contentAlignment = Alignment.Center
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                if (isVipEmail) Icons.Default.VpnKey else Icons.Default.CreditCard,
                contentDescription = null,
                tint = DesireBlack
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = if (isVipEmail) "ACTIVATE LIFETIME VIP ($0)" else "PAY WITH STRIPE / PAYPAL / E-TRANSFER",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 12.sp,
                color = DesireBlack,
                letterSpacing = 1.sp
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Fast Quick Activate (Instant)
        OutlinedButton(
          onClick = {
            if (isVipEmail) {
              SampleDataRepository.grantLifetimeVipToEmail(inputEmail)
              onSubscribedSuccess("Lifetime VIP confirmed for $inputEmail! Enjoy unlimited $0 access.")
            } else {
              SampleDataRepository.updateMembership(
                plan = selectedPlan,
                hasPremium18Plus = include18PlusUpgrade
              )
              onSubscribedSuccess("Subscription updated to ${selectedPlan.title} (${if (include18PlusUpgrade) "with 18+ VIP" else "Standard"})!")
            }
            onDismiss()
          },
          modifier = Modifier
            .fillMaxWidth()
            .height(46.dp),
          border = androidx.compose.foundation.BorderStroke(1.dp, NeonMagenta),
          shape = RoundedCornerShape(14.dp)
        ) {
          Text(
            text = "⚡ Instant 1-Tap Upgrade • $totalDue",
            color = NeonPinkLight,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }
    }
  }

  if (showPaymentPortal) {
    PaymentPortalDialog(
      initialPlan = selectedPlan,
      initialWith18Plus = include18PlusUpgrade,
      onDismiss = { showPaymentPortal = false },
      onPaymentSuccess = { receipt ->
        showPaymentPortal = false
        onSubscribedSuccess("Payment processed successfully via ${receipt.paymentMethod.displayName}! (${receipt.amount})")
        onDismiss()
      }
    )
  }
}

@Composable
fun PlanOptionCard(
  plan: PlanType,
  price: String,
  period: String,
  subtitle: String,
  badge: String?,
  isSelected: Boolean,
  onClick: () -> Unit
) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(14.dp))
      .background(if (isSelected) DesireCardSurfaceElevated else DesireCardSurface)
      .border(
        1.5.dp,
        if (isSelected) NeonMagenta else DesireBorder,
        RoundedCornerShape(14.dp)
      )
      .clickable(onClick = onClick)
      .padding(14.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
        RadioButton(
          selected = isSelected,
          onClick = onClick,
          colors = RadioButtonDefaults.colors(
            selectedColor = NeonMagenta,
            unselectedColor = TextSecondary
          )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = plan.title,
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold,
              color = TextPrimary
            )
            if (badge != null) {
              Spacer(modifier = Modifier.width(8.dp))
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(6.dp))
                  .background(NeonMagenta)
                  .padding(horizontal = 6.dp, vertical = 2.dp)
              ) {
                Text(
                  text = badge,
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color.White
                )
              }
            }
          }
          Text(
            text = subtitle,
            fontSize = 11.sp,
            color = TextSecondary
          )
        }
      }

      Column(horizontalAlignment = Alignment.End) {
        Text(
          text = price,
          fontSize = 18.sp,
          fontWeight = FontWeight.Black,
          color = TextPrimary
        )
        Text(
          text = period,
          fontSize = 10.sp,
          color = TextMuted
        )
      }
    }
  }
}

@Composable
fun FeatureCheckRow(text: String, isUnlocked: Boolean) {
  Row(
    verticalAlignment = Alignment.Top,
    modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)
  ) {
    Icon(
      if (isUnlocked) Icons.Default.Check else Icons.Default.Lock,
      contentDescription = null,
      tint = if (isUnlocked) GoldVip else TextMuted,
      modifier = Modifier.size(14.dp).padding(top = 2.dp)
    )
    Spacer(modifier = Modifier.width(8.dp))
    Text(
      text = text,
      fontSize = 12.sp,
      color = if (isUnlocked) TextPrimary else TextSecondary,
      lineHeight = 16.sp
    )
  }
}

@Composable
fun ComparisonTable() {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(12.dp))
      .background(DesireCardSurface)
      .border(1.dp, DesireBorder, RoundedCornerShape(12.dp))
      .padding(12.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    // Header Row
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Text("Feature", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextMuted, modifier = Modifier.weight(2f))
      Text("Free", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextMuted, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
      Text("Standard", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = NeonMagenta, modifier = Modifier.weight(1.2f), textAlign = TextAlign.Center)
      Text("18+ VIP", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GoldVip, modifier = Modifier.weight(1.2f), textAlign = TextAlign.Center)
    }
    Divider(color = DesireBorder)

    ComparisonRow("Browse Profiles & Parties", "Yes", "Yes", "Yes", Modifier)
    ComparisonRow("Discreet Unlimited Chat", "Limited", "Yes", "Yes", Modifier)
    ComparisonRow("Matching Algorithm", "Basic", "Advanced", "Full Priority", Modifier)
    ComparisonRow("Submit Direct Sex Requests", "No (Locked)", "No (Locked)", "YES (Unlocked)", Modifier)
    ComparisonRow("18+ Private Vault Access", "Blurred", "Blurred", "YES (Full HD)", Modifier)
  }
}

@Composable
fun ComparisonRow(feature: String, free: String, standard: String, premium: String, modifier: Modifier) {
  Row(
    modifier = modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(feature, fontSize = 11.sp, color = TextPrimary, modifier = Modifier.weight(2f))
    Text(free, fontSize = 10.sp, color = TextMuted, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
    Text(standard, fontSize = 10.sp, color = NeonPinkLight, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1.2f), textAlign = TextAlign.Center)
    Text(premium, fontSize = 10.sp, color = GoldVip, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1.2f), textAlign = TextAlign.Center)
  }
}
