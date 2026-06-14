package com.oldman.launcher.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * 老年桌面 Material3 主题
 *
 * 配置要点:
 * - 亮色主题 (lightColorScheme): 白底黑字，最高对比度
 * - 全局默认字体 ≥ 24sp + 加粗，通过 Typography 强制执行
 * - 所有表面颜色统一为白色，避免杂色干扰
 */

private val OldManColorScheme = lightColorScheme(
    primary = Color.Black,           // 主要交互元素（按钮等）
    onPrimary = Color.White,         // 主要元素上的文字
    primaryContainer = Color(0xFFE0E0E0),
    onPrimaryContainer = Color.Black,

    secondary = Color(0xFF424242),   // 次要元素
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFEEEEEE),
    onSecondaryContainer = Color.Black,

    background = Color.White,        // 背景纯白
    onBackground = Color.Black,      // 背景上的文字纯黑

    surface = Color.White,           // 卡片/按钮表面纯白
    onSurface = Color.Black,         // 表面上的文字纯黑

    surfaceVariant = Color(0xFFF5F5F5),
    onSurfaceVariant = Color.Black,

    error = Color(0xFFC62828),       // 错误/警告色
    onError = Color.White,

    outline = Color(0xFFBDBDBD)      // 边框/分割线
)

/**
 * 老年桌面全局主题 Composable
 *
 * 用法: 在 setContent {} 最外层包裹此 Composable
 * ```kotlin
 * setContent {
 *     OldManTheme {
 *         HomeScreen()
 *     }
 * }
 * ```
 */
@Composable
fun OldManTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = OldManColorScheme,
        typography = OldManTypography,
        content = content
    )
}
