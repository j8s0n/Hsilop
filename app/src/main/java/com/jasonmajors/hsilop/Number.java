package com.jasonmajors.hsilop;

import org.jetbrains.annotations.NotNull;

class Number {
  private static final double CLOSE_ENOUGH = 0.000001;
  static Number ZERO = new Number(0.0);
  static Number ONE = new Number(1.0);
  static Number NEGATIVE_ONE = new Number(-1.0);
  private double value;

  private Number(double value) {
    this.value = value;
  }

  double doubleValue() {
    return value;
  }

  Number add(Number rhs) {
    return new Number(value + rhs.value);
  }

  @NotNull
  static Number valueOf(double d) {
    return new Number(d);
  }

  @NotNull
  Number subtract(Number rhs) {
    return new Number(value - rhs.value);
  }

  @NotNull
  Number multiply(Number rhs) {
    return new Number(value * rhs.value);
  }

  @NotNull
  Number divide(Number rhs) {
    return new Number(value / rhs.value);
  }

  int compareTo(Number rhs) {
    double diff = value - rhs.value;
    double average = (value + rhs.value) / 2.0;
    if (Math.abs(diff) / average < CLOSE_ENOUGH) {
      return 0;
    }
    else if (value < rhs.value) {
      return -1;
    }
    else {
      return 1;
    }
  }

  @NotNull
  Number pow(int exponent) {
    return new Number(Math.pow(value, exponent));
  }

  boolean isInteger() {
    return (long) value == value;
  }

  int intValue() {
    return (int) value;
  }

  @NotNull
  Number negate() {
    return new Number(0 - value);
  }

  @NotNull
  static Number valueOf(String s) {
    return new Number(Double.parseDouble(s));
  }

  @NotNull
  String toString(boolean hexMode) {
    if (hexMode) {
      String neg = (value < 0) ? "-" : "";
      String hex = Integer.toHexString((int) Math.abs(value));
      return "0x" + neg + hex;
    }
    else {
      return Double.toString(value);
    }
  }
}
