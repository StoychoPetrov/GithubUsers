package com.example.githubusers.globalClasses

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.transition.Slide
import android.transition.TransitionValues
import android.view.View
import android.view.ViewGroup

class SlideFadeOut : Slide() {

    init {
        propagation = null
    }

    // Create animator set from enter slide animator and fade in animator together
    override fun onAppear(sceneRoot: ViewGroup?, view: View?, startValues: TransitionValues?,
                          endValues: TransitionValues?): Animator {
        val slideAnimator = super.onAppear(sceneRoot, view, startValues, endValues)
        val fadeInAnimator = ObjectAnimator.ofFloat(view, View.ALPHA, 0f, 1f)

        return animatorSet(slideAnimator, fadeInAnimator)
    }

    // Create animator set from exit slide animator and fade in animator together
    override fun onDisappear(sceneRoot: ViewGroup?, view: View?, startValues: TransitionValues?,
                             endValues: TransitionValues?): Animator {
        val slideAnimator = super.onDisappear(sceneRoot, view, startValues, endValues)
        val fadeOutAnimator = ObjectAnimator.ofFloat(view, View.ALPHA, 1f, 0f)

        return animatorSet(slideAnimator, fadeOutAnimator)
    }

    private fun animatorSet(explodeAnimator: Animator, fadeAnimator: Animator): AnimatorSet {
        val animatorSet = AnimatorSet()
        animatorSet.play(explodeAnimator).with(fadeAnimator)
        return animatorSet
    }
}