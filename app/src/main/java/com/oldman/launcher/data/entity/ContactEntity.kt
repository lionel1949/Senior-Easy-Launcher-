package com.oldman.launcher.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 联系人实体 — 存储在 Room 的 contacts 表中
 *
 * 每个实体代表桌面上一个联系人快捷卡片。
 * 点击后可选择「打电话」或「打微信」两个操作。
 *
 * @param id          自增主键
 * @param name        联系人姓名（如"爸爸"、"妈妈"）
 * @param wechatId    微信号/微信别名，用于 weixin://dl/chat 跳转
 * @param avatarPath  头像本地路径，为空时显示默认头像
 * @param phoneNumber 电话号码，用于 tel: 协议一键拨号
 */
@Entity(tableName = "contacts")
data class ContactEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "wechat_id")
    val wechatId: String = "",

    @ColumnInfo(name = "avatar_path")
    val avatarPath: String = "",

    @ColumnInfo(name = "phone_number")
    val phoneNumber: String = ""
)
