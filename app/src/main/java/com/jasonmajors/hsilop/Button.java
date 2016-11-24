package com.jasonmajors.hsilop;

import android.widget.TextView;

class Button {
  enum ButtonState {
    BASIC,
    SHIFT,
    HEX
  }

  interface ButtonAction {
    void operate();
  }

  private final String name;
  private final int basicTextId;
  private final int shiftedTextId;
  private final int hexModeTextId;
  private final ButtonAction action;
  private final StringResourceProvider stringProvider;

  //private ImageView imageView;
  private TextView textView;

  Button(String name, int basicTextId, int shiftedTextId, int hexModeTextId, ButtonAction action,
         StringResourceProvider stringProvider) {
    this.name = name;
    this.basicTextId = basicTextId;
    this.shiftedTextId = shiftedTextId;
    this.hexModeTextId = hexModeTextId;
    this.action = action;
    this.stringProvider = stringProvider;
  }

  void press() {
    action.operate();
  }

  String getName() {
    return name;
  }

  int getBasicTextId() {
    return basicTextId;
  }

  public void setTextView(TextView textView) {
    this.textView = textView;
  }

  void updateText(int textId) {
    textView.setText(stringProvider.getStringById(textId));
  }

  void updateTextView(ButtonState state) {
    if (state == ButtonState.BASIC) {
      textView.setText(stringProvider.getStringById(basicTextId));
    }
    else if (state == ButtonState.SHIFT && shiftedTextId != 0) {
      textView.setText(stringProvider.getStringById(shiftedTextId));
    }
    else if (state == ButtonState.HEX && hexModeTextId != 0) {
      textView.setText(stringProvider.getStringById(hexModeTextId));
    }
  }

  @Override
  public String toString() {
    return name;
  }
}
