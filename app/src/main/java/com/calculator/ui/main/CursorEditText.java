package com.calculator.ui.main;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;

import androidx.appcompat.widget.AppCompatEditText;

import com.calculator.R;

/**
 * Custom EditText with blinking blue cursor
 */
public class CursorEditText extends AppCompatEditText {

    private static final int BLINK_DURATION = 500; // ms
    private static final float CURSOR_WIDTH = 2; // dp

    private Paint cursorPaint;
    private boolean cursorVisible = true;
    private boolean blinkEnabled = true;
    private final Handler blinkHandler = new Handler(Looper.getMainLooper());
    private final Runnable blinkRunnable = new Runnable() {
        @Override
        public void run() {
            if (blinkEnabled && hasFocus()) {
                cursorVisible = !cursorVisible;
                invalidate();
                blinkHandler.postDelayed(this, BLINK_DURATION);
            }
        }
    };

    private OnSelectionChangedListener selectionChangedListener;

    public interface OnSelectionChangedListener {
        void onSelectionChanged(int selStart, int selEnd);
    }

    public CursorEditText(Context context) {
        super(context);
        init();
    }

    public CursorEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CursorEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        cursorPaint = new Paint();
        cursorPaint.setColor(getResources().getColor(R.color.cursor_blue, null));
        cursorPaint.setStyle(Paint.Style.FILL);
        cursorPaint.setStrokeWidth(CURSOR_WIDTH * getResources().getDisplayMetrics().density);

        // Disable default cursor
        setCursorVisible(false);

        // Add text change listener
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Ensure cursor is at end after text change
                if (getSelectionStart() != getText().length()) {
                    setSelection(getText().length());
                }
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw custom cursor
        if (hasFocus() && cursorVisible && getText() != null) {
            int cursorPos = getSelectionStart();
            if (cursorPos >= 0) {
                // Calculate cursor position
                String textBeforeCursor = getText().toString().substring(0, cursorPos);
                float cursorX = getPaint().measureText(textBeforeCursor) + getPaddingLeft();

                // Calculate cursor height
                Paint.FontMetrics fm = getPaint().getFontMetrics();
                float cursorHeight = fm.bottom - fm.top;
                float cursorY = (getHeight() - cursorHeight) / 2;

                canvas.drawRect(
                        cursorX,
                        cursorY,
                        cursorX + CURSOR_WIDTH * getResources().getDisplayMetrics().density,
                        cursorY + cursorHeight,
                        cursorPaint
                );
            }
        }
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);

        if (focused) {
            startBlinking();
        } else {
            stopBlinking();
        }
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        super.onSelectionChanged(selStart, selEnd);
        if (selectionChangedListener != null) {
            selectionChangedListener.onSelectionChanged(selStart, selEnd);
        }
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        return super.onKeyPreIme(keyCode, event);
    }

    private void startBlinking() {
        blinkEnabled = true;
        cursorVisible = true;
        blinkHandler.removeCallbacks(blinkRunnable);
        blinkHandler.postDelayed(blinkRunnable, BLINK_DURATION);
    }

    private void stopBlinking() {
        blinkEnabled = false;
        cursorVisible = false;
        blinkHandler.removeCallbacks(blinkRunnable);
        invalidate();
    }

    public void setBlinkEnabled(boolean enabled) {
        this.blinkEnabled = enabled;
        if (enabled && hasFocus()) {
            startBlinking();
        } else {
            stopBlinking();
        }
    }

    public void setCursorColor(int color) {
        cursorPaint.setColor(color);
        invalidate();
    }

    public void setOnSelectionChangedListener(OnSelectionChangedListener listener) {
        this.selectionChangedListener = listener;
    }
}
