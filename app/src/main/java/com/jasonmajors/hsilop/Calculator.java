package com.jasonmajors.hsilop;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.math.BigIntegerMath;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

class Calculator {
  private final Deque<BigDecimal> stack = Lists.newLinkedList();
  private final Deque<List<HistoryEntry>> undoHistory = Lists.newLinkedList();
  private static final BigDecimal NEGATIVE_ONE = BigDecimal.ONE.negate();
  private static final BigDecimal ONE = BigDecimal.ONE;

  private static final Calculator calculator = new Calculator();

  public static Calculator getInstance() {
    return calculator;
  }

  private Calculator() {
  }

  public void enter(@NotNull BigDecimal bd) {
    undoHistory.push(Lists.newArrayList(new HistoryEntry(ActionType.ADD, bd)));
    stack.push(bd);
  }

  public synchronized void add() {
    checkSize(OperatorType.BINARY);
    BigDecimal x = stack.pop();
    BigDecimal y = stack.pop();
    BigDecimal z = y.add(x);
    stack.push(z);
    undoHistory.push(Lists.newArrayList(new HistoryEntry(ActionType.REMOVE, x),
                                        new HistoryEntry(ActionType.REMOVE, y),
                                        new HistoryEntry(ActionType.ADD, z)));
  }

  public synchronized void subtract() {
    checkSize(OperatorType.BINARY);
    BigDecimal x = stack.pop();
    BigDecimal y = stack.pop();
    BigDecimal z = y.subtract(x);
    stack.push(z);
    undoHistory.push(Lists.newArrayList(new HistoryEntry(ActionType.REMOVE, x),
                                        new HistoryEntry(ActionType.REMOVE, y),
                                        new HistoryEntry(ActionType.ADD, z)));
  }

  public synchronized void multiply() {
    checkSize(OperatorType.BINARY);
    BigDecimal x = stack.pop();
    BigDecimal y = stack.pop();
    BigDecimal z = y.multiply(x);
    stack.push(z);
    undoHistory.push(Lists.newArrayList(new HistoryEntry(ActionType.REMOVE, x),
                                        new HistoryEntry(ActionType.REMOVE, y),
                                        new HistoryEntry(ActionType.ADD, z)));
  }

  public synchronized void divide() {
    checkSize(OperatorType.BINARY);
    if (stack.peek().compareTo(BigDecimal.ZERO) == 0) {
      throw new IllegalStateException("Divide by zero.");
    }

    BigDecimal x = stack.pop();
    BigDecimal y = stack.pop();
    BigDecimal z = y.divide(x, MathContext.DECIMAL128);
    stack.push(z);
    undoHistory.push(Lists.newArrayList(new HistoryEntry(ActionType.REMOVE, x),
                                        new HistoryEntry(ActionType.REMOVE, y),
                                        new HistoryEntry(ActionType.ADD, z)));
  }

  public synchronized void power() {
    checkSize(OperatorType.BINARY);
    BigDecimal x = stack.pop();
    BigDecimal y = stack.pop();
    BigDecimal z = BigDecimal.valueOf(Math.pow(y.doubleValue(), x.doubleValue()));
    stack.push(z);
    undoHistory.push(Lists.newArrayList(new HistoryEntry(ActionType.REMOVE, x),
                                        new HistoryEntry(ActionType.REMOVE, y),
                                        new HistoryEntry(ActionType.ADD, z)));
  }

  public synchronized void square() {
    checkSize(OperatorType.UNARY);
    BigDecimal x = stack.pop();
    BigDecimal z = x.pow(2);
    stack.push(z);
    undoHistory.push(Lists.newArrayList(new HistoryEntry(ActionType.REMOVE, x),
                                        new HistoryEntry(ActionType.ADD, z)));
  }

  public synchronized void squareRoot() {
    checkSize(OperatorType.UNARY);
    if (stack.peek().compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalStateException("Cannot take the root of a negative.");
    }

    BigDecimal x = stack.pop();
    BigDecimal z = BigDecimal.valueOf(Math.sqrt(x.doubleValue()));
    stack.push(z);
    undoHistory.push(Lists.newArrayList(new HistoryEntry(ActionType.REMOVE, x),
                                        new HistoryEntry(ActionType.ADD, z)));
  }

  public synchronized void naturalLog() {
    checkSize(OperatorType.UNARY);
    if (stack.peek().compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalStateException("Cannot take the log of a negative.");
    }

    BigDecimal x = stack.pop();
    BigDecimal z = BigDecimal.valueOf(Math.log(x.doubleValue()));
    stack.push(z);
    undoHistory.push(Lists.newArrayList(new HistoryEntry(ActionType.REMOVE, x),
                                        new HistoryEntry(ActionType.ADD, z)));
  }

