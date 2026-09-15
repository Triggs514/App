package com.example.ui.screens

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
import com.example.data.SampleDataRepository
import com.example.model.*
import com.example.ui.components.AiCreationPreviewCard
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AiStudioScreen(
  membership: MembershipState,
  onOpenUpgrade: () -> Unit,
  onOpenPaymentPortal: () -> Unit
) {
  val creations by SampleDataRepository.aiCreations.collectAsState()
  val currentUser by SampleDataRepository.currentUserProfile.collectAsState()
  val templates = SampleDataRepository.faceSwapTemplates

  val scope = rememberCoroutineScope()
  var currentCategory by remember { mutableStateOf(AiToolCategory.PORNO_SCENE_MAKER) }

  // Tool 1: Scene Maker State
  var sceneTitle by remember { mutableStateOf("Midnight Velvet Boudoir") }
  var scenePrompt by remember { mutableStateOf("Sensual couple and goth partner in silk sheets, dim candlelight, cinematic lighting, 8k") }
  var selectedStylePreset by remember { mutableStateOf("Noir Velvet") }
  var isGeneratingScene by remember { mutableStateOf(false) }

  // Tool 2: Face Swap State
  var selectedTemplate by remember { mutableStateOf(templates.first()) }
  var isSwappingFace by remember { mutableStateOf(false) }

  // Tool 3: Image Editor State
  var selectedFilter by remember { mutableStateOf("Sensual Glow & Contrast") }
  var isApplyingFilter by remember { mutableStateOf(false) }

  // Feedback Toast
  var toastMessage by remember { mutableStateOf<String?>(null) }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(DesireBlack)
  ) {
    // Header Banner with Unlimited Credits Badge
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .background(
          Brush.verticalGradient(
            listOf(Color(0xFF2E0836), DesireBlack)
          )
        )
        .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.AutoFixHigh, contentDescription = null, tint = NeonMagenta, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "AI CREATIVE STUDIO",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              letterSpacing = 1.5.sp,
              color = NeonPinkLight
            )
          }
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = "Intimate Scene & Face Swap Maker",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
          )
        }

        // Unlimited Credits Badge
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Brush.horizontalGradient(listOf(GoldVip, NeonMagenta)))
            .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text("∞", fontSize = 14.sp, fontWeight = FontWeight.Black, color = DesireBlack)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "UNLIMITED CREDITS",
              fontSize = 10.sp,
              fontWeight = FontWeight.Black,
              color = DesireBlack
            )
          }
        }
      }
    }

    // Category Selector
    LazyRow(
      contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      modifier = Modifier
        .fillMaxWidth()
        .background(DesireDarkSurface.copy(alpha = 0.6f))
    ) {
      items(AiToolCategory.values()) { cat ->
        val isSelected = currentCategory == cat
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) NeonMagenta else DesireCardSurface)
            .border(1.dp, if (isSelected) NeonMagenta else DesireBorder, RoundedCornerShape(10.dp))
            .clickable { currentCategory = cat }
            .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(cat.icon, fontSize = 13.sp)
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              cat.label,
              fontSize = 11.sp,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
              color = if (isSelected) Color.White else TextSecondary
            )
          }
        }
      }
    }

    // Feedback Toast
    toastMessage?.let { msg ->
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 6.dp)
          .clip(RoundedCornerShape(8.dp))
          .background(SuccessGreen.copy(alpha = 0.2f))
          .border(1.dp, SuccessGreen, RoundedCornerShape(8.dp))
          .padding(8.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(msg, fontSize = 11.sp, color = TextPrimary, fontWeight = FontWeight.Bold)
          IconButton(onClick = { toastMessage = null }, modifier = Modifier.size(18.dp)) {
            Icon(Icons.Default.Close, contentDescription = null, tint = TextPrimary, modifier = Modifier.size(12.dp))
          }
        }
      }
    }

    // Tool Body
    when (currentCategory) {
      // 1. ADULT SCENE & FANTASY MAKER
      AiToolCategory.PORNO_SCENE_MAKER -> {
        Column(
          modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp)
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
            label = { Text("Fantasy Prompt (couples, threesome, spit roast, goth style)") },
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

          // Aesthetic Presets
          Text(
            text = "SELECT AESTHETIC STYLE",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
            color = TextSecondary
          )
          Spacer(modifier = Modifier.height(6.dp))

          val presets = listOf("Noir Velvet", "Gothic Emo Alt 🖤", "Penthouse Luxury 🥂", "Cyberpunk Latex 👾", "Manor Dungeon ⛓️")
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
              .height(52.dp),
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

          Spacer(modifier = Modifier.height(20.dp))

          Text("RECENT SCENE RENDERS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
          Spacer(modifier = Modifier.height(8.dp))

          for (creation in creations.filter { it.toolCategory == AiToolCategory.PORNO_SCENE_MAKER }.take(3)) {
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
            .padding(horizontal = 16.dp, vertical = 12.dp)
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
                        .size(56.dp)
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
              .height(52.dp),
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
            .padding(horizontal = 16.dp, vertical = 12.dp)
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
              .height(52.dp),
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
          contentPadding = PaddingValues(16.dp),
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
