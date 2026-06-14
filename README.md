# 老年桌面 · OldMan Launcher

> 一款面向老年人的 Android 桌面启动器，大字体、大图标、极简交互，让不熟悉智能手机的老人也能轻松使用。

[![Kotlin](https://img.shields.io/badge/Kotlin-2.0-7F52FF?logo=kotlin)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/Jetpack_Compose-latest-4285F4?logo=android)](https://developer.android.com/compose)
[![API](https://img.shields.io/badge/API-26+-brightgreen)](https://android-arsenal.com/api?level=26)
[![License](https://img.shields.io/badge/License-MIT-blue)](LICENSE)

---

## 功能

| 功能 | 说明 |
|------|------|
| 📅 **阴阳历双显** | 主屏幕大字展示公历 + 农历日期，市面同类产品均未提供 |
| 👤 **联系人快捷图标** | 大号头像 + 大字姓名，点击即可通话 |
| 📞 **一键拨号** | 通过 `tel:` 协议直接拨打电话，无需进入通讯录 |
| 💬 **微信跳转** | 跳转微信指定联系人聊天界面 |
| 📱 **App 快捷方式** | 自定义常用 App 快捷图标 |
| 🔒 **纯本地运行** | 无网也能用，所有数据存储在本地 Room 数据库 |
| 🆓 **免费无广告** | 完全开源，无广告、无内购 |

### 亮点：阴阳历

市面上 **BIG Launcher、Senior Home、Senior Launcher、Simple Senior、Safe Home** 等竞品，无一提供农历显示。本项目是首个将农历集成到老年桌面的产品。

---

## 技术栈

| 维度 | 选型 |
|------|------|
| 语言 | Kotlin |
| UI | Jetpack Compose (Material 3) |
| 数据库 | Room |
| 架构 | MVVM (ViewModel + Repository) |
| 最低 SDK | Android 8.0 (API 26) |
| 目标 SDK | Android 14 (API 34) |

**零第三方依赖** — 仅在 AndroidX 生态内完成所有功能，包括农历算法也由内置 `LunarCalendarUtils` 实现。

---

## 项目结构

```
oldman/
├── app/
│   ├── build.gradle.kts
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── java/com/oldman/launcher/
│       │   ├── OldManApp.kt              # Application
│       │   ├── data/
│       │   │   ├── dao/                   # Room DAO
│       │   │   ├── entity/                # Room Entity
│       │   │   ├── database/              # Room Database
│       │   │   └── repository/            # 数据仓库
│       │   ├── ui/
│       │   │   ├── MainActivity.kt        # 唯一 Activity
│       │   │   ├── components/            # Composable 组件
│       │   │   │   ├── HomeScreen.kt      # 主屏幕
│       │   │   │   ├── DateTimeBar.kt     # 日期时间栏
│       │   │   │   ├── ContactGrid.kt     # 联系人网格
│       │   │   │   ├── ContactActionDialog.kt
│       │   │   │   └── AppShortcutGrid.kt # 快捷方式网格
│       │   │   └── theme/                 # 主题（Color/Type）
│       │   ├── utils/
│       │   │   ├── LunarCalendarUtils.kt  # 农历算法
│       │   │   └── AppLauncherUtils.kt    # App 启动工具
│       │   └── viewmodel/
│       │       └── MainViewModel.kt       # 主 ViewModel
│       └── res/
│           ├── drawable/                  # 图标
│           └── values/                    # 颜色/字符串/主题
├── build.gradle.kts
├── settings.gradle.kts
└── gradle.properties
```

---

## 开始使用

### 环境要求

- Android Studio Hedgehog (2023.1.1) 或更新
- JDK 17
- Gradle 8.x（项目自带 Wrapper）

### 构建 & 运行

```bash
# 克隆仓库
git clone git@github.com:lionel1949/Senior-Easy-Launcher-.git
cd Senior-Easy-Launcher-

# 调试构建
./gradlew assembleDebug

# 安装到设备
./gradlew installDebug
```

安装后，按 Home 键选择「老年桌面」并设为始终即可。

---

## 所需权限

| 权限 | 用途 | 是否必须 |
|------|------|:--------:|
| `CALL_PHONE` | 一键拨打电话 | 是 |
| `QUERY_ALL_PACKAGES` | 扫描已安装 App（快捷方式用） | 是 |
| `CATEGORY_HOME` | 注册为默认桌面 | 是 |
| `READ_CONTACTS` | 读取系统联系人 | 否（可手动录入） |

---

## 路线图

- [x] Launcher 框架 + 桌面布局
- [x] 阴阳历日期显示
- [x] 联系人图标 + 一键拨号
- [x] 微信跳转
- [x] App 快捷方式管理
- [ ] SOS 一键求助（GPS + 短信）
- [ ] 设置密码保护（防误改）
- [ ] 低电量醒目提醒
- [ ] 未读消息红点

---

## 许可

MIT License

---

> 为父母那一代人做的桌面。欢迎提 Issue 和 PR。
