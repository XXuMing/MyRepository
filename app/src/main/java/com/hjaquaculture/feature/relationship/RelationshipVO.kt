package com.hjaquaculture.feature.relationship

import com.hjaquaculture.common.utils.TimeUtils.toFormattedString
import com.hjaquaculture.domain.model.CombinedPeople

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

fun CombinedPeople.toVO(): PeopleVO{
    return PeopleVO(
        symbol = symbol.description,
        syntheticId = "${symbol}_$id",
        id = id,
        account = account,
        name = name,
        nikeName = nickName,
        phone = phone,
        role = role,
        createdAt = createdAt.toFormattedString("yyyy-MM-dd"),
        address = address,

        isDetailsExpanded = false
    )
}