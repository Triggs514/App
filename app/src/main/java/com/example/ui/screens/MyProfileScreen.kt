package com.example.ui.screens

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
fun MyProfileScreen(
  membership: MembershipState,
  onOpenUpgradeDialog: () -> Unit,
  onOpenAiGuardian: () -> Unit = {},
  onOpenAiTools: () -> Unit = {},
  onOpenPaymentPortal: () -> Unit = {}
) {
  val profile by SampleDataRepository.currentUserProfile.collectAsState()
  val idState by SampleDataRepository.idVerificationState.collectAsState()
  val botStatus by SampleDataRepository.botScanStatus.collectAsState()

  var isEditing by remember { mutableStateOf(false) }
  var name by remember(profile) { mutableStateOf(profile.name) }
  var age by remember(profile) { mutableStateOf(profile.age) }
  var userType by remember(profile) { mutableStateOf(profile.userType) }
  var sexualOrientation by remember(profile) { mutableStateOf(profile.sexualOrientation) }
  var relationshipStatus by remember(profile) { mutableStateOf(profile.relationshipStatus) }
  var cockEndowment by remember(profile) { mutableStateOf(profile.cockEndowment) }
  var isHungVerified by remember(profile) { mutableStateOf(profile.isHungVerified) }
  var hungSizeInches by remember(profile) { mutableStateOf(profile.hungSizeInches) }
  var isDiscreetAffair by remember(profile) { mutableStateOf(profile.isDiscreetAffair) }
  var discreetAlias by remember(profile) { mutableStateOf(profile.discreetAlias) }
  var fantasiesAndFetishes by remember(profile) { mutableStateOf(profile.fantasiesAndFetishes) }
  var customFantasyInput by remember { mutableStateOf("") }
  var tagline by remember(profile) { mutableStateOf(profile.tagline) }
  var bio by remember(profile) { mutableStateOf(profile.bio) }
  var lookingForText by remember(profile) { mutableStateOf(profile.lookingFor) }
  var boundariesText by remember(profile) { mutableStateOf(profile.boundaries) }

  var selectedDesires by remember(profile) { mutableStateOf(profile.desireTags.toSet()) }
  var selectedGroupActivities by remember(profile) { mutableStateOf(profile.preferredGroupActivities.toSet()) }
  var selectedTargetGenders by remember(profile) { mutableStateOf(profile.targetGendersLookingFor.toSet()) }
  var selectedSubcultures by remember(profile) { mutableStateOf(profile.subcultureTags.toSet()) }

  var showAddPhotoDialog by remember { mutableStateOf(false) }
  var showSaveToast by remember { mutableStateOf(false) }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(DesireBlack)
      .verticalScroll(rememberScrollState())
      .padding(bottom = 100.dp)
  ) {
    // Header
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .background(
          Brush.verticalGradient(
            listOf(Color(0xFF2B0A26), DesireDarkSurface)
          )
        )
        .padding(16.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = "COMPREHENSIVE PROFILE SYSTEM",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              letterSpacing = 1.sp,
              color = NeonMagenta
            )
            if (membership.isLifetimeVip) {
              Spacer(modifier = Modifier.width(6.dp))
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(4.dp))
                  .background(GoldVip)
                  .padding(horizontal = 4.dp, vertical = 1.dp)
              ) {
                Text("LIFETIME VIP", fontSize = 9.sp, fontWeight = FontWeight.Black, color = DesireBlack)
              }
            }
          }
          Text(
            text = "My Dating Persona",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
          )
        }

        IconButton(
          onClick = {
            if (isEditing) {
              SampleDataRepository.updateMyProfile(
                name = name,
                age = age,
                userType = userType,
                tagline = tagline,
                bio = bio,
                desires = selectedDesires.toList(),
                groupActivities = selectedGroupActivities.toList(),
                targetGenders = selectedTargetGenders.toList(),
                lookingFor = lookingForText,
                boundaries = boundariesText,
                subcultures = selectedSubcultures.toList(),
                sexualOrientation = sexualOrientation,
                relationshipStatus = relationshipStatus,
                cockEndowment = cockEndowment,
                isHungVerified = isHungVerified,
                hungSizeInches = hungSizeInches,
                isDiscreetAffair = isDiscreetAffair,
                discreetAlias = discreetAlias,
                fantasiesAndFetishes = fantasiesAndFetishes
              )
              showSaveToast = true
            }
            isEditing = !isEditing
          },
          modifier = Modifier
            .size(40.dp)
            .background(if (isEditing) SuccessGreen else NeonMagenta, CircleShape)
        ) {
          Icon(
            if (isEditing) Icons.Default.Check else Icons.Default.Edit,
            contentDescription = if (isEditing) "Save" else "Edit",
            tint = Color.White,
            modifier = Modifier.size(18.dp)
          )
        }
      }
    }

    if (showSaveToast) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp)
          .clip(RoundedCornerShape(12.dp))
          .background(SuccessGreen.copy(alpha = 0.2f))
          .border(1.dp, SuccessGreen, RoundedCornerShape(12.dp))
          .padding(12.dp)
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.CheckCircle, contentDescription = null, tint = SuccessGreen)
          Spacer(modifier = Modifier.width(8.dp))
          Text("Comprehensive profile updated and saved!", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
        }
      }
    }

    // SECTION 1: PHOTO GALLERY & UPLOAD (Multiple Photos Management)
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "MY PHOTOS (${profile.photos.size})",
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.sp,
          color = NeonCyan
        )
        TextButton(onClick = { showAddPhotoDialog = true }) {
          Icon(Icons.Default.AddPhotoAlternate, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("Add Photo", color = NeonCyan, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
      }

      Spacer(modifier = Modifier.height(6.dp))

      LazyRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        items(profile.photos, key = { it.id }) { photo ->
          UserPhotoThumbnail(
            photo = photo,
            isEditing = isEditing,
            onSetMain = { SampleDataRepository.setMainPhoto(photo.id) },
            onDelete = { SampleDataRepository.removePhotoFromCurrentUser(photo.id) }
          )
        }

        item {
          Box(
            modifier = Modifier
              .width(110.dp)
              .height(140.dp)
              .clip(RoundedCornerShape(14.dp))
              .background(DesireCardSurface)
              .border(1.dp, DesireBorder, RoundedCornerShape(14.dp))
              .clickable { showAddPhotoDialog = true },
            contentAlignment = Alignment.Center
          ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Icon(Icons.Default.Add, contentDescription = null, tint = NeonMagenta, modifier = Modifier.size(28.dp))
              Spacer(modifier = Modifier.height(4.dp))
              Text("Upload", fontSize = 11.sp, color = TextSecondary, fontWeight = FontWeight.SemiBold)
            }
          }
        }
      }
    }

    // SECTION 2: MEMBERSHIP & LIFETIME VIP CARRIER
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 8.dp),
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = DesireCardSurface)
    ) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .background(
            Brush.horizontalGradient(
              listOf(Color(0xFF26102D), Color(0xFF1B112B))
            )
          )
          .border(
            1.5.dp,
            if (membership.isLifetimeVip) Brush.horizontalGradient(listOf(GoldVip, NeonMagenta))
            else Brush.horizontalGradient(listOf(NeonMagenta, VelvetPurple)),
            RoundedCornerShape(20.dp)
          )
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
                  .size(36.dp)
                  .background(
                    if (membership.isLifetimeVip) GoldVip else NeonMagenta,
                    CircleShape
                  ),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  if (membership.isLifetimeVip) Icons.Default.VpnKey else Icons.Default.Star,
                  contentDescription = null,
                  tint = if (membership.isLifetimeVip) DesireBlack else Color.White,
                  modifier = Modifier.size(20.dp)
                )
              }
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Text(
                  text = if (membership.isLifetimeVip) "LIFETIME VIP FOUNDER" else "CURRENT MEMBERSHIP",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  letterSpacing = 1.sp,
                  color = if (membership.isLifetimeVip) GoldVip else NeonPinkLight
                )
                Text(
                  text = if (membership.isLifetimeVip) "Forever Free VIP ($0)" else membership.plan.title,
                  fontSize = 15.sp,
                  fontWeight = FontWeight.Bold,
                  color = TextPrimary
                )
              }
            }

            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(if (membership.isLifetimeVip) GoldVip else NeonMagenta)
                .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
              Text(
                text = if (membership.isLifetimeVip) "VIP FOUNDER" else if (membership.hasPremium18PlusUpgrade) "18+ UNLOCKED" else "STANDARD",
                fontSize = 10.sp,
                fontWeight = FontWeight.Black,
                color = if (membership.isLifetimeVip) DesireBlack else Color.White
              )
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          Text(
            text = "Active Account: ${membership.userEmail}",
            fontSize = 12.sp,
            color = TextSecondary
          )

          Spacer(modifier = Modifier.height(8.dp))
          Divider(color = DesireBorder)
          Spacer(modifier = Modifier.height(8.dp))

          Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            MembershipPerkRow("Standard Membership", if (membership.isLifetimeVip) "$0 Lifetime Free" else "${membership.plan.priceText} ${membership.plan.billingPeriod}")
            MembershipPerkRow("Direct Sex Requests", if (membership.canSendSexRequests) "Unlocked (VIP 18+ Active)" else "Locked ($15 Upgrade Needed)")
            MembershipPerkRow("18+ Private Vault Access", if (membership.canView18PlusContent) "Unlocked (Full HD)" else "Locked ($15 Upgrade Needed)")
          }

          Spacer(modifier = Modifier.height(12.dp))

          // Quick founder email switchers
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Button(
              onClick = {
                SampleDataRepository.grantLifetimeVipToEmail("triggz2905@gmail.com")
              },
              colors = ButtonDefaults.buttonColors(
                containerColor = if (membership.userEmail.equals("triggz2905@gmail.com", true)) GoldVip else DesireCardSurfaceElevated
              ),
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.weight(1f),
              contentPadding = PaddingValues(horizontal = 6.dp, vertical = 4.dp)
            ) {
              Text(
                "triggz2905 (VIP)",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (membership.userEmail.equals("triggz2905@gmail.com", true)) DesireBlack else GoldVip
              )
            }

            Button(
              onClick = {
                SampleDataRepository.grantLifetimeVipToEmail("LaurenCartier6@gmail.com")
              },
              colors = ButtonDefaults.buttonColors(
                containerColor = if (membership.userEmail.equals("LaurenCartier6@gmail.com", true)) GoldVip else DesireCardSurfaceElevated
              ),
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.weight(1f),
              contentPadding = PaddingValues(horizontal = 6.dp, vertical = 4.dp)
            ) {
              Text(
                "LaurenCartier6 (VIP)",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (membership.userEmail.equals("LaurenCartier6@gmail.com", true)) DesireBlack else GoldVip
              )
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          Button(
            onClick = onOpenUpgradeDialog,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
              containerColor = if (membership.isLifetimeVip) GoldVip else NeonMagenta
            ),
            shape = RoundedCornerShape(12.dp)
          ) {
            Text(
              text = if (membership.isLifetimeVip) "MANAGE FOUNDER PRIVILEGES" else "UPGRADE OR ENTER VIP EMAIL",
              fontWeight = FontWeight.Bold,
              fontSize = 12.sp,
              color = if (membership.isLifetimeVip) DesireBlack else Color.White,
              letterSpacing = 1.sp
            )
          }
        }
      }
    }

    // SECTION 2B: AI GUARDIAN & PAYMENT PORTAL QUICK ACCESS
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 4.dp),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = DesireCardSurface)
    ) {
      Column(modifier = Modifier.padding(14.dp)) {
        Text(
          text = "AI DEFENSE & BILLING PORTAL",
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.5.sp,
          color = NeonCyan
        )
        Spacer(modifier = Modifier.height(10.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          // AI Sentinel & ID Verification
          Box(
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(12.dp))
              .background(DesireCardSurfaceElevated)
              .border(1.dp, NeonCyan.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
              .clickable(onClick = onOpenAiGuardian)
              .padding(10.dp)
          ) {
            Column {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Shield, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("AI Shield", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
              }
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = if (idState.isVerified) "✓ Verified Human" else "ID & Bot Radar",
                fontSize = 10.sp,
                color = if (idState.isVerified) SuccessGreen else NeonPinkLight,
                fontWeight = FontWeight.SemiBold
              )
            }
          }

          // Payment Methods
          Box(
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(12.dp))
              .background(DesireCardSurfaceElevated)
              .border(1.dp, GoldVip.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
              .clickable(onClick = onOpenPaymentPortal)
              .padding(10.dp)
          ) {
            Column {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.CreditCard, contentDescription = null, tint = GoldVip, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Payments", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
              }
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = "Stripe, PayPal, Interac",
                fontSize = 10.sp,
                color = TextSecondary
              )
            }
          }

          // AI Creative Studio
          Box(
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(12.dp))
              .background(DesireCardSurfaceElevated)
              .border(1.dp, NeonMagenta.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
              .clickable(onClick = onOpenAiTools)
              .padding(10.dp)
          ) {
            Column {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AutoFixHigh, contentDescription = null, tint = NeonMagenta, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("AI Studio", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
              }
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = "∞ Unlimited",
                fontSize = 10.sp,
                color = GoldVip,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }
      }
    }

    // SECTION 3: GENDER IDENTITY & DATING DYNAMICS
    Column(modifier = Modifier.padding(16.dp)) {
      Text(
        text = "MY GENDER IDENTITY & RELATIONSHIP TYPE",
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp,
        color = NeonMagenta
      )
      Spacer(modifier = Modifier.height(8.dp))

      LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        items(UserType.values()) { type ->
          val isSelected = type == userType
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(12.dp))
              .background(if (isSelected) NeonMagenta else DesireCardSurface)
              .border(1.dp, if (isSelected) NeonMagenta else DesireBorder, RoundedCornerShape(12.dp))
              .clickable(enabled = isEditing) { userType = type }
              .padding(horizontal = 12.dp, vertical = 8.dp)
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(type.icon, fontSize = 13.sp)
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = type.label,
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) Color.White else TextSecondary
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // What I'm Looking for in Matches (Genders)
      Text(
        text = "TARGET GENDERS I'M SEEKING IN MATCHES",
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp,
        color = NeonCyan
      )
      Spacer(modifier = Modifier.height(6.dp))

      for (row in UserType.values().toList().chunked(2)) {
        Row(
          modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          for (target in row) {
            val isSelected = selectedTargetGenders.contains(target)
            Box(
              modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(10.dp))
                .background(if (isSelected) NeonCyan.copy(alpha = 0.2f) else DesireCardSurface)
                .border(1.dp, if (isSelected) NeonCyan else DesireBorder, RoundedCornerShape(10.dp))
                .clickable(enabled = isEditing) {
                  selectedTargetGenders = if (isSelected) selectedTargetGenders - target else selectedTargetGenders + target
                }
                .padding(horizontal = 10.dp, vertical = 8.dp)
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Text(target.icon, fontSize = 12.sp)
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(
                    text = target.label,
                    fontSize = 11.sp,
                    color = if (isSelected) Color.White else TextSecondary,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                  )
                }
                if (isSelected) {
                  Icon(Icons.Default.Check, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(14.dp))
                }
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // MY SEXUAL ORIENTATION
      Text(
        text = "MY SEXUAL ORIENTATION",
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp,
        color = NeonMagenta
      )
      Spacer(modifier = Modifier.height(6.dp))

      for (row in SexualOrientation.values().toList().chunked(3)) {
        Row(
          modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          for (orientation in row) {
            val isSelected = orientation == sexualOrientation
            Box(
              modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(10.dp))
                .background(if (isSelected) VelvetPurple else DesireCardSurface)
                .border(1.dp, if (isSelected) NeonCyan else DesireBorder, RoundedCornerShape(10.dp))
                .clickable(enabled = isEditing) { sexualOrientation = orientation }
                .padding(horizontal = 8.dp, vertical = 8.dp)
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(orientation.badge, fontSize = 12.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = orientation.label,
                  fontSize = 11.sp,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                  color = if (isSelected) Color.White else TextSecondary
                )
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // MY RELATIONSHIP STATUS & DYNAMIC
      Text(
        text = "MY RELATIONSHIP STATUS & DYNAMIC",
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp,
        color = Color(0xFF4361EE)
      )
      Spacer(modifier = Modifier.height(6.dp))

      for (status in RelationshipStatus.values()) {
        val isSelected = status == relationshipStatus
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) Color(0xFF4361EE).copy(alpha = 0.25f) else DesireCardSurface)
            .border(1.dp, if (isSelected) Color(0xFF4361EE) else DesireBorder, RoundedCornerShape(10.dp))
            .clickable(enabled = isEditing) { relationshipStatus = status }
            .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(status.icon, fontSize = 14.sp)
              Spacer(modifier = Modifier.width(8.dp))
              Column {
                Text(
                  text = status.label,
                  fontSize = 12.sp,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                  color = if (isSelected) Color.White else TextPrimary
                )
                Text(
                  text = status.description,
                  fontSize = 10.sp,
                  color = TextSecondary
                )
              }
            }
            if (isSelected) {
              Icon(Icons.Default.Check, contentDescription = null, tint = Color(0xFF4361EE), modifier = Modifier.size(16.dp))
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // BIG COCKS & HUNG SECTION (8"+ VERIFIED)
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = DesireCardSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, if (cockEndowment == CockEndowment.BIG_COCKS_ONLY || isHungVerified) NeonMagenta else DesireBorder)
      ) {
        Column(modifier = Modifier.padding(12.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text("🍆", fontSize = 18.sp)
              Spacer(modifier = Modifier.width(8.dp))
              Column {
                Text(
                  text = "BIG COCKS & ENDOWMENT SECTION",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  letterSpacing = 1.sp,
                  color = NeonMagenta
                )
                Text(
                  text = "Hung bull status, verification & length",
                  fontSize = 10.sp,
                  color = TextSecondary
                )
              }
            }
            if (isHungVerified) {
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(6.dp))
                  .background(NeonMagenta.copy(alpha = 0.25f))
                  .border(1.dp, NeonMagenta, RoundedCornerShape(6.dp))
                  .padding(horizontal = 6.dp, vertical = 2.dp)
              ) {
                Text("8\"+ HUNG VERIFIED", fontSize = 9.sp, fontWeight = FontWeight.Black, color = NeonMagenta)
              }
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          // Endowment level
          LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            items(CockEndowment.values()) { endowment ->
              val isSelected = endowment == cockEndowment
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(8.dp))
                  .background(if (isSelected) NeonMagenta else DesireCardSurfaceElevated)
                  .border(1.dp, if (isSelected) NeonMagenta else DesireBorder, RoundedCornerShape(8.dp))
                  .clickable(enabled = isEditing) {
                    cockEndowment = endowment
                    if (endowment == CockEndowment.BIG_COCKS_ONLY) {
                      isHungVerified = true
                    }
                  }
                  .padding(horizontal = 8.dp, vertical = 6.dp)
              ) {
                Text(
                  text = endowment.label,
                  fontSize = 11.sp,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                  color = if (isSelected) Color.White else TextSecondary
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            OutlinedTextField(
              value = hungSizeInches,
              onValueChange = { hungSizeInches = it },
              label = { Text("Hung Size / Dimensions (e.g. 8.5\" Thick)") },
              enabled = isEditing,
              modifier = Modifier.weight(1f),
              colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = DesireCardSurfaceElevated,
                unfocusedContainerColor = DesireCardSurfaceElevated,
                disabledContainerColor = DesireCardSurfaceElevated,
                focusedBorderColor = NeonMagenta,
                unfocusedBorderColor = DesireBorder,
                disabledBorderColor = DesireBorder.copy(alpha = 0.5f),
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary,
                disabledTextColor = TextPrimary
              ),
              shape = RoundedCornerShape(10.dp)
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
              Text("Verified:", fontSize = 11.sp, color = TextSecondary)
              Spacer(modifier = Modifier.width(4.dp))
              Switch(
                checked = isHungVerified,
                onCheckedChange = { if (isEditing) isHungVerified = it },
                enabled = isEditing,
                colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = NeonMagenta)
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // DISCREET & SECRET AFFAIR MODE
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = DesireCardSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, if (isDiscreetAffair) NeonCyan else DesireBorder)
      ) {
        Column(modifier = Modifier.padding(12.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text("🤫", fontSize = 18.sp)
              Spacer(modifier = Modifier.width(8.dp))
              Column {
                Text(
                  text = "DISCREET & SECRET AFFAIR SECTION",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  letterSpacing = 1.sp,
                  color = NeonCyan
                )
                Text(
                  text = "Masked identity, confidential incognito mode",
                  fontSize = 10.sp,
                  color = TextSecondary
                )
              }
            }
            Switch(
              checked = isDiscreetAffair,
              onCheckedChange = { if (isEditing) isDiscreetAffair = it },
              enabled = isEditing,
              colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = NeonCyan)
            )
          }

          if (isDiscreetAffair) {
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
              value = discreetAlias,
              onValueChange = { discreetAlias = it },
              label = { Text("Incognito Alias / Secret Handle (e.g. Shadow Velvet)") },
              enabled = isEditing,
              modifier = Modifier.fillMaxWidth(),
              colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = DesireCardSurfaceElevated,
                unfocusedContainerColor = DesireCardSurfaceElevated,
                disabledContainerColor = DesireCardSurfaceElevated,
                focusedBorderColor = NeonCyan,
                unfocusedBorderColor = DesireBorder,
                disabledBorderColor = DesireBorder.copy(alpha = 0.5f),
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary,
                disabledTextColor = TextPrimary
              ),
              shape = RoundedCornerShape(10.dp)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Display Name & Age
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        OutlinedTextField(
          value = name,
          onValueChange = { name = it },
          label = { Text("Display Name") },
          enabled = isEditing,
          modifier = Modifier.weight(2f),
          colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = DesireCardSurface,
            unfocusedContainerColor = DesireCardSurface,
            disabledContainerColor = DesireCardSurface,
            focusedBorderColor = NeonMagenta,
            unfocusedBorderColor = DesireBorder,
            disabledBorderColor = DesireBorder.copy(alpha = 0.5f),
            focusedTextColor = TextPrimary,
            unfocusedTextColor = TextPrimary,
            disabledTextColor = TextPrimary
          ),
          shape = RoundedCornerShape(12.dp)
        )

        OutlinedTextField(
          value = age,
          onValueChange = { age = it },
          label = { Text("Age(s)") },
          enabled = isEditing,
          modifier = Modifier.weight(1f),
          colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = DesireCardSurface,
            unfocusedContainerColor = DesireCardSurface,
            disabledContainerColor = DesireCardSurface,
            focusedBorderColor = NeonMagenta,
            unfocusedBorderColor = DesireBorder,
            disabledBorderColor = DesireBorder.copy(alpha = 0.5f),
            focusedTextColor = TextPrimary,
            unfocusedTextColor = TextPrimary,
            disabledTextColor = TextPrimary
          ),
          shape = RoundedCornerShape(12.dp)
        )
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Tagline
      OutlinedTextField(
        value = tagline,
        onValueChange = { tagline = it },
        label = { Text("Catchy Tagline / Dynamic Summary") },
        enabled = isEditing,
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
          focusedContainerColor = DesireCardSurface,
          unfocusedContainerColor = DesireCardSurface,
          disabledContainerColor = DesireCardSurface,
          focusedBorderColor = NeonMagenta,
          unfocusedBorderColor = DesireBorder,
          disabledBorderColor = DesireBorder.copy(alpha = 0.5f),
          focusedTextColor = TextPrimary,
          unfocusedTextColor = TextPrimary,
          disabledTextColor = TextPrimary
        ),
        shape = RoundedCornerShape(12.dp)
      )

      Spacer(modifier = Modifier.height(14.dp))

      // Detailed Bio
      OutlinedTextField(
        value = bio,
        onValueChange = { bio = it },
        label = { Text("Detailed Bio & Experience") },
        enabled = isEditing,
        modifier = Modifier
          .fillMaxWidth()
          .height(115.dp),
        colors = OutlinedTextFieldDefaults.colors(
          focusedContainerColor = DesireCardSurface,
          unfocusedContainerColor = DesireCardSurface,
          disabledContainerColor = DesireCardSurface,
          focusedBorderColor = NeonMagenta,
          unfocusedBorderColor = DesireBorder,
          disabledBorderColor = DesireBorder.copy(alpha = 0.5f),
          focusedTextColor = TextPrimary,
          unfocusedTextColor = TextPrimary,
          disabledTextColor = TextPrimary
        ),
        shape = RoundedCornerShape(12.dp)
      )

      Spacer(modifier = Modifier.height(14.dp))

      // Detailed Looking For
      OutlinedTextField(
        value = lookingForText,
        onValueChange = { lookingForText = it },
        label = { Text("What I'm Looking for in Matches") },
        enabled = isEditing,
        modifier = Modifier
          .fillMaxWidth()
          .height(85.dp),
        colors = OutlinedTextFieldDefaults.colors(
          focusedContainerColor = DesireCardSurface,
          unfocusedContainerColor = DesireCardSurface,
          disabledContainerColor = DesireCardSurface,
          focusedBorderColor = NeonCyan,
          unfocusedBorderColor = DesireBorder,
          disabledBorderColor = DesireBorder.copy(alpha = 0.5f),
          focusedTextColor = TextPrimary,
          unfocusedTextColor = TextPrimary,
          disabledTextColor = TextPrimary
        ),
        shape = RoundedCornerShape(12.dp)
      )

      Spacer(modifier = Modifier.height(14.dp))

      // Boundaries
      OutlinedTextField(
        value = boundariesText,
        onValueChange = { boundariesText = it },
        label = { Text("Personal Boundaries & Safe Practices") },
        enabled = isEditing,
        modifier = Modifier
          .fillMaxWidth()
          .height(85.dp),
        colors = OutlinedTextFieldDefaults.colors(
          focusedContainerColor = DesireCardSurface,
          unfocusedContainerColor = DesireCardSurface,
          disabledContainerColor = DesireCardSurface,
          focusedBorderColor = VelvetPurple,
          unfocusedBorderColor = DesireBorder,
          disabledBorderColor = DesireBorder.copy(alpha = 0.5f),
          focusedTextColor = TextPrimary,
          unfocusedTextColor = TextPrimary,
          disabledTextColor = TextPrimary
        ),
        shape = RoundedCornerShape(12.dp)
      )

      Spacer(modifier = Modifier.height(20.dp))

      // SECTION 4: PREFERRED GROUP ACTIVITIES (Threesomes, Foursomes, Orgies, Gang Banging, Spit Roasting)
      Text(
        text = "PREFERRED GROUP ACTIVITIES",
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp,
        color = VelvetPurple
      )
      Spacer(modifier = Modifier.height(8.dp))

      val groupDynamicsList = listOf(
        DynamicKink.THREESOME_MFF,
        DynamicKink.THREESOME_MMF,
        DynamicKink.FOURSOME,
        DynamicKink.SPIT_ROASTING,
        DynamicKink.ORGIES,
        DynamicKink.GANG_BANGING
      )

      for (row in groupDynamicsList.chunked(2)) {
        Row(
          modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          for (kink in row) {
            val isSelected = selectedGroupActivities.contains(kink)
            Box(
              modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(12.dp))
                .background(if (isSelected) Color(kink.badgeColorHex).copy(alpha = 0.25f) else DesireCardSurface)
                .border(1.dp, if (isSelected) Color(kink.badgeColorHex) else DesireBorder, RoundedCornerShape(12.dp))
                .clickable(enabled = isEditing) {
                  selectedGroupActivities = if (isSelected) selectedGroupActivities - kink else selectedGroupActivities + kink
                }
                .padding(horizontal = 10.dp, vertical = 8.dp)
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = kink.displayName,
                  fontSize = 11.sp,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                  color = if (isSelected) Color(kink.badgeColorHex) else TextSecondary
                )
                if (isSelected) {
                  Icon(Icons.Default.Check, contentDescription = null, tint = Color(kink.badgeColorHex), modifier = Modifier.size(14.dp))
                }
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // SECTION 5: KINKS & SUBCULTURE PREFERENCES (Goth/Emo dating, BDSM, etc.)
      Text(
        text = "SUBCULTURE PREFERENCES (GOTH, EMO, VAMPIRE, ALT)",
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp,
        color = Color(0xFF4361EE)
      )
      Spacer(modifier = Modifier.height(8.dp))

      for (row in SubcultureType.values().toList().chunked(2)) {
        Row(
          modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          for (sub in row) {
            val isSelected = selectedSubcultures.contains(sub)
            Box(
              modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(10.dp))
                .background(if (isSelected) Color(0xFF4361EE).copy(alpha = 0.3f) else DesireCardSurface)
                .border(1.dp, if (isSelected) Color(0xFF4361EE) else DesireBorder, RoundedCornerShape(10.dp))
                .clickable(enabled = isEditing) {
                  selectedSubcultures = if (isSelected) selectedSubcultures - sub else selectedSubcultures + sub
                }
                .padding(horizontal = 10.dp, vertical = 8.dp)
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Text(sub.emoji, fontSize = 12.sp)
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(
                    text = sub.label,
                    fontSize = 11.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if (isSelected) Color.White else TextSecondary
                  )
                }
                if (isSelected) {
                  Icon(Icons.Default.Check, contentDescription = null, tint = Color(0xFF4361EE), modifier = Modifier.size(14.dp))
                }
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // ALL OTHER DESIRES & KINKS
      Text(
        text = "ALL OTHER DESIRES & KINKS",
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp,
        color = TextSecondary
      )
      Spacer(modifier = Modifier.height(8.dp))

      for (row in DynamicKink.values().toList().chunked(2)) {
        Row(
          modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          for (kink in row) {
            val isSelected = selectedDesires.contains(kink)
            Box(
              modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(12.dp))
                .background(if (isSelected) Color(kink.badgeColorHex).copy(alpha = 0.25f) else DesireCardSurface)
                .border(1.dp, if (isSelected) Color(kink.badgeColorHex) else DesireBorder, RoundedCornerShape(12.dp))
                .clickable(enabled = isEditing) {
                  selectedDesires = if (isSelected) selectedDesires - kink else selectedDesires + kink
                }
                .padding(horizontal = 10.dp, vertical = 8.dp)
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = kink.displayName,
                  fontSize = 11.sp,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                  color = if (isSelected) Color(kink.badgeColorHex) else TextSecondary
                )
                if (isSelected) {
                  Icon(Icons.Default.Check, contentDescription = null, tint = Color(kink.badgeColorHex), modifier = Modifier.size(14.dp))
                }
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // CUSTOM FANTASIES & FETISHES
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = DesireCardSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, VelvetPurple.copy(alpha = 0.5f))
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text("✨", fontSize = 18.sp)
              Spacer(modifier = Modifier.width(8.dp))
              Column {
                Text(
                  text = "SPECIFIC FANTASIES & FETISHES",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  letterSpacing = 1.sp,
                  color = VelvetPurple
                )
                Text(
                  text = "Personal list of niche kinks, taboos & specific fantasies",
                  fontSize = 10.sp,
                  color = TextSecondary
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          // Current Fantasies Chips
          if (fantasiesAndFetishes.isNotEmpty()) {
            LazyRow(
              horizontalArrangement = Arrangement.spacedBy(6.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              items(fantasiesAndFetishes) { fantasy ->
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(VelvetPurple.copy(alpha = 0.4f))
                    .border(1.dp, VelvetPurple, RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 5.dp)
                ) {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(fantasy, fontSize = 11.sp, color = Color.White)
                    if (isEditing) {
                      Spacer(modifier = Modifier.width(4.dp))
                      Icon(
                        Icons.Default.Close,
                        contentDescription = "Remove",
                        tint = TextSecondary,
                        modifier = Modifier
                          .size(12.dp)
                          .clickable {
                            fantasiesAndFetishes = fantasiesAndFetishes - fantasy
                          }
                      )
                    }
                  }
                }
              }
            }
          } else {
            Text(
              text = "No custom fantasies added yet. Type your specific kink or taboo below.",
              fontSize = 11.sp,
              color = TextSecondary
            )
          }

          if (isEditing) {
            Spacer(modifier = Modifier.height(10.dp))
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              OutlinedTextField(
                value = customFantasyInput,
                onValueChange = { customFantasyInput = it },
                label = { Text("Add custom fantasy / fetish") },
                modifier = Modifier.weight(1f),
                colors = OutlinedTextFieldDefaults.colors(
                  focusedContainerColor = DesireCardSurfaceElevated,
                  unfocusedContainerColor = DesireCardSurfaceElevated,
                  focusedBorderColor = VelvetPurple,
                  unfocusedBorderColor = DesireBorder,
                  focusedTextColor = TextPrimary,
                  unfocusedTextColor = TextPrimary
                ),
                shape = RoundedCornerShape(10.dp)
              )

              Button(
                onClick = {
                  if (customFantasyInput.isNotBlank() && !fantasiesAndFetishes.contains(customFantasyInput.trim())) {
                    fantasiesAndFetishes = fantasiesAndFetishes + customFantasyInput.trim()
                    customFantasyInput = ""
                  }
                },
                colors = ButtonDefaults.buttonColors(containerColor = VelvetPurple),
                shape = RoundedCornerShape(10.dp)
              ) {
                Text("+ ADD", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
              }
            }
          }
        }
      }
    }
  }

  if (showAddPhotoDialog) {
    var captionText by remember { mutableStateOf("") }
    var selectedPresetIndex by remember { mutableStateOf(0) }
    val presetDrawables = listOf(
      Pair(R.drawable.couple_profile_hero, "Couple Intimate Session"),
      Pair(R.drawable.goth_profile_hero, "Goth / Emo Look"),
      Pair(R.drawable.vip_vault_banner, "VIP Playroom / Lounge")
    )

    AlertDialog(
      onDismissRequest = { showAddPhotoDialog = false },
      containerColor = DesireDarkSurface,
      title = {
        Text("Upload New Photo", fontWeight = FontWeight.Bold, color = TextPrimary)
      },
      text = {
        Column {
          Text(
            "Add high-res photos to showcase your vibe, style, and couple/solo aesthetic.",
            fontSize = 12.sp,
            color = TextSecondary
          )
          Spacer(modifier = Modifier.height(12.dp))

          OutlinedTextField(
            value = captionText,
            onValueChange = { captionText = it },
            label = { Text("Photo Caption") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
              focusedContainerColor = DesireCardSurface,
              unfocusedContainerColor = DesireCardSurface,
              focusedBorderColor = NeonCyan,
              unfocusedBorderColor = DesireBorder,
              focusedTextColor = TextPrimary,
              unfocusedTextColor = TextPrimary
            ),
            shape = RoundedCornerShape(10.dp),
            singleLine = true
          )

          Spacer(modifier = Modifier.height(14.dp))
          Text("Select Aesthetic Image:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
          Spacer(modifier = Modifier.height(8.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            presetDrawables.forEachIndexed { index, pair ->
              val isSelected = selectedPresetIndex == index
              Box(
                modifier = Modifier
                  .weight(1f)
                  .height(80.dp)
                  .clip(RoundedCornerShape(10.dp))
                  .border(2.dp, if (isSelected) NeonCyan else DesireBorder, RoundedCornerShape(10.dp))
                  .clickable { selectedPresetIndex = index }
              ) {
                Image(
                  painter = painterResource(id = pair.first),
                  contentDescription = pair.second,
                  contentScale = ContentScale.Crop,
                  modifier = Modifier.fillMaxSize()
                )
                if (isSelected) {
                  Box(
                    modifier = Modifier
                      .align(Alignment.TopEnd)
                      .padding(4.dp)
                      .size(20.dp)
                      .background(NeonCyan, CircleShape),
                    contentAlignment = Alignment.Center
                  ) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = DesireBlack, modifier = Modifier.size(14.dp))
                  }
                }
              }
            }
          }
        }
      },
      confirmButton = {
        Button(
          onClick = {
            val chosen = presetDrawables[selectedPresetIndex]
            SampleDataRepository.addPhotoToCurrentUser(
              caption = captionText.ifBlank { chosen.second },
              drawableRes = chosen.first
            )
            showAddPhotoDialog = false
          },
          colors = ButtonDefaults.buttonColors(containerColor = NeonCyan)
        ) {
          Text("Upload Photo", color = DesireBlack, fontWeight = FontWeight.Bold)
        }
      },
      dismissButton = {
        TextButton(onClick = { showAddPhotoDialog = false }) {
          Text("Cancel", color = TextSecondary)
        }
      }
    )
  }
}

@Composable
fun UserPhotoThumbnail(
  photo: PhotoItem,
  isEditing: Boolean,
  onSetMain: () -> Unit,
  onDelete: () -> Unit
) {
  Box(
    modifier = Modifier
      .width(110.dp)
      .height(140.dp)
      .clip(RoundedCornerShape(14.dp))
      .background(DesireCardSurface)
      .border(
        1.5.dp,
        if (photo.isMain) GoldVip else DesireBorder,
        RoundedCornerShape(14.dp)
      )
  ) {
    if (photo.drawableRes != null) {
      Image(
        painter = painterResource(id = photo.drawableRes),
        contentDescription = photo.caption,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize()
      )
    } else {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .background(Brush.linearGradient(listOf(VelvetPurple, NeonMagenta))),
        contentAlignment = Alignment.Center
      ) {
        Icon(Icons.Default.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(36.dp))
      }
    }

    // Scrim overlay
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(
          Brush.verticalGradient(
            listOf(Color.Black.copy(alpha = 0.3f), Color.Black.copy(alpha = 0.7f)),
            startY = 60f
          )
        )
    )

    if (photo.isMain) {
      Box(
        modifier = Modifier
          .align(Alignment.TopStart)
          .padding(6.dp)
          .clip(RoundedCornerShape(6.dp))
          .background(GoldVip)
          .padding(horizontal = 6.dp, vertical = 2.dp)
      ) {
        Text("MAIN", fontSize = 9.sp, fontWeight = FontWeight.Black, color = DesireBlack)
      }
    }

    if (isEditing) {
      Row(
        modifier = Modifier
          .align(Alignment.TopEnd)
          .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
      ) {
        if (!photo.isMain) {
          IconButton(
            onClick = onSetMain,
            modifier = Modifier
              .size(26.dp)
              .background(DesireBlack.copy(alpha = 0.75f), CircleShape)
          ) {
            Icon(Icons.Default.Star, contentDescription = "Set Main", tint = GoldVip, modifier = Modifier.size(14.dp))
          }
        }

        IconButton(
          onClick = onDelete,
          modifier = Modifier
            .size(26.dp)
            .background(DesireBlack.copy(alpha = 0.75f), CircleShape)
        ) {
          Icon(Icons.Default.Delete, contentDescription = "Delete", tint = SpicyRed, modifier = Modifier.size(14.dp))
        }
      }
    }

    // Caption
    if (photo.caption.isNotBlank()) {
      Text(
        text = photo.caption,
        fontSize = 10.sp,
        color = Color.White,
        maxLines = 1,
        modifier = Modifier
          .align(Alignment.BottomStart)
          .padding(6.dp)
      )
    }
  }
}

@Composable
fun MembershipPerkRow(title: String, value: String) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(text = title, fontSize = 12.sp, color = TextSecondary)
    Text(text = value, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
  }
}
