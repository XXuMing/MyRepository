package com.hjaquaculture.common.utils

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically

// 建议时长 400-500ms 左右，视差感最自然
private const val ANIM_DURATION = 500
private const val ALPHA = 0f

object NavAnimationsParallax {

    private val easing = FastOutSlowInEasing

    // ----------------------------------------------------------------
    // 1. 视差水平滑动 (A 进 B, A 往左推 30%)
    // ----------------------------------------------------------------

    // 【进入】新页面从右侧全速滑入
    fun slideInFromRight(): EnterTransition {
        return slideInHorizontally(
            initialOffsetX = { it },
            animationSpec = tween(ANIM_DURATION, easing = easing)
        ) + fadeIn(animationSpec = tween(ANIM_DURATION))
    }

    // 【退出】旧页面向左“视差”滑出 (只滑出 30% 宽度，并变暗)
    fun slideOutToLeftParallax(): ExitTransition {
        return slideOutHorizontally(
            targetOffsetX = { fullWidth -> -fullWidth / 3 }, // 视差位移
            animationSpec = tween(ANIM_DURATION, easing = easing)
        ) + fadeOut(
            animationSpec = tween(ANIM_DURATION),
            targetAlpha = ALPHA // 这里的 Alpha 值让旧页面变暗，显得在底层
        )
    }

    // 【返回进入】旧页面从左侧“视差”滑回 (从 -30% 位置滑回 0)
    fun slideInFromLeftParallax(): EnterTransition {
        return slideInHorizontally(
            initialOffsetX = { fullWidth -> -fullWidth / 3 },
            animationSpec = tween(ANIM_DURATION, easing = easing)
        ) + fadeIn(
            animationSpec = tween(ANIM_DURATION),
            initialAlpha = ALPHA
        )
    }

    // 【返回退出】当前页面向右全速滑出 (露出下方的视差层)
    fun slideOutToRight(): ExitTransition {
        return slideOutHorizontally(
            targetOffsetX = { it },
            animationSpec = tween(ANIM_DURATION, easing = easing)
        ) + fadeOut(animationSpec = tween(ANIM_DURATION))
    }

    // ----------------------------------------------------------------
    // 2. 垂直滑动 (保持原样，通常垂直滑动不加视差)
    // ----------------------------------------------------------------

    fun slideInFromBottom() = slideInVertically(
        initialOffsetY = { it },
        animationSpec = tween(ANIM_DURATION, easing = easing)
    ) + fadeIn(tween(ANIM_DURATION))

    fun slideOutToBottom() = slideOutVertically(
        targetOffsetY = { it },
        animationSpec = tween(ANIM_DURATION, easing = easing)
    ) + fadeOut(tween(ANIM_DURATION))
}