package com.oldman.launcher.ui

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.oldman.launcher.ui.components.HomeScreen
import com.oldman.launcher.ui.theme.OldManTheme

/**
 * 主 Activity — 老年桌面唯一入口
 *
 * 配置说明:
 * - 全屏显示: 隐藏系统状态栏和导航栏，内容占满屏幕
 * - 固定竖屏: 在 AndroidManifest 中设置 screenOrientation="portrait"
 * - 单例模式: launchMode="singleTask"，防止多个实例
 *
 * 操作:
 * - 按 Home 键 → 回到本桌面 (如果已设为默认桌面)
 * - 从桌面启动 App → 显示主屏幕
 *
 * 国产 ROM 设置默认桌面指南:
 * 小米: 设置 → 应用管理 → 默认应用 → 桌面 → 选择「老年桌面」
 * 华为: 设置 → 应用 → 默认应用 → 桌面 → 选择「老年桌面」
 * OPPO: 设置 → 应用管理 → 默认应用管理 → 桌面 → 选择「老年桌面」
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ── 全屏配置 ──
        // 启用边到边显示，让内容延伸到系统栏区域
        enableEdgeToEdge()

        // 隐藏系统状态栏和导航栏
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // 隐藏状态栏，让桌面内容占满全屏
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.hide(WindowInsetsCompat.Type.statusBars())
        controller.hide(WindowInsetsCompat.Type.navigationBars())

        // 设置全屏 flag — 让系统栏不显示
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

        // 不让屏幕休眠时自动变暗（可选，老人可能阅读较慢）
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        // ── 设置 Compose 内容 ──
        setContent {
            OldManTheme {
                HomeScreen(modifier = Modifier.fillMaxSize())
            }
        }
    }

    /**
     * 当 Activity 从后台恢复时调用
     *
     * 如果是按 Home 键后重新进入桌面:
     * - singleTask 模式确保不会创建新实例
     * - 如果收到新的 HOME Intent，刷新 UI 状态
     */
    override fun onNewIntent(intent: android.content.Intent) {
        super.onNewIntent(intent)
        // singleTask 模式下，按 Home 键回来会走这里
        // 当前 MVP 版本不需要额外处理，ViewModel 会自动从 Room 刷新数据
    }

    /**
     * 拦截系统返回键
     *
     * 桌面应用中，返回键不应该做任何事（用户不能"退出"桌面）。
     * 此方法确保按返回键不会退出桌面或回到系统桌面。
     */
    override fun onBackPressed() {
        // 桌面应用不响应返回键
        // 如果用户按返回键，不做任何操作
        // 这样可以防止老人误触返回键退出桌面
    }
}
