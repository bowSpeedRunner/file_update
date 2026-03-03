package com.calculator.ui.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.calculator.calculator.ExpressionEvaluator;
import com.calculator.calculator.ExpressionFormatter;
import com.calculator.calculator.InputProcessor;
import com.calculator.calculator.InputValidator;
import com.calculator.calculator.NegationHandler;
import com.calculator.data.DataRepository;
import com.calculator.model.CalculatorMode;
import com.calculator.model.CalculatorState;
import com.calculator.model.HistoryRecord;

import java.util.List;

/**
 * Main ViewModel for calculator
 */
public class MainViewModel extends AndroidViewModel {

    private final DataRepository repository;
    private final ExpressionEvaluator evaluator;

    private final MutableLiveData<CalculatorState> state = new MutableLiveData<>();
    private final MutableLiveData<String> displayExpression = new MutableLiveData<>();
    private final MutableLiveData<String> displayResult = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isError = new MutableLiveData<>();
    private final MutableLiveData<CalculatorMode> currentMode = new MutableLiveData<>();
    private final MutableLiveData<Boolean> showHistory = new MutableLiveData<>();
    private final MutableLiveData<HistoryRecord> loadFromHistory = new MutableLiveData<>();

    public MainViewModel(@NonNull Application application) {
        super(application);
        repository = DataRepository.getInstance(application);
        evaluator = new ExpressionEvaluator();

        // Load saved state
        loadSavedState();
    }

    private void loadSavedState() {
        CalculatorState savedState = repository.loadCurrentState();
        state.setValue(savedState);
        displayExpression.setValue(savedState.getContent());
        currentMode.setValue(savedState.getMode());
        isError.setValue(false);
        showHistory.setValue(false);
    }

    // Live data getters
    public LiveData<CalculatorState> getState() {
        return state;
    }

    public LiveData<String> getDisplayExpression() {
        return displayExpression;
    }

    public LiveData<String> getDisplayResult() {
        return displayResult;
    }

    public LiveData<Boolean> getIsError() {
        return isError;
    }

    public LiveData<CalculatorMode> getCurrentMode() {
        return currentMode;
    }

    public LiveData<Boolean> getShowHistory() {
        return showHistory;
    }

    public LiveData<HistoryRecord> getLoadFromHistory() {
        return loadFromHistory;
    }

    // User actions
    public void onDigitClick(char digit) {
        CalculatorState currentState = getCurrentStateValue();

        if (!InputProcessor.canAddPlaceholder(currentState.getContent())) {
            return;
        }

        currentState.appendCharacter(digit);
        updateState(currentState);
        clearResult();
    }

    public void onOperatorClick(char operator) {
        CalculatorState currentState = getCurrentStateValue();

        String processed = InputProcessor.processOperatorInput(currentState.getContent(), operator);
        currentState.setContent(processed);
        currentState.setCursorPosition(processed.length());

        updateState(currentState);
        clearResult();
    }

    public void onFunctionClick(String function) {
        CalculatorState currentState = getCurrentStateValue();

        // Insert function with parentheses
        String functionStr = function + "()";
        int insertPos = currentState.getCursorPosition();

        StringBuilder sb = new StringBuilder(currentState.getContent());
        sb.insert(insertPos, functionStr);
        currentState.setContent(sb.toString());
        // Position cursor inside parentheses
        currentState.setCursorPosition(insertPos + function.length() + 1);

        updateState(currentState);
        clearResult();
    }

    public void onEqualClick() {
        CalculatorState currentState = getCurrentStateValue();
        String expression = currentState.getContent();

        if (expression == null || expression.isEmpty()) {
            return;
        }

        ExpressionEvaluator.EvaluationResult result = evaluator.evaluate(expression);

        if (result.isSuccess()) {
            String formattedResult = ExpressionFormatter.formatResult(result.getValue());
            displayResult.setValue(formattedResult);
            isError.setValue(false);

            // Save to history
            HistoryRecord record = HistoryRecord.create(expression, formattedResult);
            repository.addHistoryRecord(record);
        } else {
            displayResult.setValue("Error");
            isError.setValue(true);
        }
    }

    public void onClearClick() {
        CalculatorState currentState = getCurrentStateValue();
        currentState.clear();
        updateState(currentState);
        displayResult.setValue("");
        isError.setValue(false);
    }

    public void onDeleteClick() {
        CalculatorState currentState = getCurrentStateValue();

        if (currentState.getCursorPosition() > 0) {
            currentState.deleteCharacter();
            updateState(currentState);
        }

        if (currentState.isEmpty()) {
            clearResult();
        }
    }

    public void onNegationClick() {
        CalculatorState currentState = getCurrentStateValue();
        String expression = currentState.getContent();
        int cursorPos = currentState.getCursorPosition();

        String toggled = NegationHandler.toggleNegation(expression, cursorPos);
        currentState.setContent(toggled);
        currentState.setCursorPosition(toggled.length());

        updateState(currentState);
        clearResult();
    }

    public void onConstantClick(String constant) {
        CalculatorState currentState = getCurrentStateValue();

        if (!InputProcessor.canAddPlaceholder(currentState.getContent())) {
            return;
        }

        currentState.appendString(constant);
        updateState(currentState);
        clearResult();
    }

    public void onPercentClick() {
        CalculatorState currentState = getCurrentStateValue();
        String expression = currentState.getContent();

        String processed = InputProcessor.handlePercent(expression);
        currentState.setContent(processed);
        currentState.setCursorPosition(processed.length());

        updateState(currentState);
    }

    public void onDecimalClick() {
        CalculatorState currentState = getCurrentStateValue();

        if (InputProcessor.canAddDecimalPoint(currentState.getContent(), currentState.getCursorPosition())) {
            currentState.appendCharacter('.');
            updateState(currentState);
        }
    }

    public void onParenthesisClick(boolean isOpen) {
        CalculatorState currentState = getCurrentStateValue();
        char paren = isOpen ? '(' : ')';

        if (InputValidator.canAcceptParenthesis(currentState.getContent(), currentState.getCursorPosition(), isOpen)) {
            currentState.appendCharacter(paren);
            updateState(currentState);
        }
    }

    public void switchMode(CalculatorMode mode) {
        CalculatorState currentState = getCurrentStateValue();
        currentState.setMode(mode);
        currentMode.setValue(mode);
        updateState(currentState);
    }

    public void loadFromHistory(HistoryRecord record) {
        CalculatorState currentState = getCurrentStateValue();
        currentState.setContent(record.getExpression());
        currentState.setCursorPosition(record.getExpression().length());
        displayResult.setValue(record.getResult());
        updateState(currentState);
    }

    public void showHistory() {
        showHistory.setValue(true);
    }

    public void hideHistory() {
        showHistory.setValue(false);
    }

    public void saveState() {
        CalculatorState currentState = getCurrentStateValue();
        repository.saveCurrentState(currentState);
    }

    // Helper methods
    private CalculatorState getCurrentStateValue() {
        CalculatorState currentState = state.getValue();
        if (currentState == null) {
            currentState = new CalculatorState();
        }
        return currentState.clone();
    }

    private void updateState(CalculatorState newState) {
        state.setValue(newState);
        displayExpression.setValue(newState.getContent());
    }

    private void clearResult() {
        displayResult.setValue("");
        isError.setValue(false);
    }

    public List<HistoryRecord> loadHistory() {
        return repository.loadHistory();
    }
}
