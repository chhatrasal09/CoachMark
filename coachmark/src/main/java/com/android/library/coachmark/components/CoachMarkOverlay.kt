package com.android.library.coachmark.components

import android.content.Context
import android.graphics.*
import android.os.Build
import androidx.annotation.RequiresApi
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.android.library.coachmark.configuration.CoachMarkConfig
import com.android.library.coachmark.utility.Gravity
import com.android.library.coachmark.utility.Shape
import com.android.library.coachmark.utility.ShapeType
import com.android.library.coachmark.utility.Utils
import kotlin.math.roundToInt

class CoachMarkOverlay : FrameLayout {
    constructor(context: Context, builder: Builder) : super(context) {
        init(builder)
    }

    constructor(context: Context, attrs: AttributeSet?, builder: Builder) : super(context, attrs) {
        init(builder)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, builder: Builder) : super(
            context,
            attrs,
            defStyleAttr
    ) {
        init(builder)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int, builder: Builder) : super(
            context,
            attrs,
            defStyleAttr,
            defStyleRes
    ) {
        init(builder)
    }

    lateinit var mBuilder: Builder
    private var mBaseBitmap: Bitmap? = null
    private var mLayer: Canvas? = null
    private val mOverlayTintPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mOverlayTransparentPaint = Paint()
    private var mInfoView: CoachMarkInfo? = null
    private var mSkipButton: CoachMarkSkipButton? = null

