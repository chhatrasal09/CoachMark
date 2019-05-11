package com.android.app.coachmark

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.android.library.coachmark.components.*
import com.android.library.coachmark.components.listener.SequenceListener
import com.android.library.coachmark.configuration.*
import com.android.library.coachmark.utility.TypeFace
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        start_coach_mark?.setOnClickListener {
            val coachMarkSequence = CoachMarkSequence(this)
            val coachMarkBuilder = CoachMarkOverlay.Builder(this)
                .setOverlayTargetView(button_1)
                .setInfoViewBuilder(
                    CoachMarkInfo.Builder(this)
                        .setInfoText(this.getString(R.string.info_text_message))
                        .setMargin(30, 30, 30, 30)
                )
                .setSkipButtonBuilder(
                    CoachMarkSkipButton.Builder(this)
                        .setButtonClickListener(object : CoachMarkSkipButton.ButtonClickListener {
                            override fun onSkipButtonClick(view: View) {
                                (window.decorView as ViewGroup).removeView(view)
                                coachMarkSequence.clearList()
                                start_coach_mark?.setText(R.string.start_coach_mark)
                            }
                        })
                )
            coachMarkSequence.addItem(coachMarkBuilder, true)

            val coachMarkConfig = CoachMarkConfig(this)
            coachMarkConfig.setInfoTextMargin(20, 20, 20, 0)
            coachMarkSequence.setSequenceConfig(coachMarkConfig)
            coachMarkConfig.setInfoTextTypeface(TypeFace.BOLD)
            coachMarkSequence.addItem(button_2, "Button 2")

            coachMarkConfig.showCoachMarkToolTip(boolean = true)
            coachMarkConfig.setInfoTextTypeface(TypeFace.ITALIC)
            coachMarkSequence.addItem(button_3, "Button 3")

            coachMarkConfig.setInfoTextTypeface(TypeFace.NORMAL)
            coachMarkSequence.setSequenceListener(object : SequenceListener {
                override fun onNextItem(coachMark: CoachMarkOverlay, coachMarkSeq: CoachMarkSequence) {
                    super.onNextItem(coachMark, coachMarkSequence)
                    if (coachMarkSeq.getRemainingListCount() == 0) {
                        start_coach_mark?.setText(R.string.finish_button_text)
                    }
                }
            })
            coachMarkConfig.setInfoTextBackgroundColor(Color.CYAN)
            coachMarkConfig.setCoachMarkToolTipColor(Color.CYAN)
            coachMarkSequence.addItem(start_coach_mark, this.getString(R.string.exit_info_text_message))
            start_coach_mark?.setText(R.string.continue_button_text)
            coachMarkSequence.start(window?.decorView as ViewGroup)
        }

    }
}
