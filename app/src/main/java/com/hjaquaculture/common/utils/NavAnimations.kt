package com.hjaquaculture.common.utils

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically


//定义动画时长常量，方便统一修改
private const val ANIM_DURATION = 500

object NavAnimations {

    // ----------------------------------------------------------------
    // 1. 水平滑动系列 (类似 iOS/Android 原生切换：A -> B)
    // ----------------------------------------------------------------

    // 【进入】从右侧滑入 (新页面进入)
    fun slideInFromRight(): EnterTransition {
        return slideInHorizontally(
            initialOffsetX = { fullWidth -> fullWidth }, // 从屏幕右侧边缘开始
            animationSpec = tween(ANIM_DURATION)
        ) + fadeIn(animationSpec = tween(ANIM_DURATION))
    }

    // 【退出】向左侧滑出 (旧页面离开)
    fun slideOutToLeft(): ExitTransition {
        return slideOutHorizontally(
            targetOffsetX = { fullWidth -> -fullWidth }, // 滑向屏幕左侧边缘
            animationSpec = tween(ANIM_DURATION)
        ) + fadeOut(animationSpec = tween(ANIM_DURATION))
    }

    // 【返回进入】从左侧滑入 (点击返回键，旧页面回来)
    fun slideInFromLeft(): EnterTransition {
        return slideInHorizontally(
            initialOffsetX = { fullWidth -> -fullWidth }, // 从屏幕左侧边缘开始
            animationSpec = tween(ANIM_DURATION)
        ) + fadeIn(animationSpec = tween(ANIM_DURATION))
    }

    // 【返回退出】向右侧滑出 (点击返回键，当前页面离开)
    fun slideOutToRight(): ExitTransition {
        return slideOutHorizontally(
            targetOffsetX = { fullWidth -> fullWidth }, // 滑向屏幕右侧边缘
            animationSpec = tween(ANIM_DURATION)
        ) + fadeOut(animationSpec = tween(ANIM_DURATION))
    }

    // ----------------------------------------------------------------
    // 2. 垂直滑动系列 (类似 BottomSheet：底部弹出)
    // ----------------------------------------------------------------

    // 【进入】从底部升起
    fun slideInFromBottom(): EnterTransition {
        return slideInVertically(
            initialOffsetY = { fullHeight -> fullHeight }, // 从屏幕底部开始
            animationSpec = tween(ANIM_DURATION)
        ) + fadeIn(tween(ANIM_DURATION))
    }

    // 【退出】向底部降落
    fun slideOutToBottom(): ExitTransition {
        return slideOutVertically(
            targetOffsetY = { fullHeight -> fullHeight }, // 降落到屏幕底部
            animationSpec = tween(ANIM_DURATION)
        ) + fadeOut(tween(ANIM_DURATION))
    }

    // 【退出】向上飞走 (比较少用，但有时做引导页需要)
    fun slideOutToTop(): ExitTransition {
        return slideOutVertically(
            targetOffsetY = { fullHeight -> -fullHeight },
            animationSpec = tween(ANIM_DURATION)
        ) + fadeOut(tween(ANIM_DURATION))
    }

    // ----------------------------------------------------------------
    // 3. 淡入淡出系列 (最通用，适合 Tab 切换)
    // ----------------------------------------------------------------

    fun fadeEnter(): EnterTransition = fadeIn(tween(ANIM_DURATION))
    fun fadeExit(): ExitTransition = fadeOut(tween(ANIM_DURATION))

    // ----------------------------------------------------------------
    // 4. 缩放系列 (适合弹窗 Dialog 风格)
    // ----------------------------------------------------------------

    fun scaleInExpand(): EnterTransition {
        return scaleIn(
            initialScale = 0.8f, // 从 80% 大小开始放大
            animationSpec = tween(ANIM_DURATION)
        ) + fadeIn(tween(ANIM_DURATION))
    }

    fun scaleOutShrink(): ExitTransition {
        return scaleOut(
            targetScale = 0.8f, // 缩小到 80% 后消失
            animationSpec = tween(ANIM_DURATION)
        ) + fadeOut(tween(ANIM_DURATION))
    }
}