    private fun init(builder: Builder) {
        this.setWillNotDraw(false)
        mBuilder = builder
        mOverlayTransparentPaint.color = Color.TRANSPARENT
        mOverlayTransparentPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OUT)
        setOnClickListener {
            mBuilder.getOverlayClickListener()?.apply {
                onOverlayClick(this@CoachMarkOverlay)
                mShouldRender = true
            }
        }
        addSkipButton()
    }

    fun addSkipButton() {
        mSkipButton = mBuilder.getSkipButton()
        mSkipButton?.let {
            if (indexOfChild(it) == -1) {
                addView(it)
                it.setOnClickListener {
                    mBuilder.getSkipButtonBuilder()?.getButtonClickListener()?.onSkipButtonClick(this)
                }
                setSkipButton()
            }
        }
    }

    override fun invalidate() {
        mShouldRender = true;
        super.invalidate()
    }

    private var mShouldRender = true
    override fun onDraw(canvas: Canvas?) {
        drawOverlayTint()
        drawTransparentOverlay()
        mBaseBitmap?.apply { canvas?.drawBitmap(this, 0f, 0f, null) }
        setInfoView()
        super.onDraw(canvas)
    }

    fun resetView() {
        mBuilder.setInfoViewBuilder(null)
        mBuilder.setToolTipBuilder(null)
    }

    private fun drawOverlayTint() {
        mBaseBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        mLayer = Canvas(mBaseBitmap!!)
        mOverlayTintPaint.color = mBuilder.getOverlayColor()
        val alpha = mBuilder.getOverlayOpacity()
        mOverlayTintPaint.alpha = alpha
        mLayer?.drawRect(Rect(0, 0, width, height), mOverlayTintPaint)
    }

    private fun drawTransparentOverlay() {
        val targetViewSize: Rect = Rect()
        if (mBuilder.getOverlayTargetView() != null) {
            mBuilder.getOverlayTargetView()?.getGlobalVisibleRect(targetViewSize)
        } else {
            targetViewSize.set(mBuilder.getOverlayTargetCoordinates())
        }
        targetViewSize.left -= mBuilder.getOverlayTransparentPadding().left
        targetViewSize.top -= mBuilder.getOverlayTransparentPadding().top
        targetViewSize.right += mBuilder.getOverlayTransparentPadding().right
        targetViewSize.bottom += mBuilder.getOverlayTransparentPadding().bottom

        targetViewSize.left += mBuilder.getOverlayTransparentMargin().left
        targetViewSize.top += mBuilder.getOverlayTransparentMargin().top
        targetViewSize.right += mBuilder.getOverlayTransparentMargin().right
        targetViewSize.bottom += mBuilder.getOverlayTransparentMargin().bottom
        when (mBuilder.getOverlayTransparentShape()) {
            Shape.BOX -> {
                mLayer?.drawRoundRect(
                        RectF(targetViewSize),
                        mBuilder.getOverlayTransparentCornerRadius(),
                        mBuilder.getOverlayTransparentCornerRadius(),
                        mOverlayTransparentPaint
                )
            }
            Shape.CIRCLE -> {
                var radius: Float = mBuilder.getOverlayTransparentCircleRadius()
                when (mBuilder.getOverLayType()) {
                    ShapeType.INSIDE -> {
                        if (radius < 0) {
                            radius = (targetViewSize.height() / 2).toFloat()
                        }
                    }
                    ShapeType.OUTSIDE -> {
                        if (radius < 0) {
                            radius = (targetViewSize.width() / 2).toFloat()
                        }
                    }
                }
                when (mBuilder.getOverlayTransparentGravity()) {
                    Gravity.CENTER -> mLayer?.drawCircle(
                            targetViewSize.exactCenterX(),
                            targetViewSize.exactCenterY(),
                            radius,
                            mOverlayTransparentPaint
                    )
                    Gravity.START -> mLayer?.drawCircle(
                            targetViewSize.left.toFloat(),
                            targetViewSize.exactCenterY(),
                            radius,
                            mOverlayTransparentPaint
                    )
                    Gravity.END -> mLayer?.drawCircle(
                            targetViewSize.exactCenterX(),
                            targetViewSize.right.toFloat(),
                            radius,
                            mOverlayTransparentPaint
                    )
                }
            }
        }
    }

    fun setSkipButton() {
        mSkipButton?.apply {
            val skipButtonParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            skipButtonParams.gravity =
                    android.view.Gravity.TOP or android.view.Gravity.START or android.view.Gravity.LEFT
            val margin = mBuilder.getSkipButtonBuilder()!!.getButtonMargin()
            skipButtonParams.leftMargin = margin!!.left
            skipButtonParams.marginStart = margin!!.left
            try {
                skipButtonParams.topMargin = margin.top + Utils.getStatusBarHeight(context)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            layoutParams = skipButtonParams
        }
    }

    private val targetSize = Rect()
    private var mTooltip: CoachMarkInfoToolTip? = null
    private fun setInfoView() {
        if (mBuilder.getOverlayTargetView() != null) {
            mBuilder.getOverlayTargetView()?.getGlobalVisibleRect(targetSize)
        } else {
            targetSize.set(mBuilder.getOverlayTargetCoordinates())
        }
        mBuilder.getInfoViewBuilder()?.apply {
            /**
             * creating custom info textView.
             * */
            mInfoView = mBuilder.getInfoView()
            mInfoView?.text = getInfoText()
            val infoTextLayoutParams = LayoutParams(
                    getInfoViewWidth(),
                    getInfoViewHeight()
            )
            if (isInfoViewCenterAlignment()) {
                infoTextLayoutParams.gravity = android.view.Gravity.CENTER
            }
            /**
             * creating custom tooltip.
             * */
            mTooltip = mBuilder.getToolTip()
            /**
             * if toolTip is not null then add the toolTip and info TextView
             * */
            if (mTooltip != null) {
                val lp = LayoutParams(
                        mBuilder.getToolTipBuilder()!!.getToolTipWidth(),
                        mBuilder.getToolTipBuilder()!!.getToolTipHeight()
                )
                when (getInfoViewGravity()) {
                    Gravity.BOTTOM -> {
                        when (mBuilder.getToolTipBuilder()?.getToolTipShape()) {
                            Shape.TRIANGLE -> {
                                lp.topMargin = targetSize.bottom + Utils.dpToPx(context, 8).roundToInt() +
                                        mBuilder.getOverlayTransparentPadding().bottom
                                lp.leftMargin = targetSize.centerX() -
                                        mBuilder.getToolTipBuilder()!!.getToolTipWidth() / 2
                                lp.marginStart = targetSize.centerX() -
                                        mBuilder.getToolTipBuilder()!!.getToolTipWidth() / 2
                                infoTextLayoutParams.topMargin =
                                        lp.topMargin + mBuilder.getToolTipBuilder()!!.getToolTipHeight()
                            }
                            Shape.CIRCLE -> {
                                lp.width = mBuilder.getToolTipBuilder()!!.getToolTipWidth()
                                lp.height = mBuilder.getToolTipBuilder()!!.getToolTipHeight() / 2
                                lp.topMargin = targetSize.bottom + Utils.dpToPx(context, 8).roundToInt()
                                lp.leftMargin = targetSize.centerX() -
                                        mBuilder.getToolTipBuilder()!!.getToolTipWidth() / 2
                                lp.marginStart = targetSize.centerX() -
                                        mBuilder.getToolTipBuilder()!!.getToolTipWidth() / 2
                                infoTextLayoutParams.topMargin =
                                        lp.topMargin + mBuilder.getToolTipBuilder()!!.getToolTipHeight() / 2
                            }
                        }
                        if (mShouldRender) {
                            mTooltip?.layoutParams = lp
                            addView(mTooltip)
                            mShouldRender = false
                        }
                    }
                    Gravity.TOP -> {
                        when (mBuilder.getToolTipBuilder()?.getToolTipShape()) {
                            Shape.TRIANGLE -> {
                                lp.bottomMargin = targetSize.top - Utils.dpToPx(context, 8).roundToInt()
                                lp.leftMargin =
                                        targetSize.centerX() - mBuilder.getToolTipBuilder()!!.getToolTipWidth() / 2
                                lp.marginStart =
                                        targetSize.centerX() - mBuilder.getToolTipBuilder()!!.getToolTipWidth() / 2
                                infoTextLayoutParams.bottomMargin =
                                        lp.bottomMargin + mBuilder.getToolTipBuilder()!!.getToolTipHeight()
                            }
                            Shape.CIRCLE -> {
                                lp.width = mBuilder.getToolTipBuilder()!!.getToolTipWidth()
                                lp.height = mBuilder.getToolTipBuilder()!!.getToolTipHeight() / 2
                                lp.bottomMargin = targetSize.top - Utils.dpToPx(context, 8).roundToInt()
                                lp.leftMargin = targetSize.centerX() -
                                        mBuilder.getToolTipBuilder()!!.getToolTipWidth() / 2
                                lp.marginStart = targetSize.centerX() -
                                        mBuilder.getToolTipBuilder()!!.getToolTipWidth() / 2
                                infoTextLayoutParams.bottomMargin =
                                        lp.bottomMargin + mBuilder.getToolTipBuilder()!!.getToolTipHeight() / 2
                            }
                        }
                        if (mShouldRender) {
                            mTooltip?.layoutParams = lp
                            addView(mTooltip)
                            mShouldRender = false
                        }
                    }
                }
                if (isNotAttachedToTarget()) {
                    infoTextLayoutParams.leftMargin = getMargin().left
                    infoTextLayoutParams.marginStart = getMargin().left
                    infoTextLayoutParams.rightMargin = getMargin().right
                    infoTextLayoutParams.marginEnd = getMargin().right
                }
                /*
                * If text view is attached to the target view
                */
                else {
                    //TODO - add the logic to position the text view
                }
            } else {
                when (getInfoViewGravity()) {
                    Gravity.BOTTOM -> {
                        infoTextLayoutParams.topMargin =
                                targetSize.bottom + Utils.dpToPx(context, 8).roundToInt() +
                                        mBuilder.getOverlayTransparentPadding().bottom
                    }
                    Gravity.TOP -> {
                        infoTextLayoutParams.bottomMargin = targetSize.top - Utils.dpToPx(
                                context,
                                8
                        ).roundToInt() - mBuilder.getOverlayTransparentPadding().top
                    }
                }
                /**
                 * if toolTip is null then add only the info text view at the specified position.
                 * */
                if (isNotAttachedToTarget()) {
                    infoTextLayoutParams.leftMargin = getMargin().left
                    infoTextLayoutParams.marginStart = getMargin().left
                    infoTextLayoutParams.rightMargin = getMargin().right
                    infoTextLayoutParams.marginEnd = getMargin().right
                }
                /*
                 * If text view is attached to the target view
                 */
                else {

                }
            }
            mInfoView?.let {
                it.layoutParams = infoTextLayoutParams
                it.setPadding(
                        getPadding().left,
                        getPadding().top,
                        getPadding().right,
                        getPadding().bottom
                )
                addView(it)
            }
        }

    }

    fun show(root: ViewGroup) {
        root.addView(this)
    }

    class Builder(private val mContext: Context) {
        private var mOverlayTargetView: View? = null
        private var mOverlayColor: Int = Color.BLACK
        private var mOverlayOpacity: Int = 150
        private var mOverlayTransparentShape: Shape = Shape.BOX
        private var mOverlayTransparentCircleRadius: Float = 0f
        private var mOverlayTransparentCornerRadius: Float = 8f
        private var mOverlayTransparentGravity: Gravity = Gravity.CENTER
        private var mOverlayTransparentMargin: Rect = Rect()
        private var mOverlayTransparentPadding: Rect = Rect()
        private var mOverLayType: ShapeType = ShapeType.OUTSIDE
        private var mOverlayClickListener: OverlayClickListener? = null
        private var mTargetCoordinates: Rect = Rect()
        private var mBaseTabPosition: Int = -1

        private var mSkipButtonBuilder: CoachMarkSkipButton.Builder? = null
        private var mInfoViewBuilder: CoachMarkInfo.Builder? = null
        private var mToolTipBuilder: CoachMarkInfoToolTip.Builder? = null

        fun getOverlayTargetView(): View? = mOverlayTargetView
        fun getOverlayColor(): Int = mOverlayColor
        fun getOverlayOpacity(): Int = mOverlayOpacity
        fun getOverlayTransparentShape(): Shape = mOverlayTransparentShape
        fun getOverlayTransparentCircleRadius(): Float = mOverlayTransparentCircleRadius
        fun getOverlayTransparentCornerRadius(): Float = mOverlayTransparentCornerRadius
        fun getOverlayTransparentGravity(): Gravity = mOverlayTransparentGravity
        fun getOverLayType(): ShapeType = mOverLayType
        fun getTabPosition(): Int = mBaseTabPosition
        fun getOverlayTransparentMargin(): Rect = mOverlayTransparentMargin
        fun getOverlayTransparentPadding(): Rect = mOverlayTransparentPadding
        fun getOverlayTargetCoordinates(): Rect = mTargetCoordinates
        fun getOverlayClickListener(): OverlayClickListener? = mOverlayClickListener

        fun getSkipButton(): CoachMarkSkipButton? = if (mSkipButtonBuilder == null) null else mSkipButtonBuilder?.build()
        fun getSkipButtonBuilder(): CoachMarkSkipButton.Builder? = mSkipButtonBuilder
        fun getInfoView(): CoachMarkInfo? = if (mInfoViewBuilder == null) null else mInfoViewBuilder?.build()
        fun getInfoViewBuilder(): CoachMarkInfo.Builder? = mInfoViewBuilder
        fun getToolTip(): CoachMarkInfoToolTip? = if (mToolTipBuilder == null) null else mToolTipBuilder?.build()
        fun getToolTipBuilder(): CoachMarkInfoToolTip.Builder? = mToolTipBuilder

        fun setConfig(config: CoachMarkConfig) {
            val overlayBuilder = config.getOverlayBuilder()

            this.setOriginalCustomOverlayOpacity(overlayBuilder.getOverlayOpacity())
            this.setOverlayColor(overlayBuilder.getOverlayColor())
            this.setOverlayTransparentShape(overlayBuilder.mOverlayTransparentShape)
            this.setOverlayTransparentCircleRadius(overlayBuilder.mOverlayTransparentCircleRadius)
            this.setOverlayTransparentCornerRadius(overlayBuilder.mOverlayTransparentCornerRadius)
            this.setOverlayTransparentGravity(overlayBuilder.mOverlayTransparentGravity)
            this.setOverLayType(overlayBuilder.getOverLayType())
            this.setOverlayTransparentMargin(overlayBuilder.getOverlayTransparentMargin())
            this.setOverlayTransparentPadding(overlayBuilder.getOverlayTransparentPadding())
            overlayBuilder.getOverlayClickListener()?.let { this.setOverlayClickListener(it) }
        }

        fun setOverlayTargetCoordinates(left: Int, top: Int, right: Int, bottom: Int): Builder {
            mTargetCoordinates.set(left, top, right, bottom)
            return this
        }

        fun setOverlayTargetCoordinates(coordinates: Rect): Builder {
            mTargetCoordinates.set(coordinates)
            return this
        }


        fun setOverlayTargetView(view: View?): Builder {
            mOverlayTargetView = view
            return this
        }

        fun setOverlayColor(color: Int): Builder {
            mOverlayColor = color
            return this
        }

        fun setOverlayOpacity(opacity: Float): Builder {
            val op = opacity / 100 * 255
            mOverlayOpacity = op.roundToInt()
            return this
        }

        fun setTabPosition(position: Int): Builder {
            mBaseTabPosition = position
            return this
        }

        private fun setOriginalCustomOverlayOpacity(opacity: Int) {
            mOverlayOpacity = opacity
        }

        fun setOverlayTransparentShape(shape: Shape): Builder {
            mOverlayTransparentShape = shape
            return this
        }

        fun setOverlayTransparentCircleRadius(radius: Float): Builder {
            mOverlayTransparentCircleRadius = radius
            return this
        }

        fun setOverlayTransparentCornerRadius(radius: Float): Builder {
            mOverlayTransparentCornerRadius = radius
            return this
        }

        fun setOverlayTransparentGravity(gravity: Gravity): Builder {
            mOverlayTransparentGravity = gravity
            return this
        }

        fun setOverLayType(type: ShapeType): Builder {
            mOverLayType = type
            return this
        }

        fun setOverlayTransparentMargin(margin: Rect): Builder {
            mOverlayTransparentMargin.set(margin)
            return this
        }

        fun setOverlayTransparentMargin(left: Int, top: Int, right: Int, bottom: Int): Builder {
            mOverlayTransparentMargin.left = Utils.dpToPx(mContext, left).roundToInt()
            mOverlayTransparentMargin.top = Utils.dpToPx(mContext, top).roundToInt()
            mOverlayTransparentMargin.right = Utils.dpToPx(mContext, right).roundToInt()
            mOverlayTransparentMargin.bottom = Utils.dpToPx(mContext, bottom).roundToInt()
            return this
        }

        fun setOverlayTransparentPadding(padding: Rect): Builder {
            mOverlayTransparentPadding.set(padding)
            return this
        }

        fun setOverlayTransparentPadding(left: Int, top: Int, right: Int, bottom: Int): Builder {
            mOverlayTransparentPadding.left = Utils.dpToPx(mContext, left).roundToInt()
            mOverlayTransparentPadding.top = Utils.dpToPx(mContext, top).roundToInt()
            mOverlayTransparentPadding.right = Utils.dpToPx(mContext, right).roundToInt()
            mOverlayTransparentPadding.bottom = Utils.dpToPx(mContext, bottom).roundToInt()
            return this
        }

        fun setOverlayClickListener(listener: OverlayClickListener): Builder {
            mOverlayClickListener = listener
            return this
        }

        fun setSkipButtonBuilder(builder: CoachMarkSkipButton.Builder?): Builder {
            mSkipButtonBuilder = builder
            return this
        }

        fun setInfoViewBuilder(builder: CoachMarkInfo.Builder?): Builder {
            mInfoViewBuilder = builder
            return this
        }

        fun setToolTipBuilder(builder: CoachMarkInfoToolTip.Builder?): Builder {
            mToolTipBuilder = builder
            return this
        }

        fun build(): CoachMarkOverlay {
            return CoachMarkOverlay(mContext, this)
        }
    }

    interface OverlayClickListener {
        fun onOverlayClick(overlay: CoachMarkOverlay)
    }
}