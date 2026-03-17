package com.hjaquaculture.data.local.mapper

import com.hjaquaculture.data.local.entity.CombinedPeopleView
import com.hjaquaculture.data.local.entity.CustomerEntity
import com.hjaquaculture.data.local.entity.SupplierEntity
import com.hjaquaculture.data.local.entity.UserEntity
import com.hjaquaculture.domain.model.CombinedPeople
import com.hjaquaculture.domain.model.Customer
import com.hjaquaculture.domain.model.Supplier
import com.hjaquaculture.domain.model.User
import com.hjaquaculture.domain.model.UserRole

/**
 * UserMapper: 负责 Entity 与 Domain 之间的互转
 * 注释：
 * 1. toDomain: 将数据库实体转为业务模型，处理空安全和默认值。
 * 2. toEntity: 将业务模型转回数据库实体。
 */

// --- Entity -> Domain ---
fun UserEntity.toDomain(): User {
    return User(
        id = this.id,
        account = this.account,
        // 处理数据库中的可选字段，提供 Domain 层的空安全保证
        name = this.name ?: "Unknown",
        phone = this.phone ?: "",
        role = UserRole.fromString(this.role),
        address = this.address ?: "No Address",
        createdAt = this.createdAt
    )
}

// --- Domain -> Entity ---
// 注意：转换回 Entity 时，通常需要保留 passwordHash。
// 如果 Domain 对象中没有该字段，则需要从 Repository 层面结合旧数据处理。
fun User.toEntity(passwordHash: String): UserEntity {
    return UserEntity(
        id = this.id,
        account = this.account,
        name = this.name,
        phone = this.phone,
        role = this.role.value,
        address = this.address,
        createdAt = this.createdAt,
        passwordHash = passwordHash
    )
}

/**
 * CustomerMapper: 实现 Entity 与 Domain 的双向转换
 */

// --- Entity -> Domain ---
fun CustomerEntity.toDomain(): Customer {
    return Customer(
        id = this.id,
        name = this.name,
        // 处理 Entity 中的拼写错误并提供空安全
        nickName = this.nickName ?: "",
        phone = this.phone ?: "",
        address = this.address ?: "",
        createdAt = this.createdAt
    )
}

// --- Domain -> Entity ---
// 注意：passwordHash 通常由 Auth 模块或 Repository 在更新/创建时传入
fun Customer.toEntity(passwordHash: String): CustomerEntity {
    return CustomerEntity(
        id = this.id,
        name = this.name,
        nickName = this.nickName,
        phone = this.phone,
        passwordHash = passwordHash,
        address = this.address,
        createdAt = this.createdAt
    )
}

/**
 * SupplierMapper: Entity 与 Domain 互转
 */

// --- Entity -> Domain ---
fun SupplierEntity.toDomain(): Supplier {
    return Supplier(
        id = this.id,
        name = this.name,
        // 将 Entity 中的可空值转为 Domain 的非空字符串，简化 UI 判断
        nickName = this.nickName ?: "",
        phone = this.phone ?: "",
        address = this.address ?: "",
        createdAt = this.createdAt
    )
}

// --- Domain -> Entity ---
fun Supplier.toEntity(): SupplierEntity {
    return SupplierEntity(
        id = this.id,
        name = this.name,
        nickName = this.nickName,
        phone = this.phone,
        address = this.address,
        createdAt = this.createdAt
    )
}

/**
 * CombinedPeopleMapper
 */

fun CombinedPeopleView.toDomain(): CombinedPeople {
    return CombinedPeople(
        symbol = this.symbol,
        id = this.id,
        name = this.name,
        phone = this.phone ?: "",
        account = this.account ?: "",
        nickName = this.nickName ?: "",
        role = this.role ?: "",
        address = this.address ?: "",
        createdAt = this.createdAt
    )
}