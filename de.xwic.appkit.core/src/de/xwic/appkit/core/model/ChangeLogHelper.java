package de.xwic.appkit.core.model;


public class ChangeLogHelper {

	private Object origValue;
	private Object newValue;
	private String fieldNameKey;
	
	/**
	 * @return the origValue
	 */
	public Object getOrigValue() {
		return origValue;
	}
	
	/**
	 * @param origValue the origValue to set
	 */
	public void setOrigValue(Object origValue) {
		this.origValue = origValue;
	}
	
	/**
	 * @return the newValue
	 */
	public Object getNewValue() {
		return newValue;
	}
	
	/**
	 * @param newValue the newValue to set
	 */
	public void setNewValue(Object newValue) {
		this.newValue = newValue;
	}

	
	/**
	 * @return the fieldNameKey
	 */
	public String getFieldNameKey() {
		return fieldNameKey;
	}

	
	/**
	 * @param fieldNameKey the fieldNameKey to set
	 */
	public void setFieldNameKey(String fieldNameKey) {
		this.fieldNameKey = fieldNameKey;
	}
}
