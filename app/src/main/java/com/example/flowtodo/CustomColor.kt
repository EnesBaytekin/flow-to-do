package com.example.flowtodo

import androidx.compose.ui.graphics.Color
import java.security.MessageDigest

fun hashString(input: String): String {
    val digest = MessageDigest.getInstance("SHA-256")
    val hashBytes = digest.digest(input.toByteArray())
    return hashBytes.joinToString("") { "%02x".format(it) }
}

fun getColorFromText(text: String): Color {
    return Color(hashString(text).substring(0, 6).toInt(16))
}

fun blendColors(baseColor: Color, overlayColor: Color, ratio: Float = 0.5f): Color {
    val baseRed = baseColor.red
    val baseGreen = baseColor.green
    val baseBlue = baseColor.blue
    val baseAlpha = baseColor.alpha

    val overlayRed = overlayColor.red
    val overlayGreen = overlayColor.green
    val overlayBlue = overlayColor.blue
    val overlayAlpha = overlayColor.alpha

    val mixedRed   = (1-ratio)*baseRed   + ratio*overlayRed
    val mixedGreen = (1-ratio)*baseGreen + ratio*overlayGreen
    val mixedBlue  = (1-ratio)*baseBlue  + ratio*overlayBlue
    val mixedAlpha = (1-ratio)*baseAlpha + ratio*overlayAlpha

    return Color(mixedRed, mixedGreen, mixedBlue, mixedAlpha)
}