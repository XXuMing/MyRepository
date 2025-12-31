package com.hjaquaculture.data.local.model.entity.status
import androidx.room.TypeConverter
import com.hjaquaculture.data.local.model.entity.status.InvoiceStatus.Companion.fromCode
import com.hjaquaculture.data.local.model.entity.status.PurchaseOrderStatus.Companion.fromCode
import com.hjaquaculture.data.local.model.entity.status.PurchaseOrderType.Companion.fromCode
import com.hjaquaculture.data.local.model.entity.status.SaleOrderStatus.Companion.fromCode
import com.hjaquaculture.data.local.model.entity.status.SaleOrderType.Companion.fromCode

// ==========================================
// 1. 采购模块 (Purchase Module)
// ==========================================

/**
 * 采购单状态
 * @property code 状态代码
 * @property description 状态描述
 * @property fromCode 根据代码获取状态
 */
enum class InvoiceStatus(val code: Int, val description: String) {
    UNKNOWN(0, "未知状态"),
    PAID(1, "已付"),
    UNPAID(2, "未付"),
    PARTIALLY_PAID(3, "部分支付"),
    VOID(-1, "已作废");

    companion object {
        fun fromCode(code: Int) = entries.find { it.code == code } ?: UNKNOWN
    }
}

/**
 * 采购单状态转换器
 * 将 BillStatus 转换为 Int，并反向转换
 * @property fromStatus 将 BillStatus 转换为 Int
 * @property toStatus 将 Int 转换为 BillStatus
 */
class InvoiceStatusConverter {
    @TypeConverter
    fun fromStatus(status: InvoiceStatus): Int = status.code

    @TypeConverter
    fun toStatus(value: Int): InvoiceStatus = InvoiceStatus.fromCode(value)
}

/**
 * 采购订单类型
 * @property code 类型代码
 * @property description 类型描述
 * @property fromCode 根据代码获取类型
 */
enum class PurchaseOrderType(val code: Int, val description: String) {
    UNKNOWN(-99, "未知类型"),
    RECEIVE_SHIPMENT(1, "收寄货"),
    SELF_PURCHASE(2, "自采购");

    companion object {
        fun fromCode(code: Int) = entries.find { it.code == code } ?: UNKNOWN
    }
}

/**
 * 采购订单状态
 * @property code 状态代码
 * @property description 状态描述
 * @property fromCode 根据代码获取状态
 */
enum class PurchaseOrderStatus(val code: Int, val description: String) {
    UNKNOWN(-99, "未知状态"),
    AUDITED(1, "已审核"),
    UNAUDITED(2, "未审核");

    companion object {
        fun fromCode(code: Int) = entries.find { it.code == code } ?: UNKNOWN
    }
}

/**
 * 采购订单状态转换器
 * 将 PurchaseOrderStatus 转换为 Int，并反向转换
 * @property fromStatus 将 PurchaseOrderStatus 转换为 Int
 * @property toStatus 将 Int 转换为 PurchaseOrderStatus
 * @property fromType 将 PurchaseOrderType 转换为 Int
 * @property toType 将 Int 转换为 PurchaseOrderType
 */
class PurchaseOrderConverter {
    @TypeConverter
    fun fromStatus(status: PurchaseOrderStatus): Int = status.code
    @TypeConverter
    fun toStatus(value: Int): PurchaseOrderStatus = PurchaseOrderStatus.fromCode(value)

    @TypeConverter
    fun fromType(type: PurchaseOrderType): Int = type.code
    @TypeConverter
    fun toType(value: Int): PurchaseOrderType = PurchaseOrderType.fromCode(value)
}


// ==========================================
// 2. 销售模块 (Sale Module)
// ==========================================

/**
 * 销售订单类型
 * @property code 类型代码
 * @property description 类型描述
 * @property fromCode 根据代码获取类型
 */
enum class SaleOrderType(val code: Int, val description: String) {
    UNKNOWN(-99, "未知类型"),
    PICKUP(0, "自提"),
    SHIPPING(1, "寄货");

    companion object {
        fun fromCode(code: Int) = entries.find { it.code == code } ?: UNKNOWN
    }
}

/**
 * 销售订单状态
 * @property code 状态代码
 * @property description 状态描述
 * @property fromCode 根据代码获取状态
 */
enum class SaleOrderStatus(val code: Int, val description: String) {
    //自提=10，寄货=20，取消=0
    UNKNOWN(-99, "未知状态"),
    CANCELLED(-1, "已取消"),
    COMPLETED(100, "已完成"),
    PICKUP_UNPAID(10, "自提未付"),
    PICKUP_PAID(11, "自提已付"),
    SHIPPING_UNPAID_PENDING(20, "寄货未付待处理"),
    SHIPPING_UNPAID_PROCESSED(21, "寄货未付已处理"),
    SHIPPING_PAID(22, "寄货已付");
    companion object {
        fun fromCode(code: Int) = entries.find { it.code == code } ?: UNKNOWN
    }
}

class SaleOrderConverter {
    @TypeConverter
    fun fromOrderType(type: SaleOrderType): Int = type.code

    @TypeConverter
    fun toOrderType(value: Int): SaleOrderType = SaleOrderType.fromCode(value)

    @TypeConverter
    fun fromOrderStatus(status: SaleOrderStatus): Int = status.code

    @TypeConverter
    fun toOrderStatus(value: Int): SaleOrderStatus = SaleOrderStatus.fromCode(value)
}