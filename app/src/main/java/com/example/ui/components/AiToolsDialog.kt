package com.example.ui.components

import androidx.compose.animation.*
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
fun AiToolsDialog(
  onDismiss: () -> Unit,
  onOpenUpgrade: () -> Unit
) {
  val membership by SampleDataRepository.membershipState.collectAsState()
  val creations by SampleDataRepository.aiCreations.collectAsState()
  val currentUser by SampleDataRepository.currentUserProfile.collectAsState()
  val templates = SampleDataRepository.faceSwapTemplates

  val scope = rememberCoroutineScope()
  var currentCategory by remember { mutableStateOf(AiToolCategory.PORNO_SCENE_MAKER) }

  // Tool 1: Scene Maker State
  var sceneTitle by remember { mutableStateOf("Midnight Goth & Couple Boudoir") }
  var scenePrompt by remember { mutableStateOf("Sensual couple and goth partner in silk sheets, dim candlelight, cinematic lighting, 8k") }
  var selectedStylePreset by remember { mutableStateOf("Noir Velvet") }
  var isGeneratingScene by remember { mutableStateOf(false) }

  // Tool 2: Face Swap State
  var selectedTemplate by remember { mutableStateOf(templates.first()) }
  var isSwappingFace by remember { mutableStateOf(false) }

  // Tool 3: Image Editor State
  var selectedFilter by remember { mutableStateOf("Sensual Glow & Contrast") }
  var isApplyingFilter by remember { mutableStateOf(false) }

  // Success message
  var toastMessage by remember { mutableStateOf<String?>(null) }

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      modifier = Modifier
        .fillMaxWidth(0.96f)
        .fillMaxHeight(0.95f)
        .clip(RoundedCornerShape(24.dp))
        .border(1.5.dp, Brush.verticalGradient(listOf(NeonMagenta, GoldVip)), RoundedCornerShape(24.dp)),
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
                .background(Brush.radialGradient(listOf(NeonMagenta, VelvetPurple)), CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.AutoFixHigh, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = "AI CREATIVE LAB",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  letterSpacing = 1.sp,
                  color = NeonPinkLight
                )
                Spacer(modifier = Modifier.width(8.dp))
                // Unlimited Credits Pill
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(Brush.horizontalGradient(listOf(GoldVip, NeonMagenta)))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                  Text("∞ UNLIMITED CREDITS", fontSize = 9.sp, fontWeight = FontWeight.Black, color = DesireBlack)
                }
              }
              Text(
                text = "Scene Maker & Face Swap",
                fontSize = 17.sp,
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

        Spacer(modifier = Modifier.height(12.dp))

        // Tool Category Switcher
        LazyRow(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          items(AiToolCategory.values()) { cat ->
            val isSelected = currentCategory == cat
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(if (isSelected) NeonMagenta else DesireCardSurface)
                .border(1.dp, if (isSelected) NeonMagenta else DesireBorder, RoundedCornerShape(10.dp))
                .clickable { currentCategory = cat }
                .padding(horizontal = 10.dp, vertical = 8.dp)
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(cat.icon, fontSize = 13.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  cat.label,
                  fontSize = 11.sp,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                  color = if (isSelected) Color.White else TextSecondary
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Toast feedback
        toastMessage?.let { msg ->
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(8.dp))
              .background(SuccessGreen.copy(alpha = 0.2f))
              .border(1.dp, SuccessGreen, RoundedCornerShape(8.dp))
              .padding(8.dp)
          ) {
            Text(msg, fontSize = 11.sp, color = TextPrimary, fontWeight = FontWeight.Bold)
          }
          Spacer(modifier = Modifier.height(8.dp))
        }

        // Main Tool Body
        when (currentCategory) {
          // 1. ADULT SCENE & FANTASY MAKER
          AiToolCategory.PORNO_SCENE_MAKER -> {
            Column(
              modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
            ) {
              Text(
                text = "GENERATE INTIMATE SCENE / FANTASY ART",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                color = NeonCyan
              )
              Spacer(modifier = Modifier.height(6.dp))

              OutlinedTextField(
                value = sceneTitle,
                onValueChange = { sceneTitle = it },
                label = { Text("Scene Title") },
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

              OutlinedTextField(
                value = scenePrompt,
                onValueChange = { scenePrompt = it },
                label = { Text("Prompt (Describe couples, kinks, lighting, setting)") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
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

              Spacer(modifier = Modifier.height(10.dp))

              // Style Presets
              Text(
                text = "SELECT AESTHETIC STYLE",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                color = TextSecondary
              )
              Spacer(modifier = Modifier.height(6.dp))

              val presets = listOf("Noir Velvet", "Gothic Emo Alt", "Penthouse Luxury", "Cyberpunk Latex", "Manor Dungeon")
              LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                items(presets) { preset ->
                  val isSel = selectedStylePreset == preset
                  Box(
                    modifier = Modifier
                      .clip(RoundedCornerShape(8.dp))
                      .background(if (isSel) VelvetPurple else DesireCardSurface)
                      .border(1.dp, if (isSel) NeonMagenta else DesireBorder, RoundedCornerShape(8.dp))
                      .clickable { selectedStylePreset = preset }
                      .padding(horizontal = 8.dp, vertical = 5.dp)
                  ) {
                    Text(preset, fontSize = 11.sp, color = if (isSel) Color.White else TextSecondary)
                  }
                }
              }

              Spacer(modifier = Modifier.height(16.dp))

              Button(
                onClick = {
                  isGeneratingScene = true
                  scope.launch {
                    delay(1200)
                    val newScene = SampleDataRepository.generateAdultScene(
                      title = sceneTitle,
                      prompt = scenePrompt,
                      stylePreset = selectedStylePreset
                    )
                    isGeneratingScene = false
                    toastMessage = "✨ Generated \"${newScene.title}\"! Added to your AI creations."
                  }
                },
                modifier = Modifier
                  .fillMaxWidth()
                  .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NeonMagenta),
                shape = RoundedCornerShape(14.dp)
              ) {
                if (isGeneratingScene) {
                  CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                  Spacer(modifier = Modifier.width(8.dp))
                  Text("Rendering 8K Scene with AI...", fontWeight = FontWeight.Bold)
                } else {
                  Icon(Icons.Default.Brush, contentDescription = null)
                  Spacer(modifier = Modifier.width(8.dp))
                  Text("GENERATE SCENE (∞ UNLIMITED)", fontWeight = FontWeight.Black, fontSize = 12.sp)
                }
              }

              Spacer(modifier = Modifier.height(16.dp))

              Text("RECENT SCENE RENDERS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
              Spacer(modifier = Modifier.height(8.dp))

              for (creation in creations.filter { it.toolCategory == AiToolCategory.PORNO_SCENE_MAKER }.take(2)) {
                AiCreationPreviewCard(creation = creation)
                Spacer(modifier = Modifier.height(8.dp))
              }
            }
          }

          // 2. FACE SWAP STUDIO
          AiToolCategory.SWAP_FACES -> {
            Column(
              modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
            ) {
              Text(
                text = "CHOOSE DESIRE SCENE TEMPLATE",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                color = NeonCyan
              )
              Spacer(modifier = Modifier.height(8.dp))

              Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                for (template in templates) {
                  val isSel = selectedTemplate == template
                  Box(
                    modifier = Modifier
                      .fillMaxWidth()
                      .clip(RoundedCornerShape(12.dp))
                      .background(if (isSel) DesireCardSurfaceElevated else DesireCardSurface)
                      .border(1.5.dp, if (isSel) NeonCyan else DesireBorder, RoundedCornerShape(12.dp))
                      .clickable { selectedTemplate = template }
                      .padding(10.dp)
                  ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                      if (template.previewRes != null) {
                        Image(
                          painter = painterResource(id = template.previewRes),
                          contentDescription = template.title,
                          contentScale = ContentScale.Crop,
                          modifier = Modifier
                            .size(54.dp)
                            .clip(RoundedCornerShape(8.dp))
                        )
                      }
                      Spacer(modifier = Modifier.width(10.dp))
                      Column(modifier = Modifier.weight(1f)) {
                        Text(template.title, fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 13.sp)
                        Text(template.description, color = TextSecondary, fontSize = 11.sp, maxLines = 2)
                      }
                    }
                  }
                }
              }

              Spacer(modifier = Modifier.height(14.dp))

              Text(
                text = "TARGET FACE SOURCE: YOUR PROFILE",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = TextSecondary
              )
              Spacer(modifier = Modifier.height(6.dp))

              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(12.dp))
                  .background(DesireCardSurface)
                  .padding(10.dp)
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(Icons.Default.AccountCircle, contentDescription = null, tint = NeonMagenta, modifier = Modifier.size(36.dp))
                  Spacer(modifier = Modifier.width(10.dp))
                  Column {
                    Text(currentUser.name, fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 13.sp)
                    Text("Using primary avatar photos for high-precision 3D facial matching", color = TextSecondary, fontSize = 11.sp)
                  }
                }
              }

              Spacer(modifier = Modifier.height(16.dp))

              Button(
                onClick = {
                  isSwappingFace = true
                  scope.launch {
                    delay(1400)
                    val swapped = SampleDataRepository.performFaceSwap(selectedTemplate, currentUser.name)
                    isSwappingFace = false
                    toastMessage = "🎭 Face Swap complete for \"${selectedTemplate.title}\"!"
                  }
                },
                modifier = Modifier
                  .fillMaxWidth()
                  .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = VelvetPurple),
                shape = RoundedCornerShape(14.dp)
              ) {
                if (isSwappingFace) {
                  CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                  Spacer(modifier = Modifier.width(8.dp))
                  Text("Mapping 3D Facial Geometry...", fontWeight = FontWeight.Bold)
                } else {
                  Icon(Icons.Default.Face, contentDescription = null)
                  Spacer(modifier = Modifier.width(8.dp))
                  Text("SWAP FACE INTO TEMPLATE (∞ UNLIMITED)", fontWeight = FontWeight.Black, fontSize = 12.sp)
                }
              }
            }
          }

          // 3. IMAGE GLAM & PRIVACY EDITOR
          AiToolCategory.IMAGE_EDITOR -> {
            Column(
              modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
            ) {
              Text(
                text = "SELECT AI ENHANCEMENT OR PRIVACY MASK",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                color = NeonCyan
              )
              Spacer(modifier = Modifier.height(8.dp))

              val filters = listOf(
                "Sensual Glow & Contrast" to "Enhances skin tone and dramatic boudoir shadows.",
                "Discreet Privacy Blur Mask" to "Applies smart AI blur to recognizable tattoos, faces, or background items.",
                "Dark Alt / Goth Vignette" to "Moody deep purple-black vignette with stylized film grain.",
                "Neon Cyberpunk Underglow" to "Vibrant magenta and cyan nightclub illumination."
              )

              for ((filter, desc) in filters) {
                val isSel = selectedFilter == filter
                Box(
                  modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (isSel) DesireCardSurfaceElevated else DesireCardSurface)
                    .border(1.dp, if (isSel) NeonCyan else DesireBorder, RoundedCornerShape(10.dp))
                    .clickable { selectedFilter = filter }
                    .padding(12.dp)
                ) {
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Column(modifier = Modifier.weight(1f)) {
                      Text(filter, fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 12.sp)
                      Text(desc, color = TextSecondary, fontSize = 10.sp)
                    }
                    RadioButton(
                      selected = isSel,
                      onClick = { selectedFilter = filter },
                      colors = RadioButtonDefaults.colors(selectedColor = NeonCyan)
                    )
                  }
                }
                Spacer(modifier = Modifier.height(6.dp))
              }

              Spacer(modifier = Modifier.height(16.dp))

              Button(
                onClick = {
                  isApplyingFilter = true
                  scope.launch {
                    delay(1000)
                    SampleDataRepository.applyImageEdit(currentUser.name, selectedFilter)
                    isApplyingFilter = false
                    toastMessage = "✨ Applied $selectedFilter to photo!"
                  }
                },
                modifier = Modifier
                  .fillMaxWidth()
                  .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NeonCyan),
                shape = RoundedCornerShape(14.dp)
              ) {
                if (isApplyingFilter) {
                  CircularProgressIndicator(color = DesireBlack, modifier = Modifier.size(20.dp))
                  Spacer(modifier = Modifier.width(8.dp))
                  Text("Applying AI Filter...", color = DesireBlack, fontWeight = FontWeight.Bold)
                } else {
                  Icon(Icons.Default.FilterVintage, contentDescription = null, tint = DesireBlack)
                  Spacer(modifier = Modifier.width(8.dp))
                  Text("APPLY TO PHOTO (∞ UNLIMITED)", color = DesireBlack, fontWeight = FontWeight.Black, fontSize = 12.sp)
                }
              }
            }
          }

          // 4. MY AI CREATIONS
          AiToolCategory.GENERATOR_GALLERY -> {
            LazyColumn(
              modifier = Modifier.fillMaxSize(),
              verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              items(creations) { creation ->
                AiCreationPreviewCard(creation = creation)
              }
            }
          }
        }
      }
    }
  }
}

