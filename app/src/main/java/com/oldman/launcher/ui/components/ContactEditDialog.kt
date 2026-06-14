package com.oldman.launcher.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.oldman.launcher.data.entity.ContactEntity
import com.oldman.launcher.ui.theme.AlertRed

/**
 * 联系人编辑对话框
 *
 * 用于新建或编辑联系人信息（姓名、电话号码、微信号）。
 * 长按联系人卡片后弹出。
 *
 * 设计约束:
 * - 所有文字 ≥ 24sp 加粗
 * - 输入框高度 ≥ 60dp，方便老人点击
 * - 保存/取消按钮区分明确
 *
 * @param contact     现有联系人（编辑模式），为 null 时进入新建模式
 * @param onSave      保存回调，返回更新后的 ContactEntity
 * @param onDelete    删除回调（仅编辑模式），传递被删除的 ContactEntity
 * @param onDismiss   取消/关闭回调
 */
@Composable
fun ContactEditDialog(
    contact: ContactEntity?,
    onSave: (ContactEntity) -> Unit,
    onDelete: ((ContactEntity) -> Unit)? = null,
    onDismiss: () -> Unit
) {
    val isNewContact = contact == null
    var name by remember { mutableStateOf(contact?.name ?: "") }
    var phoneNumber by remember { mutableStateOf(contact?.phoneNumber ?: "") }
    var wechatId by remember { mutableStateOf(contact?.wechatId ?: "") }

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
                .background(
                    color = androidx.compose.ui.graphics.Color.White,
                    shape = RoundedCornerShape(24.dp)
                )
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ── 标题 ──
            Text(
                text = if (isNewContact) "添加联系人" else "编辑联系人",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = androidx.compose.ui.graphics.Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            // ── 姓名输入 ──
            Text(
                text = "姓名",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = androidx.compose.ui.graphics.Color.Black,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = androidx.compose.ui.graphics.Color.Black
                ),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = androidx.compose.ui.graphics.Color(0xFF1976D2),
                    unfocusedBorderColor = androidx.compose.ui.graphics.Color(0xFFBDBDBD),
                    focusedContainerColor = androidx.compose.ui.graphics.Color(0xFFFAFAFA),
                    unfocusedContainerColor = androidx.compose.ui.graphics.Color(0xFFFAFAFA)
                )
            )

            // ── 电话号码输入 ──
            Text(
                text = "电话号码",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = androidx.compose.ui.graphics.Color.Black,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = androidx.compose.ui.graphics.Color.Black
                ),
                singleLine = true,
                placeholder = {
                    Text(
                        text = "如: 13800138000",
                        fontSize = 22.sp,
                        color = androidx.compose.ui.graphics.Color(0xFF9E9E9E)
                    )
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = androidx.compose.ui.graphics.Color(0xFF1976D2),
                    unfocusedBorderColor = androidx.compose.ui.graphics.Color(0xFFBDBDBD),
                    focusedContainerColor = androidx.compose.ui.graphics.Color(0xFFFAFAFA),
                    unfocusedContainerColor = androidx.compose.ui.graphics.Color(0xFFFAFAFA)
                )
            )

            // ── 微信号输入 ──
            Text(
                text = "微信号",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = androidx.compose.ui.graphics.Color.Black,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = wechatId,
                onValueChange = { wechatId = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = androidx.compose.ui.graphics.Color.Black
                ),
                singleLine = true,
                placeholder = {
                    Text(
                        text = "微信ID或手机号（可选）",
                        fontSize = 22.sp,
                        color = androidx.compose.ui.graphics.Color(0xFF9E9E9E)
                    )
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = androidx.compose.ui.graphics.Color(0xFF1976D2),
                    unfocusedBorderColor = androidx.compose.ui.graphics.Color(0xFFBDBDBD),
                    focusedContainerColor = androidx.compose.ui.graphics.Color(0xFFFAFAFA),
                    unfocusedContainerColor = androidx.compose.ui.graphics.Color(0xFFFAFAFA)
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // ── 保存按钮 ──
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        val updatedContact = ContactEntity(
                            id = contact?.id ?: 0,
                            name = name.trim(),
                            phoneNumber = phoneNumber.trim(),
                            wechatId = wechatId.trim()
                        )
                        onSave(updatedContact)
                        onDismiss()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = androidx.compose.ui.graphics.Color(0xFF1976D2)
                )
            ) {
                Text(
                    text = "保存",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = androidx.compose.ui.graphics.Color.White
                )
            }

            // ── 删除按钮（仅编辑模式） ──
            if (!isNewContact && onDelete != null && contact != null) {
                OutlinedButton(
                    onClick = {
                        onDelete(contact)
                        onDismiss()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = AlertRed
                    )
                ) {
                    Text(
                        text = "删除联系人",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = androidx.compose.ui.graphics.Color.White
                    )
                }
            }

            // ── 取消按钮 ──
            Button(
                onClick = onDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
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
    }
}
