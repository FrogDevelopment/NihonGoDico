package fr.frogdevelopment.nihongo.dico.ui.main;

import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.TypefaceSpan;

public class CustomTypefaceSpan extends TypefaceSpan {

    private final Typeface newType;

    public CustomTypefaceSpan(Typeface type) {
        super("family");
        newType = type;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setTypeface(newType);
    }

    @Override
    public void updateMeasureState(TextPaint paint) {
        paint.setTypeface(newType);
    }

}
