package com.jasonmajors.hsilop;

enum ButtonName {
  SHIFT (0, "Shift", "^"),
  SINE (1, "Sine", "sin"),
  COSINE (2, "Cosine", "cos"),
  TANGENT (3, "Tangent", "tan"),
  UNDO (4, "Undo", "U"),
  LOG (5, "Log / Ten to the X", "log"),
  NATURAL_LOG (6, "Natural Log / E to the X", "ln"),
  HEX_DECIMAL (7, "Hex / Decimal", "hex"),
  DEGREES_RADIANS (8, "Degrees / Radians", "deg"),
  PI_CONSTANTS (9, "Pi / Constants", "π"),
  SQUARED (10, "Square", "x^2"),
  SQUARE_ROOT (11, "Square Root", "√x"),
  INVERSE (12, "Inverse", "1/x"),
  FACTORIAL (13, "Factorial", "x!"),
  DIVIDE (14, "Divide", "/"),
  POWER_XTH_ROOT (15, "Power / Xth Root", "x^y"),
  SEVEN (16, "Seven", "7"),
  EIGHT (17, "Eight", "8"),
  NINE (18, "Nine", "9"),
  MULTIPLY (19, "Multiply", "*"),
  NEGATION (20, "Negation", "±"),
  FOUR (21, "Four", "4"),
  FIVE (22, "Five", "5"),
  SIX (23, "Six", "6"),
  SUBTRACT (24, "Subtract", "-"),
  BACKSPACE_CLEAR (25, "Backspace / Clear", "<-"),
  ONE (26, "One", "1"),
  TWO (27, "Two", "2"),
  THREE (28, "Three", "3"),
  ADDITION (29, "Add", "+"),
  DROP_SWAP (30, "Drop / Swap", "drop"),
  ZERO (31, "Zero", "0"),
  DECIMAL (32, "Decimal", "."),
  EEX (33, "Exponential Notation", "EEX"),
  ENTER (34, "Enter", "↵");

  private final int position;
  private final String description;
  private final String label;

  ButtonName(int position, String description, String label) {
    this.position = position;
    this.description = description;
    this.label = label;
  }

  int getPosition() {
    return position;
  }

  String getDescription() {
    return description;
  }

  public String getLabel() {
    return label;
  }
}
