package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

/**
 * Ambient background with soft glowing radial orbs replicating the frosted glass atmosphere.
 */
@Composable
fun FrostedBackground(
    modifier: Modifier = Modifier,
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(if (isDarkTheme) Slate900 else LightBackground)
    ) {
        // Glowing Ambient Atmospheric Orbs
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            // Top-left luminous Indigo Glow Orb
            val topGlowColor = if (isDarkTheme) Color(0x596366F1) else Color(0x33818CF8)
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(topGlowColor, Color.Transparent),
                    center = Offset(width * 0.1f, height * 0.05f),
                    radius = width * 0.75f
                ),
                center = Offset(width * 0.1f, height * 0.05f),
                radius = width * 0.75f
            )

            // Bottom-right luminous Electric Blue / Cyan Glow Orb
            val bottomGlowColor = if (isDarkTheme) Color(0x4038BDF8) else Color(0x2638BDF8)
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(bottomGlowColor, Color.Transparent),
                    center = Offset(width * 0.9f, height * 0.85f),
                    radius = width * 0.85f
                ),
                center = Offset(width * 0.9f, height * 0.85f),
                radius = width * 0.85f
            )

            // Center subtle Purple accent orb
            val centerGlowColor = if (isDarkTheme) Color(0x26A855F7) else Color(0x14C084FC)
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(centerGlowColor, Color.Transparent),
                    center = Offset(width * 0.5f, height * 0.45f),
                    radius = width * 0.6f
                ),
                center = Offset(width * 0.5f, height * 0.45f),
                radius = width * 0.6f
            )
        }

        // Foreground Content
        content()
    }
}

/**
 * Frosted Glass Card with translucent background, glossy border stroke, and smooth rounded corners.
 */
@Composable
fun FrostedGlassCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(24.dp),
    borderWidth: Dp = 1.dp,
    borderColor: Color? = null,
    backgroundColor: Color? = null,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val isDark = isSystemInDarkTheme()
    val defaultBg = if (isDark) Color(0x1FFFFFFF) else Color(0xCCFFFFFF)
    val defaultBorder = if (isDark) Color(0x2EFFFFFF) else Color(0x330F172A)

    val actualBg = backgroundColor ?: defaultBg
    val actualBorder = borderColor ?: defaultBorder

    val clickableModifier = if (onClick != null) {
        modifier
            .clip(shape)
            .clickable(onClick = onClick)
    } else {
        modifier.clip(shape)
    }

    Box(
        modifier = clickableModifier
            .background(actualBg, shape)
            .border(
                border = BorderStroke(
                    width = borderWidth,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            actualBorder,
                            actualBorder.copy(alpha = (actualBorder.alpha * 0.35f).coerceIn(0f, 1f))
                        )
                    )
                ),
                shape = shape
            )
    ) {
        content()
    }
}

/**
 * Frosted Glass Pill Tag for statuses, metrics, and categories.
 */
@Composable
fun FrostedPillBadge(
    text: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    accentColor: Color = NeonBlue,
    containerColor: Color = accentColor.copy(alpha = 0.15f),
    borderColor: Color = accentColor.copy(alpha = 0.35f)
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(50.dp),
        color = containerColor,
        border = BorderStroke(1.dp, borderColor)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if (icon != null) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(13.dp)
                )
            }
            Text(
                text = text,
                color = accentColor,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
        }
    }
}

/**
 * Frosted Quick Action Tile (e.g., Mock Tests, Flashcards, AI Doubt solver).
 */
@Composable
fun FrostedQuickActionTile(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconColor: Color,
    iconBgColor: Color = iconColor.copy(alpha = 0.15f),
    onClick: () -> Unit
) {
    val isDark = isSystemInDarkTheme()
    val bg = if (isDark) Color(0x991E293B) else Color(0xE6FFFFFF)
    val border = if (isDark) Color(0x1FFFFFFF) else Color(0x1F0F172A)

    Box(
        modifier = modifier
            .height(130.dp)
            .clip(RoundedCornerShape(24.dp))
            .clickable(onClick = onClick)
            .background(bg)
            .border(
                BorderStroke(1.dp, border),
                RoundedCornerShape(24.dp)
            )
            .padding(14.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(iconBgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = title,
                    tint = iconColor,
                    modifier = Modifier.size(20.dp)
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = if (isDark) Slate50 else Slate900
                )
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = if (isDark) Slate400 else Slate600
                )
            }
        }
    }
}

/**
 * Frosted Progress Bar with neon rounded indicator.
 */
@Composable
fun FrostedProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    barColor: Color = NeonBlue,
    fillBrush: Brush? = null,
    height: Dp = 8.dp,
    trackColor: Color = if (isSystemInDarkTheme()) Color(0xFF334155) else Color(0xFFE2E8F0)
) {
    val brush = fillBrush ?: Brush.horizontalGradient(
        listOf(
            barColor.copy(alpha = 0.8f),
            barColor
        )
    )

    Box(
        modifier = modifier
            .height(height)
            .clip(RoundedCornerShape(50.dp))
            .background(trackColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(fraction = progress.coerceIn(0f, 1f))
                .clip(RoundedCornerShape(50.dp))
                .background(brush)
        )
    }
}
