package com.mindera.skeletoid.animation

import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.Interpolator
import android.widget.ViewAnimator

object FlipAnimationHelper {
    const val DEFAULT_FLIP_DURATION = 300L

    /**
     * Creates the pair of animations to be set into the ViewAnimator as the out and in animations
     * which will result into a flipping 3D effect.
     *
     * @param center the center point of the view to animate
     * @param direction the direction of the flip
     * @param duration the duration of the animation in milliseconds
     * @param interpolator the interpolator to be used
     */
    private fun flip(center: Point, direction: Direction, duration: Long, interpolator: Interpolator = AccelerateInterpolator()): Pair<Animation, Animation> {
        val axis = when (direction) {
            Direction.TOP_TO_BOTTOM, Direction.BOTTOM_TO_TOP -> RotationAxis.ROTATION_X
            Direction.LEFT_TO_RIGHT, Direction.RIGHT_TO_LEFT -> RotationAxis.ROTATION_Y
        }

        val outFlip = FlipAnimation(direction.frontViewDegrees, center, ScaleType.SCALE_DOWN).also {
            it.duration = duration / 2
            it.fillAfter = true
            it.interpolator = interpolator
            it.axis = axis
        }

        val inFlip = FlipAnimation(direction.backViewDegrees, center, ScaleType.SCALE_UP).also {
            it.duration = duration
            it.fillAfter = true
            it.interpolator = interpolator
            it.startOffset = duration / 2
            it.axis = axis
        }

        val outAnimation = AnimationSet(true)
        outAnimation.addAnimation(outFlip)

        val inAnimation = AnimationSet(true)
        inAnimation.addAnimation(inFlip)

        return Pair(outAnimation, inAnimation)
    }

    /**
     * Flipping transition between the views of a ViewAnimator.
     *
     * @param viewAnimator the ViewAnimator (ViewFlipper, ViewSwitcher)
     * @param direction the direction of the flip
     * @param duration the duration of the animation in milliseconds
     */
    fun flip(viewAnimator: ViewAnimator, direction: Direction, duration: Long = DEFAULT_FLIP_DURATION): Pair<Animation, Animation> {

        val center = Point(viewAnimator.currentView.width / 2.0f, viewAnimator.currentView.height / 2.0f)
        val (outAnimation, inAnimation) = flip(center, direction, duration)

        viewAnimator.outAnimation = outAnimation
        viewAnimator.inAnimation = inAnimation

        when (direction) {
            Direction.LEFT_TO_RIGHT, Direction.TOP_TO_BOTTOM -> viewAnimator.showPrevious()
            Direction.RIGHT_TO_LEFT, Direction.BOTTOM_TO_TOP -> viewAnimator.showNext()
        }

        return Pair(outAnimation, inAnimation)
    }
}
