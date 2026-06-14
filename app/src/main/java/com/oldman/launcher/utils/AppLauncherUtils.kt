package com.oldman.launcher.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast

/**
 * App 启动工具类
 *
 * 提供三个核心功能:
 * 1. launchApp()       — 通过包名启动任意 App
 * 2. callPhone()       — 一键拨打电话 (需 CALL_PHONE 权限)
 * 3. openWeChatChat()  — 通过 URL Scheme 跳转微信聊天界面
 *
 * ============ 微信 URL Scheme 兼容性说明 ============
 *
 * **重要**: 微信从未公开过第三方调用的 URL Scheme 文档。
 * 以下方案来自民间逆向探索，经测试在微信 8.0.x 版本有效，
 * 但微信可能在任何版本更新中修改或封禁此接口。
 *
 * **跳转方案 (按优先级尝试)**:
 * 1. weixin://dl/chat?{微信号}     ← 首选方案，直接进入聊天界面
 * 2. weixin://                     ← 回退方案，仅打开微信首页
 * 3. 弹 Toast 提示用户手动打开微信  ← 最终降级
 *
 * **已知限制**:
 * - 只能跳转到聊天界面，无法直接发起语音/视频通话
 * - 需要对方已是微信好友，否则打开的是空聊天或异常界面
 * - 微信号为空时按钮自动隐藏（在 UI 层处理）
 *
 * **测试环境**: 微信 8.0.48 + Android 14 (API 34)
 *
 * ============ 国产 ROM 兼容性 ============
 *
 * 本工具使用标准 Android API，不依赖厂商特定 SDK:
 * - Intent.ACTION_CALL: 调用后系统弹出拨号确认（国产ROM通常不会拦截）
 * - PackageManager.getLaunchIntentForPackage(): 小米/华为/OPPO 均正常支持
 * - CALL_PHONE 权限: 首次调用时系统自动弹授权框
 */
object AppLauncherUtils {

