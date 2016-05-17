package com.jasonmajors.hsilop;

import com.google.common.collect.ImmutableList;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.math.BigDecimal;
import java.math.MathContext;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class CalculatorTest {
  public static final double DELTA = 0.00001;
  private Calculator calculator;
  private final BigDecimal x = BigDecimal.valueOf(4.3);
  private final BigDecimal y = BigDecimal.valueOf(6.2);
  private final BigDecimal four = BigDecimal.valueOf(4);
  private final BigDecimal fourFactorial = BigDecimal.valueOf(4 * 3 * 2);
  private final BigDecimal negX = x.negate();
  private final BigDecimal xFactorial = BigDecimal.valueOf(38.07797645);
  private final BigDecimal oneHalf = BigDecimal.valueOf(0.5);
  private final BigDecimal xthRootOfY = BigDecimal.valueOf(1.5285411577);

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Before
  public void setup() {
    calculator = new Calculator();
  }

  @Test
  public void enter() {
    calculator.enter(y);
    assertEquals(1, calculator.getStack().size());
    calculator.enter(x);
    assertEquals(2, calculator.getStack().size());
    calculator.enter(x);
    assertEquals(3, calculator.getStack().size());
  }

  @Test
  public void add() {
    calculator.enter(y);
    calculator.enter(x);
    calculator.add();
    assertEquals(x.add(y), calculator.getTop());
    assertEquals(1, calculator.getStack().size());
  }

  @Test
  public void subtract() {
    calculator.enter(y);
    calculator.enter(x);
    calculator.subtract();
    assertEquals(y.subtract(x), calculator.getTop());
    assertEquals(1, calculator.getStack().size());
  }

  @Test
  public void multiply() {
    calculator.enter(y);
    calculator.enter(x);
    calculator.multiply();
    assertEquals(y.multiply(x), calculator.getTop());
    assertEquals(1, calculator.getStack().size());
  }

  @Test
  public void divide() {
    calculator.enter(y);
    calculator.enter(x);
    calculator.divide();
    assertEquals(y.divide(x, MathContext.DECIMAL128), calculator.getTop());
    assertEquals(1, calculator.getStack().size());
  }

  @Test
  public void divideByZero() {
    calculator.enter(x);
    calculator.enter(BigDecimal.ZERO);
    try {
      calculator.divide();
      fail();
    }
    catch (IllegalStateException e) {
      assertEquals(2, calculator.getStack().size());
      assertEquals(BigDecimal.ZERO, calculator.getTop());
      assertEquals(x, calculator.getStack().get(1));
    }
  }

  @Test
  public void power() {
    calculator.enter(y);
    calculator.enter(x);
    calculator.power();
    assertEquals(Math.pow(y.doubleValue(), x.doubleValue()), calculator.getTop().doubleValue(), DELTA);
    assertEquals(1, calculator.getStack().size());
  }

  @Test
  public void square() {
    calculator.enter(x);
    calculator.square();
    assertEquals(x.multiply(x), calculator.getTop());
    assertEquals(1, calculator.getStack().size());
  }

  @Test
  public void squareRoot() {
    calculator.enter(x);
    calculator.squareRoot();
    assertEquals(Math.sqrt(x.doubleValue()), calculator.getTop().doubleValue(), DELTA);
  }

  @Test
  public void squareRootOfANegative() {
    calculator.enter(negX);
    try {
      calculator.squareRoot();
      fail();
    }
    catch (IllegalStateException e) {
      assertEquals(negX, calculator.getTop());
    }
  }

  @Test
  public void naturalLog() {
    calculator.enter(x);
    calculator.naturalLog();
    assertEquals(Math.log(x.doubleValue()), calculator.getTop().doubleValue(), DELTA);
  }

  @Test
  public void naturalLogOfANegative() {
    calculator.enter(negX);
    try {
      calculator.naturalLog();
      fail();
    }
    catch (IllegalStateException e) {
      assertEquals(negX, calculator.getTop());
    }
  }

  @Test
  public void eToTheX() {
    calculator.enter(x);
    calculator.eToTheX();
    assertEquals(Math.exp(x.doubleValue()), calculator.getTop().doubleValue(), DELTA);
  }

  @Test
  public void eToTheNegativeX() {
    calculator.enter(negX);
    calculator.eToTheX();
    assertEquals(Math.exp(negX.doubleValue()), calculator.getTop().doubleValue(), DELTA);
  }

  @Test
  public void log10() {
    calculator.enter(x);
    calculator.log10();
    assertEquals(Math.log10(x.doubleValue()), calculator.getTop().doubleValue(), DELTA);
  }

  @Test
  public void log10OfANegative() {
    calculator.enter(negX);
    try {
      calculator.log10();
      fail();
    }
    catch (IllegalStateException e) {
      assertEquals(negX, calculator.getTop());
    }
  }

  @Test
  public void tenToTheX() {
    calculator.enter(x);
    calculator.tenToTheX();
    assertEquals(Math.pow(10, x.doubleValue()), calculator.getTop().doubleValue(), DELTA);
  }

  @Test
  public void tenToTheNegativeX() {
    calculator.enter(negX);
    calculator.tenToTheX();
    assertEquals(Math.pow(10, negX.doubleValue()), calculator.getTop().doubleValue(), DELTA);
  }

  @Test
  public void sin() {
    calculator.enter(x);
    calculator.sin();
    assertEquals(Math.sin(x.doubleValue()), calculator.getTop().doubleValue(), DELTA);
  }

  @Test
  public void cos() {
    calculator.enter(x);
    calculator.cos();
    assertEquals(Math.cos(x.doubleValue()), calculator.getTop().doubleValue(), DELTA);
  }

  @Test
  public void tan() {
    calculator.enter(x);
    calculator.tan();
    assertEquals(Math.tan(x.doubleValue()), calculator.getTop().doubleValue(), DELTA);
  }

  @Test
  public void asin() {
    calculator.enter(oneHalf);
    calculator.asin();
    assertEquals(Math.asin(oneHalf.doubleValue()), calculator.getTop().doubleValue(), DELTA);
  }

  @Test
  public void asinOutOfRange() {
    calculator.enter(x);
    try {
      calculator.asin();
      fail();
    }
    catch (IllegalStateException e) {
      assertEquals(x, calculator.getTop());
    }
  }

  @Test
  public void acos() {
    calculator.enter(oneHalf);
    calculator.acos();
    assertEquals(Math.acos(oneHalf.doubleValue()), calculator.getTop().doubleValue(), DELTA);
  }

  @Test
  public void acosOutOfRange() {
    calculator.enter(x);
    try {
      calculator.acos();
      fail();
    }
    catch (IllegalStateException e) {
      assertEquals(x, calculator.getTop());
    }
  }

  @Test
  public void atan() {
    calculator.enter(oneHalf);
    calculator.atan();
    assertEquals(Math.atan(oneHalf.doubleValue()), calculator.getTop().doubleValue(), DELTA);
  }

  @Test
  public void oneOverX() {
    calculator.enter(x);
    calculator.oneOverX();
    assertEquals(BigDecimal.ONE.divide(x, MathContext.DECIMAL128), calculator.getTop());
  }

  @Test
  public void oneOverZero() {
    calculator.enter(BigDecimal.ZERO);
    try {
      calculator.oneOverX();
      fail();
    }
    catch (IllegalStateException e) {
      assertEquals(BigDecimal.ZERO, calculator.getTop());
    }
  }

  @Test
  public void integerFactorial() {
    calculator.enter(four);
    calculator.factorial();
    assertEquals(fourFactorial, calculator.getTop());
  }

  @Test
  public void zeroFactorial() {
    calculator.enter(BigDecimal.ZERO);
    calculator.factorial();
    assertEquals(BigDecimal.ONE, calculator.getTop());
  }

  @Test
  public void fractionFactorial() {
    calculator.enter(x);
    calculator.factorial();
    assertEquals(xFactorial.doubleValue(), calculator.getTop().doubleValue(), DELTA);
  }

  @Test
  public void xthRootOfY() {
    calculator.enter(y);
    calculator.enter(x);
    calculator.xthRootOfY();
    assertEquals(xthRootOfY.doubleValue(), calculator.getTop().doubleValue(), DELTA);
  }

  @Test
  public void xthRootOfANegative() {
    calculator.enter(negX);
    calculator.enter(x);

    try {
      calculator.xthRootOfY();
      fail();
    }
    catch (IllegalStateException e) {
      assertEquals(x, calculator.getTop());
      assertEquals(2, calculator.getStack().size());
    }
  }

  @Test
  public void negate() {
    calculator.enter(x);
    calculator.negate();
    assertEquals(negX, calculator.getTop());
  }

  @Test
  public void dropOneValue() {
    calculator.enter(x);
    calculator.enter(y);
    assertEquals(2, calculator.getStack().size());
    assertEquals(y, calculator.getTop());
    calculator.dropOneValue();
    assertEquals(1, calculator.getStack().size());
    assertEquals(x, calculator.getTop());
  }

  @Test
  public void dropValueOnEmptyStack() {
    thrown.expect(IllegalStateException.class);
    calculator.dropOneValue();
  }

  @Test
  public void clearStack() {
    calculator.enter(x);
    calculator.enter(y);
    assertEquals(2, calculator.getStack().size());
    calculator.clearStack();
    assertEquals(0, calculator.getStack().size());
  }

  @Test
  public void undo() {
    calculator.enter(y);
    calculator.enter(x);
    calculator.add();
    assertEquals(x.add(y), calculator.getTop());
    assertEquals(1, calculator.getStack().size());

    calculator.undo();
    ImmutableList<BigDecimal> stack = calculator.getStack();
    assertEquals(2, stack.size());
    assertEquals(x, stack.get(0));
    assertEquals(y, stack.get(1));
  }
}