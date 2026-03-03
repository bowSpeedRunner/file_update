package com.calculator.ui.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.calculator.R;
import com.calculator.model.CalculatorMode;

/**
 * Mode selector popup window
 */
public class ModeSelectorPopup {

    private final PopupWindow popupWindow;
    private OnModeSelectedListener listener;

    public interface OnModeSelectedListener {
        void onModeSelected(CalculatorMode mode);
    }

    public ModeSelectorPopup(Context context) {
        View contentView = LayoutInflater.from(context).inflate(R.layout.popup_mode_selector, null);

        TextView basicItem = contentView.findViewById(R.id.basicModeItem);
        TextView scientificItem = contentView.findViewById(R.id.scientificModeItem);

        basicItem.setOnClickListener(v -> {
            if (listener != null) {
                listener.onModeSelected(CalculatorMode.BASIC);
            }
            dismiss();
        });

        scientificItem.setOnClickListener(v -> {
            if (listener != null) {
                listener.onModeSelected(CalculatorMode.SCIENTIFIC);
            }
            dismiss();
        });

        popupWindow = new PopupWindow(
                contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
        );

        popupWindow.setOutsideTouchable(true);
        popupWindow.setElevation(8);
    }

    public void setOnModeSelectedListener(OnModeSelectedListener listener) {
        this.listener = listener;
    }

    public void show(View anchor) {
        popupWindow.showAsDropDown(anchor, 0, 0);
    }

    public void dismiss() {
        popupWindow.dismiss();
    }

    public boolean isShowing() {
        return popupWindow.isShowing();
    }
}
