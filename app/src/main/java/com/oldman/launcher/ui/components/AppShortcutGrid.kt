package com.oldman.launcher.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.oldman.launcher.data.entity.AppShortcutEntity
import com.oldman.launcher.ui.theme.AvatarPlaceholderBg
import com.oldman.launcher.utils.AppLauncherUtils
import com.oldman.launcher.viewmodel.MainViewModel

/**
 * App 快捷图标网格 — 2列布局
 *
 * 位于日期栏下方，显示所有常用 App 的快捷入口。
 * 每个图标包含: 80dp 圆形图标 + 28sp App 名称
 *
 * 点击行为: 通过包名启动对应 App（使用隐式 LAUNCHER Intent）
 */
@Composable
fun AppShortcutGrid(
    viewModel: MainViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val appShortcuts by viewModel.appShortcuts.collectAsStateWithLifecycle()

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        items(appShortcuts, key = { it.id }) { shortcut ->
            AppShortcutItem(shortcut = shortcut)
        }
    }
}

/**
 * 单个 App 快捷图标项
 *
 * 设计约束:
 * - 点击区域 ≥ 48dp（通过 padding 和图标尺寸保证）
 * - 图标: 80dp 圆形，从本地 drawable 资源加载
 * - 名称: 28sp 加粗，最多2行，超出省略号
 */
@Composable
fun AppShortcutItem(
    shortcut: AppShortcutEntity,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .clickable {
                AppLauncherUtils.launchApp(context, shortcut.packageName, shortcut.name)
            }
            .padding(12.dp),  // padding 扩大点击区域至 ≥ 48dp
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // ── 圆形图标 (80dp) ──
        // 使用本地 VectorDrawable 资源加载，零网络依赖
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(AvatarPlaceholderBg),
            contentAlignment = Alignment.Center
        ) {
            // 通过 iconResId 加载本地 drawable 资源
            Icon(
                painter = painterResource(id = shortcut.iconResId),
                contentDescription = shortcut.name,
                modifier = Modifier.size(48.dp),
                tint = Color(0xFF424242)
            )
        }

        // ── App 名称 (28sp 加粗) ──
        Text(
            text = shortcut.name,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}
