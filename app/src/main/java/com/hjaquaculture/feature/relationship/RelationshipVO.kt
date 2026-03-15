package com.hjaquaculture.feature.relationship

import com.hjaquaculture.common.utils.TimeUtils.toFormattedString
import com.hjaquaculture.data.local.entity.CombinedPeopleView

data class PeopleVO(
    val symbol: String,
    val syntheticId: String,
    val id: Long,
    val account: String,
    val name: String,
    val nikeName: String?,
    val phone: String?,
    val role: String?,
    val createdAt: String,
    val address: String?,

    val isDetailsExpanded: Boolean = false,
)

fun CombinedPeopleView.toVO(): PeopleVO{
    return PeopleVO(
        symbol = symbol.description,
        syntheticId = "${symbol}_$id",
        id = id,
        account = account?:"无账号",
        name = name,
        nikeName = nikeName?:"无昵称",
        phone = phone?:"无号码",
        role = role?:"无角色",
        createdAt = createdAt.toFormattedString("yyyy-MM-dd"),
        address = address,

        isDetailsExpanded = false
    )
}