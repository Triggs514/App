package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.R
import com.example.data.SampleDataRepository
import com.example.model.*
import com.example.ui.theme.*
import java.util.UUID

@Composable
fun SexWorkerMarketplaceScreen(
  membership: MembershipState,
  onOpenUpgrade: () -> Unit,
  onSendMessage: (String) -> Unit = {}
) {
  val ads by SampleDataRepository.sexWorkerAds.collectAsState()
  var searchQuery by remember { mutableStateOf("") }
  var selectedCategoryFilter by remember { mutableStateOf("All Ads") }
  var showPostAdDialog by remember { mutableStateOf(false) }
  var bookingAdTarget by remember { mutableStateOf<SexWorkerAd?>(null) }
  var bookingToast by remember { mutableStateOf<String?>(null) }

  val categories = listOf(
    "All Ads",
    "GFE / Courtesan",
    "Intersex / Hermaphrodite",
    "Big Cock Bulls",
    "BDSM & Fetish",
    "Lesbian & Duos",
    "Incall Available",
    "Outcall Available"
  )

  val filteredAds = ads.filter { ad ->
    val matchesSearch = searchQuery.isBlank() ||
      ad.providerName.contains(searchQuery, ignoreCase = true) ||
      ad.tagline.contains(searchQuery, ignoreCase = true) ||
      ad.bio.contains(searchQuery, ignoreCase = true) ||
      ad.servicesOffered.any { it.contains(searchQuery, ignoreCase = true) }

    val matchesCategory = when (selectedCategoryFilter) {
      "All Ads" -> true
      "GFE / Courtesan" -> ad.servicesOffered.any { it.contains("GFE", ignoreCase = true) }
      "Intersex / Hermaphrodite" -> ad.gender == UserType.INTERSEX_HERMAPHRODITE || ad.servicesOffered.any { it.contains("Intersex", ignoreCase = true) }
      "Big Cock Bulls" -> ad.servicesOffered.any { it.contains("Big Cock", ignoreCase = true) } || ad.tagline.contains("Hung", ignoreCase = true)
      "BDSM & Fetish" -> ad.servicesOffered.any { it.contains("BDSM", ignoreCase = true) || it.contains("Domina", ignoreCase = true) }
      "Lesbian & Duos" -> ad.sexualOrientation == SexualOrientation.LESBIAN || ad.gender == UserType.COUPLE
      "Incall Available" -> ad.incallAvailable
      "Outcall Available" -> ad.outcallAvailable
      else -> true
    }
    matchesSearch && matchesCategory
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
            listOf(Color(0xFF370617), DesireDarkSurface)
          )
        )
        .padding(16.dp)
    ) {
      Column {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = "CLASSIFIEDS & ESCORT DIRECTORY",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                color = GoldVip
              )
              Spacer(modifier = Modifier.width(6.dp))
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(4.dp))
                  .background(NeonMagenta.copy(alpha = 0.25f))
                  .padding(horizontal = 5.dp, vertical = 1.dp)
              ) {
                Text("18+ SERVICES", fontSize = 9.sp, fontWeight = FontWeight.Black, color = NeonMagenta)
              }
            }
            Text(
              text = "Personal Services & Rates",
              fontSize = 20.sp,
              fontWeight = FontWeight.Bold,
              color = TextPrimary
            )
          }

          // Post Ad CTA
          Button(
            onClick = { showPostAdDialog = true },
            colors = ButtonDefaults.buttonColors(containerColor = NeonMagenta),
            shape = RoundedCornerShape(12.dp),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
          ) {
            Icon(Icons.Default.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("POST AN AD", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Search text field
        OutlinedTextField(
          value = searchQuery,
          onValueChange = { searchQuery = it },
          placeholder = { Text("Search services, kinks, hung bulls, hermaphrodites...", fontSize = 12.sp, color = TextSecondary) },
          leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(18.dp)) },
          trailingIcon = {
            if (searchQuery.isNotEmpty()) {
              IconButton(onClick = { searchQuery = "" }) {
                Icon(Icons.Default.Close, contentDescription = "Clear", tint = TextSecondary, modifier = Modifier.size(16.dp))
              }
            }
          },
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(12.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = DesireCardSurface,
            unfocusedContainerColor = DesireCardSurface,
            focusedBorderColor = GoldVip,
            unfocusedBorderColor = DesireBorder,
            focusedTextColor = TextPrimary,
            unfocusedTextColor = TextPrimary
          ),
          singleLine = true
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Categories filter row
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          items(categories) { cat ->
            val isSelected = cat == selectedCategoryFilter
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(if (isSelected) GoldVip else DesireCardSurface)
                .border(1.dp, if (isSelected) GoldVip else DesireBorder, RoundedCornerShape(8.dp))
                .clickable { selectedCategoryFilter = cat }
                .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
              Text(
                text = cat,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) DesireBlack else TextSecondary
              )
            }
          }
        }
      }
    }

    if (bookingToast != null) {
      Surface(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(10.dp),
        color = SuccessGreen.copy(alpha = 0.2f),
        border = androidx.compose.foundation.BorderStroke(1.dp, SuccessGreen)
      ) {
        Row(
          modifier = Modifier.padding(12.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(Icons.Default.CheckCircle, contentDescription = null, tint = SuccessGreen)
          Spacer(modifier = Modifier.width(8.dp))
          Text(bookingToast ?: "", fontSize = 12.sp, color = Color.White)
        }
      }
    }

    // Ads List
    LazyColumn(
      modifier = Modifier.fillMaxSize(),
      contentPadding = PaddingValues(16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      if (filteredAds.isEmpty()) {
        item {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .padding(top = 40.dp),
            contentAlignment = Alignment.Center
          ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Icon(Icons.Default.SearchOff, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(48.dp))
              Spacer(modifier = Modifier.height(8.dp))
              Text("No ads match your criteria", fontSize = 14.sp, color = TextSecondary)
              Spacer(modifier = Modifier.height(8.dp))
              Button(
                onClick = {
                  searchQuery = ""
                  selectedCategoryFilter = "All Ads"
                },
                colors = ButtonDefaults.buttonColors(containerColor = DesireCardSurfaceElevated)
              ) {
                Text("Reset Filters", fontSize = 12.sp, color = NeonMagenta)
              }
            }
          }
        }
      } else {
        items(filteredAds, key = { it.id }) { ad ->
          SexWorkerAdCard(
            ad = ad,
            onBookClicked = { bookingAdTarget = ad },
            onDeleteClicked = { SampleDataRepository.deleteSexWorkerAd(ad.id) }
          )
        }
      }
    }
  }

  // POST AN AD DIALOG
  if (showPostAdDialog) {
    PostSexWorkerAdDialog(
      onDismiss = { showPostAdDialog = false },
      onAdPublished = { newAd ->
        SampleDataRepository.postSexWorkerAd(newAd)
        showPostAdDialog = false
        bookingToast = "Ad published successfully! Your profile is live in the sex worker classifieds directory."
      }
    )
  }

  // BOOKING / CONTACT INQUIRY DIALOG
  if (bookingAdTarget != null) {
    BookingInquiryDialog(
      ad = bookingAdTarget!!,
      onDismiss = { bookingAdTarget = null },
      onInquirySent = { serviceName, duration, notes ->
        bookingAdTarget = null
        bookingToast = "Inquiry sent to ${bookingAdTarget?.providerName}! Details forwarded via secure discreet messenger."
      }
    )
  }
}

