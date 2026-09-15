package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DesireColorScheme = darkColorScheme(
  primary = NeonMagenta,
  onPrimary = Color.White,
  primaryContainer = NeonMagentaDark,
  onPrimaryContainer = Color.White,
  secondary = VelvetPurple,
  onSecondary = Color.White,
  secondaryContainer = DesireCardSurfaceElevated,
  onSecondaryContainer = TextPrimary,
  tertiary = NeonCyan,
  onTertiary = DesireBlack,
  background = DesireBlack,
  onBackground = TextPrimary,
  surface = DesireDarkSurface,
  onSurface = TextPrimary,
  surfaceVariant = DesireCardSurface,
  onSurfaceVariant = TextSecondary,
  outline = DesireBorder,
  outlineVariant = DesireBorderGlowing,
  error = SpicyRed,
  onError = Color.White
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = true,
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  MaterialTheme(
    colorScheme = DesireColorScheme,
    typography = Typography,
    content = content
  )
}

