package com.oldman.launcher.ui.components

import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.oldman.launcher.data.entity.AppShortcutEntity
import com.oldman.launcher.ui.theme.AlertRed
import com.oldman.launcher.ui.theme.AvatarPlaceholderBg

/**
 * 快捷方式管理对话框
 *
 * 长按 App 快捷图标后弹出。分为上下两区:
 * - 上半区: 当前已添加的快捷方式（可删除）
 * - 下半区: 系统中尚未添加的 App 列表（可添加）
 *
 * 设计约束:
 * - 所有文字 ≥ 22sp
 * - 列表项高度 ≥ 64dp
 * - 删除按钮红色，添加按钮蓝色
 */
@Composable
fun AppShortcutManageDialog(
    currentShortcuts: List<AppShortcutEntity>,
    onAdd: (AppShortcutEntity) -> Unit,
    onRemove: (AppShortcutEntity) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val pm = context.packageManager

    // 扫描所有已安装的可启动 App
    val installedApps = remember {
        try {
            val intent = android.content.Intent(android.content.Intent.ACTION_MAIN).apply {
                addCategory(android.content.Intent.CATEGORY_LAUNCHER)
            }
            pm.queryIntentActivities(intent, 0)
                .map { it.activityInfo }
                .filter { it.packageName != context.packageName }  // 排除自身
                .sortedBy { it.loadLabel(pm).toString() }
        } catch (e: Exception) {
            emptyList()
        }
    }

    val existingPackages = remember(currentShortcuts) {
        currentShortcuts.map { it.packageName }.toSet()
    }

    // 未添加的 App
    val availableApps = remember(installedApps, existingPackages) {
        installedApps.filter { it.packageName !in existingPackages }
    }

    // 0/1 切换: false = 查看已添加, true = 浏览可添加
    var showAvailable by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(24.dp))
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ── 标题 ──
            Text(
                text = "管理快捷方式",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ── 切换按钮 ──
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { showAvailable = false },
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (!showAvailable) Color(0xFF1976D2) else Color(0xFFE0E0E0)
                    )
                ) {
                    Text(
                        text = "已添加 (${currentShortcuts.size})",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (!showAvailable) Color.White else Color.Black
                    )
                }
                Button(
                    onClick = { showAvailable = true },
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (showAvailable) Color(0xFF1976D2) else Color(0xFFE0E0E0)
                    )
                ) {
                    Text(
                        text = "可添加 (${availableApps.size})",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (showAvailable) Color.White else Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ── 列表区域（限高以适配屏幕） ──
            Box(modifier = Modifier.height(400.dp)) {
                if (!showAvailable) {
                    // 已添加的快捷方式 — 可删除
                    if (currentShortcuts.isEmpty()) {
                        Text(
                            text = "还没有添加快捷方式",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray,
                            modifier = Modifier.padding(32.dp)
                        )
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(
                                items = currentShortcuts,
                                key = { it.id }
                            ) { shortcut ->
                                ShortcutRow(
                                    name = shortcut.name,
                                    icon = { AppIcon(packageName = shortcut.packageName, iconResId = shortcut.iconResId) },
                                    actionLabel = "删除",
                                    actionColor = AlertRed,
                                    onAction = { onRemove(shortcut) }
                                )
                            }
                        }
                    }
                } else {
                    // 可添加的 App
                    if (availableApps.isEmpty()) {
                        Text(
                            text = "没有可添加的 App",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray,
                            modifier = Modifier.padding(32.dp)
                        )
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(
                                items = availableApps,
                                key = { it.packageName }
                            ) { info ->
                                val appName = info.loadLabel(pm).toString()
                                ShortcutRow(
                                    name = appName,
                                    icon = { AppIcon(packageName = info.packageName, iconResId = 0) },
                                    actionLabel = "添加",
                                    actionColor = Color(0xFF1976D2),
                                    onAction = {
                                        onAdd(
                                            AppShortcutEntity(
                                                name = appName,
                                                packageName = info.packageName,
                                                iconResId = 0,  // 动态加载图标
                                                sortOrder = Int.MAX_VALUE
                                            )
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ── 关闭按钮 ──
            Button(
                onClick = onDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE0E0E0)
                )
            ) {
                Text(
                    text = "关闭",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }
    }
}

/**
 * 快捷方式行 — 图标 + 名称 + 操作按钮
 */
@Composable
private fun ShortcutRow(
    name: String,
    icon: @Composable () -> Unit,
    actionLabel: String,
    actionColor: Color,
    onAction: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            icon()
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = name,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Button(
            onClick = onAction,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = actionColor),
            contentPadding = ButtonDefaults.TextButtonContentPadding
        ) {
            Text(
                text = actionLabel,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

/**
 * App 图标 — 优先从本地资源加载，回退到 PackageManager 动态加载
 *
 * 对于预置快捷方式（电话、微信等），使用 drawable 资源；
 * 对于动态添加的 App，从 APK 中提取图标。
 */
@Composable
fun AppIcon(
    packageName: String,
    iconResId: Int,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // 尝试从 PackageManager 加载图标
    val iconBitmap = remember(packageName) {
        try {
            val drawable = context.packageManager.getApplicationIcon(packageName)
            drawableToBitmap(drawable)
        } catch (e: Exception) {
            null
        }
    }

    Box(
        modifier = modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(AvatarPlaceholderBg),
        contentAlignment = Alignment.Center
    ) {
        if (iconBitmap != null) {
            // 使用 App 真实图标
            androidx.compose.foundation.Image(
                bitmap = iconBitmap.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.size(36.dp)
            )
        } else if (iconResId != 0) {
            // 回退到内置资源
            Icon(
                painter = painterResource(id = iconResId),
                contentDescription = null,
                modifier = Modifier.size(36.dp),
                tint = Color(0xFF424242)
            )
        } else {
            // 完全回退 — 默认 App 图标
            Icon(
                painter = painterResource(id = com.oldman.launcher.R.drawable.ic_settings),
                contentDescription = null,
                modifier = Modifier.size(36.dp),
                tint = Color(0xFF424242)
            )
        }
    }
}

/**
 * 将 Android Drawable 转换为 Bitmap
 */
private fun drawableToBitmap(drawable: Drawable): Bitmap? {
    return try {
        if (drawable is BitmapDrawable && drawable.bitmap != null) {
            drawable.bitmap
        } else {
            val bitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth.takeIf { it > 0 } ?: 96,
                drawable.intrinsicHeight.takeIf { it > 0 } ?: 96,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
        }
    } catch (e: Exception) {
        null
    }
}
