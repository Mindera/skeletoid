package com.mindera.skeletoid.gesture

import android.annotation.SuppressLint
import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View

open class OnSwipeListener(context: Context?) : View.OnTouchListener {

    private val gestureDetector: GestureDetector = GestureDetector(context, GestureListener())

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        gestureDetector.onTouchEvent(event)
        return true
    }

    protected open fun onSwipeLeft() {
        //unused
    }
    protected open fun onSwipeRight() {
        //unused
    }
    protected open fun onSwipeUp() {
        //unused
    }
    protected open fun onSwipeDown() {
        //unused
    }

    private enum class Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }

    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onFling(start: MotionEvent, end: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            /**
             * Given two events start = (x1, y1) and end = (x2, y2)
             * Considering start event (o) the initial point, the end event will be located at one of the following quadrants:
             * - N: means a swipe Up - the angle between two points would be in [45, 135]
             * - W: means a swipe Left - the angle between two points would be in [135, 225]
             * - E: means a swipe Right - the angle between two points would be in [0, 45] and [315, 360]
             * - S: means a swipe Down - the angle between two points would be in [225, 315]
             *
             *       \   N   /
             *        \     /
             *      W  - o -  E
             *        /     \
             *       /   S   \
             *
             * So if (x2,y2) falls in region:
             * - N => it's a swipe UP
             * - E => it's a swipe RIGHT
             * - W => it's a swipe DOWN
             * - S => it's a swipe LEFT
             */
            return onSwipe(getDirection(start.x, start.y, end.x, end.y))
        }

        /**
         * Tells you on which direction the user did the swipe.
         */
        private fun onSwipe(direction: Direction): Boolean {
            when (direction) {
                Direction.LEFT -> onSwipeLeft()
                Direction.RIGHT -> onSwipeRight()
                Direction.UP -> onSwipeUp()
                Direction.DOWN -> onSwipeDown()
            }
            return false
        }

        /**
         * Given two points in the plane p1=(x1, x2) and p2=(y1, y1), this method
         * returns the direction that an arrow pointing from p1 to p2 would have.
         *
         * @param x1 the x position of the first point
         * @param y1 the y position of the first point
         * @param x2 the x position of the second point
         * @param y2 the y position of the second point
         * @return the direction
         */
        private fun getDirection(x1: Float, y1: Float, x2: Float, y2: Float) = directionFromAngle(getAngle(x1, y1, x2, y2))

        /**
         * Returns a direction given an angle.
         * Directions are defined as follows:
         *
         * Right: [0,45] or [315, 360[
         * Up: [45, 135[
         * Left: [135, 225[
         * Down: [225, 315[
         *
         * @param angle an angle in [0, 360[
         * @return the direction of an angle
         */
        private fun directionFromAngle(angle: Float): Direction = when {
            angle in 0f..45f || angle >= 315f && angle < 360f -> Direction.RIGHT
            angle >= 45f && angle < 135f -> Direction.UP
            angle >= 135f && angle < 225f -> Direction.LEFT
            angle >= 225f && angle < 315f -> Direction.DOWN
            else -> Direction.RIGHT
        }

        /**
         * Finds the angle between two points in the plane (x1,y1) and (x2, y2).
         * The angle would be in [0, 360].
         *
         * @param x1 the x position of the first point
         * @param y1 the y position of the first point
         * @param x2 the x position of the second point
         * @param y2 the y position of the second point
         * @return the angle between two points
         */
        private fun getAngle(x1: Float, y1: Float, x2: Float, y2: Float): Float {
            var angle = Math.toDegrees(Math.atan2((y2 - y1).toDouble(), (x2 - x1).toDouble()))
            if (angle < 0) {
                angle += 360
            }
            return angle.toFloat()
        }
    }
}
