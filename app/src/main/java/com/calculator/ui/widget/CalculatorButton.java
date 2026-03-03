package com.calculator.ui.widget;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.appcompat.widget.AppCompatButton;

import com.calculator.R;

/**
 * Custom calculator button with circle or capsule shape
 */
public class CalculatorButton extends AppCompatButton {

    public enum ButtonType {
        CIRCLE,
        CAPSULE
    }

    public enum ButtonStyle {
        NUMBER,
        OPERATOR,
        EQUAL,
        SCIENTIFIC
    }

    private ButtonType buttonType = ButtonType.CIRCLE;
    private ButtonStyle buttonStyle = ButtonStyle.NUMBER;
    private static final float PRESSED_SCALE = 0.95f;
    private static final int ANIM_DURATION = 100;

    public CalculatorButton(Context context) {
        super(context);
        init(context, null);
    }

    public CalculatorButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CalculatorButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CalculatorButton);
            String typeStr = a.getString(R.styleable.CalculatorButton_calcButtonType);
            String styleStr = a.getString(R.styleable.CalculatorButton_calcButtonStyle);

            if ("capsule".equalsIgnoreCase(typeStr)) {
                buttonType = ButtonType.CAPSULE;
            }

            if ("operator".equalsIgnoreCase(styleStr)) {
                buttonStyle = ButtonStyle.OPERATOR;
            } else if ("equal".equalsIgnoreCase(styleStr)) {
                buttonStyle = ButtonStyle.EQUAL;
            } else if ("scientific".equalsIgnoreCase(styleStr)) {
                buttonStyle = ButtonStyle.SCIENTIFIC;
            }

            a.recycle();
        }

        // Disable all caps for scientific functions
        setAllCaps(false);
    }

    public void setButtonType(ButtonType type) {
        this.buttonType = type;
        requestLayout();
        invalidate();
    }

    public void setButtonStyle(ButtonStyle style) {
        this.buttonStyle = style;
        updateBackground();
        invalidate();
    }

    private void updateBackground() {
        // Background is set via style, this is just for programmatic changes
    }

    /**
     * Animate button press with scale effect
     */
    public void animatePress() {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(this, "scaleX", 1f, PRESSED_SCALE, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(this, "scaleY", 1f, PRESSED_SCALE, 1f);

        AnimatorSet set = new AnimatorSet();
        set.playTogether(scaleX, scaleY);
        set.setDuration(ANIM_DURATION);
        set.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                animatePress();
                break;
        }
        return super.onTouchEvent(event);
    }
}
