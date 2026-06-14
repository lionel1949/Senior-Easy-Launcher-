package com.oldman.launcher.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.oldman.launcher.data.entity.AppShortcutEntity
import com.oldman.launcher.ui.theme.DividerGray
import com.oldman.launcher.viewmodel.MainViewModel

/**
 * 主屏幕布局
 *
 * 整个 App 的唯一页面，垂直滚动布局:
 * ```
 * ┌────────────────────────┐
 * │      日期时间栏         │
 * ├────────────────────────┤
 * │  常用应用  [管理]       │
 * │  [电话] [短信]         │
 * ├────────────────────────┤
 * │  常用联系人  [添加]     │
 * │  [爸爸] [妈妈]         │
 * └────────────────────────┘
 * ```
 *
 * 交互:
 * - 点击快捷方式 → 启动 App
 * - 长按快捷方式 → 编辑/删除
 * - 点击联系人 → 打电话/打微信
 * - 长按联系人 → 编辑联系人信息
 */
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel()
) {
    // ── 对话框状态 ──
    var showShortcutManage by remember { mutableStateOf(false) }
    var showNewContactDialog by remember { mutableStateOf(false) }
    var editingShortcut by remember { mutableStateOf<AppShortcutEntity?>(null) }

    val currentShortcuts by viewModel.appShortcuts.collectAsStateWithLifecycle()

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
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 2.dp,
            color = DividerGray
        )

        Spacer(modifier = Modifier.height(8.dp))

        // ── 模块2: 常用应用 ──
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "常用应用",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = androidx.compose.ui.graphics.Color.Black
            )
            Button(
                onClick = { showShortcutManage = true },
                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = androidx.compose.ui.graphics.Color(0xFF1976D2)
                )
            ) {
                Text(
                    text = "管理",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = androidx.compose.ui.graphics.Color.White
                )
            }
        }

        AppShortcutGrid(
            viewModel = viewModel,
            onLongClick = { shortcut -> editingShortcut = shortcut }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // ── 分割线 ──
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 2.dp,
            color = DividerGray
        )

        Spacer(modifier = Modifier.height(8.dp))

        // ── 模块3: 常用联系人 ──
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "常用联系人",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = androidx.compose.ui.graphics.Color.Black
            )
            Button(
                onClick = { showNewContactDialog = true },
                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = androidx.compose.ui.graphics.Color(0xFF1976D2)
                )
            ) {
                Text(
                    text = "添加",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = androidx.compose.ui.graphics.Color.White
                )
            }
        }

        ContactGrid(viewModel = viewModel)

        // 底部留白
        Spacer(modifier = Modifier.height(48.dp))
    }

    // ── 快捷方式管理对话框 ──
    if (showShortcutManage) {
        AppShortcutManageDialog(
            currentShortcuts = currentShortcuts,
            onAdd = { shortcut ->
                viewModel.insertShortcut(shortcut)
            },
            onRemove = { shortcut ->
                viewModel.deleteShortcut(shortcut)
            },
            onDismiss = { showShortcutManage = false }
        )
    }

    // ── 单个快捷方式编辑（长按 → 删除） ──
    editingShortcut?.let { shortcut ->
        // 简单确认对话框 — 长按后直接问是否删除
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { editingShortcut = null },
            title = {
                Text(
                    text = "删除「${shortcut.name}」快捷方式？",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = androidx.compose.ui.graphics.Color.Black
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteShortcut(shortcut)
                        editingShortcut = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = com.oldman.launcher.ui.theme.AlertRed
                    )
                ) {
                    Text(
                        text = "删除",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = androidx.compose.ui.graphics.Color.White
                    )
                }
            },
            dismissButton = {
                Button(
                    onClick = { editingShortcut = null },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = androidx.compose.ui.graphics.Color(0xFFE0E0E0)
                    )
                ) {
                    Text(
                        text = "取消",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = androidx.compose.ui.graphics.Color.Black
                    )
                }
            }
        )
    }

    // ── 新建联系人对话框 ──
    if (showNewContactDialog) {
        ContactEditDialog(
            contact = null,  // 新建模式
            onSave = { newContact ->
                viewModel.insertContact(newContact)
                showNewContactDialog = false
            },
            onDismiss = { showNewContactDialog = false }
        )
    }
}

