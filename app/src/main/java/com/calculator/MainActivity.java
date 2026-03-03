package com.calculator;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.calculator.model.CalculatorMode;
import com.calculator.model.HistoryRecord;
import com.calculator.ui.basic.BasicKeypadFragment;
import com.calculator.ui.history.HistoryBottomSheet;
import com.calculator.ui.main.CalculatorDisplayView;
import com.calculator.ui.main.MainViewModel;
import com.calculator.ui.scientific.ScientificKeypadFragment;
import com.calculator.ui.widget.ModeSelectorPopup;

import java.util.List;

/**
 * Main Activity for the calculator app
 */
public class MainActivity extends AppCompatActivity {

    private MainViewModel viewModel;
    private CalculatorDisplayView displayView;
    private View keypadContainer;

    private BasicKeypadFragment basicKeypadFragment;
    private ScientificKeypadFragment scientificKeypadFragment;
    private HistoryBottomSheet historyBottomSheet;
    private ModeSelectorPopup modeSelectorPopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        initViews();
        setupObservers();
        setupFragments();
        loadInitialMode();
    }

    private void initViews() {
        displayView = findViewById(R.id.displayView);
        keypadContainer = findViewById(R.id.keypadContainer);

        displayView.setOnDisplayActionListener(new CalculatorDisplayView.OnDisplayActionListener() {
            @Override
            public void onHistoryClick() {
                showHistory();
            }

            @Override
            public void onModeClick() {
                showModeSelector();
            }

            @Override
            public void onDeleteClick() {
                viewModel.onDeleteClick();
            }

            @Override
            public void onDeleteLongClick() {
                viewModel.onClearClick();
            }
        });
    }

    private void setupObservers() {
        viewModel.getDisplayExpression().observe(this, expression -> {
            displayView.setExpression(expression);
            displayView.setDeleteButtonVisible(expression != null && !expression.isEmpty());
        });

        viewModel.getDisplayResult().observe(this, result -> {
            if (result != null && !result.isEmpty()) {
                displayView.setResult(result);
                displayView.setResultColor(
                        viewModel.getIsError().getValue() != null && viewModel.getIsError().getValue()
                                ? getColor(R.color.error_text)
                                : getColor(R.color.result_text)
                );
            } else {
                displayView.setResult("");
            }
        });

        viewModel.getCurrentMode().observe(this, this::switchKeypad);

        viewModel.getShowHistory().observe(this, show -> {
            if (show != null && show) {
                showHistory();
            }
        });
    }

    private void setupFragments() {
        basicKeypadFragment = new BasicKeypadFragment();
        scientificKeypadFragment = new ScientificKeypadFragment();

        basicKeypadFragment.setOnKeypadClickListener(createBasicKeypadListener());
        scientificKeypadFragment.setOnKeypadClickListener(createScientificKeypadListener());
    }

    private void loadInitialMode() {
        CalculatorMode mode = viewModel.getCurrentMode().getValue();
        if (mode == null) {
            mode = CalculatorMode.BASIC;
        }
        switchKeypad(mode);
    }

    private void switchKeypad(CalculatorMode mode) {
        Fragment fragment;
        if (mode == CalculatorMode.SCIENTIFIC) {
            fragment = scientificKeypadFragment;
        } else {
            fragment = basicKeypadFragment;
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.keypadContainer, fragment);
        transaction.commit();
    }

    private BasicKeypadFragment.OnKeypadClickListener createBasicKeypadListener() {
        return new BasicKeypadFragment.OnKeypadClickListener() {
            @Override
            public void onDigitClick(char digit) {
                viewModel.onDigitClick(digit);
            }

            @Override
            public void onOperatorClick(char operator) {
                viewModel.onOperatorClick(operator);
            }

            @Override
            public void onEqualClick() {
                viewModel.onEqualClick();
            }

            @Override
            public void onClearClick() {
                viewModel.onClearClick();
            }

            @Override
            public void onDeleteClick() {
                viewModel.onDeleteClick();
            }

            @Override
            public void onNegationClick() {
                viewModel.onNegationClick();
            }

            @Override
            public void onPercentClick() {
                viewModel.onPercentClick();
            }

            @Override
            public void onDecimalClick() {
                viewModel.onDecimalClick();
            }
        };
    }

    private ScientificKeypadFragment.OnKeypadClickListener createScientificKeypadListener() {
        return new ScientificKeypadFragment.OnKeypadClickListener() {
            @Override
            public void onDigitClick(char digit) {
                viewModel.onDigitClick(digit);
            }

            @Override
            public void onOperatorClick(char operator) {
                viewModel.onOperatorClick(operator);
            }

            @Override
            public void onEqualClick() {
                viewModel.onEqualClick();
            }

            @Override
            public void onClearClick() {
                viewModel.onClearClick();
            }

            @Override
            public void onDeleteClick() {
                viewModel.onDeleteClick();
            }

            @Override
            public void onNegationClick() {
                viewModel.onNegationClick();
            }

            @Override
            public void onPercentClick() {
                viewModel.onPercentClick();
            }

            @Override
            public void onDecimalClick() {
                viewModel.onDecimalClick();
            }

            @Override
            public void onFunctionClick(String function) {
                viewModel.onFunctionClick(function);
            }

            @Override
            public void onConstantClick(String constant) {
                viewModel.onConstantClick(constant);
            }

            @Override
            public void onParenthesisClick(boolean isOpen) {
                viewModel.onParenthesisClick(isOpen);
            }
        };
    }

    private void showHistory() {
        if (historyBottomSheet != null && historyBottomSheet.isVisible()) {
            return;
        }

        historyBottomSheet = new HistoryBottomSheet();
        historyBottomSheet.setOnHistoryActionListener(new HistoryBottomSheet.OnHistoryActionListener() {
            @Override
            public List<HistoryRecord> onLoadHistory() {
                return viewModel.loadHistory();
            }

            @Override
            public void onHistoryRecordClick(HistoryRecord record) {
                viewModel.loadFromHistory(record);
            }

            @Override
            public void onDeleteRecords(List<Long> ids) {
                com.calculator.data.DataRepository.getInstance(MainActivity.this)
                        .deleteHistoryRecords(ids);
            }
        });

        historyBottomSheet.show(getSupportFragmentManager(), "history");
    }

    private void showModeSelector() {
        if (modeSelectorPopup != null && modeSelectorPopup.isShowing()) {
            modeSelectorPopup.dismiss();
            return;
        }

        modeSelectorPopup = new ModeSelectorPopup(this);
        modeSelectorPopup.setOnModeSelectedListener(mode -> viewModel.switchMode(mode));
        modeSelectorPopup.show(findViewById(R.id.displayView).findViewById(R.id.modeButton));
    }

    @Override
    protected void onPause() {
        super.onPause();
        viewModel.saveState();
    }
}
