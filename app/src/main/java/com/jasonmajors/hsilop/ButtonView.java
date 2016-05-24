package com.jasonmajors.hsilop;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

class ButtonView extends ImageView {
  public ButtonView(Context context) {
    super(context);
  }

  public ButtonView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public ButtonView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
  }
}
