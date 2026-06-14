# ============================================================
# 老年桌面 (OldMan Launcher) — ProGuard 混淆规则
# ============================================================

# Room 实体保持不混淆（依赖反射和注解）
-keep class com.oldman.launcher.data.entity.** { *; }

# 保持 Kotlin 数据类
-keepattributes *Annotation*

# 保持枚举
-keepclassmembers enum * { *; }
