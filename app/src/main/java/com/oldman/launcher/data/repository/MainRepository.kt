package com.oldman.launcher.data.repository

import com.oldman.launcher.data.database.OldManDatabase
import com.oldman.launcher.data.entity.AppShortcutEntity
import com.oldman.launcher.data.entity.ContactEntity
import kotlinx.coroutines.flow.Flow

/**
 * 数据仓库 — MainRepository
 *
 * 统一封装所有数据访问操作，作为 ViewModel 与 Room DAO 之间的中间层。
 *
 * 当前 MVP 阶段仅做透明透传；后续可在此层添加:
 * - 数据校验逻辑
 * - 缓存策略
 * - 数据迁移/转换
 */
class MainRepository(private val database: OldManDatabase) {

    // ── App 快捷方式 ──

    /** 获取所有 App 快捷方式（响应式 Flow） */
    fun getAppShortcuts(): Flow<List<AppShortcutEntity>> {
        return database.appShortcutDao().getAllShortcuts()
    }

    /** 插入一个快捷方式 */
    suspend fun insertShortcut(shortcut: AppShortcutEntity) {
        database.appShortcutDao().insertShortcut(shortcut)
    }

    /** 删除一个快捷方式 */
    suspend fun deleteShortcut(shortcut: AppShortcutEntity) {
        database.appShortcutDao().deleteShortcut(shortcut)
    }

    /** 获取快捷方式总数 */
    suspend fun getAppShortcutCount(): Int {
        return database.appShortcutDao().getCount()
    }

    // ── 联系人 ──

    /** 获取所有联系人（响应式 Flow） */
    fun getContacts(): Flow<List<ContactEntity>> {
        return database.contactDao().getAllContacts()
    }

    /** 插入联系人 */
    suspend fun insertContact(contact: ContactEntity) {
        database.contactDao().insertContact(contact)
    }

    /** 更新联系人信息 */
    suspend fun updateContact(contact: ContactEntity) {
        database.contactDao().updateContact(contact)
    }

    /** 删除联系人 */
    suspend fun deleteContact(contact: ContactEntity) {
        database.contactDao().deleteContact(contact)
    }
}
