package com.calculator.ui.history;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.calculator.R;
import com.calculator.model.HistoryRecord;

/**
 * ViewHolder for history record item
 */
public class HistoryViewHolder extends RecyclerView.ViewHolder {

    private final TextView expressionText;
    private final TextView resultText;
    private final TextView timeText;
    private final CheckBox checkbox;

    private boolean editMode = false;

    public HistoryViewHolder(@NonNull View itemView) {
        super(itemView);
        expressionText = itemView.findViewById(R.id.expressionText);
        resultText = itemView.findViewById(R.id.resultText);
        timeText = itemView.findViewById(R.id.timeText);
        checkbox = itemView.findViewById(R.id.checkbox);
    }

    public void bind(HistoryRecord record, boolean editMode, boolean isSelected) {
        this.editMode = editMode;

        expressionText.setText(record.getExpression());
        resultText.setText(record.getResult());
        timeText.setText(record.getDisplayTime());

        if (editMode) {
            checkbox.setVisibility(View.VISIBLE);
            checkbox.setChecked(isSelected);
        } else {
            checkbox.setVisibility(View.GONE);
        }
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
        checkbox.setVisibility(editMode ? View.VISIBLE : View.GONE);
    }

    public void setSelected(boolean selected) {
        checkbox.setChecked(selected);
    }

    public boolean isSelected() {
        return checkbox.isChecked();
    }
}
