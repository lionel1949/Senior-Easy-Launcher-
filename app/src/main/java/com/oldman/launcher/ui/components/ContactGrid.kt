package com.oldman.launcher.ui.components

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.oldman.launcher.data.entity.ContactEntity
import com.oldman.launcher.ui.theme.AvatarPlaceholderBg
import com.oldman.launcher.viewmodel.MainViewModel

/**
 * 联系人快捷图标网格 — 2列布局
 *
 * 位于 App 快捷区下方，显示所有联系人卡片。
 * 每个卡片包含: 80dp 圆形头像 + 28sp 姓名
 *
 * 点击行为: 弹出 ContactActionDialog 让用户选择「打电话」或「打微信」
 */
@Composable
fun ContactGrid(
    viewModel: MainViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val contacts by viewModel.contacts.collectAsStateWithLifecycle()

    // 当前被选中的联系人（用于弹出操作对话框）
    var selectedContact by remember { mutableStateOf<ContactEntity?>(null) }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        items(contacts, key = { it.id }) { contact ->
            ContactCard(
                contact = contact,
                onClick = { selectedContact = contact }
            )
        }
    }

    // ── 操作对话框 ──
    selectedContact?.let { contact ->
        ContactActionDialog(
            contact = contact,
            onDismiss = { selectedContact = null }
        )
    }
}

/**
 * 单个联系人卡片
 *
 * 设计约束:
 * - 点击区域 ≥ 48dp
 * - 头像: 80dp 圆形，默认灰色占位 + 人物图标
 * - 姓名: 28sp 加粗
 */
@Composable
fun ContactCard(
    contact: ContactEntity,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(12.dp),  // padding 确保点击区域 ≥ 48dp
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // ── 圆形头像占位 (80dp) ──
        // 实际项目中此处应从 avatarPath 加载本地图片
        // MVP 阶段使用默认头像占位
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(AvatarPlaceholderBg),
            contentAlignment = Alignment.Center
        ) {
            // 默认人物图标 — 使用项目内置的矢量头像
            Icon(
                painter = painterResource(id = com.oldman.launcher.R.drawable.ic_default_avatar),
                contentDescription = "头像",
                modifier = Modifier.size(48.dp),
                tint = Color.Unspecified  // 保留原始颜色
            )
        }

        // ── 联系人姓名 (28sp 加粗) ──
        Text(
            text = contact.name,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
