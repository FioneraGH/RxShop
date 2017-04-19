package com.centling.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.KeyEvent;

/**
 * DetectPreImeEditText
 * Created by fionera on 17-3-31 in sweeping_robot.
 */

public class DetectPreImeEditText
        extends AppCompatEditText {
    public DetectPreImeEditText(Context context) {
        super(context);
    }

    public DetectPreImeEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DetectPreImeEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchKeyEventPreIme(KeyEvent event) {
        if(event.getKeyCode() == KeyEvent.KEYCODE_BACK){
            if (backPressListener != null) {
                backPressListener.onBackPress();
            }
        }
        return super.dispatchKeyEventPreIme(event);
    }

    public interface BackPressListener {
        void onBackPress();
    }

    private BackPressListener backPressListener;

    public void setBackPressListener(BackPressListener backPressListener) {
        this.backPressListener = backPressListener;
    }
}
