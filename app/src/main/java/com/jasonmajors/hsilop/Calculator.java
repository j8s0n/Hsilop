package com.jasonmajors.hsilop;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.math.BigIntegerMath;

import org.jetbrains.annotations.NotNull;

import java.util.Deque;
import java.util.Iterator;
import java.util.List;

import static com.jasonmajors.hsilop.Number.NEGATIVE_ONE;
import static com.jasonmajors.hsilop.Number.ONE;
import static com.jasonmajors.hsilop.Number.ZERO;

class Calculator {
  private static final int MAX_UNDO_HISTORY = 200;
  private final Deque<Number> stack = Lists.newLinkedList();
  private final Deque<List<HistoryEntry>> undoHistory = Lists.newLinkedList();

  private static final Calculator calculator = new Calculator();

  static Calculator getInstance() {
    return calculator;
  }

  private Calculator() {
  }

  void enter(@NotNull Number number) {
    addUndoItem(Lists.newArrayList(new HistoryEntry(ActionType.ADD, number)));
    stack.push(number);
  }

  synchronized void add() {
    checkSize(OperatorType.BINARY);
    Number x = stack.pop();
    Number y = stack.pop();
    Number z = y.add(x);
    stack.push(z);
    addUndoItem(Lists.newArrayList(new HistoryEntry(ActionType.REMOVE, x),
                                        new HistoryEntry(ActionType.REMOVE, y),
                                        new HistoryEntry(ActionType.ADD, z)));
  }

  synchronized void subtract() {
    checkSize(OperatorType.BINARY);
    Number x = stack.pop();
    Number y = stack.pop();
    Number z = y.subtract(x);
    stack.push(z);
    addUndoItem(Lists.newArrayList(new HistoryEntry(ActionType.REMOVE, x),
                                        new HistoryEntry(ActionType.REMOVE, y),
                                        new HistoryEntry(ActionType.ADD, z)));
  }

  synchronized void multiply() {
    checkSize(OperatorType.BINARY);
    Number x = stack.pop();
    Number y = stack.pop();
    Number z = y.multiply(x);
    stack.push(z);
    addUndoItem(Lists.newArrayList(new HistoryEntry(ActionType.REMOVE, x),
                                        new HistoryEntry(ActionType.REMOVE, y),
                                        new HistoryEntry(ActionType.ADD, z)));
  }

  synchronized void divide() {
    checkSize(OperatorType.BINARY);
    if (stack.peek().compareTo(ZERO) == 0) {
      throw new IllegalStateException("Divide by zero.");
    }

    Number x = stack.pop();
    Number y = stack.pop();
    Number z = y.divide(x);
    stack.push(z);
    addUndoItem(Lists.newArrayList(new HistoryEntry(ActionType.REMOVE, x),
                                        new HistoryEntry(ActionType.REMOVE, y),
                                        new HistoryEntry(ActionType.ADD, z)));
  }

  synchronized void power() {
    checkSize(OperatorType.BINARY);
    Number x = stack.pop();
    Number y = stack.pop();
    Number z = Number.valueOf(Math.pow(y.doubleValue(), x.doubleValue()));
    stack.push(z);
    addUndoItem(Lists.newArrayList(new HistoryEntry(ActionType.REMOVE, x),
                                        new HistoryEntry(ActionType.REMOVE, y),
                                        new HistoryEntry(ActionType.ADD, z)));
  }

  synchronized void square() {
    checkSize(OperatorType.UNARY);
    Number x = stack.pop();
    Number z = x.pow(2);
    stack.push(z);
    addUndoItem(Lists.newArrayList(new HistoryEntry(ActionType.REMOVE, x),
                                        new HistoryEntry(ActionType.ADD, z)));
  }

  synchronized void squareRoot() {
    checkSize(OperatorType.UNARY);
    if (stack.peek().compareTo(ZERO) < 0) {
      throw new IllegalStateException("Root of a negative.");
    }

    Number x = stack.pop();
    Number z = Number.valueOf(Math.sqrt(x.doubleValue()));
    stack.push(z);
    addUndoItem(Lists.newArrayList(new HistoryEntry(ActionType.REMOVE, x),
                                        new HistoryEntry(ActionType.ADD, z)));
  }

  synchronized void naturalLog() {
    checkSize(OperatorType.UNARY);
    if (stack.peek().compareTo(ZERO) < 0) {
      throw new IllegalStateException("Log of a negative.");
    }

    Number x = stack.pop();
    Number z = Number.valueOf(Math.log(x.doubleValue()));
    stack.push(z);
    addUndoItem(Lists.newArrayList(new HistoryEntry(ActionType.REMOVE, x),
                                        new HistoryEntry(ActionType.ADD, z)));
  }

  synchronized void eToTheX() {
    checkSize(OperatorType.UNARY);
    Number x = stack.pop();
    Number z = Number.valueOf(Math.exp(x.doubleValue()));
    stack.push(z);
    addUndoItem(Lists.newArrayList(new HistoryEntry(ActionType.REMOVE, x),
                                        new HistoryEntry(ActionType.ADD, z)));
  }