  public synchronized void eToTheX() {
    checkSize(OperatorType.UNARY);
    BigDecimal x = stack.pop();
    BigDecimal z = BigDecimal.valueOf(Math.exp(x.doubleValue()));
    stack.push(z);
    undoHistory.push(Lists.newArrayList(new HistoryEntry(ActionType.REMOVE, x),
                                        new HistoryEntry(ActionType.ADD, z)));
  }

  public synchronized void log10() {
    checkSize(OperatorType.UNARY);
    if (stack.peek().compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalStateException("Cannot take the log of a negative.");
    }

    BigDecimal x = stack.pop();
    BigDecimal z = BigDecimal.valueOf(Math.log10(x.doubleValue()));
    stack.push(z);
    undoHistory.push(Lists.newArrayList(new HistoryEntry(ActionType.REMOVE, x),
                                        new HistoryEntry(ActionType.ADD, z)));
  }

  public synchronized void tenToTheX() {
    checkSize(OperatorType.UNARY);
    BigDecimal x = stack.pop();
    BigDecimal z = BigDecimal.valueOf(Math.pow(10.0, x.doubleValue()));
    stack.push(z);
    undoHistory.push(Lists.newArrayList(new HistoryEntry(ActionType.REMOVE, x),
                                        new HistoryEntry(ActionType.ADD, z)));
  }

  public synchronized void sin() {
    checkSize(OperatorType.UNARY);
    BigDecimal x = stack.pop();
    BigDecimal z = BigDecimal.valueOf(Math.sin(x.doubleValue()));
    stack.push(z);
    undoHistory.push(Lists.newArrayList(new HistoryEntry(ActionType.REMOVE, x),
                                        new HistoryEntry(ActionType.ADD, z)));
  }

  public synchronized void cos() {
    checkSize(OperatorType.UNARY);
    BigDecimal x = stack.pop();
    BigDecimal z = BigDecimal.valueOf(Math.cos(x.doubleValue()));
    stack.push(z);
    undoHistory.push(Lists.newArrayList(new HistoryEntry(ActionType.REMOVE, x),
                                        new HistoryEntry(ActionType.ADD, z)));
  }

  public synchronized void tan() {
    checkSize(OperatorType.UNARY);
    BigDecimal x = stack.pop();
    BigDecimal z = BigDecimal.valueOf(Math.tan(x.doubleValue()));
    stack.push(z);
    undoHistory.push(Lists.newArrayList(new HistoryEntry(ActionType.REMOVE, x),
                                        new HistoryEntry(ActionType.ADD, z)));
  }

  public synchronized void asin() {
    checkSize(OperatorType.UNARY);
    BigDecimal peek = stack.peek();
    if (peek.compareTo(NEGATIVE_ONE) < 0 || peek.compareTo(ONE) > 0) {
      throw new IllegalStateException("Invalid input.");
    }

    BigDecimal x = stack.pop();
    BigDecimal z = BigDecimal.valueOf(Math.asin(x.doubleValue()));
    stack.push(z);
    undoHistory.push(Lists.newArrayList(new HistoryEntry(ActionType.REMOVE, x),
                                        new HistoryEntry(ActionType.ADD, z)));
  }

  public synchronized void acos() {
    checkSize(OperatorType.UNARY);
    BigDecimal peek = stack.peek();
    if (peek.compareTo(NEGATIVE_ONE) < 0 || peek.compareTo(ONE) > 0) {
      throw new IllegalStateException("Invalid input.");
    }

    BigDecimal x = stack.pop();
    BigDecimal z = BigDecimal.valueOf(Math.acos(x.doubleValue()));
    stack.push(z);
    undoHistory.push(Lists.newArrayList(new HistoryEntry(ActionType.REMOVE, x),
                                        new HistoryEntry(ActionType.ADD, z)));
  }

  public synchronized void atan() {
    checkSize(OperatorType.UNARY);
    BigDecimal x = stack.pop();
    BigDecimal z = BigDecimal.valueOf(Math.atan(x.doubleValue()));
    stack.push(z);
    undoHistory.push(Lists.newArrayList(new HistoryEntry(ActionType.REMOVE, x),
                                        new HistoryEntry(ActionType.ADD, z)));
  }

