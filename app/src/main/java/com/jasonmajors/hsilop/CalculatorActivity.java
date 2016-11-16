package com.jasonmajors.hsilop;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static android.view.Gravity.TOP;

public class CalculatorActivity extends AppCompatActivity {
  private static int DISPLAY_WIDTH = 20;
  boolean shift = false;
  boolean decimalEntered = false;
  boolean eexEntered = false;
  boolean entryMode = false;
  boolean hexMode = false;
  boolean radiansMode = true;

  private final StringBuilder input = new StringBuilder();
  private final Calculator calculator = Calculator.getInstance();
  private final Button radiansButton =
      new Button("rad", R.drawable.radians_icon, 0, 0, this::toggleRadians);

  private ArrayList<Button> buttons = Lists.newArrayList(
      new Button("^", R.drawable.dummy_icon, R.drawable.dummy_icon, 0, this::toggleShift),
      new Button("sin", R.drawable.dummy_icon, R.drawable.dummy_icon, 0, this::sine),
      new Button("cos", R.drawable.dummy_icon, R.drawable.dummy_icon, 0, this::cosine),
      new Button("tan", R.drawable.dummy_icon, R.drawable.dummy_icon, 0, this::tangent),
      new Button("undo", R.drawable.dummy_icon, 0, 0, this::undo),

      new Button("log", R.drawable.dummy_icon, R.drawable.dummy_icon, R.drawable.dummy_icon, this::log),
      new Button("ln", R.drawable.dummy_icon, R.drawable.dummy_icon, R.drawable.dummy_icon, this::ln),
      new Button("0x", R.drawable.dummy_icon, 0, 0, this::toggleHex),
      radiansButton,
      new Button("π", R.drawable.dummy_icon, 0, 0, () -> enterConstant(Math.PI)),

      new Button("x^2", R.drawable.dummy_icon, 0, R.drawable.dummy_icon, this::square),
      new Button("√x", R.drawable.dummy_icon, 0, R.drawable.dummy_icon, this::squareRoot),
      new Button("1/x", R.drawable.dummy_icon, 0, R.drawable.dummy_icon, this::inverse),
      new Button("x!", R.drawable.dummy_icon, 0, R.drawable.dummy_icon, this::factorial),
      new Button("÷", R.drawable.dummy_icon, 0, 0, this::divide),

      new Button("y^x", R.drawable.dummy_icon, R.drawable.dummy_icon, 0, this::power),
      new Button("7", R.drawable.dummy_icon, 0, 0, () -> enterDigit('7')),
      new Button("8", R.drawable.dummy_icon, 0, 0, () -> enterDigit('8')),
      new Button("9", R.drawable.dummy_icon, 0, 0, () -> enterDigit('9')),
      new Button("x", R.drawable.dummy_icon, 0, 0, this::multiply),

      new Button("±", R.drawable.dummy_icon, R.drawable.dummy_icon, 0, this::negate),
      new Button("4", R.drawable.dummy_icon, 0, 0, () -> enterDigit('4')),
      new Button("5", R.drawable.dummy_icon, 0, 0, () -> enterDigit('5')),
      new Button("6", R.drawable.dummy_icon, 0, 0, () -> enterDigit('6')),
      new Button("-", R.drawable.dummy_icon, 0, 0, this::subtract),

      new Button("⌫", R.drawable.dummy_icon, R.drawable.dummy_icon, 0, this::backspaceClear),
      new Button("1", R.drawable.dummy_icon, 0, 0, () -> enterDigit('1')),
      new Button("2", R.drawable.dummy_icon, 0, 0, () -> enterDigit('2')),
      new Button("3", R.drawable.dummy_icon, 0, 0, () -> enterDigit('3')),
      new Button("+", R.drawable.dummy_icon, 0, 0, this::add),

      new Button("drop", R.drawable.dummy_icon, R.drawable.dummy_icon, 0, this::dropSwap),
      new Button("0", R.drawable.dummy_icon, 0, 0, () -> enterDigit('0')),
      new Button(".", R.drawable.dummy_icon, 0, 0, () -> enterDigit('.')),
      new Button("EEX", R.drawable.dummy_icon, 0, 0, this::eex),
      new Button("↵", R.drawable.dummy_icon, 0, 0, this::pressEnter)
  );

  private final List<TextView> registers = new ArrayList<>(4);

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

  private void processButton(int position) {
    try {
      buttons.get(position).press();
    }
    catch (IllegalStateException | NumberFormatException e) {
      showError(e.getMessage());
    }
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
      number = number.replaceFirst("\\.0+", "");
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
    setRadiansButtonImage();
  }

  private void setRadiansButtonImage() {
    if (radiansMode) {
      radiansButton.updateImage(R.drawable.radians_icon);
    }
    else {
      radiansButton.updateImage(R.drawable.degrees_icon);
    }
  }

  private void toggleHex() {
    hexMode = !hexMode;
    redrawStack(entryMode);
    updateButtonImages();
  }

  private void toggleShift() {
    shift = !shift;
    updateButtonImages();
  }

  private void updateButtonImages() {
    Button.ImageState imageState = Button.ImageState.BASIC;
    if (hexMode) {
      imageState = Button.ImageState.HEX;
    }
    else if (shift) {
      imageState = Button.ImageState.SHIFT;
    }

    for (Button button : buttons) {
      button.updateImageView(imageState);
    }

    setRadiansButtonImage();
  }

  private void showError(String message) {
    int[] location = new int[2];
    registers.get(1).getLocationOnScreen(location);

    Toast toast = Toast.makeText(CalculatorActivity.this, message, Toast.LENGTH_SHORT);
    toast.setGravity(TOP, 0, location[1]);
    toast.show();
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
