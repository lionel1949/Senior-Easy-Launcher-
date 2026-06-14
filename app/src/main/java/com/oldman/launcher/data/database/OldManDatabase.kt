package com.oldman.launcher.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
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
 * - onCreate() 回调中注入默认数据（6个常用App + 3个示例联系人）
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
         * 确保线程安全，全 App 共享同一实例
         */
        fun getInstance(context: Context): OldManDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    OldManDatabase::class.java,
                    "oldman_database"
                )
                    .fallbackToDestructiveMigration()  // MVP阶段：数据量小，重建即可
                    .addCallback(SeedDataCallback())     // 首次创建时填充默认数据
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }

    /**
     * 种子数据回调 — 数据库首次创建时执行
     *
     * 插入6个默认 App 快捷方式 + 3个示例联系人。
     * 插入前检查是否已有数据（避免重复填充）。
     */
    private class SeedDataCallback : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            // 数据库刚创建，需要在事务中插入默认数据
            // 这里通过 INSTANCE 获取 DAO，但 onCreate 时 INSTANCE 尚未赋值
            // 因此使用 db 引用推迟到 INSTANCE 可用后执行
            CoroutineScope(Dispatchers.IO).launch {
                // 等待 INSTANCE 赋值完成（build() 返回后）
                val instance = INSTANCE ?: return@launch

                // ── 插入默认 App 快捷方式 ──
                if (instance.appShortcutDao().getCount() == 0) {
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
                    defaultApps.forEach { instance.appShortcutDao().insertShortcut(it) }
                }

                // ── 插入默认联系人 ──
                if (instance.contactDao().getCount() == 0) {
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
                    defaultContacts.forEach { instance.contactDao().insertContact(it) }
                }
            }
        }
    }
}
