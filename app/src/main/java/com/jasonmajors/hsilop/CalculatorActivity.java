package com.jasonmajors.hsilop;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static android.view.View.TEXT_ALIGNMENT_TEXT_END;

public class CalculatorActivity extends AppCompatActivity implements ResourceProvider {
  private static int DISPLAY_WIDTH = 20;
  private boolean shift = false;
  private boolean decimalEntered = false;
  private boolean eexEntered = false;
  private boolean entryMode = false;
  private boolean hexMode = false;
  private boolean radiansMode = true;

  private static int buttonRowHeight;

  private final List<TextView> registers = new ArrayList<>(4);

  private final StringBuilder input = new StringBuilder();
  private final Calculator calculator = Calculator.getInstance();
  private final Button radiansButton =
      new Button(R.string.radians_label, 0, 0, this::toggleRadians, this);

  private ArrayList<Button> buttons = Lists.newArrayList(
      new Button(R.string.shift, R.string.unshift, 0, this::toggleShift, this),
      new Button(R.string.sin, R.string.asin, 0, this::sine, this),
      new Button(R.string.cos, R.string.acos, 0, this::cosine, this),
      new Button(R.string.tan, R.string.atan, 0, this::tangent, this),
      new Button(R.string.undo, 0, 0, this::undo, this),

      new Button(R.string.log, R.string.ten_to_the_x, R.string.hex_a, this::log, this),
      new Button(R.string.ln, R.string.e_to_the_x, R.string.hex_b, this::ln, this),
      new Button(R.string.hex, 0, R.string.decimal, this::toggleHex, this),
      radiansButton,
      new Button(R.string.pi, 0, 0, () -> enterConstant(Math.PI), this),

      new Button(R.string.x_squared, 0, R.string.hex_c, this::square, this),
      new Button(R.string.square_root, 0, R.string.hex_d, this::squareRoot, this),
      new Button(R.string.one_over_x, 0, R.string.hex_e, this::inverse, this),
      new Button(R.string.factorial, 0, R.string.hex_f, this::factorial, this),
      new Button(R.string.divide, 0, 0, this::divide, this),

      new Button(R.string.y_to_the_x, R.string.xth_root_of_y, 0, this::power, this),
      new Button(R.string.seven, 0, 0, () -> enterDigit('7'), this),
      new Button(R.string.eight, 0, 0, () -> enterDigit('8'), this),
      new Button(R.string.nine, 0, 0, () -> enterDigit('9'), this),
      new Button(R.string.multiply, 0, 0, this::multiply, this),

      new Button(R.string.negate, 0, 0, this::negate, this),
      new Button(R.string.four, 0, 0, () -> enterDigit('4'), this),
      new Button(R.string.five, 0, 0, () -> enterDigit('5'), this),
      new Button(R.string.six, 0, 0, () -> enterDigit('6'), this),
      new Button(R.string.subtract, 0, 0, this::subtract, this),

      new Button(R.string.backspace, R.string.clear, 0, this::backspaceClear, this),
      new Button(R.string.one, 0, 0, () -> enterDigit('1'), this),
      new Button(R.string.two, 0, 0, () -> enterDigit('2'), this),
      new Button(R.string.three, 0, 0, () -> enterDigit('3'), this),
      new Button(R.string.add, 0, 0, this::add, this),

      new Button(R.string.drop, R.string.swap, 0, this::dropSwap, this),
      new Button(R.string.zero, 0, 0, () -> enterDigit('0'), this),
      new Button(R.string.decimal_point, 0, 0, this::enterDecimal, this),
      new Button(R.string.eex, 0, 0, this::eex, this),
      new Button(R.string.enter, 0, 0, this::pressEnter, this)
  );

  @Override
  protected void onResume() {
    super.onResume();
    DisplayMetrics metrics = new DisplayMetrics();
    getWindowManager().getDefaultDisplay().getMetrics(metrics);

    buttonRowHeight = (metrics.heightPixels - 800) / 7;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_calculator);

    registers.add((TextView) findViewById(R.id.registerX));
    registers.add((TextView) findViewById(R.id.registerY));
    registers.add((TextView) findViewById(R.id.register1));
    registers.add((TextView) findViewById(R.id.register2));

