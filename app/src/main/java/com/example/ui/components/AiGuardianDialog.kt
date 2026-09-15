package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.VerifiedUser
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
import com.example.model.IdVerificationState
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AiGuardianDialog(
  onDismiss: () -> Unit
) {
  val botScan by SampleDataRepository.botScanStatus.collectAsState()
  val idState by SampleDataRepository.idVerificationState.collectAsState()
  val chatMessages by SampleDataRepository.aiGuardianChat.collectAsState()

  val scope = rememberCoroutineScope()
  var selectedTab by remember { mutableStateOf(0) } // 0: Anti-Bot Radar, 1: AI ID Verification, 2: Sentinel Assistant Chat
  var isScanningNow by remember { mutableStateOf(false) }
  var scanSuccessBanner by remember { mutableStateOf<String?>(null) }

  // ID Verification Form
  var selectedIdType by remember { mutableStateOf("Passport / Driver's License") }
  var isVerifyingId by remember { mutableStateOf(false) }
  var idVerificationSuccess by remember { mutableStateOf(false) }

  // Chat input
  var userChatInput by remember { mutableStateOf("") }

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      modifier = Modifier
        .fillMaxWidth(0.95f)
        .fillMaxHeight(0.94f)
        .clip(RoundedCornerShape(24.dp))
        .border(1.5.dp, Brush.verticalGradient(listOf(NeonCyan, VelvetPurple)), RoundedCornerShape(24.dp)),
      color = DesireDarkSurface
    ) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(20.dp)
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
                .background(Brush.radialGradient(listOf(NeonCyan, Color(0xFF0077B6))), CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.Security, contentDescription = null, tint = DesireBlack, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "SENTINEL AI GUARDIAN",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                color = NeonCyan
              )
              Text(
                text = "Anti-Bot & ID Verification",
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

        // Navigation Tabs
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(DesireCardSurface)
            .padding(4.dp)
        ) {
          TabButton(
            title = "🛡️ Bot Shield",
            isSelected = selectedTab == 0,
            onClick = { selectedTab = 0 },
            modifier = Modifier.weight(1f)
          )
          TabButton(
            title = "🪪 AI ID Verify",
            isSelected = selectedTab == 1,
            onClick = { selectedTab = 1 },
            modifier = Modifier.weight(1f)
          )
          TabButton(
            title = "🤖 Sentinel Chat",
            isSelected = selectedTab == 2,
            onClick = { selectedTab = 2 },
            modifier = Modifier.weight(1f)
          )
        }

        Spacer(modifier = Modifier.height(14.dp))

        when (selectedTab) {
          // TAB 0: ANTI-BOT & SCAMMER DEFENSE RADAR
          0 -> {
            Column(
              modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
            ) {
              // Status Hero Banner
              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(16.dp))
                  .background(
                    Brush.horizontalGradient(
                      listOf(Color(0xFF0F2B38), Color(0xFF1E0A24))
                    )
                  )
                  .border(1.dp, NeonCyan, RoundedCornerShape(16.dp))
                  .padding(16.dp)
              ) {
                Column {
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                      Icon(Icons.Default.Shield, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(20.dp))
                      Spacer(modifier = Modifier.width(6.dp))
                      Text(
                        text = "REAL-TIME PROTECTION",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonCyan
                      )
                    }

                    Box(
                      modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(SuccessGreen.copy(alpha = 0.2f))
                        .border(1.dp, SuccessGreen, RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                      Text("99.9% BOT FREE", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = SuccessGreen)
                    }
                  }

                  Spacer(modifier = Modifier.height(8.dp))

                  Text(
                    text = botScan.scanSummary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                  )

                  Spacer(modifier = Modifier.height(4.dp))

                  Text(
                    text = "Sentinel AI actively quarantines commercial spam bots, fake GPS relocators, crypto/cash solicitations, and catfish profile cloning across REBEL UP.",
                    fontSize = 12.sp,
                    color = TextSecondary
                  )
                }
              }

              Spacer(modifier = Modifier.height(14.dp))

              // Scan Metrics Cards
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
              ) {
                MetricTile(
                  label = "Profiles Vetted",
                  value = "${botScan.scannedProfilesCount}",
                  color = NeonCyan,
                  modifier = Modifier.weight(1f)
                )
                MetricTile(
                  label = "Bots Neutralized",
                  value = "100%",
                  color = SuccessGreen,
                  modifier = Modifier.weight(1f)
                )
                MetricTile(
                  label = "Scam Links Blocked",
                  value = "0 Active",
                  color = VelvetPurple,
                  modifier = Modifier.weight(1f)
                )
              }

              Spacer(modifier = Modifier.height(16.dp))

              // Deep Scan Trigger
              if (scanSuccessBanner != null) {
                Box(
                  modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SuccessGreen.copy(alpha = 0.2f))
                    .border(1.dp, SuccessGreen, RoundedCornerShape(12.dp))
                    .padding(12.dp)
                ) {
                  Text(scanSuccessBanner!!, fontSize = 12.sp, color = TextPrimary, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(10.dp))
              }

              Button(
                onClick = {
                  isScanningNow = true
                  scope.launch {
                    delay(1200)
                    isScanningNow = false
                    scanSuccessBanner = "✅ Sentinel AI deep scan complete: 100% of nearby profiles, threesome invitations, and chat inboxes are verified real humans. No bot or scammer patterns detected."
                  }
                },
                modifier = Modifier
                  .fillMaxWidth()
                  .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NeonCyan),
                shape = RoundedCornerShape(12.dp)
              ) {
                if (isScanningNow) {
                  CircularProgressIndicator(color = DesireBlack, modifier = Modifier.size(20.dp))
                  Spacer(modifier = Modifier.width(8.dp))
                  Text("Scanning Profiles & Inboxes...", color = DesireBlack, fontWeight = FontWeight.Bold)
                } else {
                  Icon(Icons.Default.Refresh, contentDescription = null, tint = DesireBlack)
                  Spacer(modifier = Modifier.width(8.dp))
                  Text("RUN INSTANT BOT & SCAM SCAN", color = DesireBlack, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
              }

              Spacer(modifier = Modifier.height(20.dp))

              // Safety Rules for Alternative & Group Dating
              Text(
                text = "COMMUNITY TRUST & SAFETY STANDARDS",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                color = TextSecondary
              )
              Spacer(modifier = Modifier.height(8.dp))

              SafetyBulletItem("Zero Tolerance for Financial Solicitation", "Anyone requesting deposits, CashApp, or crypto before meeting is instantly banned.")
              SafetyBulletItem("Verified Consensual Group Play", "All members participating in threesomes, foursomes, and orgies must confirm age & consent beforehand.")
              SafetyBulletItem("Discreet Encryption", "All adult vault content, photos, and direct sex requests are encrypted and restricted to verified adults.")
            }
          }

          // TAB 1: AI ID VERIFICATION HUB
          1 -> {
            Column(
              modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
            ) {
              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(16.dp))
                  .background(DesireCardSurface)
                  .border(1.dp, GoldVip, RoundedCornerShape(16.dp))
                  .padding(16.dp)
              ) {
                Column {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Verified, contentDescription = null, tint = GoldVip, modifier = Modifier.size(22.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                      text = "GOLD AI VERIFIED IDENTITY",
                      fontSize = 13.sp,
                      fontWeight = FontWeight.Black,
                      color = GoldVip
                    )
                  }
                  Spacer(modifier = Modifier.height(6.dp))
                  Text(
                    text = "Verify your 18+ age and real-human biometric status to earn the gold checkmark on your profile, boost your visibility by 5x, and build instant trust with couples and singles.",
                    fontSize = 12.sp,
                    color = TextPrimary
                  )
                }
              }

              Spacer(modifier = Modifier.height(16.dp))

              if (idState.isVerified || idVerificationSuccess) {
                Box(
                  modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(SuccessGreen.copy(alpha = 0.2f))
                    .border(1.dp, SuccessGreen, RoundedCornerShape(14.dp))
                    .padding(16.dp)
                ) {
                  Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(40.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("YOU ARE 100% AI VERIFIED", fontSize = 15.sp, fontWeight = FontWeight.Black, color = SuccessGreen)
                    Text("Verified with ${idState.idType} • Biometric match 99%", fontSize = 12.sp, color = TextPrimary)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("The Gold Shield is displayed on your profile card and all direct requests.", fontSize = 11.sp, color = TextSecondary)
                  }
                }
              } else {
                Text(
                  text = "SELECT VERIFICATION DOCUMENT (18+)",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  letterSpacing = 1.sp,
                  color = TextSecondary
                )
                Spacer(modifier = Modifier.height(8.dp))

                listOf("Passport / International ID", "Driver's License", "National Age Card").forEach { doc ->
                  val isSel = selectedIdType == doc
                  Box(
                    modifier = Modifier
                      .fillMaxWidth()
                      .clip(RoundedCornerShape(10.dp))
                      .background(if (isSel) DesireCardSurfaceElevated else DesireCardSurface)
                      .border(1.dp, if (isSel) NeonCyan else DesireBorder, RoundedCornerShape(10.dp))
                      .clickable { selectedIdType = doc }
                      .padding(12.dp)
                  ) {
                    Row(
                      modifier = Modifier.fillMaxWidth(),
                      horizontalArrangement = Arrangement.SpaceBetween,
                      verticalAlignment = Alignment.CenterVertically
                    ) {
                      Text(doc, fontSize = 13.sp, fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal, color = TextPrimary)
                      RadioButton(
                        selected = isSel,
                        onClick = { selectedIdType = doc },
                        colors = RadioButtonDefaults.colors(selectedColor = NeonCyan)
                      )
                    }
                  }
                  Spacer(modifier = Modifier.height(6.dp))
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Selfie Scan Preview Simulation
                var hasTakenSelfie by remember { mutableStateOf(false) }

                Box(
                  modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF160D24))
                    .border(1.dp, if (hasTakenSelfie) SuccessGreen else VelvetPurple, RoundedCornerShape(12.dp))
                    .clickable { hasTakenSelfie = true },
                  contentAlignment = Alignment.Center
                ) {
                  if (hasTakenSelfie) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                      Icon(Icons.Default.CheckCircle, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(48.dp))
                      Spacer(modifier = Modifier.height(8.dp))
                      Text("Selfie Captured Successfully", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = SuccessGreen)
                      Text("Click to retake", fontSize = 10.sp, color = TextSecondary)
                    }
                  } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                      Icon(Icons.Default.Face, contentDescription = null, tint = NeonMagenta, modifier = Modifier.size(36.dp))
                      Spacer(modifier = Modifier.height(6.dp))
                      Text("Tap to Take Verification Selfie", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                      Text("Confirms photo matches government document securely", fontSize = 10.sp, color = TextSecondary)
                    }
                  }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                  onClick = {
                    if (!hasTakenSelfie) {
                      // Prompt to take selfie first
                      hasTakenSelfie = true // Auto-trigger for demo
                    } else {
                      isVerifyingId = true
                      scope.launch {
                        delay(2000)
                        SampleDataRepository.submitIdVerification(selectedIdType)
                        isVerifyingId = false
                        idVerificationSuccess = true
                      }
                    }
                  },
                  modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                  colors = ButtonDefaults.buttonColors(containerColor = if (hasTakenSelfie) GoldVip else NeonMagenta.copy(alpha = 0.7f)),
                  shape = RoundedCornerShape(12.dp)
                ) {
                  if (isVerifyingId) {
                    CircularProgressIndicator(color = DesireBlack, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Verifying Identity with AI...", color = DesireBlack, fontWeight = FontWeight.Bold)
                  } else {
                    Icon(if (hasTakenSelfie) Icons.Default.VerifiedUser else Icons.Default.CameraAlt, contentDescription = null, tint = DesireBlack)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                      if (hasTakenSelfie) "SUBMIT FOR INSTANT AI VERIFICATION" else "TAKE SELFIE TO PROCEED", 
                      color = DesireBlack, 
                      fontWeight = FontWeight.Black, 
                      fontSize = 12.sp
                    )
                  }
                }
              }
            }
          }

          // TAB 2: SENTINEL ASSISTANT CHAT
          2 -> {
            Column(modifier = Modifier.fillMaxSize()) {
              LazyColumn(
                modifier = Modifier
                  .weight(1f)
                  .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
              ) {
                items(chatMessages) { msg ->
                  Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = if (msg.isAi) Alignment.CenterStart else Alignment.CenterEnd
                  ) {
                    Column(
                      modifier = Modifier
                        .fillMaxWidth(0.88f)
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (msg.isAi) DesireCardSurface else Color(0xFF38083F))
                        .border(1.dp, if (msg.isAi) NeonCyan.copy(alpha = 0.5f) else NeonMagenta, RoundedCornerShape(14.dp))
                        .padding(12.dp)
                    ) {
                      Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                          text = if (msg.isAi) "🛡️ Sentinel AI" else "You",
                          fontSize = 11.sp,
                          fontWeight = FontWeight.Bold,
                          color = if (msg.isAi) NeonCyan else NeonPinkLight
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(msg.time, fontSize = 10.sp, color = TextMuted)
                      }
                      Spacer(modifier = Modifier.height(4.dp))
                      Text(msg.text, fontSize = 13.sp, color = TextPrimary, lineHeight = 18.sp)

                      if (msg.suggestedActions.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                          for (action in msg.suggestedActions) {
                            Box(
                              modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(NeonCyan.copy(alpha = 0.15f))
                                .clickable {
                                  SampleDataRepository.sendAiGuardianQuestion(action)
                                }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                              Text("👉 $action", fontSize = 11.sp, color = NeonCyan)
                            }
                          }
                        }
                      }
                    }
                  }
                }
              }

              Spacer(modifier = Modifier.height(10.dp))

              // Chat Input Row
              Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
              ) {
                OutlinedTextField(
                  value = userChatInput,
                  onValueChange = { userChatInput = it },
                  placeholder = { Text("Ask Sentinel AI about bot scans or safety...") },
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

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                  onClick = {
                    if (userChatInput.isNotBlank()) {
                      val query = userChatInput.trim()
                      userChatInput = ""
                      SampleDataRepository.sendAiGuardianQuestion(query)
                    }
                  },
                  modifier = Modifier
                    .size(48.dp)
                    .background(NeonCyan, RoundedCornerShape(12.dp))
                ) {
                  Icon(Icons.Default.Send, contentDescription = "Send", tint = DesireBlack)
                }
              }
            }
          }
        }
      }
    }
  }
}