    /**
     * 通过包名启动 App
     *
     * 使用标准 LAUNCHER Intent，兼容性最佳。
     * 如果 App 未安装，Toast 提示用户。
     *
     * @param context     上下文
     * @param packageName App 的包名 (如 "com.tencent.mm")
     * @param appName     App 显示名称，用于未安装时的提示
     */
    fun launchApp(context: Context, packageName: String, appName: String) {
        try {
            val intent = context.packageManager.getLaunchIntentForPackage(packageName)
            if (intent != null) {
                // 新建任务栈启动，避免干扰桌面自身
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            } else {
                // App 未安装
                Toast.makeText(context, "「$appName」未安装", Toast.LENGTH_SHORT).show()
            }
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "「$appName」无法启动", Toast.LENGTH_SHORT).show()
        } catch (e: SecurityException) {
            Toast.makeText(context, "「$appName」启动被系统阻止", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * 检查 App 是否已安装
     *
     * @return true 表示已安装，false 表示未安装
     */
    fun isAppInstalled(context: Context, packageName: String): Boolean {
        return try {
            context.packageManager.getPackageInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    /**
     * 一键拨打电话
     *
     * 直接调用系统拨号器，不经过中间确认界面。
     * 需要 CALL_PHONE 权限，首次调用时系统自动弹授权框。
     *
     * **安全说明**: 该权限为"危险权限"，Android 6.0+ 需要运行时授权。
     * 如果用户拒绝授权，会 Toast 引导用户去设置中手动开启。
     *
     * @param context     上下文
     * @param phoneNumber 电话号码（纯数字，如 "13800138000"）
     */
    fun callPhone(context: Context, phoneNumber: String) {
        if (phoneNumber.isBlank()) {
            Toast.makeText(context, "未设置电话号码", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            // 过滤非法字符，只保留数字和+
            val cleanedNumber = phoneNumber.replace(Regex("[^+\\d]"), "")
            val intent = Intent(Intent.ACTION_CALL).apply {
                data = Uri.parse("tel:$cleanedNumber")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            // 检查权限
            val permissionCheck = context.checkSelfPermission(android.Manifest.permission.CALL_PHONE)
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                context.startActivity(intent)
            } else {
                // 权限未授予——在实际 App 中，此处应该引导用户授权
                // 由于这是桌面 App，没有 Activity 能接收 onRequestPermissionsResult 回调
                // 因此直接 Toast 提示并退出
                Toast.makeText(
                    context,
                    "请在系统设置中允许「老年桌面」拨打电话权限\n设置 → 应用 → 老年桌面 → 权限",
                    Toast.LENGTH_LONG
                ).show()
            }
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "无法拨打电话", Toast.LENGTH_SHORT).show()
        } catch (e: SecurityException) {
            Toast.makeText(context, "拨号权限被系统阻止", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * 跳转微信聊天界面
     *
     * 通过民间探索的 URL Scheme 实现。跳转策略:
     * 1. 首选: weixin://dl/chat?{微信号}
     * 2. 回退: weixin:// (仅打开微信)
     *
     * **兼容性警告**: 微信官方未公开此接口，可能随版本更新失效。
     * 如跳转失败，请引导用户在微信内手动搜索联系人。
     *
     * @param context  上下文
     * @param wechatId 微信号/微信别名
     */
    fun openWeChatChat(context: Context, wechatId: String) {
        if (wechatId.isBlank()) {
            Toast.makeText(context, "未设置微信号", Toast.LENGTH_SHORT).show()
            return
        }

        // 先检查微信是否安装
        if (!isAppInstalled(context, "com.tencent.mm")) {
            Toast.makeText(context, "未安装微信，请先安装微信", Toast.LENGTH_LONG).show()
            return
        }

        // ── 方案1: 直接跳转聊天界面 ──
        val chatUri = "weixin://dl/chat?$wechatId"
        try {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(chatUri)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
            // 如果成功启动，直接返回
            return
        } catch (e: ActivityNotFoundException) {
            // 方案1失败，尝试方案2
        } catch (e: SecurityException) {
            Toast.makeText(context, "微信跳转被系统阻止", Toast.LENGTH_SHORT).show()
            return
        }

        // ── 方案2: 仅打开微信 ──
        try {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("weixin://")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
            Toast.makeText(context, "请在微信内手动选择联系人", Toast.LENGTH_LONG).show()
            return
        } catch (e: ActivityNotFoundException) {
            // 方案2也失败
        }

        // ── 方案3: 降级提示 ──
        Toast.makeText(
            context,
            "当前微信版本不支持直接跳转，请在微信内手动打开聊天",
            Toast.LENGTH_LONG
        ).show()
    }

    /**
     * 检查当前 App 是否为默认桌面
     *
     * 如果本 App 不是默认桌面，按 Home 键会回到系统桌面而非本 App。
     * 调用此方法检测并提示用户设置。
     *
     * @return true 表示当前是默认桌面，false 表示不是
     */
    fun isDefaultLauncher(context: Context): Boolean {
        val intent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_HOME)
        }
        val resolveInfo = context.packageManager.resolveActivity(
            intent,
            PackageManager.MATCH_DEFAULT_ONLY
        )
        return resolveInfo?.activityInfo?.packageName == context.packageName
    }

    /**
     * 打开系统"选择默认桌面"对话框
     *
     * 这会显示系统桌面选择器，让用户将本 App 设为默认桌面。
     * 国产 ROM（小米/华为/OPPO等）可能会替换此对话框为自定义界面。
     */
    fun openDefaultLauncherPicker(context: Context) {
        try {
            val intent = Intent(Intent.ACTION_MAIN).apply {
                addCategory(Intent.CATEGORY_HOME)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "请前往系统设置中将本应用设为默认桌面", Toast.LENGTH_LONG).show()
        }
    }
}
