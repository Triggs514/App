package com.example.ui.components

import androidx.compose.animation.*
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
import com.example.model.*
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PaymentPortalDialog(
  initialPlan: PlanType = PlanType.MONTHLY,
  initialWith18Plus: Boolean = true,
  onDismiss: () -> Unit,
  onPaymentSuccess: (PaymentReceipt) -> Unit
) {
  val membership by SampleDataRepository.membershipState.collectAsState()
  val scope = rememberCoroutineScope()

  var selectedPaymentMethod by remember { mutableStateOf(PaymentMethodType.STRIPE_CARD) }
  var selectedPlan by remember { mutableStateOf(initialPlan) }
  var include18PlusUpgrade by remember { mutableStateOf(initialWith18Plus) }

  // Credit Card Form fields
  var cardNumber by remember { mutableStateOf("•••• •••• •••• 4242") }
  var cardExpiry by remember { mutableStateOf("12/28") }
  var cardCvc by remember { mutableStateOf("888") }
  var cardZip by remember { mutableStateOf("90210") }

  // Processing state
  var isProcessing by remember { mutableStateOf(false) }
  var isPayPalRedirecting by remember { mutableStateOf(false) }
  var payPalEmail by remember { mutableStateOf("") }
  var completedReceipt by remember { mutableStateOf<PaymentReceipt?>(null) }

  val isVipEmail = remember(membership.userEmail, membership.isLifetimeVip) {
    membership.isLifetimeVip || LIFETIME_VIP_EMAILS.any { it.equals(membership.userEmail.trim(), ignoreCase = true) }
  }

  val totalDueAmount = when {
    isVipEmail -> "$0.00"
    selectedPlan == PlanType.YEARLY && include18PlusUpgrade -> "$60.00"
    selectedPlan == PlanType.YEARLY && !include18PlusUpgrade -> "$45.00"
    selectedPlan == PlanType.MONTHLY && include18PlusUpgrade -> "$30.00"
    selectedPlan == PlanType.MONTHLY && !include18PlusUpgrade -> "$15.00"
    else -> "$0.00"
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
          if (isVipEmail) Brush.verticalGradient(listOf(GoldVip, NeonMagenta))
          else Brush.verticalGradient(listOf(NeonCyan, NeonMagenta)),
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
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(36.dp)
                .background(Brush.radialGradient(listOf(NeonCyan, VelvetPurple)), CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.CreditCard, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "SECURE PAYMENT GATEWAY",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                color = NeonCyan
              )
              Text(
                text = "Checkout & Activation",
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

        if (completedReceipt != null) {
          // Success Screen & Receipt View
          PaymentReceiptView(
            receipt = completedReceipt!!,
            onDone = {
              onPaymentSuccess(completedReceipt!!)
              onDismiss()
            }
          )
        } else if (isPayPalRedirecting) {
          // Simulated PayPal Redirection & Authorization
          PayPalHandshakeView(
            amount = totalDueAmount,
            email = membership.userEmail,
            onAuthorized = { email ->
              payPalEmail = email
              isPayPalRedirecting = false
              isProcessing = true
              scope.launch {
                delay(1500)
                val receipt = SampleDataRepository.processPayment(
                  method = PaymentMethodType.PAYPAL,
                  amount = totalDueAmount,
                  plan = selectedPlan,
                  with18PlusUpgrade = include18PlusUpgrade
                )
                isProcessing = false
                completedReceipt = receipt
              }
            },
            onCancel = { isPayPalRedirecting = false }
          )
        } else if (isProcessing) {
          // Processing Animation
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .height(380.dp),
            contentAlignment = Alignment.Center
          ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              CircularProgressIndicator(
                color = if (isVipEmail) GoldVip else NeonCyan,
                strokeWidth = 3.dp,
                modifier = Modifier.size(54.dp)
              )
              Spacer(modifier = Modifier.height(20.dp))
              Text(
                text = "Authorizing with ${selectedPaymentMethod.displayName}...",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
              )
              Spacer(modifier = Modifier.height(6.dp))
              Text(
                text = "Tokenizing payment gateway & activating REBEL UP privileges",
                fontSize = 12.sp,
                color = TextSecondary
              )
            }
          }
        } else {
          // Lifetime VIP Founder Notification
          if (isVipEmail) {
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(Brush.horizontalGradient(listOf(Color(0xFF381E05), Color(0xFF1E0A24))))
                .border(1.dp, GoldVip, RoundedCornerShape(14.dp))
                .padding(14.dp)
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.VpnKey, contentDescription = null, tint = GoldVip, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                  Text(
                    text = "LIFETIME VIP FOUNDER FEE WAIVED ($0.00)",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    color = GoldVip
                  )
                  Text(
                    text = "Recognized account: ${membership.userEmail}. All gateway fees waived permanently. No credit card charge applied.",
                    fontSize = 11.sp,
                    color = TextPrimary
                  )
                }
              }
            }
            Spacer(modifier = Modifier.height(14.dp))
          }

          // Order Summary Box
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(14.dp))
              .background(DesireCardSurface)
              .border(1.dp, DesireBorder, RoundedCornerShape(14.dp))
              .padding(14.dp)
          ) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text("Item", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextMuted)
                Text("Price", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextMuted)
              }
              Divider(color = DesireBorder)

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text(selectedPlan.title, fontSize = 13.sp, color = TextPrimary)
                Text(
                  if (isVipEmail) "$0.00 (VIP)" else selectedPlan.priceText,
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Bold,
                  color = TextPrimary
                )
              }

              if (include18PlusUpgrade) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Text("18+ Sex Requests & Vault Upgrade", fontSize = 13.sp, color = GoldVip)
                  Text(
                    if (isVipEmail) "$0.00 (VIP)" else "$15.00",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldVip
                  )
                }
              }

              Divider(color = DesireBorder)

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text("Total Due", fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = TextPrimary)
                Text(
                  text = totalDueAmount,
                  fontSize = 18.sp,
                  fontWeight = FontWeight.Black,
                  color = if (isVipEmail) GoldVip else NeonMagenta
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(18.dp))

          // Select Payment Method
          Text(
            text = "SELECT SECURE PAYMENT METHOD",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
            color = TextSecondary
          )
          Spacer(modifier = Modifier.height(8.dp))

          Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            for (method in PaymentMethodType.values()) {
              val isSelected = selectedPaymentMethod == method
              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(12.dp))
                  .background(if (isSelected) DesireCardSurfaceElevated else DesireCardSurface)
                  .border(
                    1.5.dp,
                    if (isSelected) NeonCyan else DesireBorder,
                    RoundedCornerShape(12.dp)
                  )
                  .clickable { selectedPaymentMethod = method }
                  .padding(12.dp)
              ) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Text(method.iconEmoji, fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                      Text(
                        text = method.displayName,
                        fontSize = 13.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) Color.White else TextPrimary
                      )
                      Text(
                        text = method.description,
                        fontSize = 10.sp,
                        color = TextSecondary
                      )
                    }
                  }

                  RadioButton(
                    selected = isSelected,
                    onClick = { selectedPaymentMethod = method },
                    colors = RadioButtonDefaults.colors(
                      selectedColor = NeonCyan,
                      unselectedColor = TextMuted
                    )
                  )
                }
              }
            }
          }

          Spacer(modifier = Modifier.height(16.dp))

          // Form fields based on selected method
          when (selectedPaymentMethod) {
            PaymentMethodType.STRIPE_CARD -> {
              Text(
                text = "STRIPE CARD DETAILS (ENCRYPTED 256-BIT SSL)",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                color = NeonCyan
              )
              Spacer(modifier = Modifier.height(8.dp))

              OutlinedTextField(
                value = cardNumber,
                onValueChange = { cardNumber = it },
                label = { Text("Card Number (Stripe)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                  focusedContainerColor = DesireCardSurface,
                  unfocusedContainerColor = DesireCardSurface,
                  focusedBorderColor = NeonCyan,
                  unfocusedBorderColor = DesireBorder,
                  focusedTextColor = TextPrimary,
                  unfocusedTextColor = TextPrimary
                ),
                shape = RoundedCornerShape(12.dp)
              )

              Spacer(modifier = Modifier.height(8.dp))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
              ) {
                OutlinedTextField(
                  value = cardExpiry,
                  onValueChange = { cardExpiry = it },
                  label = { Text("MM / YY") },
                  modifier = Modifier.weight(1f),
                  singleLine = true,
                  colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = DesireCardSurface,
                    unfocusedContainerColor = DesireCardSurface,
                    focusedBorderColor = NeonCyan,
                    unfocusedBorderColor = DesireBorder,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                  ),
                  shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                  value = cardCvc,
                  onValueChange = { cardCvc = it },
                  label = { Text("CVC") },
                  modifier = Modifier.weight(1f),
                  singleLine = true,
                  colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = DesireCardSurface,
                    unfocusedContainerColor = DesireCardSurface,
                    focusedBorderColor = NeonCyan,
                    unfocusedBorderColor = DesireBorder,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                  ),
                  shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                  value = cardZip,
                  onValueChange = { cardZip = it },
                  label = { Text("ZIP") },
                  modifier = Modifier.weight(1f),
                  singleLine = true,
                  colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = DesireCardSurface,
                    unfocusedContainerColor = DesireCardSurface,
                    focusedBorderColor = NeonCyan,
                    unfocusedBorderColor = DesireBorder,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                  ),
                  shape = RoundedCornerShape(12.dp)
                )
              }
            }

            PaymentMethodType.PAYPAL -> {
              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(12.dp))
                  .background(Color(0xFF003087).copy(alpha = 0.2f))
                  .border(1.dp, Color(0xFF0079C1), RoundedCornerShape(12.dp))
                  .padding(14.dp)
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Text("🅿️", fontSize = 24.sp)
                  Spacer(modifier = Modifier.width(10.dp))
                  Column {
                    Text("PayPal Fast Checkout", fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 13.sp)
                    Text("Pay safely with PayPal balance, linked bank, or PayPal credit with Buyer Protection.", fontSize = 11.sp, color = TextSecondary)
                  }
                }
              }
            }

            PaymentMethodType.MOBILE_WALLET -> {
              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(12.dp))
                  .background(Color(0xFF1B263B))
                  .border(1.dp, NeonCyan, RoundedCornerShape(12.dp))
                  .padding(14.dp)
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(Icons.Default.TouchApp, contentDescription = null, tint = NeonCyan)
                  Spacer(modifier = Modifier.width(10.dp))
                  Column {
                    Text("Mobile Wallet Authorization", fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 13.sp)
                    Text("Authorize seamlessly via Apple Pay, Google Pay, or Cash App biometric authentication.", fontSize = 11.sp, color = TextSecondary)
                  }
                }
              }
            }

            PaymentMethodType.E_TRANSFER -> {
              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(12.dp))
                  .background(Color(0xFF2B1D0E))
                  .border(1.dp, Color(0xFFFFB703), RoundedCornerShape(12.dp))
                  .padding(14.dp)
              ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🏦", fontSize = 18.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Interac e-Transfer / Direct Bank Instructions", fontWeight = FontWeight.Bold, color = Color(0xFFFFB703), fontSize = 12.sp)
                  }
                  Text("Recipient: billing@clubdesire.com", fontSize = 12.sp, color = TextPrimary, fontWeight = FontWeight.SemiBold)
                  Text("Reference Code: CD-${membership.userEmail.take(4).uppercase()}-VIP", fontSize = 11.sp, color = TextSecondary)
                  Text("Instant auto-deposit enabled. Membership is upgraded automatically upon transmission.", fontSize = 10.sp, color = TextMuted)
                }
              }
            }
          }

          Spacer(modifier = Modifier.height(24.dp))

          // Authorize Button
          Button(
            onClick = {
              if (selectedPaymentMethod == PaymentMethodType.PAYPAL) {
                isPayPalRedirecting = true
              } else {
                isProcessing = true
                scope.launch {
                  delay(1200)
                  val receipt = SampleDataRepository.processPayment(
                    method = selectedPaymentMethod,
                    amount = totalDueAmount,
                    plan = selectedPlan,
                    with18PlusUpgrade = include18PlusUpgrade
                  )
                  isProcessing = false
                  completedReceipt = receipt
                }
              }
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
                  if (isVipEmail) Brush.horizontalGradient(listOf(GoldVip, NeonMagenta))
                  else Brush.horizontalGradient(listOf(NeonCyan, NeonMagenta)),
                  RoundedCornerShape(16.dp)
                ),
              contentAlignment = Alignment.Center
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  if (isVipEmail) Icons.Default.VpnKey else Icons.Default.Lock,
                  contentDescription = null,
                  tint = DesireBlack
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = if (isVipEmail) "ACTIVATE LIFETIME VIP FOUNDER ($0.00)" else "CONFIRM & PAY • $totalDueAmount",
                  fontWeight = FontWeight.ExtraBold,
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
}

