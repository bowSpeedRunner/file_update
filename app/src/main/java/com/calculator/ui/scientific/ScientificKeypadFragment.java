package com.calculator.ui.scientific;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.calculator.R;
import com.calculator.ui.widget.CalculatorButton;

/**
 * Scientific calculator keypad fragment
 */
public class ScientificKeypadFragment extends Fragment {

    private OnKeypadClickListener listener;

    public interface OnKeypadClickListener {
        void onDigitClick(char digit);
        void onOperatorClick(char operator);
        void onEqualClick();
        void onClearClick();
        void onDeleteClick();
        void onNegationClick();
        void onPercentClick();
        void onDecimalClick();
        void onFunctionClick(String function);
        void onConstantClick(String constant);
        void onParenthesisClick(boolean isOpen);
    }

    public ScientificKeypadFragment() {
        // Required empty public constructor
    }

    public void setOnKeypadClickListener(OnKeypadClickListener listener) {
        this.listener = listener;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_scientific_keypad, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Number buttons
        setupDigitButton(view, R.id.btn0, '0');
        setupDigitButton(view, R.id.btn1, '1');
        setupDigitButton(view, R.id.btn2, '2');
        setupDigitButton(view, R.id.btn3, '3');
        setupDigitButton(view, R.id.btn4, '4');
        setupDigitButton(view, R.id.btn5, '5');
        setupDigitButton(view, R.id.btn6, '6');
        setupDigitButton(view, R.id.btn7, '7');
        setupDigitButton(view, R.id.btn8, '8');
        setupDigitButton(view, R.id.btn9, '9');

        // Operator buttons
        setupOperatorButton(view, R.id.btnAdd, '+');
        setupOperatorButton(view, R.id.btnSubtract, '−');
        setupOperatorButton(view, R.id.btnMultiply, '×');
        setupOperatorButton(view, R.id.btnDivide, '÷');

        // Special buttons
        setupActionButton(view, R.id.btnEqual, () -> {
            if (listener != null) listener.onEqualClick();
        });

        setupActionButton(view, R.id.btnClear, () -> {
            if (listener != null) listener.onClearClick();
        });

        setupActionButton(view, R.id.btnNegate, () -> {
            if (listener != null) listener.onNegationClick();
        });

        setupActionButton(view, R.id.btnPercent, () -> {
            if (listener != null) listener.onPercentClick();
        });

        setupActionButton(view, R.id.btnDecimal, () -> {
            if (listener != null) listener.onDecimalClick();
        });

        // Scientific function buttons
        setupFunctionButton(view, R.id.btnSin, "sin");
        setupFunctionButton(view, R.id.btnCos, "cos");
        setupFunctionButton(view, R.id.btnTan, "tan");
        setupFunctionButton(view, R.id.btnLn, "ln");
        setupFunctionButton(view, R.id.btnLog, "log");
        setupFunctionButton(view, R.id.btnSqrt, "sqrt");
        setupFunctionButton(view, R.id.btnPower, "^");
        setupFunctionButton(view, R.id.btnFactorial, "!");

        // Constant buttons
        setupConstantButton(view, R.id.btnPi, "π");
        setupConstantButton(view, R.id.btnE, "e");

        // Parenthesis buttons
        setupActionButton(view, R.id.btnLeftParen, () -> {
            if (listener != null) listener.onParenthesisClick(true);
        });

        setupActionButton(view, R.id.btnRightParen, () -> {
            if (listener != null) listener.onParenthesisClick(false);
        });
    }

    private void setupDigitButton(View parent, int buttonId, char digit) {
        CalculatorButton button = parent.findViewById(buttonId);
        if (button != null) {
            button.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDigitClick(digit);
                }
            });
        }
    }

    private void setupOperatorButton(View parent, int buttonId, char operator) {
        CalculatorButton button = parent.findViewById(buttonId);
        if (button != null) {
            button.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onOperatorClick(operator);
                }
            });
        }
    }

    private void setupActionButton(View parent, int buttonId, Runnable action) {
        View button = parent.findViewById(buttonId);
        if (button != null) {
            button.setOnClickListener(v -> action.run());
        }
    }

    private void setupFunctionButton(View parent, int buttonId, String function) {
        View button = parent.findViewById(buttonId);
        if (button != null) {
            button.setOnClickListener(v -> {
                if (listener != null) {
                    if (function.equals("^") || function.equals("!")) {
                        // These are operators, insert directly
                        listener.onOperatorClick(function.charAt(0));
                    } else {
                        listener.onFunctionClick(function);
                    }
                }
            });
        }
    }

    private void setupConstantButton(View parent, int buttonId, String constant) {
        View button = parent.findViewById(buttonId);
        if (button != null) {
            button.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onConstantClick(constant);
                }
            });
        }
    }
}
