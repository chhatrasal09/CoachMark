package com.android.library.coachmark.configuration

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import com.android.library.coachmark.components.CoachMarkInfo
import com.android.library.coachmark.components.CoachMarkInfoToolTip
import com.android.library.coachmark.components.CoachMarkSkipButton
import com.android.library.coachmark.components.CoachMarkOverlay
import com.android.library.coachmark.utility.*

class CoachMarkConfig(private val mContext: Context) {

    private var mSkipButtonBuilder: CoachMarkSkipButton.Builder = CoachMarkSkipButton.Builder(mContext)
    private var mInfoBuilder: CoachMarkInfo.Builder = CoachMarkInfo.Builder(mContext)
    private var mOverlayBuilder: CoachMarkOverlay.Builder = CoachMarkOverlay.Builder(mContext)
    private var mToolTipBuilder: CoachMarkInfoToolTip.Builder? = null
    private var mShowToolTip: Boolean = false

    fun getSkipButtonBuilder(): CoachMarkSkipButton.Builder = mSkipButtonBuilder
    fun getInfoTextBuilder(): CoachMarkInfo.Builder = mInfoBuilder
    fun getOverlayBuilder(): CoachMarkOverlay.Builder = mOverlayBuilder
    fun getCoachMarkToolTipBuilder(): CoachMarkInfoToolTip.Builder? = mToolTipBuilder
    fun getToolTipVisibility(): Boolean = mShowToolTip

    fun setCoachMarkToolTipBuilder(builder: CoachMarkInfoToolTip.Builder): CoachMarkConfig {
        mToolTipBuilder = builder
        return this
    }

    fun setSkipButtonText(text: String): CoachMarkConfig {
        mSkipButtonBuilder.setText(text)
        return this
    }

    fun setSkipButtonBackgroundColor(color: Int): CoachMarkConfig {
        mSkipButtonBuilder.setButtonBackgroundColor(color)
        return this
    }

    fun setSkipButtonTextSize(size: Float): CoachMarkConfig {
        mSkipButtonBuilder.setTextSize(size)
        return this
    }

    fun setSkipButtonTextColor(color: Int): CoachMarkConfig {
        mSkipButtonBuilder.setTextColor(color)
        return this
    }

    fun setSkipButtonCornerRadius(radius: Float): CoachMarkConfig {
        mSkipButtonBuilder.setButtonCornerRadius(radius)
        return this
    }

    fun setSkipButtonMargin(left: Int, top: Int, right: Int, bottom: Int): CoachMarkConfig {
        mSkipButtonBuilder.setMargin(left, top, right, bottom)
        return this
    }

    fun setSkipButtonPadding(left: Int, top: Int, right: Int, bottom: Int): CoachMarkConfig {
        mSkipButtonBuilder.setPadding(left, top, right, bottom)
        return this
    }

    fun setInfoTextBackgroundColor(color: Int): CoachMarkConfig {
        mInfoBuilder.setBackgroundColor(color)
        return this
    }

    fun setInfoTextTypeface(typeface: TypeFace) {
        mInfoBuilder.setFontTypeFace(typeface)
    }

    fun setInfoTextStyle(style: Typeface) {
        mInfoBuilder.setFontStyle(style)
    }

    fun setInfoTextDrawable(drawable: Drawable?): CoachMarkConfig {
        mInfoBuilder.setDrawable(drawable)
        mInfoBuilder.setDrawablePosition(Gravity.END)
        return this
    }

    fun setInfoTextColor(color: Int): CoachMarkConfig {
        mInfoBuilder.setTextColor(color)
        return this
    }

    fun setInfoTextSize(size: Float): CoachMarkConfig {
        mInfoBuilder.setTextSize(size)
        return this
    }

    fun setInfoTextMargin(left: Int, top: Int, right: Int, bottom: Int): CoachMarkConfig {
        mInfoBuilder.setMargin(left, top, right, bottom)
        return this
    }

    fun setInfoTextPadding(left: Int, top: Int, right: Int, bottom: Int): CoachMarkConfig {
        mInfoBuilder.setPadding(left, top, right, bottom)
        return this
    }

    fun setInfoCornerRadius(radius: Float): CoachMarkConfig {
        mInfoBuilder.setCornerRadius(radius)
        return this
    }

    fun setInfoSize(width: Int, height: Int): CoachMarkConfig {
        mInfoBuilder.setInfoViewWidth(width)
        mInfoBuilder.setInfoViewHeight(width)
        return this
    }

    fun setInfoGravity(gravity: Gravity): CoachMarkConfig {
        mInfoBuilder.setInfoViewGravity(gravity)
        return this
    }

    fun setOverlayTintColor(color: Int): CoachMarkConfig {
        mOverlayBuilder.setOverlayColor(color)
        return this
    }

    fun setOverlayTintOpacity(opacity: Float): CoachMarkConfig {
        mOverlayBuilder.setOverlayOpacity(opacity)
        return this
    }

    fun setOverlayTargetCoordinates(left: Int, top: Int, right: Int, bottom: Int): CoachMarkConfig {
        mOverlayBuilder.setOverlayTargetCoordinates(left, top, right, bottom)
        return this
    }

    fun setOverlayTransparentMargin(left: Int, top: Int, right: Int, bottom: Int): CoachMarkConfig {
        mOverlayBuilder.setOverlayTransparentMargin(left, top, right, bottom)
        return this
    }

    fun setOverlayTransparentPadding(left: Int, top: Int, right: Int, bottom: Int): CoachMarkConfig {
        mOverlayBuilder.setOverlayTransparentPadding(left, top, right, bottom)
        return this
    }

    fun setOverLayType(type: ShapeType): CoachMarkConfig {
        mOverlayBuilder.setOverLayType(type)
        return this
    }

    fun setOverlayTransparentGravity(gravity: Gravity): CoachMarkConfig {
        mOverlayBuilder.setOverlayTransparentGravity(gravity)
        return this
    }

    fun setOverlayTransparentCornerRadius(radius: Float): CoachMarkConfig {
        mOverlayBuilder.setOverlayTransparentCornerRadius(radius)
        return this
    }

    fun setOverlayTransparentCircleRadius(radius: Float): CoachMarkConfig {
        mOverlayBuilder.setOverlayTransparentCircleRadius(radius)
        return this
    }

    fun setOverlayTransparentShape(shape: Shape): CoachMarkConfig {
        mOverlayBuilder.setOverlayTransparentShape(shape)
        return this
    }

    fun setCoachMarkToolTipShape(shape: Shape): CoachMarkConfig {
        mToolTipBuilder?.apply {
            setToolTipShape(shape)
        }
        return this
    }

    fun setCoachMarkToolTipColor(color: Int): CoachMarkConfig {
        mToolTipBuilder?.apply { setToolTipColor(color) }
        return this
    }

    fun setCoachMarkToolTipSize(width: Int, height: Int): CoachMarkConfig {
        mToolTipBuilder?.apply { setToolTipSize(width, height) }
        return this
    }

    fun setCoachMarkToolTipOrientation(orientation: Orientation): CoachMarkConfig {
        mToolTipBuilder?.apply { setToolTipOrientation(orientation) }
        return this
    }

    fun showCoachMarkToolTip(boolean: Boolean): CoachMarkConfig {
        mShowToolTip = boolean
        if (boolean) {
            mToolTipBuilder = CoachMarkInfoToolTip.Builder(mContext)
        }
        return this
    }

    fun attachInfoTextToTarget(boolean: Boolean): CoachMarkConfig {
        mInfoBuilder.setAttachToTarget(boolean)
        return this
    }

}