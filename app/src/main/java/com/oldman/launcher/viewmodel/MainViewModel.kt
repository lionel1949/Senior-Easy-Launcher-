package com.oldman.launcher.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.oldman.launcher.data.database.OldManDatabase
import com.oldman.launcher.data.entity.AppShortcutEntity
import com.oldman.launcher.data.entity.ContactEntity
import com.oldman.launcher.data.repository.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * 主 ViewModel
 *
 * 管理桌面所有数据的生命周期，作为 UI 层与数据层之间的桥梁。
 * 提供联系人与快捷方式的完整 CRUD 操作。
 *
 * 数据流:
 * Room (Flow) → Repository (Flow) → ViewModel (StateFlow) → UI (collectAsState)
 *
 * @param application Application Context，用于初始化数据库
 */
class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MainRepository

    /** App 快捷方式列表 — 响应式 StateFlow */
    val appShortcuts: StateFlow<List<AppShortcutEntity>>

    /** 联系人列表 — 响应式 StateFlow */
    val contacts: StateFlow<List<ContactEntity>>

    init {
        val database = OldManDatabase.getInstance(application)
        repository = MainRepository(database)

        appShortcuts = repository.getAppShortcuts()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

        contacts = repository.getContacts()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
    }

    // ── 联系人操作 ──

    /** 插入新联系人 */
    fun insertContact(contact: ContactEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertContact(contact)
        }
    }

    /** 更新联系人信息（修改姓名、电话、微信号等） */
    fun updateContact(contact: ContactEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateContact(contact)
        }
    }

    /** 删除联系人 */
    fun deleteContact(contact: ContactEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteContact(contact)
        }
    }

    // ── 快捷方式操作 ──

    /** 添加快捷方式 */
    fun insertShortcut(shortcut: AppShortcutEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertShortcut(shortcut)
        }
    }

    /** 删除快捷方式 */
    fun deleteShortcut(shortcut: AppShortcutEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteShortcut(shortcut)
        }
    }
}
