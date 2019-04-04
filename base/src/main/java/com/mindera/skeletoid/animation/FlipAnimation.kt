package com.mindera.skeletoid.animation

import android.graphics.Camera
import android.view.animation.Animation
import android.view.animation.Transformation

/**
 * Creates a flipping animation. Usually, you will need two of them to perform a complete flipping animation in a ViewAnimator.
 *
 * @param degrees the start and end degrees for a rotation along the x-axis or y-axis
 * @param center  the center point of rotation
 * @param scale   to get a cool 3D effect, the views should be scaled.
 * @param scaleType to get a cool 3D effect, you should scale down the exiting view and scale up the entering view.
 * @param depth affects the perspective distortion of the view as it rotates
 */
class FlipAnimation(
    private val degrees: Degrees, private val center: Point,
    private val scaleType: ScaleType = ScaleType.SCALE_NONE,
    private val scale: Float = DEFAULT_SCALE_VALUE,
    private val depth: Float = DEFAULT_CAMERA_DEPTH
) : Animation() {

    companion object {
        const val DEFAULT_SCALE_VALUE = 0.90f
        const val DEFAULT_CAMERA_DEPTH = -100f
    }

    private val camera: Camera = Camera()
    var axis: RotationAxis = RotationAxis.ROTATION_Y

    override fun initialize(width: Int, height: Int, parentWidth: Int, parentHeight: Int) {
        super.initialize(width, height, parentWidth, parentHeight)
        camera.setLocation(0.0f, 0.0f, depth)
    }

    override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
        val degrees = degrees.from + (degrees.to - degrees.from) * interpolatedTime
        val camera = camera

        val matrix = t.matrix
        camera.save()

        when (axis) {
            RotationAxis.ROTATION_X -> camera.rotateX(degrees)
            RotationAxis.ROTATION_Y -> camera.rotateY(degrees)
        }

        camera.getMatrix(matrix)
        camera.restore()

        val (x, y) = center
        matrix.preTranslate(-x, -y)
        matrix.postTranslate(x, y)
        matrix.preScale(scaleType.getScale(scale, interpolatedTime), scaleType.getScale(scale, interpolatedTime), x, y)
    }
}

data class Degrees(val from: Float, val to: Float) {

    companion object {
        public const val RECTANGLE: Float = 90.0f
    }
}

data class Point(val x: Float, val y: Float)

enum class RotationAxis {
    ROTATION_X,
    ROTATION_Y
}

enum class ScaleType {
    SCALE_UP,
    SCALE_DOWN,
    SCALE_NONE;

    /**
     * The zoom level given the current or desired maximum zoom level for the specified iteration
     *
     * @param max the maximum or current zoom level
     * @param iteration the iteration
     * @return the current zoom level
     */
    fun getScale(max: Float, iteration: Float): Float {
        return when (this) {
            SCALE_UP -> max + (1 - max) * iteration
            SCALE_DOWN -> 1 - (1 - max) * iteration
            SCALE_NONE -> 1f
        }
    }
}

enum class Direction {
    LEFT_TO_RIGHT,
    RIGHT_TO_LEFT,
    TOP_TO_BOTTOM,
    BOTTOM_TO_TOP;

    val frontViewDegrees: Degrees
        get() = when (this) {
            LEFT_TO_RIGHT, TOP_TO_BOTTOM -> Degrees(from = 0f, to = 90f)
            RIGHT_TO_LEFT, BOTTOM_TO_TOP -> Degrees(from = 0f, to = -90f)
        }

    val backViewDegrees: Degrees
        get() = when (this) {
            LEFT_TO_RIGHT, TOP_TO_BOTTOM -> Degrees(from = -90f, to = 0f)
            RIGHT_TO_LEFT, BOTTOM_TO_TOP -> Degrees(from = 90f, to = 0f)
        }
}
