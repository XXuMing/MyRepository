package com.hjaquaculture.domain.model

import com.hjaquaculture.common.base.MeasureDimension
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * 单位领域模型
 */
data class MeasureUnit(
    val id: Int,
    val name: String,
    val dimension: MeasureDimension,
    val conversionRate: Double,
    val precision: Int,
    val isBase: Boolean,
    val sort: Int
) {
    // --- 业务规则 (Business Rules) ---

    /**
     * 将当前单位的数值转换为基准单位的数值
     * 公式：基准值 = 当前值 * 换算率
     */
    fun toBaseValue(value: Double): Double {
        return (value * conversionRate).roundToPrecision(precision)
    }

    /**
     * 从基准单位数值转换为当前单位数值
     * 公式：当前值 = 基准值 / 换算率
     */
    fun fromBaseValue(baseValue: Double): Double {
        if (conversionRate == 0.0) return 0.0
        return (baseValue / conversionRate).roundToPrecision(precision)
    }

    /**
     * 格式化输出（带精度控制）
     */
    fun format(value: Double): String {
        return "%.${precision}f".format(value)
    }

    // 内部辅助：处理浮点数精度限制
    private fun Double.roundToPrecision(p: Int): Double {
        return BigDecimal(this.toString())
            .setScale(p, RoundingMode.HALF_UP)
            .toDouble()
    }
}