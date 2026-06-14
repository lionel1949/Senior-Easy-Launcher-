package com.oldman.launcher.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * 老年桌面排版系统
 *
 * 铁律（任何情况下不得违反）:
 * - 所有字号 ≥ 24sp
 * - 所有文字加粗 (FontWeight.Bold)
 * - 核心标题字号 ≥ 48sp
 *
 * 这些限制确保老花眼/白内障等视力下降老人也能清晰辨认文字。
 */

val OldManTypography = Typography(
    // 大型标题 — 48sp 加粗 (时间显示用)
    headlineLarge = TextStyle(
        fontSize = 48.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.sp
    ),

    // 中型标题 — 40sp 加粗 (分区标题如"常用应用")
    headlineMedium = TextStyle(
        fontSize = 40.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.sp
    ),

    // 小型标题 — 32sp 加粗 (阳历日期)
    headlineSmall = TextStyle(
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.sp
    ),

    // 正文大字 — 28sp 加粗 (App名称、联系人姓名、农历日期)
    bodyLarge = TextStyle(
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.sp
    ),

    // 正文中字 — 24sp 加粗 (按钮文字、对话框文字)
    bodyMedium = TextStyle(
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.sp
    ),

    // 正文小字 — 24sp 加粗 (最低限制)
    bodySmall = TextStyle(
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.sp
    )
)
