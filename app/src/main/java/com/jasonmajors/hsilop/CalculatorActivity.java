package com.jasonmajors.hsilop;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.jasonmajors.hsilop.ButtonId.ADD;
import static com.jasonmajors.hsilop.ButtonId.BACKSPACE;
import static com.jasonmajors.hsilop.ButtonId.COS;
import static com.jasonmajors.hsilop.ButtonId.DECIMAL;
import static com.jasonmajors.hsilop.ButtonId.DEG;
import static com.jasonmajors.hsilop.ButtonId.DIVIDE;
import static com.jasonmajors.hsilop.ButtonId.DROP;
import static com.jasonmajors.hsilop.ButtonId.EEX;
import static com.jasonmajors.hsilop.ButtonId.EIGHT;
import static com.jasonmajors.hsilop.ButtonId.ENTER;
import static com.jasonmajors.hsilop.ButtonId.FACTORIAL;
import static com.jasonmajors.hsilop.ButtonId.FIVE;
import static com.jasonmajors.hsilop.ButtonId.FOUR;
import static com.jasonmajors.hsilop.ButtonId.HEX;
import static com.jasonmajors.hsilop.ButtonId.INVERSE;
import static com.jasonmajors.hsilop.ButtonId.LN;
import static com.jasonmajors.hsilop.ButtonId.LOG;
import static com.jasonmajors.hsilop.ButtonId.MULTIPLY;
import static com.jasonmajors.hsilop.ButtonId.NINE;
import static com.jasonmajors.hsilop.ButtonId.ONE;
import static com.jasonmajors.hsilop.ButtonId.ONE_OVER_X;
import static com.jasonmajors.hsilop.ButtonId.PI;
import static com.jasonmajors.hsilop.ButtonId.SECOND;
import static com.jasonmajors.hsilop.ButtonId.SEVEN;
import static com.jasonmajors.hsilop.ButtonId.SIN;
import static com.jasonmajors.hsilop.ButtonId.SIX;
import static com.jasonmajors.hsilop.ButtonId.SQRT;
import static com.jasonmajors.hsilop.ButtonId.SUBTRACT;
import static com.jasonmajors.hsilop.ButtonId.TAN;
import static com.jasonmajors.hsilop.ButtonId.THREE;
import static com.jasonmajors.hsilop.ButtonId.TWO;
import static com.jasonmajors.hsilop.ButtonId.UNDO;
import static com.jasonmajors.hsilop.ButtonId.X2;
import static com.jasonmajors.hsilop.ButtonId.Y_TO_THE_X;
import static com.jasonmajors.hsilop.ButtonId.ZERO;

// import static com.jasonmajors.hsilop.R.id.picture;

