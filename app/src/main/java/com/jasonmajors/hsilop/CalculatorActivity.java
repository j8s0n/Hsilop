package com.jasonmajors.hsilop;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.collect.ImmutableList;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static com.jasonmajors.hsilop.ButtonName.ADDITION;
import static com.jasonmajors.hsilop.ButtonName.BACKSPACE_CLEAR;
import static com.jasonmajors.hsilop.ButtonName.COSINE;
import static com.jasonmajors.hsilop.ButtonName.DECIMAL;
import static com.jasonmajors.hsilop.ButtonName.DEGREES_RADIANS;
import static com.jasonmajors.hsilop.ButtonName.DIVIDE;
import static com.jasonmajors.hsilop.ButtonName.DROP_SWAP;
import static com.jasonmajors.hsilop.ButtonName.EEX;
import static com.jasonmajors.hsilop.ButtonName.EIGHT;
import static com.jasonmajors.hsilop.ButtonName.ENTER;
import static com.jasonmajors.hsilop.ButtonName.FACTORIAL;
import static com.jasonmajors.hsilop.ButtonName.FIVE;
import static com.jasonmajors.hsilop.ButtonName.FOUR;
import static com.jasonmajors.hsilop.ButtonName.HEX_DECIMAL;
import static com.jasonmajors.hsilop.ButtonName.INVERSE;
import static com.jasonmajors.hsilop.ButtonName.LOG;
import static com.jasonmajors.hsilop.ButtonName.MULTIPLY;
import static com.jasonmajors.hsilop.ButtonName.NATURAL_LOG;
import static com.jasonmajors.hsilop.ButtonName.NEGATION;
import static com.jasonmajors.hsilop.ButtonName.NINE;
import static com.jasonmajors.hsilop.ButtonName.ONE;
import static com.jasonmajors.hsilop.ButtonName.PI_CONSTANTS;
import static com.jasonmajors.hsilop.ButtonName.POWER_XTH_ROOT;
import static com.jasonmajors.hsilop.ButtonName.SEVEN;
import static com.jasonmajors.hsilop.ButtonName.SHIFT;
import static com.jasonmajors.hsilop.ButtonName.SINE;
import static com.jasonmajors.hsilop.ButtonName.SIX;
import static com.jasonmajors.hsilop.ButtonName.SQUARED;
import static com.jasonmajors.hsilop.ButtonName.SQUARE_ROOT;
import static com.jasonmajors.hsilop.ButtonName.SUBTRACT;
import static com.jasonmajors.hsilop.ButtonName.TANGENT;
import static com.jasonmajors.hsilop.ButtonName.THREE;
import static com.jasonmajors.hsilop.ButtonName.TWO;
import static com.jasonmajors.hsilop.ButtonName.UNDO;
import static com.jasonmajors.hsilop.ButtonName.ZERO;
import static com.jasonmajors.hsilop.ButtonName.values;

public class CalculatorActivity extends AppCompatActivity {
  private boolean shift = false;
  private boolean decimalEntered = false;
  private boolean eexEntered = false;
  private boolean entryMode = false;
  private boolean hexMode = false;
  private boolean radiansMode = true;

  private final StringBuilder input = new StringBuilder();
  private final Calculator calculator = Calculator.getInstance();

  private TextView registerX;
  private TextView registerY;
  private TextView register1;
  private TextView register2;
  private final List<TextView> registers = new ArrayList<>(4);