@Composable
fun PaymentReceiptView(
  receipt: PaymentReceipt,
  onDone: () -> Unit
) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(8.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Box(
      modifier = Modifier
        .size(60.dp)
        .background(SuccessGreen.copy(alpha = 0.2f), CircleShape)
        .border(2.dp, SuccessGreen, CircleShape),
      contentAlignment = Alignment.Center
    ) {
      Icon(Icons.Default.CheckCircle, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(36.dp))
    }

    Spacer(modifier = Modifier.height(14.dp))

    Text(
      text = "PAYMENT SUCCESSFUL",
      fontSize = 16.sp,
      fontWeight = FontWeight.Black,
      letterSpacing = 1.5.sp,
      color = SuccessGreen
    )

    Text(
      text = "All requested features and 18+ privileges are now unlocked!",
      fontSize = 12.sp,
      color = TextSecondary,
      textAlign = TextAlign.Center
    )

    Spacer(modifier = Modifier.height(18.dp))

    // Receipt Card
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(16.dp))
        .background(DesireCardSurface)
        .border(1.dp, DesireBorder, RoundedCornerShape(16.dp))
        .padding(16.dp)
    ) {
      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        ReceiptRow("Transaction ID", receipt.transactionId)
        ReceiptRow("Payment Method", receipt.paymentMethod.displayName)
        ReceiptRow("Amount Authorized", receipt.amount)
        ReceiptRow("Plan Purchased", receipt.planPurchased)
        ReceiptRow("Date & Status", "${receipt.dateText} • Paid & Verified")
        if (receipt.isVipWaiver) {
          ReceiptRow("VIP Waiver", "100% Lifetime Free Waiver Applied")
        }
      }
    }

    Spacer(modifier = Modifier.height(24.dp))

    Button(
      onClick = onDone,
      modifier = Modifier
        .fillMaxWidth()
        .height(50.dp),
      colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
      shape = RoundedCornerShape(14.dp)
    ) {
      Text("CONTINUE TO REBEL UP", fontWeight = FontWeight.Bold, color = DesireBlack, letterSpacing = 1.sp)
    }
  }
}

