package com.calculator.ui.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.calculator.R;
import com.calculator.model.HistoryRecord;
import com.calculator.ui.widget.DeleteConfirmationDialog;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * History bottom sheet dialog
 */
public class HistoryBottomSheet extends BottomSheetDialogFragment {

    private static final float PARTIAL_HEIGHT_RATIO = 0.4f;
    private static final float FULL_HEIGHT_RATIO = 0.9f;

    private RecyclerView recyclerView;
    private HistoryAdapter adapter;
    private TextView emptyText;
    private LinearLayout editActionsLayout;
    private Button editButton;
    private Button selectAllButton;
    private Button deselectAllButton;
    private Button deleteButton;

    private List<HistoryRecord> historyRecords = new ArrayList<>();
    private OnHistoryActionListener actionListener;

    public interface OnHistoryActionListener {
        List<HistoryRecord> onLoadHistory();
        void onHistoryRecordClick(HistoryRecord record);
        void onDeleteRecords(List<Long> ids);
    }

    public void setOnHistoryActionListener(OnHistoryActionListener listener) {
        this.actionListener = listener;
    }

    public void setHistoryRecords(List<HistoryRecord> records) {
        this.historyRecords = records != null ? records : new ArrayList<>();
        if (adapter != null) {
            adapter.setRecords(this.historyRecords);
            updateEmptyState();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.HistoryBottomSheet);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.historyRecyclerView);
        emptyText = view.findViewById(R.id.emptyText);
        editActionsLayout = view.findViewById(R.id.editActionsLayout);
        editButton = view.findViewById(R.id.editButton);
        selectAllButton = view.findViewById(R.id.selectAllButton);
        deselectAllButton = view.findViewById(R.id.deselectAllButton);
        deleteButton = view.findViewById(R.id.deleteButton);

        setupRecyclerView();
        setupButtons();
        loadHistory();
    }

    private void setupRecyclerView() {
        adapter = new HistoryAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener((record, position) -> {
            if (actionListener != null) {
                actionListener.onHistoryRecordClick(record);
            }
            dismiss();
        });

        adapter.setOnItemLongClickListener((record, position) -> {
            enterEditMode();
        });
    }

    private void setupButtons() {
        editButton.setOnClickListener(v -> {
            if (adapter.isEditMode()) {
                exitEditMode();
            } else {
                enterEditMode();
            }
        });

        selectAllButton.setOnClickListener(v -> adapter.selectAll());
        deselectAllButton.setOnClickListener(v -> adapter.deselectAll());
        deleteButton.setOnClickListener(v -> showDeleteConfirmation());
    }

    private void loadHistory() {
        if (actionListener != null) {
            List<HistoryRecord> records = actionListener.onLoadHistory();
            setHistoryRecords(records);
        }
    }

    private void updateEmptyState() {
        if (adapter.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyText.setVisibility(View.VISIBLE);
            editButton.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyText.setVisibility(View.GONE);
            editButton.setVisibility(View.VISIBLE);
        }
    }

    public void enterEditMode() {
        adapter.setEditMode(true);
        editButton.setText(R.string.history_done);
        editActionsLayout.setVisibility(View.VISIBLE);
        deselectAllButton.setVisibility(View.GONE);
    }

    public void exitEditMode() {
        adapter.setEditMode(false);
        editButton.setText(R.string.history_edit);
        editActionsLayout.setVisibility(View.GONE);
    }

    public void selectAll() {
        adapter.selectAll();
    }

    public void deselectAll() {
        adapter.deselectAll();
    }

    private void showDeleteConfirmation() {
        Set<Long> selectedIds = adapter.getSelectedIds();
        if (selectedIds.isEmpty()) {
            return;
        }

        DeleteConfirmationDialog dialog = new DeleteConfirmationDialog(requireContext());
        dialog.setMessage(getString(R.string.history_delete_confirm));
        dialog.setOnConfirmListener(new DeleteConfirmationDialog.OnConfirmListener() {
            @Override
            public void onConfirm() {
                if (actionListener != null) {
                    actionListener.onDeleteRecords(new ArrayList<>(selectedIds));
                }
                loadHistory();
                exitEditMode();
            }

            @Override
            public void onCancel() {
                // Do nothing
            }
        });
        dialog.show();
    }

    public void expandToFullHeight() {
        // Expand to 90% of screen height
        View bottomSheet = getDialog().findViewById(com.google.android.material.R.id.design_bottom_sheet);
        if (bottomSheet != null) {
            BottomSheetBehavior<?> behavior = BottomSheetBehavior.from(bottomSheet);
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    public void collapseToPartialHeight() {
        // Collapse to 40% of screen height
        View bottomSheet = getDialog().findViewById(com.google.android.material.R.id.design_bottom_sheet);
        if (bottomSheet != null) {
            BottomSheetBehavior<?> behavior = BottomSheetBehavior.from(bottomSheet);
            behavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
        }
    }
}