  synchronized void log10() {
    checkSize(OperatorType.UNARY);
    if (stack.peek().compareTo(ZERO) < 0) {
      throw new IllegalStateException("Log of a negative.");
    }

    Number x = stack.pop();
    Number z = Number.valueOf(Math.log10(x.doubleValue()));
    stack.push(z);
    addUndoItem(Lists.newArrayList(new HistoryEntry(ActionType.REMOVE, x),
                                        new HistoryEntry(ActionType.ADD, z)));
  }

  synchronized void tenToTheX() {
    checkSize(OperatorType.UNARY);
    Number x = stack.pop();
    Number z = Number.valueOf(Math.pow(10.0, x.doubleValue()));
    stack.push(z);
    addUndoItem(Lists.newArrayList(new HistoryEntry(ActionType.REMOVE, x),
                                        new HistoryEntry(ActionType.ADD, z)));
  }

  synchronized void sin(boolean radians) {
    checkSize(OperatorType.UNARY);
    Number x = stack.pop();
    Number z = Number.valueOf(Math.sin(convertToRadians(x.doubleValue(), radians)));
    stack.push(z);
    addUndoItem(Lists.newArrayList(new HistoryEntry(ActionType.REMOVE, x),
                                        new HistoryEntry(ActionType.ADD, z)));
  }

  synchronized void cos(boolean radians) {
    checkSize(OperatorType.UNARY);
    Number x = stack.pop();
    Number z = Number.valueOf(Math.cos(convertToRadians(x.doubleValue(), radians)));
    stack.push(z);
    addUndoItem(Lists.newArrayList(new HistoryEntry(ActionType.REMOVE, x),
                                        new HistoryEntry(ActionType.ADD, z)));
  }

  synchronized void tan(boolean radians) {
    checkSize(OperatorType.UNARY);
    Number x = stack.pop();
    Number z = Number.valueOf(Math.tan(convertToRadians(x.doubleValue(), radians)));
    stack.push(z);
    addUndoItem(Lists.newArrayList(new HistoryEntry(ActionType.REMOVE, x),
                                        new HistoryEntry(ActionType.ADD, z)));
  }

  synchronized void asin(boolean radians) {
    checkSize(OperatorType.UNARY);
    Number peek = stack.peek();
    if (peek.compareTo(NEGATIVE_ONE) < 0 || peek.compareTo(ONE) > 0) {
      throw new IllegalStateException("Invalid input.");
    }

    Number x = stack.pop();
    Number z = Number.valueOf(convertOutputAngle(Math.asin(x.doubleValue()), radians));
    stack.push(z);
    addUndoItem(Lists.newArrayList(new HistoryEntry(ActionType.REMOVE, x),
                                        new HistoryEntry(ActionType.ADD, z)));
  }

  synchronized void acos(boolean radians) {
    checkSize(OperatorType.UNARY);
    Number peek = stack.peek();
    if (peek.compareTo(NEGATIVE_ONE) < 0 || peek.compareTo(ONE) > 0) {
      throw new IllegalStateException("Invalid input.");
    }

    Number x = stack.pop();
    Number z = Number.valueOf(convertOutputAngle(Math.acos(x.doubleValue()), radians));
    stack.push(z);
    addUndoItem(Lists.newArrayList(new HistoryEntry(ActionType.REMOVE, x),
                                        new HistoryEntry(ActionType.ADD, z)));
  }

  synchronized void atan(boolean radians) {
    checkSize(OperatorType.UNARY);
    Number x = stack.pop();
    Number z = Number.valueOf(convertOutputAngle(Math.atan(x.doubleValue()), radians));
    stack.push(z);
    addUndoItem(Lists.newArrayList(new HistoryEntry(ActionType.REMOVE, x),
                                        new HistoryEntry(ActionType.ADD, z)));
  }

  private double convertToRadians(double value, boolean alreadyRadians) {
    if (alreadyRadians) {
      return value;
    }

    return Math.toRadians(value);
  }

  private double convertOutputAngle(double value, boolean toRadians) {
    if (toRadians) {
      return value;
    }

    return Math.toDegrees(value);
  }

  synchronized void oneOverX() {
    checkSize(OperatorType.UNARY);
    if (stack.peek().compareTo(ZERO) == 0) {
      throw new IllegalStateException("Divide by zero.");
    }

    Number x = stack.pop();
    Number z = ONE.divide(x);
    stack.push(z);
    addUndoItem(Lists.newArrayList(new HistoryEntry(ActionType.REMOVE, x),
                                        new HistoryEntry(ActionType.ADD, z)));
  }