@Composable
fun AiCreationPreviewCard(creation: AiGeneratedMedia) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(14.dp))
      .background(DesireCardSurface)
      .border(1.dp, DesireBorder, RoundedCornerShape(14.dp))
      .padding(12.dp)
  ) {
    Row(verticalAlignment = Alignment.CenterVertically) {
      if (creation.imageRes != null) {
        Image(
          painter = painterResource(id = creation.imageRes),
          contentDescription = creation.title,
          contentScale = ContentScale.Crop,
          modifier = Modifier
            .size(68.dp)
            .clip(RoundedCornerShape(10.dp))
        )
      } else {
        Box(
          modifier = Modifier
            .size(68.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Brush.horizontalGradient(listOf(Color(creation.gradientColors.first), Color(creation.gradientColors.second))))
        )
      }

      Spacer(modifier = Modifier.width(12.dp))

      Column(modifier = Modifier.weight(1f)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(4.dp))
              .background(NeonMagenta.copy(alpha = 0.2f))
              .padding(horizontal = 5.dp, vertical = 2.dp)
          ) {
            Text(creation.stylePreset, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = NeonPinkLight)
          }
          Spacer(modifier = Modifier.width(6.dp))
          Text(creation.dateText, fontSize = 9.sp, color = TextMuted)
        }
        Spacer(modifier = Modifier.height(3.dp))
        Text(creation.title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Text(creation.prompt, fontSize = 10.sp, color = TextSecondary, maxLines = 1)
      }
    }
  }
}
