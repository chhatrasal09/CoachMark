package com.android.library.coachmark.components

import android.content.Context
import android.graphics.*
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.Button
import com.android.library.coachmark.configuration.CoachMarkConfig
import com.android.library.coachmark.utility.Utils

class CoachMarkSkipButton : Button, Cloneable {

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

    private val mPaint: Paint = Paint()
    private lateinit var mBuilder: Builder

    private fun init(builder: Builder) {
        this.setWillNotDraw(false)
        mBuilder = builder
        mPaint.color = mBuilder.getButtonBackgroundColor()
        text = mBuilder.getText()
        setTextSize(TypedValue.COMPLEX_UNIT_SP, mBuilder.getTextSize())
        if (mBuilder.getButtonBackgroundColor() == mBuilder.getTextColor() && mBuilder.getTextColor() == Color.WHITE) {
            setTextColor(Color.BLACK)
        } else if (mBuilder.getButtonBackgroundColor() == mBuilder.getTextColor() && mBuilder.getTextColor() == Color.BLACK) {
            setTextColor(Color.WHITE)
        } else {
            setTextColor(mBuilder.getTextColor())
        }
        setOnClickListener { mBuilder.getButtonClickListener() }
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.drawRoundRect(
                RectF(0f,
                        0f,
                        width.toFloat(),
                        height.toFloat()),
                Utils.dpToPx(context, mBuilder.getButtonCornerRadius()),
                Utils.dpToPx(context, mBuilder.getButtonCornerRadius()),
                mPaint
        )
        super.onDraw(canvas)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    class Builder(private val mContext: Context) : Cloneable {
        private var mTextSize: Float = 16f
        private var mText: String = "Skip"
        private var mCornerRadius: Float = 8f
        private var mTextColor: Int = Color.BLACK
        private var mBackgroundColor: Int = Color.WHITE
        private var mButtonClickListener: ButtonClickListener? = null
        private var mMargin: Rect = Rect()
        private var mPadding: Rect = Rect()
//            object : ButtonClickListener {
//            override fun onSkipButtonClick(view: View) {
//                ((mContext as Activity).window.decorView as ViewGroup).removeView(view)
//            }
//        }

        fun getTextSize(): Float = mTextSize
        fun getText(): String = mText
        fun getTextColor(): Int = mTextColor
        fun getButtonCornerRadius(): Float = mCornerRadius
        fun getButtonBackgroundColor(): Int = mBackgroundColor
        fun getButtonClickListener(): ButtonClickListener? = mButtonClickListener
        fun getButtonMargin(): Rect = mMargin
        fun getButtonPadding(): Rect = mPadding


        fun setConfig(config: CoachMarkConfig) {
            val skipButtonBuilder = config.getSkipButtonBuilder()
            setTextColor(skipButtonBuilder.getTextColor())
            setButtonBackgroundColor(skipButtonBuilder.getButtonBackgroundColor())
            setTextSize(skipButtonBuilder.getTextSize())
            setButtonCornerRadius(skipButtonBuilder.getButtonCornerRadius())
            skipButtonBuilder.getButtonClickListener()?.let {
                setButtonClickListener(it)
            }
            this.setMargin(skipButtonBuilder.getButtonMargin())
            this.setPadding(skipButtonBuilder.getButtonPadding())
        }

        fun setTextSize(size: Float): Builder {
            mTextSize = size
            return this
        }

        fun setText(text: String): Builder {
            mText = text
            return this
        }

        fun setTextColor(color: Int): Builder {
            mTextColor = color
            return this
        }

        fun setMargin(left: Int, top: Int, right: Int, bottom: Int): Builder {
            mMargin.set(left, top, right, bottom)
            return this
        }

        fun setMargin(margin: Rect): Builder {
            mMargin.set(margin)
            return this
        }

        fun setPadding(padding: Rect): Builder {
            mPadding.set(padding)
            return this
        }

        fun setPadding(left: Int, top: Int, right: Int, bottom: Int): Builder {
            mPadding.set(left, top, right, bottom)
            return this
        }

        fun setButtonCornerRadius(radius: Float): Builder {
            mCornerRadius = radius
            return this
        }

        fun setButtonBackgroundColor(color: Int): Builder {
            mBackgroundColor = color
            return this
        }

        fun setButtonClickListener(listener: ButtonClickListener): Builder {
            mButtonClickListener = listener
            return this
        }

        fun build(): CoachMarkSkipButton {
            return CoachMarkSkipButton(mContext, this)
        }

        public override fun clone(): Any {
            return super.clone() as Builder
        }
    }

    interface ButtonClickListener {
        fun onSkipButtonClick(view: View)
    }
}