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
 *
 * 数据流:
 * Room (Flow) → Repository (Flow) → ViewModel (StateFlow) → UI (collectAsState)
 *
 * 设计决策:
 * - 使用 AndroidViewModel 获取 Application Context 用于构建 Room 数据库
 * - 使用 stateIn() 将 Flow 转为 StateFlow，确保 UI 始终获取最新数据
 * - WhileSubscribed(5000) 在 UI 不可见后保持 5 秒活跃，避免配置变更时重新查询
 * - 不在此 ViewModel 中管理日期时间——DateTimeBar 自行通过 LaunchedEffect 管理
 *
 * @param application Application Context，用于初始化数据库
 */
class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MainRepository

    /**
     * App 快捷方式列表 — 响应式 StateFlow
     *
     * 自动从 Room 数据库订阅更新:
     * - 数据库首次创建时包含 6 个默认 App
     * - 后续可通过管理功能增删改
     */
    val appShortcuts: StateFlow<List<AppShortcutEntity>>

    /**
     * 联系人列表 — 响应式 StateFlow
     *
     * 自动从 Room 数据库订阅更新:
     * - 数据库首次创建时包含 3 个示例联系人（爸爸/妈妈/子女）
     * - 后续可通过管理功能增删改
     */
    val contacts: StateFlow<List<ContactEntity>>

    init {
        // 初始化数据库和 Repository
        val database = OldManDatabase.getInstance(application)
        repository = MainRepository(database)

        // ── 将 Room Flow 转为 StateFlow ──
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

    /**
     * 更新联系人信息
     *
     * 用于后续版本中实现联系人编辑功能（修改电话号码、微信号等）
     *
     * @param contact 更新后的联系人实体
     */
    fun updateContact(contact: ContactEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateContact(contact)
        }
    }
}
