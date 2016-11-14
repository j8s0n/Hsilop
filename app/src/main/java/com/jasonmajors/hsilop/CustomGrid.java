package com.jasonmajors.hsilop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

class CustomGrid extends BaseAdapter {
  private Context context;
  private final List<CalculatorButton> buttons;

  CustomGrid(Context c, List<CalculatorButton> buttons) {
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
    View grid;
    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    if (convertView == null) {
      grid = inflater.inflate(R.layout.grid_single, null);

      ImageView imageView = (ImageView) grid.findViewById(R.id.grid_image);
      imageView.setImageResource(buttons.get(position).getBasicImageId());
      buttons.get(position).setImageView(imageView);

      TextView textView = (TextView) grid.findViewById(R.id.grid_text);
      textView.setText(buttons.get(position).getName());
    }
    else {
      grid = convertView;
    }

    return grid;
  }
}
