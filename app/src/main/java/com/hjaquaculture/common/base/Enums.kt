package com.hjaquaculture.common.base

import androidx.room.TypeConverter
import com.hjaquaculture.common.base.InvoiceStatus.Companion.fromCode
import com.hjaquaculture.common.base.OrderStatus.Companion.fromCode
import com.hjaquaculture.common.base.PaymentMethods.Companion.fromCode
import com.hjaquaculture.feature.components.FilterableOption
import jakarta.inject.Inject
import jakarta.inject.Singleton
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// ==========================================
// SN 前缀
// ==========================================

/**
 * 订单编号前缀
 * @property value 前缀字符
 */
enum class SnPrefix(val value: String, val padding:Int) {
    /**
     * 销售订单前缀：SO
     */
    SALE_ORDER("SO",4),

    /**
     * 销售账单前缀：SI
     */
    SALE_INVOICE("SI",4),

    /**
     * 销售流水前缀：SR
     */
    SALE_PAYMENT("SR",4),

    /**
     * 采购订单前缀：PO
     */
    PURCHASE_ORDER("PO",4),

    /**
     * 采购账单前缀：PI
     */
    PURCHASE_INVOICE("PI",4),

    /**
     * 采购流水前缀：PR
     */
    PURCHASE_PAYMENT("PR",4)
}


/**
 * 订单管理
 */
@Singleton
class OrderManager @Inject constructor() {
    /**
     * 生成订单编号
     * @param prefix 订单前缀
     * @param id 订单ID
     */
    fun generateSn(prefix: SnPrefix, id: Long): String {
        val date = SimpleDateFormat("yyMMdd", Locale.getDefault()).format(Date())
        // 拼接格式：前缀 + 日期 + 3位补零ID
        return "${prefix.value}$date${id.toString().padStart(prefix.padding, '0')}"
    }
}

// ==========================================
// 订单、账单标记
// ==========================================

/**
 * 订单标识：销售订单、采购订单
 */
enum class OrderSymbol(val dbValue: String, val description: String): FilterableOption {
    SALE("SALE_ORDER", "销售订单"),
    PURCHASE("PUR_ORDER", "采购订单");

    override val label: String get() = description

    companion object {
        fun fromString(value: String?) = entries.find { it.dbValue == value } ?:
        throw IllegalArgumentException("未知的订单类别: $value")
    }
}

/**
 * 订单标识转换器
 * 将 OrderSymbol 转换为 String，并反向转换
 */
class OrderSymbolConverters {
    @TypeConverter
    fun fromOrderSymbol(symbol: OrderSymbol): String = symbol.dbValue

    @TypeConverter
    fun toOrderSymbol(dbValue: String?): OrderSymbol {
        return OrderSymbol.fromString(dbValue)
    }
}

/**
 * 账单标识：销售账单、采购账单
 */
enum class InvoiceSymbol(val dbValue: String, val description: String): FilterableOption {
    SALE("SALE_INVOICE", "销售账单"),
    PURCHASE("PUR_INVOICE", "采购账单");

    override val label: String get() = description
    companion object {
        fun fromString(value: String?) = entries.find { it.dbValue == value } ?:
        throw IllegalArgumentException("未知的账单类别: $value")
    }
}

/**
 * 账单标识转换器
 * 将 InvoiceSymbol 转换为 String，并反向转换
 */
class InvoiceSymbolConverters {
    @TypeConverter
    fun fromInvoiceSymbol(symbol: InvoiceSymbol): String = symbol.dbValue

    @TypeConverter
    fun toInvoiceSymbol(dbValue: String?): InvoiceSymbol {
        return InvoiceSymbol.fromString(dbValue)
    }
}

/**
 * 人员标识：操作员、客户、供应商
 */
enum class PeopleSymbol(val dbValue: String, val description: String): FilterableOption {
    OPERATOR("OPERATOR", "操作员"),
    CUSTOMER("CUSTOMER", "客户"),
    SUPPLIER("SUPPLIER", "供应商");

    override val label: String get() = description
    companion object {
        fun fromString(value: String?) = entries.find { it.dbValue == value } ?:
        throw IllegalArgumentException("未知的人员类别: $value")
    }
}

/**
 * 人员标识转换器
 * 将 PeopleSymbol 转换为 String，并反向转换
 */
class PeopleSymbolConverters {
    @TypeConverter
    fun fromPeopleSymbol(symbol: PeopleSymbol): String = symbol.dbValue

    @TypeConverter
    fun toPeopleSymbol(dbValue: String?): PeopleSymbol {
        return PeopleSymbol.fromString(dbValue)
    }
}

// ==========================================
// 库存
// ==========================================

/**
 * 库存变动类型 枚举：采购入库、销售出库、盘点调整、死亡损耗
 */
enum class StockChangeType(val value: Int, val label: String) {
    PURCHASE_IN(1, "采购入库"),
    SALE_OUT(2, "销售出库"),
    ADJUST(3, "盘点调整"),
    LOSS(4,"死亡损耗");

