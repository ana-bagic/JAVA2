package hr.fer.zemris.java.custom.scripting.exec.helper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

/**
 * Class tests functionality of class {@link ValueWrapper} and its functions.
 * 
 * @author Ana Bagić
 *
 */
public class ValueWrapperTester {

	@Test
	public void testTwoNullAdd() {
		ValueWrapper v1 = new ValueWrapper(null);
		ValueWrapper v2 = new ValueWrapper(null);
		v1.add(v2.getValue());
		assertEquals(v1.getValue(), 0);
		assertEquals(v1.getValue().getClass(), Integer.class);
		assertEquals(v2.getValue(), null);
	}
	
	@Test
	public void testDoubleIntAdd() {
		ValueWrapper v1 = new ValueWrapper("1.2E1");
		ValueWrapper v2 = new ValueWrapper(Integer.valueOf(1));
		v1.add(v2.getValue());
		assertEquals(v1.getValue(), 13.0);
		assertEquals(v1.getValue().getClass(), Double.class);
		assertEquals(v2.getValue(), 1);
		assertEquals(v2.getValue().getClass(), Integer.class);
	}
	
	@Test
	public void testTwoIntAdd() {
		ValueWrapper v1 = new ValueWrapper("12");
		ValueWrapper v2 = new ValueWrapper(Integer.valueOf(1));
		v1.add(v2.getValue());
		assertEquals(v1.getValue(), 13);
		assertEquals(v1.getValue().getClass(), Integer.class);
		assertEquals(v2.getValue(), 1);
		assertEquals(v2.getValue().getClass(), Integer.class);
	}
	
	@Test
	public void testStringThrows() {
		ValueWrapper v1 = new ValueWrapper("Ankica");
		ValueWrapper v2 = new ValueWrapper(Integer.valueOf(1));
		assertThrows(RuntimeException.class, () -> v1.add(v2.getValue()));
		assertThrows(RuntimeException.class, () -> v2.add(v1.getValue()));
	}
	
	@Test
	public void testOtherClassesThrows() {
		ValueWrapper v1 = new ValueWrapper(Integer.valueOf(6));
		ValueWrapper v2 = new ValueWrapper(new ArrayList<>());
		assertThrows(RuntimeException.class, () -> v1.add(v2.getValue()));
		assertThrows(RuntimeException.class, () -> v2.add(v1.getValue()));
	}
	
	@Test
	public void testTwoNullSub() {
		ValueWrapper v1 = new ValueWrapper(null);
		ValueWrapper v2 = new ValueWrapper(null);
		v1.subtract(v2.getValue());
		assertEquals(v1.getValue(), 0);
		assertEquals(v1.getValue().getClass(), Integer.class);
		assertEquals(v2.getValue(), null);
	}
	
	@Test
	public void testDoubleIntSub() {
		ValueWrapper v1 = new ValueWrapper("1.2E1");
		ValueWrapper v2 = new ValueWrapper(Integer.valueOf(1));
		v1.subtract(v2.getValue());
		assertEquals(v1.getValue(), 11.0);
		assertEquals(v1.getValue().getClass(), Double.class);
		assertEquals(v2.getValue(), 1);
		assertEquals(v2.getValue().getClass(), Integer.class);
	}
	
	@Test
	public void testTwoIntSub() {
		ValueWrapper v1 = new ValueWrapper("12");
		ValueWrapper v2 = new ValueWrapper(Integer.valueOf(1));
		v1.subtract(v2.getValue());
		assertEquals(v1.getValue(), 11);
		assertEquals(v1.getValue().getClass(), Integer.class);
		assertEquals(v2.getValue(), 1);
		assertEquals(v2.getValue().getClass(), Integer.class);
	}
	
	@Test
	public void testTwoNullMul() {
		ValueWrapper v1 = new ValueWrapper(null);
		ValueWrapper v2 = new ValueWrapper(null);
		v1.multiply(v2.getValue());
		assertEquals(v1.getValue(), 0);
		assertEquals(v1.getValue().getClass(), Integer.class);
		assertEquals(v2.getValue(), null);
	}
	
	@Test
	public void testDoubleIntMul() {
		ValueWrapper v1 = new ValueWrapper("1.2E1");
		ValueWrapper v2 = new ValueWrapper(Integer.valueOf(1));
		v1.multiply(v2.getValue());
		assertEquals(v1.getValue(), 12.0);
		assertEquals(v1.getValue().getClass(), Double.class);
		assertEquals(v2.getValue(), 1);
		assertEquals(v2.getValue().getClass(), Integer.class);
	}
	
	@Test
	public void testTwoIntMul() {
		ValueWrapper v1 = new ValueWrapper("12");
		ValueWrapper v2 = new ValueWrapper(Integer.valueOf(1));
		v1.multiply(v2.getValue());
		assertEquals(v1.getValue(), 12);
		assertEquals(v1.getValue().getClass(), Integer.class);
		assertEquals(v2.getValue(), 1);
		assertEquals(v2.getValue().getClass(), Integer.class);
	}
	
	@Test
	public void testTwoNullDiv() {
		ValueWrapper v1 = new ValueWrapper(null);
		ValueWrapper v2 = new ValueWrapper(null);
		assertThrows(ArithmeticException.class, () -> v1.divide(v2.getValue()));
	}
	
	@Test
	public void testDoubleIntDiv() {
		ValueWrapper v1 = new ValueWrapper("1.2E1");
		ValueWrapper v2 = new ValueWrapper(Integer.valueOf(2));
		v1.divide(v2.getValue());
		assertEquals(v1.getValue(), 6.0);
		assertEquals(v1.getValue().getClass(), Double.class);
		assertEquals(v2.getValue(), 2);
		assertEquals(v2.getValue().getClass(), Integer.class);
	}
	
	@Test
	public void testTwoIntDiv() {
		ValueWrapper v1 = new ValueWrapper("12");
		ValueWrapper v2 = new ValueWrapper(Integer.valueOf(2));
		v1.divide(v2.getValue());
		assertEquals(v1.getValue(), 6);
		assertEquals(v1.getValue().getClass(), Integer.class);
		assertEquals(v2.getValue(), 2);
		assertEquals(v2.getValue().getClass(), Integer.class);
	}
	
	@Test
	public void testTwoNullCompare() {
		ValueWrapper v1 = new ValueWrapper(null);
		ValueWrapper v2 = new ValueWrapper(null);
		assertEquals(v1.numCompare(v2.getValue()), 0);
		assertEquals(v1.getValue(), null);
		assertEquals(v2.getValue(), null);
	}
	
	@Test
	public void testDoubleIntCompare() {
		ValueWrapper v1 = new ValueWrapper("1.2E1");
		ValueWrapper v2 = new ValueWrapper(Integer.valueOf(1));
		assertEquals(v1.numCompare(v2.getValue()), 1);
		assertEquals(v1.getValue(), "1.2E1");
		assertEquals(v1.getValue().getClass(), String.class);
		assertEquals(v2.getValue(), 1);
		assertEquals(v2.getValue().getClass(), Integer.class);
	}
	
	@Test
	public void testTwoIntCompare() {
		ValueWrapper v1 = new ValueWrapper("-12");
		ValueWrapper v2 = new ValueWrapper(Integer.valueOf(1));
		assertEquals(v1.numCompare(v2.getValue()), -1);
		assertEquals(v1.getValue(), "-12");
		assertEquals(v1.getValue().getClass(), String.class);
		assertEquals(v2.getValue(), 1);
		assertEquals(v2.getValue().getClass(), Integer.class);
	}
}
