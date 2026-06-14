package com.oldman.launcher.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.oldman.launcher.data.entity.AppShortcutEntity
import kotlinx.coroutines.flow.Flow

/**
 * App 快捷方式 DAO — 数据访问接口
 *
 * 提供对 app_shortcuts 表的全部 CRUD 操作。
 * 查询结果以 Flow 返回，可被 ViewModel 订阅并自动更新 UI。
 */
@Dao
interface AppShortcutDao {

    /**
     * 获取所有 App 快捷方式，按 sort_order 排序
     * 返回 Flow 实现数据的响应式订阅
     */
    @Query("SELECT * FROM app_shortcuts ORDER BY sort_order ASC, id ASC")
    fun getAllShortcuts(): Flow<List<AppShortcutEntity>>

    /**
     * 插入一个快捷方式，主键冲突时替换
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShortcut(shortcut: AppShortcutEntity)

    /**
     * 删除一个快捷方式
     */
    @Delete
    suspend fun deleteShortcut(shortcut: AppShortcutEntity)

    /**
     * 更新排序权重（为后续自定义排序预留）
     */
    @Query("UPDATE app_shortcuts SET sort_order = :order WHERE id = :id")
    suspend fun updateSortOrder(id: Long, order: Int)

    /**
     * 获取快捷方式总数
     */
    @Query("SELECT COUNT(*) FROM app_shortcuts")
    suspend fun getCount(): Int
}
