@file:Suppress("MemberVisibilityCanBePrivate")

package com.mindera.skeletoid.widgets.tooltip

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.TouchDelegate
import android.view.View
import android.view.ViewGroup
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
import com.mindera.skeletoid.generic.UIUtils.getValueInPixels

/**
 * @constructor Primary constructor for the tooltip view.
 *
 * @property anchorView The view to anchor the tooltip to.
 *
 * @property message The message to display on the tooltip
 *
 * @property layoutRes The layout resource that contains the tooltip..layout
 *
 * @property messageViewResId The resource id equivalent to the text view that contains the text of our tooltip
 *
 * @property backgroundViewResId The resource id equivalent to the root/background view of our tooltip.
 *
 * @property elevation Defines a custom elevation for all the tooltip components, in dips (Density Independent Pixels).
 *
 * @property popupWidth The tooltip width to be displayed in dips (Density Independent Pixels).
 * 250dp seems to be a good roundabout value.
 *
 * @property popupHeight The tooltip height to be displayed in dips (Density Independent Pixels).
 * 46dp seems to be a good roundabout value for two lines.
 *
 * @property tooltipGravity Defines a new gravity in relation to the [anchorView]
 *
 * @property indefinite If false the tooltip will dismiss after a certain amount of time. Else, it will remain there
 * until clicked again.
 *
 * @property showDuration  If the tooltip is definite, specify the time the tooltip should remain visible.
 *
 * @property verticalMargin The vertical offset from the anchorview to the tooltip in dips.
 *
 * @property horizontalMargin The horizontal offset from the edges of the screen.
 *
 * @property horizontalMarginClipped The horizontal offset from the edges of the screen to the tooltip when the bounds
 * of the tooltip exceed the bounds of the screen.
 *
 * @property verticalMarginClipped The vertical offset from the anchor view to the tooltip when the bounds of the tooltip exceed
 * the bounds of the screen.
 *
 * @property insideTouchDismissible Enables or disables dismiss on inside touch.
 *
 * @property outsideTouchDismissible Enables or disables dismiss on outside touch.
 *
 * @property modal Defines the popup window as focusable. Set this to false if you want the view
 * to be click through. Although this messes up with the inside/outside touch flags, so use
 * this carefully.
 *
 * @property arrow Creates the arrow to show pointing to the [anchorView] according to our [gravity]
 *
 * @property animationStyleResource Allows the usage of custom animations to show/hide the tooltip.
 */
