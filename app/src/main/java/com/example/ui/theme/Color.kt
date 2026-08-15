package com.example.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Aura Liquid Droplet Design Tokens
 * Visual North Star: Pure White Canvas + Liquid Droplet Glassmorphism
 */

// Pure White Canvas
val AuraBackground = Color(0xFFFFFFFF)

// Liquid Droplet Surface Tokens (Translucent Liquid Grey)
val AuraLiquidGlass = Color(0xCCF1F3F6)     // 80% Translucent Grey
val AuraLiquidGlassElevated = Color(0xE6F1F3F6) // 90% Translucent Grey
val AuraLiquidGlassHero = Color(0xF2F1F3F6)     // 95% Translucent Grey
val AuraLiquidWhite = Color(0x99FFFFFF)        // 60% Translucent White for highlights

// Accent System (Aura Emerald)
val AuraEmerald = Color(0xFF10B981)
val AuraEmeraldBright = Color(0xFF34D399)
val AuraEmeraldDark = Color(0xFF059669)
val AuraEmeraldMuted = Color(0xFFD1FAE5)
val AuraEmeraldGlow = Color(0x1A10B981)
val AuraEmeraldGlass = Color(0x3310B981)

// Semantic Accents
val AuraAmber = Color(0xFFF59E0B)
val AuraAmberSurface = Color(0xFFFFF7ED)
val AuraRose = Color(0xFFF43F5E)
val AuraRoseSurface = Color(0xFFFFF1F2)

// Typography (Slate Hierarchy)
val AuraTextPrimary = Color(0xFF0F172A)    // Slate 900
val AuraTextSecondary = Color(0xFF64748B)  // Slate 500
val AuraTextMuted = Color(0xFF94A3B8)      // Slate 400
val AuraTextDisabled = Color(0xFFCBD5E1)   // Slate 300

// Optical Edges & Light Response
val AuraBorder = Color(0xFFF1F5F9)         // Slate 100
val AuraBorderSubtle = Color(0xFFF8FAFC)   // Slate 50
val AuraGlassBorder = Color(0x1A000000)    // 10% Black for subtle definition on white
val AuraSpecularHighlight = Color(0x40FFFFFF) // White reflection
val AuraShadow = Color(0x0A000000)         // Minimal environment shadow

// Legacy Mapping (Ensuring Build Stability)
val AuraSurfacePrimary = AuraBackground
val AuraSurfaceSecondary = Color(0xFFF1F3F6)
val AuraSurfaceTertiary = Color(0xFFE8EBF0)
val AuraGlassElevated = AuraLiquidGlassElevated

// Legacy / Static
val AuraWhite = Color(0xFFFFFFFF)
val AuraBlack = Color(0xFF000000)

// Surface Mapping
val AuraSurfaceCard = Color(0xFFFFFFFF)
val AuraSurfaceElevated = Color(0xFFF8FAFC)
val AuraGraphiteDark = Color(0xFF0F172A)
