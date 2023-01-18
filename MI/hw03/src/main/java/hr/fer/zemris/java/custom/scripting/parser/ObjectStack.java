package hr.fer.zemris.java.custom.scripting.parser;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;

/**
 * Class represents stack.
 * 
 * @author Ana Bagić
 *
 */
public class ObjectStack {

	/** {@link List} used to implement stack. */
	private List<Object> stack;
	
	/**
	 * Default constructor. Creates new stack.
	 */
	public ObjectStack() {
		stack = new ArrayList<>();
	}
	
	/**
	 * @return <code>true</code> if stack is empty, else <code>false</code>
	 */
	public boolean isEmpty() {
		return stack.isEmpty();
	}
	
	/**
	 * @return number of elements on the stack
	 */
	public int size() {
		return stack.size();
	}
	
	/**
	 * Adds new element to the stack.
	 * 
	 * @param value element to be added
	 */
	public void push(Object value) {
		stack.add(value);
	}
	
	
	/**
	 * Removes last element from the stack and returns it.
	 * 
	 * @return element removed from the stack
	 * @throws EmptyStackException if stack is empty
	 */
	public Object pop() {
		Object last = peek();
		stack.remove(stack.size()-1);
		return last;
	}
	
	/**
	 * Returns element on the top of the stack.
	 * 
	 * @return element on the top of the stack
	 * @throws EmptyStackException if stack is empty
	 */
	public Object peek() {
		if(stack.isEmpty())
			throw new EmptyStackException();

		Object last = stack.get(stack.size()-1);
		return last;
	}
	
	/**
	 * Removes all the elements from the stack.
	 */
	public void clear() {
		stack.clear();
	}
}
