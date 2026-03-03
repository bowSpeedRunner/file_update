package com.calculator.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.calculator.R;

/**
 * Delete confirmation dialog
 */
public class DeleteConfirmationDialog extends Dialog {

    private String message;
    private OnConfirmListener listener;

    public interface OnConfirmListener {
        void onConfirm();
        void onCancel();
    }

    public DeleteConfirmationDialog(@NonNull Context context) {
        super(context);
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setOnConfirmListener(OnConfirmListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_delete_confirmation);

        TextView messageText = findViewById(R.id.messageText);
        Button cancelButton = findViewById(R.id.cancelButton);
        Button confirmButton = findViewById(R.id.confirmButton);

        if (message != null) {
            messageText.setText(message);
        }

        cancelButton.setOnClickListener(v -> {
            dismiss();
            if (listener != null) {
                listener.onCancel();
            }
        });

        confirmButton.setOnClickListener(v -> {
            dismiss();
            if (listener != null) {
                listener.onConfirm();
            }
        });
    }
}
