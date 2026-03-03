package com.calculator.ui.main;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.calculator.R;

/**
 * Custom display view for calculator
 * Contains expression display and result display
 */
public class CalculatorDisplayView extends LinearLayout {

    private CursorEditText expressionEditText;
    private TextView resultTextView;
    private ImageButton historyButton;
    private ImageButton modeButton;
    private ImageButton deleteButton;

    private OnDisplayActionListener actionListener;

    public interface OnDisplayActionListener {
        void onHistoryClick();
        void onModeClick();
        void onDeleteClick();
        void onDeleteLongClick();
    }

    public CalculatorDisplayView(Context context) {
        super(context);
        init(context);
    }

    public CalculatorDisplayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CalculatorDisplayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setOrientation(VERTICAL);

        // Inflate layout
        View view = LayoutInflater.from(context).inflate(R.layout.view_calculator_display, this, true);

        expressionEditText = view.findViewById(R.id.expressionEditText);
        resultTextView = view.findViewById(R.id.resultTextView);
        historyButton = view.findViewById(R.id.historyButton);
        modeButton = view.findViewById(R.id.modeButton);
        deleteButton = view.findViewById(R.id.deleteButton);

        // Setup click listeners
        historyButton.setOnClickListener(v -> {
            if (actionListener != null) {
                actionListener.onHistoryClick();
            }
        });

        modeButton.setOnClickListener(v -> {
            if (actionListener != null) {
                actionListener.onModeClick();
            }
        });

        deleteButton.setOnClickListener(v -> {
            if (actionListener != null) {
                actionListener.onDeleteClick();
            }
        });

        deleteButton.setOnLongClickListener(v -> {
            if (actionListener != null) {
                actionListener.onDeleteLongClick();
            }
            return true;
        });
    }

    /**
     * Set expression text
     */
    public void setExpression(String expression) {
        expressionEditText.setText(expression);
        // Move cursor to end
        if (expression != null) {
            expressionEditText.setSelection(expression.length());
        }
    }

    /**
     * Get current expression
     */
    public String getExpression() {
        return expressionEditText.getText().toString();
    }

    /**
     * Set result text
     */
    public void setResult(String result) {
        resultTextView.setText(result);
    }

    /**
     * Set result text color
     */
    public void setResultColor(int color) {
        resultTextView.setTextColor(color);
    }

    /**
     * Show/hide delete button
     */
    public void setDeleteButtonVisible(boolean visible) {
        deleteButton.setVisibility(visible ? VISIBLE : INVISIBLE);
    }

    /**
     * Set action listener
     */
    public void setOnDisplayActionListener(OnDisplayActionListener listener) {
        this.actionListener = listener;
    }

    /**
     * Get cursor position
     */
    public int getCursorPosition() {
        return expressionEditText.getSelectionStart();
    }

    /**
     * Set cursor position
     */
    public void setCursorPosition(int position) {
        expressionEditText.setSelection(Math.max(0, Math.min(position, expressionEditText.getText().length())));
    }

    /**
     * Enable/disable cursor blinking
     */
    public void setCursorBlinkEnabled(boolean enabled) {
        expressionEditText.setBlinkEnabled(enabled);
    }
}
