package com.hjaquaculture.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.hjaquaculture.common.base.StocktakingStatus

/**
 * 盘点
 * @param id 主键
 * @param operatorId 发起人ID
 * @param status 状态
 * @param remark 备注
 * @param createdAt 创建时间
 * @param completedAt 完成时间
 */
@Entity(
    tableName = "stocktaking",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["operator_id"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [
        Index("operator_id"),
        Index("status"),
        Index("created_at")
    ]
)
data class StocktakingEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "operator_id")
    val operatorId: Long,           // 发起人

    @ColumnInfo(name = "status")
    val status: StocktakingStatus = StocktakingStatus.IN_PROGRESS,

    @ColumnInfo(name = "remark")
    val remark: String? = null,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "completed_at")
    val completedAt: Long? = null   // 完成时间，null表示未完成
)
