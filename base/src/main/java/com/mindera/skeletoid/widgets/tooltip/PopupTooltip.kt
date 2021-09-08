package com.mindera.skeletoid.widgets.tooltip

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Handler
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.LayoutInflater
import android.view.TouchDelegate
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.annotation.StyleRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat

class PopupTooltip(
    private var anchorView: View,
    private var message: CharSequence,
    @LayoutRes private var layout: Int,
    @IdRes private var textViewId: Int,
    @IdRes private var tooltipBackgroundId: Int
) : PopupWindow() {

    /**
     * The possible orientation for the tooltip in relation to the [anchorView].
     * Currently it can only be displayed on top or below a view.
     */
    enum class TooltipGravity { TOP, BOTTOM, BOTTOM_RIGHT }

    private var elevation: Int = DEFAULT_ELEVATION
    private var popupWidth: Int = TOOLTIP_WIDTH
    private var popupHeight: Int = TOOLTIP_HEIGHT
    private var tooltipGravity: TooltipGravity = TooltipGravity.TOP

    private var indefinite: Boolean = false
    private var showDuration: Long = 3500L
    private var verticalMargin: Int = 0
    private var horizontalMargin: Int = 0
    private var horizontalMarginClipped: Int = 0
    private var verticalMarginClipped: Int = 0
    private var insideTouchDismissible: Boolean = true
    private var outsideTouchDismissible: Boolean = true
    private var modal: Boolean = true
    @StyleRes private var animation: Int? = null

    private var arrowWidth: Int = ARROW_WIDTH
    private var arrowHeight: Int = ARROW_HEIGHT
    private var arrow: Boolean = false
    @DrawableRes private var arrowDrawable: Int? = null
    @ColorRes private var arrowStrokeColor: Int = android.R.color.white
    @IdRes private var arrowBackgroundId: Int? = null
    @IdRes private var arrowStrokeId: Int? = null

    companion object {
        /**
         * Saves a reference to our currently displayed tooltip. This is here so we can control when
         * we dismiss/show it.
         */
        private var tooltip: PopupWindow? = null

        /**
         * The estimated height of our popup in dips (density-independent pixels).
         * This assumes the popup will have two lines of text with the text sizes
         * specified in [layout].
         */
        const val TOOLTIP_HEIGHT = 46

        /**
         * The default width value of the tooltip if none is specified, in dips (density-independent pixels).
         */
        const val TOOLTIP_WIDTH = 250

        /**
         * The default width value of the tooltip arrow if none is specified, in dips (density-independent pixels).
         */
        const val ARROW_WIDTH = 10

        /**
         * The default height value of the tooltip arrow if none is specified, in dips (density-independent pixels).
         */
        const val ARROW_HEIGHT = 10

        /**
         * The default elevation of the tooltip, in dips (density-independent pixels).
         */
        const val DEFAULT_ELEVATION = 5

        /**
         * At the moment this is hardcoded here. In the future the idea is for this
         * to be changeable.
         */
        const val GRAVITY = Gravity.NO_GRAVITY

        /**
         * Sets the tooltip as null after showDuration amount of time. This is only used
         * when we set the tooltip as definite.
         */
        private val handler = Handler()

        /**
         * Clears the current reference to the tooltip and dismisses if it is present.
         */
        private fun PopupWindow.dismissIfShowing() {
            if (isShowing) dismiss()
        }

        fun clearTooltip() {
            tooltip?.dismissIfShowing()
            tooltip = null
        }

        /**
         * Given a value in dips, gets the amount of pixels that equates to in the given context.
         */
        fun getValueInPixels(valueInDips: Int, context: Context): Float {
            return (valueInDips * context.resources.displayMetrics.density)
        }
    }

    /**
     * The vertical offset from the anchorview to the tooltip.
     *
     * @param margin - the margin in dips.
     */
    fun verticalMargin(margin: Int) = apply { this.verticalMargin = margin }

    /**
     * The horizontal offset from the edges of the screen.
     *
     * @param margin - the margin in dips.
     */
    fun horizontalMargin(margin: Int) = apply { this.horizontalMargin = margin }

    /**
     * The vertical offset from the anchor view to the tooltip when the bounds of the tooltip exceed
     * the bounds of the screen.
     *
     * @param margin - the margin in dips.
     */
    fun verticalMarginClipped(margin: Int) = apply { this.verticalMarginClipped = margin }

    /**
     * The horizontal offset from the edges of the screen to the tooltip when the bounds
     * of the tooltip exceed the bounds of the screen.
     *
     * @param margin - the margin in dips.
     */
    fun horizontalMarginClipped(margin: Int) = apply { this.horizontalMarginClipped = margin }

    /**
     * If false the tooltip will dismiss after a certain amount of time. Else, it will remain there
     * until clicked again.
     */
    fun indefinite(indefinite: Boolean) = apply { this.indefinite = indefinite }

    /**
     * Flag to draw the arrow at the bottom of the tooltip, at the top center of the anchor view.
     * In the future, the idea is to make this customizable e.g, draw the tooltip at the right, bottom, etc.
     */
    fun arrow(showArrow: Boolean) = apply { this.arrow = showArrow }

    /**
     * Defines the Id of the arrow background view of the [layout]
     */
    fun arrowBackgroundId(@IdRes arrowBackgroundId: Int) = apply { this.arrowBackgroundId = arrowBackgroundId }

    /**
     * Defines the Id of the arrow stroke view of the [layout]
     */
    fun arrowStrokeId(@IdRes arrowStrokeId: Int) = apply { this.arrowStrokeId = arrowStrokeId }

    /**
     * Changes the arrow drawable to a specified one.
     */
    fun arrowDrawable(@DrawableRes drawable: Int) = apply { this.arrowDrawable = drawable }

    /**
     * Changes the arrow drawable stroke color.
     */
    fun arrowStrokeColor(@ColorRes strokeColor: Int) = apply { this.arrowStrokeColor = strokeColor }

    /**
     * If the tooltip is definite, specify the time the tooltip should remain visible.
     */
    fun timeToDismiss(timeInMilliseconds: Long) = apply { this.showDuration = timeInMilliseconds }

    /**
     * Allows the usage of custom animations to show/hide the tooltip.
     */
    fun animationStyle(showAnimation: Int) = apply { this.animation = showAnimation }

    /**
     * Enables or disables dismiss on outside touch.
     */
    fun outsideTouchDismissable(isDismissibleOnOutsideTouch: Boolean) = apply { this.outsideTouchDismissible = isDismissibleOnOutsideTouch }

    /**
     * Enables or disables dismiss on inside touch.
     */
    fun insideTouchDismissible(isOutsideTouchDismissible: Boolean) = apply { this.insideTouchDismissible = isOutsideTouchDismissible }

    /**
     * Defines the popup window as focusable. Set this to false if you want the view
     * to be click through. Although this messes up with the inside/outside touch flags, so use
     * this carefully.
     */
    fun modal(isModal: Boolean) = apply { this.modal = isModal }

    /**
     * The tooltip width to be displayed in dips (Density Independent Pixels).
     * 250dp seems to be a good roundabout value.
     *
     * @param width - the tooltip width in dips.
     */
    fun layoutWidth(width: Int) = apply { this.popupWidth = width }

    /**
     * The tooltip height to be displayed in dips (Density Independent Pixels).
     * 46dp seems to be a good roundabout value for two lines.
     *
     * @param height - the tooltip height in dips.
     */
    fun layoutHeight(height: Int) = apply { this.popupHeight = height }

    /**
     * Defines a custom layout for this tooltip.
     */
    fun layout(@LayoutRes layout: Int) = apply { this.layout = layout }

    /**
     * Defines a custom elevation for all the tooltip components, in dips (Density Independent Pixels).
     *
     * @param elevation - the amount of elevation in dips.
     */
    fun elevation(elevation: Int) = apply { this.elevation = elevation }

    /**
     * Creates a touch delegate that increases the bounds of the view's touch area by a given amount, in
     * dips (Density Independent Pixels).
     *
     * @param increaseAmount - the amount to increase in dips.
     */
    fun touchDelegateTouchAreaIncreaseAmount(increaseAmount: Int) = apply { setupTouchDelegate(anchorView, increaseAmount, anchorView.context) }

    /**
     * Defines the arrow height in dips (Density Independent Pixels).
     *
     * @param height - the arrow height in dips.
     */
    fun arrowHeight(height: Int) = apply { this.arrowHeight = height }

    /**
     * Defines the arrow width in dips (Density Independent Pixels).
     *
     * @param width - the arrow width in dips.
     */
    fun arrowWidth(width: Int) = apply { this.arrowHeight = width }

    /**
     * Defines a new orientation in relation to the [anchorView]
     *
     * @param tooltipGravity - one of the possible [TooltipGravity] parameters
     */
    fun orientation(tooltipGravity: TooltipGravity) = apply { this.tooltipGravity = tooltipGravity }

    /**
     * Displays the tooltip
     */
    fun show() = setupTooltip(anchorView, message)

    /**
     * Internal helper class to show the tooltip.
     */
    @SuppressLint("InflateParams")
    private fun setupTooltip(view: View, text: CharSequence?) {
        handler.removeCallbacksAndMessages(null)

        if (tooltip != null) {
            clearTooltip()
            return
        }

        if (TextUtils.isEmpty(text)) {
            return
        }

        val screenPos = IntArray(2)
        val displayFrame = Rect()
        val context = view.context

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val tooltipWidth = getValueInPixels(popupWidth, context).toInt()

        view.getLocationOnScreen(screenPos)
        view.getWindowVisibleDisplayFrame(displayFrame)

        val estimatedTooltipHeight = getValueInPixels(popupHeight, context)
        val tooltipArrowHeight = getValueInPixels(arrowHeight, context).toInt()
        val tooltipArrowWidth = getValueInPixels(arrowWidth, context)

        //Custom tooltip layout
        val layout = inflater.inflate(layout, null)

        //Custom text
        val textView = layout.findViewById<TextView>(textViewId)
        textView.text = text

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textView.elevation = getValueInPixels(elevation, context)
        }

        //Background
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val backgroundView = layout.findViewById<View>(tooltipBackgroundId)
            backgroundView.elevation = getValueInPixels(elevation, context)
        }

        setupTooltipBehaviour(context, layout, tooltipWidth)

        //Handle arrow
        if (arrow) {
            setupArrow(layout, context, tooltipArrowWidth, screenPos[0], tooltipWidth)
        }

        //Handle timed dismiss
        if (!indefinite) {
            handler.postDelayed({ clearTooltip() }, showDuration)
        }

        val (x, y) = calculateXYPosition(screenPos, tooltipWidth, estimatedTooltipHeight, tooltipArrowHeight, context)

        showTooltipAtLocation(x, y, displayFrame.top, screenPos[0], tooltipWidth, context)
    }

    /**
     * If the arrow flag is set to true, this method enables the arrow and places it at the
     * required place, at the top center of the anchor view.
     */
    private fun setupArrow(layout: View, context: Context, tooltipArrowWidth: Float, anchorViewAbsoluteX: Int, tooltipWidth: Int) {
        val arrow = layout.findViewById<ImageView>(arrowBackgroundId ?: View.NO_ID)
        val stroke = layout.findViewById<ImageView>(arrowStrokeId ?: View.NO_ID)
        val elevation = getValueInPixels(elevation, context)

        if (arrow != null && arrowDrawable != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                arrow.setImageDrawable(AppCompatResources.getDrawable(context, arrowDrawable!!))
                arrow.elevation = elevation
                arrow.visibility = View.VISIBLE

                if (stroke != null) {
                    stroke.setImageDrawable(AppCompatResources.getDrawable(context, arrowDrawable!!))
                    stroke.elevation = elevation

                    stroke.setColorFilter(arrowStrokeColor)
                    stroke.visibility = View.VISIBLE
                }
            }

            arrow.post {
                val arrowScreenPositions = IntArray(2)
                arrow.getLocationOnScreen(arrowScreenPositions)

                val arrowAbsoluteX: Float =  if (tooltipGravity == TooltipGravity.BOTTOM_RIGHT) {
                    anchorViewAbsoluteX + anchorView.width / 2f - arrowScreenPositions[0].toFloat() - tooltipArrowWidth
                } else {
                    anchorViewAbsoluteX + anchorView.width / 2f - arrowScreenPositions[0].toFloat() - tooltipArrowWidth / 2f
                }


                arrow.x = arrowAbsoluteX

                if (stroke != null) {
                    stroke.x = arrowAbsoluteX
                }
            }
        }
    }

    /**
     * Increases the clickable area of the view by using a touch delegate.
     */
    private fun setupTouchDelegate(anchorView: View, touchDelegateTouchAreaIncreaseAmount: Int, context: Context) {
        val parent = anchorView.parent as View
        val increaseAmount = getValueInPixels(touchDelegateTouchAreaIncreaseAmount, context).toInt()

        // Post in the parent's message queue to make sure the parent
        // lays out its children before we call getHitRect()
        parent.post {
            val touchableArea = Rect()
            anchorView.getHitRect(touchableArea)
            touchableArea.top -= increaseAmount
            touchableArea.bottom += increaseAmount
            touchableArea.left -= increaseAmount
            touchableArea.right += increaseAmount
            parent.touchDelegate = TouchDelegate(touchableArea, anchorView)
        }
    }

    /**
     * Sets up the tooltip behaviour.
     */
    private fun setupTooltipBehaviour(context: Context, layout: View, tooltipWidth: Int) {
        tooltip = PopupWindow(context)

        tooltip?.apply {
            height = ViewGroup.LayoutParams.WRAP_CONTENT
            width = tooltipWidth

            //Set the custom content view
            contentView = layout

            //Set the background as transparent.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                setBackgroundDrawable(ColorDrawable(context.resources.getColor(android.R.color.transparent, null)))
            } else {
                setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(context, android.R.color.transparent)))
            }

            //Is outside touch dismissible?
            isOutsideTouchable = outsideTouchDismissible

            //Is inside touch dismissible?
            isTouchable = insideTouchDismissible

            //Is the view focusable? Check modal() setter method for a description.
            isFocusable = modal

            //Is inside touch dismissible
            if (insideTouchDismissible) {
                contentView?.setOnClickListener { dismiss() }
            }

            //Clear the tooltip reference on dismiss.
            setOnDismissListener { tooltip = null }

            //Show animation
            animation?.let { animationStyle = it }

            //Allow to draw outside of the screen.
            isClippingEnabled = true
        }
    }

    /**
     * Shows the tooltip at a specific place with the pre-defined Gravity in the companion object.
     */
    private fun showTooltipAtLocation(x: Float, y: Float, statusBarHeight: Int, anchorXScreenPos: Int, tooltipWidth: Int, context: Context) {
        val displayMetrics = DisplayMetrics()
        val windowManager = ContextCompat.getSystemService(context, WindowManager::class.java) as WindowManager
        windowManager.defaultDisplay.getMetrics(displayMetrics)

        val screenWidth = displayMetrics.widthPixels

        var auxX = x.toInt()
        var auxY = y.toInt()

        //If the tooltip exceeds maxX clamp it to the screen with the horizontal margin.
        if (tooltipGravity != TooltipGravity.BOTTOM_RIGHT) {
            val tooltipMaxWidth = anchorXScreenPos + anchorView.width / 2f + tooltipWidth / 2f

            if (tooltipMaxWidth >= screenWidth) {
                auxX = screenWidth - tooltipWidth - getValueInPixels(horizontalMargin, context).toInt() - getValueInPixels(horizontalMarginClipped, context).toInt()
            }
        }

        //If the tooltip is drawn out of the screen from the left side clamp it to the screen with the horizontal margin.
        if (anchorXScreenPos - anchorView.width / 2f - tooltipWidth / 2f <= 0f) {
            auxX = getValueInPixels(horizontalMargin, context).toInt() + getValueInPixels(horizontalMarginClipped, context).toInt()
        }

        //If the tooltip exceeds the top bound, clamp it as well.
        if (y - statusBarHeight <= 0f) {
            auxY = getValueInPixels(verticalMargin, context).toInt() + statusBarHeight + getValueInPixels(verticalMarginClipped, context).toInt()
        }

        //If the tooltip exceeds the screen bounds, clip it to the end of the screen.
        tooltip?.showAtLocation(anchorView, GRAVITY, auxX, auxY)
    }

    private fun calculateXYPosition(
        screenPos: IntArray,
        tooltipWidth: Int,
        estimatedTooltipHeight: Float,
        tooltipArrowHeight: Int,
        context: Context
    ): Pair<Float, Float> {

        return when(tooltipGravity) {
            TooltipGravity.TOP -> {
                Pair(
                    screenPos[0] - tooltipWidth / 2f - getValueInPixels(horizontalMargin, context).toInt() + anchorView.width / 2f,
                    screenPos[1] - estimatedTooltipHeight - getValueInPixels(verticalMargin, context).toInt() - (if (arrow) tooltipArrowHeight else 0)
                )
            }
            TooltipGravity.BOTTOM -> {
                Pair(
                    screenPos[0] - tooltipWidth / 2f - getValueInPixels(horizontalMargin, context).toInt() + anchorView.width / 2f,
                    screenPos[1] + anchorView.height / 2f + getValueInPixels(verticalMargin, context).toInt() + (if (arrow) tooltipArrowHeight else 0)
                )
            }
            TooltipGravity.BOTTOM_RIGHT -> {
                Pair(
                    screenPos[0] - tooltipWidth + anchorView.width - getValueInPixels(horizontalMargin, context),
                    screenPos[1] + anchorView.height / 2f + getValueInPixels(verticalMargin, context).toInt() + (if (arrow) tooltipArrowHeight else 0)
                )
            }
        }
    }
}