    GridView grid = (GridView) findViewById(R.id.button_grid);
    grid.setAdapter(new CustomGrid(this, buttons));
    grid.setOnItemClickListener((parent, view, position, id) -> processButton(position));
  }

  // TODO!!!! Make a cell height getter and call that from the inflater.
  static int getButtonRowHeight() {
    return buttonRowHeight;
  }

  private void processButton(int position) {
    try {
      buttons.get(position).press();
    }
    catch (IllegalStateException | NumberFormatException e) {
      clearInput();
      showError(e.getMessage());
    }
  }

  @Override
  public CharSequence getStringById(int id) {
    return getText(id);
  }

  private interface Operator {
    void operate();
  }

  private void executeOperation(Operator operator) {
    if (input.length() > 0) {
      pressEnter();
    }

    operator.operate();
    redrawStack(false);
  }

  private void add() {
    executeOperation(calculator::add);
  }

  private void subtract() {
    executeOperation(calculator::subtract);
  }

  private void multiply() {
    executeOperation(calculator::multiply);
  }

  private void divide() {
    executeOperation(calculator::divide);
  }

  private void sine() {
    if (shift) {
      executeOperation(() -> calculator.asin(radiansMode));
      toggleShift();
    }
    else {
      executeOperation(() -> calculator.sin(radiansMode));
    }
  }

  private void cosine() {
    if (shift) {
      executeOperation(() -> calculator.acos(radiansMode));
      toggleShift();
    }
    else {
      executeOperation(() -> calculator.cos(radiansMode));
    }
  }

  private void tangent() {
    if (shift) {
      executeOperation(() -> calculator.atan(radiansMode));
      toggleShift();
    }
    else {
      executeOperation(() -> calculator.tan(radiansMode));
    }
  }

  private void undo() {
    executeOperation(calculator::undo);
  }

  private void log() {
    if (hexMode) {
      enterDigit('a');
    }
    else if (shift) {
      executeOperation(calculator::tenToTheX);
      toggleShift();
    }
    else {
      executeOperation(calculator::log10);
    }
  }

  private void ln() {
    if (hexMode) {
      enterDigit('b');
    }
    else if (shift) {
      executeOperation(calculator::eToTheX);
      toggleShift();
    }
    else {
      executeOperation(calculator::naturalLog);
    }
  }


  private void enterConstant(double value) {
    if (entryMode) {
      clearFlags();
      input.delete(0, input.length());
    }

    calculator.enter(Number.valueOf(value));
    redrawStack(false);
  }

  private void square() {
    if (hexMode) {
      enterDigit('c');
    }
    else {
      executeOperation(calculator::square);
    }
  }

  private void squareRoot() {
    if (hexMode) {
      enterDigit('d');
    }
    else {
      executeOperation(calculator::squareRoot);
    }
  }

  private void inverse() {
    if (hexMode) {
      enterDigit('e');
    }
    else {
      executeOperation(calculator::oneOverX);
    }
  }

  private void factorial() {
    if (hexMode) {
      enterDigit('f');
    }
    else {
      executeOperation(calculator::factorial);
    }
  }

  private void power() {
    if (shift) {
      executeOperation(calculator::xthRootOfY);
      toggleShift();
    }
    else {
      executeOperation(calculator::power);
    }
  }

  private void negate() {
    if (eexEntered) {
      // Find the E. put a negative after it or remove it.
      int eIndex = input.indexOf("E");
      if (eIndex == input.length() - 1 || input.charAt(eIndex + 1) != '-') {
        input.insert(eIndex + 1, '-');
      }
      else {
        input.deleteCharAt(eIndex + 1);
      }

      displayInputLine();
    }
    else {
      if (input.length() > 0) {
        if (input.charAt(0) == '-') {
          input.deleteCharAt(0);
        }
        else {
          input.insert(0, '-');
        }

        displayInputLine();
      }
      else {
        calculator.negate();
        redrawStack(false);
      }
    }
  }

  private void backspaceClear() {
    if (shift) {
      clearInput();
      calculator.clearStack();
      redrawStack(false);
      toggleShift();
    }
    else {
      if (input.length() > 0) {
        int last = input.length() - 1;
        if (input.charAt(last) == '.') {
          decimalEntered = false;
        }
        else if (input.charAt(last) == 'E') {
          eexEntered = false;
        }

        input.deleteCharAt(last);
        displayInputLine();
      }
    }
  }

  private void dropSwap() {
    if (shift) {
      toggleShift();
      if (entryMode && input.length() > 0) {
        pressEnter();
      }

      calculator.swap();
    }
    else {
      if (entryMode) {
        clearInput();
      }
      else {
        calculator.dropOneValue();
      }
    }

    redrawStack(false);
  }

  private void eex() {
    if (!hexMode && !eexEntered) {
      eexEntered = true;
      enterDigit('E');
    }
  }

  private void pressEnter() {
    try {
      if (input.length() > 0) {
        if (hexMode) {
          calculator.enter(Number.valueOf((new BigInteger(input.toString(), 16).doubleValue())));
        }
        else {
          calculator.enter(Number.valueOf(input.toString()));
        }

        clearInput();
      }
      else {
        calculator.enter(calculator.getTop());
      }

      redrawStack(false);
    }
    catch (NumberFormatException e) {
      clearInput();
      showError("Invalid Number.");
    }
  }

  private void enterDigit(char c) {
    if (!entryMode) {
      input.delete(0, input.length());
      entryMode = true;
    }

    input.append(c);
    displayInputLine();
    redrawStack(true);
  }

  private void enterDecimal() {
    if (!decimalEntered) {
      decimalEntered = true;
      enterDigit('.');
    }
  }

  private void redrawStack(boolean showInputLine) {
    ImmutableList<Number> stack = calculator.getStack();
    int offset = showInputLine ? 1 : 0;
    if (showInputLine) {
      displayInputLine();
    }

    for (int i = 0; i + offset < registers.size(); i++) {
      if (stack.size() > i) {
        registers.get(i + offset).setText(formatNumber(stack.get(i).toString(hexMode)));
      }
      else {
        registers.get(i + offset).setText("");
      }
    }
  }

  private String formatNumber(String number) {
    if (hexMode) {
      if (number.length() <= DISPLAY_WIDTH) {
        return number;
      }
      else {
        // What to do????
        return number;
      }
    }
    if (number.length() <= DISPLAY_WIDTH) {
      number = number.replaceFirst("\\.0+$", "");
      return number;
    }
    else {
      int eIndex = number.indexOf('E');
      if (eIndex == -1) {
        return number.substring(0, DISPLAY_WIDTH);
      }
      else {
        int extraCount = number.length() - DISPLAY_WIDTH;
        int exponentLength = number.length() - eIndex;
        int length = number.length() - extraCount - exponentLength;
        return number.substring(0, length) + number.substring(eIndex);
      }
    }
  }

  private void displayInputLine() {
    if (input.length() < DISPLAY_WIDTH) {
      registers.get(0).setText(input);
    }
    else {
      registers.get(0).setText(input.substring(input.length() - DISPLAY_WIDTH));
    }
  }

  private void toggleRadians() {
    radiansMode = !radiansMode;
    setRadiansButtonLabel();
  }

  private void setRadiansButtonLabel() {
    if (radiansMode) {
      radiansButton.updateText(R.string.radians_label);
    }
    else {
      radiansButton.updateText(R.string.degrees_label);
    }
  }

  private void toggleHex() {
    hexMode = !hexMode;
    redrawStack(entryMode);
    updateButtonLabels();
  }

  private void toggleShift() {
    shift = !shift;
    updateButtonLabels();
  }

  private void updateButtonLabels() {
    Button.ButtonState buttonState = Button.ButtonState.BASIC;
    if (hexMode) {
      buttonState = Button.ButtonState.HEX;
    }
    else if (shift) {
      buttonState = Button.ButtonState.SHIFT;
    }

    for (Button button : buttons) {
      button.updateTextView(buttonState);
    }

    setRadiansButtonLabel();
  }

  private void showError(String message) {
    TextView registerX = registers.get(0);
    registerX.setTextAppearance(R.style.Error);
    registerX.setText(message);
    registerX.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

    final Handler handler = new Handler();
    handler.postDelayed(() -> {
      registerX.setTextAppearance(R.style.Registers);
      registerX.setTextAlignment(TEXT_ALIGNMENT_TEXT_END);
      redrawStack(false);
    }, 800);
  }

  private void clearFlags() {
    entryMode = false;
    decimalEntered = false;
    eexEntered = false;
  }

  private void clearInput() {
    input.delete(0, input.length());
    clearFlags();
  }
}