  public synchronized void oneOverX() {
    checkSize(OperatorType.UNARY);
    if (stack.peek().compareTo(BigDecimal.ZERO) == 0) {
      throw new IllegalStateException("Divide by zero.");
    }

    BigDecimal x = stack.pop();
    BigDecimal z = BigDecimal.ONE.divide(x, MathContext.DECIMAL128);
    stack.push(z);
    undoHistory.push(Lists.newArrayList(new HistoryEntry(ActionType.REMOVE, x),
                                        new HistoryEntry(ActionType.ADD, z)));
  }

  public synchronized void factorial() {
    checkSize(OperatorType.UNARY);
    // n! = gamma(n + 1)
    BigDecimal x = stack.pop();
    BigDecimal z;

    // Zero.
    if (x.equals(BigDecimal.ZERO)) {
      z = BigDecimal.ONE;
    }
    // Integer.
    else if (x.scale() <= 0 || x.stripTrailingZeros().scale() <= 0) {
      z = new BigDecimal(BigIntegerMath.factorial(x.intValue()));
    }
    // Other.
    else {
      z = BigDecimal.valueOf(gamma(x.doubleValue() + 1));
    }

    stack.push(z);
    undoHistory.push(Lists.newArrayList(new HistoryEntry(ActionType.REMOVE, x),
                                        new HistoryEntry(ActionType.ADD, z)));
  }

  private static double gamma(double z) {
    double tmp1 = Math.sqrt(2 * Math.PI / z);
    double tmp2 = z + 1.0 / (12 * z - 1.0 / (10 * z));
    tmp2 = Math.pow(tmp2 / Math.E, z);
    return tmp1 * tmp2;
  }

  public synchronized void xthRootOfY() {
    checkSize(OperatorType.BINARY);
    if (peekDeep(1).compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalStateException("Cannot take the root of a negative.");
    }

    BigDecimal x = stack.pop();
    BigDecimal y = stack.pop();
    BigDecimal z = BigDecimal.valueOf(Math.pow(y.doubleValue(), 1.0 / x.doubleValue()));
    stack.push(z);
    undoHistory.push(Lists.newArrayList(new HistoryEntry(ActionType.REMOVE, x),
                                        new HistoryEntry(ActionType.REMOVE, y),
                                        new HistoryEntry(ActionType.ADD, z)));
  }

  public synchronized void negate() {
    checkSize(OperatorType.UNARY);
    BigDecimal x = stack.pop();
    BigDecimal z = x.negate();
    stack.push(z);
    undoHistory.push(Lists.newArrayList(new HistoryEntry(ActionType.REMOVE, x),
                                        new HistoryEntry(ActionType.ADD, z)));
  }

  public synchronized void dropOneValue() {
    checkSize(OperatorType.UNARY);
    undoHistory.push(Lists.newArrayList(new HistoryEntry(ActionType.REMOVE, stack.pop())));
  }

  public synchronized void clearStack() {
    if (stack.isEmpty()) {
      return;
    }

    List undoEntries = Lists.newArrayListWithCapacity(stack.size());
    while (!stack.isEmpty()) {
      undoEntries.add(new HistoryEntry(ActionType.REMOVE, stack.pop()));
    }

    undoHistory.add(undoEntries);
  }

  public synchronized void undo() {
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

  public synchronized void swap() {
    checkSize(OperatorType.BINARY);
    BigDecimal x = stack.pop();
    BigDecimal y = stack.pop();
    stack.push(x);
    stack.push(y);

    undoHistory.push(Lists.newArrayList(new HistoryEntry(ActionType.REMOVE, x),
                                        new HistoryEntry(ActionType.REMOVE, y),
                                        new HistoryEntry(ActionType.ADD, x),
                                        new HistoryEntry(ActionType.ADD, y)));
  }

  public synchronized BigDecimal getTop() {
    return peekDeep(0);
  }

  public synchronized ImmutableList<BigDecimal> getStack() {
    return ImmutableList.copyOf(stack);
  }

  private void checkSize(@NotNull OperatorType operatorType) {
    if (stack.size() < operatorType.value()) {
      throw new IllegalStateException("Stack is undersized.");
    }
  }

  private BigDecimal peekDeep(int depth) {
    if (stack.size() < depth + 1) {
      throw new IllegalStateException("Stack is undersized.");
    }

    Iterator<BigDecimal> iterator = stack.iterator();
    BigDecimal returnValue = iterator.next();
    for (int i = 0; i < depth; i++) {
      returnValue = iterator.next();
    }

    return returnValue;
  }

  @RequiredArgsConstructor(suppressConstructorProperties = true)
  private class HistoryEntry {
    @Getter private final ActionType actionType;
    @Getter private final BigDecimal value;
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
