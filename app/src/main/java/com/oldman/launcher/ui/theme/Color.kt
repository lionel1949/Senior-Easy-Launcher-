package com.oldman.launcher.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * 老年桌面配色方案 — 高对比度、高可读性
 *
 * 设计原则:
 * - 白底黑字: 对比度最高，最易读
 * - 避免浅灰、淡色: 老人视力下降，低对比度颜色看不清
 * - 颜色语义化: 红色仅用于警示，深蓝用于区分农历信息
 * - 纯白背景: 减少视觉干扰
 */

// ── 主色调 ──
/** 纯黑 — 正文颜色，最高对比度 */
val TextPrimary = Color(0xFF000000)

/** 深灰黑 — 标题/强调文字 */
val TextEmphasis = Color(0xFF212121)

/** 纯白 — 背景色 */
val BackgroundMain = Color(0xFFFFFFFF)

/** 卡片/按钮背景 */
val SurfaceWhite = Color(0xFFFAFAFA)

// ── 功能色 ──
/** 深蓝色 — 农历日期专用，在黑色文字中醒目但不刺眼 */
val LunarDateBlue = Color(0xFF1A237E)

/** 正红色 — 警告/强调（电话按钮等），老人对红色敏感 */
val AlertRed = Color(0xFFC62828)

/** 微信绿 — 微信按钮专用色，符合用户对微信的品牌认知 */
val WechatGreen = Color(0xFF2E7D32)

// ── 辅助色 ──
/** 浅灰 — 分割线 */
val DividerGray = Color(0xFFBDBDBD)

/** 浅灰 — 头像占位背景 */
val AvatarPlaceholderBg = Color(0xFFE0E0E0)

/** 中灰 — 错误/占位文字 */
val PlaceholderGray = Color(0xFF757575)
