package hr.fer.zemris.java.custom.scripting.exec;

import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Map;

import hr.fer.zemris.java.custom.scripting.exec.helper.ValueWrapper;

/**
 * Class models structure that acts as a map. For String key it holds stack of values of any type.
 * 
 * @author Ana Bagić
 *
 */
public class ObjectMultistack {
	
	/** Structure used to map keys to stack of values. */
	private Map<String, MultistackEntry> map = new HashMap<>();

	/**
	 * Pushes on multistack valueWrapper for keyName.
	 * 
	 * @param keyName for value
	 * @param valueWrapper to push to stack
	 */
	public void push(String keyName, ValueWrapper valueWrapper) {
		MultistackEntry head = map.get(keyName);
		map.put(keyName, new MultistackEntry(valueWrapper, head));
	}
	
	/**
	 * Returns last value for keyName, and removes it from the multistack.
	 * 
	 * @param keyName for value
	 * @return last value pushed on multistack for keyName
	 * @throws EmptyStackException if there aren't any more values for keyName
	 */
	public ValueWrapper pop(String keyName) {
		MultistackEntry head = map.get(keyName);
		if(head == null) throw new EmptyStackException();
		
		if(head.previous == null) {
			map.remove(keyName);
		} else {
			map.put(keyName, head.previous);
		}
		
		return head.value;
	}
	
	/**
	 * Returns last value for keyName, but doesn't remove it from the multistack.
	 * 
	 * @param keyName for value
	 * @return last value pushed on multistack for keyName
	 * @throws EmptyStackException if there aren't any more values for keyName
	 */
	public ValueWrapper peek(String keyName) {
		MultistackEntry head = map.get(keyName);
		if(head == null) throw new EmptyStackException();
		return head.value;
	}
	
	/**
	 * Check if this multistack has any values for given key name.
	 * 
	 * @param keyName for values
	 * @return <code>true</code> if multistack doesn't have values for given key, otherwise <code>false</code>
	 */
	public boolean isEmpty(String keyName) {
		return map.get(keyName) == null;
	}
	
	/**
	 * Helper class used to model node of multistack.
	 * 
	 * @author Ana Bagić
	 *
	 */
	private static class MultistackEntry {
		
		/** Value of the node. */
		ValueWrapper value;
		/** Reference to previous node. */
		MultistackEntry previous;
		
		/**
		 * Constructor creates new node using given value and previous node.
		 * 
		 * @param value value of the node
		 * @param previous previous node in the stack
		 */
		MultistackEntry(ValueWrapper value, MultistackEntry previous) {
			this.value = value;
			this.previous = previous;
		}
	}

}