@Composable
fun TabButton(
  title: String,
  isSelected: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .clip(RoundedCornerShape(8.dp))
      .background(if (isSelected) NeonMagenta else Color.Transparent)
      .clickable(onClick = onClick)
      .padding(vertical = 8.dp),
    contentAlignment = Alignment.Center
  ) {
    Text(
      text = title,
      fontSize = 11.sp,
      fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
      color = if (isSelected) Color.White else TextSecondary
    )
  }
}

@Composable
fun MetricTile(
  label: String,
  value: String,
  color: Color,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .clip(RoundedCornerShape(12.dp))
      .background(DesireCardSurface)
      .border(1.dp, color.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
      .padding(10.dp),
    contentAlignment = Alignment.Center
  ) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
      Text(value, fontSize = 16.sp, fontWeight = FontWeight.Black, color = color)
      Text(label, fontSize = 10.sp, color = TextMuted, textAlign = TextAlign.Center)
    }
  }
}

@Composable
fun SafetyBulletItem(title: String, desc: String) {
  Row(modifier = Modifier.padding(vertical = 4.dp)) {
    Icon(Icons.Default.Check, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(16.dp))
    Spacer(modifier = Modifier.width(8.dp))
    Column {
      Text(title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
      Text(desc, fontSize = 11.sp, color = TextSecondary)
    }
  }
}
