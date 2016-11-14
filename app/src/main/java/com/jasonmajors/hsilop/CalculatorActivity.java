package com.jasonmajors.hsilop;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static android.view.Gravity.TOP;

public class CalculatorActivity extends AppCompatActivity {
  boolean shift = false;
  boolean decimalEntered = false;
  boolean eexEntered = false;
  boolean entryMode = false;
  boolean hexMode = false;
  boolean radiansMode = true;

  private final StringBuilder input = new StringBuilder();
  private final Calculator calculator = Calculator.getInstance();
  private final CalculatorButton radiansButton =
      new CalculatorButton("rad", R.drawable.dummy_icon, 0, 0, this::toggleRadians);

  private ArrayList<CalculatorButton> buttons = Lists.newArrayList(
      new CalculatorButton("^", R.drawable.dummy_icon, R.drawable.dummy_icon, 0, this::toggleShift),
      new CalculatorButton("sin", R.drawable.dummy_icon, R.drawable.dummy_icon, 0, this::sine),
      new CalculatorButton("cos", R.drawable.dummy_icon, R.drawable.dummy_icon, 0, this::cosine),
      new CalculatorButton("tan", R.drawable.dummy_icon, R.drawable.dummy_icon, 0, this::tangent),
      new CalculatorButton("undo", R.drawable.dummy_icon, 0, 0, this::undo),

      new CalculatorButton("log", R.drawable.dummy_icon, R.drawable.dummy_icon, R.drawable.dummy_icon, this::log),
      new CalculatorButton("ln", R.drawable.dummy_icon, R.drawable.dummy_icon, R.drawable.dummy_icon, this::ln),
      new CalculatorButton("0x", R.drawable.dummy_icon, 0, 0, this::toggleHex),
      radiansButton,
      new CalculatorButton("π", R.drawable.dummy_icon, 0, 0, () -> enterConstant(Math.PI)),

      new CalculatorButton("x^2", R.drawable.dummy_icon, 0, R.drawable.dummy_icon, this::square),
      new CalculatorButton("√x", R.drawable.dummy_icon, 0, R.drawable.dummy_icon, this::squareRoot),
      new CalculatorButton("1/x", R.drawable.dummy_icon, 0, R.drawable.dummy_icon, this::inverse),
      new CalculatorButton("x!", R.drawable.dummy_icon, 0, R.drawable.dummy_icon, this::factorial),
      new CalculatorButton("÷", R.drawable.dummy_icon, 0, 0, this::divide),

      new CalculatorButton("y^x", R.drawable.dummy_icon, R.drawable.dummy_icon, 0, this::power),
      new CalculatorButton("7", R.drawable.dummy_icon, 0, 0, () -> enterDigit('7')),
      new CalculatorButton("8", R.drawable.dummy_icon, 0, 0, () -> enterDigit('8')),
      new CalculatorButton("9", R.drawable.dummy_icon, 0, 0, () -> enterDigit('9')),
      new CalculatorButton("x", R.drawable.dummy_icon, 0, 0, this::multiply),

      new CalculatorButton("±", R.drawable.dummy_icon, R.drawable.dummy_icon, 0, this::negate),
      new CalculatorButton("4", R.drawable.dummy_icon, 0, 0, () -> enterDigit('4')),
      new CalculatorButton("5", R.drawable.dummy_icon, 0, 0, () -> enterDigit('5')),
      new CalculatorButton("6", R.drawable.dummy_icon, 0, 0, () -> enterDigit('6')),
      new CalculatorButton("-", R.drawable.dummy_icon, 0, 0, this::subtract),

      new CalculatorButton("⌫", R.drawable.dummy_icon, R.drawable.dummy_icon, 0, this::backspaceClear),
      new CalculatorButton("1", R.drawable.dummy_icon, 0, 0, () -> enterDigit('1')),
      new CalculatorButton("2", R.drawable.dummy_icon, 0, 0, () -> enterDigit('2')),
      new CalculatorButton("3", R.drawable.dummy_icon, 0, 0, () -> enterDigit('3')),
      new CalculatorButton("+", R.drawable.dummy_icon, 0, 0, this::add),

      new CalculatorButton("drop", R.drawable.dummy_icon, R.drawable.dummy_icon, 0, this::dropSwap),
      new CalculatorButton("0", R.drawable.dummy_icon, 0, 0, () -> enterDigit('0')),
      new CalculatorButton(".", R.drawable.dummy_icon, 0, 0, () -> enterDigit('.')),
      new CalculatorButton("EEX", R.drawable.dummy_icon, 0, 0, this::eex),
      new CalculatorButton("↵", R.drawable.dummy_icon, 0, 0, this::pressEnter)
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

    calculator.enter(new BigDecimal(value));
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

      registers.get(0).setText(input);
    }
    else {
      if (input.length() > 0) {
        if (input.charAt(0) == '-') {
          input.deleteCharAt(0);
        }
        else {
          input.insert(0, '-');
        }

        registers.get(0).setText(input);
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
        registers.get(0).setText(input);
      }
    }
  }

  private void dropSwap() {
    if (shift) {
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
          calculator.enter(new BigDecimal(new BigInteger(input.toString(), 16)));
        }
        else {
          calculator.enter(new BigDecimal(input.toString()));
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
    registers.get(0).setText(input);
    redrawStack(true);
  }

  private void redrawStack(boolean showInputLine) {
    ImmutableList<BigDecimal> stack = calculator.getStack();
    int offset = showInputLine ? 1 : 0;
    if (showInputLine) {
      registers.get(0).setText(input);
    }

    for (int i = 0; i + offset < registers.size(); i++) {
      if (stack.size() > i) {
        registers.get(i + offset).setText(formatValue(stack.get(i)));
      }
      else {
        registers.get(i + offset).setText("");
      }
    }
  }

  private String formatValue(BigDecimal value) {
    /*
    bd = bd.setScale(2, BigDecimal.ROUND_DOWN);

    DecimalFormat df = new DecimalFormat();

    df.setMaximumFractionDigits(2);

    df.setMinimumFractionDigits(0);

    df.setGroupingUsed(false);
    String result = df.format(bd);
    */
    BigDecimal bd = value.setScale(10, BigDecimal.ROUND_HALF_UP);
    DecimalFormat format = new DecimalFormat();
    format.setMaximumFractionDigits(10);
    format.setMinimumFractionDigits(0);
    format.setGroupingUsed(true);

    // TODO Limit to 15 chars.
    if (hexMode) {
      return "0x" + value.toBigInteger().toString(16);
    }

    return format.format(bd);
    // return value.toString().replaceFirst("\\.0*$", "");
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
    CalculatorButton.ImageState imageState = CalculatorButton.ImageState.BASIC;
    if (hexMode) {
      imageState = CalculatorButton.ImageState.HEX;
    }
    else if (shift) {
      imageState = CalculatorButton.ImageState.SHIFT;
    }

    for (CalculatorButton button : buttons) {
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
