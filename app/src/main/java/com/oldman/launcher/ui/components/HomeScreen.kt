package com.oldman.launcher.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oldman.launcher.ui.theme.DividerGray

/**
 * 主屏幕布局
 *
 * 整个 App 的唯一页面，垂直滚动布局:
 * ```
 * ┌────────────────────────┐
 * │      日期时间栏         │  ← DateTimeBar (自刷新)
 * ├────────────────────────┤
 * │    ── 分割线 ──        │
 * ├────────────────────────┤
 * │     常用应用 (标题)     │
 * │  [电话] [短信]         │  ← AppShortcutGrid (2列)
 * │  [微信] [抖音]         │
 * │  [相机] [设置]         │
 * ├────────────────────────┤
 * │    ── 分割线 ──        │
 * ├────────────────────────┤
 * │    常用联系人 (标题)    │
 * │  [爸爸] [妈妈]         │  ← ContactGrid (2列)
 * │  [子女] [...]          │
 * └────────────────────────┘
 * ```
 *
 * 设计约束:
 * - 可滚动: 内容超过屏幕高度时自动滚动
 * - 间距 ≥ 24dp: 所有板块之间间距充足
 * - 无动画: 所有过渡即现即隐
 */
@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // ── 模块1: 日期时间栏 ──
        DateTimeBar()

        // ── 分割线 ──
        Divider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 2.dp,
            color = DividerGray
        )

        Spacer(modifier = Modifier.height(8.dp))

        // ── 模块2: 常用应用 ──
        Text(
            text = "常用应用",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = androidx.compose.ui.graphics.Color.Black,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        AppShortcutGrid()

        Spacer(modifier = Modifier.height(8.dp))

        // ── 分割线 ──
        Divider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 2.dp,
            color = DividerGray
        )

        Spacer(modifier = Modifier.height(8.dp))

        // ── 模块3: 常用联系人 ──
        Text(
            text = "常用联系人",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = androidx.compose.ui.graphics.Color.Black,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        ContactGrid()

        // 底部留白，确保最后一个元素不被遮挡
        Spacer(modifier = Modifier.height(48.dp))
    }
}
