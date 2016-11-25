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

  private final int basicId;
  private final int shiftedId;
  private final int hexModeId;
  private final ButtonAction action;
  private final ResourceProvider resourceProvider;

  private TextView textView;

  Button(int basicId, int shiftedId, int hexModeId, ButtonAction action, ResourceProvider resourceProvider) {
    this.basicId = basicId;
    this.shiftedId = shiftedId;
    this.hexModeId = hexModeId;
    this.action = action;
    this.resourceProvider = resourceProvider;
  }

  void press() {
    action.operate();
  }

  void setTextView(TextView textView) {
    this.textView = textView;
  }

  void updateText(int textId) {
    textView.setText(resourceProvider.getStringById(textId));
  }

  void updateTextView(ButtonState state) {
    if (state == ButtonState.BASIC) {
      textView.setText(resourceProvider.getStringById(basicId));
    }
    else if (state == ButtonState.SHIFT && shiftedId != 0) {
      textView.setText(resourceProvider.getStringById(shiftedId));
    }
    else if (state == ButtonState.HEX && hexModeId != 0) {
      textView.setText(resourceProvider.getStringById(hexModeId));
    }
  }

  @Override
  public String toString() {
    return resourceProvider.getStringById(basicId).toString();
  }
}
