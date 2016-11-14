package com.jasonmajors.hsilop;

import android.widget.ImageView;

class CalculatorButton {
  enum ImageState {
    BASIC,
    SHIFT,
    HEX
  }

  interface ButtonAction {
    void operate();
  }

  private final String name;
  private final int basicImageId;
  private final int shiftedImageId;
  private final int hexModeImageId;
  private final ButtonAction action;
  private ImageView imageView;

  CalculatorButton(String name, int basicImageId, int shiftedImageId, int hexModeImageId, ButtonAction action) {
    this.name = name;
    this.basicImageId = basicImageId;
    this.shiftedImageId = shiftedImageId;
    this.hexModeImageId = hexModeImageId;
    this.action = action;
  }

  void press() {
    action.operate();
  }

  String getName() {
    return name;
  }

  int getBasicImageId() {
    return basicImageId;
  }

  void setImageView(ImageView imageView) {
    this.imageView = imageView;
  }

  void updateImage(int imageId) {
    imageView.setImageResource(imageId);
  }

  void updateImageView(ImageState state) {
    if (state == ImageState.BASIC) {
      imageView.setImageResource(basicImageId);
    }
    else if (state == ImageState.SHIFT && shiftedImageId != 0) {
      imageView.setImageResource(shiftedImageId);
    }
    else if (state == ImageState.HEX && hexModeImageId != 0) {
      imageView.setImageResource(hexModeImageId);
    }
  }

  @Override
  public String toString() {
    return name;
  }
}
