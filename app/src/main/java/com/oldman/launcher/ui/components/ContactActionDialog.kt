package com.oldman.launcher.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.oldman.launcher.data.entity.ContactEntity
import com.oldman.launcher.ui.theme.AlertRed
import com.oldman.launcher.ui.theme.WechatGreen
import com.oldman.launcher.utils.AppLauncherUtils

/**
 * 联系人操作对话框
 *
 * 点击联系人卡片后弹出，提供两个操作按钮:
 * - **打电话**: 红色大按钮，一键拨打 tel: 协议
 * - **打微信**: 绿色大按钮，跳转微信聊天界面
 *
 * 设计约束:
 * - 按钮最小高度 ≥ 60dp，确保老人轻松点击
 * - 按钮文字 ≥ 28sp 加粗
 * - 按钮间距 ≥ 16dp
 * - 电话为红色、微信为绿色——利用颜色帮助老人快速区分
 *
 * **智能显隐**: 如果微信号为空，则不显示「打微信」按钮；
 * 如果电话号码为空，则不显示「打电话」按钮。
 *
 * @param contact  被点击的联系人
 * @param onDismiss 关闭对话框回调
 */
@Composable
fun ContactActionDialog(
    contact: ContactEntity,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    // 判断哪些按钮可用
    val hasPhone = contact.phoneNumber.isNotBlank()
    val hasWechat = contact.wechatId.isNotBlank()

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        // 对话框容器
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .then(
                    // 白色背景圆角卡片
                    Modifier.padding(0.dp)
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // ── 标题: 联系人名字 ──
            Text(
                text = contact.name,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = androidx.compose.ui.graphics.Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            // ── 打电话按钮 (红色，仅在电话号码不为空时显示) ──
            if (hasPhone) {
                Button(
                    onClick = {
                        AppLauncherUtils.callPhone(context, contact.phoneNumber)
                        onDismiss()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp),  // 大按钮，方便老人点击
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AlertRed
                    )
                ) {
                    Text(
                        text = "📞 打电话",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = androidx.compose.ui.graphics.Color.White
                    )
                }
            }

            // ── 打微信按钮 (绿色，仅在微信号不为空时显示) ──
            if (hasWechat) {
                OutlinedButton(
                    onClick = {
                        AppLauncherUtils.openWeChatChat(context, contact.wechatId)
                        onDismiss()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp),  // 大按钮，方便老人点击
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = WechatGreen,
                        contentColor = androidx.compose.ui.graphics.Color.White
                    )
                ) {
                    Text(
                        text = "💬 打微信",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // ── 如果两者都为空 ──
            if (!hasPhone && !hasWechat) {
                Text(
                    text = "暂未设置联系方式\n请在设置中添加电话号码或微信号",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = androidx.compose.ui.graphics.Color.Gray,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
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
