package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Frosted Glass Palette
val Slate950 = Color(0xFF020617)
val Slate900 = Color(0xFF0F172A)
val Slate800 = Color(0xFF1E293B)
val Slate700 = Color(0xFF334155)
val Slate600 = Color(0xFF475569)
val Slate500 = Color(0xFF64748B)
val Slate400 = Color(0xFF94A3B8)
val Slate300 = Color(0xFFCBD5E1)
val Slate200 = Color(0xFFE2E8F0)
val Slate100 = Color(0xFFF1F5F9)
val Slate50 = Color(0xFFF8FAFC)

// Legacy aliases
val Navy900 = Slate900
val Navy800 = Slate800
val Navy700 = Slate700

// Frosted Accent Colors
val NeonIndigo = Color(0xFF6366F1)
val NeonBlue = Color(0xFF38BDF8)
val NeonBlueAccent = Color(0xFF60A5FA)
val NeonCyan = Color(0xFF06B6D4)
val NeonEmerald = Color(0xFF34D399)
val NeonAmber = Color(0xFFFBBF24)
val NeonRose = Color(0xFFF43F5E)
val NeonPurple = Color(0xFFA855F7)
val NeonPink = Color(0xFFEC4899)
val NeonOrange = Color(0xFFFB923C)

// Classic aliases
val PrimaryBlue = Color(0xFF3B82F6)
val PrimaryBlueDark = Color(0xFF1D4ED8)
val AccentCyan = NeonCyan
val AccentEmerald = NeonEmerald
val AccentAmber = NeonAmber
val AccentRose = NeonRose

// Frosted Surfaces & Borders
val GlassSurfaceDark = Color(0x1AFFFFFF) // 10% white for frosted glass over dark
val GlassSurfaceElevatedDark = Color(0x26FFFFFF) // 15% white
val GlassSurfaceSubtleDark = Color(0x0DFFFFFF) // 5% white
val GlassSurfaceCardDark = Color(0x991E293B) // 60% Slate800
val GlassBorderDark = Color(0x2EFFFFFF) // 18% white glossy border
val GlassBorderSubtleDark = Color(0x14FFFFFF) // 8% white

val GlassSurfaceLight = Color(0xE6FFFFFF) // 90% white for light mode glass
val GlassSurfaceElevatedLight = Color(0xF2FFFFFF) // 95% white
val GlassSurfaceSubtleLight = Color(0x80F1F5F9)
val GlassBorderLight = Color(0x330F172A)

// Background Orbs for Glassmorphism
val GlassOrbIndigo = Color(0x4D6366F1)
val GlassOrbBlue = Color(0x3338BDF8)
val GlassOrbPurple = Color(0x33A855F7)
val GlassOrbPink = Color(0x33EC4899)

// Gradients
val GlassIndigoBlueGradient = Brush.linearGradient(listOf(Color(0xFF6366F1), Color(0xFF38BDF8)))
val GlassCyanBlueGradient = Brush.linearGradient(listOf(Color(0xFF06B6D4), Color(0xFF38BDF8)))
val GlassPinkOrangeGradient = Brush.linearGradient(listOf(Color(0xFFEC4899), Color(0xFFFB923C)))
val GlassHeroDarkGradient = Brush.linearGradient(listOf(Color(0x336366F1), Color(0x1A38BDF8)))
val GlassActiveSessionGradient = Brush.linearGradient(listOf(Color(0xB3312E81), Color(0xB31E3A8A)))

// Light Palette
val LightBackground = Color(0xFFF1F5F9)
val LightSurface = Color(0xFFFFFFFF)
val LightSurfaceVariant = Color(0xFFE2E8F0)
val LightTextPrimary = Color(0xFF0F172A)
val LightTextSecondary = Color(0xFF475569)

// Dark Palette
val DarkBackground = Color(0xFF0F172A)
val DarkSurface = Color(0xFF1E293B)
val DarkSurfaceVariant = Color(0xFF334155)
val DarkPrimary = Color(0xFF60A5FA)
val DarkSecondary = Color(0xFF38BDF8)
val DarkTextPrimary = Color(0xFFF8FAFC)
val DarkTextSecondary = Color(0xFF94A3B8)

