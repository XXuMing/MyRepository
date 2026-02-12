package com.hjaquaculture.common.utils
import android.text.format.DateUtils
import androidx.room.TypeConverter
import com.hjaquaculture.common.utils.InvoiceStatus.Companion.fromCode
import com.hjaquaculture.common.utils.PurchaseOrderStatus.Companion.fromCode
import com.hjaquaculture.common.utils.PurchaseOrderType.Companion.fromCode
import com.hjaquaculture.common.utils.SaleOrderStatus.Companion.fromCode
import com.hjaquaculture.common.utils.SaleOrderType.Companion.fromCode
import jakarta.inject.Inject
import jakarta.inject.Singleton
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.concurrent.ConcurrentHashMap
import kotlin.time.ExperimentalTime

// ==========================================
// 1. 基础模块 (Base Module)
// ==========================================

/**
 * 订单编号前缀
 * @property value 前缀字符
 */
enum class OrderPrefix(val value: String,val padding:Int) {
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
    fun generateSn(prefix: OrderPrefix, id: Long): String {
        val date = SimpleDateFormat("yyMMdd", Locale.getDefault()).format(Date())
        // 拼接格式：前缀 + 日期 + 3位补零ID
        return "${prefix.value}$date${id.toString().padStart(prefix.padding, '0')}"
    }
}

/**
 * 账单状态
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
enum class PaymentMethods(val code: Int, val description: String) {
    WECHAT(1, "微信"),
    ALIPAY(2, "支付宝"),
    CASH(3, "现金"),
    OTHER(4, "其他");
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
// 2. 采购模块 (Purchase Module)
// ==========================================


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
// 3. 销售模块 (Sale Module)
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

/**
 *  时间处理工具
 */
object TimeUtils {
    // 使用线程安全的 Map 缓存格式化器
    private val formatterCache = ConcurrentHashMap<String, DateTimeFormatter>()

    // 默认时区
    private val defaultZone = ZoneId.systemDefault()

    /**
     * @param millis 时间戳
     * @param pattern 格式字符串，例如 "yyyy-MM-dd" 或 "HH:mm"
     */
    fun format(millis: Long, pattern: String): String {
        // getOrPut 的逻辑：如果缓存里有就直接拿，没有就创建并存进去
        val formatter = formatterCache.getOrPut(pattern) {
            DateTimeFormatter.ofPattern(pattern).withZone(defaultZone)
        }
        return formatter.format(Instant.ofEpochMilli(millis))
    }

    /**
     * 扩展 Long 类型，方便直接调用
     * 使用：timestamp.toFormattedString("yyyy-MM-dd")
     */
    fun Long.toFormattedString(pattern: String = "yyyy-MM-dd HH:mm"): String {
        return TimeUtils.format(this, pattern)
    }
}