@Composable
fun PayPalHandshakeView(
  amount: String,
  email: String,
  onAuthorized: (String) -> Unit,
  onCancel: () -> Unit
) {
  var step by remember { mutableStateOf(1) }
  var userEmail by remember { mutableStateOf(email) }
  var password by remember { mutableStateOf("••••••••") }

  LaunchedEffect(Unit) {
    delay(800)
    step = 2 // Move from "Redirecting" to "Login"
  }

  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(8.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    if (step == 1) {
      Box(modifier = Modifier.height(300.dp), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          CircularProgressIndicator(color = Color(0xFF0079C1), modifier = Modifier.size(48.dp))
          Spacer(modifier = Modifier.height(16.dp))
          Text("Redirecting to PayPal...", fontWeight = FontWeight.Bold, color = TextPrimary)
          Text("Securing your connection...", fontSize = 12.sp, color = TextSecondary)
        }
      }
    } else {
      // PayPal Login Lookalike
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(16.dp))
          .background(Color.White)
          .padding(24.dp)
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          // PayPal Logo Simulation
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Pay", fontWeight = FontWeight.Bold, color = Color(0xFF003087), fontSize = 24.sp)
            Text("Pal", fontWeight = FontWeight.Bold, color = Color(0xFF009CDE), fontSize = 24.sp)
          }

          Spacer(modifier = Modifier.height(24.dp))

          Text(
            text = "Pay $amount with PayPal",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2C2E2F)
          )

          Spacer(modifier = Modifier.height(20.dp))

          OutlinedTextField(
            value = userEmail,
            onValueChange = { userEmail = it },
            label = { Text("Email or mobile number") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = Color(0xFF0070BA),
              unfocusedBorderColor = Color(0xFFD1D5D7),
              focusedTextColor = Color.Black,
              unfocusedTextColor = Color.Black
            )
          )

          Spacer(modifier = Modifier.height(12.dp))

          OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = Color(0xFF0070BA),
              unfocusedBorderColor = Color(0xFFD1D5D7),
              focusedTextColor = Color.Black,
              unfocusedTextColor = Color.Black
            )
          )

          Spacer(modifier = Modifier.height(24.dp))

          Button(
            onClick = { onAuthorized(userEmail) },
            modifier = Modifier
              .fillMaxWidth()
              .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0070BA)),
            shape = RoundedCornerShape(24.dp)
          ) {
            Text("Log In", fontWeight = FontWeight.Bold, color = Color.White)
          }

          Spacer(modifier = Modifier.height(12.dp))

          TextButton(onClick = onCancel) {
            Text("Cancel and return to REBEL UP", color = Color(0xFF0070BA), fontSize = 13.sp)
          }
        }
      }
    }
  }
}

@Composable
fun ReceiptRow(label: String, value: String) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(label, fontSize = 11.sp, color = TextMuted)
    Text(value, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
  }
}
