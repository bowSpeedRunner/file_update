package com.calculator.model;

import java.io.Serializable;

/**
 * Calculator state model
 * Holds the current input expression, mode, and cursor position
 */
public class CalculatorState implements Serializable {
    private static final long serialVersionUID = 1L;

    private String content;
    private CalculatorMode mode;
    private int cursorPosition;

    public CalculatorState() {
        this.content = "";
        this.mode = CalculatorMode.BASIC;
        this.cursorPosition = 0;
    }

    public CalculatorState(String content, CalculatorMode mode, int cursorPosition) {
        this.content = content != null ? content : "";
        this.mode = mode != null ? mode : CalculatorMode.BASIC;
        this.cursorPosition = cursorPosition;
    }

    // Getters
    public String getContent() {
        return content;
    }

    public CalculatorMode getMode() {
        return mode;
    }

    public int getCursorPosition() {
        return cursorPosition;
    }

    // Setters
    public void setContent(String content) {
        this.content = content != null ? content : "";
    }

    public void setMode(CalculatorMode mode) {
        this.mode = mode != null ? mode : CalculatorMode.BASIC;
    }

    public void setCursorPosition(int cursorPosition) {
        this.cursorPosition = Math.max(0, Math.min(cursorPosition, content.length()));
    }

    // Core methods
    public void appendCharacter(char c) {
        StringBuilder sb = new StringBuilder(content);
        sb.insert(cursorPosition, c);
        content = sb.toString();
        cursorPosition++;
    }

    public void appendString(String str) {
        StringBuilder sb = new StringBuilder(content);
        sb.insert(cursorPosition, str);
        content = sb.toString();
        cursorPosition += str.length();
    }

    public void deleteCharacter() {
        if (cursorPosition > 0 && content.length() > 0) {
            StringBuilder sb = new StringBuilder(content);
            sb.deleteCharAt(cursorPosition - 1);
            content = sb.toString();
            cursorPosition--;
        }
    }

    public void clear() {
        content = "";
        cursorPosition = 0;
    }

    public void toggleNegation() {
        // This is handled by NegationHandler
    }

    public int length() {
        return content.length();
    }

    public boolean isEmpty() {
        return content.isEmpty();
    }

    public char charAt(int index) {
        return content.charAt(index);
    }

    @Override
    public CalculatorState clone() {
        return new CalculatorState(content, mode, cursorPosition);
    }
}
