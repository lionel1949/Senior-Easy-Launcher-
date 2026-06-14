package com.oldman.launcher.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.oldman.launcher.data.entity.ContactEntity
import kotlinx.coroutines.flow.Flow

/**
 * 联系人 DAO — 数据访问接口
 *
 * 提供对 contacts 表的全部 CRUD 操作。
 */
@Dao
interface ContactDao {

    /**
     * 获取所有联系人，按 ID 排序
     */
    @Query("SELECT * FROM contacts ORDER BY id ASC")
    fun getAllContacts(): Flow<List<ContactEntity>>

    /**
     * 根据 ID 查询单个联系人
     */
    @Query("SELECT * FROM contacts WHERE id = :id")
    suspend fun getContactById(id: Long): ContactEntity?

    /**
     * 插入联系人，主键冲突时替换
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact(contact: ContactEntity)

    /**
     * 更新联系人信息（修改微信号、电话号码等）
     */
    @Update
    suspend fun updateContact(contact: ContactEntity)

    /**
     * 删除联系人
     */
    @Delete
    suspend fun deleteContact(contact: ContactEntity)

    /**
     * 获取联系人总数
     */
    @Query("SELECT COUNT(*) FROM contacts")
    suspend fun getCount(): Int
}
