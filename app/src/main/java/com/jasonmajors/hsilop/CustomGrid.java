package com.jasonmajors.hsilop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomGrid extends BaseAdapter {
  private Context context;
  private final ButtonName[] buttonNames;
  private final int imageId = R.drawable.dummy_icon;

  public CustomGrid(Context c, ButtonName[] buttonNames) {
    context = c;
    this.buttonNames = buttonNames;
  }

  @Override
  public int getCount() {
    return buttonNames.length;
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
      imageView.setImageResource(imageId);

      TextView textView = (TextView) grid.findViewById(R.id.grid_text);
      textView.setText(buttonNames[position].getLabel());
    }
    else {
      grid = convertView;
    }

    return grid;
  }
}
