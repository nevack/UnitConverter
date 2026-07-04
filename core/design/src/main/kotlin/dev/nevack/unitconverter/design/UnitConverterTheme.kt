package dev.nevack.unitconverter.design

import androidx.annotation.ColorInt
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun unitConverterTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) UnitConverterDarkColorScheme else UnitConverterLightColorScheme,
        content = content,
    )
}

val UnitConverterLightColorScheme: ColorScheme =
    lightColorScheme(
        primary = UnitConverterColors.Primary,
        onPrimary = UnitConverterColors.OnPrimary,
        secondary = UnitConverterColors.Secondary,
        onSecondary = UnitConverterColors.OnSecondary,
        tertiary = UnitConverterColors.Tertiary,
        onTertiary = UnitConverterColors.OnTertiary,
        background = UnitConverterColors.Background,
        onBackground = UnitConverterColors.OnBackground,
        surface = UnitConverterColors.Surface,
        onSurface = UnitConverterColors.OnSurface,
        surfaceVariant = UnitConverterColors.SurfaceVariant,
        onSurfaceVariant = UnitConverterColors.OnSurfaceVariant,
        outline = UnitConverterColors.Outline,
    )

val UnitConverterDarkColorScheme: ColorScheme =
    darkColorScheme(
        primary = UnitConverterColors.DarkPrimary,
        onPrimary = UnitConverterColors.DarkOnPrimary,
        secondary = UnitConverterColors.Secondary,
        onSecondary = UnitConverterColors.OnSecondary,
        tertiary = UnitConverterColors.DarkTertiary,
        onTertiary = UnitConverterColors.DarkOnTertiary,
        background = UnitConverterColors.DarkBackground,
        onBackground = UnitConverterColors.DarkOnBackground,
        surface = UnitConverterColors.DarkSurface,
        onSurface = UnitConverterColors.DarkOnSurface,
        surfaceVariant = UnitConverterColors.DarkSurfaceVariant,
        onSurfaceVariant = UnitConverterColors.DarkOnSurfaceVariant,
        outline = UnitConverterColors.DarkOutline,
    )

object UnitConverterColors {
    val Primary = Color(0xFF5B35D5)
    val OnPrimary = Color.White
    val Secondary = Color(0xFFFFB000)
    val OnSecondary = Color(0xFF241A00)
    val Tertiary = Color(0xFF00A99D)
    val OnTertiary = Color.White
    val Background = Color(0xFFFFFBFF)
    val OnBackground = Color(0xFF1D1B20)
    val Surface = Color.White
    val OnSurface = Color(0xFF1D1B20)
    val SurfaceVariant = Color(0xFFE9DFF6)
    val OnSurfaceVariant = Color(0xFF4B4358)
    val Outline = Color(0xFF7C7289)
    val DarkPrimary = Color(0xFFCDBDFF)
    val DarkOnPrimary = Color(0xFF2D126C)
    val DarkTertiary = Color(0xFF74DAD0)
    val DarkOnTertiary = Color(0xFF003B37)
    val DarkBackground = Color(0xFF15121C)
    val DarkOnBackground = Color(0xFFE9E0F1)
    val DarkSurface = Color(0xFF1E1A27)
    val DarkOnSurface = Color(0xFFE9E0F1)
    val DarkSurfaceVariant = Color(0xFF4B4358)
    val DarkOnSurfaceVariant = Color(0xFFCEC3DC)
    val DarkOutline = Color(0xFF988EAA)
    val OnCategory = Color.White
    val KeypadOverlay = Color(0x29000000)
    val ShadeOverlay = Color(0x3D000000)

    object Category {
        @ColorInt val Mass = 0xFFD83B6A.toInt()

        @ColorInt val Volume = 0xFF00A86B.toInt()

        @ColorInt val Temperature = 0xFF9B3FD6.toInt()

        @ColorInt val Speed = 0xFF4355E8.toInt()

        @ColorInt val Length = 0xFF52677A.toInt()

        @ColorInt val Area = 0xFF00A99D.toInt()

        @ColorInt val Memory = 0xFF0077FF.toInt()

        @ColorInt val Time = 0xFFF9703E.toInt()

        @ColorInt val Currency = 0xFF00875A.toInt()

        @ColorInt val Other = 0xFF6D35D5.toInt()
    }
}