    companion object {
        fun fromInt(v: Int) = entries.find { it.value == v } ?: ADJUST
    }
}

/**
 * 库存变动类型转换器
 * 将 StockChangeType 转换为 Int，并反向转换
 */
class StockChangeTypeConverters{
    @TypeConverter
    fun fromStockChangeType(type: StockChangeType): Int = type.value

    @TypeConverter
    fun toStockChangeType(value: Int): StockChangeType =
        StockChangeType.entries.find { it.value == value }
            ?: throw IllegalArgumentException("未知的库存变动类型: $value")
}

/**
 * 库存计量维度 枚举：重量、数量
 */
enum class MeasureDimension(val description: String) {
    WEIGHT("重量"),
    QUANTITY("数量")
}
/**
 * 库存计量维度转换器
 * 将 MeasureDimension 转换为 String，并反向转换
 */
class StockUnitCategoryConverter {
    @TypeConverter
    fun fromStockUnitCategory(category: MeasureDimension): String = category.name

    @TypeConverter
    fun toStockUnitCategory(value: String): MeasureDimension = MeasureDimension.valueOf(value)
}


// ==========================================
// 账单状态
// ==========================================

/**
 * 账单状态
 * @property code 状态代码
 * @property description 状态描述
 * @property fromCode 根据代码获取状态
 */
enum class InvoiceStatus(val code: Int, val description: String): FilterableOption {
    UNPAID(1, "未付"),
    PARTIALLY_PAID(2, "部分支付"),
    PAID(3, "已付"),
    CANCELLED(-1, "作废");

    override val label: String get() = description

    companion object {
        fun fromCode(code: Int) = entries.find { it.code == code } ?:
        throw IllegalArgumentException("未知的账单状态: $code")
    }
}

/**
 * 账单状态转换器
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
 * 付款方式
 * @property code 方式代码
 * @property description 方式描述
 * @property fromCode 根据代码获取方式
 */
enum class PaymentMethods(val code: Int, val description: String): FilterableOption {
    WECHAT(1, "微信"),
    ALIPAY(2, "支付宝"),
    CASH(3, "现金"),
    OTHER(4, "其他");

    override val label: String get() = description
    companion object {
        fun fromCode(code: Int) = entries.find { it.code == code } ?: OTHER
    }
}

/**
 * 付款方式转换器
 * 将 PaymentMethods 转换为 Int，并反向转换
 * @property fromStatus 将 PaymentMethods 转换为 Int
 * @property toStatus 将 Int 转换为 PaymentMethods
 */
class PaymentMethodsConverter {

    @TypeConverter
    fun fromStatus(status: PaymentMethods): Int = status.code
    @TypeConverter
    fun toStatus(value: Int): PaymentMethods = PaymentMethods.fromCode(value)
}

// ==========================================
// 订单状态
// ==========================================

/**
 * 交付方式 枚举: 自提、货运
 */
enum class DeliveryMethod(val code: Int, val description: String): FilterableOption {
    PICKUP(1, "自提"),
    FREIGHT(2, "货运");

    override val label: String get() = description

    companion object {
        fun fromCode(code: Int) = entries.find { it.code == code } ?:
        throw IllegalArgumentException("未知的订单类型: $code")
    }
}

/**
 * 交付方式转换器
 * 将 DeliveryMethod 转换为 Int，并反向转换
 */
class DeliveryMethodConverter {
    @TypeConverter
    fun fromOrderType(type: DeliveryMethod): Int = type.code

    @TypeConverter
    fun toOrderType(value: Int): DeliveryMethod = DeliveryMethod.fromCode(value)

}

/**
 * 订单状态
 * @property code 状态代码
 * @property description 状态描述
 * @property fromCode 根据代码获取状态
 */
enum class OrderStatus(val code: Int, val description: String): FilterableOption {
    /**
     * 草稿
     */
    DRAFT(0,"草稿"),
    /**
     * 预定
     */
    RESERVATION(10,"预定"),
    /**
     * 确定
     */
    CONFIRMED(20,"确认"),
    /**
     * 完成
     */
    COMPLETED(30,"完成"),
    /**
     * 作废
     */
    CANCELLED(-1,"作废");

    override val label: String get() = description

    companion object {
        fun fromCode(code: Int) = entries.find { it.code == code } ?:
        throw IllegalArgumentException("未知的订单状态: $code")
    }
    /**
     * 辅助方法：判断订单是否处于活跃状态（未完成且未作废）
     */
    fun isActive(): Boolean = this.code in 0..20
}


/**
 * 订单状态转换器
 * 将 OrderStatus 转换为 Int，并反向转换
 * @property fromOrderStatus 将 OrderStatus 转换为 Int
 * @property toOrderStatus 将 Int 转换为 OrderStatus
 */
class OrderStatusConverter {
    @TypeConverter
    fun fromOrderStatus(status: OrderStatus): Int = status.code

    @TypeConverter
    fun toOrderStatus(value: Int): OrderStatus = OrderStatus.fromCode(value)
}

// ==========================================
// 工具类
// ==========================================