class PopupTooltip(
    private val anchorView: View,
    private val message: CharSequence,
    @LayoutRes private val layoutRes: Int,
    @IdRes private val messageViewResId: Int,
    @IdRes private val backgroundViewResId: Int,
    private val elevation: Int = DEFAULT_ELEVATION,
    private val popupWidth: Int = DEFAULT_TOOLTIP_WIDTH,
    private val popupHeight: Int = DEFAULT_TOOLTIP_HEIGHT,
    private val tooltipGravity: TooltipGravity = TooltipGravity.TOP,
    private val indefinite: Boolean = false,
    private val showDuration: Long = 3500L,
    private val verticalMargin: Int = 0,
    private val horizontalMargin: Int = 0,
    private val horizontalMarginClipped: Int = 0,
    private val verticalMarginClipped: Int = 0,
    private val insideTouchDismissible: Boolean = true,
    private val outsideTouchDismissible: Boolean = true,
    private val modal: Boolean = true,
    private val arrow: ArrowData? = null,
    @StyleRes private val animationStyleResource: Int? = null,
) {

    companion object {
        private const val DEFAULT_TOOLTIP_HEIGHT = 46
        private const val DEFAULT_TOOLTIP_WIDTH = 250
        private const val DEFAULT_ARROW_WIDTH = 10
        private const val DEFAULT_ARROW_HEIGHT = 10
        private const val DEFAULT_ELEVATION = 5
        private const val GRAVITY = Gravity.NO_GRAVITY

        data class Coordinates(val x: Float, val y: Float)
    }

    /**
     * The possible orientation for the tooltip in relation to the [anchorView].
     * Currently it can only be displayed on top or below a view.
     */
    enum class TooltipGravity { TOP, BOTTOM, BOTTOM_RIGHT }

    /**
     * @constructor Constructor for our arrow that will be displayed pointing at the [anchorView]
     *
     * @property arrowWidth The width of our arrow drawable.
     * @property arrowHeight The height of our arrow drawable.
     * @property arrowDrawableResId The drawable resource that contains the image/shape of our arrow.
     * @property arrowStrokeColorResId The color resource of our stroke around the arrow, IF we define an [arrowStrokeViewResId]
     * @property tooltipArrowResId The tooltip layout equivalent of the arrow view resource id.
     * @property arrowStrokeViewResId The tooltip layout equivalent of the arrow stroke view resource id.
     */
    data class ArrowData(
        val arrowWidth: Int = DEFAULT_ARROW_WIDTH,
        val arrowHeight: Int = DEFAULT_ARROW_HEIGHT,
        @DrawableRes val arrowDrawableResId: Int,
        @ColorRes val arrowStrokeColorResId: Int = android.R.color.white,
        @IdRes val tooltipArrowResId: Int,
        @IdRes val arrowStrokeViewResId: Int? = null,
    )

    /**
     * Delegate that handles the X,Y coordinates of the tooltip and handles the error/edge cases, e.g x,y coordinates
     * exceed the max size of the screen, etc.
     */
    private val positionDelegate = PopupUtils()

    /**
     * Saves a reference to our currently displayed tooltip. This is here so we can control when
     * we dismiss/show it.
     */
    private var tooltip: PopupWindow? = null

    /**
     * Sets the tooltip as null after showDuration amount of time. This is only used
     * when we set the tooltip as definite.
     */
    private val handler = Handler(Looper.getMainLooper())

    /**
     * Clears the tooltip if it's showing and removes it's reference.
     */
    fun clearTooltip() {
        tooltip?.dismissIfShowing()
        tooltip = null
    }

    /**
     * Creates a touch delegate that increases the bounds of the view's touch area by a given amount, in
     * dips (Density Independent Pixels).
     *
     * @param increaseAmount - the amount to increase in dips.
     */
    fun touchDelegateTouchAreaIncreaseAmount(increaseAmount: Int) =
        apply { setupTouchDelegate(anchorView, increaseAmount, anchorView.context) }

    /**
     * Displays the tooltip
     */
    fun show() {
        showTooltip(anchorView, message)
    }

    /**
     * Internal helper class to show the tooltip.
     */
    private fun showTooltip(anchorView: View, message: CharSequence) {
        handler.removeCallbacksAndMessages(null)

        if (tooltip != null) {
            clearTooltip()
            return
        }

        if (TextUtils.isEmpty(message)) {
            return
        }

        val (x, y) = setupTooltip(anchorView, message)

        tooltip?.showAtLocation(anchorView, GRAVITY, x.toInt(), y.toInt())
    }

    /**
     * Internal helper class to create the tooltip and calculate the display coordinates.
     */
    @SuppressLint("InflateParams")
    private fun setupTooltip(view: View, text: CharSequence?): Coordinates {
        val screenPos = IntArray(2)
        val displayFrame = Rect()
        val context = view.context

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val tooltipWidth = getValueInPixels(popupWidth, context).toInt()

        view.getLocationOnScreen(screenPos)
        view.getWindowVisibleDisplayFrame(displayFrame)

        val estimatedTooltipHeight = getValueInPixels(popupHeight, context)

        //Setup the tooltip layout
        val layout = inflater.inflate(layoutRes, null)

        //Set the tooltip text using the [messageViewResId]
        val textView = layout.findViewById<TextView>(messageViewResId)
        textView.text = text

        //Setup the elevation for both our text view and our background view
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val backgroundView = layout.findViewById<View>(backgroundViewResId)

            textView.elevation = getValueInPixels(elevation, context)
            backgroundView.elevation = getValueInPixels(elevation, context)
        }

        //Setup the arrow
        arrow?.let { nonNullArrow ->
            setupArrow(nonNullArrow, layout, context, screenPos[0])
        }

        setupTooltipBehaviour(context, layout, tooltipWidth)

        val (initialX, initialY) = positionDelegate.calculateTooltipInitialPosition(
            screenPos = screenPos,
            tooltipWidth = tooltipWidth,
            estimatedTooltipHeight = estimatedTooltipHeight,
            anchorView = anchorView,
            arrow = arrow,
            tooltipGravity = tooltipGravity,
            horizontalMargin = horizontalMargin,
            verticalMargin = verticalMargin,
            context = context
        )

        return positionDelegate.calculateTooltipFinalPosition(
            x = initialX,
            y = initialY,
            statusBarHeight = displayFrame.top,
            anchorXScreenPos = screenPos[0],
            tooltipWidth = tooltipWidth,
            anchorView = anchorView,
            tooltipGravity = tooltipGravity,
            horizontalMargin = horizontalMargin,
            horizontalMarginClipped = horizontalMarginClipped,
            verticalMargin = verticalMargin,
            verticalMarginClipped = verticalMarginClipped,
            context = context
        )
    }

    /**
     * If the arrow flag is set to true, this method enables the arrow and places it at the
     * required place, at the top center of the anchor view.
     */
    private fun setupArrow(
        arrow: ArrowData,
        layout: View,
        context: Context,
        anchorViewAbsoluteX: Int,
    ) {
        val stroke = layout.findViewById<ImageView>(arrow.arrowStrokeViewResId ?: View.NO_ID)
        val tooltipArrow = layout.findViewById<ImageView>(arrow.tooltipArrowResId)

        val elevation = getValueInPixels(elevation, context)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tooltipArrow.setImageDrawable(AppCompatResources.getDrawable(context, arrow.arrowDrawableResId))

            tooltipArrow.elevation = elevation
            tooltipArrow.visibility = View.VISIBLE

            if (stroke != null) {
                stroke.setImageDrawable(AppCompatResources.getDrawable(context, arrow.arrowDrawableResId))
                stroke.setColorFilter(arrow.arrowStrokeColorResId)

                stroke.elevation = elevation
                stroke.visibility = View.VISIBLE
            }
        }

        tooltipArrow.post {
            val arrowXPosition = positionDelegate.calculateArrowXPosition(
                tooltipArrow = tooltipArrow,
                gravity = tooltipGravity,
                context = context,
                arrowWidth = arrow.arrowWidth,
                anchorViewAbsoluteX = anchorViewAbsoluteX,
                anchorViewWidth = anchorView.width
            )

            tooltipArrow.x = arrowXPosition
            stroke?.let { it.x = arrowXPosition }
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

        //Handle timed dismiss
        if (!indefinite) {
            handler.postDelayed({ clearTooltip() }, showDuration)
        }

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
            animationStyleResource?.let { animationStyle = it }

            //Allow to draw outside of the screen.
            isClippingEnabled = true
        }
    }

    private fun PopupWindow.dismissIfShowing() {
        if (isShowing) dismiss()
    }
}