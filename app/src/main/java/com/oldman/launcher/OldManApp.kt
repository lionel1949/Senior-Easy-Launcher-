package com.oldman.launcher

import android.app.Application

/**
 * 老年桌面 Application 类
 *
 * 作用:
 * - 作为 AndroidManifest 中注册的 Application 入口
 * - 在必要时触发 Room 数据库的预初始化（首次访问时由 ViewModel 触发懒加载）
 *
 * 当前 MVP 版本不需要在 onCreate 中做特殊初始化。
 * Room 数据库采用懒加载单例模式，在 ViewModel 首次访问时自动创建。
 *
 * 后续可在此添加:
 * - 全局异常处理
 * - 日志系统（仅本地，不上传）
 */
class OldManApp : Application()
