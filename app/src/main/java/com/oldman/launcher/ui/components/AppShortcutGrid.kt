package com.oldman.launcher.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.oldman.launcher.data.entity.AppShortcutEntity
import com.oldman.launcher.utils.AppLauncherUtils
import com.oldman.launcher.viewmodel.MainViewModel

/**
 * App 快捷图标网格 — 2列布局
 *
 * 位于日期栏下方，显示所有常用 App 的快捷入口。
 * 每个图标包含: 80dp 圆形图标 + 28sp App 名称
 *
 * 点击 → 启动 App
 * 长按 → 触发 onLongClick 回调（管理/删除）
 */
@Composable
fun AppShortcutGrid(
    viewModel: MainViewModel = viewModel(),
    onLongClick: (AppShortcutEntity) -> Unit = {},
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
            AppShortcutItem(
                shortcut = shortcut,
                onLongClick = { onLongClick(shortcut) }
            )
        }
    }
}

/**
 * 单个 App 快捷图标项
 *
 * 设计约束:
 * - 点击 → 启动 App
 * - 长按 → 管理
 * - 图标优先从 App 真实图标加载，回退到内置资源
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppShortcutItem(
    shortcut: AppShortcutEntity,
    onLongClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .combinedClickable(
                onClick = {
                    AppLauncherUtils.launchApp(context, shortcut.packageName, shortcut.name)
                },
                onLongClick = onLongClick
            )
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // ── 圆形图标 (80dp) ──
        // 优先加载 App 真实图标，回退到内置 drawable
        AppIcon(
            packageName = shortcut.packageName,
            iconResId = shortcut.iconResId,
            modifier = Modifier
        )

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
