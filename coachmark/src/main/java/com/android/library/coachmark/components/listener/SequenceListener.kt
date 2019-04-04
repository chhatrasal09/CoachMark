package com.android.library.coachmark.components.listener

import com.android.library.coachmark.components.CoachMarkOverlay
import com.android.library.coachmark.configuration.CoachMarkSequence

interface SequenceListener{
    fun onNextItem(coachMark : CoachMarkOverlay, coachMarkSequence : CoachMarkSequence){
        coachMarkSequence.setNextView()
    }
}