public class CalculatorActivity extends AppCompatActivity {
  public static final int DISPLAY_SIZE = 4;
  private final Calculator calculator = Calculator.getInstance();
  private boolean second = false;
  private StringBuilder stringBuilder;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_calculator);
    setStatusBarColor();

    GridView gridView = (GridView) findViewById(R.id.grid_view);
    final MyAdapter adapter = new MyAdapter(this);
    gridView.setAdapter(adapter);

    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        MyAdapter.Item item = (MyAdapter.Item) adapter.getItem(position);
        try {
          processClick(item);
        }
        catch (IllegalStateException e) {
          Toast.makeText(CalculatorActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
        finally {
          if (item.buttonId != SECOND) {
            second = false;
          }
        }
      }
    });
  }

  private void setStatusBarColor() {
    Window window = getWindow();
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark, null));
    }
    else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
    }
  }

  private void processClick(MyAdapter.Item item) {
    switch (item.buttonId) {
    case SECOND:
      second = !second;
      // TODO: Update Button values!!!!
      break;

    // Trig.
    case SIN:
      checkEntry();
      sin(second);
      updateStack();
      break;
    case COS:
      checkEntry();
      cos(second);
      updateStack();
      break;
    case TAN:
      checkEntry();
      tan(second);
      updateStack();
      break;

    // Exponents.
    case LOG:
      checkEntry();
      log(second);
      updateStack();
      break;
    case LN:
      checkEntry();
      ln(second);
      updateStack();
      break;
    case X2:
      checkEntry();
      calculator.square();
      updateStack();
      break;
    case SQRT:
      checkEntry();
      calculator.squareRoot();
      updateStack();
      break;
    case Y_TO_THE_X:
      checkEntry();
      calculator.power();
      updateStack();
      break;

    // Multiples.
    case ONE_OVER_X:
      checkEntry();
      calculator.oneOverX();
      updateStack();
      break;
    case FACTORIAL:
      checkEntry();
      calculator.factorial();
      updateStack();
      break;

    // Formats.
    case HEX:
      break;
    case DEG:
      break;

    // Entry.
    case PI:
      checkEntry();
      setInput(Math.PI);
      break;
    case ZERO:
      appendInput('0');
      break;
    case ONE:
      appendInput('1');
      break;
    case TWO:
      appendInput('2');
      break;
    case THREE:
      appendInput('3');
      break;
    case FOUR:
      appendInput('4');
      break;
    case FIVE:
      appendInput('5');
      break;
    case SIX:
      appendInput('6');
      break;
    case SEVEN:
      appendInput('7');
      break;
    case EIGHT:
      appendInput('8');
      break;
    case NINE:
      appendInput('9');
      break;
    case DECIMAL:
      decimal();
      break;
    case EEX:
      eex();
      break;
    case ENTER:
      checkEntry();
      enter(second);
      break;

    // Deletion.
    case BACKSPACE:
      backspace(second);
      break;
    case DROP:
      drop();
      updateStack();
      break;
    case UNDO:
      checkEntry();
      calculator.undo();
      break;

    // Arithmetic.
    case ADD:
      checkEntry();
      calculator.add();
      updateStack();
      break;
    case SUBTRACT:
      checkEntry();
      calculator.subtract();
      updateStack();
      break;
    case MULTIPLY:
      checkEntry();
      calculator.multiply();
      updateStack();
      break;
    case DIVIDE:
      checkEntry();
      calculator.divide();
      updateStack();
      break;
    case INVERSE:
      checkEntry();
      calculator.negate();
      updateStack();
      break;
    }
  }

  private void checkEntry() {
    if (stringBuilder != null) {
      calculator.enter(new BigDecimal(stringBuilder.toString()));
      stringBuilder = null;
    }
  }

  private void enter(boolean second) {
    if (second) {
      calculator.swap();
      updateStack();
    }
    else {
      updateStack();
    }
  }

  private void eex() {
    // TODO!!!!
  }

  private void decimal() {
    if (stringBuilder == null) {
      appendInput('0');
      appendInput('.');
    }
    else if (stringBuilder.toString().contains(".")) {
      throw new IllegalStateException("Repeat decimal.");
    }
    else {
      appendInput('.');
    }
  }

  private void drop() {
    if (stringBuilder == null) {
      calculator.dropOneValue();
    }
    else {
      stringBuilder = null;
    }
  }

  private void backspace(boolean second) {
    if (second) {
      stringBuilder = null;
      calculator.clearStack();
      updateStack();
    }
    else if (stringBuilder.length() == 0) {
      throw new IllegalStateException("Buffer is empty.");
    }
    else {
      stringBuilder.deleteCharAt(stringBuilder.length() - 1);
    }
  }

  private void appendInput(char c) {
    if (stringBuilder == null) {
      shiftUp();
      stringBuilder = new StringBuilder();
    }

    stringBuilder.append(c);
    setXBuffer(stringBuilder.toString());
  }

  private void shiftUp() {
    TextView stack2 = (TextView) findViewById(R.id.stack_2);
    TextView stack1 = (TextView) findViewById(R.id.stack_1);
    TextView stacky = (TextView) findViewById(R.id.stack_y);
    TextView stackx = (TextView) findViewById(R.id.stack_x);

    stack2.setText(stack1.getText());
    stack1.setText(stacky.getText());
    stacky.setText(stackx.getText());
  }

  private void setInput(double value) {
    stringBuilder = new StringBuilder(Double.toString(value));
    enter(false);
  }

  private void ln(boolean second) {
    if (second) {
      calculator.eToTheX();
    }
    else {
      calculator.naturalLog();
    }

    updateStack();
  }

  private void log(boolean second) {
    if (second) {
      calculator.tenToTheX();
    }
    else {
      calculator.log10();
    }

    updateStack();
  }

  private void sin(boolean second) {
    if (second) {
      calculator.asin();
    }
    else {
      calculator.sin();
    }

    updateStack();
  }

  private void cos(boolean second) {
    if (second) {
      calculator.acos();
    }
    else {
      calculator.cos();
    }

    updateStack();
  }

  private void tan(boolean second) {
    if (second) {
      calculator.atan();
    }
    else {
      calculator.tan();
    }

    updateStack();
  }

  private void setXBuffer(String s) {
    TextView x = (TextView) findViewById(R.id.stack_x);
    x.setText(s);
  }

  private void updateStack() {
    List<String> display = new ArrayList<>(DISPLAY_SIZE);
    if (stringBuilder != null) {
      display.add(stringBuilder.toString());
    }

    List<BigDecimal> stack = calculator.getStack();
    Iterator<BigDecimal> stackIterator = stack.iterator();
    while (display.size() < DISPLAY_SIZE && stackIterator.hasNext()) {
      display.add(stackIterator.next().toString());
    }

    while (display.size() < DISPLAY_SIZE) {
      display.add("");
    }

    TextView stackX = (TextView) findViewById(R.id.stack_x);
    TextView stackY = (TextView) findViewById(R.id.stack_y);
    TextView stack1 = (TextView) findViewById(R.id.stack_1);
    TextView stack2 = (TextView) findViewById(R.id.stack_2);

    stackX.setText(display.get(0).toString());
    stackY.setText(display.get(1).toString());
    stack1.setText(display.get(2).toString());
    stack2.setText(display.get(3).toString());
  }

  private class MyAdapter extends BaseAdapter {
    private List<Item> items = new ArrayList<>();
    private LayoutInflater inflater;

    public MyAdapter(Context context) {
      inflater = LayoutInflater.from(context);

      items.add(new Item("2nd", SECOND));
      items.add(new Item("sin", SIN));
      items.add(new Item("cos", COS));
      items.add(new Item("tan", TAN));
      items.add(new Item("Undo", UNDO));

      items.add(new Item("log", LOG));
      items.add(new Item("ln", LN));
      items.add(new Item("hex", HEX));
      items.add(new Item("deg", DEG));
      items.add(new Item("π", PI));

      items.add(new Item("x<sup><small>2</small></sup>", X2));
      items.add(new Item("√x", SQRT));
      items.add(new Item("<sup><small>1</small></sup>/<sub><small>x</small></sub>", ONE_OVER_X));
      items.add(new Item("x!", FACTORIAL));
      items.add(new Item("÷", DIVIDE));

      items.add(new Item("y<sup><small>x</small></sup>", Y_TO_THE_X));
      items.add(new Item("7", SEVEN));
      items.add(new Item("8", EIGHT));
      items.add(new Item("9", NINE));
      items.add(new Item("×", MULTIPLY));

      items.add(new Item("±", INVERSE));
      items.add(new Item("4", FOUR));
      items.add(new Item("5", FIVE));
      items.add(new Item("6", SIX));
      items.add(new Item("-", SUBTRACT));

      items.add(new Item("⌫", BACKSPACE));
      items.add(new Item("1", ONE));
      items.add(new Item("2", TWO));
      items.add(new Item("3", THREE));
      items.add(new Item("+", ADD));

      items.add(new Item("Drop", DROP));
      items.add(new Item("0", ZERO));
      items.add(new Item(".", DECIMAL));
      items.add(new Item("EEX", EEX));
      items.add(new Item("⏎", ENTER));
    }

    @Override
    public int getCount() {
      return items.size();
    }

    @Override
    public Object getItem(int i) {
      return items.get(i);
    }

    @Override
    public long getItemId(int position) {
      return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
      View v = view;
      TextView name;

      if (v == null) {
        v = inflater.inflate(R.layout.gridview_item, viewGroup, false);
        v.setTag(R.id.text, v.findViewById(R.id.text));
      }

      name = (TextView) v.getTag(R.id.text);
      Item item = (Item) getItem(i);
      name.setText(Html.fromHtml(item.name));
      return v;
    }

    private class Item {
      final String name;
      final ButtonId buttonId;

      Item(String name, ButtonId buttonId) {
        this.name = name;
        this.buttonId = buttonId;
      }
    }
  }
}
