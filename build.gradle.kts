// ============================================================
// 老年桌面 (OldMan Launcher) — Project 级构建配置
// Kotlin DSL · 无冗余依赖 · 纯单机运行
// ============================================================

plugins {
    id("com.android.application") version "8.13.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
    // KSP — Room 编译期注解处理器
    id("com.google.devtools.ksp") version "1.9.22-1.0.17" apply false
}
