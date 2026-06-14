package com.oldman.launcher.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.oldman.launcher.data.dao.AppShortcutDao
import com.oldman.launcher.data.dao.ContactDao
import com.oldman.launcher.data.entity.AppShortcutEntity
import com.oldman.launcher.data.entity.ContactEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Room 数据库 — OldManDatabase
 *
 * 包含两张表:
 * - app_shortcuts: App 快捷方式
 * - contacts: 联系人信息
 *
 * 设计决策:
 * - fallbackToDestructiveMigration() — MVP 阶段数据量小(6+3条)，重建成本极低
 * - 双检锁单例 — 确保全 App 只有一个数据库实例
 * - 种子数据在 getInstance() 返回前通过协程异步填充，避免 onCreate 回调中的竞态条件
 */
@Database(
    entities = [
        AppShortcutEntity::class,
        ContactEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class OldManDatabase : RoomDatabase() {

    abstract fun appShortcutDao(): AppShortcutDao
    abstract fun contactDao(): ContactDao

    companion object {
        @Volatile
        private var INSTANCE: OldManDatabase? = null

        /**
         * 双检锁单例获取数据库实例
         *
         * 数据库创建后异步填充种子数据（6个默认App + 3个示例联系人）。
         * 使用同步锁内构建 + 返回后填充的方式，彻底避免 INSTANCE 竞态。
         */
        fun getInstance(context: Context): OldManDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    OldManDatabase::class.java,
                    "oldman_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { db ->
                        INSTANCE = db
                        // 在 INSTANCE 赋值后异步填充种子数据
                        seedIfEmpty(db)
                    }
            }
        }

        /**
         * 首次创建数据库时填充默认数据
         *
         * 在 build() 返回后调用，此时 INSTANCE 已赋值，不存在竞态条件。
         * 填充前检查表是否为空，避免重复填充。
         */
        private fun seedIfEmpty(db: OldManDatabase) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    // ── 插入默认 App 快捷方式 ──
                    if (db.appShortcutDao().getCount() == 0) {
                        val defaultApps = listOf(
                            AppShortcutEntity(
                                name = "电话",
                                packageName = "com.android.dialer",
                                iconResId = com.oldman.launcher.R.drawable.ic_phone,
                                sortOrder = 0
                            ),
                            AppShortcutEntity(
                                name = "短信",
                                packageName = "com.google.android.apps.messaging",
                                iconResId = com.oldman.launcher.R.drawable.ic_sms,
                                sortOrder = 1
                            ),
                            AppShortcutEntity(
                                name = "微信",
                                packageName = "com.tencent.mm",
                                iconResId = com.oldman.launcher.R.drawable.ic_wechat,
                                sortOrder = 2
                            ),
                            AppShortcutEntity(
                                name = "抖音",
                                packageName = "com.ss.android.ugc.aweme",
                                iconResId = com.oldman.launcher.R.drawable.ic_tiktok,
                                sortOrder = 3
                            ),
                            AppShortcutEntity(
                                name = "相机",
                                packageName = "com.android.camera",
                                iconResId = com.oldman.launcher.R.drawable.ic_camera,
                                sortOrder = 4
                            ),
                            AppShortcutEntity(
                                name = "设置",
                                packageName = "com.android.settings",
                                iconResId = com.oldman.launcher.R.drawable.ic_settings,
                                sortOrder = 5
                            )
                        )
                        defaultApps.forEach { db.appShortcutDao().insertShortcut(it) }
                    }

                    // ── 插入默认联系人 ──
                    if (db.contactDao().getCount() == 0) {
                        val defaultContacts = listOf(
                            ContactEntity(
                                name = "爸爸",
                                wechatId = "",
                                phoneNumber = "",
                                avatarPath = ""
                            ),
                            ContactEntity(
                                name = "妈妈",
                                wechatId = "",
                                phoneNumber = "",
                                avatarPath = ""
                            ),
                            ContactEntity(
                                name = "子女",
                                wechatId = "",
                                phoneNumber = "",
                                avatarPath = ""
                            )
                        )
                        defaultContacts.forEach { db.contactDao().insertContact(it) }
                    }
                } catch (e: Exception) {
                    // 种子数据填充失败不应崩溃——数据库已就绪，只是缺默认数据
                    // 用户仍可通过管理功能手动添加
                }
            }
        }
    }
}
