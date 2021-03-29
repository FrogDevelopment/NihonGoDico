package com.frogdevelopment.nihongo.dico.ui.main

import android.graphics.Typeface
import android.text.TextPaint
import android.text.style.TypefaceSpan

class CustomTypefaceSpan(private val newType: Typeface) : TypefaceSpan("japanese") {

    override fun updateDrawState(ds: TextPaint) {
        ds.typeface = newType
    }

    override fun updateMeasureState(paint: TextPaint) {
        paint.typeface = newType
    }

}