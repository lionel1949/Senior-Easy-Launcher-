package com.oldman.launcher.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * App 快捷方式实体 — 存储在 Room 的 app_shortcuts 表中
 *
 * 每个实体代表桌面上一个可点击的 App 快捷图标。
 * 图标通过 iconResId 引用本地 VectorDrawable 资源，不依赖网络下载。
 *
 * @param id          自增主键
 * @param name        显示名称（如"电话"、"微信"）
 * @param packageName Android 包名，用于 Intent 启动 App
 * @param iconResId   drawable 资源 ID（如 R.drawable.ic_phone）
 * @param sortOrder   排序权重，数值越小越靠前
 */
@Entity(tableName = "app_shortcuts")
data class AppShortcutEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "package_name")
    val packageName: String,

    @ColumnInfo(name = "icon_res_id")
    val iconResId: Int,

    @ColumnInfo(name = "sort_order")
    val sortOrder: Int = 0
)