@Composable
fun SexWorkerAdCard(
  ad: SexWorkerAd,
  onBookClicked: () -> Unit,
  onDeleteClicked: () -> Unit
) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = DesireDarkSurface),
    border = androidx.compose.foundation.BorderStroke(1.dp, DesireBorder)
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      // Top info row
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          // Photo / Avatar
          Box(
            modifier = Modifier
              .size(64.dp)
              .clip(RoundedCornerShape(12.dp))
              .background(Brush.linearGradient(listOf(Color(ad.photoGradient.first), Color(ad.photoGradient.second)))),
            contentAlignment = Alignment.Center
          ) {
            if (ad.photoRes != null) {
              Image(
                painter = painterResource(id = ad.photoRes),
                contentDescription = ad.providerName,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
              )
            } else {
              Text(
                text = ad.providerName.take(2).uppercase(),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
              )
            }
          }

          Spacer(modifier = Modifier.width(12.dp))

          Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = ad.providerName,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "• ${ad.age}",
                fontSize = 13.sp,
                color = TextSecondary
              )
              if (ad.isVerifiedProvider) {
                Spacer(modifier = Modifier.width(6.dp))
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(GoldVip.copy(alpha = 0.2f))
                    .padding(horizontal = 4.dp, vertical = 1.dp)
                ) {
                  Text("✓ VERIFIED PRO", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = GoldVip)
                }
              }
            }

            Spacer(modifier = Modifier.height(2.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = "${ad.gender.icon} ${ad.gender.label}",
                fontSize = 11.sp,
                color = NeonCyan
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "${ad.sexualOrientation.badge} ${ad.sexualOrientation.label}",
                fontSize = 11.sp,
                color = NeonMagenta
              )
            }

            Spacer(modifier = Modifier.height(2.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.Star, contentDescription = null, tint = GoldVip, modifier = Modifier.size(13.dp))
              Spacer(modifier = Modifier.width(2.dp))
              Text(
                text = "${ad.rating} (${ad.reviewsCount} reviews)",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = GoldVip
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "📍 ${ad.location}",
                fontSize = 10.sp,
                color = TextSecondary
              )
            }
          }
        }

        // Callout status
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(DesireCardSurfaceElevated)
            .padding(horizontal = 6.dp, vertical = 3.dp)
        ) {
          Text(ad.postedTimeAgo, fontSize = 9.sp, color = TextSecondary)
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Tagline
      Text(
        text = ad.tagline,
        fontSize = 13.sp,
        fontWeight = FontWeight.SemiBold,
        color = NeonPinkLight
      )

      Spacer(modifier = Modifier.height(4.dp))

      // Bio / description
      Text(
        text = ad.bio,
        fontSize = 11.sp,
        color = TextSecondary,
        maxLines = 3,
        overflow = TextOverflow.Ellipsis,
        lineHeight = 16.sp
      )

      Spacer(modifier = Modifier.height(10.dp))

      // Rates Grid Box
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(10.dp))
          .background(DesireCardSurface)
          .border(1.dp, GoldVip.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
          .padding(10.dp)
      ) {
        Column {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text("SERVICE RATES & APPOINTMENTS", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = GoldVip, letterSpacing = 1.sp)
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
              if (ad.incallAvailable) {
                Text("🏠 Incall", fontSize = 9.sp, color = SuccessGreen, fontWeight = FontWeight.Bold)
              }
              if (ad.outcallAvailable) {
                Text("🚗 Outcall", fontSize = 9.sp, color = NeonCyan, fontWeight = FontWeight.Bold)
              }
            }
          }

          Spacer(modifier = Modifier.height(6.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            RateItemColumn("Quick Visit", ad.quickVisitRate)
            RateItemColumn("1 Hour", ad.hourlyRate)
            RateItemColumn("2 Hours", ad.twoHoursRate)
            RateItemColumn("Overnight", ad.overnightRate)
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Services offered chips
      LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        items(ad.servicesOffered) { service ->
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(6.dp))
              .background(VelvetPurple.copy(alpha = 0.3f))
              .border(1.dp, VelvetPurple.copy(alpha = 0.6f), RoundedCornerShape(6.dp))
              .padding(horizontal = 7.dp, vertical = 3.dp)
          ) {
            Text(service, fontSize = 10.sp, color = Color.White)
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Bottom Action Bar
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.PhoneIphone, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(14.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text(ad.contactMethod, fontSize = 11.sp, color = TextSecondary, maxLines = 1)
        }

        Button(
          onClick = onBookClicked,
          colors = ButtonDefaults.buttonColors(containerColor = GoldVip),
          shape = RoundedCornerShape(10.dp),
          contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
        ) {
          Icon(Icons.Default.Event, contentDescription = null, tint = DesireBlack, modifier = Modifier.size(15.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text("BOOK / INQUIRE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DesireBlack)
        }
      }
    }
  }
}

@Composable
private fun RateItemColumn(label: String, price: String) {
  Column(horizontalAlignment = Alignment.CenterHorizontally) {
    Text(label, fontSize = 9.sp, color = TextSecondary)
    Text(price, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
  }
}

// POST SEX WORKER AD MODAL
@Composable
fun PostSexWorkerAdDialog(
  onDismiss: () -> Unit,
  onAdPublished: (SexWorkerAd) -> Unit
) {
  var providerName by remember { mutableStateOf("") }
  var age by remember { mutableStateOf("25") }
  var gender by remember { mutableStateOf(UserType.SINGLE_FEMALE) }
  var orientation by remember { mutableStateOf(SexualOrientation.BISEXUAL) }
  var tagline by remember { mutableStateOf("") }
  var bio by remember { mutableStateOf("") }
  var hourlyRate by remember { mutableStateOf("$250 / hr") }
  var twoHoursRate by remember { mutableStateOf("$450 / 2 hrs") }
  var overnightRate by remember { mutableStateOf("$1,500 / Overnight") }
  var quickVisitRate by remember { mutableStateOf("$180 / 30 mins") }
  var location by remember { mutableStateOf("Downtown & Metro Area") }
  var contactMethod by remember { mutableStateOf("In-App Chat / Signal") }
  var incallAvailable by remember { mutableStateOf(true) }
  var outcallAvailable by remember { mutableStateOf(true) }

  val availableServices = listOf(
    "GFE (Girlfriend Experience)",
    "PSE (Pornstar Experience)",
    "BDSM & Dominatrix",
    "Couples Threesome Guest Star",
    "Spit Roasting",
    "Intersex / Hermaphrodite Dating",
    "Big Cock Worship & Focus",
    "Sensual Prostate Massage",
    "Dinner Dates & Travel",
    "Roleplay & Cosplay",
    "Discreet Cheating Encounters",
    "Party & Orgy Appearance"
  )
  var selectedServices by remember { mutableStateOf(setOf(availableServices[0], availableServices[3])) }

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      modifier = Modifier
        .fillMaxWidth(0.95f)
        .fillMaxHeight(0.94f)
        .clip(RoundedCornerShape(20.dp))
        .border(1.5.dp, Brush.verticalGradient(listOf(GoldVip, NeonMagenta)), RoundedCornerShape(20.dp)),
      color = DesireDarkSurface
    ) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(18.dp)
          .verticalScroll(rememberScrollState())
      ) {
        // Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text("CLASSIFIED PUBLISHER", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = GoldVip, letterSpacing = 1.sp)
            Text("Post Personal Service Ad", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
          }
          IconButton(onClick = onDismiss) {
            Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Name & Age
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
          OutlinedTextField(
            value = providerName,
            onValueChange = { providerName = it },
            label = { Text("Provider Name / Alias *") },
            modifier = Modifier.weight(2f),
            colors = customTextFieldColors(),
            shape = RoundedCornerShape(10.dp)
          )
          OutlinedTextField(
            value = age,
            onValueChange = { age = it },
            label = { Text("Age") },
            modifier = Modifier.weight(1f),
            colors = customTextFieldColors(),
            shape = RoundedCornerShape(10.dp)
          )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Gender Selector
        Text("GENDER IDENTITY", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
        Spacer(modifier = Modifier.height(4.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
          items(UserType.values()) { type ->
            val isSelected = type == gender
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(if (isSelected) NeonMagenta else DesireCardSurface)
                .border(1.dp, if (isSelected) NeonMagenta else DesireBorder, RoundedCornerShape(8.dp))
                .clickable { gender = type }
                .padding(horizontal = 8.dp, vertical = 6.dp)
            ) {
              Text("${type.icon} ${type.label}", fontSize = 11.sp, color = if (isSelected) Color.White else TextSecondary)
            }
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Sexual Orientation Selector
        Text("SEXUAL ORIENTATION", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
        Spacer(modifier = Modifier.height(4.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
          items(SexualOrientation.values()) { ori ->
            val isSelected = ori == orientation
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(if (isSelected) VelvetPurple else DesireCardSurface)
                .border(1.dp, if (isSelected) NeonCyan else DesireBorder, RoundedCornerShape(8.dp))
                .clickable { orientation = ori }
                .padding(horizontal = 8.dp, vertical = 6.dp)
            ) {
              Text("${ori.badge} ${ori.label}", fontSize = 11.sp, color = if (isSelected) Color.White else TextSecondary)
            }
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Headline & Tagline
        OutlinedTextField(
          value = tagline,
          onValueChange = { tagline = it },
          label = { Text("Catchy Ad Headline / Speciality *") },
          modifier = Modifier.fillMaxWidth(),
          colors = customTextFieldColors(),
          shape = RoundedCornerShape(10.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Detailed Bio
        OutlinedTextField(
          value = bio,
          onValueChange = { bio = it },
          label = { Text("Detailed Description of Services, Hygiene & Screening *") },
          modifier = Modifier
            .fillMaxWidth()
            .height(95.dp),
          colors = customTextFieldColors(),
          shape = RoundedCornerShape(10.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Rates Box
        Text("SERVICE RATES", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GoldVip)
        Spacer(modifier = Modifier.height(6.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          OutlinedTextField(
            value = quickVisitRate,
            onValueChange = { quickVisitRate = it },
            label = { Text("30 Mins Rate") },
            modifier = Modifier.weight(1f),
            colors = customTextFieldColors(),
            shape = RoundedCornerShape(8.dp)
          )
          OutlinedTextField(
            value = hourlyRate,
            onValueChange = { hourlyRate = it },
            label = { Text("1 Hour Rate") },
            modifier = Modifier.weight(1f),
            colors = customTextFieldColors(),
            shape = RoundedCornerShape(8.dp)
          )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          OutlinedTextField(
            value = twoHoursRate,
            onValueChange = { twoHoursRate = it },
            label = { Text("2 Hours Rate") },
            modifier = Modifier.weight(1f),
            colors = customTextFieldColors(),
            shape = RoundedCornerShape(8.dp)
          )
          OutlinedTextField(
            value = overnightRate,
            onValueChange = { overnightRate = it },
            label = { Text("Overnight Rate") },
            modifier = Modifier.weight(1f),
            colors = customTextFieldColors(),
            shape = RoundedCornerShape(8.dp)
          )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Incall / Outcall Toggles
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Switch(
              checked = incallAvailable,
              onCheckedChange = { incallAvailable = it },
              colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = SuccessGreen)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text("Incall Available", fontSize = 11.sp, color = TextPrimary)
          }

          Row(verticalAlignment = Alignment.CenterVertically) {
            Switch(
              checked = outcallAvailable,
              onCheckedChange = { outcallAvailable = it },
              colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = NeonCyan)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text("Outcall Available", fontSize = 11.sp, color = TextPrimary)
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Services Checklist
        Text("SELECT SERVICES OFFERED", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
        Spacer(modifier = Modifier.height(6.dp))

        for (row in availableServices.chunked(2)) {
          Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            for (service in row) {
              val isSelected = selectedServices.contains(service)
              Box(
                modifier = Modifier
                  .weight(1f)
                  .clip(RoundedCornerShape(8.dp))
                  .background(if (isSelected) NeonMagenta.copy(alpha = 0.25f) else DesireCardSurface)
                  .border(1.dp, if (isSelected) NeonMagenta else DesireBorder, RoundedCornerShape(8.dp))
                  .clickable {
                    selectedServices = if (isSelected) selectedServices - service else selectedServices + service
                  }
                  .padding(horizontal = 8.dp, vertical = 7.dp)
              ) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(service, fontSize = 10.sp, color = if (isSelected) Color.White else TextSecondary, maxLines = 1)
                  if (isSelected) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = NeonMagenta, modifier = Modifier.size(12.dp))
                  }
                }
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Location & Contact Method
        OutlinedTextField(
          value = location,
          onValueChange = { location = it },
          label = { Text("Location / City / Area (e.g. Downtown High-Rise)") },
          modifier = Modifier.fillMaxWidth(),
          colors = customTextFieldColors(),
          shape = RoundedCornerShape(10.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
          value = contactMethod,
          onValueChange = { contactMethod = it },
          label = { Text("Contact Method (In-App Chat, Signal, Telegram, Phone)") },
          modifier = Modifier.fillMaxWidth(),
          colors = customTextFieldColors(),
          shape = RoundedCornerShape(10.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Publish Button
        Button(
          onClick = {
            if (providerName.isNotBlank() && tagline.isNotBlank()) {
              val newAd = SexWorkerAd(
                id = "ad_${UUID.randomUUID().toString().take(6)}",
                providerName = providerName.trim(),
                age = age.trim(),
                gender = gender,
                sexualOrientation = orientation,
                tagline = tagline.trim(),
                bio = bio.ifBlank { "Professional, discreet companionship and sensual care." },
                hourlyRate = hourlyRate.ifBlank { "$250 / hr" },
                twoHoursRate = twoHoursRate.ifBlank { "$450 / 2 hrs" },
                overnightRate = overnightRate.ifBlank { "$1,500 / Overnight" },
                quickVisitRate = quickVisitRate.ifBlank { "$180 / 30 mins" },
                servicesOffered = selectedServices.toList(),
                location = location.ifBlank { "Metro Area" },
                contactMethod = contactMethod.ifBlank { "In-App Discreet Messenger" },
                isVerifiedProvider = true,
                photoRes = when (gender) {
                  UserType.SINGLE_FEMALE -> R.drawable.goth_profile_hero
                  UserType.SINGLE_MALE -> R.drawable.couple_profile_hero
                  UserType.INTERSEX_HERMAPHRODITE, UserType.TRANS_FEMALE, UserType.TRANS_MALE -> R.drawable.vip_vault_banner
                  else -> R.drawable.couple_profile_hero
                },
                photoGradient = Pair(0xFFFF2A85, 0xFF7B2CBF),
                reviewsCount = 1,
                rating = 5.0,
                postedTimeAgo = "Just now",
                outcallAvailable = outcallAvailable,
                incallAvailable = incallAvailable,
                isDiscreet = true
              )
              onAdPublished(newAd)
            }
          },
          enabled = providerName.isNotBlank() && tagline.isNotBlank(),
          modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
          colors = ButtonDefaults.buttonColors(containerColor = GoldVip),
          shape = RoundedCornerShape(12.dp)
        ) {
          Icon(Icons.Default.Publish, contentDescription = null, tint = DesireBlack)
          Spacer(modifier = Modifier.width(8.dp))
          Text("PUBLISH CLASSIFIED AD", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DesireBlack)
        }
      }
    }
  }
}

// BOOKING INQUIRY MODAL
@Composable
fun BookingInquiryDialog(
  ad: SexWorkerAd,
  onDismiss: () -> Unit,
  onInquirySent: (service: String, duration: String, notes: String) -> Unit
) {
  var selectedDuration by remember { mutableStateOf("1 Hour (${ad.hourlyRate})") }
  var selectedService by remember { mutableStateOf(ad.servicesOffered.firstOrNull() ?: "General Companionship") }
  var notes by remember { mutableStateOf("") }
  var appointmentType by remember { mutableStateOf(if (ad.incallAvailable) "Incall (Provider Studio)" else "Outcall (My Hotel/Residence)") }

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(20.dp))
        .border(1.5.dp, GoldVip, RoundedCornerShape(20.dp)),
      color = DesireDarkSurface
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(18.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text("BOOKING / INQUIRY", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = GoldVip, letterSpacing = 1.sp)
            Text(ad.providerName, fontSize = 17.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
          }
          IconButton(onClick = onDismiss) {
            Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text("Select Duration & Rate:", fontSize = 11.sp, color = TextSecondary)
        val durations = listOf(
          "30 Mins (${ad.quickVisitRate})",
          "1 Hour (${ad.hourlyRate})",
          "2 Hours (${ad.twoHoursRate})",
          "Overnight (${ad.overnightRate})"
        )

        durations.chunked(2).forEach { row ->
          Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            for (d in row) {
              val isSelected = d == selectedDuration
              Box(
                modifier = Modifier
                  .weight(1f)
                  .clip(RoundedCornerShape(8.dp))
                  .background(if (isSelected) GoldVip.copy(alpha = 0.25f) else DesireCardSurface)
                  .border(1.dp, if (isSelected) GoldVip else DesireBorder, RoundedCornerShape(8.dp))
                  .clickable { selectedDuration = d }
                  .padding(horizontal = 8.dp, vertical = 6.dp)
              ) {
                Text(d, fontSize = 10.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal, color = if (isSelected) GoldVip else TextPrimary)
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Incall / Outcall choice
        Text("Meeting Preference:", fontSize = 11.sp, color = TextSecondary)
        Spacer(modifier = Modifier.height(4.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          if (ad.incallAvailable) {
            val isSelected = appointmentType.startsWith("Incall")
            Box(
              modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(8.dp))
                .background(if (isSelected) SuccessGreen.copy(alpha = 0.25f) else DesireCardSurface)
                .border(1.dp, if (isSelected) SuccessGreen else DesireBorder, RoundedCornerShape(8.dp))
                .clickable { appointmentType = "Incall (Provider Studio)" }
                .padding(8.dp)
            ) {
              Text("🏠 Incall Studio", fontSize = 10.sp, color = if (isSelected) SuccessGreen else TextSecondary, fontWeight = FontWeight.Bold)
            }
          }
          if (ad.outcallAvailable) {
            val isSelected = appointmentType.startsWith("Outcall")
            Box(
              modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(8.dp))
                .background(if (isSelected) NeonCyan.copy(alpha = 0.25f) else DesireCardSurface)
                .border(1.dp, if (isSelected) NeonCyan else DesireBorder, RoundedCornerShape(8.dp))
                .clickable { appointmentType = "Outcall (My Hotel/Residence)" }
                .padding(8.dp)
            ) {
              Text("🚗 Outcall Visit", fontSize = 10.sp, color = if (isSelected) NeonCyan else TextSecondary, fontWeight = FontWeight.Bold)
            }
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
          value = notes,
          onValueChange = { notes = it },
          label = { Text("Special Requests / Screening details") },
          placeholder = { Text("e.g., Downtown hotel reservation, preferred dress, hygiene verification") },
          modifier = Modifier.fillMaxWidth().height(80.dp),
          colors = customTextFieldColors(),
          shape = RoundedCornerShape(10.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
          onClick = {
            onInquirySent(selectedService, selectedDuration, notes)
          },
          modifier = Modifier.fillMaxWidth().height(46.dp),
          colors = ButtonDefaults.buttonColors(containerColor = GoldVip),
          shape = RoundedCornerShape(10.dp)
        ) {
          Icon(Icons.Default.Send, contentDescription = null, tint = DesireBlack)
          Spacer(modifier = Modifier.width(6.dp))
          Text("SEND DISCREET INQUIRY", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DesireBlack)
        }
      }
    }
  }
}

@Composable
private fun customTextFieldColors() = OutlinedTextFieldDefaults.colors(
  focusedContainerColor = DesireCardSurfaceElevated,
  unfocusedContainerColor = DesireCardSurfaceElevated,
  focusedBorderColor = GoldVip,
  unfocusedBorderColor = DesireBorder,
  focusedTextColor = TextPrimary,
  unfocusedTextColor = TextPrimary
)