  private GridView grid;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_calculator);

    registerX = (TextView) findViewById(R.id.registerX);
    registerY = (TextView) findViewById(R.id.registerY);
    register1 = (TextView) findViewById(R.id.register1);
    register2 = (TextView) findViewById(R.id.register2);
    registers.add(registerX);
    registers.add(registerY);
    registers.add(register1);
    registers.add(register2);

    grid = (GridView) findViewById(R.id.button_grid);
    CustomGrid adapter = new CustomGrid(CalculatorActivity.this, values());
    grid.setAdapter(adapter);
    grid.setOnItemClickListener((parent, view, position, id) -> {
      if (position == SHIFT.getPosition()) {
        toggleShift();
      }
      else if (hexMode) {
        processHex(position);
      }
      else if (shift) {
        processSecondary(position);
      }
      else {
        processPrimary(position);
      }
    });

  }

  private void processHex(int position) {
    try {
      if (LOG.getPosition() == position) {
        enterDigit('a');
      }
      else if (NATURAL_LOG.getPosition() == position) {
        enterDigit('b');
      }
      else if (SQUARED.getPosition() == position) {
        enterDigit('c');
      }
      else if (SQUARE_ROOT.getPosition() == position) {
        enterDigit('d');
      }
      else if (INVERSE.getPosition() == position) {
        enterDigit('e');
      }
      else if (FACTORIAL.getPosition() == position) {
        enterDigit('f');
      }
      else if (EEX.getPosition() == position) {
        return;
      }
      else {
        processPrimary(position);
      }
    }
    catch (IllegalStateException e) {
      Toast.makeText(CalculatorActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }
  }

  private void processSecondary(int position) {
    toggleShift();
    try {
      // Binary Operations.
      if (POWER_XTH_ROOT.getPosition() == position) {
        operation(() -> calculator.xthRootOfY());
      }

      // Unary Operations.
      else if (DROP_SWAP.getPosition() == position) {
        operation(() -> calculator.swap());
      }
      else if (LOG.getPosition() == position) {
        operation(() -> calculator.tenToTheX());
      }
      else if (SINE.getPosition() == position) {
        operation(() -> calculator.asin(radiansMode));
      }
      else if (COSINE.getPosition() == position) {
        operation(() -> calculator.acos(radiansMode));
      }
      else if (TANGENT.getPosition() == position) {
        operation(() -> calculator.atan(radiansMode));
      }
      else if (LOG.getPosition() == position) {
        operation(() -> calculator.tenToTheX());
      }
      else if (NATURAL_LOG.getPosition() == position) {
        operation(() -> calculator.eToTheX());
      }

      // Stack Operations.
      else if (BACKSPACE_CLEAR.getPosition() == position) {
        operation(() -> calculator.clearStack());
      }
      else {
        // If there's no shifted version of this button, just process the primary version of the button.
        processPrimary(position);
      }
    }
    catch (IllegalStateException e) {
      Toast.makeText(CalculatorActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }
  }

  private void processPrimary(int position) {
    try {
      // Value Entry.
      if (ZERO.getPosition() == position) {
        enterDigit('0');
      }
      else if (ONE.getPosition() == position) {
        enterDigit('1');
      }
      else if (TWO.getPosition() == position) {
        enterDigit('2');
      }
      else if (THREE.getPosition() == position) {
        enterDigit('3');
      }
      else if (FOUR.getPosition() == position) {
        enterDigit('4');
      }
      else if (FIVE.getPosition() == position) {
        enterDigit('5');
      }
      else if (SIX.getPosition() == position) {
        enterDigit('6');
      }
      else if (SEVEN.getPosition() == position) {
        enterDigit('7');
      }
      else if (EIGHT.getPosition() == position) {
        enterDigit('8');
      }
      else if (NINE.getPosition() == position) {
        enterDigit('9');
      }
      else if (DECIMAL.getPosition() == position) {
        if (!decimalEntered) {
          decimalEntered = true;
          enterDigit('.');
        }
      }
      else if (EEX.getPosition() == position) {
        if (!eexEntered) {
          eexEntered = true;
          enterDigit('E');
        }
      }
      else if (NEGATION.getPosition() == position) {
        if (eexEntered) {
          // Find the E. put a negative after it or remove it.
          int eIndex = input.indexOf("E");
          if (eIndex == input.length() - 1 || input.charAt(eIndex + 1) != '-') {
            input.insert(eIndex + 1, '-');
          }
          else {
            input.deleteCharAt(eIndex + 1);
          }

          registerX.setText(input);
        }
        else {
          if (input.length() > 0) {
            if (input.charAt(0) == '-') {
              input.deleteCharAt(0);
            }
            else {
              input.insert(0, '-');
            }

            registerX.setText(input);
          }
          else {
            calculator.negate();
            redrawStack(false);
          }
        }
      }
      else if (PI_CONSTANTS.getPosition() == position) {
        if (entryMode) {
          clearFlags();
          input.delete(0, input.length());
        }

        calculator.enter(new BigDecimal(Math.PI));
        redrawStack(false);
      }

      // Stack Operations.
      else if (ENTER.getPosition() == position) {
        pressEnter();
      }
      else if (DROP_SWAP.getPosition() == position) {
        if (entryMode) {
          input.delete(0, input.length());
          clearFlags();
        }
        else {
          calculator.dropOneValue();
        }

        redrawStack(false);
      }
      else if (UNDO.getPosition() == position) {
        operation(() -> calculator.undo());
      }
      else if (BACKSPACE_CLEAR.getPosition() == position) {
        if (input.length() > 0) {
          int last = input.length() - 1;
          if (input.charAt(last) == '.') {
            decimalEntered = false;
          }
          else if (input.charAt(last) == 'E') {
            eexEntered = false;
          }

          input.deleteCharAt(last);
          registerX.setText(input);
        }
      }

      // Display Controls.
      else if (HEX_DECIMAL.getPosition() == position) {
        toggleHex();
      }
      else if (DEGREES_RADIANS.getPosition() == position) {
        toggleRadians();
      }

      // Unary Operations.
      else if (SINE.getPosition() == position) {
        operation(() -> calculator.sin(radiansMode));
      }
      else if (COSINE.getPosition() == position) {
        operation(() -> calculator.cos(radiansMode));
      }
      else if (TANGENT.getPosition() == position) {
        operation(() -> calculator.tan(radiansMode));
      }
      else if (LOG.getPosition() == position) {
        operation(() -> calculator.log10());
      }
      else if (NATURAL_LOG.getPosition() == position) {
        operation(() -> calculator.naturalLog());
      }
      else if (SQUARED.getPosition() == position) {
        operation(() -> calculator.square());
      }
      else if (SQUARE_ROOT.getPosition() == position) {
        operation(() -> calculator.squareRoot());
      }
      else if (INVERSE.getPosition() == position) {
        operation(() -> calculator.oneOverX());
      }
      else if (FACTORIAL.getPosition() == position) {
        operation(() -> calculator.factorial());
      }

      // Binary Operations.
      else if (ADDITION.getPosition() == position) {
        operation(() -> calculator.add());
      }
      else if (SUBTRACT.getPosition() == position) {
        operation(() -> calculator.subtract());
      }
      else if (MULTIPLY.getPosition() == position) {
        operation(() -> calculator.multiply());
      }
      else if (DIVIDE.getPosition() == position) {
        operation(() -> calculator.divide());
      }
      else if (POWER_XTH_ROOT.getPosition() == position) {
        operation(() -> calculator.power());
      }
    }
    catch (IllegalStateException e) {
      Toast.makeText(CalculatorActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }
  }

  private interface Operation {
    void operate();
  }

  private void operation(Operation operation) {
    if (input.length() > 0) {
      pressEnter();
    }

    operation.operate();
    redrawStack(false);
  }

  private void pressEnter() {
    try {
      clearFlags();
      if (input.length() > 0) {
        if (hexMode) {
          calculator.enter(new BigDecimal(new BigInteger(input.toString(), 16)));
        }
        else {
          calculator.enter(new BigDecimal(input.toString()));
        }

        redrawStack(false);
        input.delete(0, input.length());
      }
      else {
        calculator.enter(calculator.getTop());
        redrawStack(false);
      }
    }
    catch (NumberFormatException e) {
      Toast.makeText(CalculatorActivity.this, "Invalid Number.", Toast.LENGTH_SHORT).show();
    }
  }

  private void enterDigit(char c) {
    if (!entryMode) {
      input.delete(0, input.length());
      entryMode = true;
    }

    input.append(c);
    registerX.setText(input);
    redrawStack(true);
  }

  private void redrawStack(boolean showInputLine) {
    ImmutableList<BigDecimal> stack = calculator.getStack();
    int offset = showInputLine ? 1 : 0;
    if (showInputLine) {
      registerX.setText(input);
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
    // TODO Limit to 15 chars.
    if (hexMode) {
      return "0x" + value.toBigInteger().toString(16);
    }

    return value.toString().replaceFirst("\\.0*$", "");
  }

  private void toggleRadians() {
    radiansMode = !radiansMode;
    // TODO: Change the button labels.
  }

  private void toggleHex() {
    // TODO: Change the button labels.
    hexMode = !hexMode;
    redrawStack(entryMode);
  }

  private void toggleShift() {
    shift = !shift;
    // TODO: Change the button labels.
  }

  private void clearFlags() {
    entryMode = false;
    decimalEntered = false;
    eexEntered = false;
  }
}
