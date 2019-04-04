package com.android.library.coachmark.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.view.View
import com.android.library.coachmark.configuration.CoachMarkConfig
import com.android.library.coachmark.utility.Orientation
import com.android.library.coachmark.utility.Shape
import com.android.library.coachmark.utility.Utils
import kotlin.math.roundToInt

class CoachMarkInfoToolTip : View {
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

    private lateinit var mBuilder: Builder
    private val mPaint = Paint()
    private val mPath = Path()

    override fun onDraw(canvas: Canvas?) {
        when (mBuilder.getToolTipShape()) {
            Shape.TRIANGLE -> {
                when (mBuilder.getToolTipOrientation()) {
                    Orientation.UP -> {
                        mPath.moveTo((width / 2).toFloat(), 0f)
                        mPath.lineTo((width / 2).toFloat(), 0f)
                        mPath.lineTo(width.toFloat(), height.toFloat())
                        mPath.lineTo(0f, height.toFloat())
                        mPath.close()
                        canvas?.drawPath(mPath, mPaint)
                    }
                    Orientation.DOWN -> {
                        mPath.moveTo(width.toFloat(), 0f)
                        mPath.lineTo(width.toFloat(), 0f)
                        mPath.lineTo((width / 2).toFloat(), height.toFloat())
                        mPath.lineTo(0f, 0f)
                        mPath.close()
                        canvas?.drawPath(mPath, mPaint)
                    }
                    Orientation.LEFT -> {
                        mPath.moveTo(width.toFloat(), (height / 2).toFloat())
                        mPath.lineTo(width.toFloat(), (height / 2).toFloat())
                        mPath.lineTo(0f, height.toFloat())
                        mPath.lineTo(0f, 0f)
                        mPath.close()
                        canvas?.drawPath(mPath, mPaint)
                    }
                    Orientation.RIGHT -> {
                        mPath.moveTo(0f, (height / 2).toFloat())
                        mPath.lineTo(0f, (height / 2).toFloat())
                        mPath.lineTo(width.toFloat(), height.toFloat())
                        mPath.lineTo(width.toFloat(), 0f)
                        mPath.close()
                        canvas?.drawPath(mPath, mPaint)
                    }
                }
            }
            Shape.CIRCLE -> {
                when (mBuilder.getToolTipOrientation()) {
                    Orientation.UP -> {
                        canvas?.drawCircle((width / 2).toFloat(), height.toFloat(), (width / 2).toFloat(), mPaint)
                    }
                    Orientation.DOWN -> {
                        canvas?.drawCircle((width / 2).toFloat(), 0f, (width / 2).toFloat(), mPaint)
                    }
                    Orientation.LEFT -> {
                        canvas?.drawCircle(width.toFloat(), (height / 2).toFloat(), (height / 2).toFloat(), mPaint)
                    }
                    Orientation.RIGHT -> {
                        canvas?.drawCircle(0f, height.toFloat(), (height / 2).toFloat(), mPaint)
                    }
                }
            }
        }
        super.onDraw(canvas)
    }

    private fun init(builder: Builder) {
        this.setWillNotDraw(false)
        mBuilder = builder
        mPaint.color = mBuilder.getToolTipColor()
        mPaint.isAntiAlias = true
        mPaint.style = Paint.Style.FILL_AND_STROKE

        mPath.fillType = Path.FillType.EVEN_ODD
    }

    class Builder(private val mContext: Context) {
        private var mToolTipShape: Shape = Shape.TRIANGLE
        private var mToolTipColor: Int = Color.WHITE
        private var mToolTipWidth: Int = Utils.dpToPx(mContext, 6).roundToInt()
        private var mToolTipHeight: Int = Utils.dpToPx(mContext, 6).roundToInt()
        private var mToolTipOrientation: Orientation = Orientation.UP

        fun getToolTipShape(): Shape = mToolTipShape
        fun getToolTipColor(): Int = mToolTipColor
        fun getToolTipWidth(): Int = mToolTipWidth
        fun getToolTipHeight(): Int = mToolTipHeight
        fun getToolTipOrientation(): Orientation = mToolTipOrientation

        fun setConfig(config: CoachMarkConfig) {
            val toolTipBuilder = config.getCoachMarkToolTipBuilder()
            toolTipBuilder?.let {
                setToolTipColor(it.getToolTipColor())
                setToolTipHeight(it.getToolTipHeight())
                setToolTipWidth(it.getToolTipWidth())
                setToolTipOrientation(it.getToolTipOrientation())
                setToolTipShape(it.getToolTipShape())
            }
        }

        fun setToolTipShape(shape: Shape): Builder {
            mToolTipShape = shape
            return this
        }

        fun setToolTipColor(color: Int): Builder {
            mToolTipColor = color
            return this
        }

        fun setToolTipSize(width: Int, height: Int): Builder {
            mToolTipWidth = Utils.dpToPx(mContext, width).roundToInt()
            mToolTipHeight = Utils.dpToPx(mContext, height).roundToInt()
            return this
        }

        fun setToolTipWidth(width: Int): Builder {
            mToolTipWidth = Utils.dpToPx(mContext, width).roundToInt()
            return this
        }

        fun setToolTipHeight(height: Int): Builder {
            mToolTipHeight = Utils.dpToPx(mContext, height).roundToInt()
            return this
        }

        fun setToolTipOrientation(orientation: Orientation): Builder {
            mToolTipOrientation = orientation
            return this
        }

        fun build(): CoachMarkInfoToolTip? {
            return CoachMarkInfoToolTip(mContext, this)
        }
    }
}