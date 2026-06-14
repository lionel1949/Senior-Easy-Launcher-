package com.oldman.launcher.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
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
 * 点击 → 弹出操作对话框（打电话/打微信）
 * 长按 → 弹出编辑对话框（修改姓名、电话、微信号）
 */
@Composable
fun ContactGrid(
    viewModel: MainViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val contacts by viewModel.contacts.collectAsStateWithLifecycle()

    // 操作对话框
    var selectedContact by remember { mutableStateOf<ContactEntity?>(null) }
    // 编辑对话框
    var editingContact by remember { mutableStateOf<ContactEntity?>(null) }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        items(contacts, key = { it.id }) { contact ->
            ContactCard(
                contact = contact,
                onClick = { selectedContact = contact },
                onLongClick = { editingContact = contact }
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

    // ── 编辑对话框 ──
    editingContact?.let { contact ->
        ContactEditDialog(
            contact = contact,
            onSave = { updated ->
                viewModel.updateContact(updated)
                editingContact = null
            },
            onDelete = { toDelete ->
                viewModel.deleteContact(toDelete)
                editingContact = null
            },
            onDismiss = { editingContact = null }
        )
    }
}

/**
 * 单个联系人卡片
 *
 * 设计约束:
 * - 点击 → 打电话/打微信
 * - 长按 → 编辑联系人信息
 * - 头像: 80dp 圆形
 * - 姓名: 28sp 加粗
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ContactCard(
    contact: ContactEntity,
    onClick: () -> Unit,
    onLongClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            )
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // ── 圆形头像占位 (80dp) ──
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(AvatarPlaceholderBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = com.oldman.launcher.R.drawable.ic_default_avatar),
                contentDescription = "头像",
                modifier = Modifier.size(48.dp),
                tint = Color.Unspecified
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
