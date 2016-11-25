package com.jasonmajors.hsilop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

class CustomGrid extends BaseAdapter {
  private Context context;
  private final List<Button> buttons;

  CustomGrid(Context c, List<Button> buttons) {
    context = c;
    this.buttons = buttons;
  }

  @Override
  public int getCount() {
    return buttons.size();
  }

  @Override
  public Object getItem(int position) {
    return null;
  }

  @Override
  public long getItemId(int position) {
    return 0;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    View gridItem;
    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    if (convertView == null) {
      gridItem = inflater.inflate(R.layout.grid_single, null);
      gridItem.setMinimumHeight(CalculatorActivity.getButtonRowHeight());
      TextView textView = (TextView) gridItem.findViewById(R.id.grid_text);
      buttons.get(position).setTextView(textView);
      buttons.get(position).updateTextView(Button.ButtonState.BASIC);
    }
    else {
      gridItem = convertView;
    }

    return gridItem;
  }
}
