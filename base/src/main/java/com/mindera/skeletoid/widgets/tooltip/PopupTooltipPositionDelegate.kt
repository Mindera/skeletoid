@file:Suppress("MemberVisibilityCanBePrivate")

package com.mindera.skeletoid.widgets.tooltip

import android.content.Context
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.mindera.skeletoid.widgets.tooltip.PopupTooltip.Companion.TooltipGravity
import com.mindera.skeletoid.widgets.tooltip.PopupTooltip.Companion.getValueInPixels

class PopupTooltipPositionDelegate {

    /**
     * Calculates the position of the arrow in relation to the tooltip. It's X and Y coordinates are associated with the
     * "parent" view, which is our tooltip, that's why the calculations are a bit different here.
     */
    fun calculateArrowXPosition(
        tooltipArrow: ImageView,
        gravity: TooltipGravity,
        context: Context,
        arrowWidth: Int,
        anchorViewAbsoluteX: Int,
        anchorViewWidth: Int
    ): Float {
        val tooltipArrowWidth = getValueInPixels(arrowWidth, context)

        val arrowScreenPositions = IntArray(2)
        tooltipArrow.getLocationOnScreen(arrowScreenPositions)

        return when (gravity) {
            TooltipGravity.BOTTOM_RIGHT -> anchorViewAbsoluteX + anchorViewWidth / 2f - arrowScreenPositions[0].toFloat() - tooltipArrowWidth
            else -> anchorViewAbsoluteX + anchorViewWidth / 2f - arrowScreenPositions[0].toFloat() - tooltipArrowWidth / 2f
        }
    }

    /**
     * Calculates the X and Y position completely disregarding screen size and boundaries.
     */
    fun calculateTooltipInitialPosition(
        screenPos: IntArray,
        tooltipWidth: Int,
        estimatedTooltipHeight: Float,
        anchorView: View,
        arrow: PopupTooltip.ArrowData?,
        tooltipGravity: TooltipGravity,
        horizontalMargin: Int,
        verticalMargin: Int,
        context: Context
    ): PopupTooltip.Coordinates {

        val tooltipArrowHeight = arrow?.let { getValueInPixels(it.arrowHeight, context) } ?: 0f

        val x = when (tooltipGravity) {
            TooltipGravity.TOP -> {
                screenPos[0] - tooltipWidth / 2f - getValueInPixels(horizontalMargin, context) + anchorView.width / 2f
            }
            TooltipGravity.BOTTOM -> {
                screenPos[0] - tooltipWidth / 2f - getValueInPixels(horizontalMargin, context) + anchorView.width / 2f
            }
            TooltipGravity.BOTTOM_RIGHT -> {
                screenPos[0] - tooltipWidth + anchorView.width - getValueInPixels(horizontalMargin, context)
            }
        }

        val y = when (tooltipGravity) {
            TooltipGravity.TOP -> {
                screenPos[1] - estimatedTooltipHeight - getValueInPixels(verticalMargin, context) - tooltipArrowHeight
            }
            TooltipGravity.BOTTOM -> {
                screenPos[1] + anchorView.height / 2f + getValueInPixels(verticalMargin, context) + tooltipArrowHeight
            }
            TooltipGravity.BOTTOM_RIGHT -> {
                screenPos[1] + anchorView.height / 2f + getValueInPixels(verticalMargin, context) + tooltipArrowHeight
            }
        }

        return PopupTooltip.Coordinates(x, y)
    }

    /**
     * Shows the tooltip at a specific place with the pre-defined Gravity in the companion object.
     */
    fun calculateTooltipFinalPosition(
        x: Float,
        y: Float,
        statusBarHeight: Int,
        anchorXScreenPos: Int,
        tooltipWidth: Int,
        anchorView: View,
        tooltipGravity: TooltipGravity,
        horizontalMargin: Int,
        horizontalMarginClipped: Int,
        verticalMargin: Int,
        verticalMarginClipped: Int,
        context: Context
    ): PopupTooltip.Coordinates {
        val displayMetrics = DisplayMetrics()
        val windowManager = ContextCompat.getSystemService(context, WindowManager::class.java) as WindowManager
        windowManager.defaultDisplay.getMetrics(displayMetrics)

        val screenWidth = displayMetrics.widthPixels

        var auxX = x
        var auxY = y

        //If the tooltip exceeds maxX clamp it to the screen with the horizontal margin. If our view is RIGHT aligned
        //we don't need to check this, as this will always be at most at the edge of the screen, since we don't
        //display views outside of the screen.
        if (tooltipGravity != TooltipGravity.BOTTOM_RIGHT) {
            val tooltipMaxWidth = anchorXScreenPos + anchorView.width / 2f + tooltipWidth / 2f

            if (tooltipMaxWidth >= screenWidth) {
                auxX = screenWidth - tooltipWidth - getValueInPixels(horizontalMargin, context) -
                        getValueInPixels(horizontalMarginClipped, context)
            }
        }

        //If the tooltip is drawn out of the screen from the left side clamp it to the screen with the horizontal margin.
        if (anchorXScreenPos - anchorView.width / 2f - tooltipWidth / 2f <= 0f) {
            auxX = getValueInPixels(horizontalMargin, context) + getValueInPixels(horizontalMarginClipped, context)
        }

        //If the tooltip exceeds the top bound, clamp it as well.
        if (y - statusBarHeight <= 0f) {
            auxY = getValueInPixels(verticalMargin, context) + statusBarHeight +
                    getValueInPixels(verticalMarginClipped, context)
        }

        return PopupTooltip.Coordinates(auxX, auxY)
    }

}