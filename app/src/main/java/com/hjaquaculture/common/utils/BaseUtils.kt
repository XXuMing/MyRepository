package com.hjaquaculture.common.utils

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.concurrent.ConcurrentHashMap

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
        return format(this, pattern)
    }
}

