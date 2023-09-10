package com.example.todoenhancedui.fragments.list.listeners

import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import kotlin.math.abs

class MyGestureListener(private val swipedListener: OnSwipedListener) : SimpleOnGestureListener() {
    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        val deltaX = e2.x.minus(e1?.x ?: 0F)
        val deltaY = e2.y.minus(e1?.y ?: 0F)

        if (abs(deltaX) > abs(deltaY)){
            if (deltaX>0){
                swipedListener.onSwipeRight()
            }else{
                swipedListener.onSwipeLeft()
            }
        }

            return true
    }
}