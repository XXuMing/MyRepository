package com.hjaquaculture.common.base

import androidx.room.TypeConverter
import com.hjaquaculture.common.base.InvoiceStatus.Companion.fromCode
import com.hjaquaculture.common.base.OrderStatus.Companion.fromCode
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
 * 交易方标识：销售订单、采购订单
 */
enum class TradeSymbol(val description: String): FilterableOption {
    SALE("销售订单"),
    PURCHASE("采购订单");

    override val label: String get() = description
}

/**
 * 交易方标识转换器
 * 将 TradeSymbol 转换为 String，并反向转换
 */
class TradeSymbolConverters {
    @TypeConverter
    fun fromTradeSymbol(symbol: TradeSymbol): String = symbol.name

    @TypeConverter
    fun toTradeSymbol(value: String): TradeSymbol = TradeSymbol.valueOf(value)
}


/**
 * 当事人标识：操作员、客户、供应商
 */
enum class PartySymbol(val description: String): FilterableOption {
    OPERATOR("操作员"),
    CUSTOMER("客户"),
    SUPPLIER("供应商");

    override val label: String get() = description
}

/**
 * 当事人标识转换器
 * 将 PartySymbol 转换为 String，并反向转换
 */
class PartySymbolConverters {
    @TypeConverter
    fun fromPartySymbol(symbol: PartySymbol): String = symbol.name

    @TypeConverter
    fun toPartySymbol(value: String): PartySymbol = PartySymbol.valueOf(value)
}

// ==========================================
// 库存
// ==========================================
/**
 * 盘点状态 枚举：进行中、已完成、已取消
 */
enum class StocktakingStatus(val description: String) {
    IN_PROGRESS("进行中"),
    COMPLETED("已完成"),
    CANCELLED("已取消")
}

/**
 * 盘点状态转换器
 * 将 StocktakingStatus 转换为 String，并反向转换
 */
class StocktakingStatusConverter {
    @TypeConverter
    fun fromStatus(status: StocktakingStatus): String = status.name
    @TypeConverter
    fun toStatus(value: String): StocktakingStatus = StocktakingStatus.valueOf(value)
}

/**
 * 库存变动类型 枚举：采购入库、销售出库、盘点调整、死亡损耗
 */
enum class StockChangeType(val description: String) {
    PURCHASE_IN("采购入库"),
    SALE_OUT("销售出库"),
    ADJUST("盘点调整"),
    LOSS("死亡损耗"),
    FORCE_ZERO("强制平库");
}

/**
 * 库存变动类型转换器
 * 将 StockChangeType 转换为 Int，并反向转换
 */
class StockChangeTypeConverters{
    @TypeConverter
    fun fromStockChangeType(type: StockChangeType): String = type.name

    @TypeConverter
    fun toStockChangeType(value: String): StockChangeType = StockChangeType.valueOf(value)
}

/**
 * 库存状态 枚举：未入库、负库存、库存预警、正常
 */
enum class StockStatus(val description: String) {
    NO_RECORD("未入库"),
    NEGATIVE("负库存"),
    LOW("库存预警"),
    NORMAL("正常")
}

/**
 * 单位 枚举：斤、件、箱、条、袋、筐、个、桶
 */
enum class StockUnit(val description: String) {
    JIN("斤"),
    PIECE("件"),
    BOX("箱"),
    STRIP("条"),
    BAG("袋"),
    BASKET("筐"),
    UNIT("个"),
    BARREL("桶");
}

/**
 * 库存单位转换器
 * 将 StockUnit 转换为 String，并反向转换
 */
class StockUnitConverters{
    @TypeConverter
    fun fromStockUnit(unit: StockUnit): String = unit.name
    @TypeConverter
    fun toStockUnit(value: String): StockUnit = StockUnit.valueOf(value)
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
    UNPAID(0, "未付"),
    PARTIALLY_PAID(10, "部分支付"),
    PAID(20, "已付"),
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
enum class PaymentMethods(val description: String): FilterableOption {
    WECHAT("微信"),
    ALIPAY("支付宝"),
    CASH("现金"),
    OTHER("其他");
    override val label: String get() = description
}

class PaymentMethodsConverter {
    @TypeConverter
    fun fromPaymentMethods(method: PaymentMethods): String = method.name
    // 存入："WECHAT"、"CASH"

    @TypeConverter
    fun toPaymentMethods(value: String): PaymentMethods = PaymentMethods.valueOf(value)
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

