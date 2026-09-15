package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.SampleDataRepository
import com.example.model.UserProfile
import com.example.ui.components.*
import com.example.ui.screens.*
import com.example.ui.theme.*

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        ClubDesireApp()
      }
    }
  }
}

enum class NavigationTab(val label: String, val icon: ImageVector) {
  DISCOVER("Discover", Icons.Default.Favorite),
  SERVICES("Ads / Pros", Icons.Default.Diamond),
  PREMIUM_18_PLUS("18+ Hub", Icons.Default.VpnKey),
  AI_STUDIO("AI Studio", Icons.Default.AutoFixHigh),
  PARTIES("Parties", Icons.Default.Groups),
  CHATS("Chats", Icons.Default.ChatBubble),
  PROFILE("Profile", Icons.Default.Person)
}

@Composable
fun ClubDesireApp() {
  val membership by SampleDataRepository.membershipState.collectAsState()
  val profiles by SampleDataRepository.profiles.collectAsState()

  var currentTab by remember { mutableStateOf(NavigationTab.DISCOVER) }
  var showUpgradeDialog by remember { mutableStateOf(false) }
  var showPaymentPortalDialog by remember { mutableStateOf(false) }
  var showAiGuardianDialog by remember { mutableStateOf(false) }
  var showAiToolsDialog by remember { mutableStateOf(false) }
  var showFilterDialog by remember { mutableStateOf(false) }

  var selectedDetailProfile by remember { mutableStateOf<UserProfile?>(null) }
  var selectedRequestTargetProfile by remember { mutableStateOf<UserProfile?>(null) }
  val snackbarHostState = remember { SnackbarHostState() }
  var statusToast by remember { mutableStateOf<String?>(null) }

  LaunchedEffect(statusToast) {
    statusToast?.let {
      snackbarHostState.showSnackbar(it)
      statusToast = null
    }
  }

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    containerColor = DesireBlack,
    snackbarHost = {
      SnackbarHost(snackbarHostState) { data ->
        Snackbar(
          snackbarData = data,
          containerColor = DesireCardSurfaceElevated,
          contentColor = TextPrimary,
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier
            .padding(16.dp)
            .border(1.dp, NeonMagenta, RoundedCornerShape(12.dp))
        )
      }
    },
    topBar = {
      AppTopBar(
        membership = membership,
        onMembershipClicked = { showUpgradeDialog = true },
        onFilterClicked = { showFilterDialog = true },
        onAiGuardianClicked = { showAiGuardianDialog = true },
        onAiToolsClicked = { showAiToolsDialog = true },
        onPaymentPortalClicked = { showPaymentPortalDialog = true }
      )
    },
    bottomBar = {
      NavigationBar(
        containerColor = DesireDarkSurface,
        tonalElevation = 8.dp,
        modifier = Modifier
          .fillMaxWidth()
          .windowInsetsPadding(WindowInsets.navigationBars)
      ) {
        for (tab in NavigationTab.entries) {
          val isSelected = currentTab == tab
          NavigationBarItem(
            selected = isSelected,
            onClick = { currentTab = tab },
            icon = {
              Icon(
                imageVector = tab.icon,
                contentDescription = tab.label,
                tint = if (isSelected) {
                  when (tab) {
                    NavigationTab.SERVICES -> GoldVip
                    NavigationTab.PREMIUM_18_PLUS -> GoldVip
                    NavigationTab.AI_STUDIO -> NeonCyan
                    else -> NeonMagenta
                  }
                } else TextSecondary
              )
            },
            label = {
              Text(
                text = tab.label,
                fontSize = 9.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) {
                  when (tab) {
                    NavigationTab.SERVICES -> GoldVip
                    NavigationTab.PREMIUM_18_PLUS -> GoldVip
                    NavigationTab.AI_STUDIO -> NeonCyan
                    else -> NeonMagenta
                  }
                } else TextSecondary,
                maxLines = 1
              )
            },
            colors = NavigationBarItemDefaults.colors(
              indicatorColor = when (tab) {
                NavigationTab.SERVICES -> GoldVip.copy(alpha = 0.2f)
                NavigationTab.PREMIUM_18_PLUS -> GoldVip.copy(alpha = 0.2f)
                NavigationTab.AI_STUDIO -> NeonCyan.copy(alpha = 0.2f)
                else -> NeonMagenta.copy(alpha = 0.2f)
              }
            )
          )
        }
      }
    }
  ) { paddingValues ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
    ) {
      when (currentTab) {
        NavigationTab.DISCOVER -> {
          DiscoverScreen(
            profiles = profiles,
            membership = membership,
            onProfileClicked = { selectedDetailProfile = it },
            onSendRequestClicked = { selectedRequestTargetProfile = it },
            onOpenUpgrade = { showUpgradeDialog = true },
            showFilterDialog = showFilterDialog,
            onDismissFilterDialog = { showFilterDialog = false }
          )
        }

        NavigationTab.SERVICES -> {
          SexWorkerMarketplaceScreen(
            membership = membership,
            onOpenUpgrade = { showUpgradeDialog = true },
            onSendMessage = { statusToast = it }
          )
        }

        NavigationTab.PREMIUM_18_PLUS -> {
          DirectRequestsScreen(
            membership = membership,
            onOpenUpgrade = { showUpgradeDialog = true }
          )
        }

        NavigationTab.AI_STUDIO -> {
          AiStudioScreen(
            membership = membership,
            onOpenUpgrade = { showUpgradeDialog = true },
            onOpenPaymentPortal = { showPaymentPortalDialog = true }
          )
        }

        NavigationTab.PARTIES -> {
          EventsAndPartiesScreen(
            membership = membership,
            onOpenUpgrade = { showUpgradeDialog = true }
          )
        }

        NavigationTab.CHATS -> {
          MessagesScreen(
            onOpenUpgrade = { showUpgradeDialog = true }
          )
        }

        NavigationTab.PROFILE -> {
          MyProfileScreen(
            membership = membership,
            onOpenUpgradeDialog = { showUpgradeDialog = true },
            onOpenAiGuardian = { showAiGuardianDialog = true },
            onOpenAiTools = { showAiToolsDialog = true },
            onOpenPaymentPortal = { showPaymentPortalDialog = true }
          )
        }
      }
    }
  }

  // Modals & Dialogs
  if (showUpgradeDialog) {
    MembershipUpgradeDialog(
      onDismiss = { showUpgradeDialog = false },
      onSubscribedSuccess = { statusToast = it }
    )
  }

  if (showPaymentPortalDialog) {
    PaymentPortalDialog(
      onDismiss = { showPaymentPortalDialog = false },
      onPaymentSuccess = { receipt ->
        showPaymentPortalDialog = false
        statusToast = "Payment processed via ${receipt.paymentMethod.displayName}! Membership privileges active."
      }
    )
  }

  if (showAiGuardianDialog) {
    AiGuardianDialog(
      onDismiss = { showAiGuardianDialog = false }
    )
  }

  if (showAiToolsDialog) {
    AiToolsDialog(
      onDismiss = { showAiToolsDialog = false },
      onOpenUpgrade = {
        showAiToolsDialog = false
        showUpgradeDialog = true
      }
    )
  }

  selectedDetailProfile?.let { prof ->
    ProfileDetailBottomSheet(
      profile = prof,
      onDismiss = { selectedDetailProfile = null },
      onSendRequestClicked = { target ->
        selectedDetailProfile = null
        selectedRequestTargetProfile = target
      },
      onOpenUpgrade = {
        selectedDetailProfile = null
        showUpgradeDialog = true
      }
    )
  }

  selectedRequestTargetProfile?.let { target ->
    DirectRequestComposerDialog(
      targetProfile = target,
      onDismiss = { selectedRequestTargetProfile = null },
      onRequestSent = { statusToast = it },
      onOpenUpgrade = {
        selectedRequestTargetProfile = null
        showUpgradeDialog = true
      }
    )
  }
}
