package com.proyecto.biblioapp.utils

import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener


class SwipeDetector : OnTouchListener {
    enum class Action {
        LR,  // Left to right
        RL,  // Right to left
        TB,  // Top to bottom
        BT,  // Bottom to top
        None // Action not found
    }

    // for vertical
    // swipe
    private var downX = 0f
    private var downY = 0f
    private var upX = 0f
    private var upY // Coordinates
            = 0f
    var action = Action.None // Last action
        private set

    fun swipeDetected(): Boolean {
        return action != Action.None
    }

    /**
     * Swipe detection
     */
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                downX = event.x
                downY = event.y
                action = Action.None
                return false // allow other events like Click to be processed
            }
            MotionEvent.ACTION_MOVE -> {
                upX = event.x
                upY = event.y
                val deltaX = downX - upX
                val deltaY = downY - upY

                // horizontal swipe detection
                if (Math.abs(deltaX) > HORIZONTAL_MIN_DISTANCE) {
                    // left or right
                    if (deltaX < 0) {
                        action = Action.LR
                        return true
                    }
                    if (deltaX > 0) {
                        action = Action.RL
                        return true
                    }
                } else  // vertical swipe detection
                    if (Math.abs(deltaY) > VERTICAL_MIN_DISTANCE) {
                        // top or down
                        if (deltaY < 0) {
                            action = Action.TB
                            return true
                        }
                        if (deltaY > 0) {
                            action = Action.BT
                            return true
                        }
                    }
                return true
            }
        }
        return false
    }

    companion object {
        private const val HORIZONTAL_MIN_DISTANCE = 30 // The minimum

        // distance for
        // horizontal swipe
        private const val VERTICAL_MIN_DISTANCE = 80 // The minimum distance
    }
}