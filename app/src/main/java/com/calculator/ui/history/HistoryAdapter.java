package com.calculator.ui.history;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.calculator.R;
import com.calculator.model.HistoryRecord;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Adapter for history record list
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryViewHolder> {

    private List<HistoryRecord> records = new ArrayList<>();
    private final Set<Long> selectedIds = new HashSet<>();
    private boolean editMode = false;
    private OnItemClickListener itemClickListener;
    private OnItemLongClickListener itemLongClickListener;

    public interface OnItemClickListener {
        void onItemClick(HistoryRecord record, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(HistoryRecord record, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.itemLongClickListener = listener;
    }

    public void setRecords(List<HistoryRecord> records) {
        this.records = records != null ? records : new ArrayList<>();
        notifyDataSetChanged();
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
        if (!editMode) {
            selectedIds.clear();
        }
        notifyDataSetChanged();
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void toggleSelection(long id) {
        if (selectedIds.contains(id)) {
            selectedIds.remove(id);
        } else {
            selectedIds.add(id);
        }
        notifyItemChanged(findPositionById(id));
    }

    public void selectAll() {
        for (HistoryRecord record : records) {
            selectedIds.add(record.getId());
        }
        notifyDataSetChanged();
    }

    public void deselectAll() {
        selectedIds.clear();
        notifyDataSetChanged();
    }

    public Set<Long> getSelectedIds() {
        return new HashSet<>(selectedIds);
    }

    public int getSelectedCount() {
        return selectedIds.size();
    }

    public boolean hasSelection() {
        return !selectedIds.isEmpty();
    }

    private int findPositionById(long id) {
        for (int i = 0; i < records.size(); i++) {
            if (records.get(i).getId() == id) {
                return i;
            }
        }
        return -1;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history_record, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        HistoryRecord record = records.get(position);
        boolean isSelected = selectedIds.contains(record.getId());
        holder.bind(record, editMode, isSelected);

        holder.itemView.setOnClickListener(v -> {
            if (editMode) {
                toggleSelection(record.getId());
            } else if (itemClickListener != null) {
                itemClickListener.onItemClick(record, position);
            }
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (!editMode && itemLongClickListener != null) {
                itemLongClickListener.onItemLongClick(record, position);
                return true;
            }
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    public boolean isEmpty() {
        return records.isEmpty();
    }
}
