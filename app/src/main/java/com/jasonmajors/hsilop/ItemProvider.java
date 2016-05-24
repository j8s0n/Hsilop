package com.jasonmajors.hsilop;

import java.util.Iterator;

/**
 * Created by jason on 2016-05-22.
 */

class ItemProvider implements Iterable {
  @Override
  public ItemIterator iterator() {
    return null;
  }

  class ItemIterator implements Iterator {

    @Override
    public boolean hasNext() {
      return false;
    }

    @Override
    public Object next() {
      return null;
    }

    @Override
    public void remove() {

    }
  }
}