  synchronized void factorial() {
    checkSize(OperatorType.UNARY);
    // n! = gamma(n + 1)
    Number x = stack.pop();
    Number z;

    // Zero.
    if (x.equals(ZERO)) {
      z = ONE;
    }
    // Integer.
    else if (x.isInteger()) {
      z = Number.valueOf(BigIntegerMath.factorial(x.intValue()).doubleValue());
    }
    // Other.
    else {
      z = Number.valueOf(gamma(x.doubleValue() + 1));
    }

    stack.push(z);
    addUndoItem(Lists.newArrayList(new HistoryEntry(ActionType.REMOVE, x),
                                        new HistoryEntry(ActionType.ADD, z)));
  }

  private static double gamma(double z) {
    double tmp1 = Math.sqrt(2 * Math.PI / z);
    double tmp2 = z + 1.0 / (12 * z - 1.0 / (10 * z));
    tmp2 = Math.pow(tmp2 / Math.E, z);
    return tmp1 * tmp2;
  }

  synchronized void xthRootOfY() {
    checkSize(OperatorType.BINARY);
    if (peekDeep(1).compareTo(Number.ZERO) < 0) {
      throw new IllegalStateException("Root of a negative.");
    }

    Number x = stack.pop();
    Number y = stack.pop();
    Number z = Number.valueOf(Math.pow(y.doubleValue(), 1.0 / x.doubleValue()));
    stack.push(z);
    addUndoItem(Lists.newArrayList(new HistoryEntry(ActionType.REMOVE, x),
                                        new HistoryEntry(ActionType.REMOVE, y),
                                        new HistoryEntry(ActionType.ADD, z)));
  }

  synchronized void negate() {
    checkSize(OperatorType.UNARY);
    Number x = stack.pop();
    Number z = x.negate();
    stack.push(z);
    addUndoItem(Lists.newArrayList(new HistoryEntry(ActionType.REMOVE, x),
                                        new HistoryEntry(ActionType.ADD, z)));
  }

  synchronized void dropOneValue() {
    checkSize(OperatorType.UNARY);
    addUndoItem(Lists.newArrayList(new HistoryEntry(ActionType.REMOVE, stack.pop())));
  }

  synchronized void clearStack() {
    if (stack.isEmpty()) {
      return;
    }

    List<HistoryEntry> undoEntries = Lists.newArrayListWithCapacity(stack.size());
    while (!stack.isEmpty()) {
      undoEntries.add(new HistoryEntry(ActionType.REMOVE, stack.pop()));
    }

    undoHistory.add(undoEntries);
  }

  synchronized void undo() {
    if (undoHistory.isEmpty()) {
      throw new IllegalStateException("Empty undo history.");
    }

    List<HistoryEntry> histories = undoHistory.pop();
    for (HistoryEntry history : Lists.reverse(histories)) {
      switch (history.actionType) {
      case ADD:
        if (!stack.peek().equals(history.getValue())) {
          throw new IllegalStateException("Stack history does not match.");
        }

        stack.pop();
        break;

      case REMOVE:
        stack.push(history.getValue());
        break;
      }
    }
  }

  synchronized void swap() {
    checkSize(OperatorType.BINARY);
    Number x = stack.pop();
    Number y = stack.pop();
    stack.push(x);
    stack.push(y);

    addUndoItem(Lists.newArrayList(new HistoryEntry(ActionType.REMOVE, x),
                                        new HistoryEntry(ActionType.REMOVE, y),
                                        new HistoryEntry(ActionType.ADD, x),
                                        new HistoryEntry(ActionType.ADD, y)));
  }

  synchronized Number getTop() {
    return peekDeep(0);
  }

  synchronized ImmutableList<Number> getStack() {
    return ImmutableList.copyOf(stack);
  }

  private void addUndoItem(List<HistoryEntry> undoItem) {
    undoHistory.push(undoItem);
    if (undoHistory.size() > MAX_UNDO_HISTORY) {
      undoHistory.removeLast();
    }
  }

  private void checkSize(@NotNull OperatorType operatorType) {
    if (stack.size() < operatorType.value()) {
      throw new IllegalStateException("Insufficient operands.");
    }
  }

  private Number peekDeep(int depth) {
    if (stack.size() < depth + 1) {
      throw new IllegalStateException("Insufficient operands.");
    }

    Iterator<Number> iterator = stack.iterator();
    Number returnValue = iterator.next();
    for (int i = 0; i < depth; i++) {
      returnValue = iterator.next();
    }

    return returnValue;
  }

  private class HistoryEntry {
    private final ActionType actionType;
    private final Number value;

    HistoryEntry(ActionType actionType, Number value) {
      this.actionType = actionType;
      this.value = value;
    }

    Number getValue() {
      return value;
    }
  }

  private enum ActionType {
    ADD,
    REMOVE
  }

  private enum OperatorType {
    UNARY(1),
    BINARY(2);

    private final int operandCount;


    OperatorType(int operandCount) {
      this.operandCount = operandCount;
    }

    int value() {
      return operandCount;
    }
  }